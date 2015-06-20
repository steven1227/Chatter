package com.example.steven.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class UserList extends Activity {

    private ArrayList<ParseUser> userlist;
    public static ParseUser user;
    private  ListView chatlist;
    private UserAdapter adapter;
    @Override
    protected void onResume() {
        super.onResume();
        loadUserList();
    }

    private void loadUserList() {
        Log.e(Thread.currentThread().getName(),"who are you");
        final ProgressDialog dia=ProgressDialog.show(this,"this is title","connecting");
        ParseUser.getQuery().whereNotEqualTo("username",this.user.getUsername()).findInBackground(
                new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        Log.e(Thread.currentThread().getName(),"what am I");
                        dia.dismiss();
                        if (list != null) {
                            Log.e("help", " " + list.size());
                            if (list.size() == 0) {
                                Toast.makeText(getApplicationContext(), "Your do not have any friends now", Toast.LENGTH_SHORT).show();
                            } else {

                                userlist=new ArrayList<ParseUser>(list);
                                adapter.notifyDataSetChanged();

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
        this.chatlist=(ListView)findViewById(R.id.chatlist);
        userlist=new ArrayList<ParseUser>();
        this.adapter=new UserAdapter();
        chatlist.setAdapter(adapter);
        chatlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent a = new Intent(UserList.this, Chat.class);
                a.putExtra("name",(userlist).get(position).getUsername());
                startActivity(a);
            }
        });
        Updatatask task=new Updatatask();
        task.execute();
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



    private class UserAdapter extends BaseSwipeAdapter {
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
        public int getSwipeLayoutResourceId(int i) {
            return R.id.swipe;
        }

        @Override
        public View generateView(int i, ViewGroup viewGroup) {
            return getLayoutInflater().inflate(R.layout.chatitem, null);
        }

        @Override
        public void fillValues(int i, View view) {
            SwipeLayout s= (SwipeLayout)view.findViewById(R.id.swipe);
            final int temp=i;
            s.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout swipeLayout) {

                }

                @Override
                public void onOpen(SwipeLayout swipeLayout) {
                    Log.e("hello, i am" + temp, "i find" + getSwipeLayoutResourceId(temp));
                }

                @Override
                public void onStartClose(SwipeLayout swipeLayout) {

                }

                @Override
                public void onClose(SwipeLayout swipeLayout) {

                }

                @Override
                public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

                }

                @Override
                public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

                }
            });
            TextView t = (TextView) view.findViewById(R.id.position);
            ImageView a=(ImageView)view.findViewById(R.id.delete);
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(v.toString(),"you click on "+temp);
                }
            });
            t.setText(userlist.get(i).getUsername());
            if (userlist.get(i).getBoolean("online")) {
                t.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online
                        , 0, R.drawable.arrow, 0);
            } else {
                t.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline
                        , 0, R.drawable.arrow, 0);
            }
        }
    }
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView==null){
//                convertView=getLayoutInflater().inflate(R.layout.chatitem,null);
//            }
//            ((TextView)convertView).setText(userlist.get(position).getUsername());
//            if(userlist.get(position).getBoolean("online")) {
//                ((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online
//                        , 0, R.drawable.arrow, 0);
//            }
//            else {
//                ((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offline
//                        , 0, R.drawable.arrow, 0);
//            }
//            return convertView;
//        }


    private class Updatatask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            while (true){
                try {
                    Log.e(Thread.currentThread().getName(),"haha");
                    ParseUser.getQuery().whereNotEqualTo("username",user.getUsername()).findInBackground(
                            new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> list, ParseException e) {
                                    Log.e(Thread.currentThread().getName(), "what am I");
                                    if (list != null) {
                                        if (list.size() == 0) {

                                        } else {

                                            userlist = new ArrayList<ParseUser>(list);
                                            adapter.notifyDataSetChanged();
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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(Thread.currentThread().getName(),e.getMessage());
                }
            }

        }
    }
}
