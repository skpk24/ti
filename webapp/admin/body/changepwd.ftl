<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Change Password</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                	<div class="col-lg-6">
                        <!-- /.panel-heading -->
                        <form id="updatePassword" name="updatePassword" method="POST" action="<@ofbizUrl>changepwd</@ofbizUrl>" role="form">
                        	<input type="hidden" name="userLoginId" value="${userLogin.userLoginId}"/>
                        	<input type="hidden" name="partyId" value="${userLogin.partyId}"/>
                        
	                        <div class="form-group">
	                            <label>Current Password * </label>
	                            <input class="form-control"  id="currentPassword" name="currentPassword" required="required" value="">
	                        </div>
	                        <div class="form-group">
	                            <label>New Password *</label>
	                            <input class="form-control" id="newPassword" name="newPassword" required="required" value="">
	                        </div>
	                        <div class="form-group">
	                            <label>Verify New Password *</label>
	                            <input class="form-control" id="newPasswordVerify" name="newPasswordVerify" required="required" value="">
	                        </div>
	                        
	                        <!--a class="btn btn-success" href="javascript:document.updatePassword.submit()">Update</a-->
	                        <button type="submit" id="btn-changepwd" name="submit" class="btn btn-success">Update</button>
	                        <a class="btn btn-danger" href="<@ofbizUrl>main</@ofbizUrl>"> Cancel </a>
	                        
	                    </form>
	                    
	            	</div>        
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
</div>
<!-- /#page-wrapper -->