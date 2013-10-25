package net.gtamps.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import net.gtamps.shared.Utils.Logger;

import java.util.HashMap;

public class MenuActivity extends Activity implements OnClickListener {

    HashMap<menuMode, Integer> idMapping = new HashMap<MenuActivity.menuMode, Integer>();

    enum menuMode {
        MAIN, CONFIGURATION, LOADING, BUBU
    }

    protected PowerManager.WakeLock mWakeLock;

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

        /*final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "gtmamps");
        this.mWakeLock.acquire();  */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        	net.gtamps.shared.Config.IPS.add("192.168.57.1");
            ((EditText)findViewById(R.id.ipText)).setText("192.168.57.1");
            net.gtamps.shared.Config.IPS.add(((EditText)findViewById(R.id.ipText)).getText().toString());
        	Logger.toast("", "IP was added!");
        }
        if (v.equals(findViewById(R.id.menuButtonQuit))) {
            finish();
        }

        ((EditText)findViewById(R.id.ipText)).setText("192.168.57.1");
    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
}