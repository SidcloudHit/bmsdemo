<%-- 
    Document   : report
    Created on : 13 May, 2021, 4:18:22 PM
    Author     : acer
--%>

<%@page import="org.json.JSONArray"%>
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
    }

    if (!userId.equals("")) {
        String failedjsonarr = session.getAttribute("failedjsonarray").toString();
        String successjson = session.getAttribute("successjson").toString();
        String headerarr = session.getAttribute("headerarr").toString();
        JSONArray jarr = new JSONArray(successjson);
        JSONArray jarrfailed = new JSONArray(failedjsonarr);
        JSONArray jarrayheader = new JSONArray(headerarr);
        if (!failedjsonarr.equals("")) {
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.7.0/css/buttons.dataTables.min.css">

    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">

                        <div class="row">
                            <div class="col-md-12">
                                <div class="list-group">
                                    <div class="list-group-item list-group-item-success text-center" style="font-size: 0.86rem;">
                                    <%= jarr.length()%> No. of beneficiaries are successfully uploaded.
                                </div>
                                <div class="list-group-item list-group-item-danger text-center" style="font-size: 0.86rem;">
                                    <%= jarrfailed.length()%> No. of beneficiaries are not uploaded.
                                </div>
                            </div> 
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 ml-2">
                            <div class="card">
                                <div class="card-body">
                                    <table id="example" class="display table table-striped" style="font-size: .72rem;" width="100%">

                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-md-6 m-auto">
                            <a href="${pageContext.request.contextPath}/BeneficiarySelection/viewUploadedData.jsp">
                                <button type="button" id="btnNameMismatchPage" name="btnNameMismatchPage" class="btn btn-warning btn-block">
                                    <strong>BENEFICIARY DATA VALIDATION</strong>
                                </button>
                            </a>
                        </div>
                    </div>
                </div>
            </div>          

            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>


    </html>
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


<script charset="utf8" src="https://cdn.datatables.net/buttons/1.7.0/js/dataTables.buttons.min.js"></script>
<script charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script charset="utf8" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script charset="utf8" src="https://cdn.datatables.net/buttons/1.7.0/js/buttons.html5.min.js"></script>
<script charset="utf8" src="https://cdn.datatables.net/buttons/1.7.0/js/buttons.print.min.js"></script>
<script type="text/javascript">
    var dataSet = <%= failedjsonarr%>;
    var headerdata =<%= jarrayheader%>
    console.log(dataSet);
    $(document).ready(function () {
        var extraData = [
            "David",
            "Software Engineer",
            "Tokyo",
            29,
            "04/25/2011",
            320800
        ];
        $('#example').DataTable({
            data: dataSet,
            columns: [
                {title: "Sl No."},
                {title: "Beneficary Name"},
                {title: "Aadhaar"},
                {title: "Ration card Number"},
                {title: "Account No."},
                {title: "IFSC"},
                {title: "District"},
                {title: "Sub-Division"},
                {title: "Block/ MC"},
                {title: "GP/VC/Ward"},
                {title: "Error message"}
            ],
            "columnDefs": [{"targets": 10, "data": 'creator', "render": function (data, type, row) {
                        var result = "";
                        if (row[10] != "") {
                            result = result + row[10]
                        }
                        if (row[11] != "") {
                            result = result + ', ' + row[11]
                        }
                        if (row[12] != "") {
                            result = result + ', ' + row[12]
                        }
                        if (row[13] != "") {
                            result = result + ', ' + row[13]
                        }
                        if (row[14] != "") {
                            result = result + ', ' + row[14]
                        }
                        return result;
                    }}],
            dom: 'Bfrtip',
//            buttons: [
//                 'csv', 'excel'
//            ]
            buttons: [{
                    extend: 'excel',
                    text: 'Save in EXCEL',
                    filename: 'error_list_of_beneficiary',
                    header: false,
                    customize: function (xlsx) {
                        var sheet = xlsx.xl.worksheets['sheet1.xml'];
                        //Bold Header Row

                        $('c[r=A1] t', sheet).text(headerdata[0][0]);
                        //$('c[r=A1] t', sheet).text('Template for Collection of Beneficiary details for the pupose of Beneficiary Management System');
                        $('row[r=3] c', sheet).attr('s', '2');
                        //Make You Input Cells Bold Too
                        $('c[r=A1]', sheet).attr('s', '2');
                        $('c[r=A2]', sheet).attr('s', '2');
                        $('c[r=A3]', sheet).attr('s', '2');
                    },
                    customizeData: function (data) {
                        //We want the first line so we disabled the header above. Let's add in our descriptions. Then we're going to add them to the top of the body and do the bolding ourselves with the customize function.
                        var desc = [
                            [headerdata[1][0], headerdata[1][1], headerdata[1][2], headerdata[1][3], headerdata[1][4], headerdata[1][5]]
                        ];
                        data.body.unshift(data.header);
                        for (var i = 0; i < desc.length; i++) {
                            data.body.unshift(desc[i]);
                        }
                        ;
                    }
//                    extend: 'excel',
//                    text: 'Save in EXCEL',
//                    filename: 'td900',
//                    customize: function (xlsx) {
//                        console.log(xlsx);
//                        var sheet = xlsx.xl.worksheets['sheet1.xml'];
//                        var downrows = 2;
//                        var clRow = $('row', sheet);
//                        //update Row
//                        clRow.each(function () {
//                            var attr = $(this).attr('r');
//                            var ind = parseInt(attr);
//                            ind = ind + downrows;
//                            $(this).attr("r", ind);
//                        });
//
//                        // Update  row > c
//                        $('row c ', sheet).each(function () {
//                            var attr = $(this).attr('r');
//                            var pre = attr.substring(0, 1);
//                            var ind = parseInt(attr.substring(1, attr.length));
//                            ind = ind + downrows;
//                            $(this).attr("r", pre + ind);
//                        });
//
//                        function Addrow(index, data) {
//                            msg = '<row r="' + index + '">'
//                            for (i = 0; i < data.length; i++) {
//                                var key = data[i].k;
//                                var value = data[i].v;
//                                msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
//                                msg += '<is>';
//                                msg += '<t>' + value + '</t>';
//                                msg += '</is>';
//                                msg += '</c>';
//                            }
//                            msg += '</row>';
//                            return msg;
//                        }
//
//                        //insert
//                        var r1 = Addrow(1, [{k: 'A', v: 'Template for Collection of Beneficiary details for the pupose of Beneficiary Management System'}, {k: 'B', v: ''}, {k: 'C', v: ''}]);
//                        var r2 = Addrow(2, [{k: 'A', v: 'Department Name:'},{k: 'B', v: 'Scheme name:'}, {k: 'C', v: 'FY:'}, {k: 'D', v: ''}, {k: 'E', v: 'Installment:'}, {k: 'F', v: ''}]);
//                        //var r3 = Addrow(3, [{k: 'A', v: ''}, {k: 'B', v: 'Scheme name:'}, {k: 'C', v: 'FY:'}]);
//
//                        sheet.childNodes[0].childNodes[1].innerHTML = r1 + r2  + sheet.childNodes[0].childNodes[1].innerHTML;
//                    }


                }]
        });
    });
</script>
<%

        } else {
            response.sendRedirect("../login.jsp");
        }
    } else {
        response.sendRedirect("../login.jsp");
    }
%>