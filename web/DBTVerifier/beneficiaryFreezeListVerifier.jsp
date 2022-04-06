<%-- 
    Document   : beneficiaryFreezeList
    Created on : 1 Jan, 2022, 1:54:10 PM
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
            String scheme_data_source = cm.getSchemeDataSource(schemeId);
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
        <!--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">-->
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
                                    <object id="pdfView" data="" type="application/pdf" width="100%" height="600">

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
                                    <h4 class="text-white">Freezed Beneficiary List:</h4>
                                </div>

                                <div class="card-body bg-light">
                                    <input type="hidden" id="ddlSchemes" name="ddlSchemes" value="<%=schemeId + "~" + scheme_code%>" />
                                    <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="<%=scheme_name%>" />
                                    <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >
                                    <!--                                    <div class="row">
                                                                            <div class="form-group col-md-3">
                                                                                <label for="ddlSchemes">Scheme</label>
                                                                                <div class="input-group mb-3">                                                                                
                                                                                    <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                    <%=ddlSchemes%>                                                    
                                </select>
                                <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
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
                    </div> -->

                                    <div class="row d-none" id="fileListDiv">
                                        <div class="col-md-12"> 
                                            <div class="card" >
                                                <div class="card-header">
                                                    <h4 class="text-primary" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                                                        <i class="fa fa-bars" aria-hidden="true">&nbsp;e-signed File List:</i></h4>
                                                </div>
                                                <div class="collapse" id="collapseExample">
                                                    <div class="card-body bg-page">
                                                        <div class="card-text">
                                                            <div class="row">
                                                                <% if (!scheme_data_source.equals("1")) {%>
                                                                <div class="input-group mb-3 col-md-12">
                                                                    <% } else { %>
                                                                    <div class="input-group mb-3 col-md-6"> 
                                                                        <% }%>
                                                                        <table id="fileList" class="table table-sm table-striped table-bordered " style="width:100%">
                                                                            <caption>File created by Self</caption>
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>Sl No.</th>
                                                                                    <th>Scheme Name</th>
                                                                                    <th>Beneficiary Count </th> 
                                                                                    <th>Freezed / Signed</th>
                                                                                    <th>Signed On</th>
                                                                                    <th>File</th>
                                                                                </tr>
                                                                            </thead>                                        
                                                                        </table>
                                                                    </div>
                                                                    <% if (scheme_data_source.equals("1")) {%>
                                                                    <div class="input-group mb-3 col-md-6">
                                                                        <table id="fileListSub" class="table table-sm table-striped table-bordered " style="width:100%">
                                                                            <caption>File signed by Maker</caption>
                                                                            <thead>
                                                                                <tr>
                                                                                    <th>Sl No.</th>
                                                                                    <th>Scheme Name</th>
                                                                                    <th>Beneficiary Count </th> 
                                                                                    <th>Maker Office</th>
                                                                                    <th>Signed On</th>
                                                                                    <th>File</th>
                                                                                </tr>
                                                                            </thead>                                        
                                                                        </table>
                                                                    </div>
                                                                    <% } %>
                                                                </div> 
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>                                
                                        </div>           
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="form-group col-md-12 required text-center">
                                                    <!--                                            <label for="domicile" class="font-weight-bold">Select option:</label>-->
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
                                        </div>
                                        <div class="row d-none" id="benListDiv">
                                            <div class="col-md-12"> 
                                                <div id="dept_route">
                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <div class="card">
                                                                <div class="card-body bg-dark ">
                                                                    <span class="text-white font-weight-bold">Freezed Beneficiary List (Uploaded by Department)</span>
                                                                </div> 
                                                            </div>
                                                        </div>                                                    
                                                    </div>
                                                    <div class="row">
                                                        <div class="input-group mb-3 col-md-12">
                                                            <table id="beneficiaryList" class="table table-sm table-striped table-bordered " style="width:100%">
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
                                                </div>
                                                <div id="citizen_route">
                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <div class="card">
                                                                <div class="card-body bg-dark ">
                                                                    <span class="text-white font-weight-bold">Freezed Beneficiary List (Uploaded by Citizen)</span>
                                                                </div> 
                                                            </div>
                                                        </div>                                                    
                                                    </div>
                                                    <div class="row">
                                                        <div class="input-group mb-3 col-md-12">
                                                            <table id="beneficiaryListCitizen" class="table table-sm table-striped table-bordered" style="width:100%">
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
                                                                        <!--                                                                <th>Recommendation</th>-->
                                                                        <th>Application</th>
                                                                        <!--                                                                <th>Action</th>-->
                                                                    </tr>
                                                                </thead>                                        
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row" id="divSchemeMapping"> 
                                                    <div class="input-group mb-3 col-md-4">
                                                        <button type="button" id="btnFreeze" name="btnFreeze" class="btn btn-success btn-block">
                                                            <strong>Esign Beneficiary</strong>
                                                        </button>
                                                    </div>
                                                    <div class="input-group mb-3 col-md-4">
                                                        <button class="btn btn-success btn-block" id="btnunFreeze" name="btnunFreeze">Unfreeze Beneficiary</button>
                                                    </div>
                                                    <div id="msgDiv" class="col-md-4"></div>
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

<!--<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.html5.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.print.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.buttons.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/jszip.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/pdfmake.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/vfs_fonts.js"></script>-->
    <script>
                                                            $(document).ready(function () {
                                                                $("#rd_route_citizen").attr("checked","true");
                                                                $("#myModal").on("hidden.bs.modal", function () {
                                                                    window.location.reload();
                                                                     
                                                                });
                                                                $("#dept_route").css("display", "none");
                                                                $("#citizen_route").css("display", "none");
                                                                $("#divSchemeMapping").css("display", "none");

                                                                if ($("#rd_route_dept").is(":checked")) {
                                                                    checkBeneficiaryRoute("1");
                                                                }
                                                                if ($("#rd_route_citizen").is(":checked")) {
                                                                    checkBeneficiaryRoute("2");

                                                                }

                                                                var table, table1, table2, table3;
                                                                var i = 1;
                                                                //   $("#ddlSchemes").on('change', function (e) {
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
                                                                                //return  "<button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" value=\"" + data.fid + "#" + data.type + "#" + data.application_id + "\">View File</button>";
                                                                                var frameSrc = "${pageContext.request.contextPath}/showPdf?fid=" + data.fid;
                                                                                return  "<a href=\"" + frameSrc + "\" class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" target=\"_blank\">View File</a>";

                                                                            }}
                                                                    ],
                                                                    "fnDrawCallback": function () {
                                                                        //some click events initilized here
                                                                        $('#fileListSub').DataTable().clear().destroy();
                                                                        table1 = $('#fileListSub').DataTable({
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
                                                                                        //return  "<button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" value=\"" + data.fid + "#" + data.type + "#" + data.application_id + "\">View File</button>";
                                                                                        var frameSrc = "${pageContext.request.contextPath}/showPdf?fid=" + data.fid;
                                                                                        return  "<a href=\"" + frameSrc + "\" class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\" target=\"_blank\">View File</a>";
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
                                                                            $('#pdfViewDiv1 object').attr('data', '');
                                                                            $('#pdfViewDiv1 object').attr('data', frameSrc);
                                                                            //$('#pdfViewDiv1 embed').attr('src', frameSrc);
                                                                            $('#myModal1').modal("show");
                                                                        });
                                                                    }

                                                                });
                                                                $('#fileList_wrapper').addClass("col-md-12");
                                                                $("select[name='fileList_length']").removeClass("custom-select custom-select-sm ");
                                                                $('#fileList tbody').on('click', 'button', function (e) {
                                                                    // console.log(this.value);
                                                                    var fid = this.value.split("#")[0];
                                                                    var type = this.value.split("#")[1];
                                                                    var applId = this.value.split("#")[2];

                                                                    //   var frameSrc = "${pageContext.request.contextPath}/EsignWorks?showPdf=1&fid=" + fid;
                                                                    var frameSrc = "${pageContext.request.contextPath}/showPdf?fid=" + fid;

                                                                    // alert(frameSrc);
                                                                    if (parseInt(type) === 1) {
                                                                        //var iframeElement = $("#myModal iframe");
                                                                        //iframeElement.attr('src', frameSrc);
                                                                        $('#eSignForm').html("");
                                                                        $('#eSignForm').removeClass("d-block").addClass("d-none");
                                                                        $('#pdfViewDiv').removeClass("d-none").addClass("d-block");
                                                                        $('#pdfViewDiv object').attr('data', '');
                                                                        $('#pdfViewDiv object').attr('data', frameSrc);
                                                                        //$('#pdfViewDiv embed').attr('src', frameSrc);
                                                                        $('#myModal').modal("show");
                                                                        $('#hdnEsignApplId').val(applId);
                                                                        $('#btnEsign').removeClass("d-none").addClass("d-block");
                                                                    }
//                                                                else if (parseInt(type) === 2) {
//                                                                    $('#eSignForm').html("");
//                                                                    $('#eSignForm').removeClass("d-block").addClass("d-none");
//                                                                    $('#pdfViewDiv').removeClass("d-none").addClass("d-block");
//                                                                    $('#pdfViewDiv object').attr('data', frameSrc);
//                                                                    //$('#pdfViewDiv embed').attr('src', frameSrc);
//                                                                    $('#myModal1').modal("show");
//                                                                    $('#hdnEsignApplId').val(applId);
//                                                                 //   $('#btnEsign').removeClass("d-none").addClass("d-block");
//                                                                } 
                                                                    else {
                                                                        //var iframeElement = $("#myModal1 iframe");
                                                                        //iframeElement.attr('src', frameSrc);
                                                                        $('#pdfViewDiv1 object').attr('data', '');
                                                                        $('#pdfViewDiv1 object').attr('data', frameSrc);
                                                                        //$('#pdfViewDiv1 embed').attr('src', frameSrc);
                                                                        $('#myModal1').modal("show");
                                                                    }

                                                                });
                                                                // });
                                                                // $('#btnSearch').on('click', function (e) {
                                                                //var distId = $('#ddlDistrict option:selected').val();
                                                                //var subdivId = $('#ddlSubDiv option:selected').val();
                                                                var officeId = $('#hdnOfficeId').val();
                                                                //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                                //var gpId = $('#ddlWardGp option:selected').val();
                                                                var schemeId = $('#ddlSchemes').val();
                                                                //var finYr = $('#ddlFinYear option:selected').val();
                                                                //var instl = $('#ddlInstallment  option:selected').val();
                                                                //var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
                                                                var status = 6;
                                                                if (officeId === "" || schemeId === "") {
                                                                    alert("Kindly Select Scheme for searching beneficiaries");
                                                                    return false;
                                                                }




                                                                $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                                $('#beneficiaryList').DataTable().clear().destroy();
                                                                table2 = $('#beneficiaryList').DataTable({
                                                                    "ajax": {
                                                                        "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptmverben=1&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status,
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
                                                                        table3 = $('#beneficiaryListCitizen').DataTable({
                                                                            "ajax": {
                                                                                "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptmverbenFreezzecit=1&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status,
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
                                                                                {"data": "full_name", "width": "15%"},
                                                                                {"data": "guardian_name", "width": "15%"},
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        ++i;
                                                                                        return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                                                                    }, "width": "25%"},
                                                                                {"data": "office_name"},
                                                                                {"data": "bank_account"},
                                                                                {"data": "ifsc"}
                                                                                ,
                                                                                {"data": function (data, type, dataToSet) {
                                                                                        return  "<a href=\"${pageContext.request.contextPath}/Reports/viewCitizenDocumentForUnfreeze.jsp?aid=" + data.appl_id + "~2\" target=\"_blank\"><button class=\"btn btn-primary btn-sm\" id=\"btnViewFile\" name=\"btnViewFile\">View Application</button></a>";
                                                                                    }}
                                                                                //,
//                                                                    {"data": function (data, type, dataToSet) {
//                                                                            return  "<button class=\"btn btn-primary btn-sm\" id=\"btnRaiseQuery\" name=\"btnRaiseQuery\" value=\"" + data.appl_id + "#" + data.full_name + "\">Unfreeze</button>";
//                                                                        }}
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

                                                                    }


                                                                });
                                                                $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                                $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");
                                                                $('#beneficiaryList tbody').on('click', 'button', function (e) {
                                                                    //console.log(this.value);

                                                                    var fileUrl = "${pageContext.request.contextPath}/ViewFile?fId=" + this.value;
                                                                    $('#pdfViewDiv2 object').attr('data', '');
                                                                    $('#pdfViewDiv2 object').attr('data', fileUrl);
                                                                    //$('#pdfViewDiv2 embed').attr('src', fileUrl);
                                                                    $('#myModal2').modal("show");
                                                                });
                                                                // });
                                                                // 
                                                                //Freeze Beneficiary List and Generate PDF
                                                                $('#btnFreeze').on('click', function (e) {

                                                                    //  var table = $('#beneficiaryListCitizen').DataTable();
                                                                    var officeId = $('#hdnOfficeId').val();
                                                                    //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                                    //var gpId = $('#ddlWardGp option:selected').val();
                                                                    var schemeId = $('#ddlSchemes').val();
                                                                    var schemeName = $('#ddlSchemesName').val();
                                                                    //var finYr = $('#ddlFinYear option:selected').val();
                                                                    //var instl = $('#ddlInstallment  option:selected').val();
                                                                    //var instlName = $('#ddlInstallmentName').val();
                                                                    var status = 6;
                                                                    var userflag = "verifier";
                                                                    if (officeId === "" || schemeId === "") {
                                                                        alert("Kindly Select Scheme for searching beneficiaries");
                                                                        return false;
                                                                    }


                                                                    var rows_selected = table2.column(0).checkboxes.selected();
                                                                    var rows_selected1 = table3.column(0).checkboxes.selected();
                                                                    var id = [];
                                                                    $.each(rows_selected, function (index, rowId) {
                                                                        var rowData = rows_selected[index];
                                                                        var b = table2
                                                                                .rows(function (idx, data, node) {
                                                                                    return data.rc_mem === rowData ? true : false;
                                                                                })
                                                                                .data();
                                                                        console.log(b[0]);
                                                                        id.push(b[0]);
                                                                    });
                                                                    $.each(rows_selected1, function (index, rowId) {
                                                                        var rowData = rows_selected1[index];
                                                                        var b = table3
                                                                                .rows(function (idx, data, node) {
                                                                                    return data.rc_mem === rowData ? true : false;
                                                                                })
                                                                                .data();
                                                                        console.log(b[0]);
                                                                        id.push(b[0]);
                                                                    });
                                                                    
                                                                    if (id.length === 0) {
                                                                        alert("No Beneficiary Selected");
                                                                        return false;
                                                                    }

                                                                    var json = JSON.stringify(id);

                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/GeneratePDFBen?argument_type=freezeBenList&argument=1&officeId=" + officeId + "&schemeId=" + schemeId + "&schemeName=" + schemeName + "&status=" + status + "&userflag=" + userflag,
                                                                        type: "POST",
                                                                        async: false,
                                                                        dataType: 'json',
                                                                        data: {selectedbenid: json},
                                                                        success: function (result) {
                                                                            var frameSrc = "";
                                                                            $('#pdfViewDiv').find('#pdfView').attr('data', '');
                                                                            // if (parseInt(result) > 0) {
                                                                            //var frameSrc = "${pageContext.request.contextPath}/GeneratePDFBen?argument_type=freezeBenList&argument=1&officeId=" + officeId + "&schemeId=" + schemeId + "&schemeName=" + schemeName + "&status=" + status + "&userflag=" + userflag + "&selectedbenid=" + json;
                                                                            frameSrc = "${pageContext.request.contextPath}/showPdf?fid=" + result;
                                                                            //var iframeElement = $("#myModal iframe");
                                                                            //console.log(frameSrc);
                                                                            //iframeElement.attr('src', frameSrc);
                                                                            //$('#myModal').show();
                                                                            //$('#myModal').modal("show");
                                                                            //$('#btnEsign').show();
                                                                            // alert(frameSrc);
                                                                            $('#eSignForm').html("");
                                                                            $('#eSignForm').removeClass("d-block").addClass("d-none");
                                                                            $('#pdfViewDiv').removeClass("d-none").addClass("d-block");
                                                                            $('#pdfViewDiv object').attr('data', '');
                                                                            $('#pdfViewDiv').find('#pdfView').attr('data', '');
                                                                            $('#pdfViewDiv').find('#pdfView').attr('data', frameSrc);
                                                                            //$(#myModal).find('#form')[0].reset();
                                                                            //$('#myModal').clone().modal();
                                                                                $("#myModal").modal("show");
                                                                            //$('#myModal').modal("show");
                                                                            $('#btnEsign').removeClass("d-none").addClass("d-block");

                                                                            // }
                                                                        }
                                                                    });

                                                                });

                                                                //Unfreeze Beneficiary List


                                                                //Freeze Beneficiary List and Generate PDF
                                                                $('#btnunFreeze').on('click', function (e) {
                                                                    //var table = $('#beneficiaryListCitizen').DataTable();
                                                                    var officeId = $('#hdnOfficeId').val();
                                                                    //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                                    //var gpId = $('#ddlWardGp option:selected').val();
                                                                    var schemeId = $('#ddlSchemes').val();
                                                                    var schemeName = $('#ddlSchemesName').val();
                                                                    //var finYr = $('#ddlFinYear option:selected').val();
                                                                    //var instl = $('#ddlInstallment  option:selected').val();
                                                                    //var instlName = $('#ddlInstallmentName').val();
                                                                    var status = 5;
                                                                    var userflag = "verifier";
                                                                    if (officeId === "" || schemeId === "") {
                                                                        alert("Kindly Select Scheme for searching beneficiaries");
                                                                        return false;
                                                                    }


                                                                    var rows_selected = table2.column(0).checkboxes.selected();
                                                                    var rows_selected1 = table3.column(0).checkboxes.selected();
//                                                                    var row = table2.row($(this).parents('tr'));
//                                                                    var row1 = table3.row($(this).parents('tr'));
                                                                    var id = [];
                                                                    $.each(rows_selected, function (index, rowId) {
                                                                        var rowData = rows_selected[index];
                                                                        var b = table2
                                                                                .rows(function (idx, data, node) {
                                                                                    return data.rc_mem === rowData ? true : false;
                                                                                })
                                                                                .data();
                                                                        console.log(b[0]);
                                                                        id.push(b[0]);
                                                                    });

                                                                    $.each(rows_selected1, function (index, rowId) {
                                                                        var rowData = rows_selected1[index];
                                                                        var b = table3
                                                                                .rows(function (idx, data, node) {
                                                                                    return data.rc_mem === rowData ? true : false;
                                                                                })
                                                                                .data();
                                                                        console.log(b[0]);
                                                                        id.push(b[0]);
                                                                    });
                                                                    if (id.length === 0) {
                                                                        alert("No Beneficiary Selected");
                                                                        return false;
                                                                    }

                                                                    var json = JSON.stringify(id);

                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/BeneficiaryUnFreeze?schemeId=" + schemeId + "&status=" + status,
                                                                        type: "POST",
                                                                        async: false,
                                                                        dataType: 'json',
                                                                        data: {json: json},
                                                                        success: function (result) {
                                                                            if (parseInt(result) > 0) {
                                                                                alert("Unfreezed Successfully");
                                                                                var route = "";
                                                                                if ($("#rd_route_dept").is(":checked")) {
                                                                                    route = "1";
                                                                                }
                                                                                if ($("#rd_route_citizen").is(":checked")) {
                                                                                    route = "2";

                                                                                }
                                                                                window.location.href = "${pageContext.request.contextPath}/DBTVerifier/beneficiaryFreezeListVerifier.jsp?route=" + route;
                                                                            }
                                                                        }
                                                                    });




                                                                });

                                                                // Esign Beneficiary List
                                                                $('#btnEsign').on('click', function (e) {
                                                                    var applId = $('#hdnEsignApplId').val();
                                                                    var frameSrc = "";
                                                                    if (applId !== '') {
                                                                        frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=2&serviceId=5&esignApplId=" + applId;
                                                                    } else {
                                                                        frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=2&serviceId=5";
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

                                                                // Esign Beneficiary List
                                                                /*$('#btnEsign1').on('click', function (e) {
                                                                 var frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=2&serviceId=5";
                                                                 var iframeElement = $("#myModal1 iframe");
                                                                 iframeElement.attr('src', frameSrc);
                                                                 });*/

                                                                //$(document).ready(function () {
                                                                //$('#ddlSchemes').select2();
                                                                //});

                                                            });
                                                            
                                                            
                                                            function checkBeneficiaryRoute(route) {
                                                                $("#divSchemeMapping").css("display", "block");
                                                                if (route === "1") {
                                                                    $("#dept_route").css("display", "block");
                                                                    $("#citizen_route").css("display", "none");

                                                                }
                                                                if (route === "2") {

                                                                    $("#citizen_route").css("display", "block");
                                                                    $("#dept_route").css("display", "none");
                                                                }
                                                            }

//                                                            $(document).ready(function () {
//                                                                $('#myModal').on('hidden.bs.modal', function () {
//                                                                    $('#pdfViewDiv object').attr('data', '');
//                                                                })
//                                                            });


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