package fi.metatavu.soteapi.emergency;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import fi.metatavu.soteapi.persistence.dao.EmergencyCongestionStatusDAO;
import fi.metatavu.soteapi.persistence.model.EmergencyCongestionStatus;
import fi.metatavu.soteapi.server.rest.model.EmergencyCongestionStatusListSort;

/**
 * Controller for emergency congestion status functions
 * 
 * @author Antti Leppä
 */
@ApplicationScoped
public class EmergencyCongestionStatusController {

  @Inject
  private Logger logger;
  
  @Inject
  private EmergencyCongestionStatusDAO emergencyCongestionStatusDAO;
  
  /**
   * Creates new emergency congestion status
   * 
   * @param value value
   * @param created created
   * @return create emergency congestion status
   */
  public EmergencyCongestionStatus createEmergencyCongestionStatus(Integer value, OffsetDateTime created) {
    return emergencyCongestionStatusDAO.create(value, created);
  }
  
  /**
   * Lists emergency congestion statuses
   * 
   * @param firstResult first result
   * @param maxResults max results
   * @param sort sort
   * @return list of emergency congestion statuses
   */
  public List<EmergencyCongestionStatus> listEmergencyCongestionStatuses(Long firstResult, Long maxResults, EmergencyCongestionStatusListSort sort) {
    if (sort == null) {
      sort = EmergencyCongestionStatusListSort.DESC;
    }
    
    switch (sort) {
      case DESC:
        return emergencyCongestionStatusDAO.listSortByCreatedDesc(firstResult, maxResults);
      case ASC:
        return emergencyCongestionStatusDAO.listSortByCreatedAsc(firstResult, maxResults);
      default:
        logger.warn("Unknown emergency congestion status list sort {}", sort);
    }
    
    return Collections.emptyList();
  }
  
  /**
   * Returns current status for emergency congestion
   * 
   * @return current status for emergency congestion
   */
  public int getCurrentEmergencyCongestionStatus() {
    List<EmergencyCongestionStatus> statues = emergencyCongestionStatusDAO.listSortByCreatedDesc(0l, 1l);
    if (statues.size() > 0) {
      return statues.get(0).getValue();
    }
    
    return 0;
  }
  
  

}
