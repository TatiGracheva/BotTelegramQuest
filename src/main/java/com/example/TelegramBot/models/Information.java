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
@Table(name = "information")
public class Information {

    @Id
    @Column(name = "id_information")
    private int id;

    @Column(name = "info")
    private String info;

    @Column(name = "text_help")
    private String textHelp;

    @Column(name = "text_welcome")
    private String textWelcome;

    @Column(name = "text_backstage")
    private String textBackstage;

    public Information() {
    }
}
