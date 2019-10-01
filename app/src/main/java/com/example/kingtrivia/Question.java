package com.example.kingtrivia;

public class Question {
    private int id;
    private int level;
    private String theQuestion;
    private String ans1;
    private String ans2;
    private String ans3;
    private int isActive;

    public Question() {
    }

    public Question(int id, int level, String theQuestion, String ans1, String ans2, String ans3, int isActive) {
        this.id = id;
        this.level = level;
        this.theQuestion = theQuestion;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTheQuestion() {
        return theQuestion;
    }

    public void setTheQuestion(String theQuestion) {
        this.theQuestion = theQuestion;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }
}
