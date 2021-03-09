package com.example.game2048;


import java.util.Comparator;

public class Scores {
    private int id;
    private String userName;
    private String score;

    public Scores(String userName, String score) {
        this.userName = userName;
        this.score = score;
    }

    public Scores(int id, String userName, String score) {
        this.id = id;
        this.userName = userName;
        this.score = score;
    }

    public static Comparator<Scores> NameUserAZComparator = new Comparator<Scores>() {
        @Override
        public int compare(Scores s1, Scores s2) {
            return s1.getUserName().compareTo(s2.getUserName());
        }
    };

    public static Comparator<Scores> NameUserZAComparator = new Comparator<Scores>() {
        @Override
        public int compare(Scores s1, Scores s2) {
            return s2.getUserName().compareTo(s1.getUserName());
        }
    };




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


}