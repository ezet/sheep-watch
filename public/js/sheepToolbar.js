function sheepToolbar() {
	
	var disable = function() {
		$('.edit-sheep-button').attr('disabled', '');
		$('.delete-sheep-button').attr('disabled', '');
	}

	var enable = function() {
		$('.edit-sheep-button').removeAttr('disabled');
		$('.delete-sheep-button').removeAttr('disabled');
	}

	var init = function() {
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

};