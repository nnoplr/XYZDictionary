package com.novika.xyzdictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{

    Context context;
    FavoritesDB favoritesDB;
    ArrayList<Word> wordArrayList;

    public WordAdapter(ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflater
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.ViewHolder holder, int position) {

        String name = wordArrayList.get(position).getWordName();
        holder.tvWordName.setText(name);

        holder.frWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent "word" buat ditambahkan ke prefix di definitionnya
                Intent intent = new Intent(context, DefinitionActivity.class);
                intent.putExtra("Name", name);
                context.startActivity(intent);

            }
        });

        favoritesDB = new FavoritesDB(context);

        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                String url = "https://myawesomedictionary.herokuapp.com/words?q="+name;


                RequestQueue queue = Volley.newRequestQueue(context);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject(i);

                                        JSONArray arDef = jsonObject.getJSONArray("definitions");

                                        for (int j =0; j<arDef.length();j++){
                                            JSONObject jsonObject1 = arDef.getJSONObject(j);
                                            String image_url = jsonObject1.getString("image_url");
                                            String type = jsonObject1.getString("type");
                                            String definition = jsonObject1.getString("definition");

                                            Word word = new Word(name, definition,image_url, type);

                                            ArrayList<Word> checkArrayList = favoritesDB.getAFavorite(wordArrayList.get(position).getWordName());
                                            int check =0;

                                            for (int k=0; k<checkArrayList.size();k++){
                                                if (checkArrayList.get(k).definition.equals(word.definition)){
                                                    check++;
                                                }
                                            }

                                            if (check==0){
                                                favoritesDB.insertProduct(word);
                                                Toast.makeText(context,"Word saved to favorites",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(context,"Word has already been in favorites", Toast.LENGTH_SHORT).show();
                                            }

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

            }
        });
    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWordName;
        FrameLayout frWordList;
        Button btnSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWordName = itemView.findViewById(R.id.wordName);
            frWordList = itemView.findViewById(R.id.frWordList);
            btnSave = itemView.findViewById(R.id.btnSave);

        }
    }
}
