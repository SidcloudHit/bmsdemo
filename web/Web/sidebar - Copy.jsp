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
    String homePath = "";

    if (session.getAttribute("user_id") != null) {
        String userId = session.getAttribute("user_id").toString();
        AdminUser prof1 = new AdminUser(userId);

        //if (prof1.getUser_role() != 1) {
        JSONArray jsarr = null, jsarr1 = null;
        JSONObject jsobj = null, jsobj1 = null;

        Common.CommonMethod cm1 = new CommonMethod();
        jsarr = cm1.getAllowedHeaders(prof1.getUser_department_id(), prof1.getUser_role());


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
            <div id="mySidepanel" class="sidepanel">
                <a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
                <div class="sidebar-content">
                    <% if (prof1.getUser_id() != null) {%>

                    <!-- sidebar-menu  -->
                    <div class="sidebar-item sidebar-menu">
                        <ul>
                            <li class="header-menu mb-5">
                                <span class="font-weight-bold text-white ">Welcome <%= prof1.getUser_name()%> , <%= prof1.getUser_designation()%></span>
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
                                            jsarr1 = cm1.getAllowedUrls(prof1.getUser_department_id(), prof1.getUser_role(), jsobj.getInt("url_id"));
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


                    <%  }
                    %> 
                </div>
            </div>
            
            
        </div>
    </body>    
    <script src="${pageContext.request.contextPath}/JS/main.js"></script>
    <script src="${pageContext.request.contextPath}/JS/jquery.mCustomScrollbar.min.js"></script>
    <script>
                /* Set the width of the sidebar to 250px (show it) */
                function openNav() {
                    document.getElementById("mySidepanel").style.width = "250px";
                }

                /* Set the width of the sidebar to 0 (hide it) */
                function closeNav() {
                    document.getElementById("mySidepanel").style.width = "0";
                }
    </script>
</html>
<%
        //} else if (prof1.getUser_role() == 1) {
        //    response.sendRedirect("../Admin/adminHome.jsp");
        //}
    } else {
        response.sendRedirect("../login.jsp");
    }

%>