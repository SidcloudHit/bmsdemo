<%-- 
    Document   : sidebar
    Created on : 30 Jul, 2018, 6:44:00 PM
    Author     : acer
--%>
<%@page import="Common.CommonMethod"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="Master.Data.AdminUser"%>
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
        
        if (loginObj.getUserScheme() != null) {
            schemeId = Integer.parseInt(loginObj.getUserScheme());
        }
    }

    // if (!userId.equals("")) {
    JSONArray jsarr = null, jsarr1 = null;
    JSONObject jsobj = null, jsobj1 = null;

    Common.CommonMethod cm1 = new CommonMethod();
    jsarr = cm1.getAllowedHeaders(roleId, schemeId);


%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/main.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/jquery.mCustomScrollbar.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/sidebar-themes.css">
        <title></title>
    </head>
    <body>
        <div class="bg-primary" id="sideNav">  
            <div id="mySidepanel" class="sidepanel d-none">
                <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
                <div class="sidebar-content sidebar-fluid">
                    <% if (userId != null && userId != "") {%>
                    <%
                        if (roleId == 1) { //System Admin
                    %>
                    <div class="sidebar-item sidebar-menu">
                        <ul>
                            <li class="header-menu mb-5">
                                <span class="font-weight-bold text-white ">Welcome, Admin</span>
                            </li>
                            <li class="sidebar-dropdown mb-5">
                                <div class="bg-light">
                                    <a href="#">                            
                                        <span class="menu-text font-weight-bold bg-light text-dark">ADMINISTRATION</span>                            
                                    </a>
                                </div>
                                <div class="sidebar-submenu">
                                    <ul class="list-group list-group-striped">

                                        <li>
                                            <a href="${pageContext.request.contextPath}/department/dept_onboarding_approval.jsp" data-toggle="tooltip" title="Department onboarding">Approve Department On-boarding</a>
                                        </li>
                                        <li>
                                            <a href="${pageContext.request.contextPath}/department/approveDeptOnboardedList.jsp" data-toggle="tooltip" title="Department onboarding">Approved Department History</a>
                                        </li>
                                        <li>
                                            <a href="${pageContext.request.contextPath}/Admin/manageApplicationUrl.jsp" data-toggle="tooltip" title="Manage URL List">Manage URL List</a>
                                        </li>
                                        <li>
                                            <a href="${pageContext.request.contextPath}/Admin/manageUserRoleUrl.jsp" data-toggle="tooltip" title="Map URL Permissions">Map URL Permissions</a>
                                        </li>
                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>

                    <%
                    } else if (roleId == 2 || roleId == 4 || roleId == 5 || roleId == 6) { // Department User -  Nodal, Approver, Checker, Maker    

                    %>
                    <!-- sidebar-menu  -->
                    <div class="sidebar-item sidebar-menu">
                        <ul>
                            <li class="header-menu mb-5">
                                <span class="font-weight-bold text-white ">Welcome <%= userName%> , <%= userDes%></span>
                            </li>

                            <%
                                if (jsarr != null) {
                                    for (int n = 0; n < jsarr.length(); n++) {
                                        jsobj = jsarr.getJSONObject(n);
                            %>

                            <li class="sidebar-dropdown mb-5">
                                <div class="bg-light">
                                    <a href="#">                            
                                        <span class="menu-text font-weight-bold bg-light text-dark"><%= jsobj.getString("url_name")%></span>                            
                                    </a>
                                </div>

                                <div class="sidebar-submenu">
                                    <ul class="list-group list-group-striped">
                                        <%
                                            jsarr1 = cm1.getAllowedUrls(roleId, schemeId, jsobj.getInt("url_id"));
                                            if (jsarr1 != null) {
                                                for (int k = 0; k < jsarr1.length(); k++) {
                                                    jsobj1 = jsarr1.getJSONObject(k);
                                        %>

                                        <li>
                                            <a href="${pageContext.request.contextPath}/<%= jsobj1.getString("url")%>" data-toggle="tooltip" title="<%= jsobj1.getString("url_desc")%>"><%= jsobj1.getString("url_name")%></a>
                                        </li>

                                        <% }
                                            } %>
                                    </ul>
                                </div>
                            </li>
                            <% }
                                } %>
                        </ul>
                    </div>
                    <!-- sidebar-menu  -->
                    <%
                        }
                        if (roleId == 2) { // Nodal Officer
                    %>
                    <div class="sidebar-item sidebar-menu">
                        <ul>
                            <li class="sidebar-dropdown mb-5">
                                <div class="bg-light">
                                    <a href="#">                            
                                        <span class="menu-text font-weight-bold bg-light text-dark">SCHEME MANAGEMENT</span>                            
                                    </a>
                                </div>
                                <div class="sidebar-submenu">
                                    <ul class="list-group list-group-striped">

                                        <li>
                                            <a href="${pageContext.request.contextPath}/Schemes/scheme_management.jsp" data-toggle="tooltip" title="Scheme onboarding">Scheme On-boarding</a>
                                        </li>

                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>

                    <%
                        }
                    %>

                    <%  } else {
                    %> 
                    <div class="sidebar-item sidebar-menu">
                        <ul>
                            <li class="header-menu mb-5">
                                <span class="font-weight-bold text-white ">Welcome , Guest</span>
                            </li>
                            <li class="sidebar-dropdown mb-5">


                                <div class="sidebar-submenu">
                                    <ul class="list-group list-group-striped">

                                        <li>
                                            <a href="${pageContext.request.contextPath}/department/dept_onboarding.jsp" data-toggle="tooltip" title="Department onboarding">Department On-boarding</a>
                                        </li>

                                    </ul>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <% } %>
                </div>
            </div>


        </div>
    </body>    
<!--     <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>-->
    <script src="${pageContext.request.contextPath}/JS/main.js"></script>
    <script src="${pageContext.request.contextPath}/JS/jquery.mCustomScrollbar.min.js"></script>
    <script>
                    /* Set the width of the sidebar to 250px (show it) */
                    function openNav() {

                        document.getElementById("mySidepanel").style.width = "350px";
                        document.getElementById("mySidepanel").style.height = "100%";
                        var cls = document.getElementById("mySidepanel").className;
                        document.getElementById("mySidepanel").className = cls.replace("d-none", "d-block");
                    }

                    /* Set the width of the sidebar to 0 (hide it) */
                    function closeNav() {
                        document.getElementById("mySidepanel").style.width = "0";
                        document.getElementById("mySidepanel").style.height = "0";
                        var cls = document.getElementById("mySidepanel").className;
                        document.getElementById("mySidepanel").className = cls.replace("d-block", "d-none");
                    }
    </script>
</html>
<%
    //} else if (prof1.getUser_role() == 1) {
    //    response.sendRedirect("../Admin/adminHome.jsp");
    //}
//    } else {
//        response.sendRedirect("../index.jsp");
//    }

%>
