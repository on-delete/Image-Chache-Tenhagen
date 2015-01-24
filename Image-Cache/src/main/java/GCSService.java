import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.StorageObject;
import com.google.appengine.repackaged.org.apache.commons.codec.binary.Base64;


public class GCSService {

	private static String AUTH_TOKEN = "ya29.BQHBkFfNLm79y7HQGJo7yUxkElfQ-z9fsjrI6rE5_eG3ivgOyqDaC9aRNr8e5xNMeZ43fSc3x-x6eQ";
	
	public static boolean uploadImage(String imageData, String name, String contentType){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		/*Die POST-Funktion auf den Storage. Bei "name" wird der Name der Datei angegeben in der URL*/
		HttpPost httppost = new HttpPost("https://www.googleapis.com/upload/storage/v1/b/imagecachebucket/o?uploadType=media&name=" + name);
        /*Der Header Authorization ist notwendig, weil man sich beim Storage authentifizieren muss.*/
		httppost.setHeader("Authorization", "Bearer " + AUTH_TOKEN);
        
		/*Hier wird der String wieder in ein Byte-Array umgewandelt, weil der Storage nur echte Dateien lesen kann.*/
        Base64 decoder = new Base64();   
        byte[] imgBytes = decoder.decode(imageData); 

        /*Hier wird das Bild als eine Entity festgelegt, die anschließend hochgeladen wird.*/
        ByteArrayEntity entity = new ByteArrayEntity(imgBytes, ContentType.create(contentType));
        httppost.setEntity(entity);
        System.out.println( "executing request " + httppost.getRequestLine( ) );
        try {
        	/*Hier wird der POST ausgeführt*/
            HttpResponse response = httpclient.execute( httppost );
            System.out.println("response: " + response.getStatusLine().toString());
            httpclient.close();
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public static ImageData downloadImage(String name){
		try{
			/*Das sind notwendige Objekte für Google*/
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			
			/*Hier wird wieder die Authentifizierung gemacht.*/
			GoogleCredential credential = new GoogleCredential().setAccessToken(AUTH_TOKEN);
			
			Storage storage = new Storage.Builder(httpTransport, jsonFactory, credential).setApplicationName("Image-Chache").build();
			
			/*Hier wird der Bucket und der name des Bildes festgelegt, was runtergeladen wird. Das Bucket ist wie eine Art Ordner.*/
			Storage.Objects.Get getObject = storage.objects().get("imagecachebucket", name);
			getObject.setFields("contentType");
			
			StorageObject object = getObject.execute();
	
			/*Hier wird das Bild heruntergeladen und in ein ByteArrayOutputStream geschrieben.*/
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			getObject.getMediaHttpDownloader().setDirectDownloadEnabled(true);
			getObject.executeMediaAndDownloadTo(out);
			
			/*Hier wird der byte-Stream wieder in einen Base64-String umgewandelt, der dann in den Cache gespeichert wird.*/
			String imageData2 = Base64.encodeBase64String(out.toByteArray());
			
			return new ImageData(name, imageData2, object.getContentType());
		}
		
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> getAllImageNames() {
		try {
			List<String> nameList = new ArrayList<String>();

			/* Das sind notwendige Objekte für Google */
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

			/* Hier wird wieder die Authentifizierung gemacht. */
			GoogleCredential credential = new GoogleCredential().setAccessToken(AUTH_TOKEN);

			Storage storage = new Storage.Builder(httpTransport, jsonFactory, credential).setApplicationName("Image-Chache").build();

			Storage.Objects.List listObjects = storage.objects().list("imagecachebucket");

			Objects objects;

			do {
				objects = listObjects.execute();
				List<StorageObject> items = objects.getItems();
				if (null == items) {
					break;
				}
				for (StorageObject object : items) {
					nameList.add(object.getName());
				}
				listObjects.setPageToken(objects.getNextPageToken());
			} while (null != objects.getNextPageToken());
			
			return nameList;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
