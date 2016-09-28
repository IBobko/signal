package ru.innopolis.messagino;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.thoughtcrime.securesms.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by i.minnakhmetov on 9/28/2016.
 */

public class ChatsDelayedMessagesList extends Activity {
    String[] delayedMessages = new String[]{" 1", "2", "3", "4"};
    public ChatsDelayedMessagesList() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_messages_list);

        ListView listView = (ListView)findViewById(R.id.delayedMessagesList);

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


        View header = getLayoutInflater().inflate(R.layout.header, null);
                listView.addHeaderView(header);

        SimpleAdapter adapter = new SimpleAdapter(this, myArrList, android.R.layout.simple_list_item_2,
                new String[] {"DateTime", "Message"},
                new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

        //ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, delayedMessages);
        //listView.setAdapter(adapter);

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }



}
