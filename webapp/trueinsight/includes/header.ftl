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
	
	<title><#if layoutSettings?has_content> ${layoutSettings.companyName?if_exists} </#if>: <#if (page.titleProperty)?has_content>${uiLabelMap[page.titleProperty]}<#else>${(page.title)!}</#if> Our mission is to enhance the behaviour of students by creating a reinforced environment among peers, to foster the choicest behaviour and deliver the same to the society. </title>


	<!-- bootstrap & fontawesome -->
	<!-- Bootstrap Core CSS -->
	<link rel="stylesheet" href="${request.getContextPath()}/static/css/bootstrap.min.css" />
	<!-- Custom CSS -->
	<link rel="stylesheet" href="${request.getContextPath()}/static/css/stylish-portfolio.css" />

	<!-- page specific plugin styles -->

	<!-- text fonts -->
	<link rel="stylesheet" href="${request.getContextPath()}/static/font-awesome/css/font-awesome.min.css" />
	
	<!-- bxSlider CSS file -->
	<link href="${request.getContextPath()}/static/css/jquery.bxslider.css" rel="stylesheet" /> 

	<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Roboto:300,400" rel="stylesheet"> 
    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->


	
</head>
<body>
	<#include "component://trueinsight/webapp/trueinsight/includes/topheader.ftl"/>
			