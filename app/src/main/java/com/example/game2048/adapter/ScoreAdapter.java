package com.example.game2048.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game2048.R;
import com.example.game2048.Scores;
import com.example.game2048.database.SqliteDatabase;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Scores> listScores;
    private ArrayList<Scores> mArrayList;
    private SqliteDatabase mDatabase;

    public ScoreAdapter(Context context, ArrayList<Scores> listScores) {
        this.context = context;
        this.listScores = listScores;
        this.mArrayList= listScores;
        mDatabase = new SqliteDatabase(context);
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_list_layout, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        final Scores scores = listScores.get(position);

        holder.userName.setText(scores.getUserName());
        holder.score.setText(scores.getScore());

        holder.editScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTaskDialog(scores);
            }
        });

        holder.deleteScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database

                mDatabase.deleteScore(scores.getId());

                //refresh the activity page.
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    listScores = mArrayList;
                } else {

                    ArrayList<Scores> filteredList = new ArrayList<>();

                    for (Scores scores : mArrayList) {

                        if (scores.getUserName().toLowerCase().contains(charString)) {

                            filteredList.add(scores);
                        }
                    }

                    listScores = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listScores;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listScores = (ArrayList<Scores>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return listScores.size();
    }


    private void editTaskDialog(final Scores scores){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_score_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText scoreField = (EditText)subView.findViewById(R.id.enter_score);

        if(scores != null){
            nameField.setText(scores.getUserName());
            scoreField.setText(String.valueOf(scores.getScore()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("EDIT SCORE");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT SCORE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String score = scoreField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    mDatabase.updateScores(new Scores(scores.getId(), name, score));
                    //refresh the activity
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}
