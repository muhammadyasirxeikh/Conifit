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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.IncomeDb;
import com.conifit.cc.RoomDatabase.ItemBudgetClass;
import com.conifit.cc.RoomDatabase.SpendAmount;
import com.conifit.cc.RoomDatabase.SubscriptionClass;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.adapter.SubscriptionAdapter;
import com.conifit.cc.adapter.TodayFragmentAdapter;
import com.conifit.cc.databinding.FragmentSubscriptionBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SubscriptionFragment extends Fragment {


    private FragmentSubscriptionBinding binding;
    int daysInCurrentMonth;

    AppDatabase db;
    MainActivity mainActivity;
    SubscriptionAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSubscriptionBinding.inflate(getLayoutInflater(),container,false);
        db = AppDatabase.getdbInstance(requireContext());
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Subscription");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        LiveData<List<SubscriptionClass>> list = db.budgetDao().getsubscription();

        list.observe((LifecycleOwner) mainActivity, new Observer<List<SubscriptionClass>>() {
            @Override
            public void onChanged(List<SubscriptionClass> subscriptionClasses) {
                adapter = new SubscriptionAdapter(subscriptionClasses, mainActivity);
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.subscriptionScreenRecycler.setLayoutManager(manager);
                binding.subscriptionScreenRecycler.setHasFixedSize(true);
                binding.subscriptionScreenRecycler.setAdapter(adapter);

            }
        });
        binding.subscriptionScreenRecycler.setOnLongClickListener(new View.OnLongClickListener() {
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
        View view = inflater.inflate(R.layout.subscription_input_layout, null);
        builder.setView(view);



        final AlertDialog dialog = builder.create();






        final Button cancel = view.findViewById(R.id.cancel);
        final Button save = view.findViewById(R.id.save);
        EditText rent_tv=view.findViewById(R.id.rent);
        final Spinner itemspinner = view.findViewById(R.id.itemspinner);

        save.setOnClickListener(v->{
            String subsc_id=itemspinner.getSelectedItem().toString();
            String rent=rent_tv.getText().toString();

            if (subsc_id.equals("Select item")){
                Toast.makeText(mainActivity, "Select Valid Item", Toast.LENGTH_SHORT).show();
            }else  if (TextUtils.isEmpty(rent)){
                rent_tv.requestFocus();
                rent_tv.setError("Enter Rent for the Month");
            }else {

                Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);

                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
                    String date=df.format(c);

                    SubscriptionClass subscriptionClass=new SubscriptionClass();
                    subscriptionClass.Subscription_item=subsc_id;
                    subscriptionClass.rent=rent;
                    subscriptionClass.rent_year=year+"";
                    db.budgetDao().insertSubscription(subscriptionClass);

                    if (db.budgetDao().isexistincomeitem()){
                        Double remaining_income=db.budgetDao().getTotalIncomeremaining()-Double.parseDouble(rent);

                        db.budgetDao().updateRemainingIncome(remaining_income+"",date);
                        dialog.dismiss();
                    }else
                    {
                       dialog.dismiss();

                    }

//                if (db.budgetDao().isexistsubscribtion(subsc_id)){
//                   List<SubscriptionClass> list1=db.budgetDao().getsubscriptionlist(subsc_id);
//                   String ol_rent=list1.get(0).rent;
//                   String old_year=list1.get(0).rent_year;
//                    Date c = Calendar.getInstance().getTime();
//                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
//                    String date=df.format(c);
//
//                    SubscriptionClass subscriptionClass=new SubscriptionClass();
//                    subscriptionClass.Subscription_item=subsc_id;
//                    subscriptionClass.rent=rent;
//                    subscriptionClass.rent_year=old_year;
//                    db.budgetDao().updateSubscription(subscriptionClass);
//
//                    if (db.budgetDao().isexistincomeitem()){
//                        Double remaining_income=db.budgetDao().getTotalIncomeremaining()-Double.parseDouble(rent);
//
//                        db.budgetDao().updateRemainingIncome(remaining_income+"",date);
//                        dialog.dismiss();
//                    }else
//                    {
//                        dialog.dismiss();
//
//                    }
//
//
//                }else {
//
//
//                    Calendar calendar = Calendar.getInstance();
//                    int year = calendar.get(Calendar.YEAR);
//
//                    Date c = Calendar.getInstance().getTime();
//                    SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
//                    String date=df.format(c);
//
//                    SubscriptionClass subscriptionClass=new SubscriptionClass();
//                    subscriptionClass.Subscription_item=subsc_id;
//                    subscriptionClass.rent=rent;
//                    subscriptionClass.rent_year=year+"";
//                    db.budgetDao().insertSubscription(subscriptionClass);
//
//                    if (db.budgetDao().isexistincomeitem()){
//                        Double remaining_income=db.budgetDao().getTotalIncomeremaining()-Double.parseDouble(rent);
//
//                        db.budgetDao().updateRemainingIncome(remaining_income+"",date);
//                        dialog.dismiss();
//                    }else
//                    {
//                       dialog.dismiss();
//
//                    }
//
//
//                }
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

        String item_name;


        try {

            item_name =adapter.getPosition();

        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        String title=item.getTitle().toString();

        switch (title){

            case "Delete":


                db.budgetDao().deleteSubscriptiont(item_name);
        }

        return super.onContextItemSelected(item);
    }

}