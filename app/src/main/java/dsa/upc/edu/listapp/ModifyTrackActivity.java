package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.TrackRequest;
import dsa.upc.edu.listapp.github.TrackService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyTrackActivity extends AppCompatActivity {
    TextInputLayout txtSong, txtAuthor;
    Button btnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_track);

        txtSong = findViewById(R.id.txtSong1);
        txtAuthor = findViewById(R.id.txtAuthor1);
        btnModify = findViewById(R.id.btnModify);

        if (getIntent().hasExtra("track_title")) {
            Objects.requireNonNull(txtSong.getEditText()).setText(getIntent().getStringExtra("track_title"));
        }
        if (getIntent().hasExtra("track_singer")) {
            Objects.requireNonNull(txtAuthor.getEditText()).setText(getIntent().getStringExtra("track_singer"));
        }

        btnModify.setOnClickListener(v -> modifyTrack());
    }

    private void modifyTrack() {
        String trackId = getIntent().getStringExtra("track_id");
        String title = Objects.requireNonNull(txtSong.getEditText()).getText().toString();
        String singer = Objects.requireNonNull(txtAuthor.getEditText()).getText().toString();

        if (trackId == null || title.isEmpty() || singer.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        TrackRequest request = new TrackRequest(
                trackId,
                title,
                singer
        );

        TrackService api = API.getTrackService();

        api.updateTrack(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ModifyTrackActivity.this, "Track modified", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 404) {
                    Toast.makeText(ModifyTrackActivity.this, "Error: Track not found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyTrackActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ModifyTrackActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
