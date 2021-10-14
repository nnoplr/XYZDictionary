package com.novika.xyzdictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class FavoritesFragment extends Fragment {


    RecyclerView rvWordList;
    ArrayList<Word> wordArrayList;
    FavoritesDB favoritesDB;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab2, container, false);

         rvWordList = v.findViewById(R.id.rvWordList);
         insertData();
        return v;
    }

    public void insertData(){

        favoritesDB = new FavoritesDB(requireContext());
         wordArrayList= favoritesDB.getAllFavorites();

        WordDeleteAdapter wordDeleteAdapter = new WordDeleteAdapter(wordArrayList);

        rvWordList.setAdapter(wordDeleteAdapter);
        rvWordList.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    @Override
    public void onResume() {
        //Munculin data baru tiap resume
        super.onResume();
        insertData();

        Log.d("lifecycle","onResume");
    }

}