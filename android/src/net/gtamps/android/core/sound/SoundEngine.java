package net.gtamps.android.core.sound;

import java.util.HashMap;

import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.configuration.Configuration;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;

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

	private static SoundEngine instance;

	

	private static final String TAG = "SoundEngine";

	private SoundPool soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
	private HashMap<Entity.Type, HashMap<EventType, SoundEffect>> soundPoolMap = new HashMap<Entity.Type, HashMap<EventType, SoundEffect>>();
	private AudioManager audioManager;

	// private Context context;
	public SoundEngine(Context context, Configuration configuration) {
		for (Configuration entity : configuration.select("game.entities.entity")) {
			String carType = entity.select("car").getString();
			Entity.Type entityType = Entity.Type.PLACEHOLDER;
			// check if the type of the entity is one in the entity enum.
			for (Entity.Type et : Entity.Type.values()) {
				if (carType.equals(et.toString())) {
					entityType = et;
				}
			}

			for (Configuration soundConf : entity.select("SOUNDS.SOUND")) {

				// ACCELERATION
				EventType eventType = EventType.ACTION_ACCELERATE;
				String soundPath = soundConf.select(eventType.toString()).getString();
				loadSoundIntoEngine(entityType, eventType, soundPath);

				// OTHER SOUNDS...

			}
		}
		audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		Log.d(TAG, "SoundEngine Initialized");

		soundPool.setOnLoadCompleteListener(this);
		instance = this;
	}

	private void loadSoundIntoEngine(Entity.Type entityType, EventType eventType, String soundPath) {
		if (soundPoolMap.containsKey(entityType)) {
			soundPoolMap.get(entityType).put(eventType,
					new SoundEffect(soundPool.load(soundPath, PRIORITY_INTERACTION), 0.2f, PRIORITY_ENVIRONMENT));
		} else {
			HashMap<EventType, SoundEffect> entitySoundsMap = new HashMap<EventType, SoundEffect>();
			entitySoundsMap.put(eventType, new SoundEffect(soundPool.load(soundPath, PRIORITY_INTERACTION), 0.2f,
					PRIORITY_ENVIRONMENT));
			soundPoolMap.put(entityType, entitySoundsMap);
		}
	}

	public static SoundEngine getInstance() {
		return instance;
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleID, int status) {
		for (HashMap<EventType, SoundEffect> semap : soundPoolMap.values()) {
			for (SoundEffect se : semap.values()) {
				if (se.getSoundPoolID() == sampleID) {
					Log.d(TAG, "Loaded Sound number: " + sampleID);
					se.setLoaded();
				}
			}
		}
	}

	public void playSound(Entity.Type type, EventType eventType) {
		if (soundPoolMap.containsKey(type) && soundPoolMap.get(type).containsKey(eventType)) {
			final SoundEffect se = soundPoolMap.get(type).get(eventType);
			if (se.isLoaded()) {
				soundPool.play(se.getSoundPoolID(), 1f, 1f, se.getPriority(), 0, se.getPlaySpeed());
			}
		} else {
			Logger.e(this, type.toString() + " does not have a Sound " + eventType.toString() + "! Omitting...");
		}
	}
}
