package com.conifit.cc.ui.fragment;

import android.app.DatePickerDialog;
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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.HistoryDb;
import com.conifit.cc.RoomDatabase.SpendAmount;
import com.conifit.cc.adapter.HistoryAdapter;
import com.conifit.cc.adapter.TodayFragmentAdapter;
import com.conifit.cc.databinding.FragmentHistoryBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    HistoryAdapter adapter;
    MainActivity mainActivity;
    AppDatabase db;
    final Calendar myCalendar= Calendar.getInstance();

    private FragmentHistoryBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHistoryBinding.inflate(getLayoutInflater(),container,false);
        binding.include.actionBar.setTitle("History");
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getdbInstance(requireContext());

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="MMM-dd-yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                binding.date.setText(dateFormat.format(myCalendar.getTime()));

                List<HistoryDb> list=db.budgetDao().history_list(dateFormat.format(myCalendar.getTime()));

                adapter = new HistoryAdapter(list, mainActivity,"day");
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.historyScreenRecycler.setLayoutManager(manager);
                binding.historyScreenRecycler.setHasFixedSize(true);
                binding.historyScreenRecycler.setAdapter(adapter);
            }
        };

        binding.date.setOnClickListener(v->{
            new DatePickerDialog(requireContext(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });




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