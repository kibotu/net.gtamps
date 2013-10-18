package net.gtamps.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import net.gtamps.shared.Utils.Logger;

import java.util.HashMap;

public class MenuActivity extends Activity implements OnClickListener {

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

        ((Button) findViewById(R.id.menuButtonStartGame)).setOnClickListener(this);
        ((Button) findViewById(R.id.menuButtonConfiguration)).setOnClickListener(this);
        ((Button) findViewById(R.id.menuAddIpButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.menuButtonQuit)).setOnClickListener(this);
        ((Button) findViewById(R.id.menuButtonGoToMain)).setOnClickListener(this);

        // autostart
        /*if(Config.FORCE_GTA_2D){
        	startGTA2D();
        } else if(Config.FORCE_GTA_3D_SIMPLE){
        	startGTA2DSimple();
        } else startGTA3D();*/
    }

    private void startGTA3DSimple() {
    	Intent intent = new Intent();
        intent.setClassName("net.gtamps.android", "net.gtamps.android.GTA3Dsimple");
        startActivity(intent);
	}

	private void startGTA2D() {
        Intent intent = new Intent();
        intent.setClassName("net.gtamps.android", "net.gtamps.android.GTA2D");
        startActivity(intent);
    }

    private void startGTA3D() {
        Intent intent = new Intent();
        intent.setClassName("net.gtamps.android", "net.gtamps.android.GTA");
        startActivity(intent);
    }

    private void setMenuMode(menuMode mode) {
        for (menuMode m : idMapping.keySet()) {
            LinearLayout layout = (LinearLayout) findViewById(idMapping.get(m));
            if (m.equals(mode)) {
                layout.setVisibility(LinearLayout.VISIBLE);
            } else {
                layout.setVisibility(LinearLayout.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.menuButtonStartGame))) {
            setMenuMode(menuMode.LOADING);
//            if(Config.FORCE_GTA_2D) startGTA2D();
//            else 
            startGTA3DSimple();
    
        }
        if (v.equals(findViewById(R.id.menuButtonConfiguration))) {
            setMenuMode(menuMode.CONFIGURATION);
        }
        if (v.equals(findViewById(R.id.menuButtonGoToMain))) {
            setMenuMode(menuMode.MAIN);
        }
        if (v.equals(findViewById(R.id.menuAddIpButton))) {
        	net.gtamps.shared.Config.IPS.add("192.168.2.101"); // ((EditText)findViewById(R.id.ipText)).getText().toString());
        	Logger.toast("", "IP was added!");
        }
        if (v.equals(findViewById(R.id.menuButtonQuit))) {
            finish();
        }
    }
}