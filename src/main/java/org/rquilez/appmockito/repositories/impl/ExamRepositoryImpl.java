package org.rquilez.appmockito.repositories.impl;

import org.rquilez.appmockito.models.Exam;
import org.rquilez.appmockito.repositories.ExamRepository;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {

    @Override
    public List<Exam> findAll() {
        return Arrays.asList(new Exam(5L, "Maths"), new Exam(6L, "Language"),
                new Exam(7L, "History"));
    }
}
