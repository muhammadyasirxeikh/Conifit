package com.conifit.cc.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conifit.cc.R;
import com.conifit.cc.databinding.FragmentAnalyticsBinding;

public class AnalyticsFragment extends Fragment {


    private FragmentAnalyticsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAnalyticsBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.include.actionBar.setTitle("Analytics");
        binding.include.history.setVisibility(View.GONE);

        binding.todayCardview.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_analyticsFragment_to_todayAnalytics);
        });
        binding.monthCardview.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_analyticsFragment_to_monthlyAnalytics);
        });
        binding.weekCardview.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_analyticsFragment_to_weeklyAnalytics);
        });
    }
}