<%-- 
    Document   : scheme_management
    Created on : Jul 27, 2021, 12:29:42 PM
    Author     : Administrator
--%>

<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
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
        <title>BMS || Scheme Management </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/datatables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/css/responsive.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/css/fixedHeader.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">
        <link rel="stylesheet" type="text/css" href="../DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">
        <script>
            function getSelectedDept(sel) {
                document.getElementById("dept_name").value = sel.options[sel.selectedIndex].text;
                document.form1.submit();
            }
        </script>    
        <style>
            .form-group.required .control-label:after {
                content:"*";
                color:red;
            }
            th{
                color:#fff;
            }
        </style>
        <%
            String citizen_dept_id = "";

            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
            String userId = "", userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                if(loginObj.getUserRole() !=null && loginObj.getUserRole() !=""){
                roleId = Integer.parseInt(loginObj.getUserRole());
                }
                if(loginObj.getUserOfficeId() !=null && loginObj.getUserOfficeId() !=""){
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
                }

            }

            if (!userId.equals("")) {
                if (session.getAttribute("scheme_onboarding_dept") != null) {
                    citizen_dept_id = session.getAttribute("scheme_onboarding_dept").toString();
                }

                Common.CommonMethod cm = new CommonMethod();

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
                String ddlDept = "", ddlPeriodicity = "", ddlbeneficiaryCategory = "";
                ddlDept = cm.getOnBoardingDepartment(Integer.toString(deptId), "-Please Select-");
                ddlPeriodicity = cm.getPeriodicity("-Please Select-");
                String regMsg = "";
                String type = "light";

                if (session.getAttribute("RegMsg") != null) {
                    regMsg = session.getAttribute("RegMsg").toString();
                    session.removeAttribute("RegMsg");
                }

                if (session.getAttribute("type") != null) {
                    type = session.getAttribute("type").toString();
                    session.removeAttribute("type");
                }
        %>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            

                <div class="row">
                     <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-sm-12">

                            <%
                                if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>
                            <!--                            <div class="card">
                            
                                                            <div class="card-body  font-weight-bold2">
                                                                <form method="post" action=""> 
                                                                    <div class="form-row">
                                                                        <div class="form-group col-md-6 required">
                                                                            <div class="input-group mb-3">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text" id="basic-addon1">Department</span>
                                                                                </div>
                                                                                <select class="custom-select mr-sm-2" required="" id="dept" name="dept" />
                            </select>     
                        </div>
                    </div>
                    <div class="form-group col-md-3">
                        <div class="input-group mb-3">
                            <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                            </button>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>-->
                            <div class="row">
                                <div class="col-md-12 mt-2">
                                    <div class="card">
                                        <div class="card-header bg-info font-weight-bold2">
                                            <input type="hidden" name="dept_id" id="dept" value="<%=deptId%>" />
                                            <h4 class="text-white">Scheme Management</h4>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-4 mt-2 scheme_list">
                                    <div class="card">
                                        <div class="card-body">
                                            <h5 class="card-title">Cash Schemes</h5>
                                            <div class="row d-none" id="cashschemeListDiv">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="cashschemeList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                        <thead class="bg-info">
                                                            <tr>  
                                                                <th>Scheme Code</th>
                                                                <th>Scheme Name</th>
                                                                <th>Launch Date</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>                                        
                                                    </table>
                                                </div>
                                            </div>                                                    
                                            <a href="schemeRegistration.jsp" target="_blank" class="btn btn-primary cash_link">Add New</a>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4 mt-2 scheme_list">
                                    <div class="card">
                                        <div class="card-body">
                                            <h5 class="card-title">Kind Schemes</h5>
                                            <div class="row d-none" id="kindschemeListDiv">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="kindschemeList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                        <thead class="bg-info">
                                                            <tr>  
                                                                <th>Scheme Code</th>
                                                                <th>Scheme Name</th>
                                                                <th>Launch Date</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>                                        
                                                    </table>
                                                </div>
                                            </div>                                                   
                                            <a href="schemeRegistration.jsp" target="_blank" class="btn btn-primary kind_link">Add New</a>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-sm-4 mt-2 scheme_list">
                                    <div class="card">
                                        <div class="card-body">
                                            <h5 class="card-title">Cash and Kind Schemes</h5>
                                            <div class="row d-none" id="cashkindschemeListDiv">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="cashkindschemeList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                        <thead class="bg-info">
                                                            <tr>  
                                                                <th>Scheme Code</th>
                                                                <th>Scheme Name</th>
                                                                <th>Launch Date</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>                                        
                                                    </table>
                                                </div>
                                            </div> <a href="schemeRegistration.jsp" target="_blank" class="btn btn-primary cashkind_link">Add New</a>
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </div>            
                    </div>

                </div>
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>

        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>
    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>    

    <!-- common JavaScript functions-->
    <script src ="${pageContext.request.contextPath}/JS/common.js"></script>
    <script src="${pageContext.request.contextPath}/JS/validation.js"></script>
    <script src="${pageContext.request.contextPath}/JS/ajaxFunctions.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/datatables.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/jquery.dataTables.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>

    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.html5.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.print.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.buttons.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/jszip.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/pdfmake.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/vfs_fonts.js"></script>
    <script>

            function handleData()
            {
                var form_data = new FormData(document.querySelector("form"));
                if (document.getElementById('scheme_features_fields'))
                    if (!form_data.has("beneficiary_fy_status_apl") && !form_data.has("beneficiary_fy_status_bpl"))
                    {
                        document.getElementById("chk_option_error").style.visibility = "visible";
                        return false;
                    } else
                    {
                        document.getElementById("chk_option_error").style.visibility = "hidden";
                        return true;
                    }

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
                }
            }
            function isAlphaNumeric(e) {
                var keyCode = window.e ? e.keyCode : e.which;
                var lblError = document.getElementById("lblError");
                lblError.innerHTML = "";
                //Regex for Valid Characters i.e. Alphabets and Numbers.
                var regex = /^[A-Za-z0-9]+$/;
                //Validate TextBox value against the Regex.
                var isValid = regex.test(String.fromCharCode(keyCode));
                if (!isValid) {
                    lblError.innerHTML = "Only Alphabets and Numbers allowed.";
                }

                return isValid;
            }
            function lettersOnly()
            {
                var charCode = event.keyCode;
                if ((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123) || charCode == 8)
                    return true;
                else
                    return false;
            }
            $(document).ready(function () {
                $(".scheme_list").css("display", "none");
                var dept_id = $('#dept').val();
                if (dept_id != "") {
                    // $("#btnSearch").click();
                    getDepartmentSchemeData(dept_id);
                }
            });
//    $("#btnSearch").click(function () {
            function getDepartmentSchemeData(dept_id) {
                $(".scheme_list").css("display", "block");
                //var dept_id = $('#dept').val();
                var a_cash_href = $("a.cash_link").attr("href");
                var a_kind_href = $("a.kind_link").attr("href");
                var a_cashkind_href = $("a.cashkind_link").attr("href");
                var cash_uri = a_cash_href.toString();
                var kind_uri = a_kind_href.toString();
                var cashkind_uri = a_cashkind_href.toString();
                if (cash_uri.indexOf("?") > 0) {
                    var clean_uri = cash_uri.substring(0, cash_uri.indexOf("?"));
                    $("a.cash_link").attr("href", clean_uri + '?data=1~' + dept_id);
                } else {
                    $("a.cash_link").attr("href", a_cash_href + '?data=1~' + dept_id);
                }
                if (kind_uri.indexOf("?") > 0) {
                    var clean_uri = kind_uri.substring(0, kind_uri.indexOf("?"));
                    $("a.kind_link").attr("href", clean_uri + '?data=2~' + dept_id);
                } else {
                    $("a.kind_link").attr("href", a_kind_href + '?data=2~' + dept_id);
                }
                if (cashkind_uri.indexOf("?") > 0) {
                    var clean_uri = cashkind_uri.substring(0, cashkind_uri.indexOf("?"));
                    $("a.cashkind_link").attr("href", clean_uri + '?data=5~' + dept_id);
                } else {
                    $("a.cashkind_link").attr("href", a_cashkind_href + '?data=5~' + dept_id);
                }

                $('#cashschemeListDiv').removeClass("d-none").addClass("d-block");
                $('#kindschemeListDiv').removeClass("d-none").addClass("d-block");
                $('#cashkindschemeListDiv').removeClass("d-none").addClass("d-block");
                $('#cashschemeList').DataTable().clear().destroy();
                $('#kindschemeList').DataTable().clear().destroy();
                $('#cashkindschemeList').DataTable().clear().destroy();
                $('#cashschemeList').DataTable({
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/Categorywiseschemelist?cashscheme=1&dept_id=" + dept_id,
                        "type": "POST"
                    },
                    "iDisplayLength": 10,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "bPaginate": false,
                    "searching": false,
                    "paging": true,
                    "info": false,

                    "columns": [
                        {"data": "scheme_codification"
                        },
                        {"data": "scheme_name"
                        },
                        {"data": "launch_date"
                        },
                        {"data": function (data, type, dataToSet) {
                                if (data.scheme_available == "no") {
                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=1~' + dept_id + "~" + data.scheme_id + "~1" + '>' + 'Edit' + '</a>';
                                } else if (data.scheme_available == "yes") {
                                    return 'Published';
                                } else {
                                    return '';
                                }

                                //                                if (data.scheme_available == null) {
                                //                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=1~' + dept_id + "~" + data.scheme_id + "~1" + '>' + 'Configure' + '</a>';
                                //                                } else if (data.scheme_available === 'N') {
                                //                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=1~' + dept_id + "~" + data.scheme_id + "~2" + '>' + 'Publish' + '</a>';
                                //                                } else if (data.scheme_available === 'Y') {
                                //                                    return 'Published';
                                //                                } else {
                                //                                    return '';
                                //                                }
                            }}
                    ]
                });
                $('#kindschemeList').DataTable({
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/Categorywiseschemelist?kindscheme=1&dept_id=" + dept_id,
                        "type": "POST"
                    },
                    "iDisplayLength": 10,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "bPaginate": false,
                    "searching": false,
                    "paging": true,
                    "info": false,
                    "columns": [
                        {"data": "scheme_codification"},
                        {"data": "scheme_name"},
                        {"data": "launch_date"},
                        {"data": function (data, type, dataToSet) {
                                if (data.scheme_available == "no") {
                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=2~' + dept_id + "~" + data.scheme_id + "~1" + '>' + 'Edit' + '</a>';
                                } else if (data.scheme_available == "yes") {
                                    return 'Published';
                                } else {
                                    return '';
                                }
                                //                                if (data.scheme_available == null) {
                                //                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=2~' + dept_id + "~" + data.scheme_id + "~1" + '>' + 'Configure' + '</a>';
                                //                                } else if (data.scheme_available === 'N') {
                                //                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=2~' + dept_id + "~" + data.scheme_id + "~2" + '>' + 'Publish' + '</a>';
                                //                                } else if (data.scheme_available === 'Y') {
                                //                                    return 'Published';
                                //                                } else {
                                //                                    return '';
                                //                                }
                            }}
                    ]
                });
                $('#cashkindschemeList').DataTable({
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/Categorywiseschemelist?cashkindscheme=1&dept_id=" + dept_id,
                        "type": "POST"
                    },
                    "iDisplayLength": 10,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "bPaginate": false,
                    "searching": false,
                    "paging": true,
                    "info": false,
                    "columns": [
                        {"data": "scheme_codification"},
                        {"data": "scheme_name"},
                        {"data": "launch_date"},
                        {"data": function (data, type, dataToSet) {
                                if (data.scheme_available == "no") {
                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=5~' + dept_id + "~" + data.scheme_id + "~1" + '>' + 'Edit' + '</a>';
                                } else if (data.scheme_available == "yes") {
                                    return 'Published';
                                } else {
                                    return '';
                                }
                                //
                                //                                if (data.scheme_available == null) {
                                //                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=3~' + dept_id + "~" + data.scheme_id + "~1" + '>' + 'Configure' + '</a>';
                                //                                } else if (data.scheme_available === 'N') {
                                //                                    return '<a class="btn btn-info btn-sm" href=schemeRegistration.jsp?data=3~' + dept_id + "~" + data.scheme_id + "~2" + '>' + 'Publish' + '</a>';
                                //                                } else if (data.scheme_available === 'Y') {
                                //                                    return 'Published';
                                //                                } else {
                                //                                    return '';
                                //                                }
                            }}
                    ]
                });
                $('#cashschemeList_wrapper').addClass("col-md-12");
                $('#kindschemeList_wrapper').addClass("col-md-12");
                $('#cashkindschemeList_wrapper').addClass("col-md-12");

            }
//   );


    </script>
</html>
<%

    } else {
        response.sendRedirect("../login.jsp");
    }
%>


