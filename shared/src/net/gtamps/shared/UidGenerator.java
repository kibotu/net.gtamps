package net.gtamps.shared;

public class UidGenerator {

	private static int nextId = 0;
	
	public static int getNextUid() {
		return nextId++;
	}
	
	
}
