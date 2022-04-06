<%-- 
    Document   : beneficiaryApplicationStatusReport
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
        String scheme_code = cm.getSchemeCode(schemeId);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Beneficiary Status Report</title>
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
                                    <h4 class="text-white">Beneficiary Status Report:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <div class="row">
                                        <input type="hidden" name="officeId" id="officeId" value=<%= officeId%> />
                                        <input type="hidden" name="roleId" id="roleId" value=<%= roleId%> />
                                        <input type="hidden" name="ddlSchemes" id="ddlSchemes" value=<%= schemeId + "~" + scheme_code%> />
                                        <!--                                        <div class="form-group  col-sm-3 col-md-3 col-lg-2">
                                                                                    <label for="ddlSchemes">Scheme</label>
                                                                                    <div class="input-group mb-3">                                                                                
                                                                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required">                                            
                                        <%=ddlSchemes%>                                                    
                                    </select>
                                </div>
                            </div>-->
                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                            <label for="txtRCMem">RC Member ID</label>
                                            <div class="input-group mb-3">
                                                <input type="text" id="txtRCMem" name="txtRCMem" class="form-control form-control-sm" />
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                            <label for="txtRCMem">Application ID</label>
                                            <div class="input-group mb-3">
                                                <input type="text" id="txtAppl" name="txtAppl" class="form-control form-control-sm" />
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                            <label for="txtRCMem">Mobile No.</label>
                                            <div class="input-group mb-3">
                                                <input type="text" id="txtMobile" name="txtMobile" class="form-control form-control-sm" />
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                            <label for="txtName">Name</label>
                                            <div class="input-group mb-3">
                                                <input type="text" id="txtName" name="txtName" class="form-control form-control-sm" />
                                            </div>
                                        </div>
                                        <!--                                        <div class="form-group  col-sm-2 col-md-3 col-lg-2">
                                                                                    <label for="ddlStatus">Status</label>
                                                                                    <div class="input-group mb-3">
                                                                                        <select id="ddlStatus" name="ddlStatus" class="form-control form-control-sm" >
                                        <%=ddlStatus%>
                                    </select>
                                </div>
                            </div>-->

                                        <div class="form-group col-md-1 align-self-end">
                                            <div class="input-group mb-3 ">
                                                <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i>
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
                                                        <th>Guardian's Name</th>
                                                        <th>Email ID</th>
                                                        <th>Mobile No</th>
                                                        <th>Address</th>
                                                        <th>Office Name</th>
                                                        <th>Status</th>
                                                        <!--<th>Timeline</th>-->
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
                var schemeId = $('#ddlSchemes').val();
                var rcMem = $('#txtRCMem').val();
                var appl = $('#txtAppl').val();
                var benName = $('#txtName').val();
                var mobile_no = $('#txtMobile').val();
                var roleId = $('#roleId').val();
                var officeId=$('#officeId').val();

                if (rcMem === "" && appl === "" && benName === "" && mobile_no === "") {
                    alert("Enter any of the value for searching beneficiaries");
                    return false;
                }

                $('#benListDiv').removeClass("d-none").addClass("d-block");

                $('#beneficiaryList').DataTable().clear().destroy();
                $('#beneficiaryList').DataTable({
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/BeneficiarySearchDataAll?benapplstatus=1&schemeId=" + schemeId + "&rcMem=" + rcMem + "&appl=" + appl + "&mobile_no=" + mobile_no + "&benName=" + benName + "&roleId=" + roleId+"&officeId="+officeId,
                        "type": "POST"
                    },

                    "iDisplayLength": 50,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": "rc_mem"},
                        {"data": "full_name"},
                        {"data": "guardian_name"},
                        {"data": "email"},
                        {"data": "mobile"},
                        {"data": function (data, type, dataToSet) {
                                ++i;
                                if (data.district !== '' && data.subdivision !== '' && data.block_mc_np_adc_nonadc !== '' && data.gp_ward_name !== '') {
                                    return  data.district + ", " + data.subdivision + ", " + data.block_mc_np_adc_nonadc + ", " + data.gp_ward_name;
                                } else {
                                    return '';
                                }
                            }, "width": "25%"},
                        {"data": "office"},
                        {"data": "status_name"}
                        /*,
                         {"data": function (data, type, dataToSet) {
                         return  "<button class=\"btn btn-primary btn-sm\" id=\"btnStatTl\" name=\"btnStatTl\" value=\"" + data.rc_mem +"#"+data.ben_type+ "\" data-toggle=\"modal\" data-target=\"#myModal2\" class=\"mt-2\">View Status Timeline</button>";
                         }}*/
                    ]
                });

                $('#beneficiaryList_wrapper').addClass("col-md-12");
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