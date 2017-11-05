package fi.metatavu.soteapi.rss.news;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class NewsUpdateQueue extends AbstractSoteApiTaskQueue<NewsUpdateTask> {

  @Override
  public String getName() {
    return "RSS-NEWS-UPDATE";
  }

}
