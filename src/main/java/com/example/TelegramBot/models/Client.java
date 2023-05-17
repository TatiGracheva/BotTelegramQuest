package com.example.TelegramBot.models;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;


@Component
@Data
@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "number_question")
    private int numberQuestion;

    @Column(name = "number_attempts")
    private int numberAttempts;

    @Column(name = "key_hint_1")
    private boolean keyHintOne;

    @Column(name = "key_hint_2")
    private boolean keyHintTwo;

    @Column(name = "key_hint_3")
    private boolean keyHintThree;

    @ManyToOne
    @JoinColumn(name = "number_question",
            referencedColumnName = "number_question",
            insertable = false, updatable = false)
    private QuestionTables question;


/*переменные keyHint - использованы ли подсказки для вопроса.
Каждый вопрос имеет от 1 до 3 подсказов
 true -не использована;
 false - использована

numberAttempts - количество попыток отгадать вопрос.
 */

    public Client(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.numberQuestion = 1;
        this.numberAttempts = 0;
        this.keyHintOne = true;
        this.keyHintTwo = true;
        this.keyHintThree = true;
    }

    public Client() {
    }

}
