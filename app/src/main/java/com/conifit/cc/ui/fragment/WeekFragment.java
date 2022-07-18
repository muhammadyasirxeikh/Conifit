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
import com.conifit.cc.databinding.FragmentWeekBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.util.List;


public class WeekFragment extends Fragment {

   private FragmentWeekBinding binding;
   AppDatabase db;
   TodayFragmentAdapter adapter;
   MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentWeekBinding.inflate(getLayoutInflater(),container,false);
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Week");
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
                adapter = new TodayFragmentAdapter(spendAmounts, mainActivity,"week");
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.weekScreenRecycler.setLayoutManager(manager);
                binding.weekScreenRecycler.setHasFixedSize(true);
                binding.weekScreenRecycler.setAdapter(adapter);

            }
        });

        if (db.budgetDao().getTotalweeklySpent()==null){
            binding.totalBudgetTv.setText("£ 0");
        }else {
            binding.totalBudgetTv.setText("£ "+db.budgetDao().getTotalweeklySpent());
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity=(MainActivity) context;
        super.onAttach(context);
    }
}