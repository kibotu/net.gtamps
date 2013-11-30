package net.gtamps.android.core.sound;

import java.util.HashMap;

import net.gtamps.android.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class SoundEngine implements OnLoadCompleteListener {
	public static final int PRIORITY_ENVIRONMENT = 0;
	public static final int PRIORITY_INTERACTION = 1;
	public static final int PRIORITY_HIGH = 2;
	public static final int PRIORITY_NOTIFICATIONS = 3;

	// TODO dies ist mega haeslich! (static singleton
	private static SoundEngine instance;

	private static final String TAG = "SoundEngine";

	public enum Sounds {
		SHOOT, HUMAN_HIT, CAR_DOOR, FOOTSTEP, CAR_HIT, SHELL
	};

	private SoundPool soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
	private HashMap<Sounds, SoundEffect> soundPoolMap = new HashMap<Sounds, SoundEffect>();
	private AudioManager audioManager;

	// private Context context;
	public SoundEngine(Context context) {

		// this.context = context;
		audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);

		soundPoolMap.put(Sounds.SHOOT,
				new SoundEffect(soundPool.load(context, R.raw.machgf1b, PRIORITY_ENVIRONMENT), 0.2f,
						PRIORITY_ENVIRONMENT));
		soundPoolMap.put(Sounds.HUMAN_HIT,
				new SoundEffect(soundPool.load(context, R.raw.bodyfall_default_d, PRIORITY_ENVIRONMENT), 0.2f,
						PRIORITY_ENVIRONMENT));
		soundPoolMap.put(Sounds.CAR_DOOR,
				new SoundEffect(soundPool.load(context, R.raw.car_door_open, PRIORITY_ENVIRONMENT), 0.2f,
						PRIORITY_ENVIRONMENT));
		soundPoolMap.put(Sounds.FOOTSTEP,
				new SoundEffect(soundPool.load(context, R.raw.footstep_enemy_run_right_b, PRIORITY_ENVIRONMENT), 0.2f,
						PRIORITY_ENVIRONMENT));
		soundPoolMap.put(Sounds.CAR_HIT,
				new SoundEffect(soundPool.load(context, R.raw.metal_hit_large_c, PRIORITY_ENVIRONMENT), 0.2f,
						PRIORITY_ENVIRONMENT));
		soundPoolMap.put(Sounds.SHELL,
				new SoundEffect(soundPool.load(context, R.raw.shell_9mm_default_freeze_b, PRIORITY_ENVIRONMENT), 0.2f,
						PRIORITY_ENVIRONMENT));
		Log.d(TAG, "SoundEngine Initialized");

		soundPool.setOnLoadCompleteListener(this);

		instance = this;
	}

	public static SoundEngine getInstance() {
		return instance;
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleID, int status) {
		for (SoundEffect se : soundPoolMap.values()) {
			if (se.getSoundPoolID() == sampleID) {
				Log.d(TAG, "Loaded Sound number: " + sampleID);
				se.setLoaded();
			}
		}

	}

	public void playSound(Sounds sound) {
		final SoundEffect se = soundPoolMap.get(sound);
		if (se.isLoaded()) {
			soundPool.play(se.getSoundPoolID(), 1f, 1f, se.getPriority(), 0, se.getPlaySpeed());
		}
	}
}
