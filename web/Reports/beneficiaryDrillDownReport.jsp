<%-- 
    Document   : beneficiaryDrillDownReport
    Created on : May 18, 2020, 3:16:17 PM
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
        <title>BMS, Tripura || Beneficiary Drilled Down Report</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" type="text/css" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"> 
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.21/css/dataTables.jqueryui.min.css"> 
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
                        <div class="card-body bg-form">
                            <div class="row">
                                <div class="form-group  col-sm-12 col-md-6 col-lg-4">
                                    <label for="ddlFinYear">View By Financial Year</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlFinYear" name="ddlFinYear" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                            <%= finYear%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-12 col-md-6 col-lg-2  align-self-center">
                                    &nbsp;&nbsp;&nbsp;&nbsp;<strong>AND / OR</strong>
                                </div>
                                <div class="form-group  col-sm-12 col-md-6 col-lg-4">
                                    <label for="ddlFinYear">Search By Ration Card Number</label>
                                    <div class="input-group mb-3">
                                        <input type="text" id="txtRcNo" name="txtRcNo" pattern="[0-9]*" class="form-control form-control-sm"/>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-12 col-md-6 col-lg-2 align-self-end">
                                    <div class="input-group mb-3 ">
                                        <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                            <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                                        </button>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header bg-info">
                            <h4 class="text-white">Beneficiary Drilled Down Report</h4>
                        </div>
                        <div class="card-body bg-form">
                            <table cellpadding="0" cellspacing="0" border="1" class="table table-sm table-bordered table-hover small nowrap"  id="distTab">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>District</th>
                                        <th class = "dt-head-right">Beneficiary Count</th>
                                        <th class = "dt-head-right">Transaction Count</th>
                                        <th class = "dt-head-right">Total Amount Disbursed</th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                                <tfoot>
                                    <tr>
                                        <th></th>
                                        <th style="text-align:right">Total:</th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                    </tr>
                                </tfoot>
                            </table>
                            <table cellpadding="0" cellspacing="0" border="1" class="table table-sm table-bordered table-hover small nowrap"  id="subDivTab"  style="display:none;">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Sub Division</th>
                                        <th class = "dt-head-right">Beneficiary Count</th>
                                        <th class = "dt-head-right">Transaction Count</th>
                                        <th class = "dt-head-right">Total Amount Disbursed</th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>


                            <table cellpadding="0" cellspacing="0" border="1" class="table table-sm table-bordered table-hover small nowrap"  id="blockTab"  style="display:none;">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Block</th>
                                        <th class = "dt-head-right">Beneficiary Count</th>
                                        <th class = "dt-head-right">Transaction Count</th>
                                        <th class = "dt-head-right">Total Amount Disbursed</th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>

                            <table cellpadding="0" cellspacing="0" border="1" class="table table-sm table-bordered table-hover small nowrap"  id="gpTab"  style="display:none;">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Gram Panchayat</th>
                                        <th class = "dt-head-right">Beneficiary Count</th>
                                        <th class = "dt-head-right">Transaction Count</th>
                                        <th class = "dt-head-right">Total Amount Disbursed</th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>

                            <table cellpadding="0" cellspacing="0" border="0" class="table table-sm table-bordered table-hover small"  id="beneFamilyTab" style="display:none;">
                                <thead>
                                    <tr>                                      
                                        <th></th>
                                        <th>RC Number</th>
                                        <th>HOF Name(RC Member ID)</th>                                        
                                        <th>Financial Year</th>                                        
                                        <th>No. of Schemes</th>
                                        <th class = "dt-head-right">Total DBT Amount</th>                                       

                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table> 

                            <table cellpadding="0" cellspacing="0" border="0" class="table table-sm table-bordered table-hover small"  id="schemeBenefitTab" style="display:none;">
                                <thead>
                                    <tr>                                      
                                        <th>#</th>
                                        <th>Scheme Name</th>                                        
                                        <th>Financial Year</th>
                                        <th class = "dt-head-right">DBT Amount</th> 
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table> 

                            <table cellpadding="0" cellspacing="0" border="0" class="table table-sm table-bordered table-hover small"  id="beneficiaryTab" style="display:none;">
                                <thead>
                                    <tr>                                      
                                        <th>#</th>
                                        <th>RC Member ID</th>
                                        <th>Beneficiary Name</th>                                        
                                        <th>Financial Year</th>                                        
                                        <th>Scheme Name</th>
                                        <th>Installment</th>
                                        <th class = "dt-head-right">DBT Amount</th>                                     

                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table> 

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
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>
<script type="text/javascript"  src="https://cdn.datatables.net/1.10.21/js/dataTables.jqueryui.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.rowsGroup.min.js"></script>



<script type="text/javascript">
    function fnFormatDetails(itable, pos) {
        var sOut = "<table id=\"inner" + pos + "\">";
        sOut += itable;
        sOut += "</table>";
        return sOut;
    }

    function fnFormatDetails1(jtable, pos1, pos2, lvl) {
        var sOut = "<table id=\"doubleinner" + lvl + '_' + pos1 + '_' + pos2 + "\">";
        sOut += jtable;
        sOut += "</table>";
        return sOut;
    }

    function get_json_data(id, parent_id) { //, callback
        var html_code = '';
        //console.log(id);
        //console.log(parent_id);
        //$.getJSON(entityListJson, function (data) {
        //console.log(data);
        $.each(JSON.parse(entityListJson), function (key, value) {
            if (value.parent_code === parent_id) {
                html_code += "<tr>" +
                        "<td class= 'control center'><i class='fa fa-plus'><input type='hidden' value='" + value.dist_code + "'></i></td>" +
                        "<td>" + value.dist_name + "</td>" +
                        "<td class = \"dt-body-right\">" + value.ben_count + "</td>" +
                        "<td class = \"dt-body-right\">" + value.ben_tran + "</td>" +
                        "<td class = \"dt-body-right\">" + new Intl.NumberFormat('en-IN').format(value.amount) + "</td>" +
                        "</tr>";
            }
        });
        //console.log(html_code);
        $('#' + id + ' tbody').html(html_code);
        //});

        //callback();
    }

    function get_json_ben_family(id, parent_id) { //, callback
        var html_code = '';
        var i = 1;
        //console.log(id);
        //console.log(parent_id);
        //$.getJSON(entityListJson, function (data) {
        //console.log(data);

        $.each(JSON.parse(benFamilyListJson), function (key, value) {
            if (value.parent_code === parent_id) {
                html_code += "<tr>" +
                        "<td class= 'control center'><i class='fa fa-plus'><input type='hidden' value='" + value.rationcard_number + "'></i></td>" +
                        "<td>" + value.rationcard_number + "</td>" +
                        "<td>" + value.beneficiary_name + "(" + value.rationcard_memberid + ")" + "</td>" +
                        "<td>" + value.financial_year + "</td>" +
                        "<td>" + value.scheme_count + "</td>" +
                        "<td class = \"dt-body-right\">" + value.amount + "</td>" +
                        //"<td class= 'control center' colspan='4'><i class='fa fa-info-circle'><input type='hidden' value='" + parent_id + "'></i>View Details</td>" +
                        "</tr>";
            }
            i++;
        });
        console.log(html_code);
        $('#' + id + ' tbody').html(html_code);
        //});

        //callback();
    }

    function get_json_scheme_view(id, parent_id) { //, callback
        var html_code = '';
        var i = 1;
        //console.log(id);
        //console.log(parent_id);
        //$.getJSON(entityListJson, function (data) {
        //console.log(data);

        $.each(JSON.parse(schemeBenefitJson), function (key, value) {
            if (value.parent_code === parent_id) {
                html_code += "<tr>" +
                        "<td>" + i + "</td>" +
                        "<td>" + value.scheme_name + "</td>" +
                        "<td>" + value.financial_year + "</td>" +
                        "<td class = \"dt-body-right\">" + value.amount + "</td>" +
                        "</tr>";
            }
            i++;
        });
        html_code += "<tr>" +
                "<td class= 'control center' colspan='4'><button type='button' id='btnViewMore' name='btnViewMore' class='btn btn-primary'>View More</button></td>" +
                "</tr>";
        console.log(html_code);
        $('#' + id + ' tbody').html(html_code);
        //});

        //callback();
    }

    function get_json_ben_view(id, parent_id) { //, callback
        var html_code = '';
        var i = 1;
        //console.log(id);
        //console.log(parent_id);
        //$.getJSON(entityListJson, function (data) {
        //console.log(data);

        $.each(JSON.parse(benListJson), function (key, value) {
            if (value.parent_code === parent_id) {
                html_code += "<tr>" +
                        "<td>" + i + "</td>" +
                        "<td>" + value.rationcard_memberid + "</td>" +
                        "<td>" + value.beneficiary_name + "</td>" +
                        //"<td>" + value.gender + "</td>" +
                        //"<td>" + value.category + "</td>" +
                        "<td>" + value.financial_year + "</td>" +
                        "<td>" + value.scheme_name + "</td>" +
                        "<td> Installment " + value.installment + "</td>" +
                        "<td class = \"dt-body-right\">" + value.amount + "</td>" +
                        "</tr>";
            }
            i++;
        });
        html_code += "<tr>" +
                "<td class= 'control center' colspan='4'><button type='button' id='btnViewLess' name='btnViewLess' class='btn btn-primary'>View Less</button></td>" +
                "</tr>";
        console.log(html_code);
        $('#' + id + ' tbody').html(html_code);
        //});

        //callback();
    }

    var oTable;
    var entityListJson = "";
    var benFamilyListJson = "";
    var schemeBenefitJson = "";
    var benListJson = "";
    var anOpen = [];
    var anOpen1 = [];
    var anOpen2 = [];
    var anOpen3 = [];
    var anOpen4 = [];
    var anOpen5 = [];

    var oInnerTable4;
    var oInnerTable5;

    $(document).ready(function ()
    {
        $('#btnSearch').on('click', function (e) {
            var fYear = $('#ddlFinYear  option:selected').val();
            var rcNo = $('#txtRcNo').val();

            if (rcNo.length > 0) {
                if (rcNo.length !== 12) {
                    alert("Kindly Enter a Valid Ration Card Number");
                    return false;
                }
            }
            var reqType = "0";

            if (fYear.length > 0 && rcNo.length > 0) {
                reqType = "4&fYear=" + fYear + "&rcNo=" + rcNo;
            } else if (fYear.length === 0 && rcNo.length > 0) {
                reqType = "3&rcNo=" + rcNo;
            } else if (fYear.length > 0 && rcNo.length === 0) {
                reqType = "2&fYear=" + fYear;
            } else if (fYear.length === 0 && rcNo.length === 0) {
                reqType = "1";
            }


            $('#distTab').DataTable().clear().destroy();

            $.ajax({
                url: "${pageContext.request.contextPath}/DrilledDownReport?ddr=" + reqType,
                type: "POST",
                success: function (result) {
                    if (result !== null) {
                        entityListJson = result;
                        get_json_data('distTab', '0');
                        oTable = $('#distTab').dataTable({
                            "bRetrieve": true,
                            "bJQueryUI": true,
                            "bSort": false,
                            "bFilter": false,
                            "lengthChange": false,
                            "bPaginate": false,
                            "orderFixed": [1, 'asc'],
                            "iDisplayLength": 8,
                            "sPaginationType": "full_numbers",
                            "sDom": '<"F"lfT>t<"F"p>',
                            "columnDefs": [
                                {"targets": [2, 3, 4], "className": 'dt-body-right'}
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
                                var totalBen = api
                                        .column(2)
                                        .data()
                                        .reduce(function (a, b) {
                                            return intVal(a) + intVal(b);
                                        }, 0);

                                var totalTran = api
                                        .column(3)
                                        .data()
                                        .reduce(function (a, b) {
                                            return intVal(a) + intVal(b);
                                        }, 0);

                                var totalAmnt = api
                                        .column(4)
                                        .data()
                                        .reduce(function (a, b) {
                                            return intVal(a) + intVal(b);
                                        }, 0);

                                //console.log(totalBen);
                                // Update footer
                                $(api.column(2).footer()).html(
                                        totalBen = new Intl.NumberFormat('en-IN').format(totalBen)
                                        );

                                $(api.column(3).footer()).html(
                                        totalTran = new Intl.NumberFormat('en-IN').format(totalTran)
                                        );
                                $(api.column(4).footer()).html(
                                        totalAmnt = new Intl.NumberFormat('en-IN', {
                                            style: 'currency',
                                            currency: 'INR'
                                        }).format(totalAmnt)
                                        );
                                $(api.column(2).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                $(api.column(3).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                                $(api.column(4).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                            }
                        });
                        oTable.find("thead tr th").removeClass("ui-state-default").addClass("bg-page0");
                        oTable.find("tbody tr td").addClass("bg-td0");
                    }
                }
            });


        });

        $.ajax({
            url: "${pageContext.request.contextPath}/DrilledDownReport?ddr=1",
            type: "POST",
            success: function (result) {
                if (result !== null) {
                    entityListJson = result;
                    get_json_data('distTab', '0');
                    oTable = $('#distTab').dataTable({
                        "bRetrieve": true,
                        "bJQueryUI": true,
                        "bSort": false,
                        "bFilter": false,
                        "lengthChange": false,
                        "bPaginate": false,
                        "orderFixed": [1, 'asc'],
                        "iDisplayLength": 8,
                        "sPaginationType": "full_numbers",
                        "sDom": '<"F"lfT>t<"F"p>',
                        "columnDefs": [
                            {"targets": [2, 3, 4], "className": 'dt-body-right'}

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
                            var totalBen = api
                                    .column(2)
                                    .data()
                                    .reduce(function (a, b) {
                                        return intVal(a) + intVal(b);
                                    }, 0);

                            var totalTran = api
                                    .column(3)
                                    .data()
                                    .reduce(function (a, b) {
                                        return intVal(a) + intVal(b);
                                    }, 0);

                            var totalAmnt = api
                                    .column(4)
                                    .data()
                                    .reduce(function (a, b) {
                                        return intVal(a) + intVal(b);
                                    }, 0);

                            //console.log(totalBen);
                            // Update footer
                            $(api.column(2).footer()).html(
                                    totalBen = new Intl.NumberFormat('en-IN').format(totalBen)
                                    );

                            $(api.column(3).footer()).html(
                                    totalTran = new Intl.NumberFormat('en-IN').format(totalTran)
                                    );
                            $(api.column(4).footer()).html(
                                    totalAmnt = new Intl.NumberFormat('en-IN', {
                                        style: 'currency',
                                        currency: 'INR'
                                    }).format(totalAmnt)
                                    );
                            $(api.column(2).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                            $(api.column(3).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                            $(api.column(4).footer()).removeClass('dt-body-right').addClass('dt-head-right');
                        }
                    });
                    oTable.find("thead tr th").removeClass("ui-state-default").addClass("bg-page0");
                    oTable.find("tbody tr td").addClass("bg-td0");
                }
            }
        });

        //var itable = $('#distTab').html();





        $('#distTab').on("click", "td.control", function ()
        {
            var nTr = this.parentNode;
            var i = $.inArray(nTr, anOpen);
            if (i === -1)
            {
                var iPos = oTable.fnGetPosition(nTr);
                //console.log(oTable);
                //console.log(iPos);
                //var tr = $(this).closest("tr");
                //iPos = tr.index();
                //iPos = oTable.row(this).index();
                //console.log(iPos);
                //$('img', this).attr('alt', "-");
                $('i', this).attr('class', "fa fa-minus");
                //console.log($('input', this).val());
                get_json_data('subDivTab', $('input', this).val());//, function () {
                var itable = $('#subDivTab').html();
                //console.log(mtable);
                //console.log(nTr);


                //var nDetailsRow = oTable.fnOpen(nTr, fnFormatDetails(itable, iPos), 'details');
                var nDetailsRow = oTable.fnOpen(nTr, fnFormatDetails(itable, iPos), 'details');
                console.log(nDetailsRow);
                $('div.innerDetails', nDetailsRow).slideDown();
                anOpen.push(nTr);
                var oInnerTable = $('#inner' + iPos).dataTable({
                    "bRetrieve": true,
                    "bJQueryUI": true,
                    "bSort": false,
                    "bFilter": false,
                    "lengthChange": false,
                    "bPaginate": false,
                    "orderFixed": [1, 'asc'],
                    "iDisplayLength": 5,
                    "sPaginationType": "full_numbers",
                    "sDom": '<"F"lfT>t<"F"p>'
                });


                oInnerTable.find("thead tr th").removeClass("ui-state-default").addClass("bg-page1");
                oInnerTable.find("tbody tr td").addClass("bg-td1");
                //2nd Level Drilldown starts here                                                         
                $('#inner' + iPos).on("click", "td.control", function ()
                {
                    var nTr1 = this.parentNode;
                    var j = $.inArray(nTr1, anOpen1);
                    if (j === -1)
                    {
                        var jPos = oInnerTable.fnGetPosition(nTr1);
                        $('i', this).attr('class', "fa fa-minus");
                        get_json_data('blockTab', $('input', this).val());
                        var jtable = $('#blockTab').html();

                        var nDetailsRow1 = oInnerTable.fnOpen(nTr1, fnFormatDetails1(jtable, iPos, jPos, 2), 'details');
                        $('div.innerDetails', nDetailsRow1).slideDown();
                        anOpen1.push(nTr1);
                        var oInnerTable1 = $('#doubleinner2_' + iPos + '_' + jPos).dataTable({
                            "bRetrieve": true,
                            "bJQueryUI": true,
                            "bSort": false,
                            "bFilter": false,
                            "lengthChange": false,
                            "bPaginate": false,
                            "orderFixed": [1, 'asc'],
                            "iDisplayLength": 5,
                            "sPaginationType": "full_numbers",
                            "sDom": '<"F"lfT>t<"F"p>'
                        });
                        oInnerTable1.find("thead tr th").removeClass("ui-state-default").addClass("bg-page2");
                        oInnerTable1.find("tbody tr td").addClass("bg-td2");
                        //3rd Level Drilldown starts here 
                        $('#doubleinner2_' + iPos + '_' + jPos).on("click", "td.control", function ()
                        {
                            var nTr2 = this.parentNode;
                            var k = $.inArray(nTr2, anOpen2);
                            if (k === -1)
                            {
                                var kPos = oInnerTable1.fnGetPosition(nTr2);
                                $('i', this).attr('class', "fa fa-minus");
                                get_json_data('gpTab', $('input', this).val());
                                var ktable = $('#gpTab').html();

                                var nDetailsRow1 = oInnerTable1.fnOpen(nTr2, fnFormatDetails1(ktable, jPos, kPos, 3), 'details');
                                $('div.innerDetails', nDetailsRow1).slideDown();
                                anOpen2.push(nTr2);
                                var oInnerTable2 = $('#doubleinner3_' + jPos + '_' + kPos).dataTable({
                                    "bRetrieve": true,
                                    "bJQueryUI": true,
                                    "bSort": false,
                                    "bFilter": false,
                                    "lengthChange": false,
                                    "bPaginate": false,
                                    "orderFixed": [1, 'asc'],
                                    "iDisplayLength": 5,
                                    "sPaginationType": "full_numbers",
                                    "sDom": '<"F"lfT>t<"F"p>'
                                });
                                oInnerTable2.find("thead tr th").removeClass("ui-state-default").addClass("bg-page3");
                                oInnerTable2.find("tbody tr td").addClass("bg-td3");
                                //4th Level Drilldown starts here



                                $('#doubleinner3_' + jPos + '_' + kPos).on("click", "td.control", function ()
                                {
                                    var nTr3 = this.parentNode;
                                    var l = $.inArray(nTr3, anOpen3);
                                    if (l === -1)
                                    {
                                        var lPos = oInnerTable2.fnGetPosition(nTr3);
                                        $('i', this).attr('class', "fa fa-minus");
                                        var gpCode = $('input', this).val();

                                        $.ajax({
                                            url: "${pageContext.request.contextPath}/DrilledDownReport?ddb=1&gpCode=" + gpCode,
                                            type: "POST",
                                            success: function (result) {
                                                if (result !== null) {
                                                    benFamilyListJson = result;
                                                    console.log(result);
                                                    get_json_ben_family('beneFamilyTab', gpCode);
                                                    var ltable = $('#beneFamilyTab').html();
                                                    //console.log(ltable);
                                                    var nDetailsRow1 = oInnerTable2.fnOpen(nTr3, fnFormatDetails1(ltable, kPos, lPos, 4), 'details');
                                                    $('div.innerDetails', nDetailsRow1).slideDown();
                                                    anOpen3.push(nTr3);
                                                    var oInnerTable3 = $('#doubleinner4_' + kPos + '_' + lPos).dataTable({
                                                        "bRetrieve": true,
                                                        "bJQueryUI": true,
                                                        "bSort": false,
                                                        "iDisplayLength": 50,
                                                        "orderFixed": [2, 'asc'],
                                                        //"rowsGroup": [1, 2, 3, 4, 5, 6],
                                                        /*"rowGroup": {
                                                         "dataSrc": 1,
                                                         "startRender": function (rows, group) {
                                                         if (group === '-1') {
                                                         rows.nodes().to$().addClass('border border-bottom border-primary');
                                                         }
                                                         return group;
                                                         }
                                                         },*/
                                                        "sPaginationType": "full_numbers",
                                                        "sDom": '<"F"lfT>t<"F"p>'
                                                    });
                                                    oInnerTable3.find("thead tr th").removeClass("ui-state-default").addClass("bg-page4 text-white");
                                                    oInnerTable3.find("tbody tr td").addClass("p-1");



                                                    //5th Level Drilldown starts here

                                                    
                                                    $('#doubleinner4_' + kPos + '_' + lPos).on("click", "td.control", function ()
                                                    {
                                                        var nTr4 = this.parentNode;
                                                        var m = $.inArray(nTr4, anOpen4);
                                                        if (m === -1)
                                                        {
                                                            var mPos = oInnerTable3.fnGetPosition(nTr4);
                                                            $('i', this).attr('class', "fa fa-minus");
                                                            var rcCode = $('input', this).val();

                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/DrilledDownReport?ddb=2&rcCode=" + rcCode,
                                                                type: "POST",
                                                                success: function (result) {
                                                                    if (result !== null) {
                                                                        
                                                                        //scheme view layer
                                                                        schemeBenefitJson = result.schemeList;                                                                        
                                                                        get_json_scheme_view('schemeBenefitTab', rcCode);
                                                                        var mtable = $('#schemeBenefitTab').html(); 
                                                                        
                                                                        var nDetailsRow1 = oInnerTable3.fnOpen(nTr4, fnFormatDetails1(mtable, lPos, mPos, 5), 'details');
                                                                        $('div.innerDetails', nDetailsRow1).slideDown();
                                                                        anOpen4.push(nTr4);
                                                                        oInnerTable4 = $('#doubleinner5_' + lPos + '_' + mPos).dataTable({
                                                                            "bRetrieve": true,
                                                                            "bJQueryUI": true,
                                                                            "bSort": false,
                                                                            "iDisplayLength": 50,
                                                                            //"orderFixed": [2, 'asc'],
                                                                            "rowsGroup": [1, 2, 3],
                                                                            "sPaginationType": "full_numbers",
                                                                            "sDom": '<"F"lfT>t<"F"p>'
                                                                        });

                                                                        oInnerTable4.find("thead tr th").removeClass("ui-state-default").addClass("bg-page5 text-white");
                                                                        oInnerTable4.find("tbody tr td").addClass("p-1");

                                                                        //beneficiary view layer                                                                        
                                                                        benListJson = result.beneficiaryList;                                                                        
                                                                        get_json_ben_view('beneficiaryTab', rcCode)
                                                                        var ntable = $('#beneficiaryTab').html();                                                                        
                                                                        
                                                                        var nDetailsRow2 = oInnerTable3.fnOpen(nTr4, fnFormatDetails1(ntable, lPos, mPos, 6), 'details');
                                                                        $('div.innerDetails', nDetailsRow2).slideDown();
                                                                        anOpen4.push(nTr4);
                                                                        oInnerTable5 = $('#doubleinner6_' + lPos + '_' + mPos).dataTable({
                                                                            "bRetrieve": true,
                                                                            "bJQueryUI": true,
                                                                            "bSort": false,
                                                                            "iDisplayLength": 50,
                                                                            //"orderFixed": [2, 'asc'],
                                                                            "rowsGroup": [1, 2, 3, 4],
                                                                            "sPaginationType": "full_numbers",
                                                                            "sDom": '<"F"lfT>t<"F"p>'
                                                                        });
                                                                        oInnerTable5.find("thead tr th").removeClass("ui-state-default").addClass("bg-page5 text-white");
                                                                        oInnerTable5.find("tbody tr td").addClass("p-1");
                                                                        oInnerTable5.hide();

                                                                    }
                                                                }
                                                            });

                                                        } else
                                                        {
                                                            $('i', this).attr('class', "fa fa-plus");
                                                            oInnerTable3.fnClose(this.parentNode);
                                                            anOpen4.splice(m, 1);
                                                        }
                                                        event.stopPropagation();
                                                    });

                                                    //5th Level Drilldown ends here



                                                }
                                            }
                                        });

                                    } else
                                    {
                                        $('i', this).attr('class', "fa fa-plus");
                                        oInnerTable2.fnClose(this.parentNode);
                                        anOpen3.splice(l, 1);
                                    }
                                    event.stopPropagation();
                                });



                                //4th Level Drilldown ends here
                            } else
                            {
                                $('i', this).attr('class', "fa fa-plus");
                                oInnerTable1.fnClose(this.parentNode);
                                anOpen2.splice(k, 1);
                            }
                            event.stopPropagation();
                        });
                        //3rd Level Drilldown ends here

                    } else
                    {
                        $('i', this).attr('class', "fa fa-plus");
                        oInnerTable.fnClose(this.parentNode);
                        anOpen1.splice(j, 1);
                    }
                    event.stopPropagation();
                });
                //2nd Level Drilldown ends here  

                //});


            } else
            {
                $('i', this).attr('class', "fa fa-plus");
                oTable.fnClose(this.parentNode);
                anOpen.splice(i, 1);
                event.stopPropagation();
            }
        });


        $('#btnViewMore').on("click", function () {
            oInnerTable4.hide();
            oInnerTable5.show();
        });
        $('#btnViewLess').on("click", function () {
            oInnerTable5.hide();
            oInnerTable4.show();
        });

    });

</script> 