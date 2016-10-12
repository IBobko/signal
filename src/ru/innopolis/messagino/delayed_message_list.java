package ru.innopolis.messagino;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.thoughtcrime.securesms.BaseActionBarActivity;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.DelayedMessageDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class delayed_message_list extends BaseActionBarActivity {
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_message_list);
        delayed_message_list.this.setTitle("Запланированные сообщения");
        //ChatsDelayedMessagesListActivity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        delayed_message_list.this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_chats_delayed_messages_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_delayed_messages_list);

        final ListView listView = (ListView) findViewById(R.id.delayedMessagesList);
        listView.setLongClickable(true);
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

        DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(delayed_message_list.this);

        for (final DelayedMessageData delayedMessageData : delayedMessage.getMessages()) {
            map = new HashMap<>();
            map.put("DateTime", "5/10/2016 09:00");
            map.put("Message", delayedMessageData.getText());
            map.put("ID", delayedMessageData.getId().toString());
            myArrList.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, myArrList, android.R.layout.simple_list_item_2,
                new String[]{"DateTime", "Message"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                //MenuItem item = (MenuItem) menu.findItem(R.id.deleteItem);
                //menu.setGroupVisible(R.id.deleteItem, true);
                MenuItem item = null;
                if (menu != null){
                    item = (MenuItem) menu.findItem(R.id.deleteItem);
                }
                item.setVisible(true);
                item.setShowAsAction(2); //show always
                return true;
            }
        });

        //266013 Send to Edit message preview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map row = (Map) listView.getAdapter().getItem(position);
                Intent myIntent = new Intent(delayed_message_list.this, DelayedMessageActivity.class);
                myIntent.putExtra("MESSAGE_ID", (String) row.get("ID")); //Optional parameters
                myIntent.putExtra("MESSAGE_TEXT", (String) row.get("Message")); //Optional parameters
                delayed_message_list.this.startActivity(myIntent);
            }
        });
    }

}
