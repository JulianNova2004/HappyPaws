package network;
import java.util.List;

import models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
        @POST("user/save")
        Call<User> saveUser(@Body User user);

        @GET("user/search/{id}")
        Call<User> searchUser(@Path("id") int id);

        @DELETE("user/delete/{id}")
        Call<Void> deleteUser(@Path("id") int id);

        @POST("user/login")
        Call<User> login(@Body User user);

        @GET("user/getUsers")
        Call<List<User>> getAllUsers();

        //stats
        @GET("user/most")
        Call<List<User>> most();

}
