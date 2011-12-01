package net.gtamps.shared.configuration;

/**
 *	Abstracts the merging of two {@link Configuration configurations} into a
 *	new one, since there are several conflicting methods for doing so. 
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public interface MergeStrategy {

	/**
	 * Merges two {@link Configuration configurations} by whatever algorithm is
	 * implemented and returns the result. This operation is not, by default,
	 * commutative: the order of arguments *might* matter, hence the different 
	 * parameter names. 
	 * <p/>
	 * Note: Depending on the implementation, the returned configuration might
	 * present another {@link Configuration#getSource() source}.
	 * <p/>
	 * Special cases:
	 * <ul>
	 * <li>{@code merge(null, null)} returns {@code null};</li>
	 * <li>{@code merge(baseConfig, null)} returns {@code baseConfig};</li>
	 * <li>{@code merge(null, otherConfig)} returns {@code null} or {@code otherConfig}.</li>
	 * </ul>
	 * 
	 * @param baseConfig	the first configuration to be merged
	 * @param otherConfig	the second configuration to be merged
	 * @return	the result of the merge process
	 */
	public Configuration merge(Configuration baseConfig, Configuration otherConfig);

}
