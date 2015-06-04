package com.example.steven.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                        if (list != null) {
                            Log.e("help", " " + list.size());
                            if (list.size() == 0) {
                                Toast.makeText(getApplicationContext(), "Your do not have any friends now", Toast.LENGTH_SHORT).show();
                            } else {
                                ListView chatlist=(ListView)findViewById(R.id.chatlist);
                                userlist=new ArrayList<ParseUser>(list);
                                chatlist.setAdapter(new UserAdapter());
                                chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent a=new Intent(UserList.this,Chat.class);
                                        startActivity(a);
                                    }
                                });

                            }


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(UserList.this);
                            builder1.setMessage(e.getMessage());
                            builder1.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
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

    private class UserAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userlist.size();
        }

        @Override
        public Object getItem(int position) {
            return userlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.chatitem,null);
            }
            ((TextView)convertView).setText(userlist.get(position).getUsername());
            ((TextView)convertView).setCompoundDrawablesRelativeWithIntrinsicBounds((userlist.get(position)).getBoolean("online") ? R.drawable.ic_online
                    : R.drawable.ic_offline, 0, R.drawable.arrow, 0);
            return convertView;
        }
    }
}
