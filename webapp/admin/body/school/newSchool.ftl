<#assign organizationList = request.getAttribute("organizationList")!>
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
    		<div id="signupbox" style=" margin-top:30px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
    			<div class="panel panel-info">
    				<div class="panel-heading">
		                <div class="panel-title">Create Student</div>
		            </div>
		            
		            <div style="padding-top:30px" class="panel-body" >
						<form action="<@ofbizUrl>createSchoolLogin</@ofbizUrl>" method="post" class="form-horizontal">
							<div style="margin-bottom: 25px" class="input-group">
								<span class="input-group-addon">School ID</span>
								<input type="text" size="20" maxlength="20" name="schoolId" class="form-control"/>
							</div>
							<div style="margin-bottom: 25px" class="input-group">
								<span class="input-group-addon">School Name</span>
								<input type="text" size="20" maxlength="100" name="schoolName" class="form-control"/>
							</div>
							<div style="margin-bottom: 25px" class="input-group">
								<span class="input-group-addon">Organization</span>
								<select name="organizationId" class="form-control">
									<#list organizationList as organizationId>
										<option value="${organizationId.partyId!}">${organizationId.description!}</option>
									</#list>
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
						</form>
					</div>
    			</div>
    		</div>
    	</div>
		
    </div>
</div>