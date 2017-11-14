package fi.metatavu.soteapi.utils;

import org.jsoup.Jsoup;

public class HtmlUtils {

  private HtmlUtils() {
  }

  public static String html2text(String html) {
    return Jsoup.parse(html).text();
  }
  
}
