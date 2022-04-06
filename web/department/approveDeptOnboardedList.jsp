<%-- 
    Document   : approveDeptOnboardedList
    Created on : Sep 13, 2021, 12:04:26 PM
    Author     : Administrator
--%>


<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page import="Master.Data.AdminUser"%>
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
    ddlDist = cm.getDistrict("-Please Select-");
    String ddlDept = "";
    ddlDept = cm.getDepartment("-Please Select-");


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || On-boarded History</title>
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
        </style>
    </head>


    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">

                        <div class="row ">
                            <div class="col-sm-12">

                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white">On-boarded History</h4>
                                    </div>
                                    <div class="card-body bg-light">
                                        <div class="row"> 
                                            <div class="col-md-12">
                                                <div class="row d-none" id="deptListDiv">
                                                    <div class="input-group mb-3 col-md-12">
                                                        <table id="deptList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                            <thead class="bg-info">
                                                                <tr>  
                                                                    <th>Department Name</th>
                                                                    <th>First Nodal Officer Details</th>
                                                                    <th>Second Nodal Officer Details</th>
                                                                    <th>On-boarded Datetime</th>
                                                                    <th>Status</th>
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
</html>
<script>


    $(document).ready(function () {
        $('#deptListDiv').removeClass("d-none").addClass("d-block");
        $('#deptList').DataTable({
            "ajax": {
                "url": "${pageContext.request.contextPath}/deptOnboardedlist?flag=approve",
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
                {"data": "department_name"},
                {"data": function (data, type, dataToSet) {

                        return '<strong>Name:</strong> '+data.nodal1_name+'<br/><strong>Email Id:</strong> '+ data.nodal1_email+'<br/><strong>Mobile No.:</strong> '+ data.nodal1_mobile;
                    }
                },
                {"data": function (data, type, dataToSet) {

                         return '<strong>Name:</strong> '+data.nodal2_name+'<br/><strong>Email Id:</strong> '+ data.nodal2_email+'<br/><strong>Mobile No.:</strong> '+ data.nodal2_mobile;
                    }
                },
                {"data": "entry_datetime"},
                {"data": function (data, type, dataToSet) {

                        return 'Approved';
                    }
                }
            ]
        });
        $('#deptList_wrapper').addClass("col-md-12");
    });

</script>