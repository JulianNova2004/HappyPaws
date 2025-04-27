package network;

import java.util.List;

import models.Paseador;
import models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaseadorService {

    @GET("paseador/paseadores")
    Call<List<Paseador>> getPaseadores();

    @POST("paseador/register")
    Call<Paseador> savePaseador(@Body Paseador paseador);

    @POST("paseador/login")
    Call<Paseador> login(@Body Paseador paseador);

}
