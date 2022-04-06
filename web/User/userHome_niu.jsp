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

    int schemeCount = 0, benCount = 0, dbtAmount = 0;
    if (!userId.equals("")) {
        if (roleId != 1) {
//            JSONArray jsarr = null, jsarr1 = null;
//            JSONObject jsobj = null, jsobj1 = null;
//
//            jsarr = cm.getAllowedHeaders(deptId, roleId);//cm.getAllowedUrls(usr.getUser_department_id(), usr.getUser_role());


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
<!--                                            <div class="card-deck">
                                                <div class="card">
                                                    <div class="card-body bg-primary">
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <i class="fa fa-file-text fa-5x"></i>
                                                            </div>
                                                            <div class="col-sm-9 text-right">
                                                                <div class="huge"><%=schemeCount%></div>
                                                                <div>Total Schemes</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer">
                                                        <span class="pull-left">View Details</span>
                                                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>

                                                        <div class="clearfix"></div>
                                                    </div>
                                                </div>
                                                <div class="card">
                                                    <div class="card-body bg-primary">
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <i class="fa fa-user-plus fa-5x"></i>
                                                            </div>
                                                            <div class="col-sm-9 text-right">
                                                                <div class="huge"></div>
                                                                <div>New Beneficiary Added Today</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer">
                                                        <span class="pull-left">View Details</span>
                                                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>

                                                        <div class="clearfix"></div>
                                                    </div>
                                                </div>
                                                <div class="card">
                                                    <div class="card-body bg-success">
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <i class="fa fa-user fa-5x"></i>
                                                            </div>
                                                            <div class="col-sm-9 text-right">
                                                                <div class="huge"><%=benCount%></div>
                                                                <div>Total Beneficiary Count</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer">
                                                        <span class="pull-left">View Details</span>
                                                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>

                                                        <div class="clearfix"></div>
                                                    </div>
                                                </div>
                                                <div class="card">
                                                    <div class="card-body bg-warning">
                                                        <div class="row">
                                                            <div class="col-sm-3">
                                                                <i class="fa fa-rupee fa-5x"></i>
                                                            </div>
                                                            <div class="col-sm-9 text-right">
                                                                <div class="huge"><i class="fa fa-rupee"></i><%= dbtAmount%></div>
                                                                <div>Total DBT Amount</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="card-footer">
                                                        <span class="pull-left">View Details</span>
                                                        <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>

                                                        <div class="clearfix"></div>
                                                    </div>
                                                </div>

                                            </div> -->
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