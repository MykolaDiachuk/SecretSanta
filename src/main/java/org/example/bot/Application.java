package org.example.bot;

import org.example.dbconnection.InitializationFromDB;
import org.example.entities.GameSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws TelegramApiException, IOException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            log.info("Registering bot...");
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            log.error("Failed to register bot(check internet connection / bot token or make sure only one instance of bot is running).", e);
        }
        log.info("Telegram bot is ready to accept updates from user......");

        GameSessions.setGameSessions(InitializationFromDB.getGameSessionslist());
        
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8888"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        System.out.println("Listening on port " + port + ".");
        server.start();
    }
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Hello world!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
