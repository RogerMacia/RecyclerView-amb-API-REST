package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.RandomUtils;
import dsa.upc.edu.listapp.github.Track;
import dsa.upc.edu.listapp.github.TrackRequest;
import dsa.upc.edu.listapp.github.TrackService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTrackActivity extends AppCompatActivity {
    private TextInputLayout txtSongName, txtAuthor;
    private Button btnAdd;

    private TrackService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        txtSongName = findViewById(R.id.txtSongName);
        txtAuthor = findViewById(R.id.txtAuthor);
        btnAdd = findViewById(R.id.btnAdd);


        btnAdd.setOnClickListener(v -> addTrack());
    }

    private void addTrack() {
        TrackRequest request = new TrackRequest(
                RandomUtils.getId(),
                txtSongName.getEditText().getText().toString(),
                txtAuthor.getEditText().getText().toString()
        );

        TrackService api = API.getTrackService();

        api.newTrack(request).enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddTrackActivity.this, "Track added", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 500){
                    Toast.makeText(AddTrackActivity.this, "Error: Invalid parameters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Toast.makeText(AddTrackActivity.this, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
