import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ImageCache {
	
	private static ImageCache imageCache = null;
	private HashMap<String, ImageData> cache = null; 
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock  = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	private Thread t = new Thread(){
		//Time between two clears of the cache in ms (60 * 60 * 1000 = 3600000 for one hour)
		private final int TIMEOUT = 3600000;
		public void run(){
			try {
				while(true){
					Thread.sleep(TIMEOUT);
					ImageCache.current().clearCache();					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private ImageCache(){
		cache = new HashMap<String, ImageData>();
		t.start();
	}
	
	public static ImageCache current(){
		if(imageCache == null){
			imageCache = new ImageCache();
		}
		return imageCache;
	}
	
	public void storeToCache(String name, ImageData imageData){
		writeLock.lock();
		try{
			cache.put(name, imageData);
		}finally{
			writeLock.unlock();
		}
	}
	
	public void storeToCache(ImageData imageData){
		writeLock.lock();
		try{
			cache.put(imageData.getName(), imageData);
		}finally{
			writeLock.unlock();
		}
	}
	
	public ImageData loadFromCache(String name){
		readLock.lock();
		try{
			if(cache.containsKey(name)){
				return cache.get(name);
			}
			return null;
		}finally{
			readLock.unlock();
		}
	}
	
	public boolean containsImage(String name){
		return cache.containsKey(name);
	}
	
	public void clearCache(){
		writeLock.lock();
		try{
			cache.clear();
		}finally{
			writeLock.unlock();
		}
	}
	
}
