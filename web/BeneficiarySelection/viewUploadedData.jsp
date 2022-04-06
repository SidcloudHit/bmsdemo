<%-- 
    Document   : viewUploadedData
    Created on : 28 Dec, 2020, 10:14:35 AM
    Author     : Banti
--%>
<%@page import="Master.Data.AdminUser"%>
<%@page import="Security.SecurityClass"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
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
        <title>BMS || View Beneficiary Data </title>
        <!-- Latest compiled and minified CSS -->
        <link href="${pageContext.request.contextPath}/CSS/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
        <style>
            .form-group.required .control-label:after {
                content:"*";
                color:red;
            }
        </style>

    </head>
    <body>
        <%
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                roleId = Integer.parseInt(loginObj.getUserRole());
                schemeId = Integer.parseInt(loginObj.getUserScheme());
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
            }

            if (!userId.equals("")) {

                String regMsg = "";
                String logMsg = "";
                String type = "light";

                if (session.getAttribute("RegMsg") != null) {
                    regMsg = session.getAttribute("RegMsg").toString();
                    session.removeAttribute("RegMsg");
                }

                if (session.getAttribute("LogMsg") != null) {
                    logMsg = session.getAttribute("LogMsg").toString();
                    session.removeAttribute("LogMsg");
                }

                if (session.getAttribute("type") != null) {
                    type = session.getAttribute("type").toString();
                    session.removeAttribute("type");
                }

                Common.CommonMethod cm = new CommonMethod();
                int urlId = cm.getUrlId(request.getRequestURI().replace("/bms/", ""));
                if (cm.isUrlAllowed(deptId, roleId, urlId)) {
                    String scheme_name = "", scheme = "";
                    String uploaded = "", validated = "", name_mismatch = "", missing = "", disapproved = "", pending = "", pendingCh = "", apicalled = "", duplicate = "";
                    String schemeCode = "";
                    //dept_id = usr.getUser_department_id();
//                    if (session.getAttribute("schemeId") != null) {
//                        schemeId = Integer.parseInt(session.getAttribute("schemeId").toString());
//                    }
//                    if (session.getAttribute("schemeCode") != null) {
//                        schemeCode = session.getAttribute("schemeCode").toString();
//                    }
//                    if (session.getAttribute("fy") != null) {
//                        fy = session.getAttribute("fy").toString();
//                    }
//                    if (session.getAttribute("installment") != null) {
//                        installment = session.getAttribute("installment").toString();
//                    }

                    if (request.getParameter("ddlSchemes") != null) {
                        scheme = SecurityClass.killcharMin(request.getParameter("ddlSchemes"));
                        schemeId = Integer.parseInt(scheme.split("~")[0]);
                        schemeCode = scheme.split("~")[1];
//                        scheme_name = scheme.split("~")[2];
//                        schemeCode = cm.getSchemeCode(schemeId);
                        scheme_name = cm.getSchemeName(schemeId);
                    }

                    String ddlScheme = "", user_scheme = "";

                    user_scheme = cm.getUserScheme(userId, Integer.toString(deptId));

                    //ddlScheme = cm.getUserSchemesData(schemeId + "~" + schemeCode + "~" + scheme_name, Integer.toString(deptId), user_scheme, "---Please Select---");
                    ddlScheme = cm.getSchemes("-Please Select-", schemeId);

//                    String ddlScheme = "";
//                    ddlScheme = cm.getSchemesData(schemeId + "~" + schemeCode, dept_id, "-Please Select-");
                    //if (user_scheme != "") {

        %>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white">View Beneficiary Data</h4>
                                    </div>
                                    <div class="card-body bg-light">
                                        <form id="frmFileUpload" action="viewUploadedData.jsp" method="post">
                                            <div class="row">
                                                <div class="form-group col-md-4">
                                                    <label for="ddlSchemes">Scheme</label>
                                                    <div class="input-group mb-3">                                                                                
                                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                                        <%=ddlScheme%>                                                    
                                                    </select>
                                                    <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                                </div>
                                            </div>

                                            <div class="form-group col-md-2 align-self-end"> 
                                                <div class="input-group mb-3">
                                                    <button type="submit" id="submit" class="btn btn-primary">Submit</button> 
                                                </div>
                                            </div>
                                        </div>
                                    </form>


                                    <%

                                        if (schemeId != 0 && schemeCode != null && !schemeCode.equals("")) {
                                            String dataCount = cm.dataCount(schemeId, schemeCode, officeId);
                                            String[] result = dataCount.split("~");
                                            uploaded = result[0];
                                            validated = result[1];
                                            name_mismatch = result[2];
                                            missing = result[3];
                                            disapproved = result[4];
                                            pending = result[5];
                                            pendingCh = result[6];
                                            apicalled = result[7];
                                            duplicate = result[8];

                                    %>    
                                    <div class="row">
                                        <div class="col-md-8 m-auto text-center">
                                            <table class="table table-sm table-striped table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th style="width:50%">Status</th>
                                                        <th>Count</th>
                                                    </tr>
                                                </thead> 
                                                <tbody>
                                                    <tr><td>Total Uploaded</td><td ><%=uploaded%></td></tr>
                                                    <tr><td>RC Checking Completed</td><td><%=apicalled%>
                                                            <%
                                                                if (Integer.parseInt(apicalled) < Integer.parseInt(uploaded)) {
                                                            %>
                                                            <button type="button" id="btnStart" name="btnStart" class="btn btn-warning btn-sm">
                                                                <i class="fa fa-search">&nbsp;&nbsp;</i><strong>Complete RC Checking</strong>
                                                            </button>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr><td>Validated</td><td><%=validated%></td> </tr>
                                                    <tr><td>Name Mismatch</td><td><a href="matchData.jsp"><%=name_mismatch%></a></td> </tr>
                                                    <tr><td>Missing in RC</td><td><a href="dataValidation.jsp"><%=missing%></a></td> </tr>
                                                    <tr><td>Name Mismatch(Require Verification)</td><td><a href="dataValidation.jsp"><%=pending%></a></td></tr>
                                                    <tr><td>Disapproved by Checker</td><td><a href="dataValidation.jsp"><%=disapproved%></a></td></tr>
                                                    <tr><td>Probable Duplicates</td><td><a href="dataValidation.jsp"><%=duplicate%></a></td></tr>
                                                    <tr><td>Pending at Checker</td><td><%=pendingCh%></td> </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>


                                    <% }%>
                                </div>                        
                            </div>
                        </div>
                    </div>      
                </div>
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        <%
            /*} else {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('User Not Mapped With Scheme. Please Contact Department Admin');");
                out.println("location='../deptUserHome.jsp';");
                out.println("</script>");
            }*/
        %>
    </body>
    <!-- jQuery library -->
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/common.js"></script>
    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>
    <script src ="${pageContext.request.contextPath}/JS/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/dataTables.bootstrap4.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
    <script>
                                                            $(document).ready(function () {

                                                                $('#ddlSchemes').select2();


                                                                $('#btnStart').on('click', function (e) {
                                                                    //var schemeId = $('#ddlSchemes option:selected').val();
                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/StartSSOTAPICall?rcvalidatefailedcasesmaker=1", // ?schemeId=" + schemeId
                                                                        type: "POST",
                                                                        async: true,
                                                                        success: function (result) {
                                                                            alert(result);
                                                                        }
                                                                    });

                                                                });

                                                                // Toggle plus minus icon on show hide of collapse element
                                                                /*$(".collapse").on('show.bs.collapse', function () {
                                                                 $(this).prev(".card_header").find(".fa").removeClass("fa-plus-circle").addClass("fa-minus-circle");
                                                                 }).on('hide.bs.collapse', function () {
                                                                 $(this).prev(".card_header").find(".fa").removeClass("fa-minus-circle").addClass("fa-plus-circle");
                                                                 });
                                                                 });
                                                                 $(document).ready(function () {
                                                                 $('#contenttable').DataTable({
                                                                 "scrollX": true
                                                                 });
                                                                 selectInstallment($("#ddlSchemes").val());
                                                                 });
                                                                 function getSelectedScheme(sel) {
                                                                 $("#fy").val("");
                                                                 $("#installment_id").val("");
                                                                 $("#match_strength").val("");
                                                                 }
                                                                 function selectInstallment(schemeId) {
                                                                 var installment_id = $("#installment_id").val();
                                                                 $("#ddlInstallment").empty();
                                                                 $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
                                                                 $.ajax({
                                                                 url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + encodeURIComponent(schemeId),
                                                                 type: "POST",
                                                                 success: function (data) {
                                                                 // console.log(data);
                                                                 var myObj = JSON.parse(data);
                                                                 //console.log(myObj.prd);
                                                                 //alert(data);
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
                                                                 if (i == installment_id) {
                                                                 $("#ddlInstallment").append("<option value=\"" + i + "\" selected =\"selected\">" + selArr[i - 1] + " </option>");
                                                                 } else {
                                                                 $("#ddlInstallment").append("<option value=\"" + i + "\" >" + selArr[i - 1] + " </option>");
                                                                 }
                                                                 }
                                                                 }
                                                                 
                                                                 }
                                                                 })
                                                                 }
                                                                 function pushdata() {
                                                                 $("#submit").click();
                                                                 }
                                                                 
                                                                 function IsNumeric(e) {
                                                                 var key = window.e ? e.keyCode : e.which;
                                                                 if (e.keyCode === 8 || e.keyCode === 46) {
                                                                 return true;
                                                                 } else if (key < 48 || key > 57) {
                                                                 lblinstallmentError.innerHTML = "Only Numbers are Allowed.";
                                                                 //alert("Only Numbers are Allowed");
                                                                 return false;
                                                                 } else {
                                                                 lblinstallmentError.innerHTML = "";
                                                                 return true;
                                                                 }*/
                                                            });
    </script>

</html>
<%
        } else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>

