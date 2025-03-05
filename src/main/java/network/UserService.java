package network;
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
        Call<User> login(@Path("email") String email, @Path("password") String password);


}
