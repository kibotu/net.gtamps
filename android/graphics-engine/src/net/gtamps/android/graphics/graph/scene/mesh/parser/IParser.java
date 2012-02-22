package net.gtamps.android.graphics.graph.scene.mesh.parser;

import net.gtamps.android.graphics.graph.scene.primitives.Object3D;

/**
 * Interface for 3D object parsers
 */
public interface IParser {

    /**
     * Start parsing the 3D object
     */
    public void parse();

    /**
     * Fills a object with loaded data.
     *
     * @param object3D
     */
    public void getParsedObject(Object3D object3D);
}
