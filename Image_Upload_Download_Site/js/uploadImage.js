/**
 * @author Andre Krause
 */

function uploadImage(){
	var validationSuccess = validation();
	if(validationSuccess){
		var nameUnique = validateNameUnique();
		
		if(nameUnique){
			readImage();
		}
		else{}
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
	var nameUnique = true;
	
	/*Rest-call hier*/
	if(nameUnique==true){
		return true;
	}
	else{
		$('#input_image_name').attr('class', 'error');
		$('#name_error_message').css('display', 'inline');
		$('#name_error_message').text('Name ist bereits vorhanden!');
		return false;
	}
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
	console.log(result);
	addImage("Hallo", result);
}
