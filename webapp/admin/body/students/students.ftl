<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            	Students
            	<div class="pull-right"> 
	            	<a class="btn btn-xs btn-info" href="<@ofbizUrl>uploadStudents</@ofbizUrl>?partyId=${parameters.partyId?if_exists}"><span class="fa fa-folder-open fa-fw"></span>Import Students</a>
	            </div>
            </h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <#--${screens.render("component://trueinsight/widget/admin/CommonScreens.xml#studentlist")}-->
    <!-- /.col-lg-12 -->
    <div class="row">
    	<div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">
					<div id="tree1" data-url="/adminRest/private/json/ti/getTree/edupaedia/S"></div>
				</div>
			</div>
    	</div>
    </div>
</div>
<!-- /#page-wrapper -->