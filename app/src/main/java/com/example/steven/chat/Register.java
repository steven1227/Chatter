package com.example.steven.chat;
import com.daimajia.swipe.SwipeLayout;
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
import android.widget.Toast;

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
        Log.e(this.getClass().getName(), this.getLocalClassName());
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

        SwipeLayout swipeLayout =  (SwipeLayout)findViewById(R.id.sample1);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipeLayout.addOnLayoutListener(new SwipeLayout.OnLayout() {
            @Override
            public void onLayout(SwipeLayout swipeLayout) {
                Toast.makeText(getApplication(),"put on it",Toast.LENGTH_SHORT).show();
            }
        });
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                Toast.makeText(getApplication(),"who am I",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                Toast.makeText(getApplication(),"what am I doing",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {
                Log.e(this.getClass().getName(),i+" "+i1+"are you ?"+getLocalClassName());
            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getName(),"are you working");
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
                        Log.e(getClass().getName(), "hell2");

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
