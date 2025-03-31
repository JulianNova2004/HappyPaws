package network;

import java.util.List;

import models.Mensaje;
import models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatService {

    @GET("chat/{chatId}/mensajes")
    Call<List<Mensaje>> obtenerMensajes(@Path("chatId") int chatId);

    @GET("chat/gchat/{userId}/{pasId}")
    Call<Integer> getchatId(@Path("userId") int userId,@Path("pasId") int pasId);


}
