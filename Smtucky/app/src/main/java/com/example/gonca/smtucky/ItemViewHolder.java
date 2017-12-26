package com.example.gonca.smtucky;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by filipemendes on 25/12/17.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageButton moreButton;
    public ImageButton deleteButton;
    public MainActivity.ItemClickListener clickListener;

    public ItemViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.main_line_title);
        moreButton = (ImageButton) itemView.findViewById(R.id.main_line_more);
        deleteButton = (ImageButton) itemView.findViewById(R.id.main_line_delete);
    }

    public void setClickListener(MainActivity.ItemClickListener itemClickListener) {
        this.clickListener = clickListener;
    }
}