	  	<div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<@ofbizUrl>main</@ofbizUrl>">
            	<img src="${request.getContextPath()}/static/img/logo.jpg" width="210px" height="35px"/>
			</a>
        </div>
        <!-- /.navbar-header -->

        <ul class="nav navbar-top-links navbar-right">
            <!-- /.dropdown -->
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="fa fa-user fa-fw"></i> <i class="fa fa-caret-down"></i>
                </a>
                <ul class="dropdown-menu dropdown-user">
                    <li><a href="#"><i class="fa fa-user fa-fw"></i> ${userLogin.userLoginId}</a>
                    </li>
                    <li><a href="<@ofbizUrl>changePassword</@ofbizUrl>"><i class="fa fa-gear fa-fw"></i> Change Password</a>
                    </li>
                    <#--li><a href="<@ofbizUrl>cms</@ofbizUrl>"><i class="fa fa-edit fa-fw"></i> CMS</a>
                    </li-->
                    <li class="divider"></li>
                    <li><a href="<@ofbizUrl>logout</@ofbizUrl>"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                    </li>
                </ul>
                <!-- /.dropdown-user -->
            </li>
            <!-- /.dropdown -->
        </ul>
        <!-- /.navbar-top-links -->