package ru.innopolis.messagino;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.thoughtcrime.securesms.ContactSelectionActivity;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.crypto.MasterSecret;

import java.util.ArrayList;
import java.util.HashMap;

public class DelayedMessagesListActivity extends Activity {
    private Button addButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chats_delayed_messages_list, menu);
        return true;

    }

    @Override
    protected void onCreate(Bundle bundle) {

        setContentView(R.layout.activity_delayed_message_list);
        this.setTitle("dsds");
        ListView listView = (ListView)findViewById(R.id.delayedMessagesList);
        //addButton = (Button)findViewById(R.id.addButton);

        ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

// First delayed message
        map = new HashMap<String, String>();
        map.put("DateTime", "29/09/2016 18:00");
        map.put("Message", "Please don't forgot  give me money");
        myArrList.add(map);

// Second delayed message
        map = new HashMap<String, String>();
        map.put("DateTime", "01/10/2016 09:00");
        map.put("Message", "C днем пожилых людей");
        myArrList.add(map);

// Third delayed message
        map = new HashMap<String, String>();
        map.put("DateTime", "4/10/2016 09:00");
        map.put("Message", "С днем космических войск");
        myArrList.add(map);

// Fourd delayed message
        map = new HashMap<String, String>();
        map.put("DateTime", "5/10/2016 09:00");
        map.put("Message", "Всех TA  с днем учителя :)");
        myArrList.add(map);


        //View header = getLayoutInflater().inflate(R.layout.header, null);
        //        listView.addHeaderView(header);

        SimpleAdapter adapter = new SimpleAdapter(this, myArrList, android.R.layout.simple_list_item_2,
                new String[] {"DateTime", "Message"},
                new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        // Add code to do when first item gets clicked
//                        Intent nIntent = null;
//                        try {
//                            nIntent = new Intent(ChatsDelayedMessagesList.this,Class.forName("com.anuraag.myapplication.NewActivity"));
//                        } catch (ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        startActivity(nIntent);
//                        break;
//                    case 1:
//                        // Add code to do when second item gets clicked
//                        break;
//                    case 2:
//                        // Add code to do when third item gets clicked
//                        break;
//                    case 3:
//                        // Add code to do when fourth item gets clicked
//                        break;
//                }
//            }
//        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 1:
//                        // Add code to do when first item gets long clicked
//                        Toast.makeText(ChatsDelayedMessagesList.this,"First Item Long Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 2:
//                        // Add code to do when second item gets long clicked
//                        Toast.makeText(ChatsDelayedMessagesList.this,"Second Item Long Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 3:
//                        // Add code to do when third item gets long clicked
//                        Toast.makeText(ChatsDelayedMessagesList.this,"Third Item Long Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 4:
//                        // Add code to do when fourth item gets long clicked
//                        Toast.makeText(ChatsDelayedMessagesList.this,"Fourth Item Long Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//                    case 5:
//                        // Add code to do when fourth item gets long clicked
//                        Toast.makeText(ChatsDelayedMessagesList.this,"Fшму Item Long Clicked",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return true;
//            }
//        });

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    public void butAdd_Click(View v){
        Intent intent = new Intent(this, DelayedMessageActivity.class);
        startActivity(intent);
    }

    void alert(String message) {

    }


}
