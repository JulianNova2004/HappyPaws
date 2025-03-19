package network;
import models.History;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface HistoryService {

    @POST("history/add/{pet_id}")
    Call<History> addHistory(@Body History history, @Path("pet_id") int pet_id);

}
