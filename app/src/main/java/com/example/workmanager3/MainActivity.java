package com.example.workmanager3;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.dogImageView);
        Button loadImageButton = findViewById(R.id.loadImageButton);

        loadImageButton.setOnClickListener(v -> {
            WorkRequest fetchDogRequest = new OneTimeWorkRequest.Builder(FetchDogWorker.class).build();
            WorkManager.getInstance(this).enqueue(fetchDogRequest);

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(fetchDogRequest.getId())
                    .observe(this, info -> {
                        if (info != null && info.getState() == WorkInfo.State.SUCCEEDED) {
                            String imageUrl = info.getOutputData().getString("dogUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Picasso.get().load(imageUrl).into(imageView);
                            }
                        }
                    });
        });
    }
}
