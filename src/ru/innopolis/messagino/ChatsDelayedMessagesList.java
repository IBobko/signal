package ru.innopolis.messagino;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.DelayedMessageDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by i.minnakhmetov on 9/28/2016.
 */

public class ChatsDelayedMessagesList extends Activity {

    private Button addButton;
    public ChatsDelayedMessagesList() {
        super();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.delay_messages_list);

        final ListView listView = (ListView)findViewById(R.id.delayedMessagesList);
        addButton = (Button)findViewById(R.id.addButton);

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


        DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(ChatsDelayedMessagesList.this);

        for (final DelayedMessageData delayedMessageData: delayedMessage.getMessages()) {
            map = new HashMap<>();
            map.put("DateTime", "5/10/2016 09:00");
            map.put("Message", delayedMessageData.getText());
            map.put("ID", delayedMessageData.getId().toString());
            myArrList.add(map);
        }


        View header = getLayoutInflater().inflate(R.layout.header, null);
        listView.addHeaderView(header);

        SimpleAdapter adapter = new SimpleAdapter(this, myArrList, android.R.layout.simple_list_item_2,
                new String[] {"DateTime", "Message"},
                new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map row = (Map)listView.getAdapter().getItem(position);
                Intent myIntent = new Intent(ChatsDelayedMessagesList.this, DelayedMessageActivity.class);
                myIntent.putExtra("MESSAGE_ID", (String)row.get("ID")); //Optional parameters
                myIntent.putExtra("MESSAGE_TEXT", (String)row.get("Message")); //Optional parameters
                ChatsDelayedMessagesList.this.startActivity(myIntent);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    public void butAdd_Click(View v){
        Intent intent = new Intent(this, DelayedMessageActivity.class);
        startActivity(intent);
    }


}
