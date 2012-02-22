package net.gtamps.android.graphics.test;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import net.gtamps.android.graphics.test.listview.ActivityModel;
import net.gtamps.android.graphics.test.listview.ListModel;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<ActivityModel> adapter = new ArrayAdapter<ActivityModel>(this, R.layout.simple_list_item_1, getModel());
        setListAdapter(adapter);

        // autostart
        startActivity(PACKAGE_NAME, "Test08Activity");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ActivityModel item = (ActivityModel) l.getAdapter().getItem(position);
//        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        startActivity(item.getPackageName(), item.getClassName());
        super.onListItemClick(l, v, position, id);
    }

    private void startActivity(String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, packageName + "." + className);
        startActivity(intent);
    }

    private static final String PACKAGE_NAME = "net.gtamps.android.graphics.test";

    private List<ActivityModel> getModel() {
        List<ActivityModel> list = new ArrayList<ActivityModel>();
        list.add(new ActivityModel("Triangle", PACKAGE_NAME, "Test01Activity"));
        list.add(new ActivityModel("Cube", PACKAGE_NAME, "Test02Activity"));
        list.add(new ActivityModel("Sphere", PACKAGE_NAME, "Test03Activity"));
        list.add(new ActivityModel("Torus", PACKAGE_NAME, "Test04Activity"));
        list.add(new ActivityModel("Cylinder", PACKAGE_NAME, "Test05Activity"));
        list.add(new ActivityModel("Planes", PACKAGE_NAME, "Test06Activity"));
        list.add(new ActivityModel("Sprite", PACKAGE_NAME, "Test07Activity"));
        list.add(new ActivityModel("Parsed Obj File (Camaro)", PACKAGE_NAME, "Test08Activity"));
        list.add(new ActivityModel("Placeholder", PACKAGE_NAME, "Test09Activity"));
        list.add(new ActivityModel("Placeholder", PACKAGE_NAME, "Test10Activity"));
        // Initially select one of the items
        list.get(1).setSelected(true);
        return list;
    }

    private ListModel get(String s) {
        return new ListModel(s);
    }
}
