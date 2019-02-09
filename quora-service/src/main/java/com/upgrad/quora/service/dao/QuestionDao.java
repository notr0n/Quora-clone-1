package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }
/////////////////////getting the list of questions for getAllQuestions endpoint/////////////////////////
    public List<QuestionEntity> getAllQuestions(){

        return entityManager.createNamedQuery("AllQuestions", QuestionEntity.class).getResultList();
    }
///////////////////////getting the list of questions for getAllQuestionsbxuserid endpoint////////////////
    public List<QuestionEntity> getQuestionByUserId(final String userId) {
        try {
            TypedQuery<QuestionEntity> query = entityManager.createNamedQuery("AllQuestions", QuestionEntity.class);

            List<QuestionEntity> questionList = query.getResultList();
            List<QuestionEntity> resultList = new ArrayList<QuestionEntity>();
            //this for loop selects all the comments whose imageId equals the comment's imageid
            for(QuestionEntity question : questionList){
                if(question.getUser().getUuid().equals(userId)){
                    //add the question to the list
                    resultList.add(question);
                }
            }

            return resultList;
        } catch(NoResultException nre) {
            return null;
        }
    }
    //This method is used to get he details of the question in the database which takes the questionId as input from the user.
    public QuestionEntity getQuestion(final String uuid) {
        try {


            return entityManager.createNamedQuery("getQuestion", QuestionEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
