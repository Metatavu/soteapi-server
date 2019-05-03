package fi.metatavu.soteapi.server.rest;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.soteapi.server.rest.api.SurveyQuestionsApi;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestion;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestionSummary;
import fi.metatavu.soteapi.survey.SurveyQuestionsController;

/**
 * Api for survey questions
 * 
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
public class SurveyQuestionsApiImpl implements SurveyQuestionsApi {

  @Inject
  private ResponseController responseController;

  @Inject
  private SurveyQuestionsController surveyQuestionsController;

  @Override
  public Response findSurveyQuestion(Long surveyQuestionId) {
    try {
      SurveyQuestion surveyQuestion = surveyQuestionsController.findSurveyQuestionById(surveyQuestionId);
      return responseController.respondOkOrNotFound(surveyQuestion);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }

  @Override
  public Response findSurveyQuestionSummary(Long surveyQuestionId) {
    SurveyQuestion surveyQuestion;
    try {
      surveyQuestion = surveyQuestionsController.findSurveyQuestionById(surveyQuestionId);
      if (surveyQuestion == null) {
        return responseController.respondNotFound();
      }

      SurveyQuestionSummary surveyQuestionSummary = surveyQuestionsController.findSurveyQuestionSummary(surveyQuestion);
      return responseController.respondOkOrNotFound(surveyQuestionSummary);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }

  }

  @Override
  public Response listSurveyQuestions() {
    try {
      List<SurveyQuestion> surveyQuestions = surveyQuestionsController.listSurveyQuestions();
      return responseController.respondOk(surveyQuestions);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }
  
}
