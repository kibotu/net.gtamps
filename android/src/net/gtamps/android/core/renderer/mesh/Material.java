package net.gtamps.android.core.renderer.mesh;

import net.gtamps.shared.Utils.math.Color4;

public class Material {

    public static final Material WHITE = new Material(Color4.WHITE, Color4.WHITE, Color4.WHITE, Color4.WHITE, 1);
    public static final Material RED = new Material(Color4.RED, Color4.RED, Color4.RED, Color4.RED, 1);
    public static final Material GREEN = new Material(Color4.GREEN, Color4.GREEN, Color4.GREEN, Color4.GREEN, 1);
    public static final Material BLUE = new Material(Color4.BLUE, Color4.BLUE, Color4.BLUE, Color4.BLUE, 1);
    public static final Material BLACK = new Material(Color4.BLACK, Color4.BLACK, Color4.BLACK, Color4.BLACK, 1);
    public static final Material LIGHT_GRAY = new Material(Color4.LIGHT_GRAY, Color4.LIGHT_GRAY, Color4.LIGHT_GRAY, Color4.LIGHT_GRAY, 1);
    public static final Material DARK_GRAY = new Material(Color4.DARK_GRAY, Color4.DARK_GRAY, Color4.DARK_GRAY, Color4.DARK_GRAY, 1);
    public static final Material GRAY = new Material(Color4.GRAY, Color4.GRAY, Color4.GRAY, Color4.GRAY, 1);
    public static final Material YELLOW = new Material(Color4.YELLOW, Color4.YELLOW, Color4.YELLOW, Color4.YELLOW, 1);
    public static final Material CYAN = new Material(Color4.CYAN, Color4.CYAN, Color4.CYAN, Color4.CYAN, 1);
    public static final Material PURPLE = new Material(Color4.PURPLE, Color4.PURPLE, Color4.PURPLE, Color4.PURPLE, 1);

    private Color4 emissive;
    private Color4 ambient;
    private Color4 diffuse;
    private Color4 specular;
    private int phongExponent;

    public Material(Color4 emissive, Color4 ambient, Color4 diffuse, Color4 specular, int phongExponent) {
        this.emissive = emissive;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.phongExponent = phongExponent;
    }

    public Color4 getEmissive() {
        return emissive;
    }

    public void setEmissive(Color4 emissive) {
        this.emissive = emissive;
    }

    public Color4 getAmbient() {
        return ambient;
    }

    public void setAmbient(Color4 ambient) {
        this.ambient = ambient;
    }

    public Color4 getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Color4 diffuse) {
        this.diffuse = diffuse;
    }

    public Color4 getSpecular() {
        return specular;
    }

    public void setSpecular(Color4 specular) {
        this.specular = specular;
    }

    public int getPhongExponent() {
        return phongExponent;
    }

    public void setPhongExponent(int phongExponent) {
        this.phongExponent = phongExponent;
    }
}
