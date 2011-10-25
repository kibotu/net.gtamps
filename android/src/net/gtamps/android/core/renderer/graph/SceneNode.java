package net.gtamps.android.core.renderer.graph;

import net.gtamps.shared.math.Matrix4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Basisklasse für Szenenknoten
 * 
 * Dies ist die Basisklasse eines Szenenknoten. Sie übernimmt das Management
 * der Sichtbarkeit, der Orientierung und des Dispatchings von Funktionsaufrufen
 * an Kindelemente. Spezifische Szenenknoten leiten von dieser Klasse ab und
 * implementieren eine eigenständige Update- und Verarbeitungslogik in den überladenen
 * Funktionen.
 * @author sunside
 */
public abstract class SceneNode extends ObjectWithOrientation implements IProcessable, IUpdatableLogic, ICleanable {

	/**
	 * Benutzerdefiniertes Objekt
	 */
	@Nullable
	private Object _userObject = null;

	/**
	 * Die kombinierte Transformationsmatrix aller Elternelemente 
	 */
	private Matrix4 _combinedTransformation = Matrix4.UNIT;
	
	/**
	 * Gibt an, ob die kombinierte Transformationsmatrix neu berechnet werden muss
	 */
	private boolean _combinedTransformationDirty = true;
	
	/**
	 * Ermittelt, ob die kombinierte Transformationsmatrix neu gesetzt werden muss
	 * @return <code>true</code>, wenn die kombinierte Matrix als dirty markiert ist
	 */
	protected final boolean isCombinedTransformationDirty() {
		return _combinedTransformationDirty;
	}
	
	/**
	 * Bezieht die kombinierte Transformationsmatrix
	 * @return Die kombinierte Transformationsmatrix
	 */
	@NotNull
	public final Matrix4 getCombinedTransformation() {
		return _combinedTransformation;
	}
	
	/**
	 * Aktualisiert die kombinierte Transformationsmatrix
	 */
	protected final void updateCombinedTransformation() {
		_combinedTransformation = getOrientationMatrix();

		// Elternknoten existiert, also "obere" Orientierung mit dieser verheiraten
		if (_parentNode != null) {
			_combinedTransformation = _parentNode.getCombinedTransformation().mul(_combinedTransformation);
		}
	}

	/**
	 * Der Elternknoten
	 */
	private SceneNode _parentNode;
	
	/**
	 * Setzt den Elternknoten
	 * @param parent Der Elternknoten
	 */
	public final void setParent(@Nullable SceneNode parent) {
		_parentNode = parent;
	}
	
	/**
	 * Bezieht den Elternknoten
	 * @return Der Elternknoten
	 */
	@Nullable
	public final SceneNode getParent() {
		return _parentNode;
	}
	
	/**
	 * Hängt den Knoten aus dem Graphen aus
	 */
	public final void detachFromGraph() {
		if (_parentNode == null) return;
		_parentNode.remove(this);
		_parentNode = null;
	}
	
	/**
	 * Ermittelt, ob dieser Knoten einen Elternknoten hat
	 * @return true, wenn ein Elternknoten vorhanden ist
	 */
	public final boolean hasParentNode() {
		return _parentNode != null;
	}
	
	/**
	 * Liste der Kindknoten
	 */
	private List<SceneNode> _childNodes;
	
	/**
	 * Fügt der Hierarchie ein Kind hinzu
	 * @param child Der hinzuzufügende Kindknoten
	 */
	public final void add(@NotNull SceneNode child) {
		if (_childNodes == null) _childNodes = new ArrayList<SceneNode>();
		_childNodes.add(child);
        if(child.hasParentNode()) child.getParent().remove(this);
        child.setParent(this);
	}

	/**
	 * Entfernt ein Kind aus der Hierarchie
	 * @param child Der zu entfernende Kindknoten
	 * @return Gibt an, ob das Kind entfernt wurde
	 */
	public final boolean remove(@NotNull SceneNode child) {
		if (_childNodes == null) return false;
		if (_childNodes.remove(child))
		{
			if (child.getParent() == this) child.setParent(null);
			return true;
		}
		return false;
	}
	
	/**
	 * Bezieht einen Kindknoten
	 * @param childIndex Der Index des Kindknotens
	 * @return Der Kindknoten
	 */
	@Nullable
	public final SceneNode get(int childIndex) {
		if (_childNodes == null || childIndex < 0 || childIndex >= _childNodes.size()) return null;
		return _childNodes.get(childIndex);
	}

	/**
	 * Liefert die Anzahl der direkten Kindknoten

	 * @return Anzahl der direkten Kindknoten
	 */
	public int getChildCount() {
		return _childNodes.size();
	}

	/**
	 * Gibt an, ob der Knoten sichtbar ist
	 */
	private boolean _isVisible = true;
	
	/**
	 * Setzt die Sichtbarkeit des Knotens.
	 * Ist der Knoten unsichtbar, werden auch alle Kindknoten nicht angezeigt.
	 * @param visible Gibt an, ob der Knoten sichtbar sein darf
	 */
	public final void setVisible(boolean visible) {
		_isVisible = visible;
	}
	
	/**
	 * Ermittelt die Sichtbarkeit des Knotens
	 * @return <code>true</code>, wenn der Knoten sichtbar ist
	 */
	public final boolean isVisible() {
		return _isVisible;
	}
	
	/**
	 * Aktualisiert den Knoten und alle Kind- und Elternknoten.
	 * Es wird ein volles Update ausgeführt.
	 * @param deltat Zeitdifferenz zum vorherigen Frame
	 */
	public final void update(float deltat) {
		assert deltat >= 0;

		update(deltat, false);
		//             ^- Volles Update durchf�hren
		//      ^- Zeit seit letztem Update
	}
	
	/**
	 * Aktualisiert den Knoten und alle Kind- und Elternknoten.
	 * Es wird ein volles Update ausgeführt.
	 * @param deltat Zeitdifferenz zum vorherigen Frame
	 * @param positionOnly Legt fest, ob nur ein Positionsupdate durchgeführt werden soll (z.B. für Kameraupdates)
	 */
	public final void update(float deltat, boolean positionOnly) {
		assert deltat >= 0;

		update(deltat, true, positionOnly);
		//                   ^- Nur Positionsupdate durchf�hren
		//             ^- Elternknoten aktualisieren
		//      ^- Zeit seit letztem Update
	}
	
	/**
	 * Aktualisiert den Knoten und alle Kindknoten
	 * @param deltat Zeitdifferenz zum vorherigen Frame
	 * @param updateParents Gibt an, ob Elternknoten ebenfalls aktualisiert werden sollen
	 * @param positionOnly Legt fest, ob nur ein Positionsupdate durchgeführt werden soll (z.B. für Kameraupdates)
	 */
	private void update(float deltat, boolean updateParents, boolean positionOnly) {
		assert deltat >= 0;
		
		// Orientierung neu berechnen
		if(updateOrientation()) {
			// Orientierungsmatrix hat sich ge�ndert, combined transformation als dirty markieren
			_combinedTransformationDirty = true;
		}
		
		// überprüfen, ob die combined transformation von parent dirty ist
		if (_parentNode != null) {
			if(updateParents) _parentNode.update(deltat, true, positionOnly);
			_combinedTransformationDirty |= _parentNode.isCombinedTransformationDirty();
		}
		
		// Wenn combined transformation als dirty markiert, neu berechnen
		if (_combinedTransformationDirty) {
			updateCombinedTransformation();
		}
		
		// Internen Updatevorgang aufrufen
		if (!positionOnly) updateInternal(deltat);
		
		// Alle Kindknoten rekursiv updaten
		if (_childNodes != null) {
			int count = _childNodes.size();
			for (int i=0; i<count; ++i) {
				_childNodes.get(i).update(deltat, false, positionOnly);
			}
		}
		
		// Combined transformation als sauber markieren
		_combinedTransformationDirty = false;
	}
	
	/**
	 * Spezifische Implementierung der Aktualisierungslogik
	 * @param deltat Zeitdifferenz zum vorherigen Frame
	 */
	protected abstract void updateInternal(float deltat);
	
	/**
	 * Verarbeitet den Knoten und alle Kindknoten
	 * @param state Die State-Referenz
	 */
	public final void process(@NotNull ProcessingState state) {
		if (!_isVisible) return;
		
		// Internen Verarbeitungsvorgang aufrufen
		processInternal(state);
		
		// Alle Kindknoten rekursiv verarbeiten
		if (_childNodes != null) {
			int count = _childNodes.size();
			for (int i=0; i<count; ++i) {
				_childNodes.get(i).process(state);
			}
		}

        afterProcess(state);
	}

    /**
     * Invoked after processing of all child nodes.
     */
    protected abstract void afterProcess(@NotNull ProcessingState state);

    /**
	 * Spezifische Implementierung des Verarbeitungsvorganges
	 * @param state Die State-Referenz
	 */
	protected abstract void processInternal(@NotNull ProcessingState state);

	/**
	 * Bereinigt den Knoten und alle Kindknoten
	 *
	 * @param state Die State-Referenz
	 */
	public final void cleanup(@NotNull ProcessingState state) {
		// Internen Verarbeitungsvorgang aufrufen
		cleanupInternal(state);

		// Alle Kindknoten rekursiv verarbeiten
		if (_childNodes != null) {
			int count = _childNodes.size();
			for (int i = 0; i < count; ++i) {
				_childNodes.get(i).cleanup(state);
			}
		}
	}

	/**
	 * Spezifische Implementierung des Bereinigungsvorganges
	 *
	 * @param state Die State-Referenz
	 */
	protected abstract void cleanupInternal(@NotNull ProcessingState state);

	/**
	 * Initialisiert den Knoten und alle Kindknoten
	 *
	 * @param state Die State-Referenz
	 */
	public final void setup(@NotNull ProcessingState state) {
		// Internen Verarbeitungsvorgang aufrufen
		setupInternal(state);

		// Alle Kindknoten rekursiv verarbeiten
		if (_childNodes != null) {
			int count = _childNodes.size();
			for (int i = 0; i < count; ++i) {
				_childNodes.get(i).setup(state);
			}
		}
	}

	/**
	 * Spezifische Implementierung des Setupvorganges
	 *
	 * @param state Die State-Referenz
	 */
	protected abstract void setupInternal(@NotNull ProcessingState state);

	/**
	 * Liefert das benutzerdefinierte Objekt
	 * @return User state
	 */
	@Nullable
	public final Object getUserObject() {
		return _userObject;
	}

	/**
	 * Setzt das benutzerdefinierte Objekt
	 * @param userState Das Objekt
	 */
	public final void setUserObject(@Nullable Object userState) {
		_userObject = userState;
	}
}
