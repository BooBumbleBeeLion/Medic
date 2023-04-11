package com.example.medic.Main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medic.Adapter;
import com.example.medic.MovieScreen.MovieScreen;
import com.example.medic.R;
import com.example.medic.mov;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<mov> recyclerURLs = new ArrayList<>();
    TabLayout tab;

    RecyclerView recyclerView;
    ImageView forreg, backg;
    TextView movie;
    int movieId;
    String backgroundImage, foregroundImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forreg = findViewById(R.id.imageView3);
        backg = findViewById(R.id.cover);
        movie = findViewById(R.id.textView4);
        tab = findViewById(R.id.tabLayout);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String s = tab.getText().toString();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                if(s.equals("В тренде"))
                {
                    new movies().execute("http://cinema.areas.su/movies?filter=inTrend");
                }
                if(s.equals("Новое"))
                {
                    new movies().execute("http://cinema.areas.su/movies?filter=new");
                }
                if(s.equals("Для Вас"))
                {
                    new movies().execute("http://cinema.areas.su/movies?filter=forMe");
                }

                //new movies().execute("http://cinema.areas.su/movies?filter=new");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MovieScreen.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });
        new mytask().execute("http://cinema.areas.su/movies/cover");
        new movies().execute("http://cinema.areas.su/movies?filter=new");

        recyclerView = findViewById(R.id.recyclerView);

        Adapter adapter = new Adapter(this, recyclerURLs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


    }

    public class mytask extends AsyncTask<String, Void, String> {

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
                    movieId = obj.getInt("movieId");
                    backgroundImage = obj.getString("backgroundImage");
                    foregroundImage = obj.getString("foregroundImage");
                    Picasso.get().load("http://cinema.areas.su/up/images/" + foregroundImage).into(forreg);
                    Picasso.get().load("http://cinema.areas.su/up/images/" + backgroundImage).into(backg);
                }
                catch (Exception e ){}


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Произошла ошибка!")
                        .setMessage(str);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(str);
        }
    }

    public class movies extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String answer = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                //connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5120);

                //JSONObject obj = new JSONObject();
                //obj.put("filter", strings[1]);

                //DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                //os.writeBytes(obj.toString());
                //os.flush();
                //os.close();

                connection.connect();
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] b = new byte[10000];
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

        String poster;

        @Override
        protected void onPostExecute(String str) {
            if (str.contains("movieId")) {
                try {
                    JSONArray arr = new JSONArray(str);
                    for(int i=0; i<arr.length();i++)
                    {
                        JSONObject obj = arr.getJSONObject(i);
                        int qwr = obj.getInt("movieId");
                        poster = obj.getString("poster");
                        recyclerURLs.add( new mov("http://cinema.areas.su/up/images/"+ poster,qwr) );
                    }
                    Adapter adapter = new Adapter(MainActivity.this, recyclerURLs);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                    //JSONObject obj = new JSONObject(str);
                    // = obj.getString("poster");
                    //Toast.makeText(MainActivity.this, poster, Toast.LENGTH_SHORT).show();

                }
                catch (Exception e ){}


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Произошла ошибка!")
                        .setMessage(str);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            super.onPostExecute(str);
        }
    }


}