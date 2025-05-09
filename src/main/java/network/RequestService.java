package network;

import java.util.List;

import models.Paseador;
import models.Request;
import models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestService {

    //Crear request
    @POST("request/create/{user_id}/{paseador_id}")
    Call<Request> crearReq(@Body Request request, @Path("user_id") int user_id, @Path("paseador_id") int paseador_id);

    //Paseadores aceptados segun user_id
    @GET("request/getPas/{user_id}")
    Call<List<Paseador>> getPasPorUserId(@Path("user_id") int user_id);

    //Paseadores disponibles para enviar request (estado -1 o sin haber relacion)
    @GET("request/getRech/{user_id}")
    Call<List<Paseador>> getNoReloRech(@Path("user_id") int user_id);

    //Buscar cuantas request quedan pendientes (estado 0)
    @GET("request/msgP/{pas_id}")
    Call<Integer> pendingmsg(@Path("pas_id") int pas_id);

    //Obtener request pendientes (estado 0)
    @GET("request/requestPending/{pas_id}")
    Call<List<Request>> pendingRequest(@Path("pas_id") int pas_id);

    //Aceptar o rechazar request -> 1 aceptar -1 rechazar
    @PATCH("request/edit/{req_id}/{code}")
    Call<Request> edit(@Path("req_id") int req_id,@Path("code") int code);

    //Obtener solicitudes de un usuario en especifico
    @GET("request/getRequest/{userId}")
    Call<List<Request>> getRequestUser(@Path("userId") int userId);

    //Usuarios aceptados por parte de paseador (estado 1)
    @GET("request/accept/{pas_id}")
    Call<List<User>> getUserAccepted(@Path("pas_id") int pas_id);

    //Filtrar según secuencia de caracteres indicada
    @GET("filter/{name}")
    Call<List<Paseador>> getPasLike(@Path("name") String name);


    //Borrar request despues de pasado un tiempo determinado
    @DELETE("/del/{id}")
    Call<Void> deleteReq(@Path("id") int id);
}
