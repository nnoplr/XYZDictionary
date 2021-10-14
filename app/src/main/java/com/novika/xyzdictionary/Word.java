package com.novika.xyzdictionary;

public class Word {

    int id;
    String wordName, definition, url, type;

    public Word() {
    }

    public Word(String wordName, String definition, String url, String type) {
        this.wordName = wordName;
        this.definition = definition;
        this.url = url;
        this.type = type;
    }

    public Word(int id, String wordName, String definition, String url, String type) {
        this.id = id;
        this.wordName = wordName;
        this.definition = definition;
        this.url = url;
        this.type = type;
    }

    public Word(String wordName) {
        this.wordName = wordName;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
