package uz.pdp.services.servicesImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.services.AdminService;

public class AdminServiceImpl implements AdminService {
    @Override
    public void showAdminMenu(Message message, SendMessage sendMessage) {
        sendMessage.setText("Assalamu alaykum admin!");
        sendMessage.setChatId(message.getChatId().toString());

    }

    @Override
    public void phoneCrud() {

    }

    @Override
    public void sellStatistics() {

    }

    @Override
    public void officesCrud() {

    }

    @Override
    public void customersListPdf() {

    }

    @Override
    public void orderStatusCrud() {

    }

    @Override
    public void changeStatus() {

    }

    @Override
    public void orderHistoryByPayTypeInExcel() {

    }
}
