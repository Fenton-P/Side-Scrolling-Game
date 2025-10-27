package gameCode;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListHandler {
	public static <T> ArrayList<T> arrToList(T[] arr) {
		ArrayList<T> list = new ArrayList<>();
		
		for(T i : arr) list.add(i);
		
		return list;
	}
}
