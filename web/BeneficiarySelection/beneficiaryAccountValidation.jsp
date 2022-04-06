<%-- 
    Document   : beneficiaryAccountValidation
    Created on : 25 Feb, 2022, 4:09:56 PM
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
                                        <!--  <div class="col-md-4">
                                              <div class="input-group mb-3 col-md-4">
                                                  <a href="${pageContext.request.contextPath}/DBTInitiator/beneficiaryFreezeListMaker.jsp" class="btn btn-success btn-block">
                                                      View Freeze And e-sign Beneficiary List
                                                  </a>
                                              </div>
                                               <div class="col-md-4">
                                                  <a href="${pageContext.request.contextPath}/DBTInitiator/beneficiaryEsignListMaker.jsp" class="btn btn-success btn-block">
                                                      View e-signed Beneficiary List
                                                  </a>
                                              </div>
                                          </div>-->


                                        <input type="hidden" id="ddlSchemes" name="ddlSchemes" value="<%=schemeId + "~" + scheme_code%>" />
                                        <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="<%=scheme_name%>" />
                                        <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >

                                        <div class="form-group col-md-3">
                                            <label for="ddlSubDiv">Beneficiary Verified By</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlOfficeChecker" name="ddlOfficeChecker" class="form-control form-control-sm" required="required" >
                                                    <%=ddlOffice%>
                                                </select>
                                            </div> 
                                        </div>
                                        <div class="form-group col-md-6 required">
                                            <label for="domicile" class="font-weight-bold">Select option:</label>
                                            <div class="input-group">

                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="rd_route" id="rd_non_validation_route" value="1" <% if (route.equals("1")) {%> checked="" <%}%>  >
                                                    <label class="form-check-label" for="inlineRadio2">To be Requested</label>
                                                </div>

                                                <div class="form-check form-check-inline">
                                                    <input class="form-check-input" type="radio" name="rd_route" id="rd_validation_requested_route" value="2" <% if (route.equals("2")) {%> checked="" <%}%> > <!--onclick="checkBeneficiaryRoute(this.value);"-->
                                                    <label class="form-check-label" for="inlineRadio2">Already Requested</label>
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
                                    <div class="row d-none mt-2" id="benListDiv">
                                        <div class="col-md-12"> 
                                            <div id="non_validation_route">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="card">
                                                            <div class="card-body bg-dark ">
                                                                <span class="text-white font-weight-bold">Beneficiary List </span>
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
                                                                    <th>Bank Account Number</th>
                                                                    <th>IFSC</th>
                                                                </tr>
                                                            </thead>                                        
                                                        </table>
                                                    </div>
                                                </div>

                                                <div class="row" id="divSchemeMapping"> 
                                                    <div class="input-group mb-3 col-md-4">
                                                        <button type="button" id="btnAccValidation" name="btnAccValidation" class="btn btn-success btn-block">
                                                            <strong>Send for Account Validation</strong>
                                                        </button>
                                                    </div>
                                                    <div id="msgDiv" class="col-md-4"></div>
                                                </div>
                                            </div>

                                            <div id="response_route">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="card">
                                                            <div class="card-body bg-dark ">
                                                                <span class="text-white font-weight-bold"> List of Batches Requested for Account Validation</span>
                                                            </div> 
                                                        </div>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
                                                    <div class="input-group mb-3 col-md-12">
                                                        <table id="beneficiaryList3" class="table table-sm table-striped table-bordered" style="width:100%">
                                                            <thead>
                                                                <tr>
                                                                    <th>Batch ID</th>
                                                                    <th>Total Beneficiary Count</th>
                                                                    <th>Request Date</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>                                        
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>

                                            <div id="validation_requested_route" style="display:none;">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="card">
                                                            <div class="card-body bg-dark ">
                                                                <span class="text-white font-weight-bold"> List of Beneficiaries Requested for Account Validation</span>
                                                            </div> 
                                                        </div>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
                                                    <div class="input-group mb-3 col-md-12">
                                                        <table id="beneficiaryList2" class="table table-sm table-striped table-bordered" style="width:100%">
                                                            <thead>
                                                                <tr>
                                                                    <th>Beneficiary ID</th>
                                                                    <th>Beneficiary Name</th>
                                                                    <th>Guardian's Name</th>
                                                                    <th>Address</th>
                                                                    <th>Mobile</th>
                                                                    <th>Bank Account Number</th>
                                                                    <th>IFSC</th>
                                                                    <th>Account Validation Status</th>
                                                                    <th>Beneficiary Name Matching Status</th>
                                                                    <th>Beneficiary Name As Per Bank Details</th>
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
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>

            <div id="loader" style="display:none;width:105px;height:150px;border:1px solid black;position:fixed;text-align: center;  top: 40%; right: 0; left: 43%; z-index: 9999999; filter: alpha(opacity=70); opacity: 0.9; background-color: #999 ">
                <img src='../Images/progress.gif' width="100" height="100" /><br>Loading Please Wait..</div>
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
            $("#non_validation_route").css("display", "none");
            $("#validation_requested_route").css("display", "none");

//                                                            if ($("#rd_non_validation_route").is(":checked")) {
//                                                                checkBeneficiaryRoute("1");
//
//                                                            }
//                                                            if ($("#rd_validation_requested_route").is(":checked")) {
//                                                                checkBeneficiaryRoute("2");
//                                                            }

            $('#ddlOfficeChecker').select2();

            var table, table1, table2;
            var i = 0;

            $('#btnSearch').on('click', function (e) {
                $("#non_validation_route").css("display", "none");
                $("#validation_requested_route").css("display", "none");
                var ddlSchemes = $("#ddlSchemes").val();
                var option = document.getElementsByName('rd_route');
                var option_val = "0";
                // var option_val=$('#rd_route').val();
                //  console.log(option_val);    
                if (!(option[0].checked || option[1].checked)) {
                    alert("Please Select Your Option");
                    return false;
                }

                if (option[0].checked) {
                    option_val = "1";

                }
                if (option[1].checked) {
                    option_val = "2";

                }

                var officeId = $('#hdnOfficeId').val();
                var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();

                var schemeId = $('#ddlSchemes').val();
                var status = 9;

                if (checkerOfficeId === "0" || checkerOfficeId === "") {
                    alert("Kindly Select Beneficiary Verified By for searching beneficiaries");
                    return false;
                }

                $('#benListDiv').removeClass("d-none").addClass("d-block");

                if (option_val === "1") {
                    $('#beneficiaryList').DataTable().clear().destroy();
                    table = $('#beneficiaryList').DataTable({
                        dom: 'Bfrtip',
                        "ajax": {
                            "url": "${pageContext.request.contextPath}/BeneficiaryDataEdit?dbtbensch=1&officeId=" + checkerOfficeId + "&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status,
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

                            {"data": "bank_account"},
                            {"data": "ifsc"}
                        ]


                    });
                    $("#response_route").css("display", "none");
                    $("#non_validation_route").css("display", "block");
                    $("#validation_requested_route").css("display", "none");
                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                    $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");
                }

                /////////////////////////////////////////////////////////////////////////////////////////
                if (option_val === "2") {
                    $('#beneficiaryList3').DataTable().clear().destroy();
                    table2 = $('#beneficiaryList3').DataTable({                        
                        "ajax": {
                            "url": "${pageContext.request.contextPath}/BeneficiaryDataEdit?dbtbenbatch=1&officeId=" + checkerOfficeId + "&officeId=" + officeId + "&schemeId=" + schemeId , //+ "&status=" + status
                            "type": "POST"
                        },
                        "iDisplayLength": 50,
                        'order': [[1, 'asc']],
                        'responsive': true,
                        "columns": [

                            //{"data": "batch_id", "width": "10%"},
                            {"data": function (data, type, dataToSet) {
                                    return  "<button class=\"btn btn-primary btn-sm\" id=\"btnGetBatchDetails\" name=\"btnGetBatchDetails\" title=\"View Batch Details\" value=\"" + data.batch_id + "\">" + data.batch_id + "</button>";
                                }, "width": "20%"},
                            {"data": "ben_count", "width": "10%"},
                            {"data": "req_time", "width": "15%"},
                            {"data": function (data, type, dataToSet) {
                                    return  "<button class=\"btn btn-primary btn-sm\" id=\"btnGetReponse\" name=\"btnGetReponse\"  value=\"" + data.batch_id + "~" + data.req_time + "\">Check Account Validation Status</button>";
                                }, "width": "20%"}
                        ]


                    });

                    $("#response_route").css("display", "block");
                    $("#non_validation_route").css("display", "none");
                    $('#beneficiaryList3_wrapper').addClass("col-md-12");
                    $("select[name='beneficiaryList3_length']").removeClass("custom-select custom-select-sm ");

                    $('#beneficiaryList3 tbody').on('click', 'button', function (e) {
                        var btnName = this.name;
                        status = 9;
                        //var getTxtValue = this.value; // this gives textbox value   
                        if (btnName === "btnGetBatchDetails") {
                            $("#validation_requested_route").css("display", "block");
                            var batchId = this.value;
                            $('#beneficiaryList2').DataTable().clear().destroy();
                            table2 = $('#beneficiaryList2').DataTable({
                                "ajax": {
                                    "url": "${pageContext.request.contextPath}/BeneficiaryDataEdit?dbtbenpsh=1&officeId=" + checkerOfficeId + "&checkerOfficeId=" + checkerOfficeId + "&batchId=" + batchId + "&schemeId=" + schemeId + "&status=" + status,
                                    "type": "POST"
                                },
                                "iDisplayLength": 50,
                                'order': [[1, 'asc']],
                                'responsive': true,
                                "columns": [

                                    {"data": "rc_mem", "width": "5%"},
                                    {"data": "full_name", "width": "15%"},
                                    {"data": "guardian_name", "width": "15%"},

                                    {"data": function (data, type, dataToSet) {
                                            ++i;
                                            return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                        }, "width": "25%"},
                                    {"data": "mobile"},
                                    {"data": "bank_account"},
                                    {"data": "ifsc"},
                                    {"data": "account_valid_status", "width": "15%"},
                                    {"data": "beneficiary_name_match_status", "width": "15%"},
                                    {"data": "beneficiary_name_as_per_bank", "width": "15%"}
                                ],
                                "dom": 'Bfrtip',
                                "buttons": [
                                    'copyHtml5',
                                    'excelHtml5',
                                    'csvHtml5',
                                    {
                                        extend: 'pdfHtml5',
                                        orientation: 'landscape',
                                        pageSize: 'LEGAL'}
                                ]


                            });
                        } else if (btnName === "btnGetReponse") {
                            var batchId = this.value.split("~")[0];
                            var refDate = this.value.split("~")[1];
                            console.log(this.value);
                            $.ajax({
                                contentType: "application/josn",
                                type: "GET",
                                url: "${pageContext.request.contextPath}/BeneficiaryAccountValidation?validResponse=1&batchId=" + batchId + "&refDate=" + refDate + "&scheme=" + ddlSchemes,
                                dataType: "json",
                                beforeSend: function () {
                                    $('#loader').show();
                                    $(".card").css("opacity", 0.2);
                                    $(".card").find("input, button, submit, textarea, select").attr("disabled", "disabled");
                                    $("#loader").css({"display": "block"});
                                },
                                success: function (result) {
                                    $('#loader').hide();
                                    $(".card").css("opacity", 1.0);
                                    $(".card").find("input, button, submit, textarea, select").removeAttr("disabled", "disabled");
                                    console.log(result);
                                    if (result !== "") {
                                        alert("Response Fetched Successfully");
                                    }
                                    $("#validation_requested_route").css("display", "none");

                                },
                                error: function (error) {
                                    //
                                }
                            });
                        }

                    });


                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    //$("#validation_requested_route").css("display", "block");
                    $("#non_validation_route").css("display", "none");
                    $('#beneficiaryList2_wrapper').addClass("col-md-12");
                    $("select[name='beneficiaryList2_length']").removeClass("custom-select custom-select-sm ");
                }
            });

            $('#btnAccValidation').on('click', function (e) {
                var officeId = $('#hdnOfficeId').val();
                var schemeId = $('#ddlSchemes').val();
                var schemeName = $('#ddlSchemesName').val();
                var status = 4;
                var userflag = "initiator";
                if (officeId === "" || schemeId === "") {
                    alert("Kindly Select Scheme for Bank Account Validation Push");
                    return false;
                }

                var rows_selected = table.column(0).checkboxes.selected();

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


                if (id.length === 0) {
                    alert("No Beneficiary Selected");
                    return false;
                }

                var json = JSON.stringify(id);

                $.ajax({
                    url: "${pageContext.request.contextPath}/BeneficiaryAccountValidation?AccValid=1&schemeId=" + schemeId + "&status=" + status,
                    type: "POST",
                    async: false,
                    dataType: 'json',
                    data: {json: json},
                    beforeSend: function () {
                        $('#loader').show();
                        $(".card").css("opacity", 0.2);
                        $(".card").find("input, button, submit, textarea, select").attr("disabled", "disabled");
                        $("#loader").css({"display": "block"});
                    },
                    success: function (result) {
                        $('#loader').hide();
                        $(".card").css("opacity", 1.0);
                        $(".card").find("input, button, submit, textarea, select").removeAttr("disabled", "disabled");
//                                                                        console.log(result);
//                                                                        console.log(result === "");
//                                                                        console.log(result !== "");
//                                                                        console.log(result.summary[0].status);
                        if (result !== "") {
                            alert("Push Status : " + result.summary[0].status + " || Push Remarks : " + result.summary[0].remarks + " || Failed Record Count: " + result.summary[0].total_failed_beneficiary_count);
                        } else {
                            alert("Push Status : failed");
                        }

                        //$('#btnSearch').click();

                        window.location.href = "${pageContext.request.contextPath}/BeneficiarySelection/beneficiaryAccountValidation.jsp";

                    }

                });
            });

        });

        /*   function checkBeneficiaryRoute(route) {
         var ben_office_id = $('#ddlOfficeChecker option:selected').val();
         if (ben_office_id !== "") {
         //  $('#btnSearch').trigger('click');
         document.getElementById("btnSearch").click();
         }
         
         if (route === "1") {
         $("#non_validation_route").css("display", "block");
         $("#validation_requested_route").css("display", "none");
         }
         if (route === "2") {
         
         $("#validation_requested_route").css("display", "block");
         $("#non_validation_route").css("display", "none");
         }
         } */


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