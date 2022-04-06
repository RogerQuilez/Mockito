package org.rquilez.appmockito.repositories;

import org.rquilez.appmockito.models.Exam;

import java.util.List;

public interface ExamRepository {

    List<Exam> findAll();
}
