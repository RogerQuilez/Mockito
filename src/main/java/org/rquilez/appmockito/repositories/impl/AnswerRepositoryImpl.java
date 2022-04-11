package org.rquilez.appmockito.repositories.impl;

import org.rquilez.appmockito.DataMock;
import org.rquilez.appmockito.repositories.AnswerRepository;

import java.util.List;

public class AnswerRepositoryImpl implements AnswerRepository {

    @Override
    public List<String> findAnswersByIdExam(Long id) {
        System.out.println("AnswerRepositoryImpl.findAnswersByIdExam");
        return DataMock.ANSWERS_LIST;
    }

    @Override
    public void saveAll(List<String> answers) {
        System.out.println("AnswerRepositoryImpl.saveAll");
    }

}
