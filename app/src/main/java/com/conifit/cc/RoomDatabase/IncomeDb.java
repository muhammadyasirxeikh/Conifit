package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(primaryKeys = {"income_date","income_note"})
public class IncomeDb {
    @NonNull
    @ColumnInfo(name = "income_date")
    public  String income_date;

    @ColumnInfo(name = "income_amount")
    public String income_amount;

    @ColumnInfo(name = "income_remaining_amount")
    public String income_remaining_amount;

    @NonNull
    @ColumnInfo(name = "income_note")
    public String income_note;


    @ColumnInfo(name = "item_name")
    public String item_name;

    @ColumnInfo(name = "old_income")
    public String old_income;


}
