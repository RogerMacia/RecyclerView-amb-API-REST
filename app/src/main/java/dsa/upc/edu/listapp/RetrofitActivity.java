package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.Track;
import dsa.upc.edu.listapp.github.TrackService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        Button button = findViewById(R.id.button);
        final TextView textView = findViewById(R.id.textView);

        button.setOnClickListener(view -> {
            TrackService trackService = API.getTrackService();
            Call<List<Track>> call = trackService.getTracks();

            call.enqueue(new Callback<List<Track>>() {
                @Override
                public void onResponse(@NonNull Call<List<Track>> call, @NonNull Response<List<Track>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        textView.setText(response.body().toString());
                    } else {
                        textView.setText("Response was not successful or body was null.");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Track>> call, @NonNull Throwable t) {
                    textView.setText("Something went wrong: " + t.getMessage());
                }
            });
        });
    }
}
