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
public interface ConsultationService {

    @POST("consulta/add/{id}")
    Call<Consulta> addConsulta(@Body Consulta consulta, @Path("id") int id);

    @GET("consulta/consulta/{id}")
    Call <List<Consulta>> getConsultasByPetId(@Path("id") int id);

    @GET("consulta/consultas")
    Call<List<Consulta>> getConsultas();

    @GET("consulta/find/{con_id}")
    Call<Consulta> getConsulta(@Path("con_id")int con_id);

    //@GET("consulta/consultaU/{user_id}")
    //Call<List<Consulta>> getConsultaU(@Path("user_id")int user_id);

    @PATCH("consulta/update/{con_id}")
    Call<Consulta> updateConsulta(@Body Consulta consulta, @Path("con_id")int con_id);

    @DELETE("consulta/delete/{con_id}")
    Call<Consulta> deleteCons(@Path("con_id") int con_id);

    //Stats
    @GET("consulta/freq")
    Call<Pet> mostFreq();

    @GET("consulta/state/{estado}")
    Call<List<Pet>> findByEstado(@Path("estado") String estado);

    @GET("consulta/contarV/{pet_id}")
    Call<Integer> countVisitsPetId(@Path("pet_id") int pet_id);
}
