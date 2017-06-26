<!--<div id="page-wrapper">
	<div class="row">
		<div class="panel-body">
			<form role="form" enctype="multipart/form-data" method="post"  action="<@ofbizUrl>uploadBulkSchool?partyId=${parameters.partyId?if_exists}</@ofbizUrl>" name="uploadBulkSchool" id="uploadBulkSchool">
			 <div class="row">
			  <div class="col-lg-12">
                <div class="form-group">
                    <label>File input</label>
                    
                    <input type="file" size="50" name="uploadFile" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
                    <input type="hidden" name="uploadHere" value="y" />
                    <input type="hidden" name="uploadType" value="school"/>
                    <input type="hidden" name="partyId" value="trueinsight"/>
                    <input type="hidden" name="productid" value="${parameters.id?if_exists}"/>
                </div>
			 	<div class="col-lg-12">
			 		<input class="btn btn-primary btn-lg btn-block" type="submit" value="Submit">
			 	</div>
			 	 </div> 
             </div>
             </form>
    	</div>                
	</div>
	
</div>-->

	<form role="form" enctype="multipart/form-data" method="post" name="uploadSurveyQue" id="uploadSurveyQue">
        <input type="hidden" name="uploadHere" value="y" />
        <input type="hidden" name="uploadType" id="uploadType"/>
        <input type="hidden" name="partyId" value="trueinsight"/>
        <table cellspacing="0" class="basic-table">
            <tr>
            	<td width="40%" align="right" valign="top">
                    <h2>Select Grade</h2>
                </td>
                <td>&nbsp;</td>
                <td width="60%" valign="top">
                    <select name="surveyId" id="surveyId">
                    	<#if gradeLists?? && gradeLists?has_content>
	                    	<#list gradeLists as gradeLists>
	                    		<option value="${gradeLists.surveyId?if_exists}">${gradeLists.surveyName?if_exists}</option>
	                    	</#list>
	                    </#if>
                    </select>
                </td>
            </tr>
            <tr>
            	<td width="40%" align="right" valign="top">
                    <h2>${uiLabelMap.ContentImport} Excel</h2>
                </td>
                <td>&nbsp;</td>
                <td width="60%" valign="top">
                    <input type="file" size="50" name="uploadFile" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
                </td>
            </tr>
            <tr>
                <td width="45%" align="center" colspan="3" valign="top">
                    <input type="submit" class="smallSubmit" value="Submit"/>
                </td>
            </tr>
        </table>
    </form>
    
<script>
$(document).ready(function(){
    $( "#uploadSurveyQue" ).submit(function( event ) {
    	var surveyId = $('#surveyId').val();
    	$('#uploadType').val(surveyId+'_SurveyQuetions');
    	$('#uploadSurveyQue').attr('action', '<@ofbizUrl>uploadSurveyQuestion?partyId=trueinsight&surveyId='+surveyId+'</@ofbizUrl>');
	});
});
</script>