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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import models.Mensaje;
import models.MessageAdapter;
import models.StompClient;
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
    private int userId;
    private static final String WEBSOCKET_URL = "ws://192.168.1.9:8080/chat-websocket";

    private StompClient stompClient;
    //private Disposable stompSubscription;
    //private WebSocketClient webSocketClient;

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
            Log.e("ChatActivity", "Error: chatId no recibido, revisar ViewChats");
            finish();
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
        Call<List<Mensaje>> call = chatService.obtenerMensajes(chatId);
        call.enqueue(new Callback<List<Mensaje>>() {
        //chatService.obtenerMensajes(chatId).enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Log.e("ChatActivity", "Error al cargar mensajes", t);
            }
        });
    }

    private void conectarWebSocket() {
        stompClient = new StompClient(WEBSOCKET_URL);
        stompClient.setMessageListener((topic, message) -> runOnUiThread(() -> {
            Gson gson = new Gson();
            Mensaje nuevoMensaje = gson.fromJson(message, Mensaje.class);
            messageList.add(nuevoMensaje);
            messageAdapter.notifyDataSetChanged();
            recyclerMessages.scrollToPosition(messageList.size() - 1);
        }));

        stompClient.connect();
        stompClient.subscribe("/chat/" + chatId);
    }

    private void enviarMensaje() {
        String contenido = editMessage.getText().toString().trim();
        if (!contenido.isEmpty()) {
            Mensaje nuevoMensaje = new Mensaje(userId, 0, contenido, true);
            Gson gson = new Gson();
            String jsonMensaje = gson.toJson(nuevoMensaje);

            stompClient.sendMessage("/app/chat", jsonMensaje);

            editMessage.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        super.onDestroy();
    }

}