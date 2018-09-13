package fi.metatavu.soteapi.rest.translate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.server.rest.model.EmergencyCongestionStatus;

/**
 * Translator for emergency congestion status related translations
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class EmergencyCongestionStatusTranslator extends AbstractTranslator {
  
  /**
   * Translates database emergencyCongestionStatus entity to REST emergencyCongestionStatus 
   * 
   * @param emergencyCongestionStatusEntity Database emergencyCongestionStatus entity to be translated
   * @return EmergencyCongestionStatus translated to the REST emergencyCongestionStatus
   */
  public EmergencyCongestionStatus translateEmergencyCongestionStatus(fi.metatavu.soteapi.persistence.model.EmergencyCongestionStatus emergencyCongestionStatusEntity) {
    if (emergencyCongestionStatusEntity == null) {
      return null;
    }
    
    EmergencyCongestionStatus result = new EmergencyCongestionStatus();
    result.setCreated(emergencyCongestionStatusEntity.getCreated());
    result.setValue(emergencyCongestionStatusEntity.getValue());
    result.setId(emergencyCongestionStatusEntity.getId());

    return result;
  }
  
  /**
   * Translates list of JPA emergencyCongestionStatusEntities into REST entities
   * 
   * @param emergencyCongestionStatusEntities JPA entities
   * @return REST entities
   */
  public List<EmergencyCongestionStatus> translateEmergencyCongestionStatuses(List<fi.metatavu.soteapi.persistence.model.EmergencyCongestionStatus> emergencyCongestionStatusEntities) {
    if (emergencyCongestionStatusEntities == null) {
      return Collections.emptyList();
    }
    
    return emergencyCongestionStatusEntities.stream()
      .map(this::translateEmergencyCongestionStatus)
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }
  
}
