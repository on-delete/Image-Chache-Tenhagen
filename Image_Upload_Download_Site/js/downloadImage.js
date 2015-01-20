/**
 * @author Andre Krause
 */

function searchImage(){
	$('#name_error_message_search').css('display', 'none');
	var validationSuccess = validationSearch();
	if(validationSuccess){
		downloadImage();
	}
	else{}	
}

function downloadImage(){
	$.ajax({
  		type: "GET",
  		url: "/image-cache/ImageServ/download/cache/" + $('#search_image_field').val(),
	}).done(function(object) {
    	if(!(typeof object === "undefined")){
    		addImage(object.name, object.imageData, object.contentType);
    	}
    	else{
    		$('#name_error_message_search').css('display', 'inline');
    		$('#name_error_message_search').text('Bild wurde nicht gefunden!');
    		$('#image_container').empty();
    	}
  	}).fail(function( jqXHR, textStatus ) {
  		alert( "Download fehlgeschlagen: " + textStatus );
	});
}

function addImage(name, imageData, contentType){
	imageData = "data:" + contentType + ";base64," + imageData;
	
	$('#image_container').empty();
	
	$('#image_container').append(
		'<div class="image">'
		+	'<img src="'+imageData+'" class="image_result" alt="image-result"/>'
		+	'<p class="image_tag">'+name+'</p>'
		+'</div>'
	);
}

function validationSearch(){
	if($('#search_image_field').val()==''){
		$('#name_error_message_search').css('display', 'inline');
    	$('#name_error_message_search').text('Kein Name angegeben!');
		return false;
	}
	
	return true;
}
