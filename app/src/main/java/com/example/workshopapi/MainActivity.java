package com.example.workshopapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.workshopapi.Adapters.FilmAdapter;
import com.example.workshopapi.Models.Film;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FilmAdapter adapter;
    List<Film> filmList = new ArrayList<>();
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //data();
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //adapter = new FilmAdapter(this, filmList);//En caso de no tener conexión, sin la API
        //recyclerView.setAdapter(adapter);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://swapi.dev/api/films/?format=json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //Toast.makeText(getApplicationContext(),"No se pudo, ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                JsonArray jsonArray;
                JsonObject jsonObject = new Gson().fromJson(json,(Type) JsonObject.class);
                jsonArray = jsonObject.getAsJsonArray("results");

                Gson gson = new GsonBuilder().create();
                Type list = new TypeToken<List<Film>>(){}.getType();
                filmList = gson.fromJson(jsonArray.toString(),list);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new FilmAdapter(getApplicationContext(),filmList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });

        update();
    }

    //Se muestran datos de forma manual a través de dicha función
    public void data(){
        Film film = new Film();
        film.setTitle("titulo");
        film.setEpisode_id(1);
        film.setOpening("Openning");
        film.setDirector("Director");
        film.setProducer("producer");
        film.setUrl("url");
        film.setCreated("created");
        film.setEdited("edited");

        filmList.add(film);
        film = new Film();
    }

    public void update(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new FilmAdapter(getApplicationContext(),filmList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}