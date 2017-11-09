package com.istea.agustinlema.todoapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.database.ItemDBHelper;
import com.istea.agustinlema.todoapp.model.ToDoItem;

public class ItemViewActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvBody;
    private CheckBox chkImportant;

    private Toolbar toolbarView;

    private ToDoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        setupUI();
        loadData();
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void setupUI() {
        tvTitle = (TextView) findViewById(R.id.tvTitleView);
        tvBody = (TextView) findViewById(R.id.tvBodyView);
        chkImportant = (CheckBox) findViewById(R.id.chkImportantView);
    }

    private void loadData() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Log.d("SUPERLOG", "loadData: "+extras.getInt("ITEMID"));
            int itemID = extras.getInt("ITEMID");
            ItemDBHelper dbHelper = ItemDBHelper.getInstance(this);
            ToDoItem item = dbHelper.getTodoItem(itemID);

            if (item!= null){
                mapItemToForm(item);
                this.item=item;
            } else {
                Toast.makeText(this, "Item inexistente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void mapItemToForm(ToDoItem item) {
        tvTitle.setText(item.getTitle());
        tvBody.setText(item.getBody());
        chkImportant.setChecked(item.isImportant());
    }

    private void setupToolbar(){
        toolbarView = (Toolbar) findViewById(R.id.toolbarView);
        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Only go back one level
        getSupportActionBar().setTitle("Ver tarea");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        //TODO: Cambiar a switch?
        if ( menuItem.getItemId() == R.id.menuEdit) {
            //Intent intent = new Intent(ItemViewActivity.this,ItemEditActivity.class);
            //intent.putExtra("ITEMID",item.getId());
            //startActivity(intent);
        } else if (menuItem.getItemId() == R.id.menuDelete) {
            //showDeleteConfirmation();
        } else if (menuItem.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
