package network;

import models.Recorrido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface RecorridoService {

    @PUT("recorrido/actualizar")
    Call<Void> actualizarUbicacion(@Body Recorrido recorrido);

    @GET("recorrido/recorrido")
    Call <Recorrido> getLast();
}
