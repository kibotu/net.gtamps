package net.gtamps.android.graphics.graph;

/**
 * Logikobjekt
 */
public interface IUpdatableLogic {

    /**
     * Aktualisiert den Knoten und alle Kind- und Elternknoten.
     * Es wird ein volles Update ausgeführt.
     *
     * @param deltat Zeitdifferenz zum vorherigen Frame
     */
    public void update(float deltat);

    /**
     * Aktualisiert den Knoten und alle Kind- und Elternknoten.
     * Es wird ein volles Update ausgeführt.
     *
     * @param deltat       Zeitdifferenz zum vorherigen Frame
     * @param positionOnly Legt fest, ob nur ein Positionsupdate durchgeführt werden soll (z.B. für Kameraupdates)
     */
    public void update(float deltat, boolean positionOnly);
}
