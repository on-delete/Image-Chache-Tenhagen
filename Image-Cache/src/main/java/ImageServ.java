
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

@Path("/ImageServ")
public class ImageServ {
	
	@GET
	@Path("/HelloWorld")
	@Produces(MediaType.TEXT_HTML)
	public String helloWorld(){
		ImageCache.current().storeToCache("test", "helloworld");
		return "<html><head><title>Hello World</title></head><body><h1>Hello World!</h1></body></html>";
	}
	
	@GET
	@Path("/download/{imageName}")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageData getImage(@PathParam("imageName") String name){
		if(ImageCache.current().containsImage(name)){
			return ImageCache.current().loadFromCache(name);
		}else{
			File imageFile = new File("C:\\Users\\Tobias Mack\\Documents\\"+name);
			FileInputStream imageIn = null;
			String image = "";
			try {
				imageIn = new FileInputStream(imageFile);
				byte imageData[] = new byte[(int) imageFile.length()];
				imageIn.read(imageData);
				image = Base64.encodeBase64URLSafeString(imageData);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if(imageIn != null){
						imageIn.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!image.equals("")){
				ImageCache.current().storeToCache(name, image);
				return new ImageData(name, image);
			}
			return null;
		}
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadImage(ImageData imageData){
		
		File file = new File("C:\\Users\\Tobias Mack\\Documents\\"+imageData.getName());
		
		FileOutputStream imageOut = null;
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			imageOut = new FileOutputStream(file);
			imageOut.write(Base64.decodeBase64(imageData.getImageData()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(imageOut != null){
					imageOut.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return Response.status(201).entity(imageData).build();
		
	}
}
