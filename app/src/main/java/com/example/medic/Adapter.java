package com.example.medic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.MovieScreen.MovieScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<mov> URLs = new ArrayList<>();

    public Adapter(Context context, ArrayList<mov> URLs){
        this.context = context;
        this.URLs = URLs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_view,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(URLs.get(position).getUrl()).into(holder.getImageView());
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieScreen.class);
                intent.putExtra("movieId",URLs.get(position).getMovieId() );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return URLs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.imageView5);

        }

        public ImageView getImageView() {
            return iv;
        }
    }
}
