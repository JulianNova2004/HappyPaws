package network;

import models.Admin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AdminService {

    @POST("admin/login")
    Call<Admin> logIn(@Body Admin admin);
}
