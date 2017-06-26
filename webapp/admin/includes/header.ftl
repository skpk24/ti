<#assign docLangAttr = locale.toString()?replace("_", "-")>
<#assign langDir = "ltr">
<#if "ar.iw"?contains(docLangAttr?substring(0, 2))>
    <#assign langDir = "rtl">
</#if>
<html  lang="${docLangAttr}" dir="${langDir}" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta name="viewport" content="width=device-width, initial-scale=1">
    	<meta name="description" content="TrueInsight">
    	<meta name="author" content="TrueInsight">
		
		<title>${layoutSettings.companyName}: <#if (page.titleProperty)?has_content>${uiLabelMap[page.titleProperty]}<#else>${(page.title)!}</#if></title>


		<!-- bootstrap & fontawesome -->
		<!-- Bootstrap Core CSS -->
		<link rel="stylesheet" href="${request.getContextPath()}/static/css/bootstrap.min.css" />
		<!-- MetisMenu CSS -->
		<link rel="stylesheet" href="${request.getContextPath()}/static/css/metisMenu.min.css" />
		
		<#if parameters.thisRequestUri?has_content && (parameters.thisRequestUri == "students")>
			<!-- DataTables CSS -->
	    	<link href="${request.getContextPath()}/static/css/dataTables.bootstrap.css" rel="stylesheet">
	    	<!-- DataTables Responsive CSS -->
	    	<link href="${request.getContextPath()}/static/css/dataTables.responsive.css" rel="stylesheet">
	    	<link href="${request.getContextPath()}/static/css/jquery.dataTables.min.css" rel="stylesheet">
    	<#else>
    		<link type="text/css" rel="stylesheet" href="${request.getContextPath()}/static/css/jquery-te-1.4.0.css">
		</#if>
		
		<#if parameters.thisRequestUri?has_content && (parameters.thisRequestUri == "school"
		 || parameters.thisRequestUri == "teachers"
		  || parameters.thisRequestUri == "students")> 
			<link href="${request.getContextPath()}/static/css/jqtree.css" rel="stylesheet">
		</#if>
		<!-- Custom CSS -->
		<link href="${request.getContextPath()}/static/css/sb-admin-2.css" rel="stylesheet">
		
		<!-- Morris Charts CSS --> 
		<link href="${request.getContextPath()}/static/css/morris.css" rel="stylesheet">
		
		<!-- Custom Fonts -->
		<link href="${request.getContextPath()}/static/css/font-awesome.min.css" rel="stylesheet" type="text/css">
		
		<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
		    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
	</head>
	<body>
	