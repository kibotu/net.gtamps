package net.gtamps.android.core.renderer.mesh.parser;

import net.gtamps.android.core.renderer.graph.scene.primitives.ParsedObject;

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
