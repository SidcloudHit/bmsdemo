<%-- 
    Document   : dbtApproverTaskList
    Created on : Aug 1, 2020, 12:35:55 PM
    Author     : NIC-Arindam
--%>

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
        response.sendRedirect("User/userHome.jsp");
    }
    Common.CommonMethod cm = new CommonMethod();
    int edit = 0;
    String ddlSchemes = "", ddlDist = "";
    ddlSchemes = cm.getSchemes("-Please Select-");//cm.getSchemes("-Please Select-");
    ddlDist = cm.getDistrict("-Please Select-");

    int year = Calendar.getInstance().get(Calendar.YEAR);
    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    String finYear = "";
    System.out.println("Financial month : " + month);
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
        <title>Agriculture, Tripura || DBT Approver Task List</title>
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
                            <h4 class="text-white">DBT Approver Task List:</h4>
                        </div>
                        <div class="card-body bg-form"><div class="row">
                                <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                    <label for="ddlSchemes">Scheme</label>
                                    <div class="input-group mb-3">                                                                                
                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required">                                            
                                            <%=ddlSchemes%>                                                    
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                    <label for="ddlFinYear">Financial Year</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlFinYear" name="ddlFinYear" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                            <%= finYear%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                    <label for="ddlInstallment">Installment</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlInstallment" name="ddlInstallment" class="form-control form-control-sm"  onchange="dependentDropDownBatch('ddlSchemes', 'ddlFinYear', 'ddlInstallment', 'ddlBatch', 'fillBatch');">
                                            <option value="">--Select--</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                    <label for="ddlBatch">Batch</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlBatch" name="ddlBatch" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                    <label for="ddlStatus">Status</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlStatus" name="ddlStatus" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                            <option value="1">Initiated</option>
                                            <option value="2">Verified</option>
                                            <option value="3">Approved</option>
                                            <option value="5">DBT Success</option>
                                            <option value="6">DBT Fail</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-md-2">
                                    <label for="ddlDistrict">District</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlDistrict" name="ddlDistrict" class="form-control form-control-sm" required="required" onchange="resetBlock(4);dependentDropDown('ddlDistrict', 'ddlSubDiv', 'fillSubDiv');">
                                            <%=ddlDist%>                                                    
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">Subdivision</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlSubDiv" name="ddlSubDiv" class="form-control form-control-sm" required="required" onchange="resetBlock(5);dependentDropDown('ddlSubDiv', 'ddlUrbRurBody', 'fillBlock');">
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">MC/NP/Block</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlUrbRurBody" name="ddlUrbRurBody" class="form-control form-control-sm" onchange="dependentDropDown('ddlUrbRurBody', 'ddlWardGp', 'fillGp');" >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">Ward/GP</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>
                                <div class="form-group col-md-2">
                                    <label for=""></label>
                                    <div class="input-group mb-3">
                                        <div class="checkbox icheck-material-blue">
                                            <input type="checkbox" id="chkBxAllowNoLand" name="chkBxAllowNoLand" value="1" >
                                            <label for="chkBxAllowNoLand">Allow Beneficiary with no Land Records</label>                                            
                                        </div>  
                                    </div> 
                                </div>
                                <div class="form-group col-md-2 align-self-end">
                                    <div class="input-group mb-3 ">
                                        <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                            <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="row d-none" id="benListDiv">
                                <div class="input-group mb-3 col-md-12">
                                    <table id="beneficiaryList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                        <thead class="bg-warning">
                                            <tr>  
                                                <th>Beneficiary ID</th>
                                                <th>Beneficiary Name</th>
                                                <th>Father's Name</th>
                                                <th>Mobile</th>
                                                <th>Scheme Name</th>
                                                <th>DBT Status</th>
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
                                                $('#btnSearch').on('click', function (e) {
                                                    var i = 0;
                                                    var distId = $('#ddlDistrict option:selected').val();
                                                    var subdivId = $('#ddlSubDiv option:selected').val();
                                                    var blockId = $('#ddlUrbRurBody option:selected').val();
                                                    var gpId = $('#ddlWardGp option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    var finYr = $('#ddlFinYear option:selected').val();
                                                    var instl = $('#ddlInstallment  option:selected').val();
                                                    var batchId = $('#ddlBatch  option:selected').val();
                                                    var status = $('#ddlStatus  option:selected').val();
                                                    var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;


                                                    if (distId === "" || (subdivId === "" || subdivId === "-1") || schemeId === "" || finYr === "" || instl === "") {
                                                        alert("Kindly Select Scheme, District, Subdivision, Financial Year, Installment,Item Category and Item for searching beneficiaries");
                                                        return false;
                                                    }

                                                    $('#benListDiv').removeClass("d-none").addClass("d-block");

                                                    $('#beneficiaryList').DataTable().clear().destroy();
                                                    $('#beneficiaryList').DataTable({
                                                        "ajax": {
                                                            "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrpt=1&distId=" + distId + "&subdivId=" + subdivId + "&blockId=" + blockId + "&gpId=" + gpId + "&schemeId=" + schemeId + "&finYr=" + finYr + "&instl=" + instl + "&batchId=" + batchId + "&status=" + status + "&land=" + land,
                                                            "type": "POST"
                                                        },

                                                        "iDisplayLength": 50,
                                                        'order': [[1, 'asc']],
                                                        'responsive': true,
                                                        "columns": [
                                                            {"data": "beneficiary_id"},
                                                            {"data": "beneficiary_name"},
                                                            {"data": "father_name"},
                                                            {"data": "mobile"},
                                                            {"data": "scheme_name"},
                                                            {"data": "status"}
                                                        ]
                                                    });

                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                });


                                                //Set Scheme Data
                                                $("#ddlSchemes").on('change', function (e) {
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    $("#ddlInstallment").empty();
                                                    $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
                                                    $.ajax({
                                                        url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + schemeId,
                                                        type: "POST",
                                                        success: function (result) {
                                                            if (parseInt(result) > 0) {

                                                                var num = parseInt(result);
                                                                //var instOpts = "";
                                                                for (var i = 1; i <= num; i++) {
                                                                    $("#ddlInstallment").append("<option value=\"" + i + "\">Installment " + i + " </option>");
                                                                }
                                                            }
                                                        }
                                                    });
                                                });



                                                //$(document).ready(function () {
                                                $('#ddlSchemes').select2();
                                                //});

                                            });


</script>

