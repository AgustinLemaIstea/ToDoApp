package com.istea.agustinlema.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.istea.agustinlema.todoapp.R;
import com.istea.agustinlema.todoapp.database.loginDBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText edtLoginUser;
    EditText edtLoginPassword;

    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI();
        setupLoginButton();
        setupRegisterButton();
    }

    private void setupUI() {
        edtLoginUser = (EditText) findViewById(R.id.edtLoginUser);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

    private void setupLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void setupRegisterButton() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitForm() {
        if (validateFields()) {
            String user = edtLoginUser.getText().toString();
            String password = edtLoginPassword.getText().toString();

            if (checkUserPasswordCombination(user, password)) {
                completeLogin();
            } else {
                edtLoginPassword.setError(getString(R.string.loginInvalid));
            }
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (edtLoginUser.getText().length() <= 0) {
            isValid = false;
            edtLoginUser.setError(getString(R.string.loginErrorUserMissing));
        }

        if (edtLoginPassword.getText().length() <= 0) {
            isValid = false;
            edtLoginPassword.setError(getString(R.string.loginErrorPasswordMissing));
        }

        return isValid;
    }

    private boolean checkUserPasswordCombination(String user, String password) {
        loginDBHelper dbHelper = loginDBHelper.getInstance(this);
        return (dbHelper.validateUser(user,password));
    }

    private void completeLogin() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }
}
