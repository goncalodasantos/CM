package com.example.gonca.smtucky;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by filipemendes on 25/12/17.
 */

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final List<String> words;

    public ItemViewAdapter(ArrayList input) {
        Log.d("IVAdapter", input.toString());
        words = input;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Log.d("IVAdapter Pos", words.get(position));
        holder.title.setText(words.get(position));

//        holder.moreButton.setOnClickListener(view -> updateList(position));
//        holder.deleteButton.setOnClickListener(view -> removerItem(position));
    }

    @Override
    public int getItemCount() {
        return words != null ? words.size() : 0;
    }

//    public void updateList(int add) {
//        insertItem(add);
//    }
//
//    // Método responsável por inserir um novo usuário na lista e notificar que há novos itens.
//    private void insertItem(int add) {
//        words.add(add);
//        notifyItemInserted(getItemCount());
//    }
//
//    private void removerItem(int position) {
//        words.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, words.size());
//    }




}