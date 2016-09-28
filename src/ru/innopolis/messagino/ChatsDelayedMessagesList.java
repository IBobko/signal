package ru.innopolis.messagino;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.thoughtcrime.securesms.R;

import static java.security.AccessController.getContext;

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

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, delayedMessages);
        listView.setAdapter(adapter);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }
}
