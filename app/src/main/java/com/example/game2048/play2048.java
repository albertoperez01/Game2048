package com.example.game2048;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class play2048 extends Activity {
    private ImageView box11, box12, box13, box14;
    private ImageView box21, box22, box23, box24;
    private ImageView box31, box32, box33, box34;
    private ImageView box41, box42, box43, box44;
    Intent context;
    private TextView text11, text12, text13, text14;
    private TextView text21, text22, text23, text24;
    private TextView text31, text32, text33, text34;
    private TextView text41, text42, text43, text44;
    private LinearLayout linearLayout;
    private int scores = 0;
    Button undo, reset;
    private int matrix[][], secondMatrix[][];
    private TextView score, best;
    private Database database;
    private int newScore = 0;

    @Override
    public void onStop() {
        saveProgress();
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Menu").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveProgress();
                        Intent startMain = new Intent(play2048.this, MainActivity.class);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startActivity(startMain);
                        finish();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).show();
            } catch (Exception e) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play2048);
        game();
        context = getIntent();
        try {
            if (context.getIntExtra("playgame", 0) == 3) continueGame();
            else play();
        } catch (Exception e) {
            play();
        }
        final GestureDetector gestureDetector = new GestureDetector(this, new gestureMovement());
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    public void saveProgress() {
        database = new Database(this);
        database.setSaveState(matrix);
        if (Integer.valueOf(database.getBest2048(this)) < scores) {
            database.setBest2048(scores);
        }
        database.setScore2048(scores);
        database.close();
    }

    public void resetGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Reset game").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                play();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();
    }

    public void continueGame() {
        database = new Database(this);
        best.setText(database.getBest2048(this));
        scores = Integer.valueOf(database.getScore2048(this));
        matrix = database.getSaveState(this);
        database.close();
        setSecondMatrix();
        set2048Layout();
    }

    public void play() {
        database = new Database(this);
        if (Integer.valueOf(database.getBest2048(this)) < scores) {
            database.setBest2048(scores);
        }
        best.setText(database.getBest2048(this));
        database.close();
        scores = 0;
        for (int i = 0; i < 25; i++) matrix[i / 5][i % 5] = 0;
        randomNumber();
        randomNumber();
        setSecondMatrix();
        set2048Layout();
    }

    void game() {
        matrix = new int[5][5];
        secondMatrix = new int[5][5];
        score = (TextView) findViewById(R.id.score);
        best = (TextView) findViewById(R.id.best);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        box11 = (ImageView) findViewById(R.id.box11);
        box12 = (ImageView) findViewById(R.id.box12);
        box13 = (ImageView) findViewById(R.id.box13);
        box14 = (ImageView) findViewById(R.id.box14);

        box21 = (ImageView) findViewById(R.id.box21);
        box22 = (ImageView) findViewById(R.id.box22);
        box23 = (ImageView) findViewById(R.id.box23);
        box24 = (ImageView) findViewById(R.id.box24);

        box31 = (ImageView) findViewById(R.id.box31);
        box32 = (ImageView) findViewById(R.id.box32);
        box33 = (ImageView) findViewById(R.id.box33);
        box34 = (ImageView) findViewById(R.id.box34);

        box41 = (ImageView) findViewById(R.id.box41);
        box42 = (ImageView) findViewById(R.id.box42);
        box43 = (ImageView) findViewById(R.id.box43);
        box44 = (ImageView) findViewById(R.id.box44);


        text11 = (TextView) findViewById(R.id.text11);
        text12 = (TextView) findViewById(R.id.text12);
        text13 = (TextView) findViewById(R.id.text13);
        text14 = (TextView) findViewById(R.id.text14);


        text21 = (TextView) findViewById(R.id.text21);
        text22 = (TextView) findViewById(R.id.text22);
        text23 = (TextView) findViewById(R.id.text23);
        text24 = (TextView) findViewById(R.id.text24);

        text31 = (TextView) findViewById(R.id.text31);
        text32 = (TextView) findViewById(R.id.text32);
        text33 = (TextView) findViewById(R.id.text33);
        text34 = (TextView) findViewById(R.id.text34);


        text41 = (TextView) findViewById(R.id.text41);
        text42 = (TextView) findViewById(R.id.text42);
        text43 = (TextView) findViewById(R.id.text43);
        text44 = (TextView) findViewById(R.id.text44);

        undo = (Button) findViewById(R.id.undo);
        reset = (Button) findViewById(R.id.reset);
    }


    public void undo() {
        boolean check = true;
        for (int i = 1; i < 25; i++) {
            if (matrix[i / 5][i % 5] != secondMatrix[i / 5][i % 5]) {
                scores -= (secondMatrix[i / 5][i % 5] + matrix[i / 5][i % 5]);
                check = false;
                break;
            }
        }
        if (check == true) {
            Toast.makeText(this, "You can't use the undo button now", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 1; i < 25; i++) matrix[i / 5][i % 5] = secondMatrix[i / 5][i % 5];
        set2048Layout();
    }

    public void set2048Layout() {
        score.setText(String.valueOf(scores));
        box11.setImageResource(getBackground(matrix[1][1]));
        box12.setImageResource(getBackground(matrix[1][2]));
        box13.setImageResource(getBackground(matrix[1][3]));
        box14.setImageResource(getBackground(matrix[1][4]));

        box21.setImageResource(getBackground(matrix[2][1]));
        box22.setImageResource(getBackground(matrix[2][2]));
        box23.setImageResource(getBackground(matrix[2][3]));
        box24.setImageResource(getBackground(matrix[2][4]));

        box31.setImageResource(getBackground(matrix[3][1]));
        box32.setImageResource(getBackground(matrix[3][2]));
        box33.setImageResource(getBackground(matrix[3][3]));
        box34.setImageResource(getBackground(matrix[3][4]));

        box41.setImageResource(getBackground(matrix[4][1]));
        box42.setImageResource(getBackground(matrix[4][2]));
        box43.setImageResource(getBackground(matrix[4][3]));
        box44.setImageResource(getBackground(matrix[4][4]));
        String matrixS[][] = new String[5][5];
        for (int i = 0; i < 25; i++) {
            if (matrix[i / 5][i % 5] == 0) matrixS[i / 5][i % 5] = "";
            else matrixS[i / 5][i % 5] = String.valueOf(matrix[i / 5][i % 5]);
        }
        setSizetext();
        text11.setText(matrixS[1][1]);
        text12.setText(matrixS[1][2]);
        text13.setText(matrixS[1][3]);
        text14.setText(matrixS[1][4]);
        text21.setText(matrixS[2][1]);
        text22.setText(matrixS[2][2]);
        text23.setText(matrixS[2][3]);
        text24.setText(matrixS[2][4]);
        text31.setText(matrixS[3][1]);
        text32.setText(matrixS[3][2]);
        text33.setText(matrixS[3][3]);
        text34.setText(matrixS[3][4]);
        text41.setText(matrixS[4][1]);
        text42.setText(matrixS[4][2]);
        text43.setText(matrixS[4][3]);
        text44.setText(matrixS[4][4]);
    }

    public void setSizetext() {
        if (matrix[1][1] <= 512) text11.setTextSize(35);
        else text11.setTextSize(25);
        if (matrix[2][1] <= 512) text21.setTextSize(35);
        else text21.setTextSize(25);
        if (matrix[3][1] <= 512) text31.setTextSize(35);
        else text31.setTextSize(25);
        if (matrix[4][1] <= 512) text41.setTextSize(35);
        else text41.setTextSize(25);

        if (matrix[1][2] <= 512) text12.setTextSize(35);
        else text12.setTextSize(25);
        if (matrix[2][2] <= 512) text22.setTextSize(35);
        else text22.setTextSize(25);
        if (matrix[3][2] <= 512) text32.setTextSize(35);
        else text32.setTextSize(25);
        if (matrix[4][2] <= 512) text42.setTextSize(35);
        else text42.setTextSize(25);

        if (matrix[1][3] <= 512) text13.setTextSize(35);
        else text13.setTextSize(25);
        if (matrix[2][3] <= 512) text23.setTextSize(35);
        else text23.setTextSize(25);
        if (matrix[3][3] <= 512) text33.setTextSize(35);
        else text33.setTextSize(25);
        if (matrix[4][3] <= 512) text43.setTextSize(35);
        else text43.setTextSize(25);


        if (matrix[1][4] <= 512) text14.setTextSize(35);
        else text14.setTextSize(25);
        if (matrix[2][4] <= 512) text24.setTextSize(35);
        else text24.setTextSize(25);
        if (matrix[3][4] <= 512) text34.setTextSize(35);
        else text34.setTextSize(25);
        if (matrix[4][4] <= 512) text44.setTextSize(35);
        else text44.setTextSize(25);

    }

    public int getBackground(int n) {
        if (n == 0) return R.drawable.box0;
        switch (n % 2048) {
            case 2:
                return R.drawable.box2;
            case 4:
                return R.drawable.box4;
            case 8:
                return R.drawable.box8;
            case 16:
                return R.drawable.box16;
            case 32:
                return R.drawable.box32;
            case 64:
                return R.drawable.box64;
            case 128:
                return R.drawable.box128;
            case 256:
                return R.drawable.box256;
            case 512:
                return R.drawable.box512;
            case 1024:
                return R.drawable.box1024;
            case 0:
                return R.drawable.box2048;
        }
        return 0;
    }

    class gestureMovement extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean check = false;
            setSecondMatrix();
            if (e1.getX() - e2.getX() > 100 && Math.abs(e1.getY() - e2.getY()) < Math.abs(e1.getX() - e2.getX()) && Math.abs(velocityX) > 100) {
                for (int i = 1; i < 5; i++) {
                    for (int j = 1; j < 4; j++) {
                        if (matrix[i][j] != 0) {
                            for (int k = j + 1; k < 5; k++) {
                                if (matrix[i][k] == matrix[i][j]) {
                                    check = true;
                                    matrix[i][j] += matrix[i][k];
                                    scores += matrix[i][j];
                                    matrix[i][k] = 0;
                                    j = k;
                                    break;
                                } else if (matrix[i][k] != 0) break;
                            }
                        }
                    }
                    for (int j = 1; j < 4; j++) {
                        if (matrix[i][j] == 0) {
                            for (int k = j + 1; k < 5; k++) {
                                if (matrix[i][k] != 0) {
                                    check = true;
                                    matrix[i][j] = matrix[i][k];
                                    matrix[i][k] = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else if (e2.getX() - e1.getX() > 100 && Math.abs(e1.getY() - e2.getY()) < Math.abs(e1.getX() - e2.getX()) && Math.abs(velocityX) > 100) {
                for (int i = 1; i < 5; i++) {
                    for (int j = 4; j > 1; j--) {

                        if (matrix[i][j] != 0) {
                            for (int k = j - 1; k > 0; k--) {
                                if (matrix[i][k] == matrix[i][j]) {
                                    check = true;
                                    matrix[i][j] += matrix[i][k];
                                    scores += matrix[i][j];
                                    matrix[i][k] = 0;
                                    j = k;
                                    break;
                                } else if (matrix[i][k] != 0) break;
                            }
                        }
                    }
                    for (int j = 4; j > 1; j--) {
                        if (matrix[i][j] == 0) {
                            for (int k = j - 1; k > 0; k--) {
                                if (matrix[i][k] != 0) {
                                    check = true;
                                    matrix[i][j] = matrix[i][k];
                                    matrix[i][k] = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else if (e1.getY() - e2.getY() > 100 && Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY()) && Math.abs(velocityY) > 100) {
                for (int i = 1; i < 5; i++) {
                    for (int j = 1; j < 4; j++) {
                        if (matrix[j][i] != 0) {
                            for (int k = j + 1; k < 5; k++) {
                                if (matrix[k][i] == matrix[j][i]) {
                                    check = true;
                                    matrix[j][i] += matrix[k][i];
                                    scores += matrix[j][i];
                                    matrix[k][i] = 0;
                                    j = k;
                                    break;
                                }
                                if (matrix[k][i] != 0) break;
                            }
                        }
                    }
                    for (int j = 1; j < 4; j++) {
                        if (matrix[j][i] == 0) {
                            for (int k = j + 1; k < 5; k++) {
                                if (matrix[k][i] != 0) {
                                    check = true;
                                    matrix[j][i] = matrix[k][i];
                                    matrix[k][i] = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else if (e2.getY() - e1.getY() > 100 && Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY()) && Math.abs(velocityY) > 100) {
                for (int i = 1; i < 5; i++) {
                    for (int j = 4; j > 1; j--) {
                        if (matrix[j][i] != 0) {
                            for (int k = j - 1; k > 0; k--) {
                                if (matrix[k][i] == matrix[j][i]) {
                                    check = true;
                                    matrix[j][i] += matrix[k][i];
                                    scores += matrix[j][i];
                                    matrix[k][i] = 0;
                                    j = k;
                                    break;
                                } else if (matrix[k][i] != 0) break;
                            }
                        }
                    }
                    for (int j = 4; j > 1; j--) {
                        if (matrix[j][i] == 0) {
                            for (int k = j - 1; k > 0; k--) {
                                if (matrix[k][i] != 0) {
                                    check = true;
                                    matrix[j][i] = matrix[k][i];
                                    matrix[k][i] = 0;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (check == true) randomNumber();
            isOver();
            set2048Layout();
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void randomNumber() {
        Random random = new Random();
        while (true) {
            int i = random.nextInt(4) + 1, j = random.nextInt(4) + 1;
            if (matrix[i][j] == 0) {
                if (random.nextInt(11) < 10
                ) matrix[i][j] = 2;
                else matrix[i][j] = 4;
                break;
            }
        }
    }

    public void setSecondMatrix() {
        for (int i = 1; i < 25; i++) secondMatrix[i / 5][i % 5] = matrix[i / 5][i % 5];
    }

    public void isOver() {
        for (int i = 1; i < 5; i++)
            for (int j = 1; j < 5; j++)
                if (matrix[i][j] == 0)
                    return;
        boolean check = false;
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 4; j++) {
                if (matrix[i][j] == matrix[i][j + 1]) check = true;
                if (matrix[j][i] == matrix[j + 1][i]) check = true;
            }
        }
        if (check == false) {
            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.game_over);
            dialog.show();
            final Button menu = (Button) dialog.findViewById(R.id.menu);
            Button Again = (Button) dialog.findViewById(R.id.Again);
            TextView score = (TextView) dialog.findViewById(R.id.score);
            TextView best = (TextView) dialog.findViewById(R.id.best);
            score.setText("Your score " + String.valueOf(scores));
            Database database;
            database = new Database(this);
            if (Integer.valueOf(database.getBest2048(this)) < scores) {
                database.setBest2048(scores);
            }
            database.close();
            database = new Database(this);
            best.setText("Best score " + database.getBest2048(this));
            database.close();
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(play2048.this, MainActivity.class));
                }
            });
            Again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play();
                    dialog.cancel();
                }
            });
        }
    }
}
