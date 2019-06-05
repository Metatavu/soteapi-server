package fi.metatavu.soteapi.survey;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import fi.metatavu.soteapi.bisnode.BisnodeController;
import fi.metatavu.soteapi.server.rest.model.Survey;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestion;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestionSummary;

/**
 * Controller for surveys
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class SurveysController {

  @Inject
  private BisnodeController bisnodeController;

  /**
   * Gets a list of surveys
   * 
   * @return list of surveys
   * 
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  public List<Survey> listSurveys() throws JsonParseException, JsonMappingException, IOException {
    return getSurveysList();
  }

  /**
   * Gets list of survey questions
   * 
   * @param surveyName name of the survey to list questions from
   * @return returns the list of survey questions or null if not found
   *
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public List<SurveyQuestion> listSurveyQuestions(String surveyName) throws JsonParseException, JsonMappingException, IOException {
    Survey survey = findSurveyByName(surveyName);
    if (survey == null) {
      return null;
    }

    return survey.getQuestions();
  }

  /**
   * Finds single survey question by survey name and question number
   * 
   * @param surveyName name of the survey to list questions from 
   * @param number survey question number
   * @return survey question or null if not found
   * 
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public SurveyQuestion findSurveyQuestionBySurveyNameAndNumber(String surveyName, long number) throws JsonParseException, JsonMappingException, IOException {
    List<SurveyQuestion> surveyQuestions = listSurveyQuestions(surveyName);
    if (surveyQuestions == null) {
      return null;
    }

    return surveyQuestions.stream()
      .filter(surveyQuestion -> number == surveyQuestion.getNumber())
      .findAny()
      .orElse(null);
  }

  /**
   * Finds survey question summary by survey name and question number
   * 
   * @param surveyName name of the survey
   * @param number survey question number
   * 
   * @return survey question summary or null if not available
   */
  public SurveyQuestionSummary findSurveyQuestionSummary(String surveyName, long number) {
    if (surveyName == null) {
      return null;
    }

    return bisnodeController.getSurveyQuestionSummary(surveyName, number);
  }

  /**
   * Finds survey by name
   * 
   * @param surveyName survey by name
   * @return survey by name or null if not found
   * 
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public Survey findSurveyByName(String surveyName) throws JsonParseException, JsonMappingException, IOException {
    if (surveyName == null) {
      return null;
    }

    return listSurveys().stream()
      .filter(survey -> surveyName.equals(survey.getName()))
      .findAny()
      .orElse(null);
  }

  /**
   * Reads the list of surveys from file
   * 
   * @return list of survey questions
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private List<Survey> getSurveysList() throws JsonParseException, JsonMappingException, IOException {
    String surveyQuestionsFilePath = System.getProperty("surveys.file");
    if (StringUtils.isBlank(surveyQuestionsFilePath)) {
      return Collections.emptyList();
    }

    File surveyQuestionsFile = new File(surveyQuestionsFilePath);
    if (!surveyQuestionsFile.exists()) {
      return Collections.emptyList();
    }

    return new ObjectMapper().readValue(surveyQuestionsFile, new TypeReference<List<Survey>>(){});
  }

}
