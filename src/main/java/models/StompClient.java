package models;

import android.util.Log;

import com.google.android.gms.nearby.messages.MessageListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;

public class StompClient {
    private WebSocketClient webSocketClient;
    private MessageListener messageListener;
    private boolean isConnected = false;

    public interface MessageListener {
        void onMessageReceived(String topic, String message);
    }

    public StompClient(String url) {
        try {
            webSocketClient = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("StompClient", "Conectado al servidor WebSocket");
                    isConnected = true;
                    sendConnectFrame();
                }

                @Override
                public void onMessage(String message) {
                    Log.d("StompClient", "Mensaje recibido: " + message);
                    if (messageListener != null) {
                        String[] parts = message.split("\n");
                        if (parts.length > 1) {
                            messageListener.onMessageReceived(parts[0], parts[1]);
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("StompClient", "Desconectado del servidor WebSocket: " + reason);
                    isConnected = false;
                }

                @Override
                public void onError(Exception ex) {
                    Log.e("StompClient", "Error en WebSocket", ex);
                }
            };
        } catch (URISyntaxException e) {
            Log.e("StompClient", "Error en la URI", e);
        }
    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public void connect() {
        if (!isConnected) {
            webSocketClient.connect();
        }
    }

    public void subscribe(String topic) {
        if (isConnected) {
            String subscribeFrame = "SUBSCRIBE\nid:sub-1\ndestination:" + topic + "\n\n\u0000";
            webSocketClient.send(subscribeFrame);
            Log.d("StompClient", "Suscrito a: " + topic);
        }
    }

    public void sendMessage(String topic, String message) {
        if (isConnected) {
            String sendFrame = "SEND\ndestination:" + topic + "\n\n" + message + "\u0000";
            webSocketClient.send(sendFrame);
            Log.d("StompClient", "Mensaje enviado a " + topic + ": " + message);
        }
    }

    public void disconnect() {
        if (isConnected) {
            String disconnectFrame = "DISCONNECT\n\n\u0000";
            webSocketClient.send(disconnectFrame);
            webSocketClient.close();
            isConnected = false;
        }
    }

    private void sendConnectFrame() {
        String connectFrame = "CONNECT\naccept-version:1.1,1.2\nhost:localhost\n\n\u0000";
        webSocketClient.send(connectFrame);
        Log.d("StompClient", "Frame CONNECT enviado");
    }
}