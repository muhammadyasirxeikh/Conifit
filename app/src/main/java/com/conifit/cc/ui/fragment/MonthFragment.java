package com.conifit.cc.ui.fragment;

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
import android.view.View;
import android.view.ViewGroup;

import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.SpendAmount;
import com.conifit.cc.adapter.TodayFragmentAdapter;
import com.conifit.cc.databinding.FragmentMonthBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.util.List;


public class MonthFragment extends Fragment {


    private FragmentMonthBinding binding;
    AppDatabase db;
    TodayFragmentAdapter adapter;
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMonthBinding.inflate(getLayoutInflater(),container,false);
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Month");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getdbInstance(mainActivity);

        LiveData<List<SpendAmount>> list = db.budgetDao().getlivespentamount();

        list.observe((LifecycleOwner) mainActivity, new Observer<List<SpendAmount>>() {
            @Override
            public void onChanged(List<SpendAmount> spendAmounts) {
                adapter = new TodayFragmentAdapter(spendAmounts, mainActivity,"month");
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.monthlyScreenRecycler.setLayoutManager(manager);
                binding.monthlyScreenRecycler.setHasFixedSize(true);
                binding.monthlyScreenRecycler.setAdapter(adapter);

            }
        });
        if (db.budgetDao().getTotalMonthlySpent()==null){
            binding.totalBudgetTv.setText("£ 0");
        }else{
            binding.totalBudgetTv.setText("£ "+db.budgetDao().getTotalMonthlySpent());
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity=(MainActivity) context;
        super.onAttach(context);
    }
}