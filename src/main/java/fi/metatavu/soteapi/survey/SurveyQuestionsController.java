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
import fi.metatavu.soteapi.server.rest.model.SurveyQuestion;
import fi.metatavu.soteapi.server.rest.model.SurveyQuestionSummary;

/**
 * Controller for survey questions
 * 
 * @author Heikki Kurhinen
 */
@ApplicationScoped
public class SurveyQuestionsController {

  @Inject
  private BisnodeController bisnodeController;

  /**
   * Gets a list of survey questions
   * 
   * @return list of survey questions
   * 
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  public List<SurveyQuestion> listSurveyQuestions() throws JsonParseException, JsonMappingException, IOException {
    return getSurveyQuestionList();
  }

  /**
   * Finds single survey question by id
   * 
   * @param id survey question id
   * @return survey question by id or null if not found
   * 
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public SurveyQuestion findSurveyQuestionById(long id) throws JsonParseException, JsonMappingException, IOException {
    List<SurveyQuestion> surveyQuestions = listSurveyQuestions();
    return surveyQuestions.stream()
      .filter(surveyQuestion -> id == surveyQuestion.getId())
      .findAny()
      .orElse(null);
  }

  /**
   * Finds survey question summary by survey question
   * 
   * @param surveyQuestion survey question to get the summary for
   * @return survey question summary or null if not available
   */
  public SurveyQuestionSummary findSurveyQuestionSummary(SurveyQuestion surveyQuestion) {
    if (surveyQuestion == null) {
      return null;
    }

    return bisnodeController.getSurveyQuestionSummary(surveyQuestion);
  }

  /**
   * Reads the list of survey questions from file
   * 
   * @return list of survey questions
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private List<SurveyQuestion> getSurveyQuestionList() throws JsonParseException, JsonMappingException, IOException {
    String surveyQuestionsFilePath = System.getProperty("survey.questions.file");
    if (StringUtils.isBlank(surveyQuestionsFilePath)) {
      return Collections.emptyList();
    }

    File surveyQuestionsFile = new File(surveyQuestionsFilePath);
    if (!surveyQuestionsFile.exists()) {
      return Collections.emptyList();
    }

    return new ObjectMapper().readValue(surveyQuestionsFile, new TypeReference<List<SurveyQuestion>>(){});
  }

}
