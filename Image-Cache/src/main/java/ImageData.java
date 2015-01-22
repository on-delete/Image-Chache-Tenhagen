public class ImageData {
	
	private String name;
	
	private String contentType;
	
	private String imageData;
	
	public ImageData(){}
	
	public ImageData(String name, String imageData, String contentType){
		this.name = name;
		this.imageData = imageData;
		this.contentType = contentType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageData() {
		return this.imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "ImageData [name=" + name + ", imageData=" + imageData + ", contentType=" + contentType +"]";
	}
	
	
}
