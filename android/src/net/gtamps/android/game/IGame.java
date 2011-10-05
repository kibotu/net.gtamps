package net.gtamps.android.game;

import java.util.ArrayList;

public interface IGame {
    public void onCreate();
    public void onDrawFrame();
    public boolean isRunning();
    public boolean isPaused();
    public ArrayList<Scene> getScenes();
}
