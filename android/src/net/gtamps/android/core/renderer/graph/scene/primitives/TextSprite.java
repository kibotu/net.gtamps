package net.gtamps.android.core.renderer.graph.scene.primitives;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.renderer.mesh.texture.SpriteTetxure;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.game.state.State;

import java.util.TreeMap;

public class TextSprite extends Sprite {

    private char[] text;
    private float letterSpacing;
    private static TreeMap<Character, Sprite> letters;

    public TextSprite(String text) {
        this(text, true, Config.DEFAULT_LETTER_SPACING);
    }

    public TextSprite(String text, boolean generateMipMap, float letterspacing) {
        this.text = text.toCharArray();
        this.letterSpacing = letterspacing;
        loadBufferedTexture(R.drawable.font_28_days_later, R.raw.font, generateMipMap);
        if (letters == null) {
            letters = new TreeMap<Character, Sprite>();
            SpriteTetxure[] spriteTetxures = bufferedTexture.getAnimation(State.Type.IDLE);
            int index = 1;
            for (char letter = '0'; letter <= '9'; letter++) {
                letters.put(letter, new Sprite(bufferedTexture.textureId, bufferedTexture.floatBufferId, spriteTetxures[index], generateMipMap));
                index++;
            }
            for (char letter = 'A'; letter <= 'Z'; letter++) {
                letters.put(letter, new Sprite(bufferedTexture.textureId, bufferedTexture.floatBufferId, spriteTetxures[index], generateMipMap));
                index++;
            }
            for (char letter = 'a'; letter <= 'z'; letter++) {
                letters.put(letter, new Sprite(bufferedTexture.textureId, bufferedTexture.floatBufferId, spriteTetxures[index], generateMipMap));
                index++;
            }
            Logger.v(this, "[FontID=" + R.drawable.font_28_days_later + "|letters=" + spriteTetxures.length + "] Font successfully loaded.");
        }

        buildWord();
    }

    private void buildWord() {
        float xOffset = 0;
        dimension.set(0, 0, 0);
        char c;
        Sprite letter;
        for (int i = 0; i < text.length; i++) {
            c = text[i];
            if (c == ' ') {
                xOffset += 5 * letterSpacing;
            } else {
                letter = letters.get(c).clone();
                xOffset += letter.getDimension().x + letterSpacing;
                letter.getPosition().x += xOffset;
                letter.useSharedTextureCoordBuffer(true);
                add(letter);
            }
        }
        position.x -= xOffset / 2;
    }

    public void setScaling(float x, float y, float z, float letterSpacing) {
        this.letterSpacing = letterSpacing;
        setScaling(x, y, z);
    }

    @Override
    public void setScaling(float x, float y, float z) {
        SceneNode node;
        for (int i = 0; i < getChildCount(); i++) {
            node = get(i);
            node.setScaling(x, y, z);
            // TODO compute correct letterspacing
            node.getPosition().x += letterSpacing;
        }
//        position.x += letterSpacing;
    }

    @Override
    public void setScaling(Vector3 scaling) {
        setScaling(scaling.x, scaling.y, scaling.z);
    }
}
