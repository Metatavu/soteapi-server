package fi.metatavu.soteapi.wordpress;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;

import fi.metatavu.soteapi.settings.SystemSettingController;

@ApplicationScoped
public class WordpressClientProvider {
  
  @Inject
  private SystemSettingController systemSettingController;

  @Produces
  public Wordpress produceWordpress() {
    String url = systemSettingController.getSettingValue(WordpressConsts.URL_SETTING);
    if (StringUtils.isNotBlank(url)) {
      return ClientFactory.fromConfig(ClientConfig.of(url, "", "", WordpressConsts.USE_PERMALINK_ENDPOINT, WordpressConsts.DEBUG_CLIENT));
    }
    
    return null;
  }

}
