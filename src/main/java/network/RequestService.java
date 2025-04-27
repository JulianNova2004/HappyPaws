package network;

import java.util.List;

import models.Request;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestService {

    @POST("request/create")
    Call<Void> Request(@Body Request request);

}
