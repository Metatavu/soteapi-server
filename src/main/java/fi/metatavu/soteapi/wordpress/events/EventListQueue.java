package fi.metatavu.soteapi.wordpress.events;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class EventListQueue extends AbstractSoteApiTaskQueue<EventListTask> {

  @Override
  public String getName() {
    return "WORDPRESS-EVENT-LISTS";
  }

}
