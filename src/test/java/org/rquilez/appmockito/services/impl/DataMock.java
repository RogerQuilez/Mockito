package org.rquilez.appmockito.services.impl;

import org.rquilez.appmockito.models.Exam;

import java.util.Arrays;
import java.util.List;

public class DataMock {
    public static final List<Exam> EXAMS_LIST = Arrays.asList(new Exam(5L, "Maths"), new Exam(6L, "Language"),
            new Exam(7L, "History"));
    public static final List<String> ANSWERS_LIST = Arrays.asList("Answer 1", "Answer 2", "Answer 3", "Answer 4");
}
