package co.edu.unipiloto.happypaws;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import models.Mensaje;
import models.MessageAdapter;
import network.ChatService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Chat extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private MessageAdapter messageAdapter;
    private List<Mensaje> messageList;
    private EditText editMessage;
    private Button btnSend;
    private ChatService chatService;
    private int chatId;
    private int userId;

    private int pasId;
    private static final String WEBSOCKET_URL = "ws://10.0.2.2:8080/ws/websocket"; // Asegúrate de que la URL es correcta

    private StompClient stompClient;
    private Disposable stompSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerMessages = findViewById(R.id.recyclerChat);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        editMessage = findViewById(R.id.etMensaje);
        btnSend = findViewById(R.id.btnEnviar);

        pasId = getIntent().getIntExtra("paseId", -1);
        //chatId = getIntent().getIntExtra("pasId", -1);


        if (pasId == -1) {
            Log.e("ChatActivity", "Error: paseId no recibido, revisar ViewChats");
            finish();
            return;
        }

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        userId = preferences.getInt("User_ID", -1);


        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, userId);
        recyclerMessages.setAdapter(messageAdapter);
        chatService = Retro.getClient().create(ChatService.class);
        crearChat();


        btnSend.setOnClickListener(v -> enviarMensaje());
    }

    private void crearChat(){
        chatService.getchatId(userId,pasId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    chatId = response.body();
                    Log.i("chat_id",chatId+"");

                    conectarWebSocket();
                    cargarMensajes();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ChatActivity", "Error crear o obtener chat", t);
            }
        });
    }
    private void cargarMensajes() {
        Call<List<Mensaje>> call = chatService.obtenerMensajes(chatId);
        call.enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ChatActivity", "Mensajes cargados: " + response.body().size());
                    messageList.clear();
                    messageList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                    recyclerMessages.scrollToPosition(messageList.size() - 1); // 🔹 Asegurar que se vean los últimos mensajes
                } else {
                    Log.e("ChatActivity", "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                Log.e("ChatActivity", "Error al cargar mensajes", t);
            }
        });
    }

    private void conectarWebSocket() {
        // 🔹 Correcta inicialización del StompClient
//        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_URL);
//
//        stompClient.lifecycle().subscribe(lifecycleEvent -> {
//            switch (lifecycleEvent.getType()) {
//                case OPENED:
//                    Log.d("WebSocket", "Conectado al WebSocket");
//                    break;
//                case ERROR:
//                    Log.e("WebSocket", "Error en la conexión", lifecycleEvent.getException());
//                    break;
//                case CLOSED:
//                    Log.d("WebSocket", "WebSocket cerrado");
//                    break;
//            }
//        });
//
//        stompClient.connect();
//
//        // 🔹 Suscribirse a un canal para recibir mensajes
//        stompSubscription = stompClient.topic("/chat/" + chatId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(message -> {
//                    Gson gson = new Gson();
//                    Mensaje nuevoMensaje = gson.fromJson(message.getPayload(), Mensaje.class);
//
//                    runOnUiThread(() -> {
//                        messageList.add(nuevoMensaje);
//                        messageAdapter.notifyDataSetChanged();
//                        recyclerMessages.scrollToPosition(messageList.size() - 1);
//                        Log.i("WebSocket", "Suscribio");
//                    });
//                }, throwable -> Log.e("WebSocket", "Error en suscripción", throwable));

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_URL);
        stompClient.connect();

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("WebSocket", "Conectado al WebSocket");
                    break;
                case ERROR:
                    Log.e("WebSocket", "Error en la conexión", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("WebSocket", "WebSocket cerrado");
                    break;
            }
        });

        // 🔹 Espera a que `chatId` esté inicializado antes de suscribirse
        if (chatId == 0) {
            Log.e("WebSocket", "Error: chatId no inicializado.");
            return;
        }


        Log.d("WebSocket", "🔍 Suscribiéndose a: /topic/chat/" + chatId);
        stompSubscription = stompClient.topic("/topic/chat/" + chatId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    Log.d("WebSocket", "📩 Mensaje recibido: " + message.getPayload()); // 🔹 Verifica qué llega
                    Mensaje nuevoMensaje = new Gson().fromJson(message.getPayload(), Mensaje.class);

                    messageList.add(nuevoMensaje);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                }, throwable -> Log.e("WebSocket", "❌ Error en suscripción", throwable));




    }

    private void enviarMensaje() {
        String contenido = editMessage.getText().toString().trim();
        if (!contenido.isEmpty()) {
            Mensaje nuevoMensaje = new Mensaje(userId, pasId, contenido, true); //COREGIRRR
            Gson gson = new Gson();
            String jsonMensaje = gson.toJson(nuevoMensaje);

            // 🔹 Enviar mensaje usando stompClient.send(...)
            stompClient.send("/app/chat", jsonMensaje).subscribe();

            editMessage.setText("");
        }else{
            Log.i("ERROOOOOOOR",contenido);
        }
    }

    @Override
    protected void onDestroy() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        if (stompSubscription != null) {
            stompSubscription.dispose();
        }
        super.onDestroy();
    }
}
