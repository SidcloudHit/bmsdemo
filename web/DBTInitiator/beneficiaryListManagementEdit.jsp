<%-- 
    Document   : beneficiaryListManagementEdit
    Created on : 9 Dec, 2021, 12:37:22 PM
    Author     : acer
--%>

<%@page import="java.util.Base64"%>
<%@page import="Security.SecurityClass"%>
<%@page import="java.security.SecureRandom"%>
<%@page import="Master.DBTBeneficiary"%>
<%@page import="Master.DBTBeneficiary"%>
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
    SessionPackage.LoginClass loginObj = null;

    if (session.getAttribute("loginInfo") != null) {

        loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

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
            String ddlScheme = "", ddlDist = "", ddlOffice = "";

            //ddlSchemes = cm.getCashSchemes("-Please Select-", deptId);//cm.getSchemes("-Please Select-");          
            ddlScheme = cm.getSchemes("-Please Select-", schemeId);

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
        <!-- Modal for account update -->
        <div id="myModal0" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">

                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Update Bank Account Details</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light" id="loadModalBody">

                        <!--                        <form id="frmManageAccount" action="#"  method="post" enctype="multipart/form-data"> -->

                        <div class="row">
                            <div class="form-group col-md-6">

                                <label for="txtUserName">Bank Name</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                    </div>

                                    <select id="ddlBank" name="ddlBank" class="form-control form-control-sm"  onchange="setSelectedName(this.id);">

                                    </select>
                                    <input type="hidden" id="ddlBankName" name="ddlBankName" value="" />
                                </div>
                            </div>
                            <div class="form-group col-md-6">
                                <label for="txtUserName">Account Number</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                    </div>
                                    <input type="text" id="txtAccount" name="txtAccount" size="120" class="form-control form-control-sm" placeholder="Type Account Number"  required="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label for="txtUserName">Confirm Account Number</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                    </div>
                                    <!--<input type="hidden" id="hdnUserId" name="hdnUserId" value="" />-->
                                    <input type="password" id="txtConfirrmAccount" name="txtConfirrmAccount" size="120"   class="form-control form-control-sm" placeholder="Confirm Account Number" required="required" /><br/>

                                    <!--                                                             <div style="margin-top: 7px;" id="CheckAccountMatch"></div>-->
                                </div>                                
                            </div>

                            <div class="form-group col-md-6">
                                <label for="txtEmail">IFSC</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                    </div>
                                    <!--<input type="hidden" id="hdnUserId" name="hdnUserId" value="" />-->
                                    <input type="text" id="txtIfsc" name="txtIfsc"  class="form-control form-control-sm" placeholder="Type your IFSC Code"  required="required"/>
                                </div>

                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-12">
                                <div id="CheckAccountMatch"></div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label for="txtAccUpdateRemarks">Remarks (reason for Update)</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-user fa-lg"></i></span>
                                    </div>
                                    <textarea id="txtAccUpdateRemarks" name="txtAccUpdateRemarks" class="form-control" maxlength="500" rows="5" id="comment" required="required"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <!--<div class="col-md-6" >
                                <img id="preview_photo" src="" alt="" height="100px" width="100px" style="border: 1px solid black;" class=" d-none mx-auto"/>
                            </div>-->
                            <div class="col-md-6">
                                <button type="button" id="updateAccInfo" name="updateAccInfo" class="btn btn-primary">Update Details</button>

                            </div>
                        </div>

                        <!--                        </form>    -->


                    </div>
                    <div class="modal-footer">
                        <!--                        <input id="hiddenValue" name="hiddenValue" type="hidden" value="" />-->
                        <input type="hidden" id="hdnRcAcc" name="hdnRcAcc" value="" />

                        <!--                        <input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />-->

                        <!--                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal for status update -->
        <div id="myModal2" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">

                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Update Beneficiary Status</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light" id="loadModalBody">

                        <div class="row">
                            <div class="form-group col-md-8">
                                <label for="lblAction">Select Action</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                    </div>
                                    <select id="ddlStatus" name="ddlStatus" class="form-control" required="required">
                                        <option value="">Please Select Status</option>
                                        <option value="11">Beneficiary Disabled</option>
                                        <option value="12">Beneficiary Deceased</option>                                         
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label for="txtStatusUpdateRemarks">Remarks (reason for Update)</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-user fa-lg"></i></span>
                                    </div>
                                    <textarea id="txtStatusUpdateRemarks" name="txtStatusUpdateRemarks" class="form-control" maxlength="500" rows="5" id="comment" required="required"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <!--<div class="col-md-6" >
                                <img id="preview_photo" src="" alt="" height="100px" width="100px" style="border: 1px solid black;" class=" d-none mx-auto"/>
                            </div>-->
                            <div class="col-md-6">
                                <button type="button" id="updateStatusInfo" name="updateStatusInfo" class="btn btn-primary">Update Details</button>

                            </div>
                        </div>

                        <!--                        </form>    -->


                    </div>
                    <div class="modal-footer">                        
                        <input type="hidden" id="hdnRcStatus" name="hdnRcStatus" value="" />

                        <!--                        <input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />-->

                        <!--                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>

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
                                    <h4 class="text-white">Verified Beneficiary List:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <div class="row">
                                        <div class="form-group col-md-3">
                                            <label for="ddlSchemes">Scheme</label>
                                            <div class="input-group mb-3">                                                                                
                                                <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                                    <%=ddlScheme%>                                                    
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
                                    </div> 

                                    <%
                                        String qrystr = "", err = "";
                                        if (request.getParameter("reqs") != null && request.getParameter("err") != null) {
                                            qrystr = SecurityClass.stripXSS(request.getParameter("reqs"));
                                            byte[] decodedURL = Base64.getUrlDecoder().decode(qrystr.getBytes("utf-8"));
                                            qrystr = new String(decodedURL);
                                            err = SecurityClass.stripXSS(request.getParameter("err"));
                                            byte[] decodedURL2 = Base64.getUrlDecoder().decode(err.getBytes("utf-8"));
                                            err = new String(decodedURL2);

                                            if (!err.equals("")) {
                                    %>
                                    <div class="alert alert-danger alert-dismissible ml-2 mr-2 mt-1 mb-1">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <strong>Alert!</strong> <%= qrystr%>
                                    </div>                      

                                    <%
                                    } else {

                                    %>
                                    <div class="alert alert-success alert-dismissible ml-2 mr-2 mt-1 mb-1">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <strong>Info!</strong> <%= qrystr%>
                                    </div>
                                    <% }
                                        }
                                    %>
                                    <div class="row d-none mt-2" id="benListDiv">
                                        <div class="col-md-12"> 
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="card">
                                                        <div class="card-body bg-dark ">
                                                            <span class="text-white font-weight-bold">Beneficiary List</span>
                                                        </div> 
                                                    </div>
                                                </div>                                                    
                                            </div>
                                            <div class="row">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="beneficiaryList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                        <thead>
                                                            <tr>
                                                                <!--                                                                <th></th>-->
                                                                <th>Beneficiary ID</th>
                                                                <th>Beneficiary Name</th>
                                                                <th>Guardian's Name</th>
                                                                <th>Address</th>                                                        
                                                                <th>Bank Account Number</th>
                                                                <th>IFSC</th>
                                                                <th>Edit/Update</th>
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

    <%
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        int myInt = sr.nextInt(1000000);
        String reqnumber = String.format("%06d", myInt);

    %>   

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
                                                        var i = 0;
                                                        $('#btnSearch').on('click', function (e) {

                                                            var officeId = $('#hdnOfficeId').val();
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var schemeName = $('#ddlSchemesName').val();
                                                            var status = 2;
                                                            if (officeId === "" || schemeId === "") {
                                                                alert("Kindly Select Scheme for searching beneficiaries");
                                                                return false;
                                                            }

                                                            $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                            $('#beneficiaryList').DataTable().clear().destroy();
                                                            table = $('#beneficiaryList').DataTable({
                                                                "ajax": {
                                                                    "url": "${pageContext.request.contextPath}/BeneficiaryDataEdit?dbtrptmben=1&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status,
                                                                    "type": "POST"
                                                                },
//                                                                "columnDefs": [
//                                                                    {
//                                                                        'targets': 0,
//                                                                        'className': 'select-checkbox',
//                                                                        'checkboxes': {
//                                                                            'selectRow': true
//                                                                        }
//                                                                    }
//                                                                ],
//                                                                "select": {
//                                                                    'style': 'multi'
//                                                                },
                                                                "iDisplayLength": 50,
                                                                'order': [[1, 'asc']],
                                                                'responsive': true,
                                                                "columns": [
                                                                    //{"data": "rc_mem", "width": "5%"},
                                                                    {"data": "rc_mem", "width": "5%"},
                                                                    /*{"data": function (data, type, dataToSet) {
                                                                     return  data.rc_mem + "<input type=\"hidden\" id=\"hdnBenId_" + i + "\" name=\"hdnBenId" + i + "\" value=\"" + data.rc_mem + "\" />" + i++;
                                                                     }},*/
                                                                    {"data": "full_name", "width": "15%"},
                                                                    {"data": "guardian_name", "width": "15%"},
                                                                    //{"data": "office_name"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            ++i;
                                                                            return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                                                        }, "width": "25%"},
                                                                    //{"data": "pmkisan_id"},
                                                                    {"data": "bank_account"},
                                                                    {"data": "ifsc"},
//                                                                    {"data": function (data, type, dataToSet) {
//                                                                            return ' <form method="post" action="../BeneficiaryDataRecordUpdate"> \n\
//                                                                         <input type="hidden" name="dept_code" value=""> \n\
//                                                                         <input type="hidden" name="userid" value="">  \n\
//                                                                         <input type="hidden" name="beneficiary_category" value="3"> \n\
//                                                                         <input type="hidden" name="beneficiary_sub_category" value="4">\n\
//                                                                         <input type="hidden" name="reqnumber" value="<%= reqnumber%>"> \n\
//                                                                         <input type="hidden" name="fullname"  value="' + data.full_name + '">\n\
//                                                                         <input type="hidden" name="uniqueid" value="' + data['tempid'] + '">    \n\
//                                                                         <input type="hidden" name="rc" value="' + data.rc_mem.substring(0, 12) + '">\n\
//                                                                         <input type="hidden" name="dob" value="">\n\
//                                                                         <input type="hidden" name="gender" value="' + data['gender'] + '">    \n\
//                                                                         <input type="hidden" name="caste"  value="' + data['caste'] + '">\n\
//                                                                         <input type="hidden" name="address_lgd_gp_vc_ward_code" value=  "' + data['postrcvalidation_gplgdcode'] + '"> \n\
//                                                                         <input type="hidden" name="areaname" value="">\n\
//                                                                         <input type="hidden" name="policestation" value="">\n\
//                                                                         <input type="hidden" name="postoffice" value=""   >    \n\
//                                                                         <input type="hidden" name="pincode" value="">\n\
//                                                                         <input type="hidden" name="bankacno" value="' + data.bank_account + '">\n\
//                                                                         <input type="hidden" name="ifsc_code" value="' + data.ifsc + '">    \n\
//                                                                         <input type="hidden" name="purpose" value="2"> \n\
//                                                                         <input type="hidden" name="remarks" value=""> \n\
//                                                                         <input type="hidden" name="email" value="">\n\
//                                                                         <input type="hidden" name="mobile" value="">  \n\
//                                                                         <input type="hidden" name="additional_param1" value="' + schemeId.split('~')[1] + '">\n\
//                                                                         <input type="hidden" name="additional_param2" value="' + schemeId + '">\n\
//                                                                         <input type="hidden" name="schemename" value="' + schemeName + '">\n\
//                                                                         <input type="submit" name="submit" value="Edit Beneficiary Data" class="btn btn-primary"  onclick="return confirm(\'You will redirect to  RC Verify page.Are you sure you want to continue?\')"/>\n\
//                                                                         </form>';
//                                                                        },
//                                                                        "width": "10%"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  "<button class=\"btn btn-primary btn-sm\" id=\"btnupdateAccInfo\" name=\"btnupdateAccInfo\"  value=\"" + data.rc_mem + "~" + data.bank_account + "~" + data.ifsc + "~" + data.bank_abbr + "\">Account Details</button> \n\
                                                                                     <button class=\"btn btn-primary btn-sm\" id=\"btnupdateStatus\" name=\"btnupdateStatus\"  value=\"" + data.rc_mem + "\">Status</button><br />";
                                                                        }, "width": "20%"}

                                                                ]

                                                            });
                                                            $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                            $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");
                                                            $('#beneficiaryList tbody').on('click', 'button', function (e) {
                                                                var btnName = this.name;
                                                                //var getTxtValue = this.value; // this gives textbox value   

                                                                if (btnName === "btnupdateAccInfo") {
                                                                    $("#hiddenValue").val(this.value); // this will set hidden field value
                                                                    var rcMem = this.value.split("~")[0];
                                                                    var bankAcc = this.value.split("~")[1];
                                                                    var ifscCode = this.value.split("~")[2];
                                                                    var bankAbbr = this.value.split("~")[3];
//                                                                    console.log(rcMem);
//                                                                    console.log(bankAcc);
//                                                                    console.log(ifscCode);
//                                                                    $("#loadModalBody").load("editBankDetails.jsp");
                                                                    $.ajax({
                                                                        contentType: "application/josn",
                                                                        type: "GET",
                                                                        url: "${pageContext.request.contextPath}/BeneficiaryDetailsUpdate?request_for=banklist",
                                                                        dataType: "json",
                                                                        beforeSend: function () {
                                                                            $('#myModal0').modal("show");
                                                                            $('#ddlBank').val('');
                                                                            $('#hdnRcAcc').val('');
                                                                            $('#txtAccount').val('');
                                                                            $('#txtConfirrmAccount').val('');
                                                                            $("#CheckAccountMatch").html('');
                                                                            $('#txtIfsc').val('');
                                                                        },
                                                                        success: function (result) {

                                                                            //console.log("hello")

                                                                            var listItems = "";
                                                                            listItems += "<option value=''>-- Select Bank--</option>";
                                                                            for (var i = 0; i < result.bankNameList.length; i++) {
                                                                                listItems += "<option value='" + result.bankNameList[i].npcibankcode + "'>" + result.bankNameList[i].bankname + "</option>";
                                                                            }

                                                                            $('#ddlBank').html(listItems);
                                                                            $('#ddlBank').val(bankAbbr);
//                                                                            $("#ddlBank").change(function () {
//                                                                                //var dropdownSelected=$('#ddlBank').val($(this).val());
//                                                                                var dropdownSelected = $(this).val();
//                                                                                $("#hdnDdl").val(dropdownSelected);
//                                                                                console.log('dropdownSelected value is' + dropdownSelected); 
//                                                                            });
                                                                            $('#hdnRcAcc').val(rcMem);
                                                                            $('#txtAccount').val(bankAcc);
                                                                            $('#txtIfsc').val(ifscCode);
//                                                                               for (var i = 0; i <= result.length; i++) {  
//                                                                                    //var opt = new Option(result[i].bankname);  
//                                                                                     bankNameddl.append('<option value="' + result[i].bankname + '">' + result[i].bankname + '</option>');
//                                                                                     
//                                                                                    //$("#ddlBank").appendTo(opt);  
//                                                                                } 
//                                                                           $('#ddlBank').change(function(){
//                                                                               
//                                                                               alert($(this).val(listItems));
//                                                                            });
//                                                                     
                                                                        },
                                                                        error: function (error) {
                                                                            //
                                                                        }
                                                                    });
                                                                } else if (btnName === "btnupdateStatus") {
                                                                    var rcMem = this.value;
                                                                    $('#myModal2').modal("show");
                                                                    $('#hdnRcStatus').val(rcMem);
                                                                }

                                                            });
                                                        });
                                                        $("#txtConfirrmAccount").focusout(function () {
                                                            var account = $("#txtAccount").val();
                                                            var confirmAccount = $("#txtConfirrmAccount").val();
                                                            if (account !== confirmAccount)
                                                                $("#CheckAccountMatch").html("Account Number does not match !").css("color", "red");
                                                            else
                                                                $("#CheckAccountMatch").html("Account Number matched !").css("color", "green");
                                                        });

                                                        $("#txtIfsc").focusout(function () {
                                                            var ifscCode = $("#txtIfsc").val();
                                                            $.ajax({
                                                                contentType: "application/josn",
                                                                type: "GET",
                                                                url: "${pageContext.request.contextPath}/BeneficiaryDetailsUpdate?request_for=ifscValidity&ifscCode=" + ifscCode,
                                                                dataType: "json",
//                                                                beforeSend: function () {
//
//                                                                },
                                                                success: function (result) {
                                                                    console.log(result);
                                                                    $("#CheckAccountMatch").html(result).css("color", "yellow");
                                                                }
//                                                                ,
//                                                                error: function (error) {
//                                                                    //
//                                                                }
                                                            });
                                                        });

                                                        $('#ddlSchemes').select2();
                                                        //});

                                                        $('#updateAccInfo').on('click', function (e) {
                                                            var rCmem = $('#hdnRcAcc').val();
                                                            var bankAbbr = $('#ddlBank  option:selected').val();
                                                            var bankName = $('#ddlBankName').val();
                                                            var bankAcc = $('#txtAccount').val();
                                                            var bankAccConfirm = $('#txtConfirrmAccount').val();
                                                            var ifsCcode = $('#txtIfsc').val();
                                                            var remarks = $('#txtAccUpdateRemarks').val();
                                                            if (rCmem === '') {
                                                                alert("Some error occured! please try again");
                                                                $('#myModal0').modal("hide");
                                                                return false;
                                                            }

                                                            if (bankAbbr === '' || bankAcc === '' || bankAccConfirm === '' || ifsCcode === '' || remarks === '') {
                                                                alert("Bank Name, Account Number, Confirm Account Number, IFSC, Remarks cannot be blank ");
                                                                return false;
                                                            }

                                                            if (bankAcc !== bankAccConfirm) {
                                                                alert("Account Number and Confirm Account Number does not match ");
                                                                return false;
                                                            }


                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/BeneficiaryDetailsUpdate?updateAccInfo=1&rCmem=" + rCmem + "&bankName=" + bankName + "&bankAbbr=" + bankAbbr + "&banKacc=" + bankAcc + "&ifsCcode=" + ifsCcode + "&remarks=" + remarks,
                                                                type: "POST",
                                                                async: false,
                                                                dataType: 'text',
                                                                //data: {json: json},
                                                                success: function (result) {
                                                                    if (result !== '') {
                                                                        alert("Data Updated Successfully");
                                                                        $('#myModal0').modal("hide");
                                                                        $('#btnSearch').click();
                                                                    }
                                                                }
//                                                             
                                                            });
                                                        });
                                                        $('#updateStatusInfo').on('click', function (e) {
                                                            var rCmem = $('#hdnRcStatus').val();
                                                            var status = $('#ddlStatus  option:selected').val();
                                                            var remarks = $('#txtStatusUpdateRemarks').val();
                                                            if (rCmem === '') {
                                                                alert("Some error occured! please try again");
                                                                $('#myModal0').modal("hide");
                                                                return false;
                                                            }

                                                            if (status === '' || remarks === '') {
                                                                alert("Status, Remarks cannot be blank ");
                                                                return false;
                                                            }


                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/BeneficiaryDetailsUpdate?updateStatusInfo=1&rCmem=" + rCmem + "&status=" + status + "&remarks=" + remarks,
                                                                type: "POST",
                                                                async: false,
                                                                dataType: 'text',
                                                                //data: {json: json},
                                                                success: function (result) {
                                                                    if (result !== '') {
                                                                        alert("Data Updated Successfully");
                                                                        $('#myModal0').modal("hide");
                                                                        $('#btnSearch').click();
                                                                    }
                                                                }
//                                                             
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
</html>