package com.example.TelegramBot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@Entity
@Table(name = "question")
public class QuestionTables {
    @Id
    @Column(name = "number_question")
    private int numberQuestion;

    @Column(name = "geolocation")
    private String geolocation;

    @Column(name = "questions")
    private String questions;

    @Column(name = "historical_reference")
    private String historicalReference;

    @Column(name = "help_1")
    private String hintOne;

    @Column(name = "help_2")
    private String hintTwo;

    @Column(name = "help_3")
    private String hintThree;

    @Column(name = "answer_possible_1")
    private String answerPossibleOne;

    @Column(name = "answer_possible_2")
    private String answerPossibleTwo;

    @Column(name = "answer_possible_3")
    private String answerPossibleThree;

    @Column(name = "answer_possible_4")
    private String answerPossibleFour;

    @Column(name = "connection_question")
    private String connectionQuestion;

    @Column(name = "backstory")
    private String backstory;

    public QuestionTables() {
    }

    public QuestionTables(int numberQuestion, String geolocation, String questions,
                          String historicalReference, String hintOne, String hintTwo,
                          String hintThree, String answerPossibleOne, String answerPossibleTwo,
                          String answerPossibleThree, String answerPossibleFour,
                          String connectionQuestion, String backstory) {
        this.numberQuestion = numberQuestion;
        this.geolocation = geolocation;
        this.questions = questions;
        this.historicalReference = historicalReference;
        this.hintOne = hintOne;
        this.hintTwo = hintTwo;
        this.hintThree = hintThree;
        this.answerPossibleOne = answerPossibleOne;
        this.answerPossibleTwo = answerPossibleTwo;
        this.answerPossibleThree = answerPossibleThree;
        this.answerPossibleFour = answerPossibleFour;
        this.connectionQuestion = connectionQuestion;
        this.backstory = backstory;
    }
}
