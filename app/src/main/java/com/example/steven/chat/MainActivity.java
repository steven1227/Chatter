package com.example.steven.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.*;
import com.parse.ParseAnalytics;


public class MainActivity extends Activity implements View.OnClickListener {

    private EditText password;
    private EditText username;
    private Button login;
    private Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "ULXtENRhpePAZUfiJOTqTaKqxOPoD6P0rfB75gJW", "URzkbWIzb9OEEqrKbeARtQ4b3aiByyiqoyXg7S3S");
        setContentView(R.layout.activity_main);
        password=(EditText)findViewById(R.id.password);
        username=(EditText)findViewById(R.id.username);
        login=(Button)findViewById(R.id.button);
        signup=(Button)findViewById(R.id.button2);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            Toast.makeText(this,"Sign up Successful",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        String tempuser = this.username.getText().toString();
        String temppass = this.password.getText().toString();

        if (v.getId()==R.id.button) {

            if (tempuser.isEmpty() || temppass.isEmpty()) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                if (tempuser.isEmpty()) {
                    builder1.setMessage("Input your username");
                } else {
                    builder1.setMessage("Input your password");
                }
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
                ProgressDialog dia = ProgressDialog.show(this, null, "tesing");
            }
        }
        else
        {
            Intent a=new Intent(this,Register.class);
            startActivityForResult(a, 10);
        }
    }
}
