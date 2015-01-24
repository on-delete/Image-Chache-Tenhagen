import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ImageServ")
public class ImageServ {

	@GET
	@Path("/HelloWorld")
	@Produces(MediaType.TEXT_HTML)
	public String helloWorld() {
		return "<html><head><title>Hello World</title></head><body><h1>Hello World!</h1></body></html>";
	}

	@GET
	@Path("/download/cache/{imageName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageCache(@PathParam("imageName") String name) {
		if (ImageCache.current().containsImage(name)) {
			return Response.status(200).entity(ImageCache.current().loadFromCache(name)).build();
		} else {
			ImageData image = GCSService.downloadImage(name);

			if (image != null) {
				ImageCache.current().storeToCache(name, image);
				return Response.status(200).entity(image).build();
			}
			return Response.status(500).build();
		}
	}

	@GET
	@Path("/download/nocache/{imageName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageNoCache(@PathParam("imageName") String name) {
		ImageData image = GCSService.downloadImage(name);

		if (image != null) {
			ImageCache.current().storeToCache(name, image);
			return Response.status(200).entity(image).build();
		}
		return Response.status(500).build();
	}

	@GET
	@Path("/checkname/{imageName}")
	public Response checkName(@PathParam("imageName") String name) {
		List<String> nameList = GCSService.getAllImageNames();
		if(nameList!=null){
			if(nameList.size()==0){
				return Response.status(200).build();
			}
			else if(!(nameList.contains(name))){
				return Response.status(200).build();
			}
			else{
				return Response.status(404).build();
			}
		}
		else{
			return Response.status(500).build();
		}
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadImage(ImageData imageData) {
		if (GCSService.uploadImage(imageData.getImageData(), imageData.getName(), imageData.getContentType())) {
			return Response.status(200).build();
		} else {
			return Response.status(500).build();
		}

	}
}
