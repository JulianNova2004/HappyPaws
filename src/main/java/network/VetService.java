package network;

import java.util.List;

import models.Vet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface VetService {

    @GET("vet/vets")
    Call<List<Vet>> getVets();

    @POST("vet/save")
    Call<Vet> addVet(@Body Vet vet);

    @POST("vet/login")
    Call<Vet> login(@Body Vet vet);

}
