import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
import com.google.appengine.repackaged.org.apache.commons.codec.binary.Base64;


public class GCSService {

	public static void uploadImage(String imageData, String name){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		/*Die POST-Funktion auf den Storage. Bei "name" wird der Name der Datei angegeben in der URL*/
		HttpPost httppost = new HttpPost("https://www.googleapis.com/upload/storage/v1/b/imagecachebucket/o?uploadType=media&name=" + name);
        /*Der Header Authorization ist notwendig, weil man sich beim Storage authentifizieren muss.*/
		httppost.setHeader("Authorization", "Bearer ya29._wC9fgLSc0uU9gl9Az8J3ltbQQTaKHmvALj6Z60RXulwzIGMK9K4mJH8zRmQ8Qno1GRhivUms8BzRA");
        
		/*Hier wird der String wieder in ein Byte-Array umgewandelt, weil der Storage nur echte Dateien lesen kann.*/
        Base64 decoder = new Base64();   
        byte[] imgBytes = decoder.decode(imageData); 

        /*Hier wird das Bild als eine Entity festgelegt, die anschließend hochgeladen wird.*/
        ByteArrayEntity entity = new ByteArrayEntity(imgBytes, ContentType.create("image/jpeg"));
        httppost.setEntity(entity);
        System.out.println( "executing request " + httppost.getRequestLine( ) );
        try {
        	/*Hier wird der POST ausgeführt*/
            HttpResponse response = httpclient.execute( httppost );
            System.out.println("response: " + response.getStatusLine().toString());
            httpclient.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static String downloadImage(String name){
		try{
			/*Das sind notwendige Objekte für Google*/
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
			
			/*Hier wird wieder die Authentifizierung gemacht.*/
			GoogleCredential credential = new GoogleCredential().setAccessToken("ya29._wC9fgLSc0uU9gl9Az8J3ltbQQTaKHmvALj6Z60RXulwzIGMK9K4mJH8zRmQ8Qno1GRhivUms8BzRA");
			
			Storage storage = new Storage.Builder(httpTransport, jsonFactory, credential).build();
			
			/*Hier wird der Bucket und der name des Bildes festgelegt, was runtergeladen wird. Das Bucket ist wie eine Art Ordner.*/
			Storage.Objects.Get getObject = storage.objects().get("imagecachebucket", name);
	
			/*Hier wird das Bild heruntergeladen und in ein ByteArrayOutputStream geschrieben.*/
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			getObject.getMediaHttpDownloader().setDirectDownloadEnabled(true);
			getObject.executeMediaAndDownloadTo(out);
			
			/*Hier wird der byte-Stream wieder in einen Base64-String umgewandelt, der dann in den Cache gespeichert wird.*/
			String imageData2 = Base64.encodeBase64String(out.toByteArray());
			if(Base64.isBase64(imageData2))
				System.out.println("JA!");
			
			
			return imageData2;
		}
		
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
