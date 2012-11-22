// Initialize application
$(document).ready(function() {
	init();
	initSheepToolbar();
	initButtons();
	initMap();
	initSheepTable();
	initEventTable();
//	initSheepTools();
});

// General initialization
function init() {
	$('#recent-alarms-list').on('click', 'a', function(e) {
		e.preventDefault();
		var id = $(this).attr('data-event-id');
		var sheepId = $(this).attr('data-sheep-id');
		fetchAndDisplayEvent(id);
		fetchAndDisplaySheep(sheepId);
	});
	$.pnotify.defaults.delay = 5000;
	checkNewEvents();
	initRecentAlarms();
}

var mainMap = new gmap("map-canvas");
//var sheepToolbar = new sheepToolbar();

function initSheepTools() {
	if ($("#sheep-table").length) {
		sheepToolbar.init();
	}
}

// Initializes main map
function initMap() {
	if ($('#map-canvas').length) {
		mainMap.init();
		showCurrentPos();
	}
	$('#show-current-pos-button').on('click', function() {
		showCurrentPos();
	});
}

// Fetches and display recent alarms on the map
function showRecentAlarms(limit) {
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



// Fetches and displays current position of all sheep on the map
function showCurrentPos() {
	jsRoutes.controllers.Sheep.positions().ajax({
		dataType: 'json',
		success : function(data) {
			mainMap.clearMarkers();
			$.each(data, function(key, event){
				mainMap.addEventMarker(event, false);
				mainMap.fitBounds();
			});
		}
		
	});
}

// 
function disableTableToolbar() {
	$('.edit-sheep-button').attr('disabled', '');
	$('.delete-sheep-button').attr('disabled', '');
}

function enableTableToolbar() {
	$('.edit-sheep-button').removeAttr('disabled');
	$('.delete-sheep-button').removeAttr('disabled');
}

// Initializes the Sheep Table Toolbar
function initSheepToolbar() {
	$('#add-sheep-form').ajaxForm({
		dataType: 'json',
		success: function(data) {
			sheepTable.fnAddData(data);
			$.pnotify({
				text: 'Sheep was successfully added.',
				type: 'success',
			});
			$('#add-sheep-modal').modal('hide');
			$('#add-sheep-form').clearForm();
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
				form.find('input[name="sheepPid"]').val(sheep.sheepPid);
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
		var sheepPid = getSelectedSheepId();
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
				eventTable.fnDeleteRows(sheepPid, 'sheepPid');
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

// Gets the currently selected row in sheep table
function getSelectedRow() {
	var row = sheepTable.$('tr.row-selected');
	return row;
}

// Gets the currently selected ID in sheep table
function getSelectedId() {
	var row = sheepTable.$('tr.row-selected');
	var id = sheepTable.fnGetData(row.get(0), 0);
	return id;
}

// Gets the currently selected sheep PID in the sheep table
function getSelectedSheepId() {
	var row = sheepTable.$('tr.row-selected');
	var id = sheepTable.fnGetData(row.get(0), 1);
	return id;
}

// Initializes the sheep table
function initSheepTable() {
	
	// Sheep table selection behaviour
	$("#sheep-table tbody").on('click', 'tr', function(e) {
		if ($(this).hasClass('row-selected')) {
			$(this).removeClass('row-selected');
			disableTableToolbar();
			sheepTable.selectedId = null;
			eventTable.fnFilter("", 1);
		} else {
			sheepTable.selectedId = sheepTable.fnGetData(this, 0);
			sheepTable.$('tr.row-selected').removeClass('row-selected');
			$(this).addClass('row-selected');
			enableTableToolbar();

			var id = sheepTable.fnGetData(this, 0)

			var sheepPid = sheepTable.fnGetData(this, 1);
			fetchAndDisplaySheep(id);
			jsRoutes.controllers.Event.listBySheep(id).ajax({
				dataType : 'json',
				success : function(data) {
					mainMap.clearMarkers();
					eventTable.fnFilter("^\\s*"+ sheepPid +"\\s*$", 1, true, false);
					if (data.data.length) {
						$('#sheep-page .alert').hide();
						$.each(data.data, function(k, event) {
							if (k > 20) {
								return false;
							}
							var marker = mainMap.addEventMarker(event);
						});
						mainMap.fitBounds();
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
	
	// Initializes the table itself
	sheepTable = $("#sheep-table").dataTable({
		"sDom": 'lfrtip',
		"bJQueryUI": true,
		"bProcessing" : true,
		"sPaginationType": "full_numbers",
		"sAjaxDataProp" : "data",
		"sAjaxSource" : jsRoutes.controllers.Sheep.list().absoluteURL(),
		"aoColumns" : [ {
			"mData" : "id",
			"bVisible" : false,
			"bSearchable" : false,
		}, {
			"mData" : "sheepPid",
			"bSmart" : false,
			"bRegex" : true,
			"sSearch" : "^\\s*"+'1'+"\\s*$"
		}, {
			"mData" : "rfid",
			"bSmart" : false,
			"bRegex" : true,
			"sSearch" : "^\\s*"+'1'+"\\s*$"
		}, {
			"mData" : "name",
			"bSmart" : false,
			"bRegex" : true,
			"sSearch" : "^\\s*"+'1'+"\\s*$"
		}, {
			"mData" : "birthWeight",
			"bSearchable" : false
		}, {
			"mData" : "dateOfBirth",
			"bSearchable" : false
		} ]
	}).fnSetFilteringDelay();

}


// Initializes the event table
function initEventTable() {
	// Initializes interaction
	$("#event-list tbody").on('click', 'tr', function(e) {
		if ($(this).hasClass('row-selected')) {
			$(this).removeClass('row-selected');
		} else {
			eventTable.$('tr.row-selected').removeClass('row-selected');
			$(this).addClass('row-selected');
			var id = eventTable.fnGetData(this, 0)
			var sheepId = eventTable.fnGetData(this).sheepId;
			fetchAndDisplaySheep(sheepId);
			jsRoutes.controllers.Event.show(id).ajax({
				dataType : 'json',
				success : function(data) {
					displayEvent(data);
				}
			});
		}
	});

	// Initializes the table itself
	eventTable = $("#event-list").dataTable({
		"sDom": 'lfrtip',
		"bJQueryUI": true,
		"bProcessing" : true,
		"sPaginationType": "full_numbers",
		"sAjaxDataProp" : "data",
		"sAjaxSource" : jsRoutes.controllers.Event.list().absoluteURL(),
		"aoColumns" : [ {
			"mData" : "id",
			"bVisible" : false,
			"bSearchable" : false
		}, {
			"mData" : "sheepPid",
			"bSmart" : false,
			"bRegex" : true,
			"sSearch" : "^\\s*"+'1'+"\\s*$"
		}, {
			"mData" : "messageType",
			"bSmart" : false,
			"bRegex" : true,
			"sSearch" : "^\\s*"+'2'+"\\s*$"
		}, {
			"mData" : "timeSent",
			"bSearchable" : false
		}, {
			"mData" : "pulse",
			"bSearchable" : false
		}, {
			"mData" : "temperature",
			"bSearchable" : false
		} ]
	}).fnSetFilteringDelay();
}

// Initializes various user interaction
function initButtons() {
	// Set up adding and editing contacts
	$('.contact-form').ajaxForm({
		beforeSubmit: function(formData, jqForm) {
			this.success = function(data) {
				$.pnotify({
					text : 'Contact was successfully updated',
					type : 'success'
				});
				if (jqForm.attr('id') == 'new-contact-form') {
					jqForm.find('.contact-form-delete').removeAttr("disabled");

					jqForm.parents('.widget-box').find('h5').text(data.name);
				}
			};
		},
		error: function() {
			$.pnotify({
				text : 'Could not store contact data.',
				type : 'error'
			});
		}
	});
	// Set up deleting contacts 
	$('.contact-form-delete').on('click', function(e) {
		var id = $(this).attr("data-contact-id");
		jsRoutes.controllers.Contact.delete(id).ajax({
			success: function(data) {
				$('#contact-form-'+id).closest($(".widget-box")).hide("slow", function() { $(this).remove() });
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

	// Set up new contact forms
	var counter = 0;
	$('#new-contact-button').on('click', function(e) {
		var form = $('#new-contact-widget').clone(true, true).removeAttr('id');
		form.find('.collapse').attr('id', 'new-contact-collapse-' + counter);
		form.find('.widget-title a').attr('href', '#new-contact-collapse-' + counter);
		counter++;
		$('#contact-forms').prepend(form);
		form.show('slow');
	});

}

// Fetches and display info on a sheep
function fetchAndDisplaySheep(id) {
	jsRoutes.controllers.Sheep.show(id).ajax({
		dataType : 'json',
		success : displaySheep
	});
}

// Display details on a sheep
function displaySheep(data) {
	var sheep = $('#sheep-details');
	sheep.find('#sheep-id').text(data.sheepPid);
	sheep.find('#delete-sheep').attr("data-sheep-id", data.id);
	sheep.find('#edit-sheep').attr("data-sheep-id", data.id);
	sheep.find('#sheep-rfid').text(data.rfid);
	sheep.find('#sheep-name').text(data.name);
	sheep.find('#sheep-weight').text(data.birthWeight);
	sheep.find('#sheep-birth').text(data.dateOfBirth);
	sheep.find('#sheep-notes').text(data.notes);
	sheep.find('#sheep-attacked').text(data.attacked);
}


// Clears all sheep details
function clearSheepDetails() {
	var sheep = $('#sheep-details');
	sheep.find('#sheep-id').empty();
	sheep.find('#sheep-rfid').empty();
	sheep.find('#sheep-name').empty();
	sheep.find('#sheep-weight').empty();
	sheep.find('#sheep-birth').empty();
	sheep.find('#sheep-notes').empty();
	sheep.find('#sheep-attacked').empty();
}


// Fetch and display details for an event
function fetchAndDisplayEvent(id) {
	jsRoutes.controllers.Event.show(id).ajax({
		dataType : 'json',
		success : displayEvent
	});
}

// Display details about an event
function displayEvent(data) {
	var events = $('#event-details');
	events.find('.event-type').text(data.messageType);
	events.find('.event-id').text(data.sheepPid);
	events.find('.event-rfid').text(data.rfid);
	events.find('.event-time').text(data.timeSent);
	events.find('.event-temp').text(data.temperature);
	events.find('.event-pulse').text(data.pulse);
	events.find('.event-lat').text(data.latitude);
	events.find('.event-long').text(data.longitude);
	mainMap.clearMarkers();
	mainMap.addEventMarker(data, true);
}


// Set up background checks for new events
var newEventInterval = window.setInterval('checkNewEvents()', 15000);
var lastId;
var checkNewEvents = function() {
	jsRoutes.controllers.Event.listLimit(10).ajax({
		dataType: 'json',
		success: function(json) {
			var data = json.data;
			if (data.length) {
				$.each(data, function(key, event) {
					if (!lastId || event.id == lastId) {
						return false;
					}
					if (event.messageType == 'ALARM') {
						$.pnotify({
							title : "New alarm!",
							text : "A new alarm has been registered and the tables have been updated.",
							hide : false
						});
						$('#recent-alarms-list')
						insertRecentAlarmItem(event);
					}
					eventTable.fnAddData(event);
				});
				lastId = data[0].id;
			} else {
				lastId = -1;
			}
		}
	});
}

// Inserts a new recent event in recent events list, and removes old ones if needed
function insertRecentAlarmItem(event) {
	var list = $('#recent-alarms-list');
	var li = '<li><a href="#" data-event-id=' + event.id
	+ ' data-sheep-id="'+event.sheepId+'">ID ' + event.sheepPid + ' (' + event.timeSent
	+ ')</a></li>';
	list.prepend(li);
	if (list.children('li').length > 5) {
		list.children('li').last().remove();
	}
	
}

// Sets up initial list of recent alarms
function initRecentAlarms() {
	jsRoutes.controllers.Event.alarmList(5).ajax({
		dataType: 'json',
		success: function(data) {
			$.each(data, function(key, event) {
				insertRecentAlarmItem(event);
			});
		}
	});
}
