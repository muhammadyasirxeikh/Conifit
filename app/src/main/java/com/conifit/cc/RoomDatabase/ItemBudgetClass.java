package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"date","note"})
public class ItemBudgetClass {

    @ColumnInfo(name = "item_name")
    public  String item_name;

    @NonNull
    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "budget_amount")
    public String budget_amount;

    @ColumnInfo(name = "per_day_budget_amount")
    public String per_day_budget_amount;

    @ColumnInfo(name = "per_week_budget_amount")
    public String per_week_budget_amount;

    @ColumnInfo(name = "per_day_budget_amount_remaining")
    public String per_day_budget_amount_remaining;

    @ColumnInfo(name = "per_week_budget_amount_remaining")
    public String per_week_budget_amount_remaining;

    @NonNull
    @ColumnInfo(name = "date")
    public String date;

}
