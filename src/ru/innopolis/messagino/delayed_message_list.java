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

public class delayed_message_list extends BaseActionBarActivity {
    private Menu menu;
    private SimpleAdapter adapter;
    private ListView listView;
    private int currentItemKeyValue;
    private MenuItem deleteButtonItem;
    private MenuItem addButtonItem;
    private MenuItem archiveButton;
    private ArrayList<HashMap<String, String>> myArrList;
    private List<DelayedMessageData> listOfMessages;
    private long treadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delayed_message_list);
        delayed_message_list.this.setTitle(R.string.title_activity_delayed_messages);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        delayed_message_list.this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_chats_delayed_messages_list, menu);
        addButtonItem = menu.findItem(R.id.AddDM);

        addButtonItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent myIntent = new Intent(delayed_message_list.this, DelayedMessageActivity.class);
                DelayedMessageData dmd = new DelayedMessageData();
                dmd.setThreadId(treadId);
                myIntent.putExtra("DelayedMessage", dmd);
                delayed_message_list.this.startActivity(myIntent);
                return true;
            }
        });
        archiveButton = menu.findItem(R.id.Archive);
        archiveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent myIntent = new Intent(delayed_message_list.this, sended_delayed_message_list.class);
                myIntent.putExtra("RECIPIENT", ""); //Optional parameters
                delayed_message_list.this.startActivity(myIntent);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_delayed_messages_list);
        listView = (ListView) findViewById(R.id.delayedMessagesList);
        listView.setLongClickable(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        myArrList = new ArrayList<>();

        Intent intent = getIntent();

        final Bundle extras = intent.getExtras();
        treadId = extras.getLong("threadId");

        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(delayed_message_list.this);
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
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                currentItemKeyValue = pos;
                deleteButtonItem = menu.findItem(R.id.deleteItem);
                deleteButtonItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Integer id = Integer.parseInt(myArrList.get(currentItemKeyValue).get("ID"));
                        myArrList.remove(currentItemKeyValue);
                        adapter.notifyDataSetChanged();
                        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(delayed_message_list.this);
                        delayedMessage.delete(id);
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
                DelayedMessageData dmd = listOfMessages.get(position);
                Intent myIntent = new Intent(delayed_message_list.this, DelayedMessageActivity.class);
                myIntent.putExtra("DelayedMessage", dmd); //Optional parameters
                delayed_message_list.this.startActivity(myIntent);
            }
        });
    }


}
