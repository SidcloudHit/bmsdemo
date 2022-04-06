<%-- 
    Document   : index
    Created on : 21 Oct, 2021, 12:20:51 PM
    Author     : acer
--%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Home </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/datatables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/css/responsive.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/css/fixedHeader.bootstrap4.min.css">

        <%
            String useremail = "", user_name = "", dept_name = "", user_role = "";
            int dept_id = 0;
            Common.CommonMethod cm = new CommonMethod();

        %>
        <style>
            .container { 
                height: 100%;
                position: relative;
            }

            .center {
    margin: 0;
    position: relative;
    top: 50%;
    left: 50%;
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
}
            
        </style>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="/Web/header.jsp"></jsp:include>           
                <div class="row">
                    <div class="col-md-6 text-center">
                        <img src="${pageContext.request.contextPath}/Images/tripura.png" alt="tripura" width="40%" />
                    </div>
                <div class="col-md-4">
<!--                    <div class="container">-->
                        <div class="center">
                            <a href="${pageContext.request.contextPath}/../bmscitizenportal/login.jsp" class="btn btn-success btn-lg btn-block">Citizen Login</a>
                            <a href="login.jsp" class="btn btn-success btn-lg btn-block">Department Login</a>
                            <a href="adminLogin.jsp" class="btn btn-success btn-lg btn-block">Admin Login</a>
                            
<!--                        </div>-->
                    </div>

                </div>
            </div>

            <jsp:include page="/Web/footer.jsp"></jsp:include>           
            </div>
        </body>
    </html>
    <!-- jQuery library -->
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>    

<!-- common JavaScript functions-->
<script src ="${pageContext.request.contextPath}/JS/common.js"></script>
<script src="${pageContext.request.contextPath}/JS/validation.js"></script>
<script src="${pageContext.request.contextPath}/JS/ajaxFunctions.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/datatables.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>
<script>
    $(document).ready(function () {
        var useremail = $("#useremail").val();
        var scheme_id = $("#scheme_id").val();
        $('#schemeListDiv').removeClass("d-none").addClass("d-block");
        $('#schemeList').DataTable().clear().destroy();
        $('#schemeList').DataTable({
            "ajax": {
                "url": "${pageContext.request.contextPath}/LaunchedSchemeAll?newscheme=1",
                "type": "POST"
            },

            "iDisplayLength": 50,
            'order': [[1, 'asc']],
            'responsive': true,
            "columns": [
                {"data": "scheme_codification"},
                {"data": "scheme_name"},
                {"data": "probable_launch_date"},
                {"data": "launch_fy"},

                {"data": function (data, type, dataToSet) {
                        if (data.active_upto == null) {
                            return 'Lifetime';
                        } else if (data.active_upto != null) {
                            data.active_upto;
                        } else {
                            return '';
                        }
                    }
                },
                //   &nbsp;<a class="btn btn-info btn-sm" href=Schemes/scheme_enrollment.jsp?data=' + data.onboarding_id + '>Enroll</a>
                {"data": function (data, type, dataToSet) {
                        if (data.scheme_codification != '') {
                            return '<a class="btn btn-info btn-sm" href=viewDetails.jsp?data=' + data.onboarding_id + '>Enroll</a>';
                        } else {
                            return '';
                        }


                    }
                }
            ]
        });

        $('#schemeList_wrapper').addClass("col-md-12");
    });




</script>
