package co.edu.unipiloto.happypaws;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import models.ChatAdapter;
import models.Paseador;
import network.PaseadorService;
import network.Retro;
import retrofit2.Call;
import retrofit2.Callback;
import android.content.SharedPreferences;
import retrofit2.Response;

public class ViewChats extends AppCompatActivity {

    private RecyclerView recyclerChats;
    private ChatAdapter chatAdapter;
    private List<Paseador> chatList;
    private PaseadorService paseadorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chats);

        recyclerChats = findViewById(R.id.recyclerChatList);
        recyclerChats.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList, paseador -> abrirChat(paseador));
        recyclerChats.setAdapter(chatAdapter);
        paseadorService = Retro.getClient().create(PaseadorService.class);
        //SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        //boolean isUser = preferences.getBoolean("isUser", false);

        cargarChats();
    }

    private void cargarChats() {
        paseadorService.getPaseadores().enqueue(new Callback<List<Paseador>>() {
            @Override
            public void onResponse(Call<List<Paseador>> call, Response<List<Paseador>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatList.clear();
                    chatList.addAll(response.body());
                    chatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Paseador>> call, Throwable t) {
                Log.e("ChatListActivity", "Error al cargar chats", t);
            }
        });
    }

    private void abrirChat(Paseador paseador) {
        Log.i("PAS_ID",paseador.getId()+"");
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("paseId", paseador.getId());
        startActivity(intent);
    }
}