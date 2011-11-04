package net.gtamps.android.core.input;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.shared.Utils.math.Vector3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Die Eingabeengine
 */
final public class InputEngine implements OnTouchListener {

    /**
     * Logging-Tag
     */
    private static final String TAG = InputEngine.class.getSimpleName();

    /**
     * Die InputEngine-Instanz
     */
    @Nullable
    private static InputEngine instance = null;

    /**
     * Der Kameraknoten
     */
    @NotNull
    private static Camera _camera;


    private final int CLICKGENAUIGKEIT = 1;  //Pixel der Clickgenauigkeit und gleichzeitig Distanz,
    //ab denen erst eine Bewegung wahrgenommen wird (-> Kein FingerZittern mehr)

    private float bewegungsVerlangsamung = 25f;  // Pixel der Pointerbewegung * wert = Tatsaechliche Beweung
    // Da wir schnell 100px scrollen, aber nur ein paar Werte in OpenGL
    // Scrollen wollen diese Verrechnungskonstante


    private float zoomDistanz = 0;              // Float, der angibt, wieviel gezoomt wurde, wenn Dirty -> Game
    private float alteZoomDistanz;              // Zoomdistanz zum Vergleich

    //Position des Berechnens pro Event (Move)
    private Vector3 lastTouchedPosition = Vector3.createNew(0, 0, 0);

    // Distanz der Bewegung, die von Game abgefragt wird
    private Vector3 moveVector = Vector3.createNew(0, 0, 0);

    // Ungueltige Pointer ID fuer multitouch -> Registiert, dass falscher Pointer hochgenommen wurde bei MultiTouch
    private static final int INVALID_POINTER_ID = -1;

    // Der Aktive Pointer ist der eine, der gerade unsere Kamera bewegt (primaerer Pointer)
    private int mActivePointerId = INVALID_POINTER_ID;

    // Abfragewerte ob CLICK/ZOOM "Dirty" ist -> Siehe Game Klasse
    private boolean isUP = false;
    private boolean isZoomed = false;
    private boolean isDown = false;

    // PositionsVektor fuer die letzten Koordinaten des primären Pointers (MOVE, CLICK, DOWN, UP)
    private Vector3 pointerPosition = Vector3.createNew();


    /**
     * Default-Konstruktor, der nicht ausserhalb dieser Klasse
     * aufgerufen werden kann -> Singleton
     */
    private InputEngine() {
    }

    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurueck
     */
    public static InputEngine getInstance() {
        if (instance == null) {
            instance = new InputEngine();
        }
        return instance;
    }


    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        final int action = ev.getAction();
        //Log.e("ACTION",""+ action);

        switch (action & MotionEvent.ACTION_MASK) {

            // Primaerpointer Down
            case MotionEvent.ACTION_DOWN: {
                lastTouchedPosition.set(ev.getX(), ev.getY(), 0);

                // Speichert die ID des Pointers
                mActivePointerId = ev.getPointerId(0);

                // den bereits vorhandenen Vektor mit den Koordinaten des Clicks (relativ zur Screenmitte) fütenr
                pointerPosition = getRelativeClickPosition(ev.getX(), ev.getY(), view.getWidth(), view.getHeight());
                berechneOpenGLKoordinaten(pointerPosition, view);
                isDown = true;

//            Utils.log(TAG, "" + view.getWidth() + " " + view.getHeight());

                break;
            }

            // Nicht Primaerpointer DOWN
            case MotionEvent.ACTION_POINTER_DOWN: {
                // Setzen der Ausgangsdistanz fuer den anschlie�enden Zoom
//            alteZoomDistanz = berechneDistanz2D(ev.getX(0), ev.getY(0), ev.getX(1), ev.getY(1));
                Vector3 temp = Vector3.createNew(ev.getX(0), ev.getY(0), 0);
                alteZoomDistanz = temp.getDistance(ev.getX(1), ev.getY(1), 0);
                temp.recycle();
                break;
            }

            // MOVE
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                Vector3 pos = Vector3.createNew(ev.getX(pointerIndex), ev.getY(pointerIndex), 0);

                if (ev.getPointerCount() == 1) {
                    // Finde den Index des aktiven Pointers und seine Position

                    final float dx = lastTouchedPosition.x - pos.x;
                    final float dy = pos.y - lastTouchedPosition.y;

                    if (Math.abs(lastTouchedPosition.x - pos.x) > CLICKGENAUIGKEIT
                            || Math.abs(lastTouchedPosition.y - pos.y) > CLICKGENAUIGKEIT) {
                        moveVector.set(dx / bewegungsVerlangsamung,
                                dy / bewegungsVerlangsamung,
                                0);
                    }
                    lastTouchedPosition.set(pos);

                    // Auch beim Bewegen Pointer Position bestimmen. (Check, ob ÜberHaus gezogen)
                    pointerPosition = getRelativeClickPosition(ev.getX(), ev.getY(), view.getWidth(), view.getHeight());
                    berechneOpenGLKoordinaten(pointerPosition, view);
                } else
                // ZOOM
                {
//                float distanz = berechneDistanz2D(x, y, ev.getX(1), ev.getY(1));

                    Vector3 temp = Vector3.createNew(ev.getX(1), ev.getY(1), 0);
                    float distanz = pos.getDistance(temp);
                    temp.recycle();
                    zoomDistanz = distanz - alteZoomDistanz;
                    isZoomed = true;

                    // OpenGL Sichtfeld berechnen um bewegungsVerlangsamung-Faktor zu erhalten
                    // Kleines Sichtfeld -> Groesserer Faktor -> Move wird langsamer
                    float kameradistanzZumSichtfeld = _camera.getPosition().getDistance(_camera.getTarget());
                    float viewwidth = berechneSichtfeldHorizontal(_camera.getHorizontalFieldOfViewEffective(), kameradistanzZumSichtfeld);
                    bewegungsVerlangsamung = view.getWidth() / viewwidth / 2;

                    alteZoomDistanz = distanz;
                }
                pos.recycle();
                break;
            }

            // Primärer Pointer UP
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                pointerPosition = getRelativeClickPosition(ev.getX(), ev.getY(), view.getWidth(), view.getHeight());
                berechneOpenGLKoordinaten(pointerPosition, view);
                isUP = true;
                break;


            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;


            // Prim�rer Pointer UP
            case MotionEvent.ACTION_POINTER_UP:
                // Erkenne, welcher Pointer UP gegangen ist
                final int pointerIndex = (action & 0x0000ff00) >> 0x00000008;
                //final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // Wenn der aktive Pointer UP ging -> Neuen waehlen und Koordinatn waehlen
                    // Wenn pointerIndex == 0 -> 1 sonst 0
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    // Letzte Position ist die des neuen Primaeren Pointers
                    lastTouchedPosition.set(ev.getX(newPointerIndex), ev.getY(newPointerIndex), 0);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;

        }


        return true;
    }

    /**
     * Nur vorläufig, bis wir richtige Positionsbestimmung aus dem Frustum haben...
     *
     * @param clickposition - Transient -> Bekommt neue Koordinaten
     * @return
     */

    @Deprecated
    private void berechneOpenGLKoordinaten(Vector3 clickposition, View view) {
        /* Berechnen der Abmasse der Kamera (Der ganze Screen!!!! in OpenGL Massen)
                *  1. Tatsaechliche Distanz von Kamera zu Sichtfeld berechnen:
                *  2. Breite des OpenGL Sichfeldes berechnen
                *  3. Umrechnungsfaktor Screenmasse -> OpenGL Masse berechnen
                * */

//                float kameradistanzZumSichtfeld = berechneDistanz3D(_camera.getPosition(), _camera.getTarget());
        float kameradistanzZumSichtfeld = _camera.getPosition().getDistance(_camera.getTarget());
        float viewwidth = berechneSichtfeldHorizontal(_camera.getHorizontalFieldOfViewEffective(), kameradistanzZumSichtfeld);

        /*  Umrechnung der Screenbreite in OpenGL Breite
       *   Bsp: Display = 400px / Kamera = 100 px
       *   -> getWidth()= 400px / viewwidth = 100px
       *
       *   umrechnungsFaktor = getWidth() / viewwidth;
       *   umrechnungsFaktor = 400 / 100 = 4
       *
       *   Das Verhaeltnis ist also 4 Pixel auf dem Screen macht eine GL Distanz von 1 Pixel
       * */
        float umrechnungsFaktor = view.getWidth() / viewwidth;

        /* Klickposition auf der Fokusebene berechnen
       *  Bewegung der Kamera + Verrechnete Clickkordinaten = Clickpunkt in OpenGL Szene
       * */

        float clickpunktX = _camera.getTarget().x + pointerPosition.x / umrechnungsFaktor;
        float clickpunktY = _camera.getTarget().y + (-pointerPosition.y) / umrechnungsFaktor;
        pointerPosition.set(clickpunktX, clickpunktY, _camera.getTarget().z);
    }

    // ___________________________________________________________________________


    /**
     * Diese Methode berechnet das Sichtfeld der OpenGL Kamera in der Hoehe
     * <p/>
     * Da die Sichthoehe der Kamera und der Vertikale Blickwinkel bekannt ist, laesst sich ueber den Tangens(alpha)
     * die Gegenkathete berechnen.
     * <p/>
     * B
     * /|
     * /  |
     * /    |
     * /      | a = ? : -> tan(alph) * b
     * /        |
     * /          |
     * A/alpha_______| C
     * b=100
     * <p/>
     * der Winkel muss halbiert werden (Geschieht in der Methode), weil wir ein rechtwinkliges Dreieck brauchen, der Winkel aber �ber die
     * Mittelachse nach oben und unten geht. Halbieren wir den winkel, koennen wir ueber
     * <p/>
     * tangens(alpha)*ankathete = gegenkathete
     * also:
     * tan(alpha)*zDistanz = Sichtbreite in der X Achse erhalten.
     * ACHTUNG: Da wir nur einen halben Winkel errechnet haben, muss das Ergebnis spaeter noch * 2 gerechnet werden  !!!!
     *
     * @param zoomwinkel der Kamera (in Horizontale)
     * @param distanz    zum Sicht-Target
     * @return float Sichtfeld in Horizontaler Achse (X-Achse)
     */
    public float berechneSichtfeldHorizontal(float zoomwinkel, float distanz) {
        // Die L�nge des Horizontalen Sichtfeldes
        return (float) Math.tan(Math.toRadians(zoomwinkel / 2)) * distanz * 2;
    }


    /**
     * Diese Methode berechnet die Vertikale Sichtweite der OpenGL Kamera
     * Dafuer muessen wir die Horizontale Sichtbreite kennen (berechneSichtfeldHorizontal -> imageWidth)
     *
     * @param imageWidth Sichtbreite in Horizontaler Ausrichtung
     * @param aspekt     Seitenverhaeltnis von Hoehe:Breite (z.B. 16:9 -> 1.6666 )
     * @return float Sichtfeld Vertikal
     */
    public float berechneSichtfeldVertikal(float imageWidth, float aspekt) {
        return imageWidth / aspekt;
    }


    /**
     * Diese Methode errechnet die Relative Position des Clicks zur Mitte des Screens bzw. zur Sichtposition der Kamera
     * Es werden K E I N E OpenGL Koordinaten verrechnet. Hier wird lediglich die Screenmitte ermittelt
     * Da die Kamera jedoch direkt in die Mitte des Screens sieht, ist das auch der Mittelpunkt der Kamera
     *
     * @param x      X Wert des Clicks auf Screen
     * @param y      Y Wert des Clicks auf Screen
     * @param width  des Screens
     * @param height des Screens
     * @return relative Screenkoordinaten des Clicks
     */
    public Vector3 getRelativeClickPosition(float x, float y, int width, int height) {
        float relativZuMitteX = x - width / 2;
        float relativZuMitteY = y - height / 2;
        return Vector3.createNew(relativZuMitteX, relativZuMitteY, 0);
    }

    public void setCamera(@NotNull Camera camera) {
        InputEngine._camera = camera;
    }

    public Vector3 getMoveVector() {
        return moveVector;
    }

    public Vector3 getPointerPosition() {
        return pointerPosition;
    }

    public float getZoomDistance() {
        return zoomDistanz;
    }


    public boolean getUpState() {
        if (isUP) {
            isUP = false;
            return true;
        }
        return false;
    }

    public boolean getDownState() {
        if (isDown) {
            isDown = false;
            return true;
        }
        return false;
    }

    public boolean getZoomState() {
        if (isZoomed) {
            isZoomed = false;
            return true;
        }
        return false;
    }
}


