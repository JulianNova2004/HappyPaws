package network;

import java.util.List;

import models.Consulta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ConsultationService {

    @POST("consulta/add/{id}")
    Call<Consulta> addConsulta(@Body Consulta consulta, @Path("id") int id);

    @GET("consulta/consultas/{id}")
    Call <List<Consulta>> getConsultas(@Path("id") int id);

}
