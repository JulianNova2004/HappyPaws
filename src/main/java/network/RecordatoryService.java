package network;

import java.util.List;

import models.Consulta;
import models.Pet;
import models.Recordatorio;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecordatoryService {

    @POST("recordatory/add/{id}")
    Call<Recordatorio> add(@Body Recordatorio recordatorio, @Path("id") int pet_id);

    @GET("recordatory/recs/{user_id}")
    Call<List<Recordatorio>> verRecs(@Path("user_id")int user_id);

    @GET("recordatory/allRecs")
    Call<List<Recordatorio>> allRecs();

    @PATCH("recordatory/update/{rec_id}")
    Call<Recordatorio> updateRec(@Body Recordatorio recordatorio, @Path("rec_id")int rec_id);

    //confirmar
    @GET("recordatory/rec/{rec_id}")
    Call<Recordatorio> getRec(@Path("rec_id")int rec_id);

    @DELETE("recordatory/del/{rec_id}")
    Call<Recordatorio> deleteRec(@Path("rec_id") int rec_id);
}
