<%-- 
    Document   : beneficiarySummaryReport
    Created on : Jul 8, 2020, 8:00:34 AM
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
        int edit = 0;
        String ddlSchemes = "", ddlDist = "", ddlStatus = "", ddlOffice = "";

        ddlSchemes = cm.getSchemes("-Please Select-", schemeId);
        ddlOffice = cm.getChildOffice(schemeId, workflow, officeId, "-Please Select-");
        ddlDist = cm.getDistrict("-Please Select-");
        ddlStatus = cm.getBeneficiaryStatus("-Please Select-");

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
        <title>BMS || Beneficiary Status Report - Checker Wise</title>
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
        <div id="myModal2" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Beneficiary Time Line</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div id="statusViewDiv" class="bg-light mt-2">

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
                                    <h4 class="text-white">Beneficiary Summary Report:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <div class="row">
                                        <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                            <label for="ddlSchemes">Scheme</label>
                                            <div class="input-group mb-3">                                                                                
                                                <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);" >                                            
                                                    <%=ddlSchemes%>                                                    
                                                </select>
                                                <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label for="ddlSubDiv">Beneficiary Verified By</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlOfficeChecker" name="ddlOfficeChecker" class="form-control form-control-sm" required="required" >
                                                    <%=ddlOffice%>
                                                </select>
                                            </div> 
                                        </div>
                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                            <label for="txtFromDate">From Date</label>
                                            <div class="input-group mb-3">
                                                <input type="date" id="txtFromDate" name="txtFromDate" class="form-control form-control-sm" />
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                            <label for="txtToDate">Name</label>
                                            <div class="input-group mb-3">
                                                <input type="date" id="txtToDate" name="txtToDate" class="form-control form-control-sm" />
                                            </div>
                                        </div>

                                        <div class="form-group col-md-1 align-self-end">
                                            <div class="input-group mb-3 ">
                                                <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >
                                                <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i>
                                                </button>
                                            </div>
                                        </div>
                                    </div>



                                    <div class="row d-none" id="benListDiv">
                                        <div class="input-group mb-3 col-md-12">
                                            <table id="beneficiaryList" class="table table-sm table-striped table-bordered " style="width:100%">
                                                <thead class="bg-warning">
                                                    <!--                                                    <tr>
                                                                                                            <th colspan="3">
                                                                                                                <span id="schemeNameSpan"></span> 
                                                                                                            </th> 
                                                                                                            <th colspan="3">
                                                                                                                <span id="dateRangeSpan"></span>
                                                                                                            </th>
                                                                                                            <th colspan="3">
                                                                                                                <span id="reportDateSpan"></span>
                                                                                                            </th>
                                                                                                        </tr>-->
                                                    <tr> 
                                                        <th>Sl No.</th>
                                                        <th>Office Name</th>
                                                        <th>Total Applications</th>
                                                        <th>Verified</th>
                                                        <th>Pending Verification</th>
                                                        <th>Approved</th>
                                                        <th>Pending Approval</th>
                                                        <th>Defective</th>
                                                        <th>Rejected</th>                                                        
                                                    </tr>
                                                </thead> 
                                                <tbody>

                                                </tbody>
                                                <tfoot class="bg-dark text-white">
                                                    <tr>
                                                        <th></th>
                                                        <th style="text-align:right">Total</th>
                                                        <th></th>
                                                        <th></th>
                                                        <th></th>
                                                        <th></th>
                                                        <th></th>
                                                        <th></th>
                                                        <th></th>
                                                    </tr>
                                                </tfoot>
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
                                                        $('#btnSearch').on('click', function (e) {

                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var schemeName = $('#ddlSchemesName').val();
                                                            var officeId = $('#hdnOfficeId').val();
                                                            var checkerOfficeId = $('#ddlOfficeChecker option:selected').val();
                                                            var fromDate = $('#txtFromDate').val();
                                                            var toDate = $('#txtToDate').val();
                                                            var today = new Date();
                                                            var fromDatePrint = " ", toDatePrint = " ";



                                                            if (fromDate.length > 0) {
                                                                fromDatePrint = changeDateFormat(fromDate);
                                                            }


                                                            if (toDate.length > 0) {
                                                                toDatePrint = changeDateFormat(toDate);
                                                            }
                                                            var todayPrint = changeDateFormat(today);

                                                            if (schemeId === "") {
                                                                alert("Kindly Select Scheme for searching beneficiaries");
                                                                return false;
                                                            }

                                                            $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                            $('#beneficiaryList').DataTable().clear().destroy();
                                                            var x = 0;
                                                            $('#beneficiaryList').DataTable({
                                                                "ajax": {
                                                                    "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?bensumrptchk=1&schemeId=" + schemeId + "&officeId=" + officeId + "&checkerOfficeId=" + checkerOfficeId + "&fromDate=" + fromDate + "&toDate=" + toDate + "",
                                                                    "type": "POST"
                                                                },
                                                                "iDisplayLength": 50,
                                                                'order': [[0, 'asc']],
                                                                'responsive': true,
                                                                "columns": [
                                                                    {"data": null , render: function (data, type, row, meta) {
                                                                            return meta.row + meta.settings._iDisplayStart + 1;
                                                                        },"width": "2%"},
                                                                    {"data": "office","width": "25%"},
                                                                    //{"data": "total_beneficiary"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  data.total_beneficiary ; //+ (data.defective_beneficiary + data.rejected_beneficiary)
                                                                        }},
                                                                    {"data": "verified_beneficiary"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  data.total_beneficiary - (data.verified_beneficiary + data.defective_beneficiary + data.rejected_beneficiary);
                                                                            //return  data.total_beneficiary - data.verified_beneficiary ;
                                                                        }},
                                                                    {"data": "approved_beneficiary"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  data.verified_beneficiary - data.approved_beneficiary;
                                                                        }},
                                                                    {"data": "defective_beneficiary"},
                                                                    {"data": "rejected_beneficiary"}

                                                                ],
                                                                "dom": 'Bfrtip',
                                                                "buttons": [
                                                                    {extend: 'print', footer: true, messageTop: 'Scheme Name : ' + schemeName + ' \n Report From : ' + fromDatePrint + '    To :' + toDatePrint + '', messageBottom: 'Report Generation Date : ' + todayPrint + ''},
                                                                    //{extend: 'print', header: true},
                                                                    {extend: 'excelHtml5', footer: true, messageTop: 'Scheme Name : ' + schemeName + ' \n Report From : ' + fromDatePrint + '    To :' + toDatePrint + '', messageBottom: 'Report Generation Date : ' + todayPrint + ''},
                                                                    {extend: 'pdfHtml5', footer: true, messageTop: 'Scheme Name : ' + schemeName + ' \n Report From : ' + fromDatePrint + '    To :' + toDatePrint + '', messageBottom: 'Report Generation Date : ' + todayPrint + ''}
                                                                ],
                                                                "footerCallback": function (row, data, start, end, display) {
                                                                    //console.log(data);
                                                                    var api = this.api(), data;

                                                                    // Remove the formatting to get integer data for summation
                                                                    var intVal = function (i) {
                                                                        return typeof i === 'string' ?
                                                                                i.replace(/[\$,]/g, '') * 1 :
                                                                                typeof i === 'number' ?
                                                                                i : 0;
                                                                    };

                                                                    // Total over all pages
                                                                    var totalBen = api.column(2).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);
                                                                    var verBen = api.column(3).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);
                                                                    var penVerBen = api.column(4).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);
                                                                    var apprvBen = api.column(5).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);
                                                                    var penApprvBen = api.column(6).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);
                                                                    var defectBen = api.column(7).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);
                                                                    var rejectBen = api.column(8).data().reduce(function (a, b) {
                                                                        return intVal(a) + intVal(b);
                                                                    }, 0);


                                                                    //console.log(totalBen);
                                                                    // Update footer
                                                                    $(api.column(2).footer()).html(
                                                                            totalBen = new Intl.NumberFormat('en-IN').format(totalBen)
                                                                            );

                                                                    $(api.column(3).footer()).html(
                                                                            verBen = new Intl.NumberFormat('en-IN').format(verBen)
                                                                            );
                                                                    $(api.column(4).footer()).html(
                                                                            penVerBen = new Intl.NumberFormat('en-IN').format(penVerBen)
                                                                            );
                                                                    $(api.column(5).footer()).html(
                                                                            apprvBen = new Intl.NumberFormat('en-IN').format(apprvBen)
                                                                            );
                                                                    $(api.column(6).footer()).html(
                                                                            penApprvBen = new Intl.NumberFormat('en-IN').format(penApprvBen)
                                                                            );
                                                                    $(api.column(7).footer()).html(
                                                                            defectBen = new Intl.NumberFormat('en-IN').format(defectBen)
                                                                            );
                                                                    $(api.column(8).footer()).html(
                                                                            rejectBen = new Intl.NumberFormat('en-IN').format(rejectBen)
                                                                            );

                                                                    $(api.column(2).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                                                    $(api.column(3).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                                                    $(api.column(4).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                                                    $(api.column(5).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                                                    $(api.column(6).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                                                    $(api.column(7).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                                                    $(api.column(8).footer()).removeClass('dt-body-right').addClass('dt-head-right');

                                                                }

                                                            });
                                                            $('#beneficiaryList_wrapper').addClass("col-md-12");
//                                                            $('#schemeNameSpan').html("Scheme Name : <strong>" + schemeName + "</strong>");
//                                                            $('#reportDateSpan').html("Report Generation Date : <strong>" + currentDate + "</strong>");
//                                                            $('#dateRangeSpan').html("Report From : <strong>" + fromDate + "</strong> To: <strong>" + toDate + "</strong>");
                                                        });
                                                        //$(document).ready(function () {
                                                        $('#ddlSchemes').select2();
                                                        //});

                                                    });


    </script>
    <%

        } else {
            response.sendRedirect("../login.jsp");
        }
    %>
</html>