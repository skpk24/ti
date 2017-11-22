<div class="top-header">
    <!-- Navigation -->
    <#include "component://trueinsight/webapp/trueinsight/includes/left.ftl"/>
    <#if userLogin?has_content>
    <div class="right_btn_logout">
       <div class="img-circle img-responsive img-rounded user-img right_btn_hide"></div>
        <div class="user-log right_btn_hide">
            <span class="welcome">Welcome</span><br/>
            <span class=""><#if party?has_content>${party.description}</#if></span>
        </div>
        <div class="user-log right_btn_hide">
        <a href="<@ofbizUrl>myaccount.html</@ofbizUrl>" class="">My Account</a>
        </div>
        <a href="<@ofbizUrl>logout</@ofbizUrl>" class="btn btn-info glyphicon glyphicon-log-out"></a>
    </div>
    <#else>
    <div class="right_btn">
        <!--button type="button" class="btn btn-info btn-lg btn-login" data-target="#myModal">Login</button-->
        
        <a href="<@ofbizUrl>login</@ofbizUrl>" class="btn btn-info glyphicon glyphicon-log-in"></a>
        <!--span class="pipe"> </span-->
        <!--button type="button" class="btn btn-info btn-lg btn-signup" data-toggle="modal" data-target="#myModalSignUp">Sign Up</button-->
        <#--a href="<@ofbizUrl>register</@ofbizUrl>" class="btn btn-info btn-signup">Sign Up</a-->
    </div>
    </#if>
    <#--include "component://trueinsight/webapp/trueinsight/includes/login.ftl"/>
	<#include "component://trueinsight/webapp/trueinsight/includes/register.ftl"/-->
    
    <a href="<@ofbizUrl>main</@ofbizUrl>"><div class="logo"></div></a>
    
</div>
		
			