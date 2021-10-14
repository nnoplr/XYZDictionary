package com.novika.xyzdictionary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.ViewHolder>{

    Context context;
    ArrayList<Word> wordArrayList;

    public DefinitionAdapter(ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }

    @NonNull
    @Override
    public DefinitionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflater
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.definition_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DefinitionAdapter.ViewHolder holder, int position) {

        holder.wordType.setText("Type: "+wordArrayList.get(position).getType());
        holder.wordDef.setText(wordArrayList.get(position).getDefinition());

        if (!wordArrayList.get(position).getUrl().equals("null")){
            Picasso.get().load(wordArrayList.get(position).getUrl()).into(holder.ivGambar);
        }else {
            //Kalau gk ada gambar, ambil gambar dari link ini
            Picasso.get().load("https://i.ibb.co/BTRzJfh/1024px-No-image-available-svg.png").into(holder.ivGambar);
        }

    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView wordType, wordDef;
        ImageView ivGambar;
        FrameLayout frDefinitionList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wordType = itemView.findViewById(R.id.wordType);
            wordDef = itemView.findViewById(R.id.wordDef);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            frDefinitionList = itemView.findViewById(R.id.frDefinitionList);

        }
    }
}
