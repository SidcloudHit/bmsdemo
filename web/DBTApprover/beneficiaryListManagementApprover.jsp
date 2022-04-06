<%-- 
    Document   : beneficiaryListManagementApprover
    Created on : Jan 21, 2021, 4:10:29 PM
    Author     : NIC-Arindam
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
                ddlOffice = cm.getChildOffice(schemeId, workflow, officeId, ben_office_id, "-Please Select-");
            } else {
                ddlOffice = cm.getChildOffice(schemeId, workflow, officeId, "-Please Select-");
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

        <!-- Modal for signed file -->
        <div id="myModal1" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel1" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Beneficiary List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="pdfViewDiv1" class="bg-light">
                                    <object data="" type="application/pdf" width="100%" height="600">

                                    </object>
                                    <!--<embed src="" type="application/pdf" width="100%" height="700px" />-->
                                </div> 
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <!--<input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal for unsigned files -->
        <div id="myModal" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-esign">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Beneficiary List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="pdfViewDiv" class="bg-light">
                                    <object data="" type="application/pdf" width="100%" height="600">

                                    </object>
                                    <!--<embed src="" type="application/pdf" width="100%" height="700px" />-->
                                </div>                            
                                <div id="eSignForm" class="bg-light d-none">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign &nbsp;&nbsp;</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="myModal2" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Beneficiary List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div id="pdfViewDiv2" class="bg-light mt-2">
                            <object data="" type="application/pdf" width="100%" height="600">

                            </object>
                            <!--<embed src="" type="application/pdf" width="100%" height="700px" />-->
                        </div>
                    </div>
                    <div class="modal-footer">                        
                    </div>
                </div>
            </div>
        </div>
        <div id="myModal3" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel3" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Raise a Query</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="col-md-12">
                                <form id="frmRaiseQuery" action="../RaiseQuery" method="post" > 
                                    <div class="row">
                                        <div class="form-group col-md-8">
                                            <label for="lblAction">Select Action</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                                </div>
                                                <select id="ddlAction" name="ddlAction" class="form-control" required="required">
                                                    <option value="">Please Select Action</option>
                                                    <option value="15">Defective</option>
                                                    <option value="16">Reject</option>                                                    
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <label for="txtActionRemarks">Remarks against Action</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-user fa-lg"></i></span>
                                                </div>
                                                <textarea id="txtActionRemarks" name="txtActionRemarks" class="form-control" maxlength="500" rows="5" id="comment" required="required"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <input type="hidden" id="hdnApplicationId" name="hdnApplicationId" value="" />
                                            <input type="hidden" id="hdnApplicantName" name="hdnApplicantName" value="" />  
                                            <input type="hidden" id="roleId" name="roleId" value="<%=roleId%>" />
                                            <button type="button" id="btnRaiseQuerySave" name="btnRaiseQuerySave" class="btn btn-primary">Save</button>
                                        </div>
                                    </div>
                                </form>
                            </div>                            
                        </div>

                    </div>
                    <div class="modal-footer">                        
                    </div>
                </div>
            </div>
        </div>
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
                                    <h4 class="text-white">Beneficiary List:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <div class="row"> 
                                        <div class="col-md-4">
                                            <!--                                            <div class="input-group mb-3 col-md-4">-->
                                            <a href="${pageContext.request.contextPath}/DBTApprover/beneficiaryFreezeListApprover.jsp" class="btn btn-success btn-block">
                                                View Freeze And e-sign Beneficiary List
                                            </a>
                                        </div>
                                        <div class="col-md-4">
                                            <a href="${pageContext.request.contextPath}/DBTApprover/beneficiaryDefectiveListApprover.jsp" class="btn btn-success btn-block">
                                                View Defective Beneficiary List
                                            </a>
                                        </div>
                                        <div class="col-md-4">
                                            <a href="${pageContext.request.contextPath}/DBTApprover/beneficiaryEsignListApprover.jsp" class="btn btn-success btn-block">
                                                View e-signed Beneficiary List
                                            </a>
                                        </div>
                                        <!--                                        </div>-->
                                    </div>
                                    <div class="row mt-5">
                                        <input type="hidden" id="ddlSchemes" name="ddlSchemes" value="<%=schemeId + "~" + scheme_code%>" />
                                        <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="<%=scheme_name%>" />
                                        <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >

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
                                        <div class="form-group col-md-4 required">
                                            <label for="domicile" class="font-weight-bold">Select option:</label>
                                            <div class="input-group">
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="rd_route" id="rd_route_dept" value="1" <% if (route.equals("1")) {%> checked="" <%}%> onclick="checkBeneficiaryRoute(this.value);">
                                                    <label class="form-check-label" for="inlineRadio2">Department Route</label>
                                                </div>
                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="rd_route" id="rd_route_citizen" value="2" <% if (route.equals("2")) {%> checked="" <%}%>  onclick="checkBeneficiaryRoute(this.value);">
                                                    <label class="form-check-label" for="inlineRadio2">Citizen Route</label>
                                                </div>
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


                                    <!--                                    <div class="row">
                                    
                                                                        </div>-->

                                    <div class="row d-none" id="benListDiv">
                                        <div class="col-md-12"> 
                                            <div id="dept_route">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="card">
                                                            <div class="card-body bg-dark ">
                                                                <span class="text-white font-weight-bold">Beneficiary List (Uploaded by Department)</span>
                                                            </div> 
                                                        </div>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
                                                    <div class="input-group mb-3 col-md-12">
                                                        <table id="beneficiaryList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                            <thead>
                                                                <tr>
                                                                    <th></th>
                                                                    <th>Beneficiary ID</th>
                                                                    <th>Beneficiary Name</th>
                                                                    <th>Guardian's Name</th>
                                                                    <th>Address</th>
                                                                    <th>Office Name</th>                                                        
                                                                    <th>Bank Account Number</th>
                                                                    <th>IFSC</th>
                                                                    <th>Recommendation</th>
                                                                </tr>
                                                            </thead>                                        
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="row" id="divSchemeMapping"> 
                                                    <div class="input-group mb-3 col-md-4">
                                                        <button type="button" id="btnFreeze" name="btnFreeze" class="btn btn-success btn-block">
                                                            <strong>Freeze Beneficiary</strong>
                                                        </button>
                                                    </div>

                                                </div>
                                            </div>
                                            <div id="citizen_route">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="card">
                                                            <div class="card-body bg-dark ">
                                                                <span class="text-white font-weight-bold">Beneficiary List (Uploaded by Citizen)</span>
                                                            </div> 
                                                        </div>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
                                                    <div class="input-group mb-3 col-md-12">
                                                        <table id="beneficiaryListCitizen" class="table table-sm table-striped table-bordered" style="width:100%">
                                                            <thead>
                                                                <tr>
                                                                    <!--                                                                    <th></th>-->
                                                                    <th>Beneficiary ID</th>
                                                                    <th>Beneficiary Name</th>
                                                                    <th>Guardian's Name</th>
                                                                    <th>Address</th>   
                                                                    <th>Office Name</th>                                                        
                                                                    <th>Bank Account Number</th>
                                                                    <th>IFSC</th>
                                                                    <!--                                                                <th>Recommendation</th>-->
                                                                    <th>Application</th>
                                                                    <th>Action</th>
                                                                </tr>
                                                            </thead>                                        
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <!--                                            <div class="row" id="divSchemeMapping"> 
                                                                                            <div class="input-group mb-3 col-md-4">
                                                                                                <button type="button" id="btnFreeze" name="btnFreeze" class="btn btn-success btn-block">
                                                                                                    <strong>Freeze Beneficiary</strong>
                                                                                                </button>
                                                                                            </div>
                                                                                            <div class="input-group mb-3 col-md-4">
                                                                                                <a href="${pageContext.request.contextPath}/DBTApprover/beneficiaryFreezeListApprover.jsp" class="btn btn-success btn-block">
                                                                                                    Freezed Beneficiary List
                                                                                                </a>
                                                                                            </div>
                                                                                            <div id="msgDiv" class="col-md-4"></div>
                                                                                        </div>-->
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
    </html>
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
                                                            $("#dept_route").css("display", "none");
                                                            $("#citizen_route").css("display", "none");
                                                            if ($("#rd_route_dept").is(":checked")) {
                                                                checkBeneficiaryRoute("1");

                                                            }
                                                            if ($("#rd_route_citizen").is(":checked")) {
                                                                checkBeneficiaryRoute("2");

                                                            }

                                                            $('#ddlOfficeChecker').select2();


                                                            var table, table1;
                                                            var i = 1;

                                                            $("#ddlSchemes").on('change', function (e) {
                                                                var officeId = $('#hdnOfficeId').val();
                                                                var schemeId = $('#ddlSchemes').val();
                                                                var j = 0;
                                                                $('#fileListDiv').removeClass("d-none").addClass("d-block");
                                                                $('#fileList').DataTable().clear().destroy();
                                                                table = $('#fileList').DataTable({
                                                                    "ajax": {
                                                                        url: "${pageContext.request.contextPath}/EsignWorks?benFileList=1&officeId=" + officeId + "&schemeId=" + schemeId,
                                                                        "type": "POST"
                                                                    },
                                                                    "iDisplayLength": 50,
                                                                    'order': [[1, 'asc']],
                                                                    'responsive': true,
                                                                    "columns": [
                                                                        {"data": function (data, type, dataToSet) {
                                                                                return  ++j;
                                                                            }},
                                                                        {"data": "scheme_name"},
                                                                        {"data": "final_beneficiary_count"},
                                                                        {"data": function (data, type, dataToSet) {
                                                                                return data.type === 1 ? "Freezed" : "Signed";
                                                                            }},
                                                                        {"data": "txn_timestamp"},
                                                                        {"data": function (data, type, dataToSet) {
                                                                                return  "<button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" value=\"" + data.fid + "#" + data.type + "#" + data.application_id + "\">View File</button>";
                                                                            }}
                                                                    ],
                                                                    "fnDrawCallback": function () {
                                                                        //some click events initilized here
                                                                        $('#fileListSub').DataTable().clear().destroy();
                                                                        table = $('#fileListSub').DataTable({
                                                                            "ajax": {
                                                                                url: "${pageContext.request.contextPath}/EsignWorks?getFileListSigned=1&userflag=verifier&purpose=1&officeId=" + officeId + "&schemeId=" + schemeId,
                                                                                "type": "POST"
                                                                            },
                                                                            "iDisplayLength": 50,
                                                                            'order': [[1, 'asc']],
                                                                            'responsive': true,
                                                                            "columns": [
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        return  ++j;
                                                                                    }},
                                                                                {"data": "scheme_name"},
                                                                                {"data": "final_beneficiary_count"},
                                                                                //{"data": function (data, type, dataToSet) {
                                                                                //       return data.type === 1 ? "Freezed" : "Signed";
                                                                                //    }},
                                                                                {"data": "office_name"},
                                                                                {"data": "txn_timestamp"},
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" value=\"" + data.fid + "#" + data.type + "#" + data.application_id + "\">View File</button>";
                                                                                    }}
                                                                            ]

                                                                        });
                                                                        $('#fileListSub_wrapper').addClass("col-md-12");
                                                                        $("select[name='fileListSub_length']").removeClass("custom-select custom-select-sm ");

                                                                        $('#fileListSub tbody').on('click', 'button', function (e) {
                                                                            //console.log(this.value);
                                                                            var fid = this.value.split("#")[0];
                                                                            var type = this.value.split("#")[1];
                                                                            var applId = this.value.split("#")[2];
                                                                            var frameSrc = "${pageContext.request.contextPath}/EsignWorks?showPdf=1&fid=" + fid;

                                                                            //var iframeElement = $("#myModal1 iframe");
                                                                            //iframeElement.attr('src', frameSrc);
                                                                            $('#pdfViewDiv1 object').attr('data', frameSrc);
                                                                            //$('#pdfViewDiv1 embed').attr('src', frameSrc);
                                                                            $('#myModal1').modal("show");


                                                                        });

                                                                    }

                                                                });
                                                                $('#fileList_wrapper').addClass("col-md-12");
                                                                $("select[name='fileList_length']").removeClass("custom-select custom-select-sm ");

                                                                $('#fileList tbody').on('click', 'button', function (e) {
                                                                    //console.log(this.value);
                                                                    var fid = this.value.split("#")[0];
                                                                    var type = this.value.split("#")[1];
                                                                    var applId = this.value.split("#")[2];
                                                                    var frameSrc = "${pageContext.request.contextPath}/EsignWorks?showPdf=1&fid=" + fid;
                                                                    if (parseInt(type) === 1) {
                                                                        //var iframeElement = $("#myModal iframe");
                                                                        //iframeElement.attr('src', frameSrc);
                                                                        $('#eSignForm').html("");
                                                                        $('#eSignForm').removeClass("d-block").addClass("d-none");
                                                                        $('#pdfViewDiv').removeClass("d-none").addClass("d-block");
                                                                        $('#pdfViewDiv object').attr('data', frameSrc);
                                                                        //$('#pdfViewDiv embed').attr('src', frameSrc);
                                                                        $('#myModal').modal("show");
                                                                        $('#hdnEsignApplId').val(applId);
                                                                        $('#btnEsign').removeClass("d-none").addClass("d-block");
                                                                    } else {
                                                                        //var iframeElement = $("#myModal1 iframe");
                                                                        //iframeElement.attr('src', frameSrc);
                                                                        $('#pdfViewDiv1 object').attr('data', frameSrc);
                                                                        //$('#pdfViewDiv1 embed').attr('src', frameSrc);
                                                                        $('#myModal1').modal("show");
                                                                    }

                                                                });
                                                            });



                                                            $('#btnSearch').on('click', function (e) {
                                                                var option = document.getElementsByName('rd_route');

                                                                if (!(option[0].checked || option[1].checked)) {
                                                                    alert("Please Select Your Option");
                                                                    return false;
                                                                }
                                                                //var distId = $('#ddlDistrict option:selected').val();
                                                                //var subdivId = $('#ddlSubDiv option:selected').val();
                                                                var officeId = $('#hdnOfficeId').val();
                                                                var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                                                                //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                                //var gpId = $('#ddlWardGp option:selected').val();
                                                                var schemeId = $('#ddlSchemes').val();
                                                                //var finYr = $('#ddlFinYear option:selected').val();
                                                                //var instl = $('#ddlInstallment  option:selected').val();
                                                                //var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
                                                                var status = 7;
                                                                if (officeId === "" || schemeId === "" || checkerOfficeId === "") {
                                                                    alert("Kindly Select Scheme and Beneficiary Verified By for searching beneficiaries");
                                                                    return false;
                                                                }


                                                                $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                                $('#beneficiaryList').DataTable().clear().destroy();
                                                                table = $('#beneficiaryList').DataTable({
                                                                    "ajax": {
                                                                        "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptmverben=1&officeId=" + officeId + "&checkerOfficeId=" + checkerOfficeId + "&schemeId=" + schemeId + "&status=" + status,
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
                                                                        {"data": "rc_mem", "width": "5%"},
                                                                        {"data": "rc_mem", "width": "5%"},
                                                                        /*{"data": function (data, type, dataToSet) {
                                                                         return  data.rc_mem + "<input type=\"hidden\" id=\"hdnBenId_" + i + "\" name=\"hdnBenId" + i + "\" value=\"" + data.rc_mem + "\" />" + i++;
                                                                         }},*/
                                                                        {"data": "full_name", "width": "15%"},
                                                                        {"data": "guardian_name", "width": "15%"},
                                                                        {"data": function (data, type, dataToSet) {
                                                                                ++i;
                                                                                return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                                                            }, "width": "25%"},
                                                                        {"data": "office_name"},
                                                                        //{"data": "pmkisan_id"},
                                                                        {"data": "bank_account"},
                                                                        {"data": "ifsc"},
                                                                        {"data": function (data, type, dataToSet) {
                                                                                if (parseInt(data.recomendation_file_id) > 0) {
                                                                                    return  "<button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" value=\"" + data.recomendation_file_id + "\">View File</button>";
                                                                                } else {
                                                                                    return null;
                                                                                }

                                                                            }, "width": "5%"}
                                                                    ],
                                                                    "fnDrawCallback": function () {

                                                                        $('#beneficiaryListCitizen').DataTable().clear().destroy();
                                                                        table1 = $('#beneficiaryListCitizen').DataTable({
                                                                            "ajax": {
                                                                                "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptmverbencit=1&checkerOfficeId=" + checkerOfficeId + "&schemeId=" + schemeId + "&status=" + status,
                                                                                "type": "POST"
                                                                            },
//                                                                            "columnDefs": [
//                                                                                {
//                                                                                    'targets': 0,
//                                                                                    'className': 'select-checkbox',
//                                                                                    'checkboxes': {
//                                                                                        'selectRow': true
//                                                                                    }
//                                                                                }
//                                                                            ],
//                                                                            "select": {
//                                                                                'style': 'multi'
//                                                                            },
                                                                            "iDisplayLength": 50,
                                                                            'order': [[1, 'asc']],
                                                                            'responsive': true,
                                                                            "columns": [
//                                                                                {"data": "rc_mem", "width": "5%"},
                                                                                {"data": "rc_mem", "width": "5%"},
                                                                                {"data": "full_name", "width": "15%"},
                                                                                {"data": "guardian_name", "width": "15%"},
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        ++i;
                                                                                        return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                                                                    }, "width": "25%"},
                                                                                {"data": "office_name"},
                                                                                {"data": "bank_account"},
                                                                                {"data": "ifsc"},
//                                                                            {"data": function (data, type, dataToSet) {
//                                                                                    if (parseInt(data.recomendation_file_id) > 0) {
//                                                                                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" value=\"" + data.recomendation_file_id + "\">View File</button>";
//                                                                                    } else {
//                                                                                        return null;
//                                                                                    }
//
//                                                                                }, "width": "5%"},
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        return  "<a href=\"${pageContext.request.contextPath}/Reports/viewCitizenDocuments.jsp?aid=" + data.appl_id + "&checkerOfficeId=" + checkerOfficeId + "\"><button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\">View Application</button></a>";
                                                                                    }},
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnRaiseQuery\" name=\"btnRaiseQuery\" value=\"" + data.appl_id + "#" + data.full_name + "\">Raise Query</button>";
                                                                                    }}
                                                                            ]

                                                                        });
                                                                        $('#beneficiaryListCitizen_wrapper').addClass("col-md-12");
                                                                        $("select[name='beneficiaryListCitizen_length']").removeClass("custom-select custom-select-sm ");
                                                                        $('#beneficiaryListCitizen tbody').on('click', 'button', function (e) {
                                                                            var btnName = this.name;

                                                                            if (btnName === "btnRaiseQuery") {
                                                                                var params = this.value.split("#");

                                                                                $('#myModal3').modal("show");
                                                                                $('#hdnApplicationId').val(params[0]);
                                                                                $('#hdnApplicantName').val(params[1]);
                                                                            }
                                                                        });
                                                                    },
                                                                    "dom": 'Bfrtip',
                                                                    "buttons": [
                                                                        'copyHtml5',
                                                                        'excelHtml5',
                                                                        'csvHtml5',
                                                                        'pdfHtml5'
                                                                    ]


                                                                });
                                                                $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                                $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");

                                                                $('#beneficiaryList tbody').on('click', 'button', function (e) {
                                                                    //console.log(this.value);

                                                                    var fileUrl = "${pageContext.request.contextPath}/ViewFile?fId=" + this.value;
                                                                    $('#pdfViewDiv2 object').attr('data', fileUrl);
                                                                    //$('#pdfViewDiv2 embed').attr('src', fileUrl);
                                                                    $('#myModal2').modal("show");


                                                                });

                                                            });

                                                            //Freeze Beneficiary List and Generate PDF
                                                            $('#btnFreeze').on('click', function (e) {
                                                                var officeId = $('#hdnOfficeId').val();
                                                                var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                                                                //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                                //var gpId = $('#ddlWardGp option:selected').val();
                                                                var schemeId = $('#ddlSchemes').val();
                                                                var schemeName = $('#ddlSchemesName').val();
                                                                //var finYr = $('#ddlFinYear option:selected').val();
                                                                //var instl = $('#ddlInstallment  option:selected').val();
                                                                //var instlName = $('#ddlInstallmentName').val();
                                                                var status = 8;
                                                                var userflag = "approver";
                                                                if (officeId === "" || schemeId === "") {
                                                                    alert("Kindly Select Scheme for searching beneficiaries");
                                                                    return false;
                                                                }


                                                                var rows_selected = table.column(0).checkboxes.selected();
                                                                //    var rows_selected1 = table1.column(0).checkboxes.selected();
                                                                var row = table.row($(this).parents('tr'));
                                                                //    var row1 = table1.row($(this).parents('tr'));
                                                                var id = [];
                                                                $.each(rows_selected, function (index, rowId) {
                                                                    var rowData = rows_selected[index];
                                                                    var b = table
                                                                            .rows(function (idx, data, node) {
                                                                                return data.rc_mem === rowData ? true : false;
                                                                            })
                                                                            .data();
                                                                    console.log(b[0]);
                                                                    id.push(b[0]);
                                                                });
//
//                                                                $.each(rows_selected1, function (index, rowId) {
//                                                                    var rowData = rows_selected1[index];
//                                                                    var b = table1
//                                                                            .rows(function (idx, data, node) {
//                                                                                return data.rc_mem === rowData ? true : false;
//                                                                            })
//                                                                            .data();
//                                                                    console.log(b[0]);
//                                                                    id.push(b[0]);
//                                                                });

                                                                if (id.length === 0) {
                                                                    alert("No Beneficiary Selected");
                                                                    return false;
                                                                }

                                                                var json = JSON.stringify(id);

                                                                //var json = JSON.stringify(b);
                                                                //console.log(id);
                                                                //console.log(json);
                                                                $.ajax({
                                                                    url: "${pageContext.request.contextPath}/BeneficiaryFreeze?schemeId=" + schemeId + "&status=" + status,
                                                                    type: "POST",
                                                                    async: false,
                                                                    dataType: 'json',
                                                                    data: {json: json},
                                                                    success: function (result) {
                                                                        if (parseInt(result) > 0) {
                                                                            alert("Freezed Successfully");
                                                                            table.row(row).remove().draw();
                                                                           // table1.row(row1).remove().draw();
                                                                            //var frameSrc = "${pageContext.request.contextPath}/GeneratePDFBen?argument_type=freezeBenList&argument=1&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status + "&userflag=" + userflag + "&checkerofficeid=" + checkerOfficeId; //&schemeName=" + schemeName + "
                                                                            //var iframeElement = $("#myModal iframe");
                                                                            //console.log(frameSrc);
                                                                            //iframeElement.attr('src', frameSrc);
                                                                            //$('#myModal').show();
                                                                            //$('#myModal').modal("show");
                                                                            //$('#btnEsign').show();

//                                                                        $('#eSignForm').html("");
//                                                                        $('#eSignForm').removeClass("d-block").addClass("d-none");
//                                                                        $('#pdfViewDiv').removeClass("d-none").addClass("d-block");
//                                                                        $('#pdfViewDiv object').attr('data', frameSrc);
//                                                                        //$('#pdfViewDiv embed').attr('src', frameSrc);
//                                                                        $('#myModal').modal("show");
//                                                                        $('#btnEsign').removeClass("d-none").addClass("d-block");
                                                                        }
                                                                    }
                                                                });




                                                            });

                                                            // Esign Beneficiary List
                                                            $('#btnEsign').on('click', function (e) {
                                                                var applId = $('#hdnEsignApplId').val();
                                                                var frameSrc = "";
                                                                if (applId !== '') {
                                                                    frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=2&serviceId=6&esignApplId=" + applId;
                                                                } else {
                                                                    frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=2&serviceId=6";
                                                                }

                                                                //var iframeElement = $("#myModal iframe");
                                                                //iframeElement.attr('src', frameSrc);

                                                                $('#pdfViewDiv object').attr('data', '');
                                                                //$('#pdfViewDiv embed').attr('src', '');
                                                                $('#pdfViewDiv').removeClass("d-block").addClass("d-none");

                                                                $('#eSignForm').removeClass("d-none").addClass("d-block");
                                                                //$('#eSignForm').html(frameSrc);
                                                                $.get(frameSrc, function (data) {
                                                                    $('#eSignForm').append(data);
                                                                });

                                                                $('#myModal').modal("show");
                                                                $('#btnEsign').removeClass("d-block").addClass("d-none");
                                                            });


                                                            $('#btnRaiseQuerySave').on('click', function (e) {
                                                                var schemeId = $('#ddlSchemes').val();
                                                                //var schemeName = $('#ddlSchemesName').val();
                                                                var applicationId = $('#hdnApplicationId').val();
                                                                var applicantName = $('#hdnApplicantName').val();
                                                                var action = $('#ddlAction option:selected').val();
                                                                var actionRemarks = $('#txtActionRemarks').val();
                                                                var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();

                                                                if (action === "" || actionRemarks === "") {
                                                                    alert("Kindly Select Action and Remarks against Action");
                                                                    return false;
                                                                }
                                                                var roleId = $('#roleId').val();


                                                                $.ajax({
                                                                    url: "${pageContext.request.contextPath}/RaiseQuery?raiseQuery=1&checkerOffice=" + checkerOfficeId + "&schemeId=" + schemeId + "&applicationId=" + applicationId + "&applicantName=" + applicantName + "&actionRemarks=" + actionRemarks + "&action=" + action,
                                                                    type: "POST",
                                                                    async: false,
                                                                    dataType: "text",
                                                                    success: function (result) {
                                                                        if (parseInt(result) > 0) {
                                                                            alert("Query Raised Successfully");
                                                                            $('#myModal3').modal("hide");
                                                                            $('#btnSearch').click();
                                                                            window.location.href = "${pageContext.request.contextPath}/DBTApprover/beneficiaryListManagementApprover.jsp?route=2&ben_office_id=" + ben_office_id;

                                                                        }
                                                                    }
                                                                });
                                                            });
                                                            // Esign Beneficiary List
                                                            /*$('#btnEsign1').on('click', function (e) {
                                                             var frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=2&serviceId=6";
                                                             var iframeElement = $("#myModal1 iframe");
                                                             iframeElement.attr('src', frameSrc);
                                                             });*/

                                                            //$(document).ready(function () {
                                                            // $('#ddlSchemes').select2();
                                                            //});

                                                        });
                                                        function checkBeneficiaryRoute(route) {
                                                            var ben_office_id = $('#ddlOfficeChecker option:selected').val();
                                                            if (ben_office_id !== "") {
                                                                //  $('#btnSearch').trigger('click');
                                                                document.getElementById("btnSearch").click();
                                                            }

                                                            if (route === "1") {
                                                                $("#dept_route").css("display", "block");
                                                                $("#citizen_route").css("display", "none");
                                                            }
                                                            if (route === "2") {

                                                                $("#citizen_route").css("display", "block");
                                                                $("#dept_route").css("display", "none");
                                                            }
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
