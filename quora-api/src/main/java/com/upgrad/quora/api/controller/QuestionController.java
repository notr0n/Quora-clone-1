package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method=RequestMethod.POST, path="/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest,
                                                   @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {

        final QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());

        final QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(questionEntity, accessToken);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }
/////////ResponseEntity GET method takes the accessToken as parameter and returns a list of question details for getAllQuestions endpoint
    @RequestMapping(method=RequestMethod.GET, path="/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity getAllQuestions(@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException{

        //  String[] bearerToken = accessToken.split("Bearer");
        // final QuestionEntity getAllQuestions = questionBusinessService.getAllQuestions(bearerToken[1]);


        final List<QuestionEntity> getAllQuestions = questionBusinessService.getAllQuestions(accessToken);

        List<QuestionDetailsResponse> entities = new ArrayList<QuestionDetailsResponse>();
        for (QuestionEntity n : getAllQuestions) {
            QuestionDetailsResponse entity = new QuestionDetailsResponse();

            entity.setId( n.getUuid());
            entity.setContent(n.getContent());
            entities.add(entity);

        }
        return  new ResponseEntity<>( entities,HttpStatus.OK);


    }

}
