package net.gtamps.android.core.renderer.graph.scene;


import javax.microedition.khronos.opengles.GL11;

public interface IShader {

    public enum Type {
        FLAT(GL11.GL_FLAT),
        SMOOTH(GL11.GL_SMOOTH);
        private final int value;

        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
