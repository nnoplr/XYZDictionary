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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class ExploreFragment extends Fragment {

    ArrayList<Word> wordArrayList=new ArrayList<>();
    EditText etSearch;
    ImageButton ibSearch;

    public ExploreFragment() {
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab1, container, false);

        RecyclerView rvWordList = v.findViewById(R.id.rvWordList);
        rvWordList.setLayoutManager(new LinearLayoutManager(this.getContext()));


        etSearch = v.findViewById(R.id.etSearch);
        ibSearch = v.findViewById(R.id.ibSearch);

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etSearch.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Type a word(s) first", Toast.LENGTH_SHORT).show();
                }

                else{
                    wordArrayList.clear();
                    String name = etSearch.getText().toString();
                    RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                    String url = "https://myawesomedictionary.herokuapp.com/words?q="+name;

                    //Mau ambil array of JSON
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                            response -> {

                                //Kalau gk ada respon, ksih message
                                if (response.length()==0){
                                    Toast.makeText(getContext(), "No word matched", Toast.LENGTH_SHORT).show();
                                }
                                //Kalau ada respon, baru loop
                                else{
                                    for (int i = 0; i<response.length(); i++){
                                        try {
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            Word word = new Word(jsonObject.getString("word"));
                                            wordArrayList.add(word);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                //Tetap ganti adapternya walaupun gk ada respond agar result yg sebelumnya tidak kelihatan lagi
                                rvWordList.setAdapter(new WordAdapter(wordArrayList));
//                    wordAdapter.notifyDataSetChanged();
                            }, error -> {
                        Log.e("infoError", error.toString());
                    });

                    requestQueue.add(jsonArrayRequest);
                }

            }
        });

        return v;
    }

}