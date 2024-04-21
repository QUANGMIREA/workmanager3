package com.example.workmanager3;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FetchDogWorker extends Worker {

    public FetchDogWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            URL url = new URL("https://random.dog/woof.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                String jsonResponse = scanner.useDelimiter("\\A").next();
                scanner.close();

                Gson gson = new Gson();
                DogResponse response = gson.fromJson(jsonResponse, DogResponse.class);
                // Output data to be passed to activity
                return Result.success(new Data.Builder().putString("dogUrl", response.url).build());
            }
        } catch (IOException e) {
            return Result.failure();
        }
        return Result.failure();
    }

    private static class DogResponse {
        String url;
    }
}
