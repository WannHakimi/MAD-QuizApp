package com.example.quizapplication;

import com.google.firebase.firestore.Exclude;

public class User implements Comparable<User>{
    private String username;
    private String email;
    private String password;

    private double score;
    private String school;
    private String documentID;
    private String pic;


    public User(){

    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public User(String username, String email, String password,String school,int score,String pic){
        this.username = username;
        this.password = password;
        this.email = email;

        this.score = 0;
        this.school = "";
        this.pic= pic;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }


    public String getSchool() {
        return school;
    }

    public double getScore(){
        return score;
    }

    public String getPic(){
        return pic;
    }

    @Override
    public int compareTo(User user) {
        if(getScore() > user.getScore())
            return -1;
        else if(getScore() == user.getScore())
            return 0;
        else
            return 1;
    }
}
