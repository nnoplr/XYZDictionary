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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WordDeleteAdapter extends RecyclerView.Adapter<WordDeleteAdapter.ViewHolder>{

    Context context;
    ArrayList<Word> wordArrayList;
    FavoritesDB favoritesDB;

    public WordDeleteAdapter(ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
    }

    @NonNull
    @Override
    public WordDeleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflater
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordDeleteAdapter.ViewHolder holder, int position) {

        favoritesDB = new FavoritesDB(context);
        String name = wordArrayList.get(position).getWordName();
        holder.tvWordName.setText(name);

        holder.frWordList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DefinitionActivity.class);

                intent.putExtra("Name", name); //Kirim "word"
                //Ksih tanda bahwa ini dari WordDeleteAdapter
                intent.putExtra("APIorDB", 1);

                context.startActivity(intent);

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordArrayList!=null){
                    favoritesDB.delete(wordArrayList.get(position).getWordName());
                    removeRecycleview(holder.getAdapterPosition());
                }
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
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWordName = itemView.findViewById(R.id.wordName);
            frWordList = itemView.findViewById(R.id.frWordList);
            btnDelete = itemView.findViewById(R.id.btnSave);
            //Ubah text buttonnya jdi "Delete"
            btnDelete.setText("Delete");

        }

    }

    private void removeRecycleview(int position) {
        wordArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wordArrayList.size());
    }


}
