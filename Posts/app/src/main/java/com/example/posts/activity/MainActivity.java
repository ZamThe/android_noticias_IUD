package com.example.posts.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.posts.R;
import com.example.posts.model.Noticia;
import com.example.posts.my_interface.GetNoticiaDataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private ListView list;
    private ArrayList<String> titles = new ArrayList<>();
    private static final String URL_BASE = "https://jsonplaceholder.typicode.com";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializa el ListView y el ArrayAdapter
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        list = findViewById(R.id.list);
        list.setAdapter(arrayAdapter);

        // Llama al método para obtener las noticias
        getPosts();

        // Manejo de los márgenes de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getPosts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetNoticiaDataService postService = retrofit.create(GetNoticiaDataService.class);
        Call<List<Noticia>> call = postService.getPost();
        call.enqueue(new Callback<List<Noticia>>() {
            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Noticia post : response.body()) {
                        titles.add(post.getTitle());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {
                // Manejar el error aquí
            }
        });
    }
}
