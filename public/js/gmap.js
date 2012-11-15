var locations = [ [ 'Sau 01', 62.2, 9 ], [ 'Sau 02', 62, 9.1 ],
                  [ 'Sau 03', 62.3, 8.9 ], [ 'Sau 04', 62, 9 ] ];



function gmap(selector) {
	this.selector = selector
	this.statKartOptions = {
			getTileUrl : function(coord, zoom) {
				return "http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=topo2&zoom="
				+ zoom + "&x=" + coord.x + "&y=" + coord.y;
			},
			tileSize : new google.maps.Size(256, 256),
			maxZoom : 17,
			minZoom : 6
	}

	this.statKartType = new google.maps.ImageMapType(this.statKartOptions);

	this.eventMarkers = [];
	this.sheepMarkers = [];
	this.alarmMarkers = [];
	this.markers = [];

	this.init = function() {
		var div = $('#' + this.selector);
		console.log(div);
		if (!div.length) {
			return false;
		}
		var latlng = new google.maps.LatLng(62, 9);
		var myOptions = {
				zoom : 8,
				center : latlng,
				mapTypeId : "STATKART",
				streetViewControl : false,
				mapTypeControl : false
		}

		this.map = new google.maps.Map(document.getElementById(this.selector),
				myOptions);
		this.map.mapTypes.set("STATKART", this.statKartType);

		var infoWindow = new google.maps.InfoWindow();
		var bounds = new google.maps.LatLngBounds();
		var marker, i;

		for (i = 0; i < locations.length; i++) {
			var markerLatLng = new google.maps.LatLng(locations[i][1],
					locations[i][2])
			marker = new google.maps.Marker({
				position : markerLatLng,
				map : this.map
			});
			bounds.extend(markerLatLng);

			this.markers.push(marker);
			google.maps.event.addListener(marker, 'click',
					(function(marker, i) {
						return function() {
							infoWindow.setContent(locations[i][0]);
							infoWindow.open(this.map, marker);
						};
					})(marker, i));
		}
		this.map.fitBounds(bounds);
	}

	this.fitBounds = function() {
		var bounds = new google.maps.LatLngBounds();
		$.each(this.markers, function(k, v) {
			bounds.extend(v.getPosition());
		});
		this.map.fitBounds(bounds);
	}

	this.clearMarkers = function() {
		$.each(this.markers, function(k, v) {
			v.setMap(null);
		});
		this.markers.length = 0;
	}

	this.addMarker = function(lat, lng, type, id, center) {
		type.toUpperCase();
		var pos = new google.maps.LatLng(lat, lng);
		var marker = new google.maps.Marker({
			position : pos,
			map : this.map
		});

		google.maps.event.addListener(marker, 'hover', function() {
		});
		var icon = jsRoutes.controllers.Assets.at("img/alert.png");
		this.markers.push(marker);
		if (type === "ALARM") {
			this.alarmMarkers[id] = marker;
			marker.setIcon(icon);
		} else if (type === "UPDATE") {
			this.eventMarkers[id] = marker;
		} else if (type === "SHEEP") {
			this.sheepMarkers[id] = marker;
		}
		if (center == true) {
			this.map.setCenter(pos);
			this.map.setZoom(12);
		}
	}
}