package net.gtamps.android.game.client;

import net.gtamps.android.core.utils.Utils;
import net.gtamps.shared.communication.AObjectSerializer;
import net.gtamps.shared.communication.ISerializer;
import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ObjectSerializer extends AObjectSerializer {

    @Override
    protected void log(String tag, String message) {
        Utils.log(TAG,message);
    }
}
