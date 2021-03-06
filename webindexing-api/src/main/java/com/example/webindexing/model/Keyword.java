package com.example.webindexing.model;

public class Keyword {
    private String wordName;
    private int count;

    public Keyword(String wordName, int count) {
        this.wordName = wordName;
        this.count = count;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
