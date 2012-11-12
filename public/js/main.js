$(document).ready(function() {
	init();
	initButtons();
	initMap();
	initSheepList();
	initEventList();
	recentAlarmsCall();
	sheepMap.init();
	console.log(eventMap);
	console.log("test");
	eventMap.init();
});

var locations = [ [ 'Sau 01', 62.2, 9 ], [ 'Sau 02', 62, 9.1 ],
		[ 'Sau 03', 62.3, 8.9 ], [ 'Sau 04', 62, 9 ] ];

var map = new gmap("map-canvas");

var sheepMap = new gmap("sheep-map");

var eventMap = new gmap("event-map");

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
	};

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
		};

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
		;
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

function showAllAlarms() {
	jsRoutes.controllers.Event.alarmList(5).ajax({
		dataType : 'json',
		success : function(data) {
			if (data.length) {
				map.clearMarkers();
				$.each(data, function(k, v) {
					map.addMarker(v.latitude, v.longitude, "ALARM", v.id)
				});
				map.fitBounds();
			}
		}
	});
}

function initMap() {
	if ($('#map-canvas').length) {
		map.init();
		showAllAlarms();
	}
}

$.extend($.fn.dataTableExt.oStdClasses, {
	"sSortAsc" : "header headerSortDown",
	"sSortDesc" : "header headerSortUp",
	"sSortable" : "header"
});

$.extend($.fn.dataTableExt.oStdClasses, {
	"sWrapper" : "dataTables_wrapper form-inline"
});

function initSheepList() {
	$("#sheep-list tbody")
			.on(
					'click',
					'tr',
					function(e) {
						if ($(this).hasClass('row-selected')) {
							$(this).removeClass('row-selected');
						} else {
							sheepTable.$('tr.row-selected').removeClass(
									'row-selected');
							$(this).addClass('row-selected');
							var id = sheepTable.fnGetData(this, 0)
							showSheep(id);
							jsRoutes.controllers.Event.listBySheep(id).ajax(
									{
										dataType : 'json',
										success : function(data) {
											sheepMap.clearMarkers();
											if (data.data.length) {
												$('#sheep-page .alert').hide();
												$.each(data.data, function(k,
														event) {
													sheepMap.addMarker(
															event.latitude,
															event.longitude,
															event.messageType,
															event.id, false);
												});
												sheepMap.fitBounds();
											} else {
												$('#sheep-page .alert').show();
											}
										}
									});
						}
					});

	var sheepTable = $("#sheep-list").dataTable({
		"bProcessing" : true,
		"sAjaxDataProp" : "data",
		"sPaginationType" : "bootstrap",
		"sAjaxSource" : jsRoutes.controllers.Sheep.list().absoluteURL(),
		// "sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
		"aoColumns" : [ {
			"mData" : "id",
			"bVisible" : false,
		}, {
			"mData" : "sheepId"
		}, {
			"mData" : "rfid"
		}, {
			"mData" : "name"
		}, {
			"mData" : "birthWeight"
		}, {
			"mData" : "dateOfBirth"
		} ]
	});

};

function initEventList() {
	$("#event-list tbody")
			.on(
					'click',
					'tr',
					function(e) {
						if ($(this).hasClass('row-selected')) {
							$(this).removeClass('row-selected');
						} else {
							eventTable.$('tr.row-selected').removeClass(
									'row-selected');
							$(this).addClass('row-selected');
							var id = eventTable.fnGetData(this, 0)
							showSheep(id);
							jsRoutes.controllers.Event.show(id).ajax(
									{
										dataType : 'json',
										success : function(data) {
											showEventDetails(data);
											eventMap.addMarker(data.latitude,
													data.longitude,
													data.messageType,
													data.id, true);
										}
									});
						}
					});

	var eventTable = $("#event-list").dataTable({
		"bProcessing" : true,
		"sAjaxDataProp" : "data",
		"sPaginationType" : "bootstrap",
		"sAjaxSource" : jsRoutes.controllers.Event.list().absoluteURL(),
		// "sDom": "<'row'<'span6'l><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
		"aoColumns" : [ {
			"mData" : "id",
			"bVisible" : false,
		}, {
			"mData" : "sheepId"
		}, {
			"mData" : "messageType"
		}, {
			"mData" : "timeSent"
		}, {
			"mData" : "pulse"
		}, {
			"mData" : "temperature"
		} ]
	});
};

function showRequest(formData, jqForm, options) {
	console.log("Request: " + $.param(formData));
	return true;
}

function showResponse(responseText, statusText, xhr, $form) {
	console.log("Response: status: " + statusText + "\nResponseText:\n"
			+ responseText);
}

function initButtons() {
	var addSheepOptions = {
		beforeSubmit : showRequest,
		success : showResponse,
	};
	$('#add-sheep-form').ajaxForm(addSheepOptions);
}

function showSheep(id) {
	jsRoutes.controllers.Sheep.show(id).ajax({
		dataType : 'json',
		success : showSheepDetails
	});
}

function showSheepDetails(data) {
	var sheep = $('#sheep-details');
	sheep.find('#sheep-id').text(data.sheepId);
	sheep.find('#delete-sheep').attr("data-sheep-id", data.id);
	sheep.find('#edit-sheep').attr("data-sheep-id", data.id);
	sheep.find('#sheep-rfid').text(data.rfid);
	sheep.find('#sheep-name').text(data.name);
	sheep.find('#sheep-weight').text(data.birthWeight);
	sheep.find('#sheep-birth').text(data.dateOfBirth);
	sheep.find('#sheep-notes').text(data.notes);
	sheep.find('#sheep-attacked').text(data.attacked);
	sheep.find('.disabled').removeClass('disabled');
}

function clearSheepDetails() {
	var sheep = $('#sheep-details');
	sheep.find('#sheep-id').empty();
	sheep.find('#sheep-rfid').empty();
	sheep.find('#sheep-name').empty();
	sheep.find('#sheep-weight').empty();
	sheep.find('#sheep-birth').empty();
	sheep.find('#sheep-notes').empty();
	sheep.find('#sheep-attacked').empty();
	sheep.find('.btn').addClass('disabled');
}

function showEvent(id) {
	jsRoutes.controllers.Event.show(id).ajax({
		dataType : 'json',
		success : showEventDetails
	});
}

function showEventDetails(data) {
	var events = $('#event-details');
	events.find('#sheep-link').attr('data-sheep-id', data.sheepId);
	events.find('#event-type').text(data.messageType);
	events.find('#event-id').text(data.sheepId);
	events.find('#event-rfid').text(data.rfid);
	events.find('#event-time').text(data.timeSent);
	events.find('#event-temp').text(data.temperature);
	events.find('#event-pulse').text(data.pulse);
	events.find('#event-lat').text(data.latitude);
	events.find('#event-long').text(data.longitude);
}

function init() {
	$('#recent-alarms').on('click', 'a', function(e) {
		e.preventDefault();
		var id = $(this).attr('data-event-id');
		showEvent(id);
	});

	$('#event-details #sheep-link').on('click', function(e) {
		e.preventDefault();
		var id = $(this).attr('data-sheep-id');
		showSheep(id);
	});

	$('#details-col .collapse').collapse({
		parent : '#details-col',
		toggle : false
	});
	
	$('#delete-sheep').on('click', function(e) {
		var id = $(this).attr('data-sheep-id');
		console.log(id);
		jsRoutes.controllers.Sheep.delete(id).ajax({
			dataType: 'json',
			success: function(data) {
				console.log(data);
				clearSheepDetails();
			}
		});
	});
}

var recentAlarmsInterval = window.setInterval('recentAlarmsCall()', 30000);
var recentAlarmsCall = function() {
	jsRoutes.controllers.Event.alarmList(5).ajax(
			{
				dataType : 'json',
				success : function(data) {
					var html = "<ul>";
					$.each(data, function(k, v) {
						html = html + '<li><a href="#" data-event-id=' + v.id
								+ '>ID ' + v.sheepId + ' (' + v.timeSent
								+ ')</a></li>';
					});
					var recentAlarms = $('#recent-alarms .accordion-inner')
							.html(html);
				}
			});
}

/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
	return {
		"iStart" : oSettings._iDisplayStart,
		"iEnd" : oSettings.fnDisplayEnd(),
		"iLength" : oSettings._iDisplayLength,
		"iTotal" : oSettings.fnRecordsTotal(),
		"iFilteredTotal" : oSettings.fnRecordsDisplay(),
		"iPage" : Math.ceil(oSettings._iDisplayStart
				/ oSettings._iDisplayLength),
		"iTotalPages" : Math.ceil(oSettings.fnRecordsDisplay()
				/ oSettings._iDisplayLength)
	};
}

/* Bootstrap style pagination control */
$
		.extend(
				$.fn.dataTableExt.oPagination,
				{
					"bootstrap" : {
						"fnInit" : function(oSettings, nPaging, fnDraw) {
							var oLang = oSettings.oLanguage.oPaginate;
							var fnClickHandler = function(e) {
								e.preventDefault();
								if (oSettings.oApi._fnPageChange(oSettings,
										e.data.action)) {
									fnDraw(oSettings);
								}
							};

							$(nPaging)
									.addClass('pagination')
									.append(
											'<ul>'
													+ '<li class="prev disabled"><a href="#">&larr; '
													+ oLang.sPrevious
													+ '</a></li>'
													+ '<li class="next disabled"><a href="#">'
													+ oLang.sNext
													+ ' &rarr; </a></li>'
													+ '</ul>');
							var els = $('a', nPaging);
							$(els[0]).bind('click.DT', {
								action : "previous"
							}, fnClickHandler);
							$(els[1]).bind('click.DT', {
								action : "next"
							}, fnClickHandler);
						},

						"fnUpdate" : function(oSettings, fnDraw) {
							var iListLength = 5;
							var oPaging = oSettings.oInstance.fnPagingInfo();
							var an = oSettings.aanFeatures.p;
							var i, j, sClass, iStart, iEnd, iHalf = Math
									.floor(iListLength / 2);

							if (oPaging.iTotalPages < iListLength) {
								iStart = 1;
								iEnd = oPaging.iTotalPages;
							} else if (oPaging.iPage <= iHalf) {
								iStart = 1;
								iEnd = iListLength;
							} else if (oPaging.iPage >= (oPaging.iTotalPages - iHalf)) {
								iStart = oPaging.iTotalPages - iListLength + 1;
								iEnd = oPaging.iTotalPages;
							} else {
								iStart = oPaging.iPage - iHalf + 1;
								iEnd = iStart + iListLength - 1;
							}

							for (i = 0, iLen = an.length; i < iLen; i++) {
								// Remove the middle elements
								$('li:gt(0)', an[i]).filter(':not(:last)')
										.remove();

								// Add the new list items and their event
								// handlers
								for (j = iStart; j <= iEnd; j++) {
									sClass = (j == oPaging.iPage + 1) ? 'class="active"'
											: '';
									$(
											'<li ' + sClass + '><a href="#">'
													+ j + '</a></li>')
											.insertBefore(
													$('li:last', an[i])[0])
											.bind(
													'click',
													function(e) {
														e.preventDefault();
														oSettings._iDisplayStart = (parseInt(
																$('a', this)
																		.text(),
																10) - 1)
																* oPaging.iLength;
														fnDraw(oSettings);
													});
								}

								// Add / remove disabled classes from the static
								// elements
								if (oPaging.iPage === 0) {
									$('li:first', an[i]).addClass('disabled');
								} else {
									$('li:first', an[i])
											.removeClass('disabled');
								}

								if (oPaging.iPage === oPaging.iTotalPages - 1
										|| oPaging.iTotalPages === 0) {
									$('li:last', an[i]).addClass('disabled');
								} else {
									$('li:last', an[i]).removeClass('disabled');
								}
							}
						}
					}
				});
