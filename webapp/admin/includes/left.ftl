<!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    
        ${screens.render("component://trueinsight/widget/admin/CommonScreens.xml#topmenu")}

        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li>
                        <a href="<@ofbizUrl>main</@ofbizUrl>"><i class="fa fa-dashboard fa-fw"></i> Dashboard</a>
                    </li>
                    <li>
                        <a href="<@ofbizUrl>school</@ofbizUrl>?partyId=${userLogin.externalAuthId?if_exists}"><i class="fa fa-users fa-fw"></i> School </a>
                    </li>
                    <li>
                        <a href="<@ofbizUrl>teachers</@ofbizUrl>?partyId=${userLogin.externalAuthId?if_exists}"><i class="fa fa-user fa-fw"></i> Teachers </a>
                    </li>
                    <li>
                        <a href="<@ofbizUrl>students</@ofbizUrl>?partyId=${userLogin.externalAuthId?if_exists}"><i class="fa fa-user fa-fw"></i> Students </a>
                    </li>
                    <li>
                        <a href="<@ofbizUrl>questionbank</@ofbizUrl>?partyId=${userLogin.externalAuthId?if_exists}"><i class="fa fa-user fa-fw"></i> Question Bank </a>
                    </li>
                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>