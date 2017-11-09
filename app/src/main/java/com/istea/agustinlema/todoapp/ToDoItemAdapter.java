package com.istea.agustinlema.todoapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.istea.agustinlema.todoapp.model.ToDoItem;

import java.util.List;

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ToDoItem> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    //Se ejecuta por cada elemento de la lista
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ToDoItem item = getItem(position);

        //Genero una vista en base a mi xml
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_todoitem,null);

        //Accedo al textview de la vista que acabo de generar
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvBody = view.findViewById(R.id.tvBody);
        LinearLayout layoutItem = view.findViewById(R.id.layoutItem);
        tvTitle.setText(item.getTitle());
        tvBody.setText(item.getBody());
        if (item.isImportant()) layoutItem.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));

        return view;
    }
}
