package com.example.TelegramBot.DAO;

import com.example.TelegramBot.models.Information;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InformationDAO {

    public final SessionFactory sessionFactory;

    public InformationDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public enum TYPE {INFO, TEXT_HELP, TEXT_WELCOME, TEXT_BACKSTAGE}

    @Transactional
    public String returnInformation(TYPE type) {
        switch (type) {
            case INFO -> {
                Session session = sessionFactory.getCurrentSession();
                Information information = session.get(Information.class, 0);
                return information.getInfo();
            }
            case TEXT_HELP -> {
                Session session = sessionFactory.getCurrentSession();
                Information information = session.get(Information.class, 0);
                return information.getTextHelp();
            }
            case TEXT_WELCOME -> {
                Session session = sessionFactory.getCurrentSession();
                Information information = session.get(Information.class, 0);
                return information.getTextWelcome();
            }
            case TEXT_BACKSTAGE -> {
                Session session = sessionFactory.getCurrentSession();
                Information information = session.get(Information.class, 0);
                return information.getTextBackstage();
            }
        }
        return null;
    }
}
