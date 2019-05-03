package fi.metatavu.soteapi.server.rest;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.soteapi.review.ReviewProductController;
import fi.metatavu.soteapi.server.rest.api.ReviewProductsApi;
import fi.metatavu.soteapi.server.rest.model.ReviewProduct;

/**
 * Api for review products
 * 
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
public class ReviewProductsApiImpl implements ReviewProductsApi {

  @Inject
  private ResponseController responseController;

  @Inject
  private ReviewProductController reviewProductController;

  @Override
  public Response findReviewProduct(Long reviewProductId) {
  try {
      ReviewProduct reviewProduct = reviewProductController.findReviewProductById(reviewProductId);
      return responseController.respondOkOrNotFound(reviewProduct);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }

  @Override
  public Response listReviewProducts() {
    try {
      List<ReviewProduct> reviewProducts = reviewProductController.listReviewProducts();
      return responseController.respondOkOrNotFound(reviewProducts);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }
  
  
}
