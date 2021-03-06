package com.istea.agustinlema.todoapp.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.ToDoItemAdapter;
import com.istea.agustinlema.todoapp.async.Callback;
import com.istea.agustinlema.todoapp.database.ItemDBHelper;
import com.istea.agustinlema.todoapp.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lvToDoItems;
    private List<ToDoItem> todoItems;

    private Toolbar toolbarList;

    private ListView lvDrawer;

    private boolean startedFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        startedFlag=false;

        setupUI();
        setupToolbar();
        initializeDrawer();

        //Create XML with default values
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);
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
        getSupportActionBar().setTitle(R.string.titleTaskList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.menuConfig) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.menuAdd) {
            Intent intent = new Intent(this,ItemEditActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeListView() {

        ItemDBHelper dbHelper = ItemDBHelper.getInstance(this);

        //Forma divertida de usar async task. Le paso un objeto callback a mi método del dbhelper
        //Este método se ejecutará usando un async task y luego ejecutará mi callback con el resultado
        //Tuve que usar RunOnUiThread porque las para modificar vistas necesito ejecutar en el hilo de UI
        dbHelper.getTodoItems(new Callback<ArrayList<ToDoItem>>() {
            @Override
            public void onFinish(final ArrayList<ToDoItem> result) {
                runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run()
                    {
                        todoItems=result;
                        showList();
                        if (!startedFlag){ //Run only at start
                            showNotification();
                            startedFlag=true;
                        }
                    }
                });
            }
        });
    }

    private void showList(){
        ToDoItemAdapter adapter = new ToDoItemAdapter(ListActivity.this, R.layout.item_todoitem, todoItems);
        lvToDoItems.setAdapter(adapter);


        lvToDoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                goToViewItem(todoItems.get(position).getId());
            }
        });
    }

    private void goToViewItem(int itemID){
        Intent intent = new Intent(ListActivity.this,ItemViewActivity.class);
        intent.putExtra(getString(R.string.extrasItemID),itemID);
        startActivity(intent);
    }

    private void initializeDrawer() {
        lvDrawer = (ListView) findViewById(R.id.lvDrawer);
        final String[] drawerItems = getResources().getStringArray(R.array.drawerItems);


        ArrayAdapter adapter = new ArrayAdapter(ListActivity.this
                , android.R.layout.simple_list_item_1, drawerItems);

        lvDrawer.setAdapter(adapter);

        lvDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, AboutMeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showNotification(){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ListActivity.this);
        builder.setContentTitle(String.format(getString(R.string.notificationPendingTasks),todoItems.size()))
                .setContentText(getString(R.string.notificationAddMore))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);



        Intent intent = new Intent(this,ItemEditActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ListActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);//pending intent
        builder.setContentIntent(pendingIntent);

        NotificationManager managerCompat = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//obtengo el servicio de notificaciones y se lo pongo al manager
        managerCompat.notify(1, builder.build());    //le paso un id y el builder

    }
}
