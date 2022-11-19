//funktio tietojen hakemista varten. Kutsutaan backin GET metodia
function haeAutot() {
	let url = "autot"; 
	let requestOptions = {
        method: "GET",
        headers: { "Content-Type": "application/x-www-form-urlencoded" }       
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi 
   	.then(response => printItems(response)) 
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Kirjoitetaan tiedot taulukkoon JSON-objektilistasta
function printItems(respObjList){
	//console.log(respObjList);
	let htmlStr="";
	for(let item of respObjList){//yksi kokoelmalooppeista		
    	htmlStr+="<tr id='rivi_"+item.id+"'>";
    	htmlStr+="<td>"+item.rekno+"</td>";
    	htmlStr+="<td>"+item.merkki+"</td>";
    	htmlStr+="<td>"+item.malli+"</td>";
    	htmlStr+="<td>"+item.vuosi+"</td>";     	
    	htmlStr+="</tr>";    	
	}	
	document.getElementById("tbody").innerHTML = htmlStr;	
}