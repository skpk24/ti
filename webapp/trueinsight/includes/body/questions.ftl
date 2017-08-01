<section id="services" class="services bg-primary">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 text-left">
                ${screens.render("component://trueinsight/widget/CommonScreens.xml#breadcrumb")}
                	<div class="row">
                    <form action="<@ofbizUrl>save.html</@ofbizUrl>" name="students" method="POST" id="radio-btn">
                        <div class="form-group">	
                        	<div class="">
                                <div class="question-panel">
                                    <#if qAppls?has_content>
                                    	<#assign cnt = 1 />
                                    	<#list qAppls as qs>
                                    		<#assign options = Static["org.ofbiz.entity.util.EntityUtil"].filterByAnd(qOpts, {"surveyQuestionId" : qs.surveyQuestionId}) />
		                                    <div class="question-main btn-group" data-toggle="buttons">
		                                        <div class="question-heading">${cnt}. ${qs.question}</div>
		                                        <input type="hidden" name="qId" value="${qs.surveyQuestionId}" />
		                                        <div class="question-points">
			                                        <#list options as option>                            
				                                        <div class="btn btn-default">
				                                            <input type="radio" required name="id_${qs.surveyQuestionId}" value="${oldId+","+qs.surveyQuestionId+","+option.surveyOptionSeqId+","+userLogin.userLoginId}" />
				                                            <label for="not-achieved">${option.description}</label>  
				                                        </div> 
				                                    </#list>
		                                        </div>
		                                    </div>
		                                    <#assign cnt = cnt + 1 />
                                    	</#list>
                                    </#if>
                                    
                                    <!--div class="textarea-comments">
                                    <textarea class="form-control" id="exampleTextarea" required rows="3" placeholder="Enter Your Comments"></textarea>
                                    </div-->
                                    
                                </div>
                            </div>
                        </div>
                         
                        
                        <div class="" align="right">
                        <a href="<@ofbizUrl>students.html</@ofbizUrl>" class="btn btn-danger">PREVIOUS</a>
                        <input type="submit" value="SUBMIT" class="btn btn-success " style="color:#333333;">
                        </div>
                      </form>   
                        
                    </div>
                    <!-- /.row (nested) -->
                </div>
                <!-- /.col-lg-10 -->
            </div>
            <!-- /.row -->
        </div>
    </section>