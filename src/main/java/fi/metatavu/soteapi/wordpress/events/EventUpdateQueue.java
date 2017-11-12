package fi.metatavu.soteapi.wordpress.events;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class EventUpdateQueue extends AbstractSoteApiTaskQueue<EventUpdateTask> {

  @Override
  public String getName() {
    return "WORDPRESS-EVENT-UPDATES";
  }

}
