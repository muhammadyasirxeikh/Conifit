package com.conifit.cc.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.HistoryDb;
import com.conifit.cc.RoomDatabase.ItemBudgetClass;
import com.conifit.cc.RoomDatabase.SpendAmount;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.adapter.TodayFragmentAdapter;
import com.conifit.cc.databinding.FragmentTodayBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TodayFragment extends Fragment {


    private FragmentTodayBinding binding;
    AppDatabase db;
    TodayFragmentAdapter adapter;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTodayBinding.inflate(getLayoutInflater(),container,false);



        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Today");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getdbInstance(requireContext());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
        String date=df.format(c);

        LiveData<List<SpendAmount>> list = db.budgetDao().gettodayspentamount(date);

        list.observe((LifecycleOwner) mainActivity, new Observer<List<SpendAmount>>() {
            @Override
            public void onChanged(List<SpendAmount> spendAmounts) {

                adapter = new TodayFragmentAdapter(spendAmounts, mainActivity,"day");
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.todayScreenRecycler.setLayoutManager(manager);
                binding.todayScreenRecycler.setHasFixedSize(true);
                binding.todayScreenRecycler.setAdapter(adapter);

            }
        });



        LiveData<String> d=db.budgetDao().getTotalTodaySpent(date);
        d.observe(mainActivity, new Observer<String>() {
            @Override
            public void onChanged(String aDouble) {
                if (aDouble==null){
                    binding.totalBudgetTv.setText("£ 0");
                }else{
                    binding.totalBudgetTv.setText("£ "+aDouble);
                }

            }
        });


        binding.todayScreenRecycler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });

        binding.fabButton.setOnClickListener(v -> {
            additem();
        });
    }

    private void additem() {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from((requireContext()));
        View view = inflater.inflate(R.layout.input_layout, null);
        builder.setView(view);



        final AlertDialog dialog = builder.create();


        final Spinner itemspinner = view.findViewById(R.id.itemspinner);
        final EditText amount = view.findViewById(R.id.amount);


        final Button cancel = view.findViewById(R.id.cancel);
        final Button save = view.findViewById(R.id.save);
        EditText note=view.findViewById(R.id.note);

        List<TotalBudgetClass> list=db.budgetDao().getbudgetitems();

        List<String > item_name=new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            item_name.add(list.get(i).item_name);

        }

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1, item_name);
        itemspinner.setAdapter(adp1);

        save.setOnClickListener(v->{
            String budgetAmount = amount.getText().toString();
            String budgetitem;
            if (item_name.isEmpty()){
                Toast.makeText(mainActivity, "No Item Exist", Toast.LENGTH_SHORT).show();
                return;
            }else {
                budgetitem = itemspinner.getSelectedItem().toString();
            }

            String notetext=note.getText().toString();

            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
            String date=df.format(c);
            SimpleDateFormat dfmonth = new SimpleDateFormat("LLLL", Locale.getDefault());
            String month =dfmonth.format(c);


            List<ItemBudgetClass> itemBudgetClassList=db.budgetDao().getitemoldamount(budgetitem);
            List<TotalBudgetClass> totalBudgetClasses=db.budgetDao().getTotalBudget(month,budgetitem);

            if (TextUtils.isEmpty(budgetitem)) {

            } else if (TextUtils.isEmpty(budgetAmount)) {
                amount.requestFocus();
                amount.setError("Amount is required!");

            }else if (TextUtils.isEmpty(notetext)) {
                note.requestFocus();
                note.setError("Note is required!");

            } else if (budgetitem.equals("Select item")) {

                Toast.makeText(requireContext(), "Select a valid item", Toast.LENGTH_SHORT).show();
            } else {
                if (db.budgetDao().isexistspentbudget(date,notetext,budgetitem)){
                    List<SpendAmount> spentlist=db.budgetDao().getTotalspent(date,notetext);
                    float day_spent=spentlist.get(0).today_spent;
                    float week_spent=spentlist.get(0).weekly_spent;
                    float monthly_spent= spentlist.get(0).monthly_spent;
                    String remaining_day_budget=totalBudgetClasses.get(0).total_budget_amount_remaining;

                    Toast.makeText(mainActivity, remaining_day_budget+day_spent+"", Toast.LENGTH_SHORT).show();

                    if (day_spent<Float.parseFloat(remaining_day_budget)){

                        float remaining=Float.parseFloat(remaining_day_budget);
                        if (Float.parseFloat(budgetAmount)<=remaining){



                            SpendAmount spendAmount=new SpendAmount();
                            spendAmount.item_name=budgetitem;
                            spendAmount.date=date;
                            spendAmount.note=notetext;
                            spendAmount.today_spent=Float.parseFloat(budgetAmount)+day_spent;
                            spendAmount.weekly_spent=Float.parseFloat(budgetAmount)+week_spent;
                            spendAmount.monthly_spent=Float.parseFloat(budgetAmount)+monthly_spent;
                            db.budgetDao().updateSpentAmountData(spendAmount);


                         String budget_amount_remaining= ((remaining-Float.parseFloat(budgetAmount))+"");

                            HistoryDb h=new HistoryDb();
                            h.date=date;
                            h.item_name=budgetitem;
                            h.note=notetext;
                            h.amount=budgetAmount;
                            h.history_name="expense";

                            db.budgetDao().inserthistory(h);

                            db.budgetDao().update_remaining_budget(month,budgetitem,budget_amount_remaining);

                            dialog.dismiss();

                        }else {
                            Toast.makeText(requireContext(), "your Daily remaining budget limit is "+remaining+" $", Toast.LENGTH_SHORT).show();
                        }
////
                    }else {
                        Toast.makeText(requireContext(), " Cannot Spent more, Your Daily  budget limit has reached ", Toast.LENGTH_SHORT).show();

                    }


                }
                else {
                    String monthly_remaining_day_budget=totalBudgetClasses.get(0).total_budget_amount_remaining;



                    if (Float.parseFloat(budgetAmount)<=Float.parseFloat(monthly_remaining_day_budget)){

                        SpendAmount spendAmount=new SpendAmount();
                        spendAmount.item_name=budgetitem;
                        spendAmount.date=date;
                        spendAmount.note=notetext;
                        spendAmount.today_spent=Float.parseFloat(budgetAmount);
                        spendAmount.weekly_spent=Float.parseFloat(budgetAmount);
                        spendAmount.monthly_spent=Float.parseFloat(budgetAmount);
                        db.budgetDao().insertspent(spendAmount);

                        HistoryDb h=new HistoryDb();
                        h.date=date;
                        h.item_name=budgetitem;
                        h.note=notetext;
                        h.amount=budgetAmount;
                        h.history_name="expense";
                        db.budgetDao().inserthistory(h);


                        String budget_amount_remaining= ((Float.parseFloat(monthly_remaining_day_budget)-Float.parseFloat(budgetAmount))+"");


                        db.budgetDao().update_remaining_budget(month,budgetitem,budget_amount_remaining);


                        dialog.dismiss();

//
                    }else {
                        Toast.makeText(requireContext(), "Your Daily Budget limit for this item is : £"+totalBudgetClasses.get(0).total_budget_amount+"", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity=(MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String position;
        String note;

        try {

            position =adapter.getPosition();
            note=adapter.getNote();
        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        String title=item.getTitle().toString();

        switch (title){

            case "Delete":


                db.budgetDao().deleteItem(position,note);
        }

        return super.onContextItemSelected(item);
    }
}