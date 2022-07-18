package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryDb {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "history_name")
    public  String history_name;

    @ColumnInfo(name = "item_name")
    public  String item_name;


    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "amount")
    public String amount;


    @ColumnInfo(name = "date")
    public String date;
}
