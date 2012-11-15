$(document).ready(function() {
	init();
	initSheepToolbar();
	initButtons();
	initMap();
	initSheepTable();
	initEventList();
	recentAlarmsCall();
	sheepMap.init();
});

var map = new gmap("map-canvas");

var sheepMap = new gmap("sheep-map");

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

function disableTableToolbar() {
	$('.edit-sheep-button').addClass('disabled');
	$('.delete-sheep-button').addClass('disabled');
}

function enableTableToolbar() {
	$('.edit-sheep-button').removeClass('disabled');
	$('.delete-sheep-button').removeClass('disabled');
}

$.extend($.fn.dataTableExt.oStdClasses, {
	"sSortAsc" : "header headerSortDown",
	"sSortDesc" : "header headerSortUp",
	"sSortable" : "header"
});

$.extend($.fn.dataTableExt.oStdClasses, {
	"sWrapper" : "dataTables_wrapper form-inline"
});

// Sheep Table Toolbar
function initSheepToolbar() {
	$('#add-sheep-form').ajaxForm({
		beforeSubmit: showRequest,
		success: function() {
			$.pnotify({
				text: 'Sheep was successfully added.',
				type: 'success',
			});
			$('#add-sheep-modal').modal('hide');
			$(this).clearForm();
			// TODO add sheep to table
		},
		error: function() {
			$.pnotify({
				text : 'Could not store sheep data.',
				type : 'error',
			});
		}
	});

	$('.edit-sheep-button').click(function(e) {
		jsRoutes.controllers.Sheep.show(sheepTable.selectedId).ajax({
			dataType: 'json',
			success: function(sheep) {
				var form = $('#edit-sheep-form');
				form.attr("action", jsRoutes.controllers.Sheep.update(sheep.id).url);
				form.attr("method", jsRoutes.controllers.Sheep.update(sheep.id).method);
				form.find('input[name="sheepId"]').val(sheep.sheepId);
				form.find('input[name="rfid"]').val(sheep.rfid);
				form.find('input[name="name"]').val(sheep.name);
				form.find('input[name="birthWeight"]').val(sheep.birthWeight);
				form.find('input[name="dateOfBirth"]').val(sheep.dateOfBirth);
				form.find('input[name="notes"]').val(sheep.notes);
			},
			error: function(data) {
				$.pnotify({
					text : 'Could not load sheep data.',
					type: 'error',
				});
			}
		});

	});

	$('#edit-sheep-form').ajaxForm({
		beforeSubmit: showRequest,
		success: function() {
			$.pnotify({
				text: 'Sheep was successfully updated.',
				type: 'success',
			});
		},
		error: function() {
			$.pnotify({
				text : 'Could not update sheep data',
				type: 'error',
			});
		}
	});

	$('.delete-sheep-button').on('click', function(e) {
		var id = getSelectedId();
		var sheepId = getSelectedSheepId();
		jsRoutes.controllers.Sheep.delete(id).ajax({
			dataType: 'json',
			success: function(data) {
				$.pnotify({
					text: 'Sheep was successfully deleted',
					type: 'success',
				});
				sheepTable.fnDeleteRow(getSelectedRow().get(0));
				disableTableToolbar();
				clearSheepDetails();
				eventTable.fnDeleteRows(sheepId, 'sheepId');
			},
			error : function() {
				$.pnotify({
					text : 'Could not delete sheep.',
					type : 'error'
				});
			}
		});
	});
}

function getSelectedRow() {
	var row = sheepTable.$('tr.row-selected');
	return row;
}

function getSelectedId() {
	var row = sheepTable.$('tr.row-selected');
	var id = sheepTable.fnGetData(row.get(0), 0);
	return id;
}

function getSelectedSheepId() {
	var row = sheepTable.$('tr.row-selected');
	var id = sheepTable.fnGetData(row.get(0), 1);
	return id;
}

function initSheepTable() {
	// Sheep table selection
	$("#sheep-table tbody").on('click', 'tr', function(e) {
		if ($(this).hasClass('row-selected')) {
			$(this).removeClass('row-selected');
			disableTableToolbar();
			sheepTable.selectedId = null;
		} else {
			sheepTable.selectedId = sheepTable.fnGetData(this, 0);
			sheepTable.$('tr.row-selected').removeClass('row-selected');
			$(this).addClass('row-selected');
			enableTableToolbar();

			var id = sheepTable.fnGetData(this, 0)

			var sheepId = sheepTable.fnGetData(this, 1);
			showSheep(id);
			jsRoutes.controllers.Event.listBySheep(id).ajax({
				dataType : 'json',
				success : function(data) {
					sheepMap.clearMarkers();
					eventTable.fnFilter(sheepId, 1);
					if (data.data.length) {
						$('#sheep-page .alert').hide();
						$.each(data.data, function(k, event) {
							sheepMap.addMarker(
									event.latitude,
									event.longitude,
									event.messageType,
									event.id, false);
						});
						sheepMap.fitBounds();
					} else {
						$.pnotify({
							text: "We could not find any events registered on this entity.",
						});
					}
				},
				error: function() {
					$.pnotify({
						text : 'We could not load the events',
						type : 'error',
					});
				}
			});
		}
	});

	sheepTable = $("#sheep-table").dataTable({
		"bProcessing" : true,
		"bStateSave" : true,
		"sAjaxDataProp" : "data",
		"sPaginationType" : "bootstrap",
		"sAjaxSource" : jsRoutes.controllers.Sheep.list().absoluteURL(),
		// "sDom": "<'row'<'span3'l><'#sheep-table-toolbar-tmp
		// span3'><'span6'f>r>t<'row'<'span6'i><'span6'p>>",
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
	}).fnSetFilteringDelay();

	$('#sheep-table_length').before($('#sheep-table-toolbar'));

}

function initEventList() {
	$("#event-list tbody").on('click', 'tr', function(e) {
		if ($(this).hasClass('row-selected')) {
			$(this).removeClass('row-selected');
		} else {
			eventTable.$('tr.row-selected').removeClass('row-selected');
			$(this).addClass('row-selected');
			var id = eventTable.fnGetData(this, 0)
			showSheep(id);
			jsRoutes.controllers.Event.show(id).ajax({
				dataType : 'json',
				success : function(data) {
					showEventDetails(data);
					sheepMap.addMarker(data.latitude,
							data.longitude,
							data.messageType,
							data.id, true);
				}
			});
		}
	});

	eventTable = $("#event-list").dataTable({
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
	}).fnSetFilteringDelay();
}

function showRequest(formData, jqForm, options) {
	console.log($(this));
	console.log("Request: " + $.param(formData));
	return true;
}

function showResponse(responseText, statusText, xhr, $form) {
	console.log("Response: status: " + statusText + "\nResponseText:\n"
			+ responseText);
}

function initButtons() {

	$('.contact-form').ajaxForm({
		beforeSubmit: showRequest,
		success: function() {
			$.pnotify({
				text : 'Contact was successfully updated',
				type : 'success'
			});
			// TODO add id and set up delete/edit buttons
		},
		error: function() {
			$.pnotify({
				text : 'Could not store contact data.',
				type : 'error'
			});
		}
	});

	$('.contact-form-delete').on('click', function(e) {
		var id = $(this).attr("data-contact-id");
		jsRoutes.controllers.Contact.delete(id).ajax({
			success: function(data) {
				$('#contact-form-'+id).hide("fast", function() { $(this).remove() });
				$.pnotify({
					text: "Contact was successfully deleted.",
					type : "success"
				});
			},
			error : function() {
				$.pnotify({
					text : 'Could not delete contact.',
					type : 'error'
				});
			}
		});
	});

	$('#new-contact-button').on('click', function(e) {
		var form = $('#new-contact-form').clone(true, true);
		$('#contact-forms').append(form);
		form.show('slow');
	});

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

	$('#details-col .collapse').collapse({
		parent : '#details-col',
		toggle : false
	});

	$.pnotify.defaults.history = false;
	$.pnotify.defaults.delay = 5000;
	checkNewEvents();
}

var newEventInterval = window.setInterval('checkNewEvents()', 30000);
var lastId;

var checkNewEvents= function() {
	jsRoutes.controllers.Event.alarmList(1).ajax({
		dataType: 'json',
		success: function(data) {
			console.log(lastId);
			console.log(data);
			if (data.id != lastId) {
				$.pnotify({
					title : "New alarm!",
					text : "A new alarm has been registered and the tables have been updated.",
					type : "info",
					hide : false
				});
				lastId = data.id;
			}
		}
	});
}

var recentAlarmsInterval = window.setInterval('recentAlarmsCall()', 30000);
var recentAlarmsCall = function() {
	jsRoutes.controllers.Event.alarmList(5).ajax({
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
