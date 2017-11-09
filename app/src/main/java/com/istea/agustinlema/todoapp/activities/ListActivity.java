package com.istea.agustinlema.todoapp.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.ToDoItemAdapter;
import com.istea.agustinlema.todoapp.database.ItemDBHelper;
import com.istea.agustinlema.todoapp.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ListView lvToDoItems;
    private List<ToDoItem> todoItems;

    private Toolbar toolbarList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupUI();
        setupToolbar();
        initializeListView();
        showNotification();
    }

    @Override
    protected void onResume() {
        initializeListView();
        super.onResume();
    }

    private void setupUI() {
        lvToDoItems = (ListView) findViewById(R.id.lvToDoItems);

        toolbarList = (Toolbar) findViewById(R.id.toolbarList);
    }

    private void setupToolbar(){
        setSupportActionBar(toolbarList);
        getSupportActionBar().setTitle("Tareas pendientes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.menuConfig) {
            Toast.makeText(this, "Muestro config", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this,SettingsActivity.class);
            //startActivity(intent);
        }
        else if (item.getItemId() == R.id.menuAdd) {
            Toast.makeText(this, "Agregar elemento", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,ItemEditActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeListView() {

        ItemDBHelper dbHelper = ItemDBHelper.getInstance(this);
        todoItems = dbHelper.getTodoItems();

        ToDoItemAdapter adapter = new ToDoItemAdapter(ListActivity.this, R.layout.item_todoitem, todoItems);
        lvToDoItems.setAdapter(adapter);


        lvToDoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("SUPERLOG", "onItemClick: "+todoItems.get(position).getTitle());
                goToViewItem(todoItems.get(position).getId());
            }
        });
    }

    private void goToViewItem(int itemID){
        Intent intent = new Intent(ListActivity.this,ItemViewActivity.class);
        intent.putExtra("ITEMID",itemID);
        startActivity(intent);
    }

    private void showNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ListActivity.this);
        builder.setContentTitle(String.format("Tenés %d tareas pendientes.",todoItems.size()))
                .setContentText("Presioná acá para agregar una más.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);



        Intent intent = new Intent(this,ItemEditActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ListActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);//pending intent
        builder.setContentIntent(pendingIntent);

        NotificationManager managerCompat = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//obtengo el servicio de notificaciones y se lo pongo al manager
        managerCompat.notify(1, builder.build());    //le paso un id y el builder

    }
}
