<%-- 
    Document   : userHome
    Created on : 16 Apr, 2020, 12:04:15 AM
    Author     : Arindam Mukherjee
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
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0, rDeptId = 0;
    String userId = "", deptName = "", schemeName = "", roleName = "", officeName = "";
    String regMsg = "";
    String logMsg = "";
    String type = "light";
    boolean isLoginSessionUpdated = false;
    Common.CommonMethod cm = new CommonMethod();

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();
        if (loginObj.getUserRole() != null && !loginObj.getUserRole().equals("")) {
            roleId = Integer.parseInt(loginObj.getUserRole());
            deptName = loginObj.getUserDeptName();
            schemeName = loginObj.getUserSchemeName();
            roleName = loginObj.getUserRoleName();
            officeName = loginObj.getUserOfficeName();
            schemeId = Integer.parseInt(loginObj.getUserScheme());
        }

    }
    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        //if (session.getAttribute("user_dept") != null) {
        deptId = Integer.parseInt(loginObj.getUserDeptId());
        //} 
        if (request.getParameter("deptId") != null) {
            rDeptId = Integer.parseInt(request.getParameter("deptId"));
            if (deptId != rDeptId) {
                session.invalidate();
            } else {
                loginObj.setUserDeptName(cm.getDepartmentName(deptId));
                isLoginSessionUpdated = true;
            }

        }
//        if (request.getParameter("deptName") != null) {
//            deptName = request.getParameter("deptName");
//            loginObj.setUserDeptName(request.getParameter("deptName"));
//        }

        if (request.getParameter("schemeId") != null) {
            schemeId = Integer.parseInt(request.getParameter("schemeId"));
            loginObj.setUserScheme(request.getParameter("schemeId"));
            loginObj.setUserSchemeName(cm.getSchemeName(schemeId));
            isLoginSessionUpdated = true;
        }
//        if (request.getParameter("schemeName") != null) {
//            schemeName = request.getParameter("schemeName");
//            loginObj.setUserSchemeName(request.getParameter("schemeName"));
//        }

        if (request.getParameter("roleId") != null) {
            roleId = Integer.parseInt(request.getParameter("roleId"));
            loginObj.setUserRole(request.getParameter("roleId"));
            isLoginSessionUpdated = true;
        }
        if (request.getParameter("roleName") != null) {
            roleName = request.getParameter("roleName");
            loginObj.setUserRoleName(request.getParameter("roleName"));
            isLoginSessionUpdated = true;
        }

        if (request.getParameter("officeId") != null) {
            officeId = Integer.parseInt(request.getParameter("officeId"));
            loginObj.setUserOfficeId(request.getParameter("officeId"));
            loginObj.setUserOfficeName(cm.getOfficeName(officeId));
            isLoginSessionUpdated = true;
        }
//        if (request.getParameter("officeName") != null) {
//            officeName = request.getParameter("officeName");
//            loginObj.setUserOfficeName(request.getParameter("officeName"));
//        }

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

        if (isLoginSessionUpdated) {
            session.removeAttribute("loginInfo");
            session = request.getSession(true);
            session.setAttribute("loginInfo", loginObj);
        }

    }

    int schemeCount = 0, benCount = 0, dbtAmount = 0, totVerifiedBenCount = 0, totApprovedBenCount = 0, dept_route = 0, online_route = 0, query_raised = 0, query_rejected = 0;
    if (!userId.equals("")) {
        JSONArray jsarr = null, jsarr1 = null;
        JSONObject jsobj = null, jsobj1 = null;
        if (roleId != 4) {
            jsarr1 = cm.getBenCountReport(schemeId, officeId);
        } else {
            jsarr1 = cm.getBenCountReport(schemeId);
        }
        if (jsarr1 != null) {
            for (int n = 0; n < jsarr1.length(); n++) {
                jsobj1 = jsarr1.getJSONObject(n);

                benCount = jsobj1.getInt("applied_benef_count");
                totVerifiedBenCount = jsobj1.getInt("verified_benef_count");
                totApprovedBenCount = jsobj1.getInt("approved_benef_count");
                dept_route = jsobj1.getInt("dept_rt_applied_benef_count");
                online_route = jsobj1.getInt("online_rt_applied_benef_count");
                query_raised = jsobj1.getInt("defectivebychecker_benef_count") + jsobj1.getInt("defectivebyapprover_benef_count");
                query_rejected = jsobj1.getInt("rejectbychecker_benef_count") + jsobj1.getInt("rejectbyapprover_benef_count");
            }
        }
        if (roleId != 1) {

//            jsarr = cm.getAllowedHeaders(deptId, roleId);//cm.getAllowedUrls(usr.getUser_department_id(), usr.getUser_role());

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || User Home</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <!-- Custom CSS -->




        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
        <style>
            .main-section{
                margin:0 auto;
                text-align: center;
                padding: 0px 5px;
            }
            .dashbord{
                background-color:#34495E;
                color:#fff;
                margin-top: 50px;
            }
            .icon-section{
                min-height: 170px;
            }
            .icon-section i{
                font-size: 30px;
                padding:10px;
                border:1px solid #fff;
                border-radius:50%;
                margin-top:-25px;
                margin-bottom: 10px;
                background-color:#34495E;
            }
            .icon-section p{
                margin:0px;
                font-size: 20px;
                padding-bottom: 10px;
            }
            .detail-section{
                background-color: #2F4254;
                padding: 5px 0px;
            }
            .dashbord .detail-section:hover{
                background-color: #5a5a5a;
                cursor: pointer;
            }
            .detail-section a{
                color:#fff;
                text-decoration: none;
            }
            .dashbord-green .icon-section,.dashbord-green .icon-section i{
                background-color: #16A085;
            }
            .dashbord-green .detail-section{
                background-color: #149077;
            }
            .dashbord-orange .icon-section,.dashbord-orange .icon-section i{
                background-color: #F39C12;
            }
            .dashbord-orange .detail-section{
                background-color: #DA8C10;
            }
            .dashbord-blue .icon-section,.dashbord-blue .icon-section i{
                background-color: #2980B9;
            }
            .dashbord-blue .detail-section{
                background-color:#2573A6;
            }
            .dashbord-red .icon-section,.dashbord-red .icon-section i{
                background-color:#E74C3C;
            }
            .dashbord-red .detail-section{
                background-color:#CF4436;
            }
            .dashbord-skyblue .icon-section,.dashbord-skyblue .icon-section i{
                background-color:#8E44AD;
            }
            .dashbord-skyblue .detail-section{
                background-color:#803D9B;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-12">
                            <%                        if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>
                            <div class="card ml-0">
                                <div class="card-header bg-info">
                                    <h5 class="text-white">USER HOME</h5>
                                </div>
                                <div class="card-body bg-light">


                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="main-section">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <div class="dashbord">
                                                            <div class="icon-section">
                                                                <i class="fa fa-users" aria-hidden="true"></i><br>
                                                                <h5>Total Application Submitted</h5>
                                                                <p><%=benCount%></p>
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <small>Department Route </small>
                                                                        <p><%=dept_route%></p>
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <small>Online Route </small>
                                                                        <p><%=online_route%></p>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                            <div class="detail-section">
<!--                                                                <a href="#">View Details </a>-->
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <div class="dashbord dashbord-orange">
                                                            <div class="icon-section">
                                                                <i class="fas fa-user-check" aria-hidden="true"></i><br>
                                                                <h5>Total Application Verified</h5>
                                                                <p><%=totVerifiedBenCount%></p>
                                                            </div>
                                                            <div class="detail-section">
<!--                                                                <a href="#">View Details </a>-->
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <div class="dashbord dashbord-blue">
                                                            <div class="icon-section">
                                                                <i class="fa fa-tasks" aria-hidden="true"></i><br>
                                                                <h5>Total Application Approved</h5>
                                                                <p><%=totApprovedBenCount%></p>
                                                            </div>
                                                            <div class="detail-section">
<!--                                                                <a href="#">View Details </a>-->
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                            <div class="row">
                                                    <div class="col-md-4">
                                                        <div class="dashbord dashbord-skyblue">
                                                            <div class="icon-section">
                                                                <i class="fa fa-question-circle" aria-hidden="true"></i><br>
                                                                <h5>Total Application Defective </h5>
                                                                <p><%=query_raised%></p>
                                                                                                                            </div>
                                                            <div class="detail-section">
<!--                                                                <a href="#">View Details </a>-->
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <div class="dashbord dashbord-red">
                                                            <div class="icon-section">
                                                                <i class="fa fa-ban" aria-hidden="true"></i><br>
                                                                <h5>Total Application Rejected</h5>
                                                                <p><%=query_rejected%></p>
                                                            </div>
                                                            <div class="detail-section">
<!--                                                                <a href="#">View Details </a>-->
                                                            </div>
                                                        </div>
                                                    </div>
                                                   
                                                </div>
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
    <!-- jQuery library -->
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>

<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>

<%
        } else if (roleId == 1) {
            response.sendRedirect("../Admin/adminHome.jsp");
        } else {
            response.sendRedirect("../login.jsp");
        }
    } else {
        response.sendRedirect("../login.jsp");
    }

%>