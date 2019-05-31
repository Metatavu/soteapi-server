package fi.metatavu.soteapi.exreport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Parser for Exreport wait times report
 * 
 * @author Antti Leppä
 */
public class ExreportExcelParser {
  
  private static final int DATA_ROW_INDEX = 3;
  private static final int MAX_DATA_ROWS = 100;
  private static final String SUMMARY_CELL_VALUE = "Yhteensä";
  private static final String RULES_CELL_VALUE = "Ehdot";
  private static final Map<String, String> nameMap;
  
  /**
   * Parses Exreport wait times report file
   * @param file file
   * @return parsed wait times
   * @throws ExreportParseException thrown when parsing fails
   */
  public WaitTimes parseExcel(File file) throws ExreportParseException {
    WaitTimes result = new WaitTimes();
    
    List<ServiceWaitTimes> services = new ArrayList<>();
    
    try (Workbook workbook = WorkbookFactory.create(file)) {
      if (workbook.getNumberOfSheets() < 1) {
        throw new ExreportParseException("Could not find any sheets");
      }
      
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        Sheet sheet = workbook.getSheetAt(i);
        if (sheet == null) {
          throw new ExreportParseException(String.format("Could not find data sheet %d", i));
        }
        
        services.add(parseServiceWaitTimes(sheet));
      }
      
      result.setServices(services);
      
      return result;
    } catch (EncryptedDocumentException | IOException e) {
      throw new ExreportParseException(e);
    }
  }
  
  /**
   * Parses a service wait times sheet
   * 
   * @param sheet sheet
   * @return parsed sheet
   * @throws ExreportParseException thrown when parsing fails
   */
  private ServiceWaitTimes parseServiceWaitTimes(Sheet sheet) throws ExreportParseException {
    ServiceWaitTimes result = new ServiceWaitTimes();
    List<ServiceUnitWaitTime> waitTimes = new ArrayList<>();
    Map<String, String> rules = new HashMap<>();
    
    ParseStatus parseStatus = ParseStatus.WAIT_TIMES;
    int rowIndex = DATA_ROW_INDEX;
    while (parseStatus != ParseStatus.DONE) {
      Row row = sheet.getRow(rowIndex);
      if (row == null) {
        if (parseStatus == ParseStatus.RULES) {
          parseStatus = ParseStatus.DONE;
          break;
        } else {
          rowIndex++;
        }
        
        continue;
      }
      
      String firstValue = getCellAsString(row, 0);
      
      switch (parseStatus) {
        case FINDING_RULES:
          if (StringUtils.contains(firstValue, RULES_CELL_VALUE)) {
            parseStatus = ParseStatus.RULES;
          }
        break;
        case RULES:
          if (StringUtils.isBlank(firstValue)) {
            parseStatus = ParseStatus.DONE;
          } else {
            parseRule(rules, firstValue);
          }
        break;
        case WAIT_TIMES:
          if (StringUtils.contains(firstValue, SUMMARY_CELL_VALUE)) {
            parseStatus = ParseStatus.FINDING_RULES;
          } else {  
            Cell waitTimeCell = row.getCell(1);
            if (waitTimeCell == null) {
              throw new ExreportParseException(String.format("Could not row %d wait time cell", rowIndex));
            }
            
            if (waitTimeCell.getCellType() != CellType.NUMERIC) {
              throw new ExreportParseException(String.format("Row %d name cell is not numeric", rowIndex));
            }
            
            double waitTime = waitTimeCell.getNumericCellValue();
            
            ServiceUnitWaitTime serviceUnitWaitTime = new ServiceUnitWaitTime();
            serviceUnitWaitTime.setUnitName(firstValue);
            serviceUnitWaitTime.setWaitTime(waitTime);
            waitTimes.add(serviceUnitWaitTime);
          }
        break;
        default:
          throw new ExreportParseException("Invalid state");
      }

      rowIndex++;
      
      if (rowIndex >= MAX_DATA_ROWS) {
        throw new ExreportParseException("Max data rows exceeded");
      } 
    }
    
    String serviceId = String.format("%s-%s", rules.get("toiminto"), rules.get("vastaanottolaji"));
    String serviceName = nameMap.get(serviceId);
    
    result.setServiceId(DigestUtils.md5Hex(serviceId));
    result.setServiceName(serviceName);
    result.setWaitTimes(waitTimes);
    
    return result;
  }
  
  /**
   * Parses rule string
   * 
   * @param rules rules map
   * @param value rule value
   */
  private void parseRule(Map<String, String> rules, String value) {
    String[] parts = value.split(" on ", 2);
    if (parts.length == 2) {
      rules.put(parts[0], parts[1]);
    }
  }

  /**
   * Returns cell value as string 
   * 
   * @param row row object
   * @param cellIndex cell index
   * @return cell value or null
   * @throws ExreportParseException thrown when cell type is not a string
   */
  private String getCellAsString(Row row, int cellIndex) throws ExreportParseException {
    Cell cell = row.getCell(cellIndex);
    if (cell == null) {
      return null;
    }
    
    if (cell.getCellType() != CellType.STRING) {
      throw new ExreportParseException("Cell is not string");
    }
    
    return cell.getStringCellValue();
  }
  
  /**
   * Enum describing parse status 
   */
  private enum ParseStatus {
    
    WAIT_TIMES,
    
    FINDING_RULES,
    
    RULES,
    
    DONE
    
  }
  
  static {
    nameMap = new HashMap<>();
    nameMap.put("Avosairaanhoito,-Lääkäri vo, ja", "Hoitoonpääsy lääkärin vastaanotolle");
    nameMap.put("Avosairaanhoito,-Hoitaja vo, ja", "Hoitoonpääsy hoitajan vastaanotolle");
    nameMap.put("Suun terveydenhuolto,-Suuhygienisti vo, ja", "Hoitoonpääsy suuhygienistin vastaanotolle");
    nameMap.put("Suun terveydenhuolto,-Hammaslääkäri vo tai Tutkimus, ja", "Hoitoonpääsy hammaslääkärin vastaanotolle");
  }

}
