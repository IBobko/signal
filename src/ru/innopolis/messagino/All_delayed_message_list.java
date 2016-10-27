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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class All_delayed_message_list extends BaseActionBarActivity {
    private Menu menu;
    private SimpleAdapter adapter;
    private ListView listView;
    private MenuItem deleteButtonItem;
    private List<DelayedMessageData> listOfMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_delayed_message_list);
        setTitle(R.string.all_planned_messages);
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
        final ArrayList<HashMap<String, String>> myArrList = new ArrayList<>();

        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(All_delayed_message_list.this);
        listOfMessages = delayedMessage.getMessages();
        for (final DelayedMessageData delayedMessageData : delayedMessage.getMessages()) {
            final HashMap<String, String> map = new HashMap<>();
            map.put("DateTime", DateFormat.getDateTimeInstance().format(delayedMessageData.getDateForSending().getTime()));
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
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                deleteButtonItem = menu.findItem(R.id.deleteItem);
                deleteButtonItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        myArrList.remove(pos);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                });
                deleteButtonItem.setVisible(true);
                listView.setItemChecked(pos, true);
                arg1.setSelected(true);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DelayedMessageData dmd = listOfMessages.get(position);
                Intent myIntent = new Intent(All_delayed_message_list.this, DelayedMessageActivity.class);
                myIntent.putExtra("DelayedMessage", dmd); //Optional parameters
                All_delayed_message_list.this.startActivity(myIntent);
            }
        });
    }
}
