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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.HistoryDb;
import com.conifit.cc.RoomDatabase.IncomeDb;
import com.conifit.cc.RoomDatabase.ItemBudgetClass;
import com.conifit.cc.RoomDatabase.PlayersDb;
import com.conifit.cc.RoomDatabase.SpendAmount;
import com.conifit.cc.RoomDatabase.SubscriptionClass;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.adapter.IncomeAdapter;
import com.conifit.cc.adapter.SubscriptionAdapter;
import com.conifit.cc.databinding.FragmentIncomeBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class IncomeFragment extends Fragment {


    private FragmentIncomeBinding binding;
    MainActivity mainActivity;
    AppDatabase db;
    int daysInCurrentMonth;
    IncomeAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentIncomeBinding.inflate(getLayoutInflater(),container,false);
        db = AppDatabase.getdbInstance(requireContext());
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Income");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveData<List<IncomeDb>> list = db.budgetDao().getIncome();

        list.observe((LifecycleOwner) mainActivity, new Observer<List<IncomeDb>>() {
            @Override
            public void onChanged(List<IncomeDb> incomeDbs) {
                adapter = new IncomeAdapter(incomeDbs, mainActivity);
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.incomeScreenRecycler.setLayoutManager(manager);
                binding.incomeScreenRecycler.setHasFixedSize(true);
                binding.incomeScreenRecycler.setAdapter(adapter);

            }
        });



        binding.fabButton.setOnClickListener(v -> {
            additem();
        });
    }

    private void additem() {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from((requireContext()));
        View view = inflater.inflate(R.layout.income_input_layout, null);
        builder.setView(view);



        final AlertDialog dialog = builder.create();


        final Spinner itemspinner = view.findViewById(R.id.itemspinner);

        final EditText amount = view.findViewById(R.id.amount);


        final Button cancel = view.findViewById(R.id.cancel);
        final Button save = view.findViewById(R.id.save);
        EditText note=view.findViewById(R.id.note);

        List<PlayersDb> list_player=db.budgetDao().player_name_list();

        itemspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String  item_name=itemspinner.getSelectedItem().toString();
                if (item_name.equals("Players Match Day Fee")){

                    note.setVisibility(View.GONE);

                }else {
                    note.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(v->{
            String  item_name=itemspinner.getSelectedItem().toString();
            String income_amount=amount.getText().toString();
            String income_note=note.getText().toString();

            int total_player=  list_player.size();

            if (item_name.equals("Players Match Day Fee")){


                if (total_player==0){
                    income_amount=income_amount;
                }else
                {
                    income_amount=(Integer.parseInt(income_amount)*total_player)+"";
                }
                income_note="players";

            }

//            String pl_name=playerSpinner.getSelectedItem().toString();
            if (item_name.equals("Select item")){
                Toast.makeText(mainActivity, "Select a valid item", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(income_amount)){
                amount.requestFocus();
                amount.setError("Enter Amount");
            } else if (TextUtils.isEmpty(income_note)){
                note.requestFocus();
                note.setError("Enter Note");
            }else{
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
                String date=df.format(c);


                    IncomeDb i=new IncomeDb();

                        i.item_name=item_name;
                        i.income_amount=income_amount;
                        i.income_note=income_note;
                        i.income_date=date;
                        i.income_remaining_amount=income_amount;
                        i.old_income=income_amount;
                        db.budgetDao().insertIncome(i);

                HistoryDb h=new HistoryDb();
                h.date=date;
                h.item_name=item_name;
                h.note=income_note;
                h.amount=income_amount;
                h.history_name="income";

                db.budgetDao().inserthistory(h);


                        dialog.dismiss();




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


                db.budgetDao().deleteIncome(position,note);
        }

        return super.onContextItemSelected(item);
    }
}