package uz.pdp.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface AdminService {
    void showAdminMenu(Message message, SendMessage sendMessage);
    void phoneCrud();
    void sellStatistics();
    void officesCrud();
    void customersListPdf();
    void orderStatusCrud();
    void changeStatus();
    void orderHistoryByPayTypeInExcel();
}
