package network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import models.Request;
import retrofit2.Call;
import retrofit2.Response;

public class WorkerPaws extends Worker {

    public static final String INPUT_REQ_ID = "request_id";
    public WorkerPaws(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Funciona similar al SharedPreferences
        int reqId = getInputData().getInt(INPUT_REQ_ID, -1);
        if (reqId < 0) {
            return Result.failure();
        }
        
        RequestService requestService = Retro.getClient().create(RequestService.class);

        try {
            // Ver estado de la request
            Call<Request> getCall = requestService.getRequest(reqId);
            Response<Request> getResp = getCall.execute();
            if (!getResp.isSuccessful() || getResp.body() == null) {
                return Result.retry();
            }

            Request req = getResp.body();
            Log.i("WorkerPaws", "Estado de request " + reqId + " = " + req.getEstado());

            if (req.getEstado() == 0) {
                // Borrarla si estado sigue en 0
                Call<Void> delCall = requestService.deleteReq(reqId);
                Response<Void> delResp = delCall.execute();

                if (delResp.isSuccessful()) {
                    Log.i("WorkerPaws", "Request " + reqId + " borrada correctamente.");
                } else {
                    Log.e("WorkerPaws", "Error borrando request " + reqId +
                            ": HTTP " + delResp.code());
                    //Se vuelve a intentar borrarla
                    return Result.retry();
                }
            } else {
                Log.i("WorkerPaws", "Request " + reqId + " ya vista, no se borra.");
            }
            
            return Result.success();

        } catch (IOException e) {
            //Probable error de red o error inesperado
            return Result.retry();
        }
    }
    
}
