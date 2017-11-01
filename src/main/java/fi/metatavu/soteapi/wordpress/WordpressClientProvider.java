package fi.metatavu.soteapi.wordpress;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;

@ApplicationScoped
public class WordpressClientProvider {

  @Produces
  public Wordpress produceWordpress() {
    return ClientFactory.fromConfig(ClientConfig.of(WordpressConsts.BASE_URL, "", "", WordpressConsts.USE_PERMALINK_ENDPOINT, WordpressConsts.DEBUG_CLIENT));
  }

}
