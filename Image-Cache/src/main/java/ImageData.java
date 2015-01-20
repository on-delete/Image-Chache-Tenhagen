public class ImageData {
	
	private String name;
	
	private String imageData;
	
	public ImageData(){}
	
	public ImageData(String name, String imageData){
		this.name = name;
		this.imageData = imageData;
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

	@Override
	public String toString() {
		return "ImageData [name=" + name + ", imageData=" + imageData + "]";
	}
	
	
}
