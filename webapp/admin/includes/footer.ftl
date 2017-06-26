	<#--a href="<@ofbizUrl>cmslogin</@ofbizUrl>">Login</a-->
	
	<!-- jQuery -->
    <script src="${request.getContextPath()}/static/js/jquery.min.js"></script>
    
    <#if parameters.thisRequestUri?has_content && (parameters.thisRequestUri == "school" || parameters.thisRequestUri == "teachers" || parameters.thisRequestUri == "students")> 
    <!--script src="//code.jquery.com/jquery-1.9.1.min.js"></script-->	
    <script src="${request.getContextPath()}/static/js/tree.jquery.js"></script>
	<script type="text/javascript">
	$('#tree1').tree({
	    autoOpen: true,
	    dragAndDrop: false
	});
	</script>
	</#if>
    

    <!-- Bootstrap Core JavaScript -->
    <script src="${request.getContextPath()}/static/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="${request.getContextPath()}/static/js/metisMenu.min.js"></script>
	
	<#--if parameters.thisRequestUri?has_content && parameters.thisRequestUri == "main">
    ${screens.render("component://admin/widget/CommonScreens.xml#chartdata")}
    </#if-->
    
    <#if parameters.thisRequestUri?has_content && (parameters.thisRequestUri == "students")> 
    <!-- DataTables JavaScript -->
    <script src="${request.getContextPath()}/static/js/jquery.dataTables.min.js"></script>
    <script src="${request.getContextPath()}/static/js/dataTables.bootstrap.min.js"></script>
    <script src="${request.getContextPath()}/static/js/dataTables.responsive.js"></script>
    
	<!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script type="text/javascript">
    $(document).ready(function() {
        $('#dataTables-example').DataTable({
            responsive: true
        });
    });
    
    $(document).ready(function(){
    	//alert(" S         ");
	    $('#example').DataTable({
	        "ajax": '/admin/static/data/students.txt'
	    });
	});
    </script>
	</#if>
	
    <!-- Custom Theme JavaScript -->
    <script src="${request.getContextPath()}/static/js/sb-admin-2.js"></script>
    <script src="${request.getContextPath()}/static/js/jquery.validate.min.js"></script>
    <script src="${request.getContextPath()}/static/js/form-validation.js"></script>
    <script src="${request.getContextPath()}/static/js/jquery-te-1.4.0.min.js"></script>	
	<script>
	    function verifyMinLength(event, o, minLen, maxLen) {
	    	 var pwdLen = o.value.length;
	        if (pwdLen<minLen || maxLen < pwdLen) {
	           // alert("The password must be between "+minLen+"-"+maxLen+ " characters in length.");
	            //console.log("The password must be between "+minLen+"-"+maxLen+ " characters in length.");
	            event.srcElement.style.color = "red";
	            $("#pwderror").html("The password must be between "+minLen+"-"+maxLen+ " characters in length.").css("color","red"); 
	          //  o.focus();
	        } else {
	        	  event.srcElement.style.color = "";
	        	  $("#pwderror").html(""); 
	        }
	    }
		
		$('.jqte-test').jqte();
		
		// settings of status
		/*
		var jqteStatus = true;
		$(".status").click(function()
		{
			jqteStatus = jqteStatus ? false : true;
			$('.jqte-test').jqte({"status" : jqteStatus})
		});*/
	</script>
</body>

</html>
