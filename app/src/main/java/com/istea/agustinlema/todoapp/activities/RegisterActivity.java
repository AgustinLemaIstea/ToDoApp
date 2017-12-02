package com.istea.agustinlema.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.database.loginDBHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText edtRegisterUser;
    EditText edtRegisterPassword;
    EditText edtRegisterPassword2;

    Button submitRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();
        setupSubmitRegisterButton();
    }

    private void setupUI() {
        edtRegisterUser = (EditText) findViewById(R.id.edtRegisterUser);
        edtRegisterPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtRegisterPassword2 = (EditText) findViewById(R.id.edtRegisterPassword2);
        submitRegisterButton = (Button) findViewById(R.id.submitRegisterButton);
    }

    private void setupSubmitRegisterButton() {
        submitRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        if (validateFields()) {
            String user = edtRegisterUser.getText().toString();
            String password = edtRegisterPassword.getText().toString();

            loginDBHelper dbHelper = loginDBHelper.getInstance(this);

            if (dbHelper.createUser(user, password)) {
                completeRegister();
            } else {
                edtRegisterUser.setError(getString(R.string.userCreateError));
            }
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (edtRegisterUser.getText().length() <= 0) {
            isValid = false;
            edtRegisterUser.setError(getString(R.string.loginErrorUserMissing));
        }

            if (edtRegisterPassword.getText().length() <= 0) {
            isValid = false;
            edtRegisterPassword.setError(getString(R.string.loginErrorPasswordMissing));
        }

        if (!edtRegisterPassword.getText().toString().equals(edtRegisterPassword2.getText().toString())){
            isValid=false;
            edtRegisterPassword2.setError(getString(R.string.passwordMismatch));
        }

        return isValid;
    }

    private void completeRegister() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }
}
