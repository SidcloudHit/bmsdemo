<%-- 
    Document   : beneficiarySelectedReportForDBTCash
    Created on : 28 Jan, 2020, 2:37:03 PM
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
        <title>BMS || DBT Initiation Status Report</title>
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
        <link rel="stylesheet" type="text/css" href="../DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">



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
                            <h4 class="text-white">DBT Initiation Status Report:</h4>
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
                                        <select id="ddlInstallment" name="ddlInstallment" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                        </select>
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
                                </div>--> 

                            </div>

                            <div class="row">
                                <!--<div class="form-group col-md-2">
                                    <label for="ddlDistrict">District</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlDistrict" name="ddlDistrict" class="form-control form-control-sm" required="required" >
                                                                                    
                                        </select>
                                    </div>
                                </div>-->

                                <div class="form-group col-md-2">
                                    <label for="ddlOffice">My Office</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlOffice" name="ddlOffice" class="form-control form-control-sm" required="required" onchange="resetBlock(2);
                                                setSelectedName(this.id);
                                                dependentDropDown('ddlOffice', 'ddlUrbRurBody', 'fillOfficeBlock');">
                                            <%=ddlOffice%>                                                  
                                        </select>
                                        <input type="hidden" id="ddlOfficeName" name="ddlOfficeName" value="" />
                                    </div> 
                                </div>

                                <!--<div class="form-group col-md-2">
                                    <label for="ddlSubDiv">Subdivision</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlSubDiv" name="ddlSubDiv" class="form-control form-control-sm" required="required" onchange="resetBlock(2);dependentDropDown('ddlSubDiv', 'ddlUrbRurBody', 'fillBlock');">
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>-->
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">MC/NP/Block</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlUrbRurBody" name="ddlUrbRurBody" class="form-control form-control-sm" onchange="dependentDropDown1('ddlUrbRurBody', 'ddlOffice', 'ddlWardGp', 'fillOfficeGp');" >
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
                                <!--<div class="form-group col-md-2">
                                    <label for=""></label>
                                    <div class="input-group mb-3">
                                        <div class="checkbox icheck-material-blue">
                                            <input type="checkbox" id="chkBxAllowNoLand" name="chkBxAllowNoLand" value="1" checked="checked">
                                            <label for="chkBxAllowNoLand">Allow Beneficiary with no Land Records</label>                                            
                                        </div>  
                                    </div> 
                                </div>-->
                                <div class="form-group  col-sm-3 col-md-2 col-lg-2">
                                    <label for="ddlStatus">Status</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlStatus" name="ddlStatus" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                            <option value="1">Initiated</option>
                                            <option value="2">Verified</option>
                                            <option value="4">Approved</option>

                                            <!--<option value="5">DBT Success</option>
                                            <option value="6">DBT Fail</option>-->
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-2 align-self-end">
                                    <div class="input-group mb-3">
                                        <button type="button" id="btnSearch" name="btnSearch" class="btn btn-warning btn-block">
                                            <i class="fa fa-search">&nbsp;&nbsp;</i><strong>Search</strong>
                                        </button>
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
                                                        <th>Scheme Name</th>
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
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>
    </html>
    <!-- jQuery library -->
    <script type="text/javascript" src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
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
                                                var table;
                                                var i = 1;
                                                $('#btnSearch').on('click', function (e) {

                                                    //var distId = $('#ddlDistrict option:selected').val();
                                                    //var subdivId = $('#ddlSubDiv option:selected').val();
                                                    var officeId = $('#ddlOffice option:selected').val();
                                                    var blockId = $('#ddlUrbRurBody option:selected').val();
                                                    var gpId = $('#ddlWardGp option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    var finYr = $('#ddlFinYear option:selected').val();
                                                    var instl = $('#ddlInstallment  option:selected').val();
                                                    //var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
                                                    var status = $('#ddlStatus  option:selected').val();
                                                    if (officeId === "" || schemeId === "" || finYr === "" || instl === "") {
                                                        alert("Kindly Select Scheme, Financial Year, Installment and My Office  for searching beneficiaries");
                                                        return false;
                                                    }

                                                    $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                    $('#beneficiaryList').DataTable().clear().destroy();
                                                    table = $('#beneficiaryList').DataTable({
                                                        "ajax": {
                                                            "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtrptm=1&finYr=" + finYr + "&instl=" + instl + "&officeId=" + officeId + "&blockId=" + blockId + "&gpId=" + gpId + "&schemeId=" + schemeId + "&status=" + status,
                                                            "type": "POST"
                                                        },
                                                        /*"columnDefs": [
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
                                                         },*/
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
                                                            {"data": "scheme_name"}
                                                        ],
                                                        "dom": 'Bfrtip',
                                                        "buttons": [
                                                            'copyHtml5',
                                                            'excelHtml5',
                                                            'csvHtml5',
                                                            'pdfHtml5'
                                                        ],
                                                        "drawCallback": function (settings) {

                                                            $('input:checkbox').each(function () {
                                                                $(this).attr('checked', true);
                                                                //$(this).prop('readonly', true);
                                                            });
                                                        }


                                                    });
                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                    //var rows = table.cells().nodes();
                                                    //console.log(table.column(0).checkboxes.select());
                                                    //console.log(table.column(0));
                                                    //$('input[type="checkbox"]', rows).prop('checked',true);
                                                    //table.column(0).checkboxes.select();
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
