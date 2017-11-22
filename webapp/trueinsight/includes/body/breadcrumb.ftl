<div class="row">
<div class="pg-heading">
    <ul class="breadcrumb">
      <li><a href="index.html">Home</a></li>
      <#if headerItem?has_content >
	      <#if headerItem.equals("questions") >
	      <li><a href="grades.html?type=${sessionAttributes.type?if_exists}">${sessionAttributes.grade?default("Grade")}</a></li>
	      <li><a href="categories.html?id=${sessionAttributes.gradeId?if_exists}">${sessionAttributes.category?default("Category")}</a></li>
	      <li><a href="students.html?id=${parameters.id?if_exists}">${sessionAttributes.student?default("Students")}</a></li>
	      <li><a href="questions.html">Question</a></li>
	      <#elseif headerItem.equals("students")>
			<li><a href="grades.html?type=${sessionAttributes.type?if_exists}">${sessionAttributes.grade?default("Grade")}</a></li>
	      	<li><a href="categories.html?id=${sessionAttributes.gradeId?if_exists}">${sessionAttributes.category?default("Category")}</a></li>
	      	<li><a href="students.html?id=${parameters.id?if_exists}">${sessionAttributes.student?default("Students")}</a></li>
	      <#elseif headerItem.equals("categories")>
			<li><a href="grades.html?type=${sessionAttributes.type?if_exists}">${sessionAttributes.grade?default("Grade")}</a></li>
	      	<li><a href="categories.html">${sessionAttributes.category?default("Category")}</a></li>
	      <#elseif headerItem.equals("grades")>
			<li><a href="grades.html?type=${sessionAttributes.type?if_exists}">${sessionAttributes.grade?default("Grade")}</a></li>
	      </#if>
      </#if>
    </ul>
</div>
</div>
<#if headerItem?has_content && headerItem.equals("students")?has_content>
<div class="row">
<div class="search-box">
    <div class="input-group stylish-input-group">
        <input type="text" class="form-control"  placeholder="Search" >
        <span class="input-group-addon">
            <button type="submit">
                <span class="glyphicon glyphicon-search"></span>
            </button>  
        </span>
    </div>
</div>
</div>
</#if>
