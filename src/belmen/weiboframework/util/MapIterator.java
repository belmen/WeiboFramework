package belmen.weiboframework.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapIterator {

	public static interface OnIterateListener<K, V> {
		void onIterate(K name, V value);
	}
	
	public static <K, V> void iterate(Map<K, V> map, OnIterateListener<K, V> l) {
		if(map == null || l == null) {
			return;
		}
		Iterator<Entry<K, V>> iter = map.entrySet().iterator();
		Entry<K, V> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			l.onIterate(entry.getKey(), entry.getValue());
		}
	}
}
