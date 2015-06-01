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

    }

    @Override
    public void onClick(View v) {
        String tempuser = this.username.getText().toString();
        String temppass = this.password.getText().toString();
        String tempemail=this.email.getText().toString();

        if (tempuser.isEmpty() || temppass.isEmpty()||tempemail.isEmpty()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Fill every input");

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
                        //UserList.user = pu;// change anotehr method
                        //startActivity(new Intent(getApplicationContext(), UserList.class));
                        Log.e(getClass().getName(),"hello");
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                    {
                        Log.e(getClass().getName(),e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
