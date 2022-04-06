<%-- 
    Document   : progress_monitoring_dashboard
    Created on : 18 Dec, 2020, 11:21:45 AM
    Author     : Banti
--%>

<%@page import="Common.CommonMethod"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
// Set to expire far in the past.
    response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
    // Set standard HTTP/1.1 no-cache headers.
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
    // Set standard HTTP/1.0 no-cache header.
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>TRBT || Beneficiary Count Entry </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
       <style>
            .form-group.required .control-label:after {
  content:"*";
  color:red;
}
        </style>
    </head>
    <body>
        <%
    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    String finYear = "";
//System.out.println("Financial month : " + month);
    if (month <= 3) {
        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
    } else {
        finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
    }
    Common.CommonMethod cm = new CommonMethod();
       String ddlScheme = "";
       ddlScheme = cm.getSchemes("-Please Select-");
            %>
        <div class="container bg-light">
            <jsp:include page="Web/header.jsp"></jsp:include>
                <div class="row mt-2 mb-2">
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="text-primary">Beneficiary Count Entry</h4>
                            </div>
                            <div class="card-body bg-page font-weight-bold">
                               <form id="frmDashboard" action="saveMonitoringData">
                                   <div class="form-group row mx-auto required">
                                       <label for="scheme" required=""  class="col-sm-3 col-form-label control-label">Select Scheme :</label>
                                       <div class="col-sm-7">
                                           <select class="custom-select mr-sm-2" id="ddlSchemes" name="scheme" required="" >
                                               <%= ddlScheme %>
                                           </select>
                                           </div>
                                   </div>
                                  <div class="form-group row mx-auto required">
                                       <label for="FY" required="" class="col-sm-3 col-form-label control-label" >Select FY :</label>
                                       <div class="col-sm-7">
                                           <select class="custom-select mr-sm-2" id="fy" name="fy" required="" >
                                              <%= finYear %>
                                           </select>
                                   </div>
                                   </div>
                                   <div class="form-group row mx-auto required">
                                       <label for="Installment" required="" class="col-sm-3 col-form-label control-label" >Select Installment :</label>
                                       <div class="col-sm-7">
                                           <select class="custom-select mr-sm-2" id="ddlInstallment" name="installment">
                                               
                                           </select>
                                   </div>
                                   </div>
                                   <div class="form-group row mx-auto required">
                                       <label for="scheme_code" class="col-sm-3 col-form-label control-label" >Beneficiary Count :</label>
                                       <div class="col-sm-7">
                                           <input type="text" required="" onkeypress="return IsNumeric(event);" class="form-control" maxlength="7" id="benificiary_count" name="benificiary_count">
                                           <span id="lblinstallmentError" style="color: red"></span>
                                   </div>
                                   </div>
                                   <div class="form-group row mx-auto">
                                       <div class="col-sm-3 col-form-label" ></div>
                                       <div class="col-sm-7">
                                   <button type="submit" class="btn btn-primary">Submit</button>
                                       </div>
                                   </div>
                            </form>
                            </div>                        
                        </div>
                    </div>                 
                </div>
            <jsp:include page="Web/footer.jsp"></jsp:include>
        </div>
    </body>
    
    <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>
            <script>
            $("#ddlSchemes").on('change', function (e) {

	var schemeId = $('#ddlSchemes option:selected').val();
	$("#ddlInstallment").empty();
	$("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
	$.ajax({
		url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + schemeId,
		type: "POST",
		success: function (data) {
			//console.log(data);
			var myObj = JSON.parse(data);
			//console.log(myObj.prd);
			if (parseInt(myObj.prd) > 0) {
				var num = parseInt(myObj.prd);
				var yearly = ['April-March'];
				var halfyearly = ['April-September', 'October-March'];
				var triannualy = ['April-July', 'August-November', 'December-March'];
				var quarterly = ['April-June', 'July-September', 'October-December', 'January-March'];
				var monthly = ['April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March'];
				var selArr;
				if (num === 1) {
					selArr = yearly;
				} else if (num === 2) {
					selArr = halfyearly;
				} else if (num === 3) {
					selArr = triannualy;
				} else if (num === 4) {
					selArr = quarterly;
				} else if (num === 12) {
					selArr = monthly;
				}

				//var instOpts = "";
				for (var i = 1; i <= num; i++) {
					$("#ddlInstallment").append("<option value=\"" + i + "\">" + selArr[i - 1] + " </option>");
				}
			}
			
		}
	});
});
      function IsNumeric(e) {
          var key = window.e ? e.keyCode : e.which;
    if (e.keyCode === 8 || e.keyCode === 46) {
        return true;
    } else if ( key < 48 || key > 57 ) {
        lblinstallmentError.innerHTML = "Only Numbers are Allowed.";
        //alert("Only Numbers are Allowed");
        return false;
    } else {
        lblinstallmentError.innerHTML = "";
        return true;
    }
        }
        </script>
</html>

