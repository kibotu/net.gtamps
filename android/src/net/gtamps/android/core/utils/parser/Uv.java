package net.gtamps.android.core.utils.parser;

/**
 * Simple VO used for texture positioning 
 */
public class Uv {

	public float u;
	public float v;
	
	public Uv()	{
		u = 0;
		v = 0;
	}
	
	public Uv(float u, float v)	{
		this.u = u;
		this.v = v;
	}

    @Override
	public Uv clone() {
		return new Uv(u, v);
	}
}
