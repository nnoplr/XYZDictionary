package com.novika.xyzdictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DefinitionActivity extends AppCompatActivity {

    DefinitionAdapter definitionAdapter;
    ArrayList<Word> definitionArrayList = new ArrayList<>();
    ArrayList<Word> checkArrayList = new ArrayList<>();
    TextView tvNameOnDefinition;
    FavoritesDB favoritesDB;
    Word word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvDefinitionList = findViewById(R.id.rvDefinitionList);
        Button btnSaveInDefActivity = findViewById(R.id.btnSaveInDefActivity);
        tvNameOnDefinition = findViewById(R.id.tvNameOnDef);
        rvDefinitionList.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        //Buat ambil "word"-nya
        String name = intent.getStringExtra("Name");
        //Buat ksih tau dia dari explore atau Favorite
        int apiOrDB = intent.getIntExtra("APIorDB", 0);

//        Word w = new Word("ayam", "ayam", "ayam", "ayam");
//        definitionArrayList.add(w);

        tvNameOnDefinition.setText("Word: "+String.valueOf(name));

        //Kalau dari Explore:
        if (apiOrDB == 0) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "https://myawesomedictionary.herokuapp.com/words?q=" + name;

            rvDefinitionList.setLayoutManager(new LinearLayoutManager(this));
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    JSONArray arDef = jsonObject.getJSONArray("definitions");

                                    for (int j = 0; j < arDef.length(); j++) {
                                        JSONObject jsonObject1 = arDef.getJSONObject(j);
                                        String image_url = jsonObject1.getString("image_url");
                                        String type = jsonObject1.getString("type");
                                        String definition = jsonObject1.getString("definition");

                                        word = new Word(name, definition, image_url, type);
                                        definitionArrayList.add(word);
                                    }
//                                Toast.makeText(getApplicationContext(),String.valueOf(arDef),Toast.LENGTH_SHORT).show();

                                    definitionAdapter = new DefinitionAdapter(definitionArrayList);
                                    rvDefinitionList.setAdapter(definitionAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(jsonArrayRequest);
        }

        //Kalau dari Favorite:
        else if (apiOrDB == 1) {

            favoritesDB = new FavoritesDB(this);
            definitionArrayList = favoritesDB.getAFavorite(String.valueOf(name));

            DefinitionAdapter definitionAdapter = new DefinitionAdapter(definitionArrayList);
            rvDefinitionList.setAdapter(definitionAdapter);
            rvDefinitionList.setLayoutManager(new LinearLayoutManager(this));

        }


        btnSaveInDefActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kalau dia dari Explore trus mau save:
                if (apiOrDB == 0) {

                    favoritesDB = new FavoritesDB(getApplicationContext());
                    checkArrayList = favoritesDB.getAllFavorites();
                    int check = 0;

                    //Cek di DB ada "word" itu atau tdk. Kalau tdk ada (check==0), insert ke DB.
                    //Kalau ada, toast bahwa wordnya udh pernah disave.
                    for (int k = 0; k < checkArrayList.size(); k++) {
                        if (checkArrayList.get(k).getWordName().equals(name)) {
                            check++;
                        }
                    }

                    if (check == 0) {

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String url = "https://myawesomedictionary.herokuapp.com/words?q=" + name;

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        for (int i = 0; i < response.length(); i++) {
                                            try {
                                                JSONObject jsonObject = response.getJSONObject(i);

                                                JSONArray arDef = jsonObject.getJSONArray("definitions");

                                                for (int j = 0; j < arDef.length(); j++) {
                                                    JSONObject jsonObject1 = arDef.getJSONObject(j);
                                                    String image_url = jsonObject1.getString("image_url");
                                                    String type = jsonObject1.getString("type");
                                                    String definition = jsonObject1.getString("definition");

                                                    word = new Word(name, definition, image_url, type);
                                                    favoritesDB.insertProduct(word);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        requestQueue.add(jsonArrayRequest);

                        Toast.makeText(getApplicationContext(), "Word saved to favorites", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Word has already been in favorites", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Word has already been in favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}