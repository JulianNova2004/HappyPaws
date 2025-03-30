package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import models.Mensaje;
import models.MessageAdapter;
import network.ChatService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private MessageAdapter messageAdapter;
    private List<Mensaje> messageList;
    private EditText editMessage;
    private Button btnSend;
    private ChatService chatService;
    private int chatId;
    private WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerMessages = findViewById(R.id.recyclerChat);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        editMessage = findViewById(R.id.etMensaje);
        btnSend = findViewById(R.id.btnEnviar);

        chatId = getIntent().getIntExtra("chatId", -1);
        if (chatId == -1) {
            Log.e("ChatActivity", "Error: chatId no recibido");
            //finish();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        int userId = preferences.getInt("User_ID",-1);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, userId);
        recyclerMessages.setAdapter(messageAdapter);

        chatService = Retro.getClient().create(ChatService.class);

        cargarMensajes();
        conectarWebSocket();

        btnSend.setOnClickListener(v -> enviarMensaje());
    }

    private void cargarMensajes() {
        chatService.obtenerMensajes(chatId).enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    //messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Log.e("ChatActivity", "Error al cargar mensajes", t);
            }
        });
    }

    private void conectarWebSocket() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://tu-servidor/chat/" + chatId)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("WebSocket", "Conectado");
                }

                @Override
                public void onMessage(String message) {
                    runOnUiThread(() -> {
                        Mensaje nuevoMensaje = new Mensaje(); // Aquí debes convertir el mensaje JSON a objeto
                        nuevoMensaje.setContent(message);
                        messageList.add(nuevoMensaje);
                        messageAdapter.notifyDataSetChanged();
                        recyclerMessages.scrollToPosition(messageList.size() - 1);
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("WebSocket", "Desconectado: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.e("WebSocket", "Error", ex);
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensaje() {
        String contenido = editMessage.getText().toString().trim();
        if (!contenido.isEmpty()) {
            webSocketClient.send(contenido);
            editMessage.setText("");
        }
    }
}