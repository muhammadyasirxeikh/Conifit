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

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.BudgetClassVH> {
    private final List<PlayersDb> itemlist;
    Context mcontext;

    private String  position;

    public String  getPosition() {
        return position;
    }

    public void setPosition(String  position) {
        this.position = position;
    }

    public PlayersAdapter(List<PlayersDb> topCategories, Context context) {
        itemlist = topCategories;
        mcontext=context;
    }


    @NonNull
    @NotNull
    @Override
    public PlayersAdapter.BudgetClassVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false);
        return new PlayersAdapter.BudgetClassVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PlayersAdapter.BudgetClassVH holder, int position) {
        PlayersDb list = itemlist.get(position);
        holder.note.setText("Phone Number: "+list.number);
        holder.item_name.setText("Player Name: "+list.name);
        holder.date.setText("Player Kit Number: "+list.player_id);
        holder.amount_allocated.setText("age: "+list.age);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setPosition(list.player_id);

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
    public void onViewRecycled(@NonNull PlayersAdapter.BudgetClassVH holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}