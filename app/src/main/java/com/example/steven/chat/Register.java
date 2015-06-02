package com.example.steven.chat;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by steven on 1-6-15.
 */
public class Register extends Activity implements View.OnClickListener {
    private EditText password;
    private EditText username;
    private EditText email;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        this.email=(EditText)findViewById(R.id.email);
        this.username=(EditText)findViewById(R.id.user);
        this.password=(EditText)findViewById(R.id.pa);
        this.register=(Button)findViewById(R.id.reg);
        register.setOnClickListener(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        View.OnFocusChangeListener temp= new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    hideKeyboard(v);
            }
        };
        username.setOnFocusChangeListener(temp);
        password.setOnFocusChangeListener(temp);
        email.setOnFocusChangeListener(temp);
    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onClick(View v) {
        String tempuser = this.username.getText().toString();
        String temppass = this.password.getText().toString();
        String tempemail=this.email.getText().toString();

        if (tempuser.isEmpty() || temppass.isEmpty()||tempemail.isEmpty()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Fill out every input");

            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else{
            final ProgressDialog dia = ProgressDialog.show(this, null,
                  "waiting");
            final ParseUser pu = new ParseUser();
            pu.setEmail(tempemail);
            pu.setPassword(temppass);
            pu.setUsername(tempuser);
            pu.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    dia.dismiss();
                    if (e == null)
                    {
                        Log.e(getClass().getName(),"hello");
                        setResult(RESULT_OK);
                        finish();

                    }
                    else
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Register.this);
                        builder1.setMessage(e.getMessage());

                        builder1.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        Log.e(getClass().getName(),e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
