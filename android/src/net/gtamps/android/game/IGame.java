package net.gtamps.android.game;

import net.gtamps.android.game.scenes.BasicScene;

import java.util.ArrayList;

public interface IGame {
    public void onCreate();
    public void onDrawFrame();
    public boolean isRunning();
    public boolean isPaused();
    public ArrayList<BasicScene> getScenes();
}
