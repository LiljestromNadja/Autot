//funktio lomaketietojen muuttamiseksi JSON-stringiksi (automaattisesti)
function serialize_form(form){
	return JSON.stringify(
	    Array.from(new FormData(form).entries())
	        .reduce((m, [ key, value ]) => Object.assign(m, { [key]: value }), {})
	        );	
}



//funktio tietojen hakemista varten. Kutsutaan backin GET metodia
function haeAutot() {
	let url = "autot?hakusana=" + document.getElementById("hakusana").value; /**Lähetetään hakusana backendiin */
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
    	htmlStr+="<td><span class='poista' onclick=varmistaPoisto("+item.id+",'"+encodeURI(item.rekno)+"')>Poista</span></td>"; //encodeURI() muutetaan erikoismerkit, välilyönnit jne. UTF-8 merkeiksi.
    	htmlStr+="</tr>";    	
	}	
	document.getElementById("tbody").innerHTML = htmlStr;	
}

// Tutkitaan lisättävät tiedot ennen niiden lähettämistä backendiin
function tutkiJaLisaa() {
	if(tutkiTiedot()){ //Jos syöttöarvot ovat kelvolliset, eli sama kuin if tutkiTiedot() == true
		lisaaTiedot();
	}
}

//funktio syöttötietojen tarkistamista varten
function tutkiTiedot() {
	let ilmo="";	
	let d = new Date();
	if(document.getElementById("rekno").value.length<3){
		ilmo="Rekisterinumero ei kelpaa!";	
		document.getElementById("rekno").focus();	
	}else if(document.getElementById("merkki").value.length<2){
		ilmo="Merkki ei kelpaa!";
		document.getElementById("merkki").focus();			
	}else if(document.getElementById("malli").value.length<1){
		ilmo="Malli ei kelpaa!";	
		document.getElementById("malli").focus();	
	}else if(document.getElementById("vuosi").value*1!=document.getElementById("vuosi").value){
		ilmo="Vuosi ei ole luku!";	
		document.getElementById("vuosi").focus();	
	}else if(document.getElementById("vuosi").value<1900 || document.getElementById("vuosi").value>d.getFullYear()+1){
		ilmo="Vuosi ei kelpaa!";	
		document.getElementById("vuosi").focus();	
	}
	if(ilmo!=""){
		document.getElementById("ilmo").innerHTML=ilmo;
		setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 3000);   //3000 millisenkuntia  niin ilmo poistuu, InnerHTML- arvo tyhjä
		return false;
	}else{
		document.getElementById("rekno").value=siivoa(document.getElementById("rekno").value);
		document.getElementById("merkki").value=siivoa(document.getElementById("merkki").value);
		document.getElementById("malli").value=siivoa(document.getElementById("malli").value);
		document.getElementById("vuosi").value=siivoa(document.getElementById("vuosi").value);	
		return true;
	}
}

//Funktio XSS-hyökkäysten estämiseksi (Cross-site scripting)
function siivoa(teksti){
	teksti=teksti.replace(/</g, "");//&lt;
	teksti=teksti.replace(/>/g, "");//&gt;	
	teksti=teksti.replace(/'/g, "''");//&apos;	
	return teksti;
}

//funktio tietojen lisäämistä varten. Kutsutaan backin POST-metodia ja välitetään kutsun mukana auton tiedot json-stringinä.
function lisaaTiedot(){
	let formData = serialize_form(document.lomake); //Haetaan tiedot lomakkeelta ja muutetaan JSON-stringiksi käyttäen serialize_form()ia
	//console.log(formData);
	let url = "autot";    
    let requestOptions = {
        method: "POST", //Lisätään auto
        headers: { "Content-Type": "application/json" },  
    	body: formData
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if(responseObj.response==0){
   			document.getElementById("ilmo").innerHTML = "Auton lisäys epäonnistui.";	
        }else if(responseObj.response==1){ 
        	document.getElementById("ilmo").innerHTML = "Auton lisäys onnistui.";
			document.lomake.reset(); //Tyhjennetään auton lisäämisen lomake		        	
		}
		setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 3000);
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

function varmistaPoisto(id, rekno){
	if(confirm("Poista auto " + decodeURI(rekno) +"?")){ //decodeURI() muutetaan enkoodatut merkit takaisin normaaliksi kirjoitukseksi, confirm on varmistusboksi
		poistaAuto(id, encodeURI(rekno));
	}
}

//Poistetaan auto kutsumalla backin DELETE-metodia ja välittämällä sille poistettavan auton id
function poistaAuto(id, rekno){
	let url = "autot?id=" + id;    
    let requestOptions = {
        method: "DELETE"             
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if(responseObj.response==0){
			alert("Auton poisto epäonnistui.");	        	
        }else if(responseObj.response==1){ 
			document.getElementById("rivi_"+id).style.backgroundColor="red"; //muuttaa poistettavan taustan punaiseksi, ei välttämättä toimi näin kaikissa selaimissa
			alert("Auton " + decodeURI(rekno) +" poisto onnistui."); //decodeURI() muutetaan enkoodatut merkit takaisin normaaliksi kirjoitukseksi
			haeAutot();       //   haetaan lista uudelleen	
		}
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}	
