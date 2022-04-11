package org.rquilez.appmockito.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.rquilez.appmockito.models.Exam;
import org.rquilez.appmockito.repositories.AnswerRepository;
import org.rquilez.appmockito.repositories.ExamRepository;
import org.rquilez.appmockito.repositories.impl.AnswerRepositoryImpl;
import org.rquilez.appmockito.repositories.impl.ExamRepositoryImpl;

import javax.xml.crypto.Data;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Habilita las anotaciones de mockito en la clase
final class ExamServiceImplTest {

    private static final String EXAM_NAME = "Maths";

    @Mock
    private static ExamRepositoryImpl examRepository;
    @Mock
    private static AnswerRepositoryImpl answerRepository;
    @InjectMocks //Inyecta las dependencias de Service
    private static ExamServiceImpl examService;
    @Captor
    private static ArgumentCaptor<Long> argumentCaptor;

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

//    @Test
//    void getAnswersAndVerify_whenAnswersNoExist() {
//        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
//        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
//        final Exam exam = examService.findExamByNameWithAnswers(EXAM_NAME);
//        assertAll(
//                () -> assertNull(exam),
//                /* Verify if method was called */
//                () -> verify(examRepository).findAll(),
//                () -> verify(answerRepository).findAnswersByIdExam(anyLong())
//        );
//    }

    @Test
    void testSaveExam() {
        /* Given */
        Exam newExam = DataMock.EXAM;
        newExam.setAnswers(DataMock.ANSWERS_LIST);
        when(examRepository.save(any(Exam.class))).then(new Answer<Exam>(){ //Crea una clase al vuelo de Respuesta y podemos manejar el objeto que estamos guardando
            Long sequence = 8L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);
                return exam;
            }
        });

        /* When */
        Exam exam = examService.save(newExam);

        /* Then */
        assertAll(
                () -> assertNotNull(exam.getId()),
                () -> assertEquals(8L, exam.getId()),
                () -> verify(examRepository).save(any(Exam.class)),
                () -> verify(answerRepository).saveAll(anyList())
        );
    }

    @Test
    void testExceptionManagement() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST_ID_NULL);
        when(answerRepository.findAnswersByIdExam(isNull())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            examService.findExamByNameWithAnswers("Maths");
        });
    }

    @Test
    void testArgumentMatchers() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        examService.findExamByNameWithAnswers("Maths");

        verify(examRepository).findAll();
//        verify(answerRepository).findAnswersByIdExam(argThat(arg -> arg.equals(5L)));
        verify(answerRepository).findAnswersByIdExam(eq(5L));
    }

    @Test
    void testArgumentCustomMatchers() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        examService.findExamByNameWithAnswers("Maths");

        verify(examRepository).findAll();
        verify(answerRepository).findAnswersByIdExam(argThat(new MyArgsMatchers()));
    }

    @Test
    void testArgumentCatch() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        examService.findExamByNameWithAnswers("Maths");

//        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(answerRepository).findAnswersByIdExam(argumentCaptor.capture());

        assertEquals(5L, argumentCaptor.getValue());
    }

    @Test
    void testDoThrow() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);

        Exam exam = examService.findExamByNameWithAnswers("Maths");

        doThrow(IllegalArgumentException.class).when(answerRepository).saveAll(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            examService.save(exam);
        });
    }

    @Test
    void testDoAnswer() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
//        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);
        doAnswer(invocationOnMock -> { //Se ejecuta esat invocación cuando se ejecuta el método "findAnswersByIdExam"
            Long id = invocationOnMock.getArgument(0);
            return id == 5L ? DataMock.ANSWERS_LIST : null;
        }).when(answerRepository).findAnswersByIdExam(anyLong());

        Exam exam = examService.findExamByNameWithAnswers("Maths");

        assertAll(
                () -> assertEquals(5L, exam.getId()),
                () -> assertEquals("Maths", exam.getName())
        );
    }

    @Test
    void testDoCallRealMethod() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
//        when(answerRepository.findAnswersByIdExam(anyLong())).thenReturn(DataMock.ANSWERS_LIST);

        doCallRealMethod().when(answerRepository).findAnswersByIdExam(anyLong()); //Execute a real method returning real data

        Exam exam = examService.findExamByNameWithAnswers("Maths");

        assertAll(
                () -> assertEquals(5L, exam.getId()),
                () -> assertEquals("Maths", exam.getName())
        );
    }

    @Test
    void testOrderInvocation() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);

        examService.findExamByNameWithAnswers("Maths");
        examService.findExamByNameWithAnswers("Language");

        InOrder inOrder = inOrder(answerRepository);
        inOrder.verify(answerRepository).findAnswersByIdExam(5L);
        inOrder.verify(answerRepository).findAnswersByIdExam(6L);
    }

    @Test
    void testOrderInvocation2() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);

        examService.findExamByNameWithAnswers("Maths");
        examService.findExamByNameWithAnswers("Language");

        InOrder inOrder = inOrder(examRepository, answerRepository);
        inOrder.verify(examRepository).findAll();
        inOrder.verify(answerRepository).findAnswersByIdExam(5L);
        inOrder.verify(answerRepository).findAnswersByIdExam(6L);
    }

    @Test
    void testNumberOfInvocation() {
        when(examRepository.findAll()).thenReturn(DataMock.EXAMS_LIST);
        examService.findExamByNameWithAnswers("Maths");

        verify(answerRepository, times(1)).findAnswersByIdExam(5L);
        verify(answerRepository, atLeast(1)).findAnswersByIdExam(5L);
        verify(answerRepository, atLeastOnce()).findAnswersByIdExam(5L);
        verify(answerRepository, atMost(1)).findAnswersByIdExam(5L);
        verify(answerRepository, atMostOnce()).findAnswersByIdExam(5L);
        verify(answerRepository, never()).findAnswersByIdExam(6L);
    }

    public static class MyArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long aLong) {
            this.argument = aLong;
            return !Objects.equals(aLong, null) && aLong > 0;
        }

        @Override
        public String toString() {
            return "Class error -> MyArgsMatchers " + this.argument + " have to be positive";
        }

    }
}
