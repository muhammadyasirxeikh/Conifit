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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.ItemBudgetClass;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.adapter.BudgetScreenAdapter;
import com.conifit.cc.databinding.FragmentBudgetBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BudgetFragment extends Fragment {

    private FragmentBudgetBinding binding;
    int daysInCurrentMonth;
    MainActivity mainActivity;
    AppDatabase db;
    BudgetScreenAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(getLayoutInflater(), container, false);
        binding.include.actionBar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Budget");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getdbInstance(mainActivity);



       LiveData< List<TotalBudgetClass>> list = db.budgetDao().getlivebudget();


       list.observe((LifecycleOwner) mainActivity, new Observer<List<TotalBudgetClass>>() {
           @Override
           public void onChanged(List<TotalBudgetClass> itemBudgetClasses) {
               adapter = new BudgetScreenAdapter(itemBudgetClasses, mainActivity);
               LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
               binding.budegtScreenRecycler.setLayoutManager(manager);
               binding.budegtScreenRecycler.setHasFixedSize(true);
               binding.budegtScreenRecycler.setAdapter(adapter);
           }
       });


        LiveData<String > d1=db.budgetDao().getTotalbudgetamount();
        d1.observe(mainActivity, new Observer<String>() {
            @Override
            public void onChanged(String aDouble) {
                if (aDouble==null){
                    binding.totalBudgetTv.setText("£ 0");
                }else {
                    binding.totalBudgetTv.setText("£ "+aDouble);
                }

            }
        });




        binding.fabButton.setOnClickListener(v -> {
            additem();
        });

    }

    private void additem() {


        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        LayoutInflater inflater = LayoutInflater.from((mainActivity));
        View view = inflater.inflate(R.layout.input_layout, null);
        builder.setView(view);



        final AlertDialog dialog = builder.create();


        final Spinner itemspinner = view.findViewById(R.id.itemspinner);
        final EditText amount = view.findViewById(R.id.amount);

        final Button cancel = view.findViewById(R.id.cancel);
        final Button save = view.findViewById(R.id.save);
        EditText note=view.findViewById(R.id.note);
        note.setVisibility(View.GONE);

        save.setOnClickListener(v -> {

            String budgetAmount = amount.getText().toString();
            String budgetitem = itemspinner.getSelectedItem().toString();


            if (TextUtils.isEmpty(budgetAmount)) {
                amount.requestFocus();
                amount.setError("Amount is required!");

            } else if (budgetitem.equals("Select item")) {

                Toast.makeText(mainActivity, "Select a valid item", Toast.LENGTH_SHORT).show();
            } else {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
                SimpleDateFormat dfmonth = new SimpleDateFormat("LLLL", Locale.getDefault());
                String formattedDate = df.format(c);
                String month = dfmonth.format(c);

                if (db.budgetDao().isexisttotalbudget(month,budgetitem)){



                    int total_amount=db.budgetDao().getitemAmount(month,budgetitem)+Integer.parseInt(budgetAmount);
                    int total_amount_remaining=db.budgetDao().getitemAmountremaining(month,budgetitem)+Integer.parseInt(budgetAmount);


                    TotalBudgetClass t=new TotalBudgetClass();
                    t.month=month;
                    t.item_name=budgetitem;
                    t.total_budget_amount=total_amount+"";
                    t.total_budget_amount_remaining=total_amount_remaining+"";

                    db.budgetDao().updatetotalbudgetData(t);
                    dialog.dismiss();

                }else {
                    TotalBudgetClass t=new TotalBudgetClass();
                    t.month=month;
                    t.item_name=budgetitem;
                    t.total_budget_amount=budgetAmount;
                    t.total_budget_amount_remaining=budgetAmount;

                    db.budgetDao().insertTotalBudget(t);
                    dialog.dismiss();
                }



            }

        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

    }

    private int calculateweek() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = format.parse(year + "-" + month);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.setTime(date);

        int start = c.get(Calendar.WEEK_OF_MONTH);

        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, -1);
        int end = c.get(Calendar.WEEK_OF_MONTH);

        int week = end - start + 1;
        return week;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity=(MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        String month;
        String item_name;

        try {

            month =adapter.getPosition();
            item_name=adapter.getNote();
        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        String title=item.getTitle().toString();

        switch (title){

            case "Delete":


                db.budgetDao().deleteBudget(month,item_name);
        }

        return super.onContextItemSelected(item);
    }
}