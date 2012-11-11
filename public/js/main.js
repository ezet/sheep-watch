var locations = [ [ 'Sau 01', 62.2, 9 ], [ 'Sau 02', 62, 9.1 ],
		[ 'Sau 03', 62.3, 8.9 ], [ 'Sau 04', 62, 9 ] ];

var statKartOptions = {
	getTileUrl : function(coord, zoom) {
		return "http://opencache.statkart.no/gatekeeper/gk/gk.open_gmaps?layers=topo2&zoom="
				+ zoom + "&x=" + coord.x + "&y=" + coord.y;
	},
	tileSize : new google.maps.Size(256, 256),
	maxZoom : 17,
	minZoom : 6
};

var statKartType = new google.maps.ImageMapType(statKartOptions);

function initMap() {
	var latlng = new google.maps.LatLng(62, 9);
	var myOptions = {
		zoom : 8,
		center : latlng,
		mapTypeId : "STATKART",
		streetViewControl : false,
		mapTypeControl : false
	};
	var map = new google.maps.Map(document.getElementById("map-canvas"),
			myOptions);
	map.mapTypes.set("STATKART", statKartType);

	var infoWindow = new google.maps.InfoWindow();
	var bounds = new google.maps.LatLngBounds();
	var marker, i;

	for (i = 0; i < locations.length; i++) {
		var markerLatLng = new google.maps.LatLng(locations[i][1],
				locations[i][2])
		marker = new google.maps.Marker({
			position : markerLatLng,
			map : map
		});
		bounds.extend(markerLatLng);

		google.maps.event.addListener(marker, 'click', (function(marker, i) {
			return function() {
				infoWindow.setContent(locations[i][0]);
				infoWindow.open(map, marker);
			};
		})(marker, i));
	}
	;
	map.fitBounds(bounds);
};

$.extend($.fn.dataTableExt.oStdClasses, {
	"sSortAsc" : "header headerSortDown",
	"sSortDesc" : "header headerSortUp",
	"sSortable" : "header"
});

$.extend($.fn.dataTableExt.oStdClasses, {
	"sWrapper" : "dataTables_wrapper form-inline"
});

function initSheepList() {

	$("#sheep-list tbody tr").live('click', function(e) {
		if ($(this).hasClass('row-selected')) {
			$(this).removeClass('row-selected');
		} else {
			sheepTable.$('tr.row-selected').removeClass('row-selected');
			$(this).addClass('row-selected');
		}
	});

	var sheepTable = $("#sheep-list").dataTable({
		"bProcessing" : true,
		"sAjaxDataProp" : "data",
		"sPaginationType" : "bootstrap",
		"sAjaxSource" : jsRoutes.controllers.Sheep.index().absoluteURL(),
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
	var eventTable = $("#event-list").dataTable({
		"bProcessing" : true,
		"sAjaxDataProp" : "data",
		"sPaginationType" : "bootstrap",
		"sAjaxSource" : jsRoutes.controllers.Event.index().absoluteURL(),
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


function init() {
	$('#recent-alarms').on('click', 'a', function(e) {
		e.preventDefault();
		var id = $(this).attr("data-event-id");
		jsRoutes.controllers.Event.show(id).ajax({
			dataType : 'json',
			success: function(data) {
				var events = $('#event-details ul');
				events.find('#event-type').text(data.messageType);
				events.find('#event-id').text(data.sheepId);
				events.find('#event-rfid').text(data.rfid);
				events.find('#event-time').text(data.timeSent);
				events.find('#event-temp').text(data.temperature);
				events.find('#event-pulse').text(data.pulse);
				events.find('#event-lat').text(data.latitude);
				events.find('#event-long').text(data.longitude);
			}
		})
	});
}

var recentAlarmsInterval = window.setInterval('recentAlarmsCall()', 30000);
var recentAlarmsCall = function() {
	jsRoutes.controllers.Event.eventsByType(5).ajax(
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

$(document).ready(function() {
	initButtons();
	initMap();
	initSheepList();
	initEventList();
	recentAlarmsCall();
	init();
});

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
