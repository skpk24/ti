<section id="services" class="services bg-primary">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 text-left">
                  
                ${screens.render("component://trueinsight/widget/CommonScreens.xml#breadcrumb")}

                    <div class="row">
                    <form action="<@ofbizUrl>questions.html</@ofbizUrl>" name="students" method="POST" id="studentsForm">
                    	<#if partyRelationship?has_content>
                    	<#list partyRelationship as studs>
                        <div class="form-group">	
                        	<div class="col-md-4">
                                <div class="student-items txt-check">
                                    <span class="student-name">${studs.relationshipName?if_exists}</span>
                                    <span class="student-number">${studs.comments?default("0")}</span>
                                </div>
                                <input type="checkbox" name="id" id="students" value="${catId+","+gradeId+","+surveyId+","+studs.partyIdTo}" class="hidden" autocomplete="off">
                            </div>
                        </div>
                        </#list>
                        </#if>
                        
                        
                        <!--div class="col-md-12" align="right">
                        <a href="<@ofbizUrl>categories.html</@ofbizUrl>" class="btn btn-danger">PREVIOUS</a>
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