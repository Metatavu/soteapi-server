package fi.metatavu.soteapi.review;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.server.rest.model.ReviewProduct;

/**
 * Controller for review products
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class ReviewProductController {

  /**
   * Gets a list of review products
   * 
   * @return list of review products
   * 
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  public List<ReviewProduct> listReviewProducts() throws JsonParseException, JsonMappingException, IOException {
    return getReviewProductList();
  }

  /**
   * Finds single review product by id
   * 
   * @param id review product id
   * @return review product by id or null if not found
   * 
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public ReviewProduct findReviewProductById(long id) throws JsonParseException, JsonMappingException, IOException {
    List<ReviewProduct> reviewProducts = listReviewProducts();
    return reviewProducts.stream()
      .filter(reviewProduct -> id == reviewProduct.getId())
      .findAny()
      .orElse(null);
  }

  /**
   * Reads the list of review products from file
   * 
   * @return list of review products
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private List<ReviewProduct> getReviewProductList() throws JsonParseException, JsonMappingException, IOException {
    String reviewProductsFilePath = System.getProperty("review.products.file");
    if (StringUtils.isBlank(reviewProductsFilePath)) {
      return Collections.emptyList();
    }

    File reviewProductsFile = new File(reviewProductsFilePath);
    if (!reviewProductsFile.exists()) {
      return Collections.emptyList();
    }

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(reviewProductsFile, new TypeReference<List<ReviewProduct>>(){});
  }

}
