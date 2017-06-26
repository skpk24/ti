<#if requestAttributes.uiLabelMap??><#assign uiLabelMap = requestAttributes.uiLabelMap></#if>
<#assign useMultitenant = Static["org.ofbiz.base.util.UtilProperties"].getPropertyValue("general.properties", "multitenant")>

<#assign username = requestParameters.USERNAME?default((sessionAttributes.autoUserLogin.userLoginId)?default(""))>
<#if username != "">
  <#assign focusName = false>
<#else>
  <#assign focusName = true>
</#if>

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">${uiLabelMap.CommonRegistered}</h3>
                </div>
                <div class="panel-body">
                	<form method="post" action="<@ofbizUrl>login</@ofbizUrl>" name="loginform" role="form" id="loginform">
                        <fieldset>
                            <div class="form-group">
                            	<input class="form-control" placeholder="User name" type="text" name="USERNAME" value="${username}" size="20" autofocus/>
                            </div>
                            <div class="form-group">
                            	<input class="form-control" placeholder="Password" type="password" name="PASSWORD" value="" size="20"/>
                            </div>
                            <div class="form-group">
                            	<input class="form-control" placeholder="Schoool Id" type="text" name="COMPANYID" value="" size="20"/>
                            </div>
                            <!-- Change this to a button or input when using this as a form -->
                            <input type="submit"  class="btn btn-lg btn-success btn-block" value="${uiLabelMap.CommonLogin}"/>
                            <#--a href="<@ofbizUrl>forgotPassword</@ofbizUrl>">${uiLabelMap.CommonForgotYourPassword}?</a-->
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
    
