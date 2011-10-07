package net.gtamps.android.core.utils.parser;

import net.gtamps.android.game.objects.ParsedObject;

/**
 * Interface for 3D object parsers
 * 
 * @author dennis.ippel
 *
 */
public interface IParser {

	/**
	 * Start parsing the 3D object
	 */
	public void parse();
	/**
	 * Returns the parsed object
	 * @return
	 */
	public ParsedObject getParsedObject();
}
