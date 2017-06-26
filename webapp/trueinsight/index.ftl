<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="TrueInsight">
    <meta name="author" content="TrueInsight">

    <title>TrueInsight</title>

    <!-- Bootstrap Core CSS -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/static/css/stylish-portfolio.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/static/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
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
 
 	<div class="top-header">
    <!-- Navigation -->
    
    <a id="menu-toggle" href="#" class="btn btn-dark btn-lg toggle"><i class="fa fa-bars"></i></a>
    <nav id="sidebar-wrapper">
        <ul class="sidebar-nav" id="menu">
            <a id="menu-close" href="#" class="btn btn-light btn-lg pull-left toggle"><i class="fa fa-times"></i></a>
            <li class="sidebar-brand">
                <a href="#top" onclick=$("#menu-close").click();></a>
            </li>
            <li>
                <a href="about-us.html" class="mi-about">About Us</a>
            </li>
            <li>
                <a href="support.html" class="mi-support">Support</a>
            </li>
            <li>
                <a href="#" class="mi-login" data-toggle="modal" data-target="#myModal">Login</a>
            </li>
            <li>
                <a href="register.html" class="mi-register" data-toggle="modal" data-target="#myModalSignUp">Register</a>
            </li>
            <li>
                <a href="#" tabindex="-1" class="mi-set-up">Set Up <span class="fa fa-angle-down"></span></a>
                <ul>
                    <li><a href="#">Import Student Excel</a></li>
                    <li><a href="#">Import Teacher Excel</a></li>
                    <li><a href="#">Add New Category</a></li>
                    <li><a href="#">Define Points</a></li>
                </ul>    
            </li>
            <li>
                <a href="#" class="mi-reports">Reports <span class="fa fa-angle-down"></span></a>
                <ul>
                    <li><a href="#">Reports 1</a></li>
                    <li><a href="#">Reports 2</a></li>
                </ul>    
            </li>
            <li>
                <a href="#" class="mi-insight">Insight <span class="fa fa-angle-down"></span></a>
                <ul>
                    <li><a href="#">Insight 1</a></li>
                    <li><a href="#">Insight 2</a></li>
                </ul>   
            </li>
        </ul>
    </nav>
    
    <div class="right_btn">
        <a href="/control/login" class="btn btn-info btn-login">Login</a>
        <span class="pipe"> </span>
    </div>
    
        <!--div class="modal fade" id="myModal" role="dialog" style="margin-top:100px;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h2 class="modal-title">User Login</h2>
                    </div>
                    
                    <div class="modal-body">
                    <form name="login" id="login">
                        <div class="form-group">
                            <select class="form-control" id="selectrole">
                                <option selected>Select Role</option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                        	<input type="email" class="form-control" id="email" placeholder="Enter email">
                        </div>
                        
                        <div class="form-group">
                        	<input type="password" class="form-control" id="password" placeholder="Password">
                        </div>
                        
                        <div class="form-group"><a href="#">Forgot Password?</a></div>
                        
                        <div class="form-group">
                        	<button type="submit" class="btn btn-primary">Submit</button>
                        </div>
                    </form>
                    </div>
                </div>
            </div>
        </div>
    
    	<div class="modal fade" id="myModalSignUp" role="dialog" style="margin-top:50px;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h2 class="modal-title">User Sign Up</h2>
                    </div>
                    
                    <div class="modal-body">
                    <form name="signup" id="signup">
                        <div class="form-group">
                            <select class="form-control" id="inlineFormCustomSelect">
                                <option selected>Select</option>
                                <option value="1">Mr</option>
                                <option value="2">Mrs</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                        	<input type="firstname" class="form-control" id="firstname" placeholder="First Name">
                        </div>
                        
                        <div class="form-group">
                        	<input type="lastname" class="form-control" id="lastname" placeholder="Last Name">
                        </div>
                        
                        <div class="form-group">
                        	<input type="schoolname" class="form-control" id="schoolname" placeholder="School Name">
                        </div>
                        
                        <div class="form-group">
                        	<input type="mobilnumber" class="form-control" id="mobilnumber" placeholder="Mobile Number">
                        </div>
                        
                        <div class="form-group">
                        	<input type="createpassword" class="form-control" id="createpassword" placeholder="Create Password">
                        </div>
                        
                        <div class="form-group">
                        	<input type="retypepassword" class="form-control" id="retypepassword" placeholder="Re-Type Password">
                        </div>
                        
                        <div class="form-group">By completing sigh up you agree to our <a href="#">User Agreement</a></div>
                        
                        <div class="form-group">
                        	<button type="register" class="btn btn-primary">Register</button>
                        </div>
                    </form>
                    </div>
                </div>
            </div>
        </div-->
    
    <a href="index.html"><div class="logo"> </div></a>
    
   
    
</div>
    <!-- Header -->
   <header id="top" class="header">
        <div class="text-vertical-center">
            <h1>Learning is Fun!</h1>
            <div class="text-vertical-center-sub">Create search your flashcards using simple tools and study<br/>
anywhere and anytime for FREE</div>
            <br>
            <a href="#about" class="btn btn-more">Know More</a>
        </div>
    </header>

    <!-- About -->
    <section id="about" class="about">
        <div class="col-lg-12 text-center">
            <h2>Behavior Tracking, Made Easy</h2>
            <p class="lead">Track points, Demerits, & Comments from any device</p>
        </div>
        <div class="container panel2"></div>
        <!-- /.container -->
    </section>

    <!-- Services -->
    <!-- The circle icons use Font Awesome's stacked icon classes. For more information, visit http://fontawesome.io/examples/ -->
    <section id="services" class="services bg-primary">
        <div class="container">
            <div class="row text-center">
                <div class="col-lg-10 col-lg-offset-1">
                    <h1>How it works</h1>
                    <hr class="small">
                    <div class="row">
                        <div class="col-md-4 col-sm-6">
                            <div class="service-item">
                                <span class="fa-stack fa-4x">
                                <i class="fa fa-circle fa-stack-2x"></i>
                                <i class="fa fa-eye fa-stack-1x text-primary"></i>
                            </span>
                                <h4>
                                    <strong>Track</strong>
                                </h4>
                            </div>
                        </div>
                        <div class="col-md-4 col-sm-6">
                            <div class="service-item">
                                <span class="fa-stack fa-4x">
                                <i class="fa fa-circle fa-stack-2x"></i>
                                <i class="fa fa-trophy fa-stack-1x text-primary"></i>
                            </span>
                                <h4>
                                    <strong>Reward</strong>
                                </h4>
                                <p></p>
                            </div>
                        </div>
                        <div class="col-md-4 col-sm-6">
                            <div class="service-item">
                                <span class="fa-stack fa-4x">
                                <i class="fa fa-circle fa-stack-2x"></i>
                                <i class="fa fa-user fa-stack-1x text-primary"></i>
                            </span>
                                <h4>
                                    <strong>Shape Behaviour</strong>
                                </h4>
                            </div>
                        </div>
                    </div>
                    <!-- /.row (nested) -->
                    <a href="#about" class="btn btn-more btn-white">Know More</a>
                </div>
                <!-- /.col-lg-10 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container -->
    </section>

    <!-- Portfolio -->
    <section id="portfolio" class="portfolio">
        <div class="container">
            <div class="row">
                <div class="col-lg-10 col-lg-offset-1 text-center">
                    <h2>What We Believe</h2>
                    <hr class="small">
                    <p class="lead">Our mission is to make the Indian's students career choice  Scientific, planned and make it available to every one.
We believe that Parents and students should be educated to decide and plan there career scientifically using a career course and psychometric test developed by Mind Metric Professional.</p>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="portfolio-item">
                                <a href="#">
                                    <img class="img-portfolio img-responsive" src="img/portfolio-1.jpg">
                                </a>
                            </div>
                        </div>
                    </div>
                    <!-- /.row (nested) -->
                </div>
                <!-- /.col-lg-10 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.container -->
    </section>

    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-12 col-lg-offset-0 text-left">
                    
                    <div class="col-md-4 col-sm-1">
                            <p class="footer-link">Get in touch</p>
                                <ul class="list-inline">
                                    <li>
                                    <a href="#"><i class="fa fa-twitter fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a href="#"><i class="fa fa-facebook fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a href="#"><i class="fa fa-instagram fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a href="#"><i class="fa fa-dribbble fa-fw fa-2x"></i></a>
                                    </li>
                                    <li>
                                    <a href="#"><i class="fa fa-github fa-fw fa-2x"></i></a>
                                    </li>
                                </ul>
                    </div>
                        
                    <div class="col-md-5 col-sm-8 footer-link">
                        	<a href="#">Terms & Conditions</a> | <a href="#">Private Policy</a> | <a href="#">FAQs</a> | <a href="#">Contact Us</a> | <a href="#">Blog</a>
                    </div>
                        
                    <div class="col-md-3 col-sm-9 footer-link">
                            <span style="color:#11a0dc;">Request More Information</span><br/> 
                            If you would like to contact us please call on +91 985489658
                            between the hours of 10:00 am to 6:00 pm
                            
                    </div>
                    
                

                    


                </div>
            </div>
        </div>
        <a id="to-top" href="#top" class="btn btn-dark btn-lg"><i class="fa fa-chevron-up fa-fw fa-1x"></i></a>
    </footer>

    <!-- jQuery -->
    <script src="/static/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="/static/js/bootstrap.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script>
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
                    }, 1000);
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

</body>

</html>
