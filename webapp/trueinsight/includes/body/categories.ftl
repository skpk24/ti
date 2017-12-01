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
                                <div class="category-items txt-check">
                                	<span class="category-name">${cat.description?if_exists}</span>
                                	<input type="checkbox" name="id" id="categories" value="${cat.surveyQuestionCategoryId+","+request.getParameter("id")+","+surveyId}" class="hidden" autocomplete="off">
                                    <span class="category-number">?</span>
                                    <span class="category-description" style="display:none">
                                    	${cat.longDescription?if_exists}
                                    </span>
                                </div>
                            </div>
                        </div>
                        </#list>
                        <#else>
                      	<div class="form-group">	
                        	<div class="col-md-12">
                                <div class="">
                        			Comming Soon ...
                        		</div>
                        	</div>
                        </div>
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
    
    
    <!-- Modal -->
		  <div class="modal fade" id="alertModalCF" style="top:30%;">
		    <div class="modal-dialog">
		    
		      <!-- Modal content-->
		      <div class="modal-content">
		        <div class="modal-header">
		          <button type="button" class="close" data-dismiss="modal">&times;</button>
		          <h3 class="modal-title" id="alertModalTitle"></h3>
		        </div>
		        <div class="modal-body"  style="text-align: justify;" id="alertModalContent">
		          <p>lore lo posim  lore lo posim lore lo posim lore lo posim lore lo posim lore lo posim lore lo posim lore lo posim lore lo posim lore lo posim 
					 </p>
		        </div>
		      </div>
		      
		    </div>
		  </div>
		  <!-- end Modal -->