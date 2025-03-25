package network;

import java.util.List;

import models.Pet;
import retrofit2.Call;
import retrofit2.http.Body;
<<<<<<< HEAD
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
=======
>>>>>>> main
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetService {
//        @POST("pet/save/{id_owner}")
//        Call<Pet> savePet(@Body Pet pet, @Path("id_owner") int idOwner);

        @POST("pet/saves/{id_owner}")
        Call<Void> savePets(@Body List<Pet> pets, @Path("id_owner") int idOwner);

<<<<<<< HEAD
        @GET("pet/getPets/{id_owner}")
        Call<List<Pet>> getAllPets(@Path("id_owner")int idOwner);

        @PATCH("pet/update/{id_owner}")
        Call<Void> updatePet(@Path("id_owner")int idPet, @Body Pet pet);

        //confirmar
        @GET("pet/getPet/{id_pet}")
        Call<Pet> getPet(@Path("id_pet")int id_pet);

        @DELETE("pet/delete/{id}")
        Call<Pet> deletePet(@Path("id") int pet_id);
=======
>>>>>>> main
}
