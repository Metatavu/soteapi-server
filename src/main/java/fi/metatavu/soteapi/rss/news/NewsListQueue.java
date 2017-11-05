package fi.metatavu.soteapi.rss.news;

import javax.enterprise.context.ApplicationScoped;

import fi.metatavu.soteapi.tasks.AbstractSoteApiTaskQueue;

@ApplicationScoped
public class NewsListQueue extends AbstractSoteApiTaskQueue<NewsListTask> {

  @Override
  public String getName() {
    return "RSS-NEWS-LISTS";
  }

}
