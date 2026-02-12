package ru.otus.chat.server;

public interface OtusServer {

    void start();
    void subscribe(ClientHandler clientHandler);
    void unsubscribe(ClientHandler clientHandler);
    void broadcast(String message);
}
