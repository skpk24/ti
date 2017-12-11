<div id="page-wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Events</h1>
		</div>
	</div>
	 <div class="row">
	        	<div class="col-xs-3 form-group card-padding">
	        		<label>Select Center : </label>
	        	</div>
			    <div class="col-xs-3 form-group card-padding">
			    <form action="<@ofbizUrl>events</@ofbizUrl>" method="post">
			      <select id="schoolCenter" class="form-control" name="partyId">
			        <option value="">All</option>
			       	<#list schoolCenters as schoolCenter>
						<option value="${schoolCenter.partyIdTo?if_exists}" <#if
								schoolCenter.partyIdTo == "${partyId!}">
							selected="selected"</#if>>${schoolCenter.relationshipName?if_exists}</option>
					</#list>
					
			      </select>
			      </form>
			    </div>
	    </div>
	${screens.render("component://calendar/widget/CalendarScreens.xml#Calendar")}
	</div>