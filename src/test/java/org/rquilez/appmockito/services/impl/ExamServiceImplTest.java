package org.rquilez.appmockito.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rquilez.appmockito.models.Exam;
import org.rquilez.appmockito.repositories.AnswerRepository;
import org.rquilez.appmockito.repositories.ExamRepository;
import org.rquilez.appmockito.repositories.impl.ExamRepositoryImpl;
import org.rquilez.appmockito.services.ExamService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Habilita las anotaciones de mockito en la clase
final class ExamServiceImplTest {

    private static final String EXAM_NAME = "Maths";

    @Mock
    private static ExamRepository examRepository;
    @Mock
    private static AnswerRepository answerRepository;
    @InjectMocks //Inyecta las dependencias de Service
    private static ExamServiceImpl examService;

    @Test
    void findExamByName_whenExamExist() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        final Optional<Exam> optionalExam = examService.findExamByName(EXAM_NAME);
        assertAll(
                () -> assertTrue(optionalExam.isPresent()),
                () -> assertEquals(EXAM_NAME, optionalExam.orElseThrow().getName()),
                () -> assertEquals(5L, optionalExam.orElseThrow().getId())
        );
    }

    @Test
    void findExamByName_whenExamNoExist() {
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        final Optional<Exam> optionalExam = examService.findExamByName(EXAM_NAME);
        assertAll(
                () -> assertFalse(optionalExam.isPresent())
        );
    }

    @Test
    void getAnswers_whenAnswersExist() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        final Exam exam = examService.findExamByNameWithAnswers(EXAM_NAME);
        assertAll(
                () -> assertEquals(4, exam.getAnswers().size()),
                () -> assertTrue(exam.getAnswers().contains("Answer 1"))
        );
    }

    @Test
    void getAnswersAndVerify_whenAnswersExist() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        final Exam exam = examService.findExamByNameWithAnswers(EXAM_NAME);
        assertAll(
                () -> assertEquals(4, exam.getAnswers().size()),
                () -> assertTrue(exam.getAnswers().contains("Answer 1")),
                /* Verify if method was called */
                () -> verify(examRepository).findAll(),
                () -> verify(answerRepository).findAnswersByIdExam(anyLong())
        );
    }

    @Test
    void getAnswersAndVerify_whenAnswersNoExist() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        final Exam exam = examService.findExamByNameWithAnswers(EXAM_NAME);
        assertAll(
                () -> assertNull(exam),
                /* Verify if method was called */
                () -> verify(examRepository).findAll(),
                () -> verify(answerRepository).findAnswersByIdExam(anyLong())
        );
    }
}
