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
import models.User;
import models.UserChatAdapter;
import network.PaseadorService;
import network.Retro;
import network.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import android.content.SharedPreferences;
import retrofit2.Response;

public class ViewChats extends AppCompatActivity {

    private RecyclerView recyclerChats;
    private ChatAdapter chatAdapter;
    private UserChatAdapter userChatAdapter;
    private List<Paseador> chatList;

    private List<User> userChatList;
    private PaseadorService paseadorService;

    private UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chats);

        recyclerChats = findViewById(R.id.recyclerChatList);
        recyclerChats.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = getSharedPreferences("SaveSession", MODE_PRIVATE);
        boolean isUser = preferences.getBoolean("isUser", false);



        if(isUser)
        {   chatList = new ArrayList<>();
            chatAdapter = new ChatAdapter(chatList, paseador -> abrirChatUser(paseador));
            recyclerChats.setAdapter(chatAdapter);
            paseadorService = Retro.getClient().create(PaseadorService.class);
        }
        else{
            userChatList = new ArrayList<>();
            userChatAdapter = new UserChatAdapter(userChatList, user -> abrirChatPaseador(user));
            recyclerChats.setAdapter(userChatAdapter);
            userService = Retro.getClient().create(UserService.class);
        }

        cargarChats(isUser);
    }

    private void cargarChats(boolean isUser) {
        if (isUser){
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
                Log.e("ChatListActivity", "Error al cargar chats de Usuario", t);
            }
            });
        } else {
            userService.getAllUsers().enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        userChatList.clear();
                        userChatList.addAll(response.body());
                        userChatAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.e("ChatListActivity", "Error al cargar chats de paseadores", t);
                }
            });
        }
    }

    private void abrirChatUser(Paseador paseador) {
        Log.i("PAS_ID",paseador.getId()+"");
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("paseId", paseador.getId());
        startActivity(intent);
    }

    private void abrirChatPaseador(User user){
        Log.i("USER_ID",user.getUserId()+"");
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("UserViewId", user.getUserId());
        startActivity(intent);
    }
}