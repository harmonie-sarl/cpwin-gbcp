package fr.symphonie.budget.ui.doc;

import java.util.HashMap;

public class CustomHashMap<K, V> extends HashMap<K, V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 345287074218231214L;

	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		if(value==null){
			value=(V) "";
		}
		return super.put(key, value);
	}
	
	

}
