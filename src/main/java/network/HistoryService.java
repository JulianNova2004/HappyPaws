package network;
import java.util.List;

import models.Consulta;
import models.History;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface HistoryService {

    @POST("history/add/{pet_id}")
    Call<History> addHistory(@Body History history, @Path("pet_id") int pet_id);

    @GET("history/getAll")
    Call<List<History>> getAllHistories();

    @GET("history/tipos")
    Call<List<String>> getVaccineTypes();

    @GET("history/get/{id}")
    Call<List<History>> getHistoryByPetId(@Path("id") int id);

    @GET("history/find/{his_id}")
    Call<History> getHistory(@Path("his_id") int his_id);

    @PATCH("history/update/{id}")
    Call<History> updateHistory(@Body History history, @Path("id")int his_id);

    @DELETE("history/delete/{id}")
    Call<History> deleteHistory(@Path("id") int id);

    //Stats
    @GET("history/between/{fecha1}/{fecha2}")
    Call<List<History>> getVaccinesBetween(@Path("fecha1")String fecha1, @Path("fecha2")String fecha2);

    @GET("history/numVac/{vaccine}")
    Call<Integer> getNumVaccines(@Path("vaccine")String vaccine);
}
