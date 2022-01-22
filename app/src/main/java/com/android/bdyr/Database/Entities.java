package com.android.bdyr.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Entities {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Date")
    private String date;
    @ColumnInfo(name = "Number")
    private String number;
    @ColumnInfo(name = "Category")
    private String category;
    @ColumnInfo(name = "Wish_Text")
    private String text;

    public Entities(int id,String name, String date, String number, String category, String text) {
        this.id=id;
        this.name = name;
        this.date = date;
        this.number = number;
        this.category = category;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
