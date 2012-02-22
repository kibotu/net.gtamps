package net.gtamps.android.graphics.graph.scene.mesh.parser;

/**
 * Parser factory class. Specify the parser type and the corresponding
 * specialized class will be returned.
 */
public class Parser {

    /**
     * Parser types enum
     */
    public static enum Type {
        OBJ
    }

    ;

    /**
     * Create a parser of the specified type.
     *
     * @param type
     * @param resourceID
     * @return
     */
    public static IParser createParser(Type type, String resourceID, boolean generateMipMap) {
        switch (type) {
            case OBJ:
                return new ObjParser(resourceID, generateMipMap);
        }
        return null;
    }
}
