package com.example.TelegramBot.DAO;

import com.example.TelegramBot.models.Client;
import com.example.TelegramBot.models.QuestionTables;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ClientDAO {
    private static final int ATTEMPT_ZERO = 0;
    public static final int MAX_NUMBER_ATTEMPTS = 2;
    private final int NUMBER_QUESTION_QUEST = 10;

    public enum TYPE {CONNECTION_QUESTION, QUESTION, BACKSTORY, GEOLOCATION, HISTORICAL_REFERENCE}

    public enum TYPE_key_Hint {KEY_HINT_ONE, KEY_HINT_TWO, KEY_HINT_THREE}

    public final SessionFactory sessionFactory;

    public ClientDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void saveClientSQL(long chatId, String name, String lastName) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatId);
        if (client == null) {
            client = new Client(chatId, name, lastName);
            session.save(client);
        }
    }


    @Transactional
    public String returnTextDatabase(TYPE type, long chatId) {
        switch (type) {
            case CONNECTION_QUESTION -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return questionTables.getConnectionQuestion();
            }
            case QUESTION -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return questionTables.getQuestions();
            }
            case BACKSTORY -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return questionTables.getBackstory();
            }
            case HISTORICAL_REFERENCE -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return questionTables.getHistoricalReference();
            }
            case GEOLOCATION -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return questionTables.getGeolocation();
            }
        }
        return null;
    }


    @Transactional
    public boolean checkAnswers(long chatID, String messageText) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatID);
        QuestionTables questionTables = client.getQuestion();
        boolean resultAnswer = checkAnswersRegEx(messageText, questionTables.getAnswerPossibleOne(),
                questionTables.getAnswerPossibleTwo(),
                questionTables.getAnswerPossibleThree(),
                questionTables.getAnswerPossibleFour());
        return resultAnswer;
    }

    private boolean checkAnswersRegEx(String messageText, String option1, String option2,
                                      String option3, String option4) {
        String arrAnswerOptions[] = {option1, option2, option3, option4};
        for (String s : arrAnswerOptions) {
            if (s == null) {
                return false;
            }
            Pattern patternArr = Pattern.compile(s);
            Matcher matcherArr = patternArr.matcher(messageText.toLowerCase(Locale.ROOT));
            if (matcherArr.find()) {
                return true;
            }
        }
        return false;
    }



    @Transactional
    public void editNumberQuestion(long chatID) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatID);
        client.setNumberQuestion(client.getNumberQuestion() + 1);
    }


    @Transactional
    public boolean checkNumberQuestion(long chatID) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatID);
        return client.getNumberQuestion() == NUMBER_QUESTION_QUEST;
    }

    @Transactional
    public String returnHint(TYPE_key_Hint type, long chatID) {
        switch (type) {
            case KEY_HINT_ONE -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatID);
                QuestionTables questionTables = client.getQuestion();
                client.setNumberAttempts(ATTEMPT_ZERO);//обнуляем попытки
                client.setKeyHintOne(false); //помечаем что использовали подсказку №1
                return questionTables.getHintOne();
            }
            case KEY_HINT_TWO -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatID);
                QuestionTables questionTables = client.getQuestion();
                client.setNumberAttempts(ATTEMPT_ZERO);//обнуляем попытки
                client.setKeyHintTwo(false); //помечаем что использовали подсказку №2
                return questionTables.getHintTwo();
            }
            case KEY_HINT_THREE -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatID);
                QuestionTables questionTables = client.getQuestion();
                client.setNumberAttempts(ATTEMPT_ZERO);//обнуляем попытки
                client.setKeyHintThree(false); //помечаем что использовали подсказку №3
                return questionTables.getHintThree();
            }
        }
        return null;
    }


    @Transactional
    public void editCorrectNumberAttempts(long chatID) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatID);
        client.setNumberAttempts(client.getNumberAttempts() + 1);
    }


    @Transactional
    public boolean checkAttempts(long chatID) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatID);
        return client.getNumberAttempts() == MAX_NUMBER_ATTEMPTS;
    }


    /*использованы ли подсказки для вопроса. Каждый вопрос имеет от 1 до 3 подсказов
        true -не использована;
        false - использована
        */
    @Transactional
    public void editAllKeyHint(long chatID) {
        Session session = sessionFactory.getCurrentSession();
        Client client = session.get(Client.class, chatID);
        client.setNumberAttempts(ATTEMPT_ZERO);
        client.setKeyHintOne(true);
        client.setKeyHintTwo(true);
        client.setKeyHintThree(true);
    }


    @Transactional
    public boolean checkTheHintUsed(TYPE_key_Hint type, long chatId) {
        switch (type) {
            case KEY_HINT_ONE -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                return client.isKeyHintOne();
                //true -не использована;  false - использована
            }
            case KEY_HINT_TWO -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return client.isKeyHintTwo() && questionTables.getHintTwo() != null;
                //true - не использована и имеет текст
            }
            case KEY_HINT_THREE -> {
                Session session = sessionFactory.getCurrentSession();
                Client client = session.get(Client.class, chatId);
                QuestionTables questionTables = client.getQuestion();
                return client.isKeyHintThree() && questionTables.getHintThree() != null;
                //true - не использована и имеет текст
            }
        }
        return false;
    }
}