package net.gtamps.shared.game.entity;

import java.util.HashMap;

import net.gtamps.shared.game.entity.Entity.Type;

public class EntityTypeProvider {
	private static HashMap<String,Type> valueOf = null;
	
	public static Type valueOf(String name) {
		if(valueOf == null){
			valueOf = new HashMap<String, Entity.Type>();
			for(Type t : Type.values()){
				valueOf.put(t.name().toUpperCase(),t);
			}
		}
		return valueOf.get(name);
	}
}
