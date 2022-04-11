package org.rquilez.appmockito.repositories.impl;

import org.rquilez.appmockito.DataMock;
import org.rquilez.appmockito.models.Exam;
import org.rquilez.appmockito.repositories.ExamRepository;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {

    @Override
    public List<Exam> findAll() {
        System.out.println("ExamRepositoryImpl.findAll");
        return DataMock.EXAMS_LIST;
    }

    @Override
    public Exam save(Exam exam) {
        System.out.println("ExamRepositoryImpl.save");
        return DataMock.EXAM;
    }
}
