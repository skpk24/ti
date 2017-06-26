// Wait for the DOM to be ready
$(function() {
  $("form[name='uploadTeacherForm']").validate({
    rules: {
    	uploadFile: {required: true}
    },
    messages: {
    	uploadFile: {
			required: "Please select the file"
		}
    },
    // Make sure the form is submitted to the destination defined
    // in the "action" attribute of the form when valid
    submitHandler: function(form) {
    	form.submit();
    }
  });
  
  $("form[name='imageUploadForm']").validate({
	rules: {
    	uploadFile: {required: true},
  		centerId:{required: true}
    },
    messages: {
    	uploadFile: {
			required: "Please select the file"
		},
		centerId: {
			required: "Please select the school center"
		}
    },
    // Make sure the form is submitted to the destination defined
    // in the "action" attribute of the form when valid
    submitHandler: function(form) {
    	form.submit();
    }
  });
  
  
  $("form[name='uploadBulkSchool']").validate({
	    rules: {
	    	uploadFile: {required: true}
	    },
	    messages: {
	    	uploadFile: {
				required: "Please select the file"
			}
	    },
	    // Make sure the form is submitted to the destination defined
	    // in the "action" attribute of the form when valid
	    submitHandler: function(form) {
	    	form.submit();
	    }
	});
  
  
  $("form[name='loginform']").validate({
	    // Specify validation rules
	    rules: {
	      // The key name on the left side is the name attribute
	      // of an input field. Validation rules are defined
	      // on the right side
	      USERNAME: {required: true},
	      PASSWORD: {required: true, minlength: 5},
	      COMPANYID: {required: true}
	    },
	    // Specify validation error messages
	    messages: {
	    	USERNAME: {
				required: "Please enter the user name"
			},
			PASSWORD: {
				required: "Please enter the password",
				minlength: "Your password must be at least 5 characters long"
			},
			COMPANYID: {
				required: "Please enter the school Id"
			}
	    },
	    // Make sure the form is submitted to the destination defined
	    // in the "action" attribute of the form when valid
	    submitHandler: function(form) {
	      form.submit();
	    }
	  });
  
  
	$("#country").change(function(){
		//alert($("#country").val());
		$.get("/admin/control/states?parent_id="+$("#country").val(), function(data, status){
      //  alert("Data: " + data + " " + status);
        $("#regionid").html(data);
		});
	});
	
	$("#sectorid").change(function(){
		$.get("/admin/control/subsectors?sectorid="+$("#sectorid").val(), function(data, status){
        //alert("Data: " + data + " " + status);
        $("#subsectorid").html(data);
		});
	}); 
});


function getStates(id, rid){
	//alert(id);
	$.get("/admin/control/states?parent_id="+id, function(data, status){
    //alert("Data: " + data + " " + status);
		$("#regionid").html(data);
		 if(status == "success"){
			 setState(rid);
		 }
	});
}


function setCountry(id){
	options = document.getElementById("country").options;
	for(var i = 0; i < options.length; i++){
		if(id == options[i].value){
			options[i].selected = true;
			break;
		}
	}
}

function setState(id){
	options = document.getElementById("regionid").options;
	for(var i = 0; i < options.length; i++){
		if(id == options[i].value){
			options[i].selected = true;
			break;
		}
	}
}
	
function setCompanyType(id){
		options = document.getElementById("company_type").options;
		for(var i = 0; i < options.length; i++){
			if(id == options[i].value){
				options[i].selected = true;
				break;
			}
		}
}

function setLanguage(value){
	options = document.getElementById("language").options;
	for(var i = 0; i < options.length; i++){
		if(value == options[i].value){
			options[i].selected = true;
			break;
		}
	}
}

function setAnualPurchase(value){
	options = document.getElementById("anualpurchage").options;
	for(var i = 0; i < options.length; i++){
		if(value == options[i].value){
			options[i].selected = true;
			break;
		}
	}
}