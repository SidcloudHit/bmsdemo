<%-- 
    Document   : beneficiarySelectionFreezeNEsign
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
            //ddlSchemes = cm.getCashSchemes("-Please Select-");//cm.getSchemes("-Please Select-");            
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


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Beneficiary Selection for DBT Cash Schemes</title>
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
                            <h4 class="text-white">Freeze Beneficiary Selection for DBT Cash:</h4>
                        </div>
                        <div class="card-body bg-form">
                            <div class="row">
                                <div class="form-group col-md-3">
                                    <label for="ddlSchemes">Scheme</label>
                                    <div class="input-group mb-3">                                                                                
                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                            <%=ddlSchemes%>                                                    
                                        </select>
                                        <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                    </div>
                                </div>
                                <div class="form-group col-md-3">
                                    <label for="ddlFinYear">Financial Year</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlFinYear" name="ddlFinYear" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                            <%= finYear%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-3">
                                    <label for="ddlInstallment">Installment</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlInstallment" name="ddlInstallment" class="form-control form-control-sm" onchange="setSelectedName(this.id);">
                                            <option value="">--Select--</option>
                                        </select>
                                        <input type="hidden" id="ddlInstallmentName" name="ddlInstallmentName" value="" />
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
                            <div class="row d-none" id="pdfLinkDiv">
                                <div class="col-md-12">
                                    <div id="fPdf" class="row d-none">
                                        <div class="input-group mb-3 col-md-4">
                                            <a href="" target="_blank">
                                                <button type="button" id="btnViewFPDF" name="btnViewFPDF" class="btn btn-danger btn-block">
                                                    <strong>View Freezed PDF</strong>
                                                </button>
                                            </a>
                                        </div>
                                        <div class="input-group mb-3 col-md-4">                                            
                                            <button type="button" id="btnEsign1" name="btnEsign1" class="btn btn-danger btn-block">
                                                <strong>Proceed for eSigning the Freezed PDF</strong>
                                            </button>                                            
                                        </div>
                                    </div>
                                    <div id="sPdf" class="row d-none">
                                        <div class="input-group mb-3 col-md-4">
                                            <a href="" target="_blank">
                                                <button type="button" id="btnViewSPDF" name="btnViewSPDF" class="btn btn-danger btn-block">
                                                    <strong>View Signed PDF</strong>
                                                </button>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row d-none" id="benListDiv">
                                <div class="col-md-12">

                                    <!--<div class="row">
                                        <div class="input-group mb-3 col-md-4">
                                            <button type="button" id="btnAddNew" name="btnAddNew" class="btn btn-danger btn-block">
                                                <strong>Add New Beneficiary</strong>
                                            </button>
                                        </div>
                                    </div>-->
                                    <div class="row">
                                        <div class="input-group mb-3 col-md-12">
                                            <table id="beneficiaryList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                <thead>
                                                    <tr>
                                                        <th>Beneficiary ID</th>
                                                        <th>Beneficiary Name</th>
                                                        <th>Guardian's Name</th>
                                                        <th>Office Name</th>                                                        
                                                        <th>Bank Account Number</th>
                                                        <th>IFSC</th>
                                                        <th>DBT Amount</th>
                                                    </tr>
                                                </thead>                                        
                                            </table>
                                        </div>
                                    </div>

                                    <!-- Modal -->
                                    <div id="myModal1" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <!-- Modal content-->
                                            <div class="modal-content">
                                                <div class="modal-header bg-warning">
                                                    <h4 class="modal-title">Beneficiary List</h4>
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                </div>
                                                <div class="modal-body bg-light">
                                                    <div class="mt-2" id="div_generate_certificate1">
                                                        <div class="embed-responsive embed-responsive-16by9">
                                                            <iframe id="ifm2" src="" style="zoom:0.60" frameborder="0" width="100%" class="embed-responsive-item"></iframe>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Modal -->
                                    <div id="myModal" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <!-- Modal content-->
                                            <div class="modal-content">
                                                <div class="modal-header bg-warning">
                                                    <h4 class="modal-title">Beneficiary List</h4>
                                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                </div>
                                                <div class="modal-body bg-light">
                                                    <div class="mt-2" id="div_generate_certificate">
                                                        <div class="embed-responsive embed-responsive-16by9">
                                                            <iframe id="ifm1" src="" style="zoom:0.60" frameborder="0" width="100%" class="embed-responsive-item"></iframe>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" id="btnEsign" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign &nbsp;&nbsp;</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>



                                    <div class="row" id="divSchemeMapping"> 
                                        <div class="input-group mb-3 col-md-4">
                                            <button type="button" id="btnFreeze" name="btnFreeze" class="btn btn-success btn-block">
                                                <strong>Freeze Beneficiary List for DBT Payment</strong>
                                            </button>
                                        </div>
                                        <div id="msgDiv" class="col-md-4"></div>
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

<!--<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.html5.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.print.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.buttons.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/jszip.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/pdfmake.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/vfs_fonts.js"></script>-->
<script>
                                            $(document).ready(function () {
                                                var table;
                                                var i = 1;
                                                $('#btnSearch').on('click', function (e) {
                                                    //var distId = $('#ddlDistrict option:selected').val();
                                                    //var subdivId = $('#ddlSubDiv option:selected').val();
                                                    var officeId = $('#hdnOfficeId').val();
                                                    //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                    //var gpId = $('#ddlWardGp option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    var finYr = $('#ddlFinYear option:selected').val();
                                                    var instl = $('#ddlInstallment  option:selected').val();
                                                    //var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
                                                    var status = 1;
                                                    if (officeId === "" || schemeId === "" || finYr === "" || instl === "") {
                                                        alert("Kindly Select Scheme, Financial Year, Installment for searching beneficiaries");
                                                        return false;
                                                    }

                                                    $.ajax({
                                                        //search if signed pdf exists
                                                        url: "${pageContext.request.contextPath}/EsignWorks?search=1&dbttype=1&finYr=" + finYr + "&instl=" + instl + "&officeId=" + officeId + "&schemeId=" + schemeId,
                                                        type: "POST",
                                                        async: false,
                                                        success: function (result) {
                                                            if (parseInt(result) > 0) {
                                                                var frameSrc = "${pageContext.request.contextPath}/EsignWorks?showPdf=1&fid=" + parseInt(result);
                                                                /*$("#sPdf").removeClass("d-none");
                                                                 if ($("#fPdf").attr('class').indexOf("d-none") > -1) {
                                                                 $("#fPdf").addClass("d-none");
                                                                 }
                                                                 var pdfLink = $("#sPdf a");
                                                                 pdfLink.attr('href', frameSrc);*/
                                                                var iframeElement = $("#myModal1 iframe");
                                                                console.log(frameSrc);
                                                                iframeElement.attr('src', frameSrc);
                                                                //$('#myModal').show();
                                                                $('#myModal1').modal("show");
                                                                //$('#div_generate_certificate1').show();
                                                                //$('#btnEsign1').hide();
                                                            } else {
                                                                $.ajax({
                                                                    //else search if unsigned pdf exists
                                                                    url: "${pageContext.request.contextPath}/EsignWorks?search=2&dbttype=1&finYr=" + finYr + "&instl=" + instl + "&officeId=" + officeId + "&schemeId=" + schemeId,
                                                                    type: "POST",
                                                                    async: false,
                                                                    success: function (result) {
                                                                        if (parseInt(result) > 0) {
                                                                            var frameSrc = "${pageContext.request.contextPath}/EsignWorks?showPdf=1&fid=" + parseInt(result);
                                                                            /*if ($("#sPdf").attr('class').indexOf("d-none") > -1) {
                                                                             $("#sPdf").addClass("d-none");
                                                                             }
                                                                             $("#fPdf").removeClass("d-none");
                                                                             var pdfLink = $("#fPdf a");
                                                                             pdfLink.attr('href', frameSrc);*/
                                                                            var iframeElement = $("#myModal1 iframe");
                                                                            //console.log(frameSrc);
                                                                            iframeElement.attr('src', frameSrc);
                                                                            //console.log(1);
                                                                            //$('#myModal').show();
                                                                            $('#myModal1').modal("show");
                                                                            //$('#div_generate_certificate1').show();
                                                                            //console.log(2);
                                                                            //$('#btnEsign1').show();
                                                                            //console.log(3);
                                                                        } else {
                                                                            if ($("#fPdf").attr('class').indexOf("d-none") > -1) {
                                                                                $("#fPdf").addClass("d-none");
                                                                            }
                                                                            if ($("#sPdf").attr('class').indexOf("d-none") > -1) {
                                                                                $("#sPdf").addClass("d-none");
                                                                            }

                                                                            $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                                            $('#beneficiaryList').DataTable().clear().destroy();
                                                                            table = $('#beneficiaryList').DataTable({
                                                                                "ajax": {
                                                                                    "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptm=1&finYr=" + finYr + "&instl=" + instl + "&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status,
                                                                                    "type": "POST"
                                                                                },
                                                                                "iDisplayLength": 50,
                                                                                'order': [[1, 'asc']],
                                                                                'responsive': true,
                                                                                "columns": [
                                                                                    //{"data": "rc_mem"},
                                                                                    {"data": "rc_mem"},
                                                                                    /*{"data": function (data, type, dataToSet) {
                                                                                     return  data.rc_mem + "<input type=\"hidden\" id=\"hdnBenId_" + i + "\" name=\"hdnBenId" + i + "\" value=\"" + data.rc_mem + "\" />" + i++;
                                                                                     }},*/
                                                                                    {"data": "full_name"},
                                                                                    {"data": "guardian_name"},
                                                                                    {"data": "office_name"},
                                                                                    //{"data": "pmkisan_id"},
                                                                                    {"data": "bank_account"},
                                                                                    {"data": "ifsc"},
                                                                                    {"data": "amount"}
                                                                                ]


                                                                            });
                                                                            $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                                        }
                                                                    }

                                                                });
                                                            }
                                                        }
                                                    });
                                                });
                                                //alert('ok');


                                                //Set Scheme Data
                                                $("#ddlSchemes").on('change', function (e) {
                                                    $('#txtAmount').removeAttr("readonly");
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    $("#ddlInstallment").empty();
                                                    $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
                                                    $.ajax({
                                                        url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + schemeId,
                                                        type: "POST",
                                                        success: function (data) {
                                                            //console.log(data);
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

                                                            $('#txtAmount').val(myObj.amount);
                                                            $('#txtAmount').attr("readonly", "readonly");
                                                        }
                                                    });
                                                });

                                                //Freeze Beneficiary List and Generate PDF
                                                $('#btnFreeze').on('click', function (e) {
                                                    var officeId = $('#hdnOfficeId').val();
                                                    //var blockId = $('#ddlUrbRurBody option:selected').val();
                                                    //var gpId = $('#ddlWardGp option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    var schemeName = $('#ddlSchemesName').val();
                                                    var finYr = $('#ddlFinYear option:selected').val();
                                                    var instl = $('#ddlInstallment  option:selected').val();
                                                    var instlName = $('#ddlInstallmentName').val();
                                                    var status = 10;
                                                    if (officeId === "" || schemeId === "" || finYr === "" || instl === "") {
                                                        alert("Kindly Select Scheme, Financial Year, Installment for searching beneficiaries");
                                                        return false;
                                                    }

                                                    //table.column(0).select();
                                                    //var rows_selected = table.column(0).selected();
                                                    var id = []; //table.column( 0 ).data().toArray();

                                                    var b = table.rows().data().toArray();


                                                    var json = JSON.stringify(b);
                                                    //console.log(id);
                                                    //console.log(json);
                                                    $.ajax({
                                                        url: "${pageContext.request.contextPath}/DBTFreezeCash?finYr=" + finYr + "&instl=" + instl + "&schemeId=" + schemeId,
                                                        type: "POST",
                                                        async: false,
                                                        dataType: 'json',
                                                        data: {json: json},
                                                        success: function (result) {
                                                            if (parseInt(result) > 0) {
                                                                var frameSrc = "${pageContext.request.contextPath}/GeneratePDF?argument_type=freezeBenList&argument=1&finYr=" + finYr + "&instl=" + instl + "&instlName=" + instlName + "&officeId=" + officeId + "&schemeId=" + schemeId + "&schemeName=" + schemeName + "&status=" + status;
                                                                var iframeElement = $("#myModal iframe");
                                                                //console.log(frameSrc);
                                                                iframeElement.attr('src', frameSrc);
                                                                //$('#myModal').show();
                                                                $('#myModal').modal("show");
                                                                //$('#btnEsign').show();
                                                            }
                                                        }
                                                    });




                                                });

                                                // Esign Beneficiary List
                                                $('#btnEsign').on('click', function (e) {
                                                    var frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=1&serviceId=3";
                                                    var iframeElement = $("#myModal iframe");
                                                    iframeElement.attr('src', frameSrc);
                                                });

                                                // Esign Beneficiary List
                                                $('#btnEsign1').on('click', function (e) {
                                                    var frameSrc = "${pageContext.request.contextPath}/esign_certificate_request?purpose=1&serviceId=3";
                                                    var iframeElement = $("#myModal1 iframe");
                                                    iframeElement.attr('src', frameSrc);
                                                });

                                                //$(document).ready(function () {
                                                $('#ddlSchemes').select2();
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
