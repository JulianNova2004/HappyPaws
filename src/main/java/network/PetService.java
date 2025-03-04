package network;

import java.util.List;

import models.Pet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetService {
        @POST("pet/save/{id_owner}")
        Call<Pet> savePet(@Body Pet pet, @Path("id_owner") int idOwner);

        @POST("pet/saves/{id_owner}")
        Call<Void> savePets(@Body List<Pet> pets, @Path("id_owner") int idOwner);

}
