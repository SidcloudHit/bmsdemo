<%-- 
    Document   : beneficiarySchemeMapping
    Created on : 28 Jan, 2020, 2:37:03 PM
    Author     : acer
--%>

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
    ddlSchemes = cm.getSchemes("-Please Select-");
    ddlDist = cm.getDistrict("-Please Select-");
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
        <title>Agriculture, Tripura || Beneficiary Selection for DBT Schemes</title>
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
                            <h4 class="text-white">Beneficiary Selection for DBT Schemes:</h4>
                        </div>
                        <div class="card-body bg-form">
                            <div class="row">
                                <div class="form-group col-md-2">
                                    <label for="ddlSchemes">Select Scheme</label>
                                    <div class="input-group mb-3">                                    
                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" >                                            
                                            <%=ddlSchemes%>                                                    
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-2">
                                    <label for="ddlDistrict">District</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlDistrict" name="ddlDistrict" class="form-control form-control-sm" required="required" onchange="resetBlock(1);dependentDropDown('ddlDistrict', 'ddlSubDiv', 'fillSubDiv');">
                                            <%=ddlDist%>                                                    
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">Subdivision</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlSubDiv" name="ddlSubDiv" class="form-control form-control-sm" required="required" onchange="resetBlock(2);dependentDropDown('ddlSubDiv', 'ddlUrbRurBody', 'fillBlock');">
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
                                <div class="form-group col-md-2 align-self-end">
                                    <div class="input-group mb-3">
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
                                                <th></th>
                                                <th>Beneficiary ID</th>
                                                <th>Beneficiary Name</th>
                                                <th>Father's Name</th>
                                                <th>Gender</th>
                                                <th>Mobile</th>
                                                <th>Bank Account Number</th>
                                                <th>IFSC</th>
                                            </tr>
                                        </thead>                                        
                                    </table>
                                </div>
                            </div>
                            <div class="row" id="divSchemeMapping">                                
                                <div class="input-group mb-3 col-md-4">
                                    <button type="button" id="btnMap" name="btnMap" class="btn btn-primary">
                                        <i class="fa fa-search">&nbsp;&nbsp;</i>Map Beneficiaries with Scheme
                                    </button>
                                </div>
                                <div id="msgDiv" class="col-md-4"></div>
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

                                                    var distId = $('#ddlDistrict option:selected').val();
                                                    var subdivId = $('#ddlSubDiv option:selected').val();
                                                    var blockId = $('#ddlUrbRurBody option:selected').val();
                                                    var gpId = $('#ddlWardGp option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();

                                                    if (distId === "" || (subdivId === "" || subdivId === "-1") || schemeId === "") {
                                                        alert("Kindly Select Scheme, District and Subdivision for searching beneficiaries");
                                                        return false;
                                                    }

$('#benListDiv').removeClass("d-none").addClass("d-block");
                                                    $('#beneficiaryList').DataTable().clear().destroy();

                                                    $('#beneficiaryList').DataTable({
                                                        "ajax": {
                                                            "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?map=1&distId=" + distId + "&subdivId=" + subdivId + "&blockId=" + blockId + "&gpId=" + gpId + "&schemeId=" + schemeId,
                                                            "type": "POST"
                                                        },
                                                        "columnDefs": [
                                                            {
                                                                'targets': 0,
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
                                                            {"data": "beneficiary_id"},
                                                            {"data": "beneficiary_id"},
                                                            {"data": "beneficiary_name"},
                                                            {"data": "father_name"},
                                                            {"data": "gender"},
                                                            {"data": "mobile"},
                                                            {"data": "bank_account"},
                                                            {"data": "ifsc"}
                                                        ]
                                                    });

                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                });

                                                // Handle form submission event
                                                $('#btnMap').on('click', function (e) {

                                                    var elem = $('#divSchemeMapping');
                                                    var schemeId = $('#ddlSchemes option:selected').val();


                                                    var table = $('#beneficiaryList').DataTable();
                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                    //var params = table.$('input,select,textarea').serializeArray();
                                                    var rows_selected = table.column(0).checkboxes.selected();

                                                    var id = [];
                                                    // Iterate over all selected checkboxes
                                                    //$.each(rows_selected, function (index, rowId) {
                                                    $.each(rows_selected, function () {
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
                                                        id.push(this);

                                                    });
                                                    var json = JSON.stringify(id);

                                                    $.ajax({
                                                        data: {json: json},
                                                        url: "${pageContext.request.contextPath}/DBTSchemeMapping?schemeId=" + schemeId,
                                                        type: "POST",
                                                        dataType: "json",
                                                        success: function (response, status, xhr) {
                                                            $("#msgDiv").html("Number of beneficiary successfully mapped is " + response);
                                                            $('#btnSearch').click();

                                                        }
                                                    });


                                                });




                                                //$(document).ready(function () {
                                                $('#ddlSchemes').select2();
                                                //});
                                            });


</script>

