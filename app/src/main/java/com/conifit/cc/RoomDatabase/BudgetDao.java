package com.conifit.cc.RoomDatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BudgetDao {

    @Insert
    void insertdata(ItemBudgetClass itemBudgetClass);

    @Query("SELECT * FROM totalbudgetclass ")
    List<TotalBudgetClass> getbudget();

    @Query("SELECT * FROM TotalBudgetClass ")
    LiveData<List<TotalBudgetClass>> getlivebudget();


    @Query("SELECT * FROM itembudgetclass WHERE item_name=:id")
    List<ItemBudgetClass> getitemoldamount(String id);


    @Query("DELETE FROM SpendAmount WHERE date=:date AND note=:note")
    void deleteItem(String date, String note);

    //add total budget

    @Insert
    void insertTotalBudget(TotalBudgetClass totalBudgetClass);

    @Query("SELECT * FROM totalbudgetclass WHERE month=:id AND item_name=:item_name")
    List<TotalBudgetClass> getTotalBudget(String id, String item_name);

    @Query("SELECT total_budget_amount FROM totalbudgetclass WHERE month=:id AND item_name=:item_name")
    int getitemAmount(String id, String item_name);

    @Query("SELECT total_budget_amount_remaining FROM totalbudgetclass WHERE month=:id AND item_name=:item_name")
    int getitemAmountremaining(String id, String item_name);

    @Query("UPDATE totalbudgetclass SET total_budget_amount_remaining= :budget_amount WHERE item_name=:id AND month=:month")
    void update_remaining_budget(String month, String id, String budget_amount);


    @Query("SELECT EXISTS(SELECT * FROM totalbudgetclass WHERE month=:id AND item_name=:item_name)")
    Boolean isexisttotalbudget(String id, String item_name);

    @Update
    void updatetotalbudgetData(TotalBudgetClass totalBudgetClass);

    @Query("UPDATE totalbudgetclass SET total_budget_amount_remaining= :monthly, total_weekly_budget_amount_remaining= :perweek, total_per_day_budget_amount_remaining= :perday WHERE month=:id")
    void update_remaining_monthly_budget(String monthly, String perday, String perweek, String id);

    @Query("SELECT SUM(total_budget_amount) as total FROM totalbudgetclass")
    LiveData<String> getTotalbudgetamount();

    @Query("SELECT * FROM TotalBudgetClass ")
    List<TotalBudgetClass> getbudgetitems();

    @Query("DELETE FROM TotalBudgetClass WHERE month=:date AND item_name=:note")
    void deleteBudget(String date, String note);

    //add expenxe

    @Insert
    void insertspent(SpendAmount spendAmount);

    @Query("SELECT * FROM spendamount WHERE date=:date AND note=:note ")
    List<SpendAmount> getTotalspent(String date, String note);

    @Query("SELECT * FROM spendamount ")
    List<SpendAmount> getspentamount();

    @Query("SELECT * FROM spendamount WHERE date=:date ")
    LiveData<List<SpendAmount>> gettodayspentamount(String date);

    @Query("SELECT * FROM spendamount  ")
    LiveData<List<SpendAmount>> getweekspentamount();

    @Query("SELECT * FROM spendamount ")
    LiveData<List<SpendAmount>> getlivespentamount();


    @Query("SELECT EXISTS(SELECT * FROM spendamount WHERE date=:date AND note=:note AND item_name=:name)")
    Boolean isexistspentbudget(String date, String note, String name);

    @Query("SELECT EXISTS(SELECT * FROM spendamount WHERE date=:date AND item_name=:name )")
    Boolean isexistspentbydate(String date, String name);

    @Update
    void updateSpentAmountData(SpendAmount spendAmount);

    @Query("SELECT SUM(weekly_spent) as total FROM spendamount WHERE  item_name=:name")
    String spentanalyticsweek(String name);

    @Query("SELECT SUM(today_spent) as total FROM spendamount WHERE date=:date AND item_name=:name")
    String spentanalytics(String date, String name);

    @Query("SELECT SUM(weekly_spent) as total FROM spendamount WHERE item_name=:name")
    String spentanalyticsweekly(String name);

    @Query("SELECT SUM(monthly_spent) as total FROM spendamount WHERE item_name=:name")
    String spentanalyticsmonthly(String name);

    @Query("SELECT SUM(today_spent) as total FROM spendamount WHERE date=:date")
    LiveData<String> getTotalTodaySpent(String date);

    @Query("SELECT SUM(weekly_spent) as total FROM spendamount ")
    LiveData<String> getTotalweekSpent();

    @Query("SELECT SUM(weekly_spent) as total FROM spendamount")
    String getTotalweeklySpent();

    @Query("SELECT SUM(monthly_spent) as total FROM spendamount")
    String getTotalMonthlySpent();


    //subscription

    @Insert
    void insertSubscription(SubscriptionClass subscriptionClass);

    @Query("SELECT * FROM subscriptionclass ")
    LiveData<List<SubscriptionClass>> getsubscription();

    @Query("SELECT EXISTS(SELECT * FROM subscriptionclass WHERE Subscription_item=:id)")
    Boolean isexistsubscribtion(String id);

    @Update
    void updateSubscription(SubscriptionClass subscriptionClass);

    @Query("SELECT * FROM subscriptionclass WHERE Subscription_item=:id")
    List<SubscriptionClass> getsubscriptionlist(String id);

    @Query("DELETE FROM subscriptionclass WHERE Subscription_item=:item")
    void deleteSubscriptiont(String item);

    //player expense

    @Insert
    void insertPlayer(PlayersDb playersDb);

    @Query("SELECT * FROM playersdb ")
    LiveData<List<PlayersDb>> getPlayer();

    @Query("SELECT EXISTS(SELECT * FROM playersdb WHERE player_id=:id)")
    Boolean isexistplayer(String id);

    @Update
    void updatePlayer(PlayersDb playersDb);

    @Query("SELECT * FROM playersdb ")
    List<PlayersDb> player_name_list();


    @Query("SELECT * FROM playersdb WHERE player_id=:id")
    List<PlayersDb> getplayerlist(String id);


    @Query("DELETE FROM playersdb WHERE player_id=:id")
    void deletePlayer(String id);
    //income

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIncome(IncomeDb incomeDb);

    @Query("SELECT * FROM incomedb ")
    LiveData<List<IncomeDb>> getIncome();

    @Query("SELECT EXISTS(SELECT * FROM incomedb WHERE income_date=:id)")
    Boolean isexistincomedate(String id);

    @Query("SELECT EXISTS(SELECT * FROM incomedb )")
    Boolean isexistincomeitem();

    @Update
    void updateIncome(IncomeDb incomeDb);

    @Query("SELECT * FROM incomedb ")
    List<IncomeDb> income_list();


    @Query("SELECT * FROM incomedb WHERE income_date=:id")
    List<IncomeDb> getincomelist(String id);

    @Query("SELECT SUM(income_amount) as total FROM incomedb ")
    String getTotalIncome();

    @Query("SELECT SUM(income_remaining_amount) as total FROM incomedb ")
    Double getTotalIncomeremaining();

    @Query("UPDATE incomedb SET income_remaining_amount = :remaining WHERE income_date=:id")
    void updateRemainingIncome(String remaining, String id);

    @Query("SELECT old_income FROM incomedb WHERE item_name= :item_name AND income_date= :date")
    int getamount(String item_name, String date);

    @Query("UPDATE incomedb SET income_amount = :remaining WHERE income_date=:id AND item_name=:item_name")
    void updateplayer_amount(String remaining, String id, String item_name);

    @Query("DELETE FROM incomedb WHERE income_date=:date AND income_note=:note")
    void deleteIncome(String date, String note);


    //history


    @Insert
    void inserthistory(HistoryDb historyDb);

    @Query("SELECT * FROM historydb WHERE date= :date")
    List<HistoryDb> history_list(String date);
}
