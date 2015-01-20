import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ImageCache {
	
	private static ImageCache imageCache = null;
	private HashMap<String, ImageData> cache = null; 
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock  = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	private ImageCache(){
		cache = new HashMap<String, ImageData>();
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
	
}
