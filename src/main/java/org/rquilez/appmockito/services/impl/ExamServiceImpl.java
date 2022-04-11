package org.rquilez.appmockito.services.impl;

import org.rquilez.appmockito.models.Exam;
import org.rquilez.appmockito.repositories.AnswerRepository;
import org.rquilez.appmockito.repositories.ExamRepository;
import org.rquilez.appmockito.services.ExamService;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    private ExamRepository repository;
    private AnswerRepository answerRepository;

    public ExamServiceImpl(ExamRepository repository, AnswerRepository answerRepository) {
        this.repository = repository;
        this.answerRepository = answerRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return repository.findAll()
                .stream()
                .filter(e -> e.getName().contains(name))
                .findFirst();
    }

    @Override
    public Exam findExamByNameWithAnswers(String name) {
        Optional<Exam> optionalExam = findExamByName(name);
        if (optionalExam.isPresent()) {
            List<String> answers = answerRepository.findAnswersByIdExam(optionalExam.get().getId());
            optionalExam.get().setAnswers(answers);
        }
        return optionalExam.orElseThrow();
    }

    @Override
    public Exam save(Exam exam) {
        if (!exam.getAnswers().isEmpty()) {
            answerRepository.saveAll(exam.getAnswers());
        }
        return repository.save(exam);
    }

}
