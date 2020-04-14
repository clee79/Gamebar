package com.example.a4322_term_project;

public class QuestionFormat {
    private int ID;
    private String QUESTION;
    private String OPTA;
    private String OPTB;
    private String OPTC;
    private String ANSWER;

    public QuestionFormat() {
        ID = 0;
        QUESTION = "";
        OPTA = "";
        OPTB = "";
        OPTC = "";
        ANSWER = "";
    }

    public QuestionFormat(String question, String optionA, String optionB, String optionC,
                    String answer) {

        // We have the question
        // 3 wrong responses
        // 1 correct response
        QUESTION = question;
        OPTA = optionA;
        OPTB = optionB;
        OPTC = optionC;
        ANSWER = answer;
    }

    public QuestionFormat(String q, String a) {
        QUESTION = q;
        ANSWER = a;
    }

    // Getters and setters
    public void setID(int id) {
        ID=id;
    }

    public int getID() {
        return ID;
    }

    public void setQuestion(String q) {
        QUESTION = q;
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public void setOptionA(String choiceA) {
        OPTA = choiceA;
    }
    public String getOPTA() {
        return OPTA;
    }


    public void setOptionB(String choiceB) {
        OPTB = choiceB;
    }
    public String getOPTB() {
        return OPTB;
    }

    public void setOptionC(String choiceC) {
        OPTC = choiceC;
    }
    public String getOPTC() {
        return OPTC;
    }

    public void setAnswer(String answer) {
        ANSWER = answer;
    }
    public String getANSWER() {
        return ANSWER;
    }
}
