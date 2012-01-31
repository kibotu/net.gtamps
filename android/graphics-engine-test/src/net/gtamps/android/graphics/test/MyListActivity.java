package net.gtamps.android.graphics.test;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import net.gtamps.android.graphics.test.listview.ListModel;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<ListModel> adapter = new ArrayAdapter<ListModel>(this, R.layout.simple_list_item_1, getModel());
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ListModel item = (ListModel) l.getAdapter().getItem(position);
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        super.onListItemClick(l, v, position, id);
    }

    private List<ListModel> getModel() {
        List<ListModel> list = new ArrayList<ListModel>();
        list.add(get("Triangle"));
        list.add(get("Cube"));
        list.add(get("Sphere"));
        list.add(get("Torus"));
        // Initially select one of the items
        list.get(1).setSelected(true);
        return list;
    }

    private ListModel get(String s) {
        return new ListModel(s);
    }
}
