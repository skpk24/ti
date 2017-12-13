<#assign schoolCenterList = request.getAttribute("schoolCenterList")!>
<#assign createdSchoolCenterList = request.getAttribute("createdSchoolCenterList")!>
<#assign months = request.getAttribute("months")!>
<#assign currentYear = .now?string('yyyy')>
<#assign nextYear = currentYear?number + 1>

<style>
	.input-group-addon {
		min-width: 150px;
		font-weight: bold;
	}
	
	.btn-info {
	    margin:0 auto;
	    display:block;
	}
</style>

<div id="page-wrapper">
    <div class="row">

		<div class="container">
			<form action="<@ofbizUrl>createSchoolLogin</@ofbizUrl>" method="post" class="form-newSchoolEvent">
	    		<div id="signupbox" style=" margin-top:30px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
	    			<div class="panel panel-info">
	    				<div class="panel-heading">
			                <div class="panel-title">
			                	<input type="radio" name="newSchoolEvent" value="newSchool" checked/>&nbsp; New School &nbsp;&nbsp;&nbsp;
								<input type="radio" name="newSchoolEvent" value="newEvent"/>&nbsp; New Academic event
			                </div>
			            </div>
			            
			            <div style="padding-top:30px" class="panel-body" >
							<div class="newSchoolDiv">
								<#if schoolCenterList?? && schoolCenterList?has_content>
									<div style="margin-bottom: 25px" class="input-group">
										<span class="input-group-addon">School ID</span>
										<input type="text" size="20" maxlength="20" name="schoolId" class="form-control" required/>
									</div>
									<#-- <div style="margin-bottom: 25px" class="input-group">
										<span class="input-group-addon">School Name</span>
										<input type="text" size="20" maxlength="100" name="schoolName" class="form-control"/>
									</div> -->
									<div style="margin-bottom: 25px" class="input-group">
										<span class="input-group-addon">School Name</span>
										<select name="schoolCenterId" class="form-control">
											<#list schoolCenterList as individualSchool>
												<option value="${individualSchool.partyIdTo!}">${individualSchool.relationshipName!}</option>
											</#list>
										</select>
									</div>
									
									<input type="submit" class="btn btn-info" value="Submit"/>
								<#else>
									<h2>All schools are created</h2>
								</#if>
							</div>
							<div class="newEventDiv" style="display:none;">
								<div style="margin-bottom: 25px" class="input-group">
									<span class="input-group-addon">School Name</span>
									<select name="schoolCenterId" class="form-control">
										<#if createdSchoolCenterList?? && createdSchoolCenterList?has_content>
											<#list createdSchoolCenterList as schoolUser>
												<option value="${schoolUser.partyId!}">${schoolUser.userLdapDn!}</option>
											</#list>
										</#if>
									</select>
								</div>
								<div style="margin-bottom: 25px" class="input-group">
									<span class="input-group-addon">Task Cycle</span>
									<select name="taskCycle" class="form-control">
										<option value="1">Weekly</option>
										<option value="2">15 Days</option>
										<option value="3">Monthly</option>
									</select>
								</div>
								<div style="margin-bottom: 25px" class="input-group">
									<span class="input-group-addon">Start Month (${currentYear!})</span>
									<select name="startMonth" class="form-control">
										<#list months as monthNub>
											<option value="${monthNub_index}">${monthNub!}</option>
											<#if monthNub_index == 11>
												<#break>
											</#if>
										</#list>
									</select>
								</div>
								<div style="margin-bottom: 25px" class="input-group">
									<span class="input-group-addon">End Month (${nextYear!})</span>
									<select name="endMonth" class="form-control">
										<#list months as monthNub>
											<option value="${monthNub_index}">${monthNub!}</option>
											<#if monthNub_index == 11>
												<#break>
											</#if>
										</#list>
									</select>
								</div>
								
								<input type="submit" class="btn btn-info" value="Submit"/>
							</div>
						</div>
	    			</div>
	    		</div>
			</form>
    		
    		<div style="margin-top:30px" class="table-responsive mainbox col-md-6 col-sm-8">
    			<table class="table table-bordered">
    				<thead>
						<tr>
					        <th>Sl.No</th>
					        <th>UserLogin Id</th>
					        <th>School Name</th>
						</tr>
					</thead>
				    <tbody>
				    	<#list createdSchoolCenterList as schoolUser>
							<tr>
								<td>${schoolUser_index + 1}</td>
								<td>${schoolUser.userLoginId!}</td>
								<td>${schoolUser.userLdapDn!}</td>
							</tr>
						</#list>
					</tbody>
    			</table>
    		</div>
    	</div>
		
    </div>
</div>

