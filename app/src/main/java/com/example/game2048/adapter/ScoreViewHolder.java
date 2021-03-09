package com.example.game2048.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.game2048.R;


public class ScoreViewHolder extends RecyclerView.ViewHolder {

    public TextView userName, score;
    public ImageView deleteScore;
    public ImageView editScore;

    public ScoreViewHolder(View itemView) {
        super(itemView);
        userName = (TextView)itemView.findViewById(R.id.score_name);
        score = (TextView)itemView.findViewById(R.id.score);
        deleteScore = (ImageView)itemView.findViewById(R.id.delete_score);
        editScore = (ImageView)itemView.findViewById(R.id.edit_score);
    }
}
