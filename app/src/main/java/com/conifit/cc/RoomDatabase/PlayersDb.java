package com.conifit.cc.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlayersDb {

    @PrimaryKey
    @NonNull
    public  String player_id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "number")
    public String number;
    @ColumnInfo(name = "age")
    public String age;
}
