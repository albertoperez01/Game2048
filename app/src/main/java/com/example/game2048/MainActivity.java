package com.example.game2048;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.*;
import android.content.Intent;
import android.view.*;
import android.widget.Button;


public class MainActivity extends Activity {
    private Button continueGame, manageScore;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("EXIT").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startActivity(startMain);
                        finish();
                    }
                })
                               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {

                                   }
                               }).show();
            }catch (Exception e)
            {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        game();
        menuOnClick();

    }
    public void menuOnClick()
    {
        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, play2048.class);
                intent.putExtra("playgame", 3);
                startActivity(intent);
            }
        });

        manageScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                startActivity(intent);
            }
        });
    }
    public void game()
    {
        continueGame = (Button)findViewById(R.id.continueGame);
        manageScore = (Button)findViewById(R.id.manageScore);
    }


}
