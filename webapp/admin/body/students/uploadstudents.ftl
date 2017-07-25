<div id="page-wrapper">
	<div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            	<a href="<@ofbizUrl>students</@ofbizUrl>?partyId=${parameters.partyId?if_exists}">
        			<button type="button" class="btn btn-success btn-circle"><i class="fa fa-backward"></i></button>
        		</a>
            	Upload File
            	
            	<!--div class="pull-right"> 
                	<a class="btn btn-xs btn-info" href="${request.getContextPath()}/static/js/Product_Test.xlsx"><span class="fa fa-folder-open fa-fw"></span>Product Import Sample</a>
                </div-->
            </h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
	<div class="row">
		<div class="panel-body">
			<form role="form" enctype="multipart/form-data" method="post"  action="<@ofbizUrl>uploadBulkStudents?partyId=${parameters.partyId?if_exists}</@ofbizUrl>" name="imageUploadForm" id="imageUploadForm">
			 <div class="row">
			  <div class="col-lg-12">
			  	<div class="form-group">
                    <label>Centers</label>
                    <#if partyRelationships?has_content>
                    <select name="centerId" onchange="setUrlParam(this.value);">
                    	<option value="">Select Center</option>
                    	<#list partyRelationships as pr>
                    	<option value="${pr.partyIdTo}">${pr.relationshipName?if_exists}</option>
                    	</#list>
                    </select>
                    </#if>
                </div>
                <div class="form-group">
                    <label>File input</label>
                    <input type="file" size="50" name="uploadFile"/>
                    <input type="hidden" name="uploadHere" value="y"/>
                    <input type="hidden" name="uploadType" value="student"/>
                    <input type="hidden" name="partyId" value="${parameters.partyId?if_exists}"/>
                    <input type="hidden" name="productid" value="${parameters.id?if_exists}"/>
                </div>
			 	<div class="col-lg-12">
			 		<button  type="submit"  name="submit" class="btn btn-primary btn-lg btn-block">Upload</button>
			 	</div>
			 	 </div> 
             </div>
             </form>
    	</div>                
	</div>
	
	<#if products?has_content>
		<div class="row">
			<div class="panel-body">
				<table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                    <thead>
                        <tr>
                        <#assign fields = delegator.getModelEntity("SellerProduct").getFieldsUnmodifiable() />
                        <#list fields as field>
                            <th>${field.getName()}</th>
                        </#list>
                        </tr>
                    </thead>
                    <tbody>
                    	<#list products as product>
                    	<tr>
                    		<#list fields as field>
                    		<td>${product.get(field.getName())}</td>
                    		</#list>
                    	</tr>
                    	</#list>
                    </tbody>
               </table>
			</div>
		</div>
		<div class="row">
			<div class="panel-body">
				<div class="col-lg-12">
					<a href="<@ofbizUrl>importproducts</@ofbizUrl>?id=${parameters.id?if_exists}" class="btn btn-success btn-lg btn-block">
			 			Import
			 		</a>
			 	</div>
			</div>
		</div>
	</#if>
</div>
<script type="text/javascript">
function setUrlParam(val){
	var x = document.forms["imageUploadForm"].action;
	document.forms["imageUploadForm"].action = x+"&centerId="+val;
}

</script>