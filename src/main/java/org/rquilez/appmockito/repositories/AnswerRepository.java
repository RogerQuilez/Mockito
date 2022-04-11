package org.rquilez.appmockito.repositories;

import java.util.List;

public interface AnswerRepository {

    List<String> findAnswersByIdExam(Long id);
    void saveAll(List<String> answers);
}
