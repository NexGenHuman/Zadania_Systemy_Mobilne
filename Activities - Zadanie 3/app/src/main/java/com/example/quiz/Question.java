package com.example.quiz;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
    private int questionID;
    private boolean trueAnswer;
    private String content;

    public Question(int questionID, boolean trueAnswer) {
        this.questionID = questionID;
        this.trueAnswer = trueAnswer;
    }
}
