package net.gtamps.shared.serializer.helper;

public class ArrayPointer {
	private int val = 0;
	public ArrayPointer(){
		
	}
	public void inc(int i){
		val += i;
	}
	public int pos(){
		return val;
	}
	public void set(int i){
		val = i;
	}
	public void reset() {
		this.set(0);
	}
}
