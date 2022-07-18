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

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.ItemBudgetClass;
import com.conifit.cc.RoomDatabase.SpendAmount;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.adapter.TodayAnalyticAdapter;
import com.conifit.cc.databinding.FragmentMonthlyAnalyticsBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MonthlyAnalytics extends Fragment {


    private FragmentMonthlyAnalyticsBinding binding;
    AppDatabase db;

    TodayAnalyticAdapter adapter;
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMonthlyAnalyticsBinding.inflate(getLayoutInflater(),container,false);
        db = AppDatabase.getdbInstance(requireContext());
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Monthly Analytics");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
        String date=df.format(c);
        SimpleDateFormat dfmonth = new SimpleDateFormat("LLLL", Locale.getDefault());
        String month = dfmonth.format(c);

        List<String > l_name=new ArrayList<>();

        List<String > l_amount=new ArrayList<>();
        List<String > l_total_amount=new ArrayList<>();


        LiveData<String> d=db.budgetDao().getTotalweekSpent();
        d.observe(mainActivity, new Observer<String>() {
            @Override
            public void onChanged(String aDouble) {
                if (aDouble==null){
                    binding.pieChart.setVisibility(View.GONE);
                }else{
                    binding.pieChart.setVisibility(View.VISIBLE);
                }

            }
        });
        LiveData<List<SpendAmount>> listt = db.budgetDao().getweekspentamount();

        listt.observe((LifecycleOwner) mainActivity, new Observer<List<SpendAmount>>() {
            @Override
            public void onChanged(List<SpendAmount> spendAmounts) {

                for (int i=0;i<spendAmounts.size();i++){

                    l_name.add(spendAmounts.get(i).item_name);
                }

                Set<String> set = new HashSet<>(l_name);
                l_name.clear();
                l_name.addAll(set);




                for (int i=0;i<l_name.size();i++){
                    l_amount.add(db.budgetDao().spentanalyticsmonthly(l_name.get(i)));

                }
                List<TotalBudgetClass> list_item=db.budgetDao().getbudget();
                for (int i=0;i<list_item.size();i++){
                    l_total_amount.add(String.valueOf(db.budgetDao().getitemAmount(month,l_name.get(i))));

                }
                adapter=new TodayAnalyticAdapter(l_name,l_amount,l_total_amount,date,mainActivity);
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.monthlyScreenRecycler.setLayoutManager(manager);
                binding.monthlyScreenRecycler.setHasFixedSize(true);
                binding.monthlyScreenRecycler.setAdapter(adapter);





                List<DataEntry> l=new ArrayList<>();
                Pie pie= AnyChart.pie();


                for (int i=0;i<l_name.size();i++){

                    l.add(new ValueDataEntry(l_name.get(i),Integer.parseInt(l_amount.get(i))));
                }


                pie.data(l);

                pie.title("Month Analytics");
                pie.labels().position("outside");
                pie.legend().title().enabled(true);

                pie.legend().title().text("Items Spent on");

                pie.legend().position("center-bottom");


                binding.pieChart.setChart(pie);
            }
        });


    }
    @Override
    public void onAttach(@NonNull Context context) {
        mainActivity=(MainActivity) context;
        super.onAttach(context);
    }
}