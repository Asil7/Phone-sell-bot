package uz.pdp.bot;

import com.google.gson.Gson;
import jdk.internal.util.xml.impl.Input;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.DataJson;
import uz.pdp.model.*;
import uz.pdp.model.abs.User;
import uz.pdp.model.enums.Role;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.FileNotFoundException;


import java.io.*;
import java.io.File;
import java.util.*;

import static uz.pdp.DataBase.*;
import static uz.pdp.ServicesImpl.adminService;
import static uz.pdp.ServicesImpl.customerService;

public class OnlinePhoneSellBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "News_b7_bot";
    }

    @Override
    public String getBotToken() {
        return "5001208656:AAHZerqZ5Dq9zTVAJCiENOi9h3ShXXAD07E";
    }

    @Override
    public void onUpdateReceived(Update update) {
        UserActivity currentUserActivity;
        SendMessage sendMessage = new SendMessage();
        EditMessageText editMessage = new EditMessageText();
        SendPhoto sendPhoto = new SendPhoto();
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            sendMessage.setChatId(chatId.toString());
            Message message = update.getMessage();

            currentUserActivity = getCurrentUserActivity(chatId);
            if (message.hasText()) {
                String text = message.getText();
                switch (text) {
                    case "/start":
                        startMessage(sendMessage, message);
                        try {
                            currentUserActivity.setMsgFromBot(execute(sendMessage));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "See phones":
                        currentUserActivity.setRound(3);
                        sendMessage.setText(customerService.showPhones(currentUserActivity));
                        sendMessage.setReplyMarkup(getReplyKeyboard(currentUserActivity));
                        try {
                            currentUserActivity.setMsgFromBot(execute(sendMessage));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "My cart":
                        currentUserActivity.setRound(5);
                        String phones = "";
                        for (OrderItem phone : currentUserActivity.getMyCart()) {
                            phones += "\nMODEL: " + phone.getPhone().getModel() + "\nQuantity : " + phone.getQuantity() + "\n";
                        }
                        sendMessage.setText("My Cart \n" + phones);
                        sendMessage.setReplyMarkup(getReplyKeyboard(currentUserActivity));

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "Order history":
                        writeInPdf();
                        sendMessage.setText("PDF");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        InputFile inputFile = new InputFile(new File("src/main/resources/history.pdf"));
                        SendDocument sendDocument = new SendDocument();
                        sendDocument.setChatId(chatId.toString());
                        sendDocument.setDocument(inputFile);
                        try {
                            execute(sendMessage);
                            execute(sendDocument);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            //
            else if (message.hasContact()) {
                checkUserRole(message, sendMessage);
                try {
                    currentUserActivity.setMsgFromBot(execute(sendMessage));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            currentUserActivity = getCurrentUserActivity(chatId);
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            sendMessage.setChatId(chatId.toString());
            editMessage.setChatId(chatId.toString());
            sendPhoto.setChatId(chatId.toString());
            switch (data) {
                case "Order":
                    sendMessage.setText("Ordered");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    OrderHistory orderHistory = new OrderHistory();
                    for (OrderItem orderItem : currentUserActivity.getMyCart()) {
                        orderHistory.getOrderItems().add(orderItem);
                    }
                    Customer user = new Customer();
                    user.setFirstName(currentUserActivity.getUser().getFirstName());
                    user.setId(currentUserActivity.getUser().getId());
                    orderHistory.setClient(user);
                    historyList.add(orderHistory);
                    currentUserActivity.getMyCart().clear();
                    writeListJson();
                    break;
                case "quantity":
                    break;
                case "plus":
                    currentUserActivity.setQuantity(currentUserActivity.getQuantity() + 1);
                    editPhotoMessage(chatId, currentUserActivity);
                    break;
                case "minus":
                    currentUserActivity.setQuantity(currentUserActivity.getQuantity() - 1);
                    editPhotoMessage(chatId, currentUserActivity);
                    break;
                case "addMyCart":
                    OrderItem orderItem = new OrderItem(currentUserActivity.getCurrentPhone(), currentUserActivity.getQuantity());

                    currentUserActivity.getMyCart().add(orderItem);

                    currentUserActivity.getMyCart().forEach(System.out::println);

                    System.out.println("Phone : " + currentUserActivity.getCurrentPhone().toString() + "\nquantity" + currentUserActivity.getQuantity());
                    sendMessage.setText("Added");
                    try {
                        execute(new DeleteMessage(chatId.toString(), currentUserActivity.getMsgFromBot().getMessageId()));
                        currentUserActivity.setMsgFromBot(execute(sendMessage));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(5000);
                            execute(new DeleteMessage(chatId.toString(), currentUserActivity.getMsgFromBot().getMessageId()));
                        } catch (TelegramApiException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        currentUserActivity.setMsgFromBot(null);
                    });
                    thread.start();
                    break;
                case "prev":
                    currentUserActivity.setPosition(currentUserActivity.getPosition() - 10);
                    editTextMessage(currentUserActivity, editMessage);
                    break;
                case "next":
                    int position = currentUserActivity.getPosition() + 10;
                    currentUserActivity.setPosition(position);
                    editTextMessage(currentUserActivity, editMessage);
                    break;
                default:
                    Phone phone = null;
                    for (Phone phoneF : phoneList) {
                        if (data.equals(String.valueOf(phoneF.getId()))) phone = phoneF;
                    }
                    currentUserActivity.setCurrentPhone(phone);
                    currentUserActivity.setRound(4);
                    sendPhoto.setPhoto(new InputFile(new File(phone.getImgUrl())));
                    sendPhoto.setReplyMarkup(getReplyKeyboard(currentUserActivity));
                    sendPhoto.setCaption("Brand: " + phone.getBrand() + "\n" + "Model: " + phone.getModel() + "\n" + "Memory: " + phone.getMemory() + "\n" + "Price: " + phone.getPrice() + "\n" + "Year: " + phone.getYear());
                    try {
                        currentUserActivity.setMsgFromBot(execute(sendPhoto));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void editPhotoMessage(Long chatId, UserActivity currentUserActivity) {
        Phone phone = currentUserActivity.getCurrentPhone();
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        editMessageMedia.setChatId(chatId.toString());
        editMessageMedia.setMessageId(currentUserActivity.getMsgFromBot().getMessageId());
        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
        inputMediaPhoto.setMedia(new File(phone.getImgUrl()), "image");
        inputMediaPhoto.setCaption("Brand: " + phone.getBrand() + "\n" + "Model: " + phone.getModel() + "\n" + "Memory: " + phone.getMemory() + "\n" + "Price: " + phone.getPrice() + "\n" + "Year: " + phone.getYear());
        editMessageMedia.setMedia(inputMediaPhoto);
        editMessageMedia.setReplyMarkup((InlineKeyboardMarkup) getReplyKeyboard(currentUserActivity));
        try {
            currentUserActivity.setMsgFromBot((Message) execute(editMessageMedia));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void editTextMessage(UserActivity currentUserActivity, EditMessageText editMessage) {
        editMessage.setText(customerService.showPhones(currentUserActivity));
        editMessage.setReplyMarkup((InlineKeyboardMarkup) getReplyKeyboard(currentUserActivity));
        editMessage.setMessageId(currentUserActivity.getMsgFromBot().getMessageId());
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private UserActivity getCurrentUserActivity(Long chatId) {
        if (!userActivityMap.containsKey(chatId)) userActivityMap.put(chatId, new UserActivity());
        return userActivityMap.get(chatId);
    }


    private void startMessage(SendMessage sendMessage, Message message) {
        Long chatId = message.getChatId();

        UserActivity currentUserActivity = getCurrentUserActivity(chatId);
        sendMessage.setText("Welcome to the online phone sales bot");
        currentUserActivity.setRound(1);
        sendMessage.setReplyMarkup(getReplyKeyboard(currentUserActivity));
    }

    private void checkUserRole(Message message, SendMessage sendMessage) {
        Contact contact = message.getContact();
        Long chatId = message.getChatId();
        if (userMap.containsKey(chatId) && userMap.get(chatId).getRole() == Role.ADMIN) {
            Admin user = (Admin) userMap.get(chatId);
            userActivityMap.get(chatId).setUser(user);
            adminService.showAdminMenu(message, sendMessage);

        } else if (userMap.containsKey(contact.getUserId()) && userMap.get(contact.getUserId()).getRole() == Role.CUSTOMER) {
            Customer currentUser = (Customer) userMap.get(contact.getUserId());
            UserActivity currentUserActivity = userActivityMap.get(chatId);
            currentUserActivity.setUser(currentUser);

            customerService.showCustomerMenu(currentUserActivity, message, sendMessage);
        } else {
            Customer currentUser = new Customer(contact.getUserId(), contact.getFirstName(), contact.getLastName(), contact.getPhoneNumber(), message.getFrom().getUserName(), Role.CUSTOMER);
            userMap.put(currentUser.getId(), currentUser);
            Map<Long, User> mapTemp = new HashMap<>(userMap);
            mapTemp.remove(905951214L);
            DataJson.writer(mapTemp.values(), "src/main/resources/jsons/customers.json");
            UserActivity currentUserActivity = userActivityMap.get(chatId);
            currentUserActivity.setUser(currentUser);
            customerService.showCustomerMenu(currentUserActivity, message, sendMessage);
        }
    }

    public static ReplyKeyboard getReplyKeyboard(UserActivity currentUserActivity) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRowList.add(keyboardRow);
        keyboardRowList.add(keyboardRow2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inKeyBtnRowList = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(inKeyBtnRowList);

        switch (currentUserActivity.getRound()) {
            case 1:
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setText("Share contact");
                keyboardButton.setRequestContact(true);
                keyboardRow.add(keyboardButton);
                break;
            case 2:
                keyboardRow.add("See phones");
                keyboardRow.add("My cart");
                keyboardRow2.add("Order history");
                break;
            case 3:
                int clothListSize = currentUserActivity.getPhonesInPageList().size();
                List<InlineKeyboardButton> inlineBtnRowN = new ArrayList<>();
                inKeyBtnRowList.add(inlineBtnRowN);
                for (int i = 0; i < clothListSize; i++) {
                    InlineKeyboardButton btnN = new InlineKeyboardButton();
                    btnN.setText(String.valueOf(i + 1));
                    btnN.setCallbackData(String.valueOf(i + 1));
                    inlineBtnRowN.add(btnN);
                    if (i + 1 == clothListSize / 2) {
                        inlineBtnRowN = new ArrayList<>();
                        inKeyBtnRowList.add(inlineBtnRowN);
                    }
                }
                List<InlineKeyboardButton> inlineBtnRowPrevNext = new ArrayList<>();
                InlineKeyboardButton btnPrev = new InlineKeyboardButton("< Prev");
                btnPrev.setCallbackData("prev");
                if (currentUserActivity.getPosition() > 1) inlineBtnRowPrevNext.add(btnPrev);
                InlineKeyboardButton btnNext = new InlineKeyboardButton("Next >");
                btnNext.setCallbackData("next");
                if (currentUserActivity.getPosition() + 10 < phoneList.size()) inlineBtnRowPrevNext.add(btnNext);
                inKeyBtnRowList.add(inlineBtnRowPrevNext);
                return inlineKeyboardMarkup;
            case 4:
                List<InlineKeyboardButton> inKeyBtnRow1 = new ArrayList<>();
                InlineKeyboardButton inKeyBtnMinus = new InlineKeyboardButton("➖");
                inKeyBtnMinus.setCallbackData("minus");
                if (currentUserActivity.getQuantity() > 1) inKeyBtnRow1.add(inKeyBtnMinus);
                InlineKeyboardButton inKeyBtnQuantity = new InlineKeyboardButton("" + currentUserActivity.getQuantity());
                inKeyBtnQuantity.setCallbackData("quantity");
                inKeyBtnRow1.add(inKeyBtnQuantity);
                InlineKeyboardButton inKeyBtnPlus = new InlineKeyboardButton("➕");
                inKeyBtnPlus.setCallbackData("plus");
                inKeyBtnRow1.add(inKeyBtnPlus);
                InlineKeyboardButton inKeyBtnAddCart = new InlineKeyboardButton("Add my cart");
                inKeyBtnAddCart.setCallbackData("addMyCart");
                List<InlineKeyboardButton> inKeyBtnRow2 = new ArrayList<>();
                inKeyBtnRow2.add(inKeyBtnAddCart);
                inKeyBtnRowList.add(inKeyBtnRow1);
                inKeyBtnRowList.add(inKeyBtnRow2);
                return inlineKeyboardMarkup;
            case 5:
                List<InlineKeyboardButton> inKeyBtnRow = new ArrayList<>();
                InlineKeyboardButton inKeyBtnOrder = new InlineKeyboardButton("Order");
                inKeyBtnOrder.setCallbackData("Order");
                inKeyBtnRow.add(inKeyBtnOrder);
                InlineKeyboardButton inKeyBtnRemove = new InlineKeyboardButton("product remove");
                inKeyBtnRemove.setCallbackData("product remove");
                inKeyBtnRow.add(inKeyBtnRemove);
                inKeyBtnRowList.add(inKeyBtnRow);
                return inlineKeyboardMarkup;
        }
        return replyKeyboardMarkup;
    }

    public static void writeListJson() {
        try (Writer writer = new FileWriter("src/main/resources/jsons/historyList.json")) {
            Gson gson = new Gson();
            String data = gson.toJson(historyList);

            writer.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeInPdf() {
        try {
            PdfWriter pdfWriter = new PdfWriter("src/main/resources/history.pdf");
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A5);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            Paragraph paragraph = new Paragraph();
            paragraph.add("Currencies").setBackgroundColor(Color.BLUE);
            document.add(paragraph);
            float[] pointColumnWidths = {50F, 150F, 150F, 150F, 50F, 150F};
            Table table = new Table(pointColumnWidths);
            table.addCell(new Cell().add("№"));
            table.addCell(new Cell().add("USER NAME"));
            table.addCell(new Cell().add("Brand"));
            table.addCell(new Cell().add("PHONE"));
            table.addCell(new Cell().add("QUANTITY"));
            table.addCell(new Cell().add("DATA"));


            int i=1;
            for (OrderHistory orderHistory : historyList) {
                table.addCell("" + (i++));
                table.addCell("" + orderHistory.getClient().getFirstName());
                for (OrderItem orderItem : orderHistory.getOrderItems()) {
                    table.addCell("" + orderItem.getPhone().getBrand());
                    table.addCell("" + orderItem.getPhone().getModel());
                    table.addCell("" + orderItem.getQuantity());
                }
                table.addCell("" + orderHistory.getDate());
            }
            System.out.println("Started!!!");
            document.add(table);
            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
