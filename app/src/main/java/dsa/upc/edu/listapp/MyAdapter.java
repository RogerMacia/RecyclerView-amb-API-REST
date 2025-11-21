package dsa.upc.edu.listapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import dsa.upc.edu.listapp.github.Track;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Track> values;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView icon;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = v.findViewById(R.id.firstLine);
            txtFooter = v.findViewById(R.id.secondLine);
            icon = v.findViewById(R.id.icon);
        }
    }

    public void setData(List<Track> myDataset) {
        values = myDataset;
        notifyDataSetChanged();
    }

    public void add(int position, Track item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    public Track getItem(int position) {
        return values.get(position);
    }

    public MyAdapter() {
        values = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Track t = values.get(position);
        final String title = t.getTitle();
        holder.txtHeader.setText(title);

        holder.txtFooter.setText("Singer: " + t.getSinger());

        Glide.with(holder.icon.getContext())
                .load("http://147.83.7.203:8080/img/logodsa.png")
                .into(holder.icon);

        holder.layout.setOnClickListener(
            v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ModifyTrackActivity.class);
                intent.putExtra("track_id", t.getId());
                intent.putExtra("track_title", t.getTitle());
                intent.putExtra("track_singer", t.getSinger());
                context.startActivity(intent);
            }
        );
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
