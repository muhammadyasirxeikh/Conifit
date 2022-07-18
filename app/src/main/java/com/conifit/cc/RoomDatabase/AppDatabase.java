package com.conifit.cc.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ItemBudgetClass.class,TotalBudgetClass.class,SpendAmount.class,SubscriptionClass.class,PlayersDb.class,IncomeDb.class,HistoryDb.class},version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BudgetDao budgetDao();

    private static AppDatabase Instance;
    public static AppDatabase getdbInstance(Context context){



        if (Instance==null){

            Instance= Room.databaseBuilder(context,AppDatabase.class,"Budget")
                    .allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build();

        }
        return Instance;
    }

}
