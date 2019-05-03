package fi.metatavu.soteapi.server.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.EnumUtils;

import fi.metatavu.soteapi.emergency.EmergencyCongestionStatusController;
import fi.metatavu.soteapi.persistence.model.EmergencyCongestionStatus;
import fi.metatavu.soteapi.rest.translate.EmergencyCongestionStatusTranslator;
import fi.metatavu.soteapi.server.rest.api.EmergencyCongestionStatusesApi;
import fi.metatavu.soteapi.server.rest.model.EmergencyCongestionStatusListSort;

@RequestScoped
@Stateful
public class EmergencyCongestionStatusesApiImpl implements EmergencyCongestionStatusesApi {

  @Inject
  private EmergencyCongestionStatusTranslator emergencyCongestionStatusTranslator;
  
  @Inject
  private EmergencyCongestionStatusController emergencyCongestionStatusController;
  
  @Inject
  private ResponseController responseController;
  
  @Override
  public Response listEmergencyCongestionStatuses(String sortParam, Long firstResult, Long maxResults) {
    EmergencyCongestionStatusListSort sort = EnumUtils.getEnum(EmergencyCongestionStatusListSort.class, sortParam);
    if (sort == null) {
      sort = EmergencyCongestionStatusListSort.DESC;
    }
    
    List<EmergencyCongestionStatus> emergencyCongestionStatusEntities = emergencyCongestionStatusController.listEmergencyCongestionStatuses(firstResult, maxResults, sort);
    return responseController.respondOk(emergencyCongestionStatusTranslator.translateEmergencyCongestionStatuses(emergencyCongestionStatusEntities));
  }
  
}
