package com.conifit.cc.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.IncomeDb;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.databinding.FragmentHomeBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Home extends Fragment {


    private FragmentHomeBinding binding;
    List<TotalBudgetClass> tableBudgetData;
    List<IncomeDb> tbaleIncomeData;
    AppDatabase db;
    MainActivity mainActivity;
    String weektext;
    String monthtext;
    String incometext;
    String todaytext;
    String budgettext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        db = AppDatabase.getdbInstance(requireContext());
        binding.include.actionBar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Home");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat dfmonth = new SimpleDateFormat("LLLL", Locale.getDefault());
        String month = dfmonth.format(c);

        LiveData<String > d1=db.budgetDao().getTotalbudgetamount();
        d1.observe(mainActivity, new Observer<String>() {
            @Override
            public void onChanged(String aDouble) {
                if (aDouble==null){
                    binding.budgetTv.setText("£ 0");
                }else {
                    binding.budgetTv.setText("£ "+aDouble);
                }

            }
        });

        binding.include.history.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_historyFragment);
        });

        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
        String date=df.format(c);

        LiveData<String> d = db.budgetDao().getTotalTodaySpent(date);
        d.observe(mainActivity, new Observer<String>() {
            @Override
            public void onChanged(String aDouble) {



                if (aDouble==null) {
                    binding.todayTv.setText("£ 0");
                } else {
                    binding.todayTv.setText("£ " + aDouble);
                }

            }
        });


        monthtext = db.budgetDao().getTotalMonthlySpent();
        incometext = db.budgetDao().getTotalIncome();

        if (db.budgetDao().getTotalweeklySpent()==null){
            binding.weekTv.setText("£ 0");
        }else {
            binding.weekTv.setText("£ "+db.budgetDao().getTotalweeklySpent());
        }
        if (monthtext==null){
            binding.monthTv.setText("£ 0");
        }else {
            binding.monthTv.setText("£ "+monthtext);
        }
        if (incometext==null){
            binding.incomeTv.setText("£ 0");
        }else {
            binding.incomeTv.setText("£ "+incometext);
        }





        binding.budgetCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_budgetFragment);
        });

        binding.todayCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_todayFragment);
        });

        binding.weekCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_weekFragment);
        });

        binding.monthCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_monthFragment);
        });
        binding.analyticsCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_analyticsFragment);
        });
        binding.subscriptionCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_subscriptionFragment);
        });
        binding.playersCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_playerFragment);
        });

        binding.incomeCardview.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home2_to_incomeFragment);
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity = (MainActivity) context;
        super.onAttach(context);
    }
}