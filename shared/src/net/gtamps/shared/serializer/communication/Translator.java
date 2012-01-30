package net.gtamps.shared.serializer.communication;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.gtamps.shared.Utils.Logger;


public class Translator {
	private static String[] lookup = null;
	private static HashMap<String,Byte> reverseLookup = new HashMap<String, Byte>();
	
	private static Byte lookupresult = -1;
	public static byte lookup(String s){
		if(lookup==null){
			init();
		}
		lookupresult = reverseLookup.get(s);
		if(lookupresult==null) return -1;
		return lookupresult;
	}
	public static String lookup(int l){
		if(lookup==null){
			init();
		}
		return lookup[l];
	}
	private static void init(){
		lookup = new String[StringConstants.class.getDeclaredFields().length];
		Logger.d("Translator", lookup.length+" String Constants declared: "+(255-lookup.length)+" left.");
		Byte i = 0;
		LinkedList<String> fieldList = new LinkedList<String>();
		for(Field f: StringConstants.class.getDeclaredFields()){
			try {
				fieldList.add((String) f.get(null));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(fieldList);
		for(String f: fieldList){
				lookup[i] = f;
				reverseLookup.put(lookup[i], i);
				i++;
		}
	}
}
