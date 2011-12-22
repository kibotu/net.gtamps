package net.gtamps.android;

import java.util.HashMap;

import net.gtamps.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenuActivity extends Activity implements OnClickListener {

	HashMap<menuMode, Integer> idMapping = new HashMap<MenuActivity.menuMode, Integer>();

	enum menuMode {
		MAIN, CONFIGURATION, LOADING
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		idMapping.put(menuMode.MAIN, R.id.mainScreen);
		idMapping.put(menuMode.CONFIGURATION, R.id.configurationScreen);
		idMapping.put(menuMode.LOADING, R.id.loadingScreen);

		setMenuMode(menuMode.MAIN);
		
		((Button)findViewById(R.id.menuButtonStartGame)).setOnClickListener(this);
		((Button)findViewById(R.id.menuButtonConfiguration)).setOnClickListener(this);
		((Button)findViewById(R.id.menuButtonQuit)).setOnClickListener(this);
		((Button)findViewById(R.id.menuButtonGoToMain)).setOnClickListener(this);
		
	}

	private void startGame() {
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
		if(v.equals(findViewById(R.id.menuButtonStartGame))){
			setMenuMode(menuMode.LOADING);
			startGame();
		}
		if(v.equals(findViewById(R.id.menuButtonConfiguration))){
			setMenuMode(menuMode.CONFIGURATION);
		}
		if(v.equals(findViewById(R.id.menuButtonGoToMain))){
            setMenuMode(menuMode.MAIN);
        }
        if(v.equals(findViewById(R.id.menuButtonQuit))){
            finish();
        }
	}
}
