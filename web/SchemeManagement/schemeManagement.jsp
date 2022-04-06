<%-- 
    Document   : schemeManagement
    Created on : 21 Jan, 2022, 12:22:21 PM
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
        int onboarding_id = cm.getOnboardedSchemeId(schemeId);
        String appl_submission_date = "", verification_date = "", approval_date = "";
        JSONArray jsarr = null;
        JSONObject jsobj = null;
        jsarr = cm.getSchemePublishedData(onboarding_id);
        if (jsarr != null) {
            for (int n = 0; n < jsarr.length(); n++) {
                jsobj = jsarr.getJSONObject(n);

                appl_submission_date = jsobj.get("active_upto").toString();
                verification_date = jsobj.get("last_date_for_verification").toString();
                approval_date = jsobj.get("last_date_for_approval").toString();
            }
        }

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Scheme Management</title>
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
                                    <h4 class="text-white">Scheme Management:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <input type="hidden" name="ddlSchemes" id="ddlSchemes" value=<%= schemeId%> />
                                    <input type="hidden" name="dept_id" id="dept_id" value=<%= deptId%> />
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="txtName">Application Submission Last Date</label>
                                            <div class="input-group mb-3">
                                                <input type="Date" required="" id="txtApplDate" name="txtApplDate" class="form-control form-control-sm" value="<%=appl_submission_date%>" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="txtName">Last Date For Verification</label>
                                            <div class="input-group mb-3">
                                                <input type="Date" required="" id="txtVeriDate" name="txtVeriDate" class="form-control form-control-sm" value="<%=verification_date%>" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="txtName">Last Date For Approval</label>
                                            <div class="input-group mb-3">
                                                <input type="Date" required="" id="txtApprDate" name="txtApprDate" class="form-control form-control-sm" value="<%=approval_date%>" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-1 align-self-end">
                                        <div class="input-group mb-3 ">
                                            <button type="button" id="btnChange" name="btnChange" class="btn btn-primary">
                                                Change
                                            </button>
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
                                                        <th>Address</th>
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
            $('#btnChange').on('click', function (e) {
                var scheme_id=$("#ddlSchemes").val();
                var dept_id=$("#dept_id").val();
                var i = 0;
                var txtApplDate = $('#txtApplDate').val();
                var txtVeriDate = $('#txtVeriDate').val();
                var txtApprDate = $('#txtApprDate').val();
                var today = new Date();
                var dd = String(today.getDate()).padStart(2, '0');
                var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                var yyyy = today.getFullYear();
                
                today = yyyy + '-' + mm + '-' + dd;


                if (txtApplDate === "" || txtVeriDate === "" || txtApprDate === "") {
                    alert("All dates are mandetory");
                    return false;
                }
                if (txtApplDate < today) {
                    alert("Application Submission date cannot be less than current date. ");
                    return false;
                }
                if (txtVeriDate < txtApplDate || txtVeriDate > txtApprDate) {
                    alert("Verification date cannot be less than Application submission date and Verification date cannot be greater than Approval date. ");
                    return false;
                }
                if (txtApprDate < txtApplDate || txtApprDate < txtVeriDate) {
                    alert("Approval date cannot be less than Application submission date and Verification date. ");
                    return false;
                }
                $.ajax({
                    url: "${pageContext.request.contextPath}/saveSchemeManagementData?changeSchemeDate=1&scheme_id="+scheme_id+"&dept_id="+dept_id+"&txtApplDate=" + txtApplDate + "&txtVeriDate=" + txtVeriDate + "&txtApprDate=" + txtApprDate,
                    type: "POST",
                    success: function (data) {
                        if (data === "1") {
                            alert("Changed Successfully");
                        } else {
                            alert("Error on Saving.");
                        }
                    }

                });

            });
        });


    </script>
    <%

        } else {
            response.sendRedirect("../login.jsp");
        }
    %>
</html>