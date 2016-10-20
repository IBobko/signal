package ru.innopolis.messagino;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
    private MenuItem deleteButtonItem;
    private MenuItem sendToArchiveItem;
    private List<DelayedMessageData> listOfMessages;
    private long threadId;
    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delayed_message_list.this.setTitle(R.string.title_activity_delayed_messages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        delayed_message_list.this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_chats_delayed_messages_list, menu);

        MenuItem archiveButton = menu.findItem(R.id.Archive);
        archiveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent myIntent = new Intent(delayed_message_list.this, delayed_message_list.class);
                myIntent.putExtra("threadId", threadId);
                myIntent.putExtra("status", 1);
                startActivity(myIntent);
                return true;
            }
        });
        archiveButton.setVisible(status == 0);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_delayed_messages_list);
        listView = (ListView) findViewById(R.id.delayedMessagesList);
        listView.setLongClickable(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final ArrayList<HashMap<String, String>> myArrList = new ArrayList<>();

        Intent intent = getIntent();

        final Bundle extras = intent.getExtras();
        threadId = extras.getLong("threadId");
        status = extras.getInt("status",0);

        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(delayed_message_list.this);
        listOfMessages = delayedMessage.getByRecipientAndStatus(threadId,status);
        for (final DelayedMessageData delayedMessageData : listOfMessages) {
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
                        Integer id = Integer.parseInt(myArrList.get(pos).get("ID"));
                        myArrList.remove(pos);
                        adapter.notifyDataSetChanged();
                        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(delayed_message_list.this);
                        delayedMessage.delete(id);
                        return true;
                    }
                });

                deleteButtonItem.setVisible(true);
                listView.setItemChecked(pos, true);
                arg1.setSelected(true);

                sendToArchiveItem = menu.findItem(R.id.sendToArchive);
                sendToArchiveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Integer id = Integer.parseInt(myArrList.get(pos).get("ID"));
                        myArrList.remove(pos);
                        adapter.notifyDataSetChanged();
                        final DelayedMessageDatabase delayedMessage = DatabaseFactory.getDelayedMessageDatabase(delayed_message_list.this);
                        delayedMessage.updateStatus(id,1);
                        return true;
                    }
                });

                sendToArchiveItem.setVisible(true);
                listView.setItemChecked(pos, true);
                arg1.setSelected(true);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DelayedMessageData dmd = listOfMessages.get(position);
                Intent myIntent = new Intent(delayed_message_list.this, DelayedMessageActivity.class);
                myIntent.putExtra("DelayedMessage", dmd);
                delayed_message_list.this.startActivity(myIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (status == 0)fab.show();
        else fab.hide();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent myIntent = new Intent(delayed_message_list.this, DelayedMessageActivity.class);
                final DelayedMessageData dmd = new DelayedMessageData();
                dmd.setThreadId(threadId);
                myIntent.putExtra("DelayedMessage", dmd);
                delayed_message_list.this.startActivity(myIntent);
            }
        });
        // change title name
        if(status==1) {
            delayed_message_list.this.setTitle(R.string.title_activity_archive);
        }
    }
}
