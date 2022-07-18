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
import android.widget.Toast;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.AppDatabase;
import com.conifit.cc.RoomDatabase.IncomeDb;
import com.conifit.cc.RoomDatabase.PlayersDb;
import com.conifit.cc.RoomDatabase.SubscriptionClass;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.adapter.PlayersAdapter;
import com.conifit.cc.adapter.SubscriptionAdapter;
import com.conifit.cc.databinding.FragmentPlayerBinding;
import com.conifit.cc.ui.activity.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PlayerFragment extends Fragment {


    private FragmentPlayerBinding binding;
    MainActivity mainActivity;
    AppDatabase db;
    int daysInCurrentMonth;
    PlayersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentPlayerBinding.inflate(getLayoutInflater(),container,false);
        db = AppDatabase.getdbInstance(requireContext());
        binding.include.actionBar.setNavigationOnClickListener(v->{
            getActivity().onBackPressed();
        });
        binding.include.actionBar.setTitle("Players");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LiveData<List<PlayersDb>> list = db.budgetDao().getPlayer();

        list.observe((LifecycleOwner) mainActivity, new Observer<List<PlayersDb>>() {
            @Override
            public void onChanged(List<PlayersDb> playersDbs) {
                adapter = new PlayersAdapter(playersDbs, mainActivity);
                LinearLayoutManager manager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
                binding.playerScreenRecycler.setLayoutManager(manager);
                binding.playerScreenRecycler.setHasFixedSize(true);
                binding.playerScreenRecycler.setAdapter(adapter);

            }
        });
        binding.playerScreenRecycler.setOnLongClickListener(new View.OnLongClickListener() {
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
        View view = inflater.inflate(R.layout.player_input_layout, null);
        builder.setView(view);



        final AlertDialog dialog = builder.create();



        final EditText player_id = view.findViewById(R.id.player_id);
        EditText player_age=view.findViewById(R.id.age);
        EditText player_number=view.findViewById(R.id.player_number);
        EditText player_name=view.findViewById(R.id.player_name);

        final Button cancel = view.findViewById(R.id.cancel);
        final Button save = view.findViewById(R.id.save);



        save.setOnClickListener(v->{

            String play_id=player_id.getText().toString();
            String name=player_name.getText().toString();
            String number=player_number.getText().toString();
            String age=player_age.getText().toString();

            if (TextUtils.isEmpty(play_id)){
                player_id.requestFocus();
                player_id.setError("Enter Player id");
            }else if (TextUtils.isEmpty(name)){
                player_name.requestFocus();
                player_name.setError("Enter Player name");
            }else if (TextUtils.isEmpty(number)){
                player_number.requestFocus();
                player_number.setError("Enter Player phone number");
            }else if (TextUtils.isEmpty(age)){
                player_age.requestFocus();
                player_age.setError("Enter Player age");
            }else {


                if (db.budgetDao().isexistplayer(play_id)){

                    PlayersDb p=new PlayersDb();
                    p.player_id=play_id;
                    p.name=name;
                    p.number=number;
                    p.age=age;

                    db.budgetDao().updatePlayer(p);
                    dialog.dismiss();
                }else {
                    PlayersDb p=new PlayersDb();
                    p.player_id=play_id;
                    p.name=name;
                    p.number=number;
                    p.age=age;

                    db.budgetDao().insertPlayer(p);
                    dialog.dismiss();
                }
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy", Locale.getDefault());
                String date=df.format(c);
                List<PlayersDb> list= db.budgetDao().player_name_list();

                List<IncomeDb> list_income=db.budgetDao().getincomelist(date);

                int t=db.budgetDao().getamount("Players Match Day Fee",date);

                db.budgetDao().updateplayer_amount((t*list.size())+"",date,"Players Match Day Fee");

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

        String player_id;


        try {

            player_id =adapter.getPosition();

        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        String title=item.getTitle().toString();

        switch (title){

            case "Delete":
                db.budgetDao().deletePlayer(player_id);
        }

        return super.onContextItemSelected(item);
    }
}