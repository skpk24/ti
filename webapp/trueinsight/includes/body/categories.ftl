<section id="services" class="services bg-primary">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 text-left">
                  
                
                ${screens.render("component://trueinsight/widget/CommonScreens.xml#breadcrumb")}

                    <div class="row">
                    <form action="<@ofbizUrl>students.html</@ofbizUrl>" name="categories" method="POST" id="categoriesForm">
                    	<#if surveyCategories?has_content>
                    	<#list surveyCategories as cat>
                        <div class="form-group">	
                        	<div class="col-md-12">
                                <div class="category-items txt-check">${cat.description?if_exists}</div>
                                <input type="checkbox" name="id" id="categories" value="${cat.surveyQuestionCategoryId+","+request.getParameter("id")+","+surveyId}" class="hidden" autocomplete="off">
                            </div>
                        </div>
                        </#list>
                        </#if>
                        
                        
                        <!--div class="col-md-12" align="right">
                        <a href="<@ofbizUrl>grades.html</@ofbizUrl>" class="btn btn-danger">PREVIOUS</a>
                        <input type="submit" value="NEXT" class="btn btn-secondary" style="color:#333333;">
                        </div-->
                      </form>   
                        
                        
                    </div>
                    <!-- /.row (nested) -->
                </div>
                <!-- /.col-lg-10 -->
            </div>
            <!-- /.row -->
        </div>
    </section>