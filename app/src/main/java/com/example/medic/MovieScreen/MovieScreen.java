package com.example.medic.MovieScreen;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.ImageAdapter;
import com.example.medic.R;
import com.example.medic.episode;
import com.example.medic.movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieScreen extends AppCompatActivity {

    //Button btn;
    int movieId;
    VideoView videoView;
    RecyclerView recyclerView;
    TextView movieName, discr;
    ImageView cover, back, chat, age;
    movie movieToShow = new movie();
    ArrayList<episode> episodes = new ArrayList<episode>();
    int flag = 1;
    int flagEpi = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_screen);
        cover = findViewById(R.id.cover);
        back = findViewById(R.id.back);
        chat = findViewById(R.id.imageView8);
        age = findViewById(R.id.age);
        videoView = findViewById(R.id.videoView);
        movieName = findViewById(R.id.movieName);
        recyclerView = findViewById(R.id.recyclerView2);
        discr = findViewById(R.id.discr);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        movieId = getIntent().getIntExtra("movieId",0);
        //movieId = 1;
        new getMovie().execute("http://cinema.areas.su/movies/" + movieId);
        new getEpisodes().execute("http://cinema.areas.su/movies/" + movieId + "/episodes");





        //while(flagEpi != 0)
        //{
        //
        //}

        //btn = findViewById(R.id.button);

       // btn.setOnClickListener(new View.OnClickListener() {
        //    @Override
       //    public void onClick(View v) {
          //      finish();
          //  }
       // });
    }

    public class getMovie extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String answer = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);

                connection.connect();
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] b = new byte[5120];
                int readBytes;
                while ((readBytes = is.read(b)) != -1) {
                    baos.write(b, 0, readBytes);
                }

                answer = new String(baos.toByteArray(), "UTF-8");

                connection.disconnect();
            } catch (Exception e) {
                answer = e.getMessage();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String str) {
            if (str.contains("movieId")) {
                try {
                    JSONObject obj = new JSONObject(str);
                    String qw = obj.toString();
                    movieToShow.setMovieId(obj.getInt("movieId"));
                    movieToShow.setName(obj.getString("name"));
                    movieToShow.setDescription(obj.getString("description"));
                    movieToShow.setAge(obj.getInt("age"));

                    JSONArray objImages = obj.getJSONArray("images");
                    ArrayList<String> s = new ArrayList<>();
                    for(int i =0; i < objImages.length(); i++)
                    {
                        s.add("http://cinema.areas.su/up/images/" + objImages.getString(i));
                    }
                    movieToShow.setImages(s);

                    movieToShow.setPoster("http://cinema.areas.su/up/images/" + obj.getString("poster"));
                    movieToShow.setMovieId(obj.getInt("movieId")); //tags

                    JSONArray objTags = obj.getJSONArray("tags");
                    s.clear();
                    for(int i =0; i < objTags.length(); i++)
                    {
                        s.add(objTags.getString(i));
                    }
                    movieToShow.setTags(s);


                    Picasso.get().load(movieToShow.getPoster()).into(cover);
                    //URI uri = new URI(movieToShow.);
                    //videoView.setVideoURI();
                    //
                    movieName.setText(movieToShow.getName());
                    discr.setText(movieToShow.getDescription());

                    if(movieToShow.getAge() == 18)
                        age.setBackgroundResource(R.drawable.r);
                    else if (movieToShow.getAge() == 16)
                        age.setBackgroundResource(R.drawable.ee);
                    else if (movieToShow.getAge() == 12)
                        age.setBackgroundResource(R.drawable.w);
                    else if (movieToShow.getAge() == 616)
                        age.setBackgroundResource(R.drawable.q);



                }
                catch (Exception e ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MovieScreen.this);
                    builder.setTitle("Произошла ошибка!")
                            .setMessage(e.getMessage());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieScreen.this);
                builder.setTitle("Произошла ошибка!")
                        .setMessage(str);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(str);
        }
    }

    public class getEpisodes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String answer = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);

                connection.connect();
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] b = new byte[5120];
                int readBytes;
                while ((readBytes = is.read(b)) != -1) {
                    baos.write(b, 0, readBytes);
                }

                answer = new String(baos.toByteArray(), "UTF-8");

                connection.disconnect();
            } catch (Exception e) {
                answer = e.getMessage();
            }
            return answer;
        }

        episode e = new episode();

        @Override
        protected void onPostExecute(String str) {
            if (str.contains("episodeId")) {
                try {
                    JSONArray objects = new JSONArray(str);
                    for(int q = 0; q< objects.length(); q++) {
                        JSONObject obj = objects.getJSONObject(q);
                        e.setEpisodeId(obj.getInt("episodeId"));
                        e.setName(obj.getString("name"));
                        e.setDescription(obj.getString("description"));
                        e.setDirector(obj.getString("director"));

                        JSONArray objstars = obj.getJSONArray("stars");
                        ArrayList<String> s = new ArrayList<>();
                        for (int i = 0; i < objstars.length(); i++) {
                            s.add(objstars.getString(i));
                        }
                        e.setStars(s);

                        e.setYear(obj.getInt("year"));

                        JSONArray objimages = obj.getJSONArray("images");
                        s.clear();
                        for (int i = 0; i < objimages.length(); i++) {
                            s.add(objimages.getString(i));
                        }
                        e.setImages(s);

                        e.setRuntime(obj.getInt("runtime"));
                        e.setPreview("http://cinema.areas.su/up/video/" + obj.getString("preview"));
                        e.setMoviesId(obj.getInt("moviesId"));
                        episodes.add(e);
                    }





                        ImageAdapter adapter = new ImageAdapter(MovieScreen.this, episodes.get(0).getImages());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MovieScreen.this, RecyclerView.HORIZONTAL, false));

                    videoView.setVideoURI(Uri.parse(episodes.get(0).getPreview()));
                    videoView.start();

                }
                catch (Exception e ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MovieScreen.this);
                    builder.setTitle("Произошла ошибка!")
                            .setMessage(e.getMessage());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


            } else {
                flag = 0;
                //videoView.setVisibility(View.INVISIBLE);
                //videoView.setEnabled(false);
            }
            super.onPostExecute(str);
        }
    }


}