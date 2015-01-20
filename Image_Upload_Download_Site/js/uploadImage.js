/**
 * @author Andre Krause
 */

function uploadImage(){
	$('#image_success').css('display', 'none');
	var validationSuccess = validation();
	if(validationSuccess){
		validateNameUnique();
	}
	else{}
}

function validation(){
	if($('#input_image_name').val()==''){
		$('#input_image_name').attr('class', 'error');
		$('#name_error_message').css('display', 'inline');
		$('#name_error_message').text('Kein Name angegeben!');
		return false;
	}
	else{
		$('#input_image_name').attr('class', '');
		$('#name_error_message').css('display', 'none');
		$('#name_error_message').text('');
	}
	
	var file = $('#fileToUpload').prop("files")[0];
	
	if(!file){
		$('#image_error_message').css('display', 'inline');
		$('#image_error_message').text('Kein Bild ausgewählt!');
		return false;
	}
	else{
		$('#image_error_message').css('display', 'none');
		$('#image_error_message').text('');
	}
	
	return true;
}

function validateNameUnique(){
	$.ajax({
  		type: "GET",
  		url: "/image-cache/ImageServ/checkname/" + $('#input_image_name').val(),
	}).done(function() {
    	readImage();
  	}).fail(function( jqXHR, textStatus ) {
  		alert( "Anfrage fehlgeschlagen: " + textStatus );
  		setImageNameUsed();
	});	
}

function setImageNameUsed(){
	$('#input_image_name').attr('class', 'error');
	$('#name_error_message').css('display', 'inline');
	$('#name_error_message').text('Name ist bereits vorhanden!');
}

function readImage(){
	var file = $('#fileToUpload').prop("files")[0];
	var reader = new FileReader();
	
	reader.onloadend = function () {
    	sendImage(reader.result);
  	};
	
	reader.readAsDataURL(file);
}

function sendImage(result){
	/*data:image/...;base64,...*/
	var splittedString = result.split(",");
	
	/*data:image/...;base64*/
	var splittedString2 = splittedString[0].split(":");
	/*image/...;base64*/
	var splittedString3 = splittedString2[1].split(";");
	
	var imageData = splittedString[1];
	/*image/...*/
	var contentType = splittedString3[0];
	
	var jsonObject = {
		"name" : $('#input_image_name').val(),
		"imageData" : imageData,
		"contentType" : contentType
	};
	
	$.ajax({
  		type: "POST",
  		url: "/image-cache/ImageServ/upload",
  		contentType: "application/json",
  		data: JSON.stringify(jsonObject)
	}).done(function() {
		$('#image_success').css('display', 'inline');
  	}).fail(function( jqXHR, textStatus ) {
  		alert( "Upload fehlgeschlagen: " + textStatus );
	});
		
	console.log(result);
}
