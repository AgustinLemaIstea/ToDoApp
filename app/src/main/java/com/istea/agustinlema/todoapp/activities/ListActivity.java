package com.istea.agustinlema.todoapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.ToDoItemAdapter;
import com.istea.agustinlema.todoapp.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ListView lvToDoItems;
    private List<ToDoItem> todoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupUI();
        initializeListView();
    }

    private void setupUI() {
        lvToDoItems = (ListView) findViewById(R.id.lvToDoItems);
    }

    private void initializeListView() {

        todoItems = new ArrayList<ToDoItem>();
        ToDoItem item1 = new ToDoItem(1);
        ToDoItem item2 = new ToDoItem(2);


        item1.setTitle("Irse a dormir");
        item1.setBody("Preparase para ir a dormir");

        item2.setTitle("Preparar la comida");
        item2.setBody("Hacer todo lo necesario para la cena de hoy");
        item2.setImportant(true);

        todoItems.add(item1);
        todoItems.add(item2);
        todoItems.add(item1);

        ToDoItemAdapter adapter = new ToDoItemAdapter(ListActivity.this, R.layout.item_todoitem, todoItems);
        lvToDoItems.setAdapter(adapter);


        lvToDoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("SUPERLOG", "onItemClick: "+todoItems.get(position).getTitle());
                Toast.makeText(ListActivity.this, todoItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
