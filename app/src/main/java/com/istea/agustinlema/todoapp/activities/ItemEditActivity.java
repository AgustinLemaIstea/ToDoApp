package com.istea.agustinlema.todoapp.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.database.ItemDBHelper;
import com.istea.agustinlema.todoapp.model.ToDoItem;

public class ItemEditActivity extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtBody;
    private CheckBox chkImportant;

    private Button btnSubmit;

    private ToDoItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);
        setupUI();
        setupSubmitButton();
        loadItem();
    }

    private void setupUI() {
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtBody = (EditText) findViewById(R.id.edtBody);
        chkImportant = (CheckBox) findViewById(R.id.chkImportant);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private boolean validateFields() {
        boolean isValid=true;

        if (edtTitle.getText().length()<=0){
            isValid=false;
            edtTitle.setError("Debe ingresar un título");
        }

        if (edtBody.getText().length()>50){
            isValid=false;
            edtBody.setError("El máximo de caracteres es 50");
        }

        return isValid;
    }

    private void saveToDB() {

        if (item == null){
            item = new ToDoItem(0);
        }
        item.setTitle(edtTitle.getText().toString());
        item.setBody(edtBody.getText().toString());
        item.setImportant(chkImportant.isChecked());

        ItemDBHelper dbHelper = ItemDBHelper.getInstance(this);
        dbHelper.saveTodoItem(item);
    }

    private void promptGoBack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemEditActivity.this);

        builder.setTitle("Volver")
                .setMessage("¿Desea volver y perder los cambios?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void submitForm() {
        if (validateFields()){
            Log.d("superlog", "onClick: es valido");
            saveToDB();
            Toast.makeText(ItemEditActivity.this, "Tarea guardada", Toast.LENGTH_SHORT).show();
            //Volver a pantalla anterior.
            finish();
        }
    }

    private void loadItem() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int itemID = extras.getInt("ITEMID");
            ItemDBHelper dbHelper = ItemDBHelper.getInstance(this);
            this.item = dbHelper.getTodoItem(itemID);
            loadFormData(item);
        }
    }

    private void loadFormData(ToDoItem item){
        edtTitle.setText(item.getTitle());
        edtBody.setText(item.getBody());
        chkImportant.setChecked(item.isImportant());
    }

    @Override
    public void onBackPressed() {
        this.promptGoBack();
    }
}
