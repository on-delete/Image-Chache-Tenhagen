/**
 * @author Andre Krause
 */



function benchmark(){
	var anzWiederholungen = $('#number_iterations').val();
	$('#results2').text("Anz. Wiederholungen: " + anzWiederholungen);
	$('#results6').text("Anz. Wiederholungen: " + anzWiederholungen);

	var CacheArray = new Array();

	console.log("Abfrage mit Cache");
	subFunctionCache(0, CacheArray);
}

function subFunctionCache(count, CacheArray){
	
	var startTime = new Date() / 1000;
		
		$.ajax({
	  		type: "GET",
	  		url: "/image-cache/ImageServ/download/cache/" + $('#search_image_field_benchmark').val()
		}).done(function(object) {
	    	if(!(typeof object === "undefined")){
	    		var finishTime = new Date() / 1000;
	    		if(count<$('#number_iterations').val()){
	    			var time = finishTime - startTime;
	    			CacheArray.push(time);
	    			console.log("Iteration " + (count  + 1)+ ": Zeit: " + time);
	    			count++;
	    			subFunctionCache(count, CacheArray);
	    		}
	    		else{
	    			CacheArray.sort();
	    			var numNumbers = $('#number_iterations').val();
	    			var lowest = CacheArray[0];
	    			var highest = CacheArray[numNumbers - 1];
	    			$('#results3').text("Höchste Antwortzeit: " + highest);
	    			$('#results4').text("Niedrigste Antwortzeit: " + lowest);
	    			var average = 0;
	    			for(var i =0; i<numNumbers; i++){
	    				average += CacheArray[i];
	    			}
	    			average = average / numNumbers;
	    			$('#results5').text("Durchschnittliche Antwortzeit: " + average);
	    			
	    			var NoCacheArray = new Array();
	    			console.log("Abfrage ohne Cache");
	    			subFunctionNoCache(0, NoCacheArray);
	    		}
	    	}
	  	}).fail(function( jqXHR, textStatus ) {
	  		alert( "Download fehlgeschlagen: " + textStatus );
		});
}

function subFunctionNoCache(count2, NoCacheArray){
	var startTime = new Date() / 1000;
		
		$.ajax({
	  		type: "GET",
	  		url: "/image-cache/ImageServ/download/nocache/" + $('#search_image_field_benchmark').val()
		}).done(function(object) {
	    	if(!(typeof object === "undefined")){
	    		var finishTime = new Date() / 1000;
	    		if(count2<$('#number_iterations').val()){
	    			var time = finishTime - startTime;
	    			NoCacheArray.push(time);
	    			console.log("Iteration " + (count2 + 1) + ": Zeit: " + time);
	    			count2++;
	    			subFunctionNoCache(count2, NoCacheArray);
	    		}
	    		else{
	    			NoCacheArray.sort();
	    			var numNumbers = $('#number_iterations').val();
	    			var lowest = NoCacheArray[0];
	    			var highest = NoCacheArray[numNumbers - 1];
	    			$('#results7').text("Höchste Antwortzeit: " + highest);
	    			$('#results8').text("Niedrigste Antwortzeit: " + lowest);
	    			var average = 0;
	    			for(var i =0; i<numNumbers; i++){
	    				average += NoCacheArray[i];
	    			}
	    			average = average / numNumbers;
	    			$('#results9').text("Durchschnittliche Antwortzeit: " + average);
	    		}
	    	}
	  	}).fail(function( jqXHR, textStatus ) {
	  		alert( "Download fehlgeschlagen: " + textStatus );
		});
}
