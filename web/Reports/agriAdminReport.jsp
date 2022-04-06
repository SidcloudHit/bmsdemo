<%-- 
    Document   : agriAdminReport
    Created on : Jul 6, 2020, 5:00:11 PM
    Author     : NIC-Arindam
--%>

<%@page import="java.util.Calendar"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="Common.CommonMethod"%>
<%@page import="Master.Data.AdminUser"%>
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
        int urlId = cm.getUrlId(request.getRequestURI().replace("/agriculture/", ""));
        if (true || cm.isUrlAllowed(deptId, roleId, urlId)) {
            JSONArray jsarr = null, jsarr1 = null;
            JSONObject jsobj = null, jsobj1 = null;
            jsarr = cm.getUserJuridictiondetailsAgri(userId, deptId);
            String ddlDist = "";
            String edit = "0";
            String regMsg = "";
            String type = "light";
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
            int distId = 0;
            int saStoreId = 0;
            if (jsarr != null) {
                for (int n = 0; n < jsarr.length(); n++) {
                    jsobj = jsarr.getJSONObject(n);
                    ddlDist = cm.getDistrict(jsobj.getInt("dist_code"), "-Please Select-");
                    distId = jsobj.getInt("dist_code");
                    saStoreId = jsobj.getInt("store_id");
                }
            }

            if (session.getAttribute("RegMsg") != null) {
                regMsg = session.getAttribute("RegMsg").toString();
                session.removeAttribute("RegMsg");
            }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Agriculture || GP Wise List </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/datatables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/css/responsive.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/css/fixedHeader.bootstrap4.min.css">

        <script>
            $(document).ready(function () {
                $(window).on("load", function () {
                    //console.log("window loaded");





                });

            });
        </script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">

                    <%if (!regMsg.equals("")) {
                    %>
                    <div class="alert alert-<%=type%>">
                        <strong><%=regMsg%></strong>
                    </div>
                    <%
                        }
                    %>
                    <div class="row">                                                            
                        <div class="col-md-12 mt-2 mb-3">
                            <div class="card mb-3">
                                <div class="card-header bg-info">
                                    <h4 class="text-white">Basic Information on Gram Panchayat (GP)/ Village Council (VC) at a Glance</h4>
                                </div>
                                <div class="card-body bg-form">
                                    <div class="row">                                        
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                            <label for="ddlDistrict1">District</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlDistrict1" name="ddlDistrict1" class="form-control form-control-sm" onchange="resetBlock(8);
                                                        setSelectedName(this.id);
                                                        dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSaStore');
                                                        $('#ddlSubDiv1 option:not([value = \'-1\'],[value=<%= saStoreId%>])').remove();">
                                                    <%=ddlDist%>                                                    
                                                </select>
                                                <input type="hidden" id="ddlDistrict1Name" name="ddlDistrict1Name" value="" />
                                            </div>
                                        </div>
                                        <div class="form-group col-sm-4 col-md-3 col-lg-2 ">
                                            <label for="ddlSubDiv1">Agri Sub-Division</label>
                                            <div class="input-group mb-3">
                                                <select id="ddlSubDiv1" name="ddlSubDiv1" class="form-control form-control-sm" onchange="setSelectedName(this.id);
                                                        dependentDropDown('ddlSubDiv1', 'ddlWardGp', 'fillSaGp');">
                                                    <option value="0">-Please Select-</option>                                                 
                                                </select>
                                                <input type="hidden" id="ddlSubDiv1Name" name="ddlSubDiv1Name" value="" />
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                            <label for="ddlWardGp">Ward/GP/VC</label>
                                            <div class="input-group mb-3">                                                    
                                                <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" onchange="refreshAgriBasicReportEntryPage();setSelectedName(this.id);" required="required">
                                                    <option value="0">-Please Select-</option>                                                 
                                                </select>
                                                <input type="hidden" id="ddlWardGpName" name="ddlWardGpName" value="" />
                                            </div>
                                        </div>

                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                            <label for="ddlFYear">Select Financial Year :</label>                                                 
                                            <div class="input-group mb-3">                                                    
                                                <select id="ddlFYear" name="ddlFYear" class="form-control form-control-sm" required="required" onchange="refreshAgriBasicReportEntryPage();">
                                                    <option value="0">--Select--</option>
                                                    <%= finYear%>
                                                </select>
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
        </body>
    </html>

    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
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


<%
        } else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>
