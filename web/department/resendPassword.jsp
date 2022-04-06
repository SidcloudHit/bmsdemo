<%-- 
    Document   : resendPassword
    Created on : Oct 4, 2021, 4:57:07 PM
    Author     : Administrator
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<!DOCTYPE html>
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
<%
    Common.CommonMethod cm = new CommonMethod();

    String edit = "0";
    String regMsg = "";
    String logMsg = "";
    String type = "light";
    String user_id = "test1234";

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

    String ddlDist = "";
    // ddlDist = cm.getDistrict("-Please Select-");
    String ddlDept = "";
    ddlDept = cm.getDepartment("-Please Select-");


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Resend Password</title>
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
        <style>
            .form-group.required .control-label:after {
                content: "*";
                color: red;
            }
            .dataTables_wrapper .dataTables_paginate .paginate_button{
                padding: 0 !important;
            }
        </style>
    </head>


    <body>

        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div class="col-md-12">

                        <div class="row ">
                            <div class="col-sm-12">

                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white">Resend User Credentials:</h4>
                                    </div>

                                    <div class="card-body bg-light">
                                        <form id="resendpwd"  method="post"> 
                                            <div class="row">
                                                <div class="form-group col-md-6 required">
                                                    <label for="txtUserName" class="control-label">Department</label>
                                                    <select class="custom-select" name="dept_id" id="dept_id" required>
                                                    <%=ddlDept%>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-md-6 required">
                                                <label for="txtUserName" class="control-label">User Type: </label><br/>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="user_type" id="user_type" value="1" >
                                                    <label class="form-check-label" for="flexRadioDefault1">
                                                        Admin Users
                                                    </label>
                                                </div>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="user_type"  id="user_type" value="2">
                                                    <label class="form-check-label" for="flexRadioDefault2">
                                                        Dept User
                                                    </label>
                                                </div>

                                            </div>
                                        </div>
                                        <div class="row mt-2">
                                            <div class="col-md-6">
                                                <button type="button" id="btnSubmit" name="btnSubmit" class="btn btn-primary mb-2">Submit</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="row"> 
                                        <div class="col-md-12">
                                            <div class="row d-none" id="deptListDiv">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="deptList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                        <thead class="bg-info">
                                                            <tr>  
                                                                <th>Name</th>
                                                                <th>Designation</th>
                                                                <th>Department</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>                                        
                                                    </table>
                                                </div>
                                            </div>      
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

        <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

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

    <script src='${pageContext.request.contextPath}/JS/jquery.easing.min.js'></script>
    <script src='${pageContext.request.contextPath}/JS/jquery.validate.js'></script>
</html>
<script>
    $("#btnSubmit").click(function () {
        if ($("#resendpwd").valid()) {
            event.preventDefault();
            var form = $('#resendpwd')[0];
            var dept_id = $("#dept_id").val();
            var user_type = $('input[name="user_type"]:checked').val();
            $('#deptListDiv').removeClass("d-none").addClass("d-block");
            $('#deptList').DataTable().clear().destroy();
            $('#deptList').DataTable({
                "ajax": {
                    "url": "${pageContext.request.contextPath}/userList?dept_id=" + dept_id + "&user_type=" + user_type,
                    "type": "POST"
                },
                "iDisplayLength": 50,
                'order': [[1, 'asc']],
                'responsive': true,
                "bPaginate": false,
                // "searching": false,
                "paging": true,
                "info": false,
                "columns": [
                    {"data": "emp_name"},
                    {"data": "emp_designation"},
                    {"data": "department_name"},
                    {"data": function (data, type, dataToSet) {

                            if (data.emp_name != "") {
                                return '<button class="btn btn-info btn-sm" onclick=sendSms("' + data.user_id + '")>Resend SMS</button>';
                            } else {

                                return '';
                            }
                        }
                    }
                ]
            });
            $('#deptList_wrapper').addClass("col-md-12");
        }
    });
    $(document).ready(function () {

        $("#resendpwd").validate({
            rules: {
                user_type: "required"

            },
            messages: {
                user_type: "This field is required."
            },
            errorPlacement: function (error, element) {
                if (element.is(":radio")) {
                    error.prependTo(element.parent());
                } else { // This is the default behavior of the script
                    error.insertAfter(element);
                }
            }
        });
    });
    function sendSms(user_id) {


        $.ajax({
            url: "${pageContext.request.contextPath}/resendSms?user_id=" + user_id,
            type: "POST",
            success: function (data) {

                if (data == "1") {
                    alert("Password Sent Successfully");
                } else if (data == "2") {
                    alert("Error: This User is not Approved.");
                } else {
                    alert("Error!");
                }
            }
        });
    }
</script>