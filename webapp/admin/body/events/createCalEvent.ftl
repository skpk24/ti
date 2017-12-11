<div id="page-wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Create calendar event</h1>
		</div>
	</div>
	<div class="pha-content-wrapper container-fluid well">
		<div class="row">
			<div class="col-md-2 col-lg-2"></div>
			<div class="col-xs-12 col-sm-12 col-md-8 col-lg-8">
				<form id="calEventsForm" class="form-horizontal"
					name="calEventsForm" method="POST"
					action="<@ofbizUrl>createEvent</@ofbizUrl>">
					
					<!-- hidden fields -->
					<input type="hidden"  name="scopeEnumId" value="WES_PUBLIC">
					<input type="hidden" name="workEffortTypeId" value="TASK">
					<!-- end hidden fields -->
					
							<div class="form-group">
								<label for="eventName" class="col-form-label">Select School Center</label>
									<select name="partyId" class="form-control">
										<#list schoolCenters as schoolCenter>
										<option value="${schoolCenter.partyIdTo?if_exists}">${schoolCenter.relationshipName?if_exists}</option>
										</#list>
									</select>
							</div>
							<div class="form-group">
								<label for="eventName" class="col-form-label">Event Name</label>
									<input type="text" name="workEffortName" id="workEffortName"
										class="form-control" required>
							</div>
							
							<div class="form-group">
								<label for="summary" class="col-form-label">Summary</label>
									<textarea rows="4" cols="50" name="description" id="description"
										class="form-control" required></textarea>
							</div>
							<div class="form-group">
								<label for="description" class="col-form-label">Description</label>
									<textarea name="textData" cols="120" rows="20" id="textData" class="form-control"></textarea>
							</div>
	
							<div class="form-group">
								<label for="status" class="col-form-label">Status</label>
									<select name="currentStatusId" id="status" class="form-control">
										<#list statusItems as statusItem>
										<option value="${statusItem.statusId?if_exists}">${statusItem.description?if_exists}</option>
										</#list>
									</select>
							</div>
							<div class="form-group">
								<label for="scope" class="col-form-label">Estimated
									Start Date</label>
									<input type="text" name="estimatedStartDate"
										class="form-control" id="date_timepicker_start" />
							</div>
							<div class="form-group">
								<label for="scope" class="col-form-label">Estimated
									Completion Date</label>
									<input type="text" name="estimatedCompletionDate"
										class="form-control" id="date_timepicker_end" />
							</div>
							<div class="form-group">
								<div class="col-sm-2 col-lg-3 col-md-3"></div>
								<div class="col-sm-8 col-xs-12 col-lg-6 col-md-6">
									<input type="submit" name="addButton" class="btn btn-large btn-block btn-primary" value="Add" />
								</div>
								<div class="col-sm-2 col-lg-3 col-md-3"></div>
							</div>	
				</form>
			</div>
			<div class="col-md-2 col-lg-2"></div>
		</div>
		
	</div>
</div>

<script src="/images/jquery/jquery-1.11.0.min.js" type="text/javascript"></script>
<script src="/images/jquery/jquery-migrate-1.2.1.js"
	type="text/javascript"></script>



<script language="javascript"
	src="/images/jquery/plugins/elrte-1.3/js/elrte.full.js"
	type="text/javascript"></script>
<!-- link href="/images/jquery/plugins/elrte-1.3/css/elrte.min.css" rel="stylesheet" type="text/css"-->
<link href="${request.getContextPath()}/static/css/elrte.full.css"
	rel="stylesheet" type="text/css">

<script language="javascript" type="text/javascript">
	var opts = {
		cssClass : 'el-rte',
		lang : 'en',
		toolbar : 'maxi',
		doctype : '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">', //'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">',
		cssfiles : [ '${request.getContextPath()}/static/css/elrte-inner.css' ]
	}
	jQuery('#textData').elrte(opts);
</script>

<script type="text/javascript">
	jQuery(function() {
		jQuery('#date_timepicker_start').datetimepicker(
				{
					format : 'Y-m-d H:i:s',
					value:'${estimatedStartDate?if_exists}',
					timepicker : true
				});
		jQuery('#date_timepicker_end').datetimepicker(
				{
					format : 'Y-m-d H:i:s',
					value:'${estimatedCompletionDate?if_exists}',
					onShow : function(ct) {
						this
								.setOptions({
									minDate : jQuery('#date_timepicker_start')
											.val() ? jQuery(
											'#date_timepicker_start').val()
											: false
								})
					},
					timepicker : true
				});
	});
</script>

