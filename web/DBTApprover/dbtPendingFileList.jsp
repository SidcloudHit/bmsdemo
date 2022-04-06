<%-- 
    Document   : dbtPendingFileList
    Created on : Sep 21, 2020, 3:18:23 PM
    Author     : NIC-Arindam
--%>

<%@page import="Master.Data.AdminUser"%>
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
<%
    String message = "";
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
    String userId = "", userName = "", userDes = "";

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        officeId = Integer.parseInt(loginObj.getUserOfficeId());

    }

    if (!userId.equals("")) {

        Common.CommonMethod cm = new CommonMethod();
        int urlId = cm.getUrlId(request.getRequestURI().replace("/bms/", ""));
        if (cm.isUrlAllowed(deptId, roleId, urlId)) {
            //String roleLevel = cm.getUserRoleLevel(usr.getUser_role());
            //JSONArray jsarr = null, jsarr1 = null;
            //JSONObject jsobj = null, jsobj1 = null;
            //jsarr = cm.getUserOfficeDetails(userId,usr.getUser_department_id());

            int edit = 0;
            String ddlSchemes = "", ddlDist = "", ddlOffice = "";
            ddlSchemes = cm.getCashSchemes("-Please Select-");//cm.getSchemes("-Please Select-");            

            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            String finYear = "";
            //System.out.println("Financial month : " + month);
            if (month <= 3) {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                //finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
            } else {
                finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                //finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
            }

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

            //String ddlDept = "";
            //ddlDept = cm.getDepartment("-Please Select-");
            int distId = 0;
            //if (jsarr != null) {
            //    for (int n = 0; n < jsarr.length(); n++) {
            //        jsobj = jsarr.getJSONObject(n);
            //        distId = jsobj.getInt("dist_code");

            //ddlDist = cm.getDistrict(distId, "-Please Select-");
            ddlOffice = cm.getOffice(officeId, "-Please Select-");

            //if (usr.getUser_role() == 1) {
            //    ddlDept = cm.getDepartment("-Please Select-");
            //} else {
            //    ddlDept = cm.getDeprtment(usr.getUser_department_id(), "-Please Select-");
            //}
            //    }
            //}

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Pending File List</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
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


    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">

                    <%                        if (!regMsg.equals("")) {
                    %>
                    <div class="alert alert-<%=type%>">
                        <strong><%=regMsg%></strong>
                    </div>
                    <%
                        }
                    %>
                    <div class="card">
                        <div class="card-header bg-info">
                            <h4 class="text-white">Pending File List for DBT in Cash:</h4>
                        </div>
                        <div class="card-body bg-form">
                            <div class="row" id="fileListDiv">
                                <div class="input-group mb-3 col-md-12">
                                    <table id="beneficiaryList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                        <thead class="bg-warning">
                                            <tr>  
                                                <th>Sl No.</th>
                                                <th>Scheme Name</th>
                                                <th>Financial Year</th>
                                                <th>Installment</th>
                                                <th>File Name</th>
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
            <jsp:include page="../Web/footer.jsp"></jsp:include>
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
        //$('#btnSearch').on('click', function (e) {

        //var distId = $('#ddlDistrict option:selected').val();
        //var subdivId = $('#ddlSubDiv option:selected').val();
        var blockId = $('#ddlUrbRurBody option:selected').val();
        var gpId = $('#ddlWardGp option:selected').val();
        var schemeId = $('#ddlSchemes option:selected').val();
        var finYr = $('#ddlFinYear option:selected').val();
        var instl = $('#ddlInstallment  option:selected').val();
        var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;

        if (schemeId === "" || finYr === "" || instl === "") {
            alert("Kindly Select Scheme, Financial Year and Installment for searching beneficiaries");
            return false;
        }
        $('#benListDiv').removeClass("d-none").addClass("d-block");

        $('#beneficiaryList').DataTable().clear().destroy();
        $('#beneficiaryList').DataTable({
            "ajax": {
                "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtapp=1&finYr=" + finYr + "&instl=" + instl + "&blockId=" + blockId + "&gpId=" + gpId + "&schemeId=" + schemeId + "&land=" + land,
                "type": "POST"
            },
            "columnDefs": [
                {
                    'targets': 0,
                    'className': 'select-checkbox',
                    'checkboxes': {
                        'selectRow': true
                    }
                }
            ],
            "select": {
                'style': 'multi'
            },
            "iDisplayLength": 50,
            'order': [[1, 'asc']],
            'responsive': true,
            "columns": [
                {"data": "rc_mem"},
                {"data": "rc_mem"},
                {"data": "full_name"},
                {"data": "guardian_name"},
                {"data": "mobile"},
                {"data": "bank_account"},
                {"data": "ifsc"},
                {"data": "scheme_name"},
                {"data": "amount"}
            ]
        });

        $('#beneficiaryList_wrapper').addClass("col-md-12");
        //});



        $('#btnPushFile').on('click', function (e) {
            var fileName = $("#hdnFileName").val();
            var finYr = $('#ddlFinYear option:selected').val();
            $.ajax({
                url: "${pageContext.request.contextPath}/DBTFileGenerationCash?reqType=2&finYr=" + finYr + "&fileName=" + fileName,
                type: "POST",
                success: function (data) {
                    console.log(data);

                    if (data.CTOS_ack === "SUCCESS") {
                        $("#msgDiv").html("File pushed successfully for DBT");

                        $('#ddlSchemes').removeAttr('readonly');
                        $('#ddlFinYear').removeAttr('readonly');
                        $('#ddlInstallment').removeAttr('readonly');
                        $('#chkBxAllowNoLand').removeAttr('readonly');
                        $('#ddlOffice').removeAttr('readonly');
                        $('#ddlOfficeChecker').removeAttr('readonly');
                        $('#ddlOfficeMaker').removeAttr('readonly');
                        $('#ddlUrbRurBody').removeAttr('readonly');
                        $('#ddlWardGp').removeAttr('readonly');
                        $('#btnSearch').removeAttr('readonly');

                        $('#btnAdd').removeAttr("disabled");
                        $("#hdnFileName").val("");
                        $('#filePushDiv').addClass("d-none");
                        $('#btnSearch').click();
                    }


                }
            });
        });


    });


</script>
<%
        } else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>
