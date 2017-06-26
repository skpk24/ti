<section id="services" class="services bg-primary">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 text-left">
                  
                
                ${screens.render("component://trueinsight/widget/CommonScreens.xml#breadcrumb")}

                    <div class="row">
                    <form action="<@ofbizUrl>categories.html</@ofbizUrl>" name="grades" method="POST" id="gradesForm">
                        <div class="form-group">
							<#if partyRels?has_content>
                            	<#list partyRels as rel>
                            		<div class="portfolio-item">
			                            <div class="col-md-3">
			                                <label class="">
		                                		<img src="${request.getContextPath()}/static/img/grade/${rel.relationshipName?if_exists}.png" alt="..." id="test" class="img-check img-portfolio img-responsive">
		                                    	<input type="checkbox" name="id" id="playgroup" value="${rel.partyIdTo}" class="hidden" autocomplete="off">
			                                </label>
		                                </div>
		                            </div>
                            	</#list>
                            </#if>
                            
                            
                            <#--div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/nursery.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="nursery" id="nursery" value="val2" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/lkg.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="lkg" id="lkg" value="val3" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/ukg.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="ukg" id="ukg" value="val4" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/1st.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="1st" id="1st" value="val5" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/2nd.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="2nd" id="2nd" value="val6" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/3rd.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="3rd" id="3rd" value="val7" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/4th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="4th" id="4th" value="val8" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/5th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="5th" id="5th" value="val9" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/6th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="6th" id="6th" value="val10" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/7th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="7th" id="7th" value="val11" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/8th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="8th" id="8th" value="val12" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/9th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="9th" id="9th" value="val13" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div>
                            
                            <div class="portfolio-item">
	                            <div class="col-md-3">
	                                <label class="">
	                                    <img src="${request.getContextPath()}/static/img/grade/10th.png" alt="..." class="img-portfolio img-responsive img-check">
	                                    <input type="checkbox" name="10th" id="10th" value="val14" class="hidden" autocomplete="off">
	                                </label>
                                </div>
                            </div-->
                      </div>
                        <!--div class="col-md-12" align="right"><input type="submit" value="NEXT" class="btn btn-secondary" style="color:#333333;"></div-->
                      </form>   
                         
                        
                        
                    </div>
                    <!-- /.row (nested) -->
                </div>
                <!-- /.col-lg-10 -->
            </div>
            <!-- /.row -->
        </div>
    </section>