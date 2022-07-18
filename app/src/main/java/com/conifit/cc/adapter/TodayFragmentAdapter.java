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
import com.conifit.cc.RoomDatabase.SpendAmount;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodayFragmentAdapter extends RecyclerView.Adapter<TodayFragmentAdapter.BudgetClassVH> {
    private final List<SpendAmount> itemlist;
    String total_spent_on;
    Context mcontext;
    private String  position;
    private String  note;




    public String  getPosition() {
        return position;
    }

    public void setPosition(String  position) {
        this.position = position;
    }

    public String  getNote() {
        return note;
    }

    public void setNote(String  note) {
        this.note = note;
    }


    public TodayFragmentAdapter(List<SpendAmount> topCategories, Context context, String day) {
        itemlist = topCategories;
        mcontext=context;
        this.total_spent_on=day;
    }


    @NonNull
    @NotNull
    @Override
    public TodayFragmentAdapter.BudgetClassVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false);
        return new TodayFragmentAdapter.BudgetClassVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodayFragmentAdapter.BudgetClassVH holder, int position) {
        SpendAmount list = itemlist.get(position);

        holder.item_name.setText(list.item_name);
        holder.date.setText("on "+list.date);
        if (total_spent_on.equals("day")){
            holder.amount_allocated.setText("Spent: £"+list.today_spent);
        }else if (total_spent_on.equals("week")){
            holder.amount_allocated.setText("Spent: £"+list.weekly_spent);
        }else if (total_spent_on.equals("month")){
            holder.amount_allocated.setText("Spent: "+list.monthly_spent);
        }

        holder.note.setText("note: "+list.note);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setPosition(list.date);
                setNote(list.note);

                return false;
            }
        });


        //bind data

    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return itemlist.size();
    }



    public static class BudgetClassVH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private final ImageView item_image;
        private final TextView item_name,amount_allocated,date,note;


        public BudgetClassVH(@NonNull @NotNull View itemView) {
            super(itemView);
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
            item_name=itemView.findViewById(R.id.item_name);
            amount_allocated=itemView.findViewById(R.id.item_amount_allocated);
            date=itemView.findViewById(R.id.date);
            item_image=itemView.findViewById(R.id.item_image);
            note=itemView.findViewById(R.id.note);



        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add("Delete");
        }
    }//VH

    @Override
    public void onViewRecycled(@NonNull BudgetClassVH holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}
