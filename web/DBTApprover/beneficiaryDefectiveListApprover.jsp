<%-- 
    Document   : beneficiaryDefectiveListVerifier
    Created on : 8 Jan, 2022, 8:18:11 AM
    Author     : acer
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
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
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0, workflow = 1;
    String userId = "", userName = "", userDes = "";

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        officeId = Integer.parseInt(loginObj.getUserOfficeId());
        schemeId = Integer.parseInt(loginObj.getUserScheme());

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

            //ddlSchemes = cm.getCashSchemes("-Please Select-", deptId);//cm.getSchemes("-Please Select-");   
            ddlSchemes = cm.getSchemes("-Please Select-", schemeId);

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

            String ben_office_id = "";
            if (request.getParameter("ben_office_id") != null && request.getParameter("ben_office_id") != "") {
                ben_office_id = request.getParameter("ben_office_id");
            }
            if (ben_office_id != "") {
                ddlOffice = cm.getChildOffice(schemeId, workflow, officeId, ben_office_id, "-All-");
            } else {
                ddlOffice = cm.getChildOffice(schemeId, workflow, officeId, "-All-");
            }

            if (session.getAttribute("RegMsg") != null) {
                regMsg = session.getAttribute("RegMsg").toString();
                session.removeAttribute("RegMsg");
            }

            if (session.getAttribute("type") != null) {
                type = session.getAttribute("type").toString();
                session.removeAttribute("type");
            }
            String scheme_name = cm.getSchemeName(schemeId);
            String scheme_code = cm.getSchemeCode(schemeId);
            String route = "";
            if (request.getParameter("route") != null && request.getParameter("route") != "") {
                route = request.getParameter("route");
            }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Beneficiary Management</title>
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
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">



    </head>
    <body>

        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            

                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">


                        <div class="row">
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
                                    <h4 class="text-white">Defective Beneficiary List:</h4>
                                </div>

                                <div class="card-body bg-light">
                                    <div class="row">
                                        <input type="hidden" id="ddlSchemes" name="ddlSchemes" value="<%=schemeId + "~" + scheme_code%>" />
                                        <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="<%=scheme_name%>" />
                                         <input type="hidden" id="roleId" name="roleId" value="<%=roleId%>" />

                                        <!--                                        <div class="form-group col-md-3">
                                                                                    <label for="ddlSchemes">Scheme</label>
                                                                                    <div class="input-group mb-3">                                                                                
                                                                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                        <%=ddlSchemes%>                                                    
                                    </select>
                                    <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                </div>
                            </div>  -->
                                        <div class="form-group col-md-3">
                                            <label for="ddlSubDiv">Beneficiary Verified By</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlOfficeChecker" name="ddlOfficeChecker" class="form-control form-control-sm" required="required" >
                                                    <%=ddlOffice%>
                                                </select>
                                            </div> 
                                        </div>

                                        <div class="form-group col-md-3 align-self-end">
                                            <div class="input-group mb-3">
                                                <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >
                                                <button type="button" id="btnSearch" name="btnSearch" class="btn btn-warning btn-block">
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i><strong>Search</strong>
                                                </button>
                                            </div>
                                        </div>
                                    </div> 
                                    <div class="row d-none" id="benListDiv">
                                        <div class="col-md-12"> 

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="card">
                                                        <div class="card-body bg-dark ">
                                                            <span class="text-white font-weight-bold">Defective Beneficiary List (Uploaded by Citizen)</span>
                                                        </div> 
                                                    </div>
                                                </div>                                                    
                                            </div>
                                            <div class="row">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="beneficiaryListCitizen" class="table table-sm table-striped table-bordered" style="width:100%">
                                                        <thead>
                                                            <tr>
                                                                <th>Application ID</th>
                                                                <th>Beneficiary Name</th>
                                                                <th>Mobile No</th>
                                                                <th>Office Name</th>
                                                                <th>Entry Date</th>   
                                                                <th>Remarks</th>
                                                                <th>Status</th>
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
        $(document).ready(function () {


            var table, table1;
            searchbuttonclick();

            $('#btnSearch').on('click', function (e) {
                var i = 1;
                //var distId = $('#ddlDistrict option:selected').val();
                //var subdivId = $('#ddlSubDiv option:selected').val();
                var officeId = $('#hdnOfficeId').val();
                var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                //var blockId = $('#ddlUrbRurBody option:selected').val();
                //var gpId = $('#ddlWardGp option:selected').val();
                var schemeId = $('#ddlSchemes').val();
                var roleId= $('#roleId').val();
                //var finYr = $('#ddlFinYear option:selected').val();
                //var instl = $('#ddlInstallment  option:selected').val();
                //var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
                var status = 8;

//                if (officeId === "" || schemeId === "" || checkerOfficeId === "") {
//                    alert("Kindly Select Scheme and Beneficiary Verified By for searching beneficiaries");
//                    return false;
//                }




                console.log(officeId);
                console.log(checkerOfficeId);


                $('#benListDiv').removeClass("d-none").addClass("d-block");
                $('#beneficiaryListCitizen').DataTable().clear().destroy();
                table1 = $('#beneficiaryListCitizen').DataTable({
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptmverbenDefectivecit=1&officeId=" + officeId + "&checkerOfficeId=" + checkerOfficeId + "&schemeId=" + schemeId + "&status=" + status+ "&roleId="+roleId,
                        "type": "POST"
                    },
                    dom: 'Bfrtip',
                    "iDisplayLength": 50,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": "application_id", "width": "15%"},
                        {"data": "applicant_name", "width": "20%"},
                        {"data": "mobile", "width": "15%"},
                        {"data": "office_name", "width": "20%"},
                        {"data": "entrydate", "width": "15%"},
                        {"data": "remarks", "width": "15%"},
                        {"data": function (data, type, dataToSet) {
                            if (data.action_taken === 13 || data.action_taken === 15) {
                                return  "Defective";
                            } else {
                                return  "Rejected";
                            }
                        }},
                       {"data": function (data, type, dataToSet) {
                          return  "<a class=\"btn btn-primary btn-sm\" href=\"${pageContext.request.contextPath}/Reports/viewCitizenDocumentForUnfreeze.jsp?aid=" + data.application_id + "~1\" target=\"_blank\">View Application</a>";
                       }}
                        //  ,
//                                                                                {"data": function (data, type, dataToSet) {
//                                                                                        return  "<a href=\"${pageContext.request.contextPath}/Reports/viewCitizenDocumentForUnfreeze.jsp?aid=" + data.application_id + "\" target=\"_blank\"><button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\">View Application</button></a>";
//                                                                                    }}

                        //,
//                                                                    {"data": function (data, type, dataToSet) {
//                                                                            return  "<button class=\"btn btn-primary btn-sm\" id=\"btnRaiseQuery\" name=\"btnRaiseQuery\" value=\"" + data.appl_id + "#" + data.full_name + "\">Unfreeze</button>";
//                                                                        }}
                    ]

                });
                $('#beneficiaryListCitizen_wrapper').addClass("col-md-12");
                $("select[name='beneficiaryListCitizen_length']").removeClass("custom-select custom-select-sm ");

                $('#beneficiaryList_wrapper').addClass("col-md-12");
                $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");

            });
             $('#ddlOfficeChecker').select2();
        });

        function searchbuttonclick() {
            var i = 1;
            //var distId = $('#ddlDistrict option:selected').val();
            //var subdivId = $('#ddlSubDiv option:selected').val();
            var officeId = $('#hdnOfficeId').val();
            var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
            var roleId= $('#roleId').val();
            //var blockId = $('#ddlUrbRurBody option:selected').val();
            //var gpId = $('#ddlWardGp option:selected').val();
            var schemeId = $('#ddlSchemes').val();
            //var finYr = $('#ddlFinYear option:selected').val();
            //var instl = $('#ddlInstallment  option:selected').val();
            //var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
            var status = 8;
//
//            if (officeId === "" || schemeId === "" || checkerOfficeId === "") {
//                alert("Kindly Select Scheme and Beneficiary Verified By for searching beneficiaries");
//                return false;
//            }

            $('#benListDiv').removeClass("d-none").addClass("d-block");
            $('#beneficiaryListCitizen').DataTable().clear().destroy();
            table1 = $('#beneficiaryListCitizen').DataTable({
                "ajax": {
                    "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptmverbenDefectivecit=1&checkerOfficeId=" + checkerOfficeId + "&schemeId=" + schemeId + "&status=" + status+ "&roleId="+roleId,
                    "type": "POST"
                },
                dom: 'Bfrtip',
                "iDisplayLength": 50,
                'order': [[1, 'asc']],
                'responsive': true,
                "columns": [
                    {"data": "application_id", "width": "15%"},
                    {"data": "applicant_name", "width": "20%"},
                    {"data": "mobile", "width": "15%"},
                    {"data": "office_name", "width": "20%"},
                    {"data": "entrydate", "width": "10%"},
                    {"data": "remarks", "width": "15%"},
                    {"data": function (data, type, dataToSet) {
                            if (data.action_taken === 13 || data.action_taken === 15) {
                                return  "Defective";
                            } else {
                                return  "Rejected";
                            }
                        }},
                       {"data": function (data, type, dataToSet) {
                          return  "<a class=\"btn btn-primary btn-sm\" href=\"${pageContext.request.contextPath}/Reports/viewCitizenDocumentForUnfreeze.jsp?aid=" + data.application_id + "~1\" target=\"_blank\">View Application</a>";
                       }}

                    //,
//                                                                    {"data": function (data, type, dataToSet) {
//                                                                            return  "<button class=\"btn btn-primary btn-sm\" id=\"btnRaiseQuery\" name=\"btnRaiseQuery\" value=\"" + data.appl_id + "#" + data.full_name + "\">Unfreeze</button>";
//                                                                        }}
                ]

            });
            $('#beneficiaryListCitizen_wrapper').addClass("col-md-12");
            $("select[name='beneficiaryListCitizen_length']").removeClass("custom-select custom-select-sm ");

            $('#beneficiaryList_wrapper').addClass("col-md-12");
            $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");
        }


    </script>
    <%
            } else {
                response.sendRedirect("../User/userHome.jsp");
            }

        } else {
            response.sendRedirect("../login.jsp");
        }
    %>
</html>