package net.gtamps.android.gtandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import net.gtamps.android.graphics.RenderActivity;

import java.util.HashMap;

public class MenuActivity extends Activity implements View.OnClickListener {

    HashMap<menuMode, Integer> idMapping = new HashMap<MenuActivity.menuMode, Integer>();

    enum menuMode {
        MAIN, CONFIGURATION, LOADING, BUBU
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        idMapping.put(menuMode.MAIN, R.id.mainScreen);
        idMapping.put(menuMode.CONFIGURATION, R.id.configurationScreen);
        idMapping.put(menuMode.LOADING, R.id.loadingScreen);

        setMenuMode(menuMode.MAIN);

        findViewById(R.id.menuButtonStartGame).setOnClickListener(this);
        findViewById(R.id.menuButtonConfiguration).setOnClickListener(this);
        findViewById(R.id.menuAddIpButton).setOnClickListener(this);
        findViewById(R.id.menuButtonQuit).setOnClickListener(this);
        findViewById(R.id.menuButtonGoToMain).setOnClickListener(this);

        // autostart
        startGTA2D();
    }

    private void startGTA3D() {
        Intent intent = new Intent();
        intent.setClassName("net.gtamps.android.gtandroid", "net.gtamps.android.gtandroid.GTA3D");
        startActivity(intent);
    }

    private void startGTA2D() {
        Intent intent = new Intent();
        intent.setClassName("net.gtamps.android.gtandroid", "net.gtamps.android.gtandroid.GTA2D");
        startActivity(intent);
    }

    private void setMenuMode(menuMode mode) {
        for (menuMode m : idMapping.keySet()) {
            LinearLayout layout = (LinearLayout) findViewById(idMapping.get(m));
            layout.setVisibility(m.equals(mode) ? LinearLayout.VISIBLE : LinearLayout.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.menuButtonStartGame))) {
            setMenuMode(menuMode.LOADING);
            startGTA2D();
        }
        if (v.equals(findViewById(R.id.menuButtonConfiguration))) {
            setMenuMode(menuMode.CONFIGURATION);
        }
        if (v.equals(findViewById(R.id.menuButtonGoToMain))) {
            setMenuMode(menuMode.MAIN);
        }
        if (v.equals(findViewById(R.id.menuAddIpButton))) {
            net.gtamps.shared.Config.IPS.add(((EditText) findViewById(R.id.ipText)).getText().toString());
            RenderActivity.toast(this, "IP was added!");
        }
        if (v.equals(findViewById(R.id.menuButtonQuit))) {
            finish();
        }
    }
}
