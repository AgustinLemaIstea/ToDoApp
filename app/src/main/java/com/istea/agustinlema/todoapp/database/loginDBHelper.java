package com.istea.agustinlema.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.istea.agustinlema.todoapp.model.ToDoItem;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class loginDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "Login";
    static final int DB_VERSION=1;
    static final String USER_TABLE = "Users";
    static final String USERNAME = "User";
    static final String PASSWORD_HASH = "PasswordHash";

    static loginDBHelper instance;

    private loginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public static loginDBHelper getInstance(Context context){
        if (instance==null)
            instance = new loginDBHelper(context);
        return instance;
    }

    /***
     * Se ejecuta cada vez que se instancia el DBHelper.
     * Sólo si la DB no estaba creada anteriormente.!!!
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + USER_TABLE+ " (id integer primary key, "
                + USERNAME + " text unique, " + PASSWORD_HASH + " text)");
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

    public boolean createUser(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(USERNAME, user);
            contentValues.put(PASSWORD_HASH, getDigested(password));
            db.insert(USER_TABLE, null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getDigested(String password) throws NoSuchAlgorithmException {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(password.getBytes());
            byte[] md5sum = messageDigest.digest();
            return String.format("%032X", new BigInteger(1, md5sum));
    }

    public boolean validateUser(String user, String password) {

        boolean isValid=false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("select * from " + USER_TABLE + " WHERE "+USERNAME+"=? AND "+PASSWORD_HASH+"=?"
                    ,new String[]{user, getDigested(password)}, null);

            if (cur.getCount()==1){
                isValid=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
