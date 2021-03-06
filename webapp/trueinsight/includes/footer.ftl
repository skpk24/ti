<!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-lg-offset-0 text-left">
                    
                    <div class="col-md-4 col-sm-1">
                            <p class="footer-link">Get in touch</p>
                                <ul class="list-inline">
                                    <li>
                                    <a target="_blank" href="https://www.youtube.com/channel/UCBDl9RUNDt9ZPSWjW9wqfrw"><i class="fa fa-youtube fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a target="_blank" href="https://www.facebook.com/insightfulinsights/"><i class="fa fa-facebook fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a target="_blank" href="https://www.linkedin.com/company/insightfulinsightsprivatelimited"><i class="fa fa-linkedin fa-fw fa-2x"></i></a>
                                    </li>
                                    <!--li>
                                    <a href="#"><i class="fa fa-dribbble fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a href="#"><i class="fa fa-github fa-fw fa-2x"></i></a>
                                    </li-->
                                </ul>
                    </div>
                        
                    <div class="col-md-5 col-sm-8 footer-link">
                        	<a href="<@ofbizUrl>terms-conditions.html</@ofbizUrl>">Terms & Conditions</a> | <a href="<@ofbizUrl>privacy-refund-policy.html</@ofbizUrl>">Private Policy</a> | <a href="#">Blog</a>
                    </div>
                        
                    <div class="col-md-3 col-sm-9 footer-link">
                            <span style="color:#11a0dc;">Request More Information</span><br/> 
                            If you would like to contact us please call on +91 99455 57733
                            between the hours of 9:00 am to 6:00 pm
                    </div>
                    
                    


                </div>
            </div>
        </div>
        <a id="to-top" href="#top" class="btn btn-dark btn-lg"><i class="fa fa-chevron-up fa-fw fa-1x"></i></a>
    </footer>

    <!-- jQuery -->
    <script src="${request.getContextPath()}/static/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${request.getContextPath()}/static/js/bootstrap.min.js"></script>
    
    <!-- bxSlider Javascript file -->
	<script src="${request.getContextPath()}/static/js/jquery.bxslider.min.js"></script>
	
	 <!-- jQuery data table Javascript file -->
	<script src="${request.getContextPath()}/static/js/jquery.dataTables.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script type="text/javascript">
    // Closes the sidebar menu
    $("#menu-close").click(function(e) {
        e.preventDefault();
        $("#sidebar-wrapper").toggleClass("active");
    });
    // Opens the sidebar menu
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#sidebar-wrapper").toggleClass("active");
    });
    // Scrolls to the selected menu item on the page
    $(function() {
        $('a[href*=#]:not([href=#],[data-toggle],[data-target],[data-slide])').click(function() {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') || location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: target.offset().top
                    }, 2000);
                    return false;
                }
            }
        });
    });
    //#to-top button appears after scrolling
    var fixed = false;
    $(document).scroll(function() {
        if ($(this).scrollTop() > 250) {
            if (!fixed) {
                fixed = true;
                // $('#to-top').css({position:'fixed', display:'block'});
                $('#to-top').show("slow", function() {
                    $('#to-top').css({
                        position: 'fixed',
                        display: 'block'
                    });
                });
            }
        } else {
            if (fixed) {
                fixed = false;
                $('#to-top').hide("slow", function() {
                    $('#to-top').css({
                        display: 'none'
                    });
                });
            }
        }
    });
    // Disable Google Maps scrolling
    // See http://stackoverflow.com/a/25904582/1607849
    // Disable scroll zooming and bind back the click event
    var onMapMouseleaveHandler = function(event) {
        var that = $(this);
        that.on('click', onMapClickHandler);
        that.off('mouseleave', onMapMouseleaveHandler);
        that.find('iframe').css("pointer-events", "none");
    }
    var onMapClickHandler = function(event) {
            var that = $(this);
            // Disable the click handler until the user leaves the map area
            that.off('click', onMapClickHandler);
            // Enable scrolling zoom
            that.find('iframe').css("pointer-events", "auto");
            // Handle the mouse leave event
            that.on('mouseleave', onMapMouseleaveHandler);
        }
        // Enable map zooming with mouse scroll when the user clicks the map
    $('.map').on('click', onMapClickHandler);
    </script>
    
    <#if parameters.thisRequestUri?has_content && (parameters.thisRequestUri == "main" 
    || parameters.thisRequestUri == "index.html" || parameters.thisRequestUri == "login") >
    <script type="text/javascript">
	    $(document).ready(function(){
	  		$('.bxslider').bxSlider({
	  		  //mode: 'vertical',
			  auto: true,
			  autoControls: true
			});
		});
	</script>
	</#if>
	<#if parameters.thisRequestUri?has_content && parameters.thisRequestUri == "grades.html" >
	<script type="text/javascript">
    $(document).ready(function(e){
		$(".img-check").click(function(){
            if($(this).parent("label").find(".check").length == 0) {
				$(this).closest("label").prepend("<img src='/static/img/tick.png' class='check'>");
				$(this).next().prop('checked', true);
				$('#gradesForm').submit();
			} else {
				$(this).parent("label").find(".check").remove();
			}
        });
    });
    </script>
	</#if>
	<#if parameters.thisRequestUri?has_content && parameters.thisRequestUri == "categories.html" >
	<script type="text/javascript">
	$(document).ready(function(e){
		$(".category-name").click(function(){
            if($(this).parent("div").find(".checkCat").length == 0) {
				$(this).closest("div").prepend("<img src='/static/img/tick.png' class='checkCat'>");
				$(this).next().prop('checked', true);
				$('#categoriesForm').submit();
			} else {
				$(this).parent("div").find(".checkCat").remove();
			}
        });
        
        $(".category-number").click(function(){
			 if($(this).parent("div").find(".category-items").length == 0) {
			 	$('#alertModalTitle').html($(this).closest("div").find(".category-name").html());
			 	$('#alertModalContent').html($(this).closest("div").find(".category-description").html());
			 }
			$('#alertModalCF').modal();
		});
	});
	</script>
	</#if>
	<#if parameters.thisRequestUri?has_content && parameters.thisRequestUri == "students.html" >
	<script>
	
	$(document).ready(function() {
	   oTable = $('#example').DataTable({
	    	 "paging":   false,
        	 "ordering": false,
             "info":     false,
             "dom":''
	    });
	    
	    $('#exampleSearch').keyup(function(){
		      oTable.search($(this).val()).draw() ;
		})
	    
	});
	
	$(document).ready(function(e){
		$(".student-items").click(function(){
            if($(this).parent("div").find(".checkstudent").length == 0) {
				$(this).closest("div").prepend("<img src='/static/img/tick.png' class='checkstudent'>");
				$(this).next().prop('checked', true);
				$('#studentsForm').submit();
			} else {
				$(this).parent("div").find(".checkstudent").remove();
			}
        });
	});
	</script>
	</#if>
	
	<script type="text/javascript">
  window._mfq = window._mfq || [];
  (function() {
    var mf = document.createElement("script");
    mf.type = "text/javascript"; mf.async = true;
    mf.src = "//cdn.mouseflow.com/projects/15c23d75-a942-4a41-b027-b928d6ac3cdd.js";
    document.getElementsByTagName("head")[0].appendChild(mf);
  })();
</script>

<!--Start of Tawk.to Script-->
<script type="text/javascript">
var Tawk_API=Tawk_API||{}, Tawk_LoadStart=new Date();
(function(){
var s1=document.createElement("script"),s0=document.getElementsByTagName("script")[0];
s1.async=true;
s1.src='https://embed.tawk.to/5a1fe543198bd56b8c03e3b5/default';
s1.charset='UTF-8';
s1.setAttribute('crossorigin','*');
s0.parentNode.insertBefore(s1,s0);
})();
</script>
<!--End of Tawk.to Script-->
</body>
</html>