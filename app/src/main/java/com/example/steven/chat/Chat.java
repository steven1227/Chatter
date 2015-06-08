package com.example.steven.chat;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.text.format.DateUtils;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Chat extends Activity {

    private ArrayList<Conversation> converlist;
    private String buddy;
    private static Handler hand;
    private boolean isRunning;
    private chatAdapter adapter;
    private EditText txt;
    private Date lastMsgDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        converlist=new ArrayList<Conversation>();
        ListView list=(ListView)findViewById(R.id.listView);
        adapter=new chatAdapter();
        list.setAdapter(adapter);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        txt=(EditText)findViewById(R.id.messege);


        Button a=(Button)findViewById(R.id.send);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        Intent name=getIntent();
        buddy=name.getStringExtra("name");
        getActionBar().setTitle(buddy);
        hand=new Handler();
        Log.e("is it true?",name.getStringExtra("name"));
    }

    private void sendMessage() {

        if(txt.length()==0)
            return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s=txt.getText().toString();
        final Conversation te=new Conversation(s,new Date(),UserList.user.getUsername());
        te.setStatus(Conversation.STATUS_SENDING);
        converlist.add(te);
        adapter.notifyDataSetChanged();
        txt.setText(null);
        ParseObject po = new ParseObject("Chat");
        po.put("sender", UserList.user.getUsername());
        po.put("receiver", buddy);
        // po.put("createdAt", "");
        po.put("message", s);
        po.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    te.setStatus(Conversation.STATUS_SENT);
                else {
                    te.setStatus(Conversation.STATUS_FAILED);
                    Log.e("what happened?",e.getMessage());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        loadConversationList();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        isRunning = false;
    }


    private void loadConversationList(){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
        if(converlist.size()==0){
            ArrayList<String>al=new ArrayList<>();
            al.add(buddy);
            al.add(UserList.user.getUsername());
            q.whereContainedIn("sender", al);
            q.whereContainedIn("receiver",al);
        }else {
            if (lastMsgDate != null)
                q.whereGreaterThan("createdAt", lastMsgDate);
            q.whereEqualTo("sender", buddy);
            q.whereEqualTo("receiver", UserList.user.getUsername());
            
        }
        q.orderByDescending("createdAt");
        q.setLimit(30);
        q.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(list!=null && list.size()>0){

                    for(ParseObject i:list){
                     Conversation t=new Conversation(i.getString("message"),i.getCreatedAt(),i.getString("sender"));
                        converlist.add(t);
                        if(lastMsgDate==null||lastMsgDate.before(t.getDate()));
                        lastMsgDate=t.getDate();
                    }
                    adapter.notifyDataSetChanged();
                }


                hand.postDelayed(new Runnable() {

                    @Override
                    public void run()
                    {
                        if (isRunning)
                            loadConversationList();
                    }
                }, 1000);
            }
        });



        }

    class chatAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return converlist.size();
        }

        @Override
        public Object getItem(int position) {
            return converlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Conversation temp=(Conversation)getItem(position);
            if(temp.isSent())
                convertView=getLayoutInflater().inflate(R.layout.chat_sent,null);
            else
                convertView=getLayoutInflater().inflate(R.layout.chat_receive,null);
            TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, temp
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));
            lbl = (TextView) convertView.findViewById(R.id.lbl2);
            lbl.setText(temp.getMsg());

            lbl = (TextView) convertView.findViewById(R.id.lbl3);
            if(temp.isSent()){
                if(temp.getStatus()==Conversation.STATUS_FAILED)
                    lbl.setText("failed");
                else if (temp.getStatus()==Conversation.STATUS_SENDING)
                    lbl.setText("Sending...");
                else
                    lbl.setText("");

            }
            else
            {
                lbl.setText("");
            }

            return convertView;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.chat_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
