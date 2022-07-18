package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"month", "item_name"})
public class TotalBudgetClass {


    @NonNull
    @ColumnInfo(name = "month")
    public  String month;

    @NonNull
    @ColumnInfo(name = "item_name")
    public String item_name;

    @ColumnInfo(name = "total_budget_amount")
    public String total_budget_amount;

    @ColumnInfo(name = "total_per_day_budget_amount")
    public String total_per_day_budget_amount;

    @ColumnInfo(name = "total_weekly_budget_amount")
    public String total_weekly_budget_amount;

    @ColumnInfo(name = "total_per_day_budget_amount_remaining")
    public String total_per_day_budget_amount_remaining;

    @ColumnInfo(name = "total_weekly_budget_amount_remaining")
    public String total_weekly_budget_amount_remaining;

    @ColumnInfo(name = "total_budget_amount_remaining")
    public String total_budget_amount_remaining;

    @ColumnInfo(name = "today_spending")
    public float today_spending;

    @ColumnInfo(name = "weekly_spending")
    public float weekly_spending;

    @ColumnInfo(name = "monthly_spending")
    public float monthly_spending;


}
