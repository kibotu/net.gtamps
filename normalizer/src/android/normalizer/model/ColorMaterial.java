package android.normalizer.model;

public class ColorMaterial {
	
	public final Vector ambient;
	public final Vector diffuse;
	public final Vector specular;
	public final String name;
	
	public ColorMaterial(final String name, final Vector ambient, final Vector diffuse, final Vector specular) {
		this.name = name;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ColorMaterial)) {
			return false;
		}
		ColorMaterial other = (ColorMaterial) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ColorMaterial [ambient=" + ambient + ", diffuse=" + diffuse
				+ ", specular=" + specular + ", name=" + name + "]";
	}
}
