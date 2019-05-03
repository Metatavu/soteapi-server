package fi.metatavu.soteapi.bisnode.tasks.reviews;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class ReviewListQueue extends AbstractSoteApiTaskQueue<ReviewListTask> {

  @Override
  public String getName() {
    return "BISNODE-REVIEW-LISTS";
  }

}
