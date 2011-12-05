package net.gtamps.android.core.sound;

import java.util.Random;

public class SoundEffect {
	private int priority;
	private int soundPoolID;
	private float randomness;
	private boolean isLoaded = false;
	public SoundEffect(int soundPoolID, float randomness, int priority){
		this.soundPoolID = soundPoolID;
		this.randomness = randomness;
		this.priority = priority;
	}
	public int getSoundPoolID(){
		return soundPoolID;
	}
	public float getPlaySpeed(){
		if(this.randomness!=0f){
			return 1f+(float)Math.random()*this.randomness;
		}
		return 1f;
	}
	public void setLoaded(){
		this.isLoaded = true;
	}
	public boolean isLoaded(){
		return this.isLoaded;
	}
	public int getPriority(){
		return this.priority;
	}
}
