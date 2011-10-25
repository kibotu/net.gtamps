package net.gtamps.android.core.renderer.graph;

import org.jetbrains.annotations.NotNull;

/**
 * Verarbeitbares Element
 */
public interface IProcessable {
    /**
     * Verarbeitet den Knoten und alle Kindknoten
     * @param state Die State-Referenz
     */
    public void process(@NotNull ProcessingState state);

	/**
	 * Ermittelt die Sichtbarkeit des Knotens
	 *
	 * @return <code>true</code>, wenn der Knoten sichtbar ist
	 */
	public boolean isVisible();
}
