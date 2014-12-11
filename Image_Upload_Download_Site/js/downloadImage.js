/**
 * @author Andre Krause
 */

function addImage(name, imageData){
	$('#image_container').append(
		'<div class="image">'
		+	'<img src="'+imageData+'" class="image_result" alt="image-result"/>'
		+	'<p class="image_tag">'+name+'</p>'
		+'</div>'
	);
}
