package com.android.bdyr.Holder;

public class EventHolder
{
    private String name,date,number,category,text;

    public EventHolder(String name, String date, String number, String category,String text) {
        this.name = name;
        this.date = date;
        this.number = number;
        this.category = category;
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
