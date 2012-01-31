package net.gtamps.android.graphics.test;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import net.gtamps.android.graphics.test.listview.InteractiveArrayAdapter;
import net.gtamps.android.graphics.test.listview.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 17:36
 */
public class MyInteractiveActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[]{"Linux", "Windows7", "Eclipse", "Suse",
                "Ubuntu", "Solaris", "Android", "iPhone"};
        InteractiveArrayAdapter adapter = new InteractiveArrayAdapter(this, getModel());
        setListAdapter(adapter);
    }

    private List<Model> getModel() {
        List<Model> list = new ArrayList<Model>();
        list.add(get("Linux"));
        list.add(get("Windows7"));
        list.add(get("Suse"));
        list.add(get("Eclipse"));
        list.add(get("Ubuntu"));
        list.add(get("Solaris"));
        list.add(get("Android"));
        list.add(get("iPhone"));
        // Initially select one of the items
        list.get(1).setSelected(true);
        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Model item = (Model) l.getAdapter().getItem(position);
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        super.onListItemClick(l, v, position, id);
    }

    private Model get(String s) {
        return new Model(s);
    }
}