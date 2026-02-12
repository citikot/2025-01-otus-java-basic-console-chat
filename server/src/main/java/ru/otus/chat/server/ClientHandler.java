package ru.otus.chat.server;

import ru.otus.chat.server.exception.CommonServerException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        handleClient();
    }

    private void handleClient() throws IOException {
        sendMsg("Введите ваш никнейм: ");
        String username = in.readUTF();

        new Thread(() -> {
            try {
                System.out.println("Клиент подключился на порту: " + socket.getPort());

                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith("/")) {
                        if (message.equals("/exit")) {
                            sendMsg("/exitok");
                            break;
                        }

                    } else {
                        server.broadcast(username + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

public void sendMsg(String message) {
    try {
        out.writeUTF(message);
    } catch (IOException e) {
        throw new CommonServerException(e);
    }
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public void disconnect() {
    server.unsubscribe(this);
    try {
        if (in != null) {
            in.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        if (out != null) {
            out.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        if (socket != null) {
            socket.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
