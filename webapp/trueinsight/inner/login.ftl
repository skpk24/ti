
<br /><br /><br /><br /><br />
<#if requestAttributes.uiLabelMap??><#assign uiLabelMap = requestAttributes.uiLabelMap></#if>
<#assign useMultitenant = Static["org.ofbiz.base.util.UtilProperties"].getPropertyValue("general.properties", "multitenant")>

<#assign username = requestParameters.USERNAME?default((sessionAttributes.autoUserLogin.userLoginId)?default(""))>
<#if username != "">
  <#assign focusName = false>
<#else>
  <#assign focusName = true>
</#if>
<div>
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">User Login</h2>
            </div>
            <div class="modal-body">
            <form method="POST" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform" >
            	<#--input type="hidden" name="orgId" value="trueinsight" /-->
            	<#--if branches?has_content>
                <div class="form-group">
                    <select class="form-control" id="branchId">
                        <#list branches as branch>
                        <option value="${branch.partyIdTo}">${branch.relationshipName?if_exists}</option>
                        </#list>
                        
                    </select>
                </div>
                </#if-->
                <div class="form-group">
                	<#--input type="email" class="form-control" id="email" placeholder="Enter email"-->
                	<input type="text" class="form-control" id="USERNAME" name="USERNAME" value="${username?if_exists}" placeholder="Username" />
                </div>
                
                <div class="form-group">
                	<input type="password" class="form-control" name="PASSWORD" id="password" placeholder="Password">
                </div>
                
                <!--div class="form-group"><a href="#">Forgot Password?</a></div-->
                
                <div class="form-group">
                	<input type="submit"  class="btn btn-primary" value="${uiLabelMap.CommonLogin}"/>
                	<!--button type="submit" class="btn btn-primary">Submit</button-->
                </div>
            </form>
            </div>
        </div>
    </div>
</div>