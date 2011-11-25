package net.gtamps.shared.configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * A {@link MergeStrategy merge strategy} whose {@link #merge(Configuration, Configuration)}
 * operation protects the first configuration argument in such a way
 * that the second configuration may only add, but never override. 
 * <p/>
 * Previously undefined keys and corresponding values can be added, but it is 
 * not possible to alter existing values in any way. This protection
 * extends to lists, which can not be added to.
 * <p/> 
 * <strong>NOTE:</strong> This means that the second configuration 
 * MAY NOT add sub-keys where the first configuration defines a literal 
 * or a list, since doing so would modify the original value. 
 * <p/>
 * For example: consider
 * <p/>
 * {@code ConfigA = ("X" => 1)} and <br/>
 * {@code ConfigB = ("X" => 2, "Y" => 3)}. 
 * <p/>
 * Merging them will yield
 * <p/> 
 * {@code merge(ConfigA, ConfigB) => ("X" => 1, "Y" => 3)}, 
 * <p/>
 * because the definition {@code ("X" => 1)} in <tt>ConfigA</tt> may not be modified.
 * <p/>
 * The same is true for 
 * {@code ConfigB = ("X" => ("XX" => 1, "XY" => 2), "Y" => 3)}.
 * 
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public class ProtectedMergeStrategy implements MergeStrategy {

	private ConfigSource getMergeSource(final Configuration baseConfig, final Configuration otherConfig) {
		//TODO
		return new ConfigSource();
	}

	@Override
	public Configuration merge(final Configuration baseConfig, final Configuration otherConfig) {
		if (baseConfig == null) {
			return otherConfig;
		}
		if (otherConfig == null || otherConfig == baseConfig) {
			return baseConfig;
		}
		final ConfigBuilder builder = ConfigBuilder.buildConfig(getMergeSource(baseConfig, otherConfig));
		final Set<String> keys = new HashSet<String>();
		keys.addAll(otherConfig.getKeys());
		keys.addAll(baseConfig.getKeys());
		for (final String key : keys) {
			final Configuration baseList = baseConfig.select(key);
			final Configuration otherList = otherConfig.select(key);
			final Configuration mergeList;
			if (baseList == null) {
				mergeList = otherList;
			} else if (baseList.getType() == Map.class && otherList.getType() == Map.class){
				mergeList = merge(baseList, otherList);
			} else {
				mergeList = baseList;
			}
			builder.select(key).addConfig(mergeList);
		}
		return builder.getConfig();
	}




}
