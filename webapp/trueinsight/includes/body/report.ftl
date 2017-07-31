<#assign partyId = request.getParameter("partyId")?if_exists/>
<!-- About -->
<section id="services" class="services bg-primary">
	<div class="container">
		<div class="row">
			<div class="col-lg-12"><br />
    		<iframe src="<@ofbizUrl>report.pdf?partyId=${partyId?if_exists}</@ofbizUrl>" width="100%" height="1500px" ></iframe>
    		</div>
   		</div>
   	</div>
</section>