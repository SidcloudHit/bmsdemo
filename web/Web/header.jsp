<%-- 
    Document   : header
    Created on : 30 Jul, 2018, 6:45:48 PM
    Author     : acer

<%@page import="Master.Data.MasUserProfile"%>--%>
<%
    String homePath = "";

    String userId = "";
    int roleId = 0;
    String deptName = "", schemeName = "", roleName = "", officeName = "";
    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();
        if (loginObj.getUserRole() != null && !loginObj.getUserRole().equals("")) {
            roleId = Integer.parseInt(loginObj.getUserRole());
            deptName = loginObj.getUserDeptName();
            schemeName = loginObj.getUserSchemeName();
            roleName = loginObj.getUserRoleName();
            officeName = loginObj.getUserOfficeName();
        }

    }

    if (!userId.equals("")) {
        homePath = "User/userHome.jsp";

        if (roleId == 1 || roleId == 2) {
            homePath = "Admin/adminHome.jsp";
        }
    } else {
        homePath = "index.jsp";
    }

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="icon" href="${pageContext.request.contextPath}/Images/icon.png">
        <title></title>
        <script language="javascript" type="text/javascript">
            window.onload = function () {
                closeNav();
            }
        </script>
    </head>
    <body>        
        <div class="navbar navbar-expand-md bg-dark navbar-dark"> <!--navbar-trb-->
            <!-- Brand -->
            <div class="col-1 col-sm-1 col-md-1 col-lg-1">                
                <img src="${pageContext.request.contextPath}/Images/fca_logo_new.png" class="float-left">
            </div>
            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                <span class="navbar-brand">Beneficiary Management System </span>
                <span class="navbar-brand">Govt. of Tripura</span>
            </div>
        </div>
        <!-- A grey horizontal navbar that becomes vertical on small screens -->
        <nav class="navbar navbar-expand-md bg-dark navbar-dark">

            <!-- Toggler/collapsibe Button -->
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>

            <!-- Navbar links -->
            <div class="collapse navbar-collapse" id="collapsibleNavbar">
                <ul class="navbar-nav ml-2">
                    <li class="nav-item">
                        <a class="nav-link" href="#" onclick="openNav()">
                            <i class="fa fa-arrow-right">&nbsp;</i>
                            Menu
                        </a>                        
                    </li>
                </ul>
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/<%= homePath%>">
                            <i class="fa fa-home">&nbsp;</i>
                            Home
                        </a>

                    </li>
                    <%
                        if (userId.equals("")) {
                    %> 
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/../bmslegacydata/index.jsp" target="_blank">
                            <i class="fa fa-home">&nbsp;</i>
                            Legacy Data
                        </a>

                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/../dashboard/home.jsp" target="_blank">
                            <i class="fa fa-home">&nbsp;</i>
                            Dashboard
                        </a>

                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/../bmscitizenportal/index.jsp" target="_blank">
                            <i class="fa fa-home">&nbsp;</i>
                            Citizen
                        </a>

                    </li>
                    <%
                        }
                    %>
                    <%
                        if (!userId.equals("")) {
                            if (roleId != 0) {
                    %>                    

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/User/changePassword.jsp">
                            <span class="input-group-addon">  <img src="${pageContext.request.contextPath}/Images/pwd.png" width="15">  </span> Change Password</a>
                    </li>
                    <% } %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/../rcverify/rcverify/rcAdvancesearch.jsp" target="_blank">
                            <i class="fa fa-search">&nbsp;</i> Search RC Data</a>
                    </li>
                    <!--<li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/Logout">
                            <i class="fa fa-sign-out">&nbsp;</i>Logout</a>
                    </li>-->
                    <%
                        if (roleId == 1 || roleId == 2 || roleId == 3) {

                    %>
                    <!--<li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/Admin/changePassword.jsp">
                            <span class="input-group-addon">  <img src="${pageContext.request.contextPath}/Images/pwd.png" width="15">  </span> Change Password</a>
                    </li>-->                    
                    <%} %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/Logout">
                            <i class="fa fa-sign-out">&nbsp;</i>Logout</a>
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </nav>
        <%
            if (roleId == 4 || roleId == 5 || roleId == 6) {

        %>

        <nav class="navbar navbar-expand-md bg-dark navbar-dark">
            <div class="collapse navbar-collapse" id="collapsibleNavbar">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">                        
                        <span class="nav-link"> Department : <strong> <%= deptName%> </strong></span>                      
                    </li>
                    <li class="nav-item">                        
                        <span class="nav-link">Scheme : <strong> <%= schemeName%> </strong></span>                       
                    </li>
                    <li class="nav-item">                        
                        <span class="nav-link">Role : <strong> <%= roleName%> </strong></span>                       
                    </li>
                    <li class="nav-item">                        
                        <span class="nav-link">Office : <strong> <%= officeName%> </strong></span>                       
                    </li>
                </ul> 
            </div>
        </nav>
        <%
            }
        %>                  
    </body>

</html>
