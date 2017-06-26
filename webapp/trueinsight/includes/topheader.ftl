<div class="top-header">
    <!-- Navigation -->
    <#include "component://trueinsight/webapp/trueinsight/includes/left.ftl"/>
    <#if userLogin?has_content>
    <div class="right_btn_login">
       <div class="img-circle img-responsive img-rounded user-img"> </div>
        <div class="user-log">
            <span class="welcome">Welcome</span><br/>
            <span class="welcome"><#if party?has_content>${party.description}</#if></span>
        </div>
        <div class="logout"><a href="<@ofbizUrl>logout</@ofbizUrl>"><strong>Logout</strong></a></div>
    </div>
    <#else>
    <div class="right_btn">
        <!--button type="button" class="btn btn-info btn-lg btn-login" data-target="#myModal">Login</button-->
        
        <a href="<@ofbizUrl>login</@ofbizUrl>" class="btn btn-info btn-login">Login</a>
        <span class="pipe"> </span>
        <!--button type="button" class="btn btn-info btn-lg btn-signup" data-toggle="modal" data-target="#myModalSignUp">Sign Up</button-->
        <#--a href="<@ofbizUrl>register</@ofbizUrl>" class="btn btn-info btn-signup">Sign Up</a-->
    </div>
    </#if>
    <#--include "component://trueinsight/webapp/trueinsight/includes/login.ftl"/>
	<#include "component://trueinsight/webapp/trueinsight/includes/register.ftl"/-->
    
    <a href="<@ofbizUrl>main</@ofbizUrl>"><div class="logo"></div></a>
    
</div>
		
			