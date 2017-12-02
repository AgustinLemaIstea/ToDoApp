package com.istea.agustinlema.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.istea.agustinlema.todoapp.model.ToDoItem;

import java.util.ArrayList;

public class ItemDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "ISTEA_USUARIOS";
    static final int DB_VERSION=1;
    static final String ITEM_TABLE = "TodoItems";
    static final String ITEM_TITLE = "Title";
    static final String ITEM_BODY = "Body";
    static final String ITEM_ISIMPORTANT = "Important";

    static ItemDBHelper instance;

    private ItemDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public static ItemDBHelper getInstance(Context context){
        if (instance==null)
            instance = new ItemDBHelper(context);
        return instance;
    }

    /***
     * Se ejecuta cada vez que se instancia el DBHelper.
     * Sólo si la DB no estaba creada anteriormente.!!!
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + ITEM_TABLE+ " (id integer primary key, "
                + ITEM_TITLE + " text, " + ITEM_BODY + " text, "
                + ITEM_ISIMPORTANT + " boolean )");

        addMockData(sqLiteDatabase);
    }

    private void addMockData(SQLiteDatabase db){
        ToDoItem item1 = new ToDoItem(1);
        ToDoItem item2 = new ToDoItem(2);


        item1.setTitle("Irse a dormir");
        item1.setBody("Preparase para ir a dormir");

        item2.setTitle("Preparar la comida");
        item2.setBody("Hacer todo lo necesario para la cena de hoy");
        item2.setImportant(true);
        ArrayList<ToDoItem> items = new ArrayList<ToDoItem>();
        items.add(item1);
        items.add(item2);

        for (ToDoItem item : items){
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(ITEM_TITLE, item.getTitle());
                contentValues.put(ITEM_BODY, item.getBody());
                contentValues.put(ITEM_ISIMPORTANT, item.isImportant());
                db.insert(ITEM_TABLE, null, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Se ejecuta cuando se hace una modificación en la estructura de la base
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }

    public boolean saveTodoItem(ToDoItem item){
        if (item.getId()>0){
            return updateTodoItem(item);
        }
        else {
            return insertTodoItem(item);
        }
    }

    private boolean insertTodoItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(ITEM_TITLE, item.getTitle());
            contentValues.put(ITEM_BODY, item.getBody());
            contentValues.put(ITEM_ISIMPORTANT, item.isImportant());
            db.insert(ITEM_TABLE, null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateTodoItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(ITEM_TITLE, item.getTitle());
            contentValues.put(ITEM_BODY, item.getBody());
            contentValues.put(ITEM_ISIMPORTANT, item.isImportant());
            db.update(ITEM_TABLE, contentValues, "id = ?", new String[]{String.valueOf(item.getId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTodoItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(ITEM_TABLE, "id = ?", new String[]{String.valueOf(item.getId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ToDoItem> getTodoItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDoItem> items = new ArrayList<>();
        Cursor cur = db.rawQuery("select * from " + ITEM_TABLE, null);
        cur.moveToFirst();
        //Recorro con un cursor toda la DB
        try {
            while (!cur.isAfterLast()) {
                int id = cur.getInt(cur.getColumnIndex("id")); //Get an INT from column id
                String title = cur.getString(cur.getColumnIndex(ITEM_TITLE));
                String body =  cur.getString(cur.getColumnIndex(ITEM_BODY));
                boolean isImportant = cur.getInt(cur.getColumnIndex(ITEM_ISIMPORTANT)) > 0;

                ToDoItem item = new ToDoItem(id, title, body, isImportant);

                items.add(item);
                cur.moveToNext();
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ToDoItem getTodoItem(int id) {
        ToDoItem item=null;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from " + ITEM_TABLE + " WHERE ID=?"
                    ,new String[]{String.valueOf(id)}, null);
            //Cursor cur = db.query(TABLA_USUARIOS,null,"id = ?", new String[]{String.valueOf(id)},null,null,null);

            if (cur.moveToFirst()){
                String title = cur.getString(cur.getColumnIndex(ITEM_TITLE));
                String body =  cur.getString(cur.getColumnIndex(ITEM_BODY));
                boolean isImportant = cur.getInt(cur.getColumnIndex(ITEM_ISIMPORTANT)) > 0;

                item = new ToDoItem(id, title, body, isImportant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }
}
