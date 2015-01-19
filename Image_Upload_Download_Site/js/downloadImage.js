/**
 * @author Andre Krause
 */

function searchImage(){
	validationSearch();	
}

function addImage(name, imageData){
	$('#image_container').append(
		'<div class="image">'
		+	'<img src="'+imageData+'" class="image_result" alt="image-result"/>'
		+	'<p class="image_tag">'+name+'</p>'
		+'</div>'
	);
}

function validationSearch(){
	if($('#search_image_field').val()==''){
		$('#search_image_field').attr('class', 'error');
		/*$('#search_image_field').css('display', 'inline');
		$('#search_image_field').text('Kein Name angegeben!');*/
		return false;
	}
	
	return true;
}
