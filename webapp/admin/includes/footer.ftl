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
	
	<#if parameters.thisRequestUri?has_content && (parameters.thisRequestUri=="createCalEvent" || parameters.thisRequestUri=="editEvent") >
		<script src="${request.getContextPath()}/static/js/jquery.datetimepicker.full.min.js"></script>
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
	
	<!-- For schoolCenter select to submit form in events -->
	<script type="text/javascript">
		$('#schoolCenter').change(function() {
			var parentForm = $(this).closest("form");
			if (parentForm && parentForm.length > 0)
				parentForm.submit();
		});
		
		function showModal(formType){
			if(formType == 'createEvent'){
				var header = "Create Event";
				var content = '<input type="hidden"  name="scopeEnumId" value="WES_PUBLIC">';
				content += '<input type="hidden" name="workEffortTypeId" value="TASK">';
				content += '<div class="form-group">';
				content += '<label for="eventName" class="col-form-label">Select Center</label>';
				content += '<select name="partyId" id="schoolCenter" class="form-control"><#if schoolCenters?has_content><#list schoolCenters as schoolCenter><optionvalue="${schoolCenter.partyIdTo?if_exists}">${schoolCenter.relationshipName?if_exists}</option></#list></#if>';
				content += '</select>';
				content += '</div>';
				var strSubmitFunc = "applyButtonFunc()";
				var btnText = "Add";
			}
			else
				{
				var header = "Update Event";
				var content = "This is my dynamic content";
				var strSubmitFunc = "applyButtonFunc()";
				var btnText = "update";
				}
			doModal('idMyModal', header, content, strSubmitFunc, btnText);
		}
		
		function doModal(placementId, heading, formContent, strSubmitFunc, btnText)
		{
			var html = '<div class="modal fade" id="modalWindow" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">'
				  html += '<div class="modal-dialog modal-lg" role="document">'
				  html += '<div class="modal-content">'
				  html += '<form id="calEventsForm" class="form-horizontal" name="calEventsForm" method="POST" action="<@ofbizUrl>createEvent</@ofbizUrl>">'
				  html += '<div class="modal-header">'
				   html += '<h5 class="modal-title h5" id="exampleModalLongTitle">'+heading+'</h5>'
				   html += '<button type="button" class="close" data-dismiss="modal" aria-label="Close">'
				   html += '<span aria-hidden="true">&times;</span>'
				   html += '</button>'
				   html += '</div>'
				   html += '<div class="modal-body" style="padding:-200px;">'
				   html += formContent
				   html +=  '</div>'
				   html +=  '<div class="modal-footer">'
				    html +=  '<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>'
				    html +=  '<button type="button" class="btn btn-primary">'+btnText+'</button>'
				   html +=   '</div>'
				   html += '</form>'
				  html +=   '</div>'
				 html +=  '</div>'
				html += '</div>'  
		    $("#"+placementId).html(html);
		    $('#modalWindow').modal();
		}
	</script>
</body>

</html>
