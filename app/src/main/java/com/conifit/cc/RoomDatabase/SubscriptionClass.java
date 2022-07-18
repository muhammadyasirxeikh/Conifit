package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SubscriptionClass {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    @ColumnInfo(name = "subscription_item")
    public  String Subscription_item;

    @ColumnInfo(name = "year")
    public String rent_year;

    @ColumnInfo(name = "rent")
    public String rent;


}
