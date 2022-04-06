<%-- 
    Document   : adminHome
    Created on : May 10, 2020, 3:12:48 PM
    Author     : NIC-Arindam
--%>

<%@page import="Common.CommonMethod"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
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
    int deptId = 0, roleId = 0, officeId = 0, schemeId = 0;
    String userId = "", userName = "", userDes = "";

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        if (loginObj.getUserScheme() != null) {
            schemeId = Integer.parseInt(loginObj.getUserScheme());
        }
    }

    if (!userId.equals("")) {
        JSONArray jsarr = null, jsarr1 = null;
        JSONObject jsobj = null, jsobj1 = null;

        Common.CommonMethod cm = new CommonMethod();
        //jsarr = cm.getAllowedUrls(usr.getUser_department_id(), usr.getUser_role());

        String regMsg = "";
        String logMsg = "";
        String type = "light";

        if (session.getAttribute("RegMsg") != null) {
            regMsg = session.getAttribute("RegMsg").toString();
            session.removeAttribute("RegMsg");
        }

        if (session.getAttribute("LogMsg") != null) {
            logMsg = session.getAttribute("LogMsg").toString();
            session.removeAttribute("LogMsg");
        }

        if (session.getAttribute("type") != null) {
            type = session.getAttribute("type").toString();
            session.removeAttribute("type");
        }
        String ddlScheme = "";
        ddlScheme = cm.getDeptUserSchemes(deptId, "-Please Select-");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || User Home</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <!-- Custom CSS -->




        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h5 class="text-white">Welcome <%= userName%> , <%= userDes%> </h5>
                                </div>
                                <div class="card-body bg-light">
                                    <% if (schemeId == 0 && roleId != 1) {%>
                                    <form name="frm" method="post">
                                        <div class="form-row">
                                            <div class="form-group col-md-6 required">
                                                <label for="dept" class="control-label" >Scheme :</label>
                                                <select class="custom-select mr-sm-2" required="" id="scheme" name="scheme">
                                                    <%=ddlScheme%>
                                                </select>
                                                <input type="hidden" name="dept_id" id="dept_id" value="<%=deptId%>" />
                                                <button type="button" name="proceed" id="proceed" class="btn btn-primary mt-2" >Proceed</button>
                                            </div>

                                        </div>
                                    </form>

                                    <% } %>
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
<script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>

<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>
<script>
    $("#proceed").click(function () {
        var scheme_data = $("#scheme").val();
        var deptId = $("#dept_id").val();
        if (scheme_data == "") {
            alert("PLease Select Scheme to Process");
        } else {
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?scheme_data=" + scheme_data + "&dept_id=" + deptId,
                type: "POST",
                success: function (data) {
                    if (data == "ok") {
                        $(location).attr('href', '${pageContext.request.contextPath}/Admin/manageDeptUserGroup.jsp');
                    } else {
                        alert("Session Not Set");
                    }

                }
            });
        }
    });
</script>
<%
    } else {
        response.sendRedirect("../login.jsp");
    }

%>