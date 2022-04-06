<%-- 
    Document   : beneficiaryApprovalforDBTCashDirect
    Created on : Feb 28, 2020, 2:53:42 PM
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
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0, workflow = 2;
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

            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            String finYear = "";
            //System.out.println("Financial month : " + month);
            if (month <= 3) {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                //finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
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
            //ddlSchemes = cm.getCashSchemes("-Please Select-", deptId);//cm.getSchemes("-Please Select-");
            ddlSchemes = cm.getSchemes("-Please Select-", schemeId);

            int distId = 0;
            //if (jsarr != null) {
            //    for (int n = 0; n < jsarr.length(); n++) {
            //        jsobj = jsarr.getJSONObject(n);
            //        distId = jsobj.getInt("dist_code");

            //ddlDist = cm.getDistrict(distId, "-Please Select-");
            //ddlOffice = cm.getOffice(officeId, "-Please Select-");
            ddlOffice = cm.getChildOffice(schemeId, workflow, officeId, "-Please Select-");

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
        <title>BMS || Beneficiary Approval for DBT Schemes</title>
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
                                    <object data="" type="application/pdf" width="100%" height="700">

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
                                    <object data="" type="application/pdf" width="100%" height="700">

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

                        <div class="row ">
                            <div class="col-sm-12">

                            <%                                if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>
                            <div class="card">
                                <div class="card-header bg-info">
                                    <h4 class="text-white">Beneficiary Approval for DBT in Cash:</h4>
                                </div>
                                <div class="card-body bg-light">                                   

                                    <div class="row">
                                        <div class="form-group col-md-2">
                                            <label for="ddlSchemes">Scheme</label>
                                            <div class="input-group mb-3">                                                                                
                                                <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required">                                            
                                                    <%=ddlSchemes%>                                                    
                                                </select>
                                                <input type="hidden" id="txtWorkflow" name="txtWorkflow" value="2" />
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label for="ddlFinYear">Financial Year</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlFinYear" name="ddlFinYear" class="form-control form-control-sm" >
                                                    <option value="">--Select--</option>
                                                    <%= finYear%>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label for="ddlInstallment">Installment</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlInstallment" name="ddlInstallment" class="form-control form-control-sm"  onchange="setSelectedName(this.id);">
                                                    <option value="">--Select--</option>
                                                </select>
                                                <input type="hidden" id="ddlInstallmentName" name="ddlInstallmentName" value="" />
                                            </div>
                                        </div>
                                        <!--<div class="form-group col-md-3">
                                                <label for="txtStartingMonth">Period Starting From</label>
                                                <div class="input-group mb-3" > 
                                                        <span class="input-group-addon">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-calendar"></i></span>
                                                        </span>
                                                        <input type="date" class="form-control form-control-sm" id="txtStartingMonth" name="txtStartingMonth" />
        
                                                </div> 
                                        </div>                                
                                        <div class="form-group col-md-3">
                                            <label for="txtAmount">Amount</label>
                                            <div class="input-group mb-3">
                                                <input type="number" id="txtAmount" name="txtAmount" class="form-control form-control-sm"/>
                                            </div> 
                                                </div>

                                        <div class="form-group col-md-0 invisible">
                                            <label for=""></label>
                                            <div class="input-group mb-3">
                                                <div class="checkbox icheck-material-blue">
                                                    <input type="checkbox" id="chkBxAllowNoLand" name="chkBxAllowNoLand" value="1"  checked="checked">
                                                    <label for="chkBxAllowNoLand">Allow Beneficiary with no Land Records</label>                                            
                                                </div>  
                                            </div> 
                                        </div>-->
                                        <div class="form-group col-md-2">
                                            <label for="txtAmount">Amount</label>
                                            <div class="input-group mb-3">
                                                <input type="number" id="txtAmount" name="txtAmount" class="form-control form-control-sm"/>
                                            </div> 
                                        </div>

                                        <!--</div>
    
                                        <div class="row">
                                            <div class="form-group col-md-2">
                                                <label for="ddlSubDiv">My Office</label>
                                                <div class="input-group mb-3">
                                                    <select id="ddlOffice" name="ddlOffice" class="form-control form-control-sm" required="required" onchange="resetBlock(2);dependentDropDown2('ddlOffice', 'ddlSchemes', 'txtWorkflow', 'ddlOfficeChecker', 'fillChildOffice');">
                                        <%=ddlOffice%>                                                  
                                    </select>
                                </div> 
                            </div>-->
                                        <div class="form-group col-md-2">
                                            <label for="ddlSubDiv">Beneficiary Verified By</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlOfficeChecker" name="ddlOfficeChecker" class="form-control form-control-sm" required="required" > <!-- onchange="resetBlock(2);"dependentDropDown2('ddlOfficeChecker', 'ddlSchemes', 'txtWorkflow', 'ddlOfficeMaker', 'fillChildOffice'); -->
                                                    <%=ddlOffice%>
                                                </select>
                                            </div> 
                                        </div>

                                        <div class="form-group col-md-2 align-self-end">
                                            <div class="input-group mb-3 ">
                                                <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >
                                                <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary btn-sm">
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row d-none" id="fileListDiv">
                                        <div class="col-md-12"> 
                                            <div class="card" >
                                                <div class="card-header">
                                                    <h4 class="text-primary" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                                                        <i class="fa fa-bars" aria-hidden="true">&nbsp;Existing File List:</i></h4>
                                                </div>
                                                <div class="collapse" id="collapseExample">
                                                    <div class="card-body bg-page">
                                                        <div class="card-text">
                                                            <div class="row">
                                                                <div class="input-group mb-3 col-md-12">
                                                                    <table id="fileList" class="table table-sm table-striped table-bordered " style="width:100%">
                                                                        <caption>File created by Self</caption>
                                                                        <thead>
                                                                            <tr>
                                                                                <th>Sl No.</th>
                                                                                <th>Scheme Name</th>
                                                                                <th>Verified By</th>
                                                                                <th>Financial Year</th>
                                                                                <th>Installment</th>
                                                                                <th>Beneficiary Count </th> 
                                                                                <th>Freezed / Signed</th>
                                                                                <th>Signed On</th>
                                                                                <th>File</th>
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
                                    <div class="row d-none" id="benListDiv">
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="beneficiaryList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                        <thead class="bg-warning">
                                                            <tr>  
                                                                <th></th>
                                                                <th>Beneficiary ID</th>
                                                                <th>Beneficiary Name</th>
                                                                <th>Father's Name</th>
                                                                <th>Address</th>
                                                                <th>Office Name</th>
                                                                <th>Bank Account Number</th>
                                                                <th>IFSC</th>
                                                                <!--<th>Scheme Name</th>-->
                                                                <th>Amount</th>
                                                            </tr>
                                                        </thead>                                        
                                                    </table>
                                                </div>
                                            </div>     
                                            <div class="row" id="divSchemeMapping"> 
                                                <div class="input-group mb-3 col-md-4">
                                                    <button type="button" id="btnFreeze" name="btnFreeze" class="btn btn-success btn-block">
                                                        <strong>Esign Beneficiary List for DBT Payment</strong>
                                                    </button>
                                                </div>
                                                <div id="msgDiv" class="col-md-4"></div>
                                            </div>
                                        </div>                                            
                                    </div>
                                    <div class="row d-none" id="divSchemeMapping">                                
                                        <div class="input-group mb-3 col-md-2">
                                            <button type="button" id="btnAdd" name="btnAdd" class="btn btn-primary">
                                                <i class="fa fa-search">&nbsp;&nbsp;</i>Approve
                                            </button>
                                        </div> 
                                        <div class="input-group mb-3 col-md-2 d-none" id="filePushDiv" >

                                        </div>
                                        <div class="col-md-6" id="msgDiv">

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
                                                        var i = 0;
                                                        var table, table1;
                                                        $('#ddlSchemes').select2();
                                                        //$('#ddlOffice').val(<%=officeId%>);
                                                        //dependentDropDown2('ddlOffice', 'ddlSchemes', 'txtWorkflow', 'ddlOfficeChecker', 'fillChildOffice');


                                                        $("#ddlInstallment").on('change', function (e) {
                                                            var officeId = $('#hdnOfficeId').val();
                                                            var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var finYr = $('#ddlFinYear option:selected').val();
                                                            var instl = $('#ddlInstallment  option:selected').val();
                                                            var instlName = $('#ddlInstallmentName').val();
                                                            var j = 0;
                                                            $('#fileListDiv').removeClass("d-none").addClass("d-block");
                                                            $('#fileList').DataTable().clear().destroy();
                                                            table = $('#fileList').DataTable({
                                                                "ajax": {
                                                                    url: "${pageContext.request.contextPath}/EsignWorks?paymentFileList=1&officeId=" + officeId + "&checkerOfficeId=" + checkerOfficeId + "&schemeId=" + schemeId + "&finYr=" + finYr + "&instl=" + instl,
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
                                                                    {"data": "office_name"},
                                                                    {"data": "financial_year"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  instlName;
                                                                        }},
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
                                                                ]
//                                                                

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

                                                            //var distId = $('#ddlDistrict option:selected').val();
                                                            //var subdivId = $('#ddlSubDiv option:selected').val();
                                                            var officeId = $('#hdnOfficeId').val();//$('#ddlOffice option:selected').val();
                                                            var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                                                            //var makerOfficeId = $('#ddlOfficeMaker option:selected').val();
                                                            //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                            //var gpId = $('#ddlWardGp option:selected').val();
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var finYr = $('#ddlFinYear option:selected').val();
                                                            var instl = $('#ddlInstallment  option:selected').val();
                                                            var amount = $('#txtAmount').val();
                                                            var land = true;//$('#chkBxAllowNoLand').is(":checked") ? true : false;

                                                            if (officeId === "" || schemeId === "" || finYr === "" || instl === "" || checkerOfficeId === "-1" || checkerOfficeId === "") { //
                                                                alert("Kindly Select Scheme, Financial Year, Installment and Beneficiary Verified By for searching beneficiaries");
                                                                return false;
                                                            }



                                                            $('#benListDiv').removeClass("d-none").addClass("d-block");

                                                            $('#beneficiaryList').DataTable().clear().destroy();
                                                            table = $('#beneficiaryList').DataTable({
                                                                "ajax": {
                                                                    "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtappdirectesign=1&finYr=" + finYr + "&instl=" + instl + "&officeId=" + officeId + "&checkerOfficeId=" + checkerOfficeId + "&schemeId=" + schemeId + "&land=" + land,
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
                                                                    {"data": function (data, type, dataToSet) {
                                                                            ++i;
                                                                            return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                                                        }, "width": "25%"},
                                                                    {"data": "office_name"},
                                                                    {"data": "bank_account"},
                                                                    {"data": "ifsc"},
                                                                    //{"data": "scheme_name"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            var elgAmount;
                                                                            if (data.eligible_dbt_amount !== '0.00') {
                                                                                if (parseInt(data.eligible_dbt_amount) > 0) {
                                                                                    elgAmount = data.eligible_dbt_amount;
                                                                                } else {
                                                                                    elgAmount = amount;
                                                                                }
                                                                            }
                                                                            return elgAmount;

                                                                        }}
                                                                ],
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
//                                                                               

                                                        });

                                                        //Set Scheme Data
                                                        $("#ddlSchemes").on('change', function (e) {
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            $("#ddlInstallment").empty();
                                                            $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + schemeId,
                                                                type: "POST",
                                                                success: function (data) {
                                                                    var myObj = JSON.parse(data);
                                                                    //console.log(myObj.prd);
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
                                                                            $("#ddlInstallment").append("<option value=\"" + i + "\">" + selArr[i - 1] + " </option>");
                                                                        }
                                                                    }
                                                                    if (parseInt(myObj.amount) > 0) {
                                                                        $('#txtAmount').val(myObj.amount);
                                                                        $('#txtAmount').attr("readonly", "readonly");
                                                                    }

                                                                }
                                                            });
                                                        });


                                                        // Handle form submission event
                                                        $('#btnAdd').on('click', function (e) {

                                                            var elem = $('#divSchemeMapping');
                                                            var finYr = $('#ddlFinYear option:selected').val();
                                                            var instl = $('#ddlInstallment  option:selected').val();
                                                            var amount = $('#txtAmount').val();
                                                            //var distId = $('#ddlDistrict option:selected').val();
                                                            //var subdivId = $('#ddlSubDiv option:selected').val();
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            //alert($('#chkBxAllowNoLand').is(":checked"));
                                                            var land = true;//$('#chkBxAllowNoLand').is(":checked") ? true : false;

                                                            if (schemeId === "" || finYr === "" || instl === "") {
                                                                alert("Kindly Select Scheme, Financial Year and Installment for adding beneficiaries");
                                                                return false;
                                                            }


                                                            var table = $('#beneficiaryList').DataTable();
                                                            $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                            //var params = table.$('input,select,textarea').serializeArray();
                                                            var rows_selected = table.column(0).checkboxes.selected();
                                                            /*var tblData = table.rows('.selected').data();
                                                             var tmpData;
                                                             $.each(tblData, function (i, val) {
                                                             tmpData = tblData[i];
                                                             console.log(tmpData);
                                                             });*/

                                                            var id = [];
                                                            // Iterate over all selected checkboxes
                                                            //$.each(rows_selected, function (index, rowId) {
                                                            $.each(rows_selected, function (index, rowId) {
                                                                /*if (!$.contains(document, elem[this])) {
                                                                 // Create a hidden element
                                                                 elem.append(
                                                                 $('<input>')
                                                                 .attr('type', 'hidden')
                                                                 .attr('name', 'bid_' + i)
                                                                 .val(this)
                                                                 );
                                                                 i++;
                                                                 }*/
                                                                //var rowData = table.row($(this).parents('tr')).data();
                                                                //console.log(table.row($(this).closest('tr')).data());
                                                                // console.log(table.row($(this).parents('tr')).data());
                                                                //alert(this);
                                                                //alert(rows_selected['pmkisan_id']);


                                                                /*working code for entire row selection start*/
                                                                var rowData = rows_selected[index];
                                                                var b = table
                                                                        .rows(function (idx, data, node) {
                                                                            return data.rc_mem === rowData ? true : false;
                                                                        })
                                                                        .data();
                                                                //console.log(b[0]);
                                                                id.push(b[0]);
                                                                /*working code for entire row selection end*/
                                                                //id.push(this);

                                                            });

                                                            if (id.length === 0) {
                                                                alert("No Beneficiary Selected");
                                                                return false;
                                                            }

                                                            var json = JSON.stringify(id);

                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/DBTApproveCash?finYr=" + finYr + "&instl=" + instl + "&amount=" + amount + "&schemeId=" + schemeId + "&land=" + land + "&reqType=1",
                                                                //url: "${pageContext.request.contextPath}/DBTInitiate?finYr=" + finYr + "&instl=" + instl + "&amount=" + amount + "&distId=" + distId + "&subdivId=" + subdivId + "&schemeId=" + schemeId + "&land=" + land,
                                                                type: "POST",
                                                                async: false,
                                                                dataType: 'json',
                                                                data: {json: json},
                                                                success: function (data) {
                                                                    console.log(data);
                                                                    //var ret = JSON.parse(data);
                                                                    //console.log(data.nob);
                                                                    //console.log(data.fileName);
                                                                    //$('#filePushDiv').removeClass("d-none");
                                                                    //$('#btnAdd').attr("disabled", "disabled");
                                                                    $('#btnPushFile').removeAttr("disabled");
                                                                    //$("#hdnFileName").val(data.fileName);
                                                                    //$("#msgDiv").html("DBT File for " + data.nob + " beneficiaries created successfully ");
                                                                    $("#msgDiv").html("Number of DBT beneficiary successfully sent for Payment is " + data);
                                                                    $('#btnSearch').click();
                                                                    /*Diable all Form Fields*/

//                                                            $('#ddlSchemes').attr('readonly', 'readonly');
//                                                            $('#ddlFinYear').attr('readonly', 'readonly');
//                                                            $('#ddlInstallment').attr('readonly', 'readonly');
//                                                            $('#chkBxAllowNoLand').attr('readonly', 'readonly');
//                                                            $('#ddlOffice').attr('readonly', 'readonly');
//                                                            $('#ddlOfficeChecker').attr('readonly', 'readonly');
//                                                            $('#ddlOfficeMaker').attr('readonly', 'readonly');
//                                                            $('#ddlUrbRurBody').attr('readonly', 'readonly');
//                                                            $('#ddlWardGp').attr('readonly', 'readonly');
//                                                            $('#btnSearch').attr('readonly', 'readonly');


                                                                }
                                                            });
                                                        });


                                                        //Freeze Beneficiary List and Generate PDF
                                                        $('#btnFreeze').on('click', function (e) {
                                                            var officeId = <%=officeId%>;//$('#ddlOffice option:selected').val();
                                                            var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                                                            //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                            //var gpId = $('#ddlWardGp option:selected').val();
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var schemeName = $('#ddlSchemesName').val();
                                                            var finYr = $('#ddlFinYear option:selected').val();
                                                            var instl = $('#ddlInstallment  option:selected').val();
                                                            var instlName = $('#ddlInstallmentName').val();
                                                            var amount = $('#txtAmount').val();
                                                            var status = 14;
                                                            var userflag = "approver";
                                                            if (officeId === "" || schemeId === "" || finYr === "" || instl === "" || checkerOfficeId === "-1" || checkerOfficeId === "") {
                                                                alert("Kindly Select Scheme, Financial Year, Installment and DBT Verified By for initiating payment to beneficiaries");
                                                                return false;
                                                            }
//                                                    if (officeId === "" || schemeId === "" || finYr === "" || instl === ""|| checkerOfficeId==="-1") {
//                                                        alert("Kindly Select Scheme, Financial Year, Installment for searching beneficiaries");
//                                                        return false;
//                                                    }

                                                            //var table = $('#beneficiaryList').DataTable();
                                                            var rows_selected = table.column(0).checkboxes.selected();
                                                            var id = [];
                                                            $.each(rows_selected, function (index, rowId) {
                                                                var rowData = rows_selected[index];
                                                                var b = table
                                                                        .rows(function (idx, data, node) {
                                                                            return data.rc_mem === rowData ? true : false;
                                                                        })
                                                                        .data();
                                                                id.push(b[0]);
                                                            });

                                                            if (id.length === 0) {
                                                                alert("No Beneficiary Selected");
                                                                return false;
                                                            }
                                                            var json = JSON.stringify(id);

                                                            console.log(id);
                                                            console.log(json);
                                                            //alert(checkerOfficeId);

                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/GeneratePDF?argument_type=freezeBenList&argument=1&direct=1&finYr=" + finYr + "&instl=" + instl + "&instlName=" + instlName + "&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status + "&userflag=" + userflag + "&checkerofficeid=" + checkerOfficeId,
                                                                type: "POST",
                                                                async: false,
                                                                dataType: 'json',
                                                                data: {selectedbenid: json},
                                                                success: function (result) {
//                                                                    if (parseInt(result) > 0) {
                                                                    var frameSrc = "${pageContext.request.contextPath}/showPdf?fid=" + result;
                                                                    $('#eSignForm').html("");
                                                                    $('#eSignForm').removeClass("d-block").addClass("d-none");
                                                                    $('#pdfViewDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#pdfViewDiv object').attr('data', frameSrc);
                                                                    //$('#pdfViewDiv embed').attr('src', frameSrc);
                                                                    $('#myModal').modal("show");
                                                                    //$('#hdnEsignApplId').val(applId);
                                                                    $('#btnEsign').removeClass("d-none").addClass("d-block");
//                                                                    }
                                                                }
                                                            });



                                                        });

                                                        // Esign Beneficiary List
                                                        $('#btnEsign').on('click', function (e) {
                                                            var applId = $('#hdnEsignApplId').val();
                                                            var frameSrc = "";
                                                            if (applId !== '') {
                                                                frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=1&serviceId=3&esignApplId=" + applId;
                                                            } else {
                                                                frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=1&serviceId=3";
                                                            }

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


                                                        //$(document).ready(function () {

                                                        //});

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
