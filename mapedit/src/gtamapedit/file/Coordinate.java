package gtamapedit.file;

public class Coordinate {
	float x;
	float y;
	public Coordinate(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		return "Coordinate x:"+x+" y:"+y;
	}
}
