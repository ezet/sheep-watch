// Avoid `console` errors in browsers that lack a console.
if (!(window.console && console.log)) {
	(function() {
		var noop = function() {
		};
		var methods = [ 'assert', 'clear', 'count', 'debug', 'dir', 'dirxml',
				'error', 'exception', 'group', 'groupCollapsed', 'groupEnd',
				'info', 'log', 'markTimeline', 'profile', 'profileEnd',
				'markTimeline', 'table', 'time', 'timeEnd', 'timeStamp',
				'trace', 'warn' ];
		var length = methods.length;
		var console = window.console = {};
		while (length--) {
			console[methods[length]] = noop;
		}
	}());
}

// Place any jQuery/helper plugins in here.

// Processing Indicator
jQuery.fn.dataTableExt.oApi.fnProcessingIndicator = function(oSettings, onoff) {
	if (typeof (onoff) == 'undefined') {
		onoff = true;
	}
	this.oApi._fnProcessingDisplay(oSettings, onoff);
}

// Filtering Delay
jQuery.fn.dataTableExt.oApi.fnSetFilteringDelay = function(oSettings, iDelay) {
	var _that = this;

	if (iDelay === undefined) {
		iDelay = 250;
	}

	this
			.each(function(i) {
				$.fn.dataTableExt.iApiIndex = i;
				var $this = this, oTimerId = null, sPreviousSearch = null, anControl = $(
						'input', _that.fnSettings().aanFeatures.f);

				anControl.unbind('keyup').bind(
						'keyup',
						function() {
							var $$this = $this;

							if (sPreviousSearch === null
									|| sPreviousSearch != anControl.val()) {
								window.clearTimeout(oTimerId);
								sPreviousSearch = anControl.val();
								oTimerId = window.setTimeout(function() {
									$.fn.dataTableExt.iApiIndex = i;
									_that.fnFilter(anControl.val());
								}, iDelay);
							}
						});

				return this;
			});
	return this;
}

$.fn.dataTableExt.oApi.fnDeleteRows = function(oSettings, sSearch, iColumn) {
	for (var i = 0; i < oSettings.aoData.length; i++) {
		aData = oSettings.aoData[i]._aData;
		if (typeof iColumn == 'undefined') {
			for (var j = 0; j < aData.length; j++) {
				if (aData[j] == sSearch) {
					this.fnDeleteRow(i);
					i--;
				}
			}
		} else if (aData[iColumn] == sSearch) {
			this.fnDeleteRow(i);
			i--;
		}
	}
}