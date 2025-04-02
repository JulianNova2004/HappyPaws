package network;

import models.Consulta;
import models.Recordatorio;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecordatoryService {

    @POST("recordatory/add/{id}")
    Call<Recordatorio> add(@Body Recordatorio recordatorio, @Path("id") int pet_id);

}
