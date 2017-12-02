package com.istea.agustinlema.todoapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.ToDoItemAdapter;
import com.istea.agustinlema.todoapp.async.Callback;
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
        if (extras != null) {
            int itemID = extras.getInt(getString(R.string.extrasItemID));

            MyAsyncTask async = new MyAsyncTask();
            async.execute(itemID);
        }
    }

    private void mapItemToForm(ToDoItem item) {
        tvTitle.setText(item.getTitle());
        tvBody.setText(item.getBody());
        chkImportant.setChecked(item.isImportant());
    }

    private void setupToolbar() {
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
        if (menuItem.getItemId() == R.id.menuEdit) {
            Intent intent = new Intent(ItemViewActivity.this, ItemEditActivity.class);
            intent.putExtra(getString(R.string.extrasItemID), item.getId());
            startActivity(intent);
        } else if (menuItem.getItemId() == R.id.menuDelete) {
            showDeleteConfirmation();
        } else if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showDeleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = item.getTitle();

        builder.setTitle(R.string.titleDeleteUser)
                .setMessage(String.format(getString(R.string.dialogDeleteUser), title))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ItemDBHelper dbHelper = ItemDBHelper.getInstance(ItemViewActivity.this);
                        String message;
                        if (dbHelper.deleteTodoItem(item)) {
                            message = getString(R.string.itemDeleted);
                        } else {
                            message = getString(R.string.errorItemDelete);
                        }
                        Toast.makeText(ItemViewActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, ToDoItem> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ToDoItem doInBackground(Integer... itemIDs) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            ItemDBHelper dbHelper = ItemDBHelper.getInstance(ItemViewActivity.this);

            ToDoItem item = dbHelper.getTodoItemSync(itemIDs[0]);
            return item;
        }

        //Recibe lo que sale de doInBackground
        //Se ejecuta en el thread de UI
        @Override
        protected void onPostExecute(ToDoItem item) {
            super.onPostExecute(item);

            if (item != null) {
                mapItemToForm(item);
                ItemViewActivity.this.item = item;
            } else {
                Log.e("ItemViewActivity", "loadData: non existing item");
                finish();
            }
        }
    }
}

