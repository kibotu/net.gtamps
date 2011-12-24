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

    private String name;
    private Color4 emission;
    private Color4 ambient;
    private Color4 diffuse;
    private Color4 specular;
    private int phongExponent;
    public String diffuseTextureMap;

    public Material(String name) {
        this.name = name;
    }

    public Material(Color4 emission, Color4 ambient, Color4 diffuse, Color4 specular, int phongExponent) {
        this.emission = emission;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.phongExponent = phongExponent;
    }

    public Color4 getEmission() {
        return emission == null ? Color4.TRANSPARENT : emission;
    }

    public void setEmission(Color4 emission) {
        this.emission = emission;
    }

    public void setEmission(float r, float g, float b, int a) {
        if (emission == null) emission = new Color4(r, g, b, a);
        emission.setAll(r, g, b, a);
    }

    public Color4 getAmbient() {
        return ambient == null ? Color4.TRANSPARENT : ambient;
    }

    public void setAmbient(Color4 ambient) {
        this.ambient = ambient;
    }

    public Color4 getDiffuse() {
        return diffuse == null ? Color4.TRANSPARENT : diffuse;
    }

    public void setDiffuse(Color4 diffuse) {
        this.diffuse = diffuse;
    }

    public Color4 getSpecular() {
        return specular == null ? Color4.TRANSPARENT : specular;
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

    public void setAmbient(float r, float g, float b, int a) {
        if (ambient == null) ambient = new Color4(r, g, b, a);
        ambient.setAll(r, g, b, a);
    }

    public void setDiffuse(float r, float g, float b, int a) {
        if (diffuse == null) diffuse = new Color4(r, g, b, a);
        diffuse.setAll(r, g, b, a);
    }

    public void setSpecular(float r, float g, float b, int a) {
        if (specular == null) specular = new Color4(r, g, b, a);
        specular.setAll(r, g, b, a);
    }
}
