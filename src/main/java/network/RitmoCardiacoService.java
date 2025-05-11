package network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RitmoCardiacoService {

    //@GET("/ritmo/heart-rate/{dogId}")
    //Call<> RitmoCardiaco getHeartRate(@Path int dogId);

    @GET("/ritmo/filtro/{petId}/{fecha}")
    Call<Double> getByFecha(@Path("fecha") String fechaStr, @Path("petId") int petId);
}
