package org.rquilez.appmockito.models;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Exam {

    private Long id;
    private String name;
    private List<String> answers;

    public Exam(Long id, String name) {
        this.id = id;
        this.name = name;
        this.answers = new LinkedList<>();
    }
}
