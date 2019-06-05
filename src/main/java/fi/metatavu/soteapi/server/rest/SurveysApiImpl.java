package fi.metatavu.soteapi.server.rest;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import fi.metatavu.soteapi.server.rest.api.SurveysApi;
import fi.metatavu.soteapi.server.rest.model.Survey;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestion;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestionSummary;
import fi.metatavu.soteapi.survey.SurveysController;

/**
 * Api for surveys 
 * 
 * @author Heikki Kurhinen
 */
@RequestScoped
@Stateful
public class SurveysApiImpl implements SurveysApi {

  @Inject
  private ResponseController responseController;

  @Inject
  private SurveysController surveysController;

  @Override
  public Response findSurvey(String surveyName) {
    try {
      Survey survey = surveysController.findSurveyByName(surveyName);
      return responseController.respondOkOrNotFound(survey);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }

  @Override
  public Response findSurveyQuestionSummary(String surveyName, Long questionNumber) {
    try {
      SurveyQuestion surveyQuestion = surveysController.findSurveyQuestionBySurveyNameAndNumber(surveyName, questionNumber);
      if (surveyQuestion == null) {
        return responseController.respondNotFound();
      }

      SurveyQuestionSummary surveyQuestionSummary = surveysController.findSurveyQuestionSummary(surveyName, questionNumber);
      return responseController.respondOkOrNotFound(surveyQuestionSummary);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }

  @Override
  public Response listSurveyQuestions(String surveyName) {
    try {
      List<SurveyQuestion> surveyQuestions = surveysController.listSurveyQuestions(surveyName);
      return responseController.respondOk(surveyQuestions);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }

  @Override
  public Response listSurveys() {
    try {
      List<Survey> surveys = surveysController.listSurveys();
      return responseController.respondOk(surveys);
    } catch (IOException e) {
      return responseController.respondInternalServerError(e);
    }
  }
}
