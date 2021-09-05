package myAdmin.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpiryMap<K, V> implements Map<K, V> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpiryMap.class);

	
	private boolean isRefresh = false;
	
	private ConcurrentHashMap workMap = new ConcurrentHashMap();
	private ConcurrentHashMap<Object, Long> expiryMap = new ConcurrentHashMap<>();
	
	public ExpiryMap() {
		super();
	}
	
	public ExpiryMap(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}
	
	/**
	 * 定时清除失效key
	 */
	{
		int interval = 60;// 间隔时间，单位：分
		int threshold = 10000;// map的容量阈值，达到该值后，将较频繁的执行key扫描工作！
		new Timer().schedule(new TimerTask() {
			int i = 0;
			
			@Override
			public void run() {
				try {
					boolean isRun = ++i % interval == 0 || expiryMap.keySet().size() > threshold;
					if (isRun) {
						removeInValidKeys();
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(),e);
				}
			}
		}, 60 * 1000, 60 * 1000);// 每隔1分钟启动一次
	}
	
	private void removeInValidKeys() {
		expiryMap.keySet().forEach(key -> {
			if (expiryMap.get(key) < System.currentTimeMillis()) {
				expiryMap.remove(key);
				workMap.remove(key);
			}
		});
		System.gc();
	}
	
	/**
	 * put方法，需要设置key 的有效期！单位为：毫秒
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 *            key的有效期，单位：毫秒
	 * @return
	 */
	public V put(K key, V value, long expiry) {
		if (!containsKey(key) || isRefresh) {// 更新value，只有需要刷新时间时才需要操作expiryMap
			expiryMap.put(key, System.currentTimeMillis() + expiry);
		}
		workMap.put(key, value);
		return value;
	}
	
	@Override
	public int size() {
		return keySet().size();
	}
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public boolean containsKey(Object key) {
		if (key != null && expiryMap.containsKey(key)) {
			boolean flag = expiryMap.get(key) > System.currentTimeMillis();
			return flag;
		}
		return false;
	}
	
	@Override
	public boolean containsValue(Object value) {
		Collection values = workMap.values();
		if (values != null) {
			return values.contains(value);
		}
		return false;
	}
	
	@Override
	public V get(Object key) {
		if (containsKey(key)) {
			return (V) workMap.get(key);
		}
		return null;
	}
	
	@Deprecated
	@Override
	public V put(K key, V value) {
		throw new RuntimeException("此方法已废弃！请加上key失效时间");
	}
	
	@Override
	public V remove(Object key) {
		boolean containKey = containsKey(key);
		expiryMap.remove(key);
		if (containKey) {
			return (V) workMap.remove(key);
		} else {
			return null;
		}
		
	}
	
	@Deprecated
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new RuntimeException("此方法已废弃！");
	}
	
	@Override
	public void clear() {
		expiryMap.clear();
		workMap.clear();
	}
	
	@Override
	public Set<K> keySet() {
		removeInValidKeys();
		return workMap.keySet();
	}
	
	@Override
	public Collection<V> values() {
		removeInValidKeys();
		return workMap.values();
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		removeInValidKeys();
		return workMap.entrySet();
	}
	
	public static void main(String[] args) {
		ExpiryMap emap = new ExpiryMap();
		emap.put("key1", "value1", 100);
		emap.put("key2", "value2", 15);
		emap.put("key3", "value3", 5);
		System.out.println(emap.size());
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(emap.size());
		System.out.println(emap.keySet());
		System.out.println(emap.values());
		System.out.println(emap.entrySet());
	}
}
