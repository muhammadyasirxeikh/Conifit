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
import com.conifit.cc.RoomDatabase.TotalBudgetClass;
import com.conifit.cc.RoomDatabase.TotalBudgetClass;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BudgetScreenAdapter extends RecyclerView.Adapter<BudgetScreenAdapter.BudgetClassVH> {
    private final List<TotalBudgetClass> itemlist;
    Context mcontext;


    public BudgetScreenAdapter(List<TotalBudgetClass> topCategories, Context context) {
        itemlist = topCategories;
        mcontext=context;
    }
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



    @NonNull
    @NotNull
    @Override
    public BudgetClassVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false);
        return new BudgetClassVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BudgetClassVH holder, int position) {
        TotalBudgetClass list = itemlist.get(position);
        holder.note.setVisibility(View.GONE);
        holder.item_name.setText(list.item_name);
        holder.date.setText(list.month);
        holder.amount_allocated.setText("£ "+list.total_budget_amount);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setPosition(list.month);
                setNote(list.item_name);

                return false;
            }
        });
        //bind data
        
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return itemlist.size();
    }



    public static class BudgetClassVH extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
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
    public void onViewRecycled(@NonNull BudgetScreenAdapter.BudgetClassVH holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}
