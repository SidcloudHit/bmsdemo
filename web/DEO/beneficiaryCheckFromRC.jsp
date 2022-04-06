<%-- 
    Document   : beneficiaryCheckFromRC
    Created on : Mar 6, 2020, 3:51:56 PM
    Author     : NIC-Arindam
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

        Common.CommonMethod cm = new CommonMethod();
        int edit = 0;
        String ddlDist = "";
        String ddlLoc = "";
        ddlDist = cm.getDistrict("-Please Select-");
        ddlLoc = cm.getLocation("-Please Select-");
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
        <title>Agriculture, Tripura || Beneficiary Data Entry 2</title>
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
                            <h4 class="text-white">Search Beneficiary:</h4>
                        </div>
                        <div class="card-body bg-form">
                            <div class="row">
                                <!--<div class="form-group col-md-3">                                    
                                        <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1">UID</span>
                                                </div>
                                                <input type="text" id="txtUIDCardNo" name="txtUIDCardNo" class="form-control form-control-sm"/>
                                        </div>
                                </div>-->
                                <div class="form-group col-md-3">
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1">RC Number</span>
                                        </div>
                                        <input type="text" id="txtRationCardNo" name="txtRationCardNo" pattern="[0-9]*" class="form-control form-control-sm" required="required"/>
                                    </div>
                                </div>
                                <div class="form-group col-md-3">
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1">RC Member ID</span>
                                        </div>
                                        <input type="text" id="txtRationCardMem" name="txtRationCardMem" pattern="[0-9]*" class="form-control form-control-sm" required="required"/>
                                    </div>
                                </div>
                                <div class="form-group col-md-3">
                                    <div class="input-group mb-3">
                                        <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                            <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="row d-none" id="benListDiv">
                                <div class="input-group mb-3">
                                    <table id="beneficiaryList" class="table table-sm table-striped table-bordered small dt-responsive nowrap" style="width:100%">
                                        <thead class="bg-warning">
                                            <tr> 
                                                <th>Sl. No.</th>
                                                <th>Card Type/Ration Card No</th>                                 
                                                <th>Member ID</th>                                                
                                                <th>Name</th>
                                                <th>Fathers Name</th>                                                
                                                <!--<th>Aadhaar ID</th> -->                                               
                                                <th>DOB</th>                                                
                                                <th>Gender</th>                                               
                                                <th>Mobile No</th>
                                                <th></th>
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
<script>
    $(document).ready(function () {
        $('#btnSearch').on('click', function (e) {
            var i = 1;
            var auid = "";//$('#txtUIDCardNo').val();
            var rcn = $('#txtRationCardNo').val();
            var rcmem = $('#txtRationCardMem').val();
            if (rcn === "" && rcmem === "") {
                alert("Kindly provide any of the search value");
                return false;
            }

            var param = "";
            if (auid !== "") {
                param = auid + "&type=1";
            } else if (rcn !== "") {
                param = rcn + "&type=2";
            } else if (rcmem !== "") {
                param = rcmem + "&type=3";
            }

            $('#benListDiv').removeClass("d-none").addClass("d-block");

            $('#beneficiaryList').DataTable().clear().destroy();
            var table = $('#beneficiaryList').DataTable({
                "ajax": {
                    "url": "${pageContext.request.contextPath}/AjaxServlet?bsid=" + param,
                    "type": "POST"
                },

                "columnDefs": [
                    {
                        "targets": -1,
                        "responsivePriority": 1,
                        "data": null,
                        "defaultContent": "<button class='btn btn-primary btn-sm' value='1'>Add</button>"
                    }
                ],
                'order': [[1, 'asc']],
                'responsive': true,
                "language": {
                    "zeroRecords": "No records to display <a  href=\"${pageContext.request.contextPath}/DEO/beneficiaryDataEntryNew.jsp\" target=\"_blank\"><button class='btn btn-primary btn-sm' value='1'>Add New Beneficiary</button></a>"
                },
                "columns": [
                    {"data": function (data, type, dataToSet) {
                            return  "<input type=\"hidden\" id=\"hdnBenId_" + i + "\" name=\"hdnBenId" + i + "\" value=\"" + data.uid + "\" />" + i++;
                        }},

                    {"data": function (data, type, dataToSet) {
                            return data.card_type + "/" + data.ration_card_no;
                        }},

                    {"data": "member_id"},
                    {"data": "member_name_en"},
                    {"data": "father_name_en"},
                    {"data": "member_dob"},
                    {"data": "gender"},
                    {"data": "mobile_no"},
                    {"data": null}
                ]
            });

            $('#beneficiaryList_wrapper').addClass("col-md-12");

            $('#beneficiaryList tbody').on('click', 'button', function (e) {
                if (parseInt(this.value) === 1) {
                    var rowData = table.row($(this).parents('tr')).data();
                    $.ajax({
                        url: "${pageContext.request.contextPath}/AjaxServlet?searchBen=1&rc=" + rowData['ration_card_no'] + "&rcm=" + rowData['member_id'] + "&name=" + rowData['member_name_en'], //uid=" + rowData['uid'] + "&
                        type: "POST",
                        success: function (result) {
                            //alert(result);
                            if (parseInt(result) === 0) {
                                $.redirect("${pageContext.request.contextPath}/DEO/beneficiaryDataEntryRC.jsp?rcm=" + rowData['member_id'], null, "POST", "_blank", null, true); //JSON.stringify(params)
                            } else if (parseInt(result) > 0) {
                                $.redirect("${pageContext.request.contextPath}/DEO/beneficiaryDataUpdate.jsp?benid=" + result, null, "POST", "_blank", null, true); //JSON.stringify(params)
                            }
                        }
                    });
                }

            });
        });

    });


</script>
<%
    } else {
        response.sendRedirect("User/userHome.jsp");
    }


%>