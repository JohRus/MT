var map;
//var ct_marker = '/Users/Johan/Workspace/MT/BaseStations/resources/ct.png';
//var ct_calc_marker = '/Users/Johan/Workspace/MT/BaseStations/resources/ct_calc.png';


function initmap() {
	// set up the map
	//map = new L.Map('map');

	// create the tile layer with correct attribution
	//var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	//var osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
	//var osm = new L.TileLayer(osmUrl, {minZoom: 0, maxZoom: 18});		

	// start the map in South-East England
	//map.setView(new L.LatLng(51.3, 0.0),10);
	//map.addLayer(osm);

	map = L.map('map').setView([51.3, 0.0], 2);
	L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    	//attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    	maxZoom: 18,
    	minZoom: 0
	}).addTo(map);

	//, attribution: osmAttrib

	//L.control.mousePosition({
	//	position: 'bottomright'
	//}).addTo(map);
}

/*function processSearch() {
	var mcc = document.cellLocation.mcc.value;
	var mnc = document.cellLocation.mnc.value;
	var lac = document.cellLocation.lac.value;
	var cellid = document.cellLocation.cellid.value;

	

	var op = '<div> MCC='+mcc+' MNC='+mnc+' LAC='+lac+' CellID='+cellid+'</div>';

	document.getElementById('op').innerHTML = op;

}*/

function readText(filePath) {
	var reader = new FileReader();    
    
    reader.onload = function (event) {
        var content = event.target.result; // event.target is the FileReader instance
        var json = jQuery.parseJSON(content);
        //displayContents(json.cell.lon+','+json.cell.lat+'<br>');
        //displayContents(json.calculatedCell.lon+','+json.calculatedCell.lat+'<br>');
        //for(i = 0; i < json.measurements.length; i++) {
        //	displayContents(json.measurements[i].lon+','+json.measurements[i].lat+'<br>');
        //}

        var ctMarker = L.icon({
			iconUrl: '/Users/Johan/Workspace/MT/BaseStations/resources/ct_alt.png',
			iconSize: [25, 41],
			iconAnchor: [13, 41]
		});

		var ctCalcMarker = L.icon({
			iconUrl: '/Users/Johan/Workspace/MT/BaseStations/resources/ct_calc.png',
			iconSize: [25, 41],
			iconAnchor: [13, 41]
		});

        L.marker([json.cell.lat, json.cell.lon], {icon: ctMarker}).addTo(map);
        L.marker([json.calculatedCell.lat, json.calculatedCell.lon], {icon: ctCalcMarker}).addTo(map);

        for(i = 0; i < json.measurements.length; i++) {
        	var circle = L.circleMarker([json.measurements[i].lat, json.measurements[i].lon], {
        		stroke: true,
    			color: '#1A4982',
    			weight: 1,
    			opacity: 0.9,
    			fillColor: '#1A4982',
   				fillOpacity: 0.5
			}).addTo(map);

			L.polyline(
				[[json.measurements[i].lat, json.measurements[i].lon],
				[json.cell.lat, json.cell.lon]], 
				{color: '#666',
				weight: 1
			}).addTo(map);
        }

        displayContents('Error: '+json.calculatedCell.errorDist);

        map.setView([json.cell.lat, json.cell.lon], 13);
    };
    reader.onerror = function(event) {
    	console.error("File could not be read! Code " + event.target.error.code);
	};
    reader.readAsText(filePath.files[0]);

    
}

function displayContents(txt) {
        var el = document.getElementById('main').innerHTML = txt; 
         //display output in DOM
    } 

