package com.example.game2048.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.game2048.Scores;

import java.util.ArrayList;


public class SqliteDatabase extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	5;
    private	static final String DATABASE_NAME = "score";
    private	static final String TABLE_SCORES = "scores";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "userName";
    private static final String COLUMN_NO = "score";

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE	TABLE " + TABLE_SCORES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_NO + " INTEGER" + ")";
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public ArrayList<Scores> listScores(){
        String sql = "select * from " + TABLE_SCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Scores> storeScores = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String userName = cursor.getString(1);
                String scores = cursor.getString(2);
                storeScores.add(new Scores(id, userName, scores));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeScores;
    }

    public void addScores(Scores scores){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, scores.getUserName());
        values.put(COLUMN_NO, scores.getScore());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_SCORES, null, values);
    }

    public void updateScores(Scores scores){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, scores.getUserName());
        values.put(COLUMN_NO, scores.getScore());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_SCORES, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(scores.getId())});
    }

    public Scores findScores(String userName){
        String query = "Select * FROM "	+ TABLE_SCORES + " WHERE " + COLUMN_NAME + " = " + "userName";
        SQLiteDatabase db = this.getWritableDatabase();
        Scores scores = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String scoresName = cursor.getString(1);
            String scoresNo = cursor.getString(2);
            scores = new Scores(id, scoresName, scoresNo);
        }
        cursor.close();
        return scores;
    }

    public void deleteScore(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCORES, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
