package android.normalizer.model;

public class UV {
	
	public final float u;
	public final float v;
	
	public UV(final float u, final float v) {
		this.u = u;
		this.v = v;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(u);
		result = prime * result + Float.floatToIntBits(v);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UV)) {
			return false;
		}
		UV other = (UV) obj;
		if (Float.floatToIntBits(u) != Float.floatToIntBits(other.u)) {
			return false;
		}
		if (Float.floatToIntBits(v) != Float.floatToIntBits(other.v)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return u + " " + v;
	}
}