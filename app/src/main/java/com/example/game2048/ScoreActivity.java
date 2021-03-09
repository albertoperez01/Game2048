package com.example.game2048;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.game2048.adapter.ScoreAdapter;
import com.example.game2048.database.SqliteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreActivity extends AppCompatActivity {

    private static final String TAG = ScoreActivity.class.getSimpleName();

    private SqliteDatabase mDatabase;
    private ArrayList<Scores> allScores =new ArrayList<>();
    private ScoreAdapter mAdapter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_main);

        FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);

        RecyclerView scoreView = (RecyclerView)findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        scoreView.setLayoutManager(linearLayoutManager);
        scoreView.setHasFixedSize(true);
        mDatabase = new SqliteDatabase(this);
        allScores = mDatabase.listScores();

        if(allScores.size() > 0){
            scoreView.setVisibility(View.VISIBLE);
            mAdapter = new ScoreAdapter(this, allScores);
            scoreView.setAdapter(mAdapter);

        }else {
            scoreView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no score in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog();
            }
        });
    }

    private void addTaskDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_score_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_name);
        final EditText noField = (EditText)subView.findViewById(R.id.enter_score);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ADD NEW SCORE");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD SCORE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String score = noField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(ScoreActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    Scores newScore = new Scores(name, score);
                    mDatabase.addScores(newScore);

                    finish();
                    startActivity(getIntent());
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ScoreActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            mDatabase.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_aToz:
                Collections.sort(allScores, Scores.NameUserAZComparator);
                Toast.makeText(ScoreActivity.this, "Sort A to Z", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_zToa:
                Collections.sort(allScores, Scores.NameUserZAComparator);
                Toast.makeText(ScoreActivity.this, "Sort Z to A", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter!=null)
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
