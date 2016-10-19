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

public class All_delayed_message_list extends BaseActionBarActivity {
    private Menu menu;
    private SimpleAdapter adapter;
    private ListView listView;
    private HashMap<String, String> map;
    private int currentItemKeyValue;
    private MenuItem deleteButtonItem;
    private MenuItem addButtonItem;
    private ArrayList<HashMap<String, String>> myArrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_delayed_message_list);
        All_delayed_message_list.this.setTitle("Запланированные сообщения");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        All_delayed_message_list.this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_all_delayed_messages_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_all_delayed_message_list);
        listView = (ListView) findViewById(R.id.alldelayedMessagesList);
        listView.setLongClickable(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myArrList = new ArrayList<HashMap<String, String>>();


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

        DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(All_delayed_message_list.this);

        for (final DelayedMessageData delayedMessageData : delayedMessage.getMessages()) {

            map.put("DateTime", "5/10/2016 09:00");
            map.put("Message", delayedMessageData.getText());
            map.put("ID", delayedMessageData.getId().toString());
            myArrList.add(map);
        }

        adapter = new SimpleAdapter(this, myArrList, android.R.layout.simple_list_item_2,
                new String[]{"DateTime", "Message"},
                new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                deleteButtonItem = (MenuItem) menu.findItem(R.id.deleteItem);
                HashMap<String, String> obj = (HashMap<String, String>) arg0.getItemAtPosition(pos);
                currentItemKeyValue = pos;

                deleteButtonItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        myArrList.remove(currentItemKeyValue);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                });

                deleteButtonItem.setVisible(true);
                deleteButtonItem.setShowAsAction(2); //show always
                listView.setItemChecked(pos, true);
                arg1.setSelected(true);
                System.out.println("Set selected item");
                return true;
            }
        });

        //266013 Send to Edit message preview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map row = (Map) listView.getAdapter().getItem(position);
                Intent myIntent = new Intent(All_delayed_message_list.this, DelayedMessageActivity.class);
                myIntent.putExtra("MESSAGE_ID", (String) row.get("ID")); //Optional parameters
                myIntent.putExtra("MESSAGE_TEXT", (String) row.get("Message")); //Optional parameters
                All_delayed_message_list.this.startActivity(myIntent);
            }
        });
    }



}
