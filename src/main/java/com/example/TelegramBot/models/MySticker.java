package com.example.TelegramBot.models;


import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MySticker {
    private final String DUCK = "CAACAgIAAxkBAAEH3UJj9zaDJ1lsStQew0DvQmEOgJv-MgAC_gADVp29CtoEYTAu-df_LgQ";
    private final String CHAMOMILE = "CAACAgIAAxkBAAEH3URj9za1ImwR1gZRqDFV-rp70YJMAgACsAEAAladvQqTGPSLTiiWyS4E";
    private final String KOALA = "CAACAgIAAxkBAAEH3UZj9zb9E019UviPVSRbW_F6BnHduAACJQADO2AkFIJXwKgdRCEtLgQ";
    private final String HAMSTER = "CAACAgIAAxkBAAEH3Upj9zcyMA6EsBvJZNME48KCXr2v9gACBgADr8ZRGp7O7vhbqf36LgQ";
    private final String FOX = "CAACAgIAAxkBAAEH3Uxj9zeyKCcK7jV8WCZRL_DJclgDwAACoAAD9wLID8NHHQGgm8AaLgQ";
    private final String CHERRY = "CAACAgIAAxkBAAEH3U5j9zfoBLQ8WT4gnFjy2hE2NSJAdQACFQADwDZPE81WpjthnmTnLgQ";
    private final String OWL = "CAACAgIAAxkBAAEH3VFj9zg8mZfoHrWgsam1RIAufAno-QACKQADwZxgDPBLqR6_2N98LgQ";
    private final String YES = "CAACAgIAAxkBAAEH3VRj9zh-OpHqI-mvwkJSYkI_5lXNHgACRAADQbVWDFpIqKL_emcULgQ";
    private final String DOG = "CAACAgEAAxkBAAEH3VZj9zi80AJOTTx3fl_1rB1mEv6o1AAC6wEAAjgOghGzhgTO4ZxJOS4E";
    private final String HARE = "CAACAgIAAxkBAAEH3Vpj9zk2qZxFhiL6-6JsZMT2aVjTCAACcgUAAj-VzAoR4VZdHmW_cC4E";


    final String arrSticker[] = {DUCK, CHAMOMILE, KOALA, HAMSTER, FOX, CHERRY, OWL, YES, DOG, HARE};

    public String returnIdSticker() {
        Random random = new Random();
        return arrSticker[random.nextInt(arrSticker.length)];
    }

}
