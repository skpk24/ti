<div class="pg-heading">
    <ul class="breadcrumb">
      <li><a href="index.html">Home</a></li>
      <#if headerItem?has_content >
      <#if headerItem.equals("questions") >
      <li><a href="grades.html">Grade</a></li>
      <li><a href="categories.html?id=${parameters.id.split(",")[1]?if_exists}">Category</a></li>
      <li><a href="students.html?id=${parameters.id?if_exists}">Students</a></li>
      <li><a href="questions.html">Question</a></li>
      <#elseif headerItem.equals("students")>
		<li><a href="grades.html">Grade</a></li>
      	<li><a href="categories.html?id=${parameters.id.split(",")[1]?if_exists}">Category</a></li>
      	<li><a href="students.html?id=${parameters.id?if_exists}">Students</a></li>
      <#elseif headerItem.equals("categories")>
		<li><a href="grades.html">Grade</a></li>
      	<li><a href="categories.html">Category</a></li>
      <#elseif headerItem.equals("grades")>
		<li><a href="grades.html">Grade</a></li>
      </#if>
      
      </#if>
    </ul>
</div>

<div class="col-sm-offset-8 search-box">
    <div class="input-group stylish-input-group">
        <input type="text" class="form-control"  placeholder="Search" >
        <span class="input-group-addon">
            <button type="submit">
                <span class="glyphicon glyphicon-search"></span>
            </button>  
        </span>
    </div>
</div>