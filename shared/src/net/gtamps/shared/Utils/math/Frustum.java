package net.gtamps.shared.Utils.math;

import org.jetbrains.annotations.NotNull;

/**
 * Work in progress
 */
public final class Frustum {

    /**
     * Entfernung der nahen Plane
     */
    private float nearDistance;

    /**
     * Entfernung der fernen Plane
     */
    private float farDistance;

    private boolean hasDepthTest = true;

    /**
     * Sichtwinkel in Grad
     */
    private float fovy;

    /**
     * Der inverse Zoomfaktor (1/Zoomfaktor)
     *
     * @see #zoomFactor
     */
    private float inverseZoomFactor = 1.0f;

    /**
     * Der Zoomfaktor
     *
     * @see #inverseZoomFactor
     */
    private float zoomFactor = 1.0f;

    /**
     * Aspektverhältnis
     */
    private float aspectRatio;

    /**
     * Der Tangens des Blickwinkels
     */
    private float tangens;

    /**
     * Höhe der near plane
     */
    private float height;

    /**
     * Breite der near plane
     */
    private float width;

    /**
     * Kameraposition, ...
     */
    @NotNull
    private Vector3 position, target, up;

    /**
     * Kamerareferenzvektoren
     */
    @NotNull
    private Vector3 xAxis = Vector3.createNew();
    @NotNull
    private Vector3 yAxis = Vector3.createNew();
    @NotNull
    private Vector3 zAxis = Vector3.createNew();

    /**
     * Kugel-Faktoren
     */
    private float sphereFactorY, sphereFactorX;

    protected Matrix4 projectionMatrix = Matrix4.createNew();
    protected Matrix4 viewMatrix = Matrix4.createNew();
    protected Matrix4 normalMatrix = Matrix4.createNew();
    public static final float MIN_ZOOM = 5;
    public static final float MAX_ZOOM = 90;

    /**
     * Erzeugt einen neuen Sichtkegel
     *
     * @param aspectRatio  Das Apsektverh�ltnis
     * @param fov          Das Blickfeld in Radians
     * @param nearDistance Die Entfernung zur nahen Ebene
     * @param farDistance  Die Entfernung zur fernen Ebene
     */
    public Frustum(float aspectRatio, float fov, float nearDistance, float farDistance) {
        this(aspectRatio, fov, 1.0f, nearDistance, farDistance);
    }

    /**
     * Erzeugt einen neuen Sichtkegel
     *
     * @param aspectRatio  Das Apsektverhältnis
     * @param fov          Das Blickfeld in Radians
     * @param zoomFactor   Der Zoomfaktor
     * @param nearDistance Die Entfernung zur nahen Ebene
     * @param farDistance  Die Entfernung zur fernen Ebene
     */
    public Frustum(float aspectRatio, float fov, float zoomFactor, float nearDistance, float farDistance) {
        set(aspectRatio, fov, zoomFactor, nearDistance, farDistance);
    }

    /**
     * Setzt die Parameter des Sichtkegels
     *
     * @param aspectRatio  Das Apsektverhältnis
     * @param fov          Das Blickfeld in Radians
     * @param nearDistance Die Entfernung zur nahen Ebene
     * @param farDistance  Die Entfernung zur fernen Ebene
     */
    public void set(float aspectRatio, float fov, float nearDistance, float farDistance) {
        set(aspectRatio, fov, 1.0f, nearDistance, farDistance);
    }

    /**
     * Setzt die Parameter des Sichtkegels
     *
     * @param aspectRatio  Das Apsektverhältnis
     * @param fov          Das Blickfeld in Radians
     * @param zoomFactor   Der Zoomfaktor
     * @param nearDistance Die Entfernung zur nahen Ebene
     * @param farDistance  Die Entfernung zur fernen Ebene
     */
    public void set(float aspectRatio, float fov, float zoomFactor, float nearDistance, float farDistance) {
        assert aspectRatio > 0;
        assert fov > 0;
        assert zoomFactor > 0;
        assert nearDistance > 0;
        assert farDistance > nearDistance;

        this.aspectRatio = aspectRatio;
        this.nearDistance = nearDistance;
        this.farDistance = farDistance;
        fovy = fov;
        setZoomFactorWithoutUpdatingPlanes(zoomFactor);

        calculatePlaneSizes();
    }

    /**
     * Setzt den Zoomfaktor, ohne die Planes zu aktualisieren
     *
     * @param zoomFactor Der Zoomfaktor
     * @see #calculatePlaneSizes()
     */
    private void setZoomFactorWithoutUpdatingPlanes(float zoomFactor) {
        assert zoomFactor > 0;

        inverseZoomFactor = 1.0f / zoomFactor;
        this.zoomFactor = zoomFactor;
    }

    /**
     * Setzt das horizontale Blickfeld und berechnet den Sichtkegel neu
     *
     * @param fov       Das Sichtfeld in Radians
     * @param resetZoom Gibt an, ob der Zoomfaktor zurückgesetzt werden soll
     * @see #setHorizontalFieldOfView(float, float)
     */
    public void setHorizontalFieldOfView(float fov, boolean resetZoom) {
        assert fov > 0;

        if (resetZoom) setZoomFactorWithoutUpdatingPlanes(1.0f);
        fovy = fov;

        calculatePlaneSizes();
    }

    /**
     * Setzt das horizontale Blickfeld und berechnet den Sichtkegel neu
     *
     * @param fov        Das Sichtfeld in Radians
     * @param zoomFactor Der Zoomfaktor
     */
    public void setHorizontalFieldOfView(float fov, float zoomFactor) {
        assert fov > 0;
        assert zoomFactor > 0;

        fovy = fov;
        setZoomFactorWithoutUpdatingPlanes(zoomFactor);

        calculatePlaneSizes();
    }

    /**
     * Liefert das horizontale Sichtfeld
     *
     * @return Winkel in Radians
     * @see #getZoomFactor()
     * @see #getHorizontalFieldOfViewEffective()
     */
    public float getHorizontalFieldOfView() {
        return fovy;
    }

    /**
     * Liefert den Zoomfaktor
     *
     * @return Der Zoomfaktor
     * @see #getHorizontalFieldOfView()
     */
    public float getZoomFactor() {
        return zoomFactor;
    }

    /**
     * Liefert den inversen Zoomfaktor
     *
     * @return Der inverse Zoomfaktor
     * @see #getHorizontalFieldOfView()
     */
    public float getInverseZoomFactor() {
        return inverseZoomFactor;
    }

    /**
     * Liefert den Winkel des horizontalen Sichtbereiches unter Beachtung des Zoomfaktors
     *
     * @return Der Winkel in Radians
     * @see #getHorizontalFieldOfView()
     */
    public float getHorizontalFieldOfViewEffective() {
        return getHorizontalFieldOfView() * getInverseZoomFactor();
    }

    /**
     * Liefert das Aspektverhältnis
     *
     * @return Das Aspektverhältnis
     */
    public float getAspectRatio() {
        return aspectRatio;
    }

    /**
     * Berechnet die Größen der nahen und fernen Plane
     */
    private void calculatePlaneSizes() {
        tangens = (float) Math.tan(fovy * inverseZoomFactor); // TODO: Noch weiter vorberechnen?
        height = nearDistance * tangens;
        width = height * aspectRatio;

        sphereFactorY = 1.0f / (float) Math.cos(fovy);

        // compute half of the the horizontal field of view and sphereFactorX
        float anglex = (float) Math.atan(tangens * aspectRatio);
        sphereFactorX = 1.0f / (float) Math.cos(anglex);

        setPerspectiveProjection();
    }

    /**
     * Setzt die Kameraparameter
     *
     * @param position Die Position der Kamera
     * @param target   Der Punkt, auf den die Kamera blickt
     * @param up       Der Hoch-Vektor der Kamera
     */
    public void setCamera(@NotNull Vector3 position, @NotNull Vector3 target, @NotNull Vector3 up) {
        // http://www.lighthouse3d.com/opengl/viewfrustum/index.php?gimp

        this.position = position;
        this.target = target;
        this.up = up;

        // compute the Z axis of the camera referential
        // this axis points in the same direction from
        // the looking direction
        zAxis.set(position).subInPlace(target);
        zAxis.normalize();

        // X axis of camera with given "up" vector and Z axis
        xAxis.set(zAxis).crossInPlace(up);
        xAxis.normalize();

        // the real "up" vector is the dot product of X and Z
        yAxis.set(xAxis).crossInPlace(zAxis);
    }

    public void setCamera(float posX, float posY, float posZ, float targetX, float targetY, float targetZ, float upX, float upY, float upZ) {
        this.position.set(posX,posY,posZ);
        this.target.set(targetX, targetY,targetZ);
        this.up.set(upX,upY,upZ);
        setCamera(position,target,up);
    }

    /**
     * Ermittelt, ob ein Punkt im Sichtfeld liegt
     *
     * @param point Der Punkt
     * @return Testergebnis
     */
    public Intersection contains(@NotNull Vector3 point) {

        float pcz, pcx, pcy, aux;

        // compute vector from camera position to p
        Vector3 v = point.sub(position);

        // compute and test the Z coordinate
        pcz = v.dot(zAxis);
        if (pcz > farDistance || pcz < nearDistance)
            return Intersection.OUTSIDE;

        // compute and test the Y coordinate
        pcy = v.dot(yAxis);
        aux = pcz * tangens;
        if (pcy > aux || pcy < -aux)
            return Intersection.OUTSIDE;

        // compute and test the X coordinate
        pcx = v.dot(xAxis);
        aux = aux * aspectRatio;
        if (pcx > aux || pcx < -aux)
            return Intersection.OUTSIDE;

        return Intersection.INSIDE;
    }

    /**
     * Ermittelt, ob eine Kugel vom Sichtfeld eingeschlossen ist
     *
     * @param sphere Die Kugel
     * @return Testergebnis
     */
    public Intersection contains(@NotNull Sphere sphere) {

        Intersection result = Intersection.INSIDE;

        Vector3 position = sphere.getPosition();
        float radius = sphere.getRadius();

        float d;
        float az, ax, ay;

        Vector3 v = position.sub(this.position);

        az = v.dot(zAxis);
        if (az > farDistance + radius || az < nearDistance - radius)
            return (Intersection.OUTSIDE);
        if (az > farDistance - radius || az < nearDistance + radius)
            result = Intersection.INTERSECTS;

        ay = v.dot(yAxis);
        d = sphereFactorY * radius;
        az *= tangens;
        if (ay > az + d || ay < -az - d)
            return (Intersection.OUTSIDE);
        if (ay > az - d || ay < -az + d)
            result = Intersection.INTERSECTS;

        ax = v.dot(xAxis);
        az *= aspectRatio;
        d = sphereFactorX * radius;
        if (ax > az + d || ax < -az - d)
            return (Intersection.OUTSIDE);
        if (ax > az - d || ax < -az + d)
            result = Intersection.INTERSECTS;

        return (result);
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @NotNull
    public Vector3 getPosition() {
        return position;
    }

    @NotNull
    public Vector3 getTarget() {
        return target;
    }

    public void rotate(float angle, float x, float y, float z) {
        Vector3 newView = Vector3.createNew(x, y, z);
        Matrix4 rotMatrix = MatrixFactory.getRotationAxisAngle(newView, angle);
        target.set(rotMatrix.transform(target.sub(position)).add(position));
    }

    public void setTarget(Vector3 point) {
        target.set(point);
    }

    public void move(Vector3 offset, boolean moveTarget) {
        position.addInPlace(offset);
        if (moveTarget) target.addInPlace(offset);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    public void move(float offsetX, float offsetY, float offsetZ, boolean moveTarget) {
        position.addInPlace(offsetX, offsetY, offsetZ);
        if (moveTarget) target.addInPlace(offsetX, offsetY, offsetZ);
    }

    public float getNearDistance() {
        return nearDistance;
    }

    public float getFarDistance() {
        return farDistance;
    }

    @NotNull
    public Vector3 getUp() {
        return up;
    }

    public void setPerspectiveProjection() {
        setPerspectiveProjection(projectionMatrix);
    }

    public void setPerspectiveProjection(Matrix4 matrix) {
        Matrix4.setPerspectiveProjection(matrix, getHorizontalFieldOfViewEffective(), getNearDistance(), getFarDistance(), getAspectRatio());
    }

    public void setOrthographicProjection() {
        setOrthographicProjection(projectionMatrix);
    }

    public void setOrthographicProjection(Matrix4 projectionMatrix) {
        Matrix4.setOrthographicProjection(projectionMatrix, -getAspectRatio(), getAspectRatio(), -1, 1, getNearDistance(), getFarDistance());
    }

    public void setTarget(float x, float y, float z) {
        target.set(x,y,z);
    }

    public Matrix4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4 getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4 getNormalMatrix() {
        return normalMatrix;
    }

    public boolean hasDepthTest() {
        return hasDepthTest;
    }

    public void enableDepthTest(boolean enableDepthTest) {
        hasDepthTest = enableDepthTest;
    }

    public void move(float x, float y, float z) {
        move(x,y,z,false);
    }

    public float getDistanceToVieField() {
        return position.getDistance(target);
    }

    public void setZoomFactor(float factor) {
        if (fovy / factor >= MIN_ZOOM && fovy / factor <= MAX_ZOOM) {
            setHorizontalFieldOfView(fovy, factor);
        }
    }
}
