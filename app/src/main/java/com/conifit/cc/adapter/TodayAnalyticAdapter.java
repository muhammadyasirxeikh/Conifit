package com.conifit.cc.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conifit.cc.R;
import com.conifit.cc.RoomDatabase.PlayersDb;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodayAnalyticAdapter extends RecyclerView.Adapter<TodayAnalyticAdapter.BudgetClassVH> {
    private final List<String> itemname;
    private final List<String> itemamount;
    private final List<String> itemtotalamount;
    String date;
    Context mcontext;



    public TodayAnalyticAdapter(List<String > name,List<String > amount,List<String > total_amount, String  date,Context context) {
        itemname=name;
        itemamount=amount;
        itemtotalamount=total_amount;
        mcontext=context;
        date=date;
    }


    @NonNull
    @NotNull
    @Override
    public TodayAnalyticAdapter.BudgetClassVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analytic_item, parent, false);
        return new TodayAnalyticAdapter.BudgetClassVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodayAnalyticAdapter.BudgetClassVH holder, int position) {

        holder.item_name.setText(itemname.get(position));
        holder.amount_allocated.setText("Spent: £ "+itemamount.get(position));
        float pers=Float.parseFloat(itemamount.get(position))/Float.parseFloat(itemtotalamount.get(position)) *100;
        holder.note.setText(pers+"% used of "+itemtotalamount.get(position));
        holder.date.setText(date);
        if (pers<50){
            holder.item_image.setBackgroundResource(R.drawable.green);
        }else if(pers>=50&& pers<100){
            holder.item_image.setBackgroundResource(R.drawable.brown);
        }else if( pers==100){
            holder.item_image.setBackgroundResource(R.drawable.red);
        }


    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return itemname.size();
    }



    public static class BudgetClassVH extends RecyclerView.ViewHolder  {
        private final ImageView item_image;
        private final TextView item_name,amount_allocated,date,note;


        public BudgetClassVH(@NonNull @NotNull View itemView) {
            super(itemView);

            item_name=itemView.findViewById(R.id.item_name);
            amount_allocated=itemView.findViewById(R.id.amount_spent);
            date=itemView.findViewById(R.id.date);
            item_image=itemView.findViewById(R.id.dot);
            note=itemView.findViewById(R.id.budget_percentage);


        }

    }//VH


}
