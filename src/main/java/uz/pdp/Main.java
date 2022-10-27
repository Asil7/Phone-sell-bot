package uz.pdp;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.bot.OnlinePhoneSellBot;

import static uz.pdp.DataBase.*;
import static uz.pdp.DataJson.getAllDataFromJson;
import static uz.pdp.DataJson.writer;

public class Main {
    public static final String ACCOUNT_SID = "ACdf7295fdef60822f073f426f75977743";
    public static final String AUTH_TOKEN = "96d2c196fde7f17636b8acc0adc2d77b";

    public static void main(String[] args) throws TelegramApiException {

        getAllDataFromJson();
        new TelegramBotsApi(DefaultBotSession.class).registerBot(new OnlinePhoneSellBot());
    }
}
