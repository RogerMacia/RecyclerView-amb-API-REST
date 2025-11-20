package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.Track;
import dsa.upc.edu.listapp.github.TrackService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivAdd;

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = findViewById(R.id.my_swipe_refresh);
        ivAdd = findViewById(R.id.ivAdd);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        doApiCall(null);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        adapter.remove(viewHolder.getBindingAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(
                () -> doApiCall(swipeRefreshLayout)
        );

        ivAdd.setOnClickListener(
                v -> addTrack()
        );
    }

    private void doApiCall(final SwipeRefreshLayout mySwipeRefreshLayout) {
        TrackService trackService = API.getTrackService();
        Call<List<Track>> call = trackService.getTracks();

        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(@NonNull Call<List<Track>> call, @NonNull Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    adapter.setData(response.body());
                } else {
                    String msg = "Error in retrofit: " + response.code();
                    Log.e(TAG, msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                if (mySwipeRefreshLayout != null) mySwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Track>> call, @NonNull Throwable t) {
                if (mySwipeRefreshLayout != null) mySwipeRefreshLayout.setRefreshing(false);
                String msg = "Error in retrofit: " + t.getMessage();
                Log.e(TAG, msg, t);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addTrack() {
        Intent intent = new Intent(MainActivity.this, AddTrackActivity.class);
        startActivity(intent);
    }
}
