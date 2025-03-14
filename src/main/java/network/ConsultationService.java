package network;

import java.util.List;

import models.Consultation;
import models.Pet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface ConsultationService {

    @POST("consulta/add/{id}")
    Call<Void> addConsultation(@Body Consultation consultation, @Path("id") int id);

}
