package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//The annotation which adds the @Controller.The RESTful web service controller simply returns the object
// and the object data is written directly to the HTTP response as JSON/XML.
@RestController
//The annotation to map with URL request of type '/'
@RequestMapping("/")
public class AnswerBusinessController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    //An annotation to map this method with Http request of POST type, map it with URL request of type '/question/{questionId}/answer/create' and it produces and consumes Json
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId,
                                                       @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        //An instance of AnswerEntity is created which sets the Answer Content from the Answer Request.
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());
        //The accessToken is split to as Bearer and the actual AuthorizationToken
        String[] bearerToken = accessToken.split("Bearer ");
        //Here the answerBusinessService is calls createAnswer method where it takes in the answerEntity,questionId and the BearerToken as parameters
        answerBusinessService.createAnswer(answerEntity, questionId, bearerToken[1]);
        //after persist.The answerResponse gets the new Uuid of the  Answer created
        AnswerResponse answerResponse = new AnswerResponse().id(answerEntity.getUuid()).status("ANSWER CREATED");
        //returns the answerResponse with HTTP status
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);

    }

}