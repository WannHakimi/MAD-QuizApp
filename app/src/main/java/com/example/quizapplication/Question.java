package com.example.quizapplication;

public class Question {
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private int answer;
    private String choice4;
    private String hint;
    private int userAnswer = -1;
    private int key;

    public Question(){

    }

    public Question(String question,String choice1,String choice2,String choice3,String choice4,int answer,String hint,int key){
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.answer = answer;
        this.hint = hint;
        this.key = key;

    }

    public String getQuestion() {
        return question;
    }

    public String getChoice4() {
        return choice4;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setUserAnswer(int userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getHint() {

        return hint;
    }

    public String getChoice1() {
        return choice1;
    }

    public int getAnswer() {
        return answer;
    }

    public int getKey(){return key;}


}
