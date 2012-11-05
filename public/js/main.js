var locations = [
	['Sau 01', 62.2, 9],
	['Sau 02', 62, 9.1],
	['Sau 03', 62.3, 8.9],
	['Sau 04', 62, 9]
];
var statKartOptions = {
	getTileUrl: function(coord, zoom) {
		return "http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=topo2&zoom="
			+ zoom + "&x=" + coord.x + "&y=" + coord.y;
	},
	tileSize: new google.maps.Size(256, 256),
	maxZoom: 17,
	minZoom: 6
};

var statKartType = new google.maps.ImageMapType(statKartOptions);

function initMap() {		
	var latlng = new google.maps.LatLng(62, 9);
	var myOptions = {
		zoom: 8,
		center: latlng,
		mapTypeId: "STATKART",
		streetViewControl: false,
		mapTypeControl: false
	};
	var map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
	map.mapTypes.set("STATKART", statKartType);
	
	var infoWindow = new google.maps.InfoWindow();		
	var bounds = new google.maps.LatLngBounds();		
	var marker, i;
	
	for (i=0; i < locations.length; i++) {
		var markerLatLng = new google.maps.LatLng(locations[i][1], locations[i][2])
		marker = new google.maps.Marker({
			position: markerLatLng,
			map: map
		});
		bounds.extend(markerLatLng);
		
		google.maps.event.addListener(marker, 'click', (function(marker, i) {
			return function() {
				infoWindow.setContent(locations[i][0]);
				infoWindow.open(map, marker);
			};
		})(marker, i));
	};
	map.fitBounds(bounds);
};

function initSheepList() {
	$("#sheep-list").dataTable({
		"bProcessing": true,
		"sAjaxSource": jsRoutes.controllers.Sheep.index()
	});
};

function initEventList() {
	$("#event-list").dataTable();
};

$(document).ready(function() {
	initMap();
	initSheepList();
	initEventList();
});

