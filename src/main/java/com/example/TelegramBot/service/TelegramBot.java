package com.example.TelegramBot.service;

import com.example.TelegramBot.Config.BotConfig;
import com.example.TelegramBot.DAO.ClientDAO;
import com.example.TelegramBot.DAO.InformationDAO;
import com.example.TelegramBot.models.MySticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final String TEXT_FAILED_ATTEMPT = "давай подумаем ещё))";
    private static final String TEXT_NO_HINTS = "Увы, подсказок больше нет.";
    private final String TEXT_BEFORE_HINT = "Подсказку? Жми)";
    private final String TEXT_FEEDBACK = "Бабушка очень благодарна, что Вы решили оставить отзыв. " +
            "Введите, пожалуйста, ваши пожелания в формате:\n" +
            "/feedback отзыв";

    private final String BUTTON_INFO = "Информация о квесте";
    private final String BUTTON_START = "Начать";
    private final String BUTTON_QUESTION = "Вопрос";
    private final String BUTTON_GEOLOCATION = "Геолокация";
    private final String BUTTON_FEEDBACK = "Оставить отзыв";
    private final String BUTTON_HINT_1 = "Подсказка";
    private final String BUTTON_HINT_2 = "Подсказка №2";
    private final String BUTTON_HINT_3 = "Подсказка №3";
    private final BotConfig botConfig;
    private final ClientDAO clientDAO;
    private final InformationDAO infoDAO;

    private final MySticker mySticker;

    public TelegramBot(BotConfig config,
                       ClientDAO clientDAO,
                       InformationDAO informationDAO,
                       MySticker mySticker) {
        this.botConfig = config;
        this.clientDAO = clientDAO;
        this.infoDAO = informationDAO;
        this.mySticker = mySticker;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();

            if (messageText.contains("/feedback")) {
                sendMessage(botConfig.getOwner(), messageText.substring(messageText.indexOf(" ")));
            } else {
                switch (messageText) {
                    case "/start" -> {
                        clientDAO.saveClientSQL(chatID,
                                update.getMessage().getChat().getFirstName(),
                                update.getMessage().getChat().getLastName());
                        sendMessageAndButton(chatID,
                                infoDAO.returnInformation(InformationDAO.TYPE.TEXT_WELCOME),
                                BUTTON_INFO, BUTTON_START);
                    }
                    default -> {
                        if (clientDAO.checkAnswers(chatID, messageText)) {//ответ верный
                            sendEmoji(chatID);
                            String message = clientDAO.returnTextDatabase(ClientDAO.TYPE.HISTORICAL_REFERENCE, chatID);
                            clientDAO.editAllKeyHint(chatID);//для следующего вопроса все подсказки и попытки становяться не использованы
                            if (clientDAO.checkNumberQuestion(chatID)) {//если вопросов больше нет -> кнопка оставить отзыв
                                sendMessageAndButton(
                                        chatID, message, BUTTON_FEEDBACK, null);
                            } else {// вопросы есть - продолжаем
                                clientDAO.editNumberQuestion(chatID);
                                sendMessageAndButton(chatID, message, BUTTON_GEOLOCATION, null);
                            }
                        } else {//ответ не верный
                            if (clientDAO.checkAttempts(chatID)) {// попыток > 3 выдаем подсказку
                                //проверка наличия не ипользованных подсказок + наличие самой подсказки
                                if (clientDAO.checkTheHintUsed(ClientDAO.TYPE_key_Hint.KEY_HINT_ONE, chatID)) {
                                    sendMessageAndButton(chatID, TEXT_BEFORE_HINT, BUTTON_HINT_1, null);
                                } else if (clientDAO.checkTheHintUsed(ClientDAO.TYPE_key_Hint.KEY_HINT_TWO, chatID)) {
                                    sendMessageAndButton(chatID, TEXT_BEFORE_HINT, BUTTON_HINT_2, null);
                                } else if (clientDAO.checkTheHintUsed(ClientDAO.TYPE_key_Hint.KEY_HINT_THREE, chatID)) {
                                    sendMessageAndButton(chatID, TEXT_BEFORE_HINT, BUTTON_HINT_3, null);
                                } else {
                                    sendMessage(chatID, TEXT_NO_HINTS);
                                }
                            } else {
                                clientDAO.editCorrectNumberAttempts(chatID);
                                sendMessage(chatID, TEXT_FAILED_ATTEMPT);
                            }
                        }
                    }
                }
            }


        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatID = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case BUTTON_INFO -> {
                    sendMessageAndButton(chatID, infoDAO.returnInformation(InformationDAO.TYPE.INFO),
                            BUTTON_START, null);
                }
                case BUTTON_START -> {
                    sendMessageAndButton(chatID,
                            infoDAO.returnInformation(InformationDAO.TYPE.TEXT_HELP),
                            BUTTON_GEOLOCATION,
                            null);
                }
                case BUTTON_GEOLOCATION -> {
                    sendMessage(chatID, clientDAO.returnTextDatabase(ClientDAO.TYPE.CONNECTION_QUESTION, chatID));
                    sendMessage(chatID, clientDAO.returnTextDatabase(ClientDAO.TYPE.GEOLOCATION, chatID));
                    sendMessageAndButton(chatID, clientDAO.returnTextDatabase(ClientDAO.TYPE.BACKSTORY, chatID),
                            BUTTON_QUESTION, null);
                }
                case BUTTON_QUESTION -> {
                    sendMessage(chatID, clientDAO.returnTextDatabase(ClientDAO.TYPE.QUESTION, chatID));
                }
                case BUTTON_FEEDBACK -> {
                    sendMessage(chatID, TEXT_FEEDBACK);
                }
                case BUTTON_HINT_1 -> {//отправляем текст подсказки №1
                    sendMessage(chatID, clientDAO.returnHint(ClientDAO.TYPE_key_Hint.KEY_HINT_ONE, chatID));
                }
                case BUTTON_HINT_2 -> {//отправляем текст подсказки №2
                    sendMessage(chatID, clientDAO.returnHint(ClientDAO.TYPE_key_Hint.KEY_HINT_TWO, chatID));
                }
                case BUTTON_HINT_3 -> {//отправляем текст подсказки №3
                    sendMessage(chatID,
                            clientDAO.returnHint(ClientDAO.TYPE_key_Hint.KEY_HINT_THREE, chatID));
                }
                default -> sendMessage(chatID, "вот вот доделаем)");
            }
        }
    }


    private void sendMessage(long chatID, String testToSed) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatID));
        sendMessage.setText(testToSed);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

    private void sendMessageAndButton(long chatId, String text, String buttonOne, String buttonTwo) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        InlineKeyboardMarkup markupInLine = creatingMenuButtons(buttonOne, buttonTwo);
        sendMessage.setReplyMarkup(markupInLine);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }


    private InlineKeyboardMarkup creatingMenuButtons(String buttonOne, String buttonTwo) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        if (buttonOne != null) {
            creatingButton(rowsInLine, buttonOne);
        }
        if (buttonTwo != null) {
            creatingButton(rowsInLine, buttonTwo);
        }
        markupInLine.setKeyboard(rowsInLine);
        return markupInLine;
    }

    private void creatingButton(List list, String button) {
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton infoButton = new InlineKeyboardButton();
        infoButton.setText(button);
        infoButton.setCallbackData(button);
        rowInLine.add(infoButton);
        list.add(rowInLine);
    }

    private void sendEmoji(long chatID) {
        SendSticker sendSticker = new SendSticker(String.valueOf(chatID),
                new InputFile(mySticker.returnIdSticker()));
        try {
            execute(sendSticker);
        } catch (TelegramApiException e) {

        }
    }
}
