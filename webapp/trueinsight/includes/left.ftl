<a id="menu-toggle" href="#" class="btn btn-dark btn-lg toggle"><i class="fa fa-bars"></i></a>
    <nav id="sidebar-wrapper">
        <ul class="sidebar-nav" id="menu">
            <a id="menu-close" href="#" class="btn btn-light btn-lg pull-left toggle"><i class="fa fa-times"></i></a>
            <li class="sidebar-brand">
                <a href="#top" onclick="$('#menu-close').click();"></a>
            </li>
            <#if userLogin?has_content>
            <li>
	            <a href="#" class="mi-insight"><span class="welcome">Welcome</span>
	            <span style="text-transform:none"><#if party?has_content>${party.description}</#if></span></a>
            </li>
            <li>
	            <a href="<@ofbizUrl>myaccount.html</@ofbizUrl>" class="mi-register">My Account</a>
            </li>
            </#if>
            
            <#if security.hasEntityPermission("TI", "_TEACHER", session)>
            <li>
                <a href="grades.html?type=A" class="mi-support" style="text-transform:none">Developmental Milstone</a>
            </li>
            <li>
                <a href="grades.html?type=B" class="mi-support" style="text-transform:none">Academic Assessment</a>
            </li>
            <#else>
            <li>
                <a href="#" class="mi-support" onclick="toggle_visibility('subChilds');">Report</a>
            	<#if userLogin?? && userLogin?has_content>
	            	<form action="report.html" method="POST" id="reportForm">
	            		<input type="hidden" name="partyId" id="inptParty"/>
	                	<ul id="subChilds" style="display:none;">
		                	<#if multiChildExists?exists && multiChildExists?has_content>
			                	<#if multiChildExists && partyDetails?? && partyDetails?has_content>
			                		<#list partyDetails as partyDetailsEach>
			                			<li><a href="#" class="mi-support" onclick="getReport('${partyDetailsEach.partyId?if_exists}')">${partyDetailsEach.firstName?if_exists} ${partyDetailsEach.lastName?if_exists}</a></li>
			                		</#list>
			                	<#else>
			                		<li><a href="#" class="mi-support" onclick="getReport('${userLogin.partyId?if_exists}')"><#if party?has_content>${party.description}</#if></a></li>
			                	</#if>
			                <#else>
		                		<li><a href="#" class="mi-support" onclick="getReport('${userLogin.partyId?if_exists}')"><#if party?has_content>${party.description}</#if></a></li>
		                	</#if>
	                	</ul>
	            	</form>
            	</#if>
            </li>
            </#if>
            <li>
                <a href="about-us.html" class="mi-about">Glossory</a>
            </li>
            <!--li>
                <a href="about-us.html" class="mi-about">About Us</a>
            </li-->
            <li>
                <a href="support.html" class="mi-support">Support</a>
            </li>
            <li>
                <a href="support.html" class="mi-support">FAQ</a>
            </li>
            <li>
                <a href="support.html" class="mi-support">How it works</a>
            </li>
            
            <#--li>
                <a href="#" class="mi-login" data-toggle="modal" data-target="#myModal">Login</a>
            </li>
            <li>
                <a href="register.html" class="mi-register" data-toggle="modal" data-target="#myModalSignUp">Register</a>
            </li-->
            <#--if userLogin?has_content>
            <li>
                <a href="#" tabindex="-1" class="mi-set-up">Set Up <span class="fa fa-angle-down"></span></a>
                <ul>
                    <li><a href="#">Import Student Excel</a></li>
                    <li><a href="#">Import Teacher Excel</a></li>
                    <li><a href="#">Add New Category</a></li>
                    <li><a href="#">Define Points</a></li>
                </ul>    
            </li>
            <li>
                <a href="#" class="mi-reports">Reports <span class="fa fa-angle-down"></span></a>
                <ul>
                    <li><a href="#">Reports 1</a></li>
                    <li><a href="#">Reports 2</a></li>
                </ul>    
            </li>
            <li>
                <a href="#" class="mi-insight">Insight <span class="fa fa-angle-down"></span></a>
                <ul>
                    <li><a href="#">Insight 1</a></li>
                    <li><a href="#">Insight 2</a></li>
                </ul>   
            </li>
            </#if-->
        </ul>
        <#--if userLogin?has_content>
        <div class="right_btn_login_M">
	       <div class="img-circle img-responsive img-rounded user-img"> </div>
	        <div class="user-log">
	            <span class="welcome">Welcome</span><br/>
	            <span class="welcome"><#if party?has_content>${party.description}</#if></span>
	        </div>
	        <div class="logout"><a href="<@ofbizUrl>logout</@ofbizUrl>"><strong>Logout</strong></a></div>
	    </div>
	    <#else>
	    
	    </#if-->
    </nav>
 <script type="text/javascript">
    function toggle_visibility(id) {
       var e = document.getElementById(id);
       if(e.style.display == 'block')
          e.style.display = 'none';
       else
          e.style.display = 'block';
    }
    function getReport(partyNub) {
    	$("#inptParty").val(partyNub);
    	$("#reportForm").submit();
    }
</script>