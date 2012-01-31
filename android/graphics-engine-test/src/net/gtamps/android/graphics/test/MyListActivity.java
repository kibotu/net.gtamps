package net.gtamps.android.graphics.test;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import net.gtamps.android.graphics.test.listview.Model;

public class MyListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[]{"Triangle", "Cube", "Sphere", "Torus"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Model item = (Model) l.getAdapter().getItem(position);
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        super.onListItemClick(l, v, position, id);
    }
}
