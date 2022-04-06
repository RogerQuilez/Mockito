package org.rquilez.appmockito.services;

import org.rquilez.appmockito.models.Exam;

import java.util.Optional;

public interface ExamService {

    Optional<Exam> findExamByName(String name);
    Exam findExamByNameWithAnswers(String name);
}
