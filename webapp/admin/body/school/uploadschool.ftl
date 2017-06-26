<div id="page-wrapper">
	<div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            	<a href="<@ofbizUrl>school</@ofbizUrl>?partyId=${parameters.partyId?if_exists}">
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
			 		<!--button type="submit" name="submit" class="btn btn-primary btn-lg btn-block">Upload</button-->
			 	</div>
			 	 </div> 
             </div>
             </form>
    	</div>                
	</div>
	
</div>