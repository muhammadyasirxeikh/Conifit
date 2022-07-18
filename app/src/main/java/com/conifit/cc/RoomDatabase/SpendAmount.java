package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"date","note","item_name"})
public class SpendAmount {

    @NonNull
    @ColumnInfo(name = "item_name")
    public  String item_name;

    @NonNull
    @ColumnInfo(name = "note")
    public String note;

    @NonNull
    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "today_spent")
    public float today_spent;

    @ColumnInfo(name = "monthly_spent")
    public float monthly_spent;

    @ColumnInfo(name = "weekly_spent")
    public float weekly_spent;
}
