package org.example;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MyBot extends TelegramLongPollingBot {

    public MyBot(){
        super("My_API");
    }

    public void sendMessage(long chatId, String text) throws Exception{
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }

    public void sendPrice(long chatId, String name) throws Exception{
        var price = CryptoPrice.spotPrice(name);
        sendMessage(chatId,"Ціна "+name+" : "+ price.getAmount().doubleValue()+" $");
    }
    public void sendPicture(long chatId, String name) throws Exception{
        var photo = getClass().getClassLoader().getResourceAsStream(name);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(photo,name));
        execute(message);
    }
    public void sendBuy(long chatId,long amount,  String name) throws Exception{
        var price = CryptoPrice.spotPrice(name);
        var result = amount/price.getAmount().doubleValue();
        sendMessage(chatId,"За "+amount+"$ ви можете купити "+result+" "+name);
    }

    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        try {
            var message = new SendMessage();
            message.setChatId(chatId);

            if(text.equals("/start")){
                sendPicture(chatId,"Bitcoin.png");
                sendMessage(chatId,"Hello!");
            }else if (text.equals("/all")) {
                sendPicture(chatId,"Bitcoin.png");
                sendPrice(chatId,"BTC");
                sendPrice(chatId,"ETH");
                sendPrice(chatId,"DOGE");
            } else if (text.equals("btc")||text.equals("BTC")) {
                sendPicture(chatId,"Bitcoin.png");
                sendPrice(chatId,"BTC");
            }else if (text.equals("eth")||text.equals("ETH")) {
                sendPicture(chatId,"Bitcoin.png");
                sendPrice(chatId,"ETH");
            }else if (text.equals("doge")||text.equals("DOGE")) {
                sendPicture(chatId,"Bitcoin.png");
                sendPrice(chatId,"DOGE");
            }else if (text.equals("btc,eth")) {
                sendPicture(chatId,"Bitcoin.png");
                sendPrice(chatId,"BTC");
                sendPrice(chatId,"ETH");
            }else if (text.equals("eth,btc,doge")) {
                sendPicture(chatId,"Bitcoin.png");
                sendPrice(chatId,"BTC");
                sendPrice(chatId,"ETH");
                sendPrice(chatId,"DOGE");
            }else if (text.startsWith("btc ")) {
                sendPicture(chatId,"Bitcoin.png");
                var amountString = text.substring(4);
                var amount = Long.parseLong(amountString);
                sendBuy(chatId,amount,"BTC");
            }else if (text.startsWith("eth ")) {
                sendPicture(chatId,"ETH.png");
                var amountString = text.substring(4);
                var amount = Long.parseLong(amountString);
                sendBuy(chatId,amount,"ETH");
            }else if (text.startsWith("doge ")) {
                sendPicture(chatId,"DOGE.jpg");
                var amountString = text.substring(5);
                var amount = Long.parseLong(amountString);
                sendBuy(chatId,amount,"DOGE");
            } else{
                sendMessage(chatId,"Невідома команда!");
            }

        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    @Override
    public String getBotUsername() {
        return "VindenerCryptoBot";
    }
}
