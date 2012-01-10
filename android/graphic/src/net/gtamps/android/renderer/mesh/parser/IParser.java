package net.gtamps.android.renderer.mesh.parser;

import net.gtamps.android.renderer.graph.scene.primitives.ParsedObject;

/**
 * Interface for 3D object parsers
 *
 * @author dennis.ippel
 */
public interface IParser {

    /**
     * Start parsing the 3D object
     */
    public void parse();

    /**
     * Returns the parsed object
     *
     * @return
     */
    public ParsedObject getParsedObject();
}
