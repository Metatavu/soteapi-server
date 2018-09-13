package fi.metatavu.soteapi.wordpress.tasks.emergency;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class EmergencyCongestionStatusQueue extends AbstractSoteApiTaskQueue<EmergencyCongestionStatusTask> {

  @Override
  public String getName() {
    return "WORDPRESS-EMERGENCY-CONGESTION-STATUS";
  }

}
