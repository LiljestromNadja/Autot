<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="scripts/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Autot Listattu</title>
</head>
<body>
<table id="listaus">
	<thead>	
		<tr> 
		<th colspan="5" class="oikealle"><a id="linkki" href="lisaaauto.jsp">Lisää uusi auto</a></th>
		</tr>		
		<tr> <!-- <tr> = uusi rivi, <th> =  table head -->
			<th>Hakusana: </th>
			<th colspan="3"> <input type="text" id="hakusana"></th> <!-- colspan3 = 3 sarakkeen levyinen -->
			<th><input type="button" value="hae" id="hakunappi" onclick="haeAutot()"></th>
		</tr>
		<tr>
			<th>Rekisterinumero</th>
			<th>Merkki</th>
			<th>Malli</th>
			<th>Vuosi</th>
			<th></th>
		</tr>
	</thead>
	<tbody id="tbody">
	</tbody>
</table>
<span id="ilmo"></span>
<script>
haeAutot();
</script>
</body>
</html>