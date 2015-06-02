package com.example.steven.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class UserList extends Activity {

    private ArrayList<ParseUser> userlist;
    public static ParseUser user;

    @Override
    protected void onResume() {
        super.onResume();
        loadUserList();
    }

    private void loadUserList() {
        final ProgressDialog dia=ProgressDialog.show(this,"this is title","connecting to server");
        ParseUser.getQuery().whereNotEqualTo("username",this.user.getUsername()).findInBackground(
                new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        dia.dismiss();
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent a = this.getIntent();
        Log.e("key", a.getStringExtra("key"));
        updateUserStatus(true);
    }

    private void updateUserStatus(boolean online)
    {
        user.put("online", online);
        user.saveEventually();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        updateUserStatus(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
}
