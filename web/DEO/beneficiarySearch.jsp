<%-- 
    Document   : beneficiarySearch
    Created on : 29 Mar, 2020, 3:28:24 PM
    Author     : Arindam Mukherjee
--%>

<%@page import="Master.Data.DBTBeneficiary"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
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
                response.sendRedirect("User/userHome.jsp");
        }
        Common.CommonMethod cm = new CommonMethod();
        int edit = 0;
        String regMsg = "";
        String type = "light";
        String rcm = "", msg = "";
        DBTBeneficiary ben = null;
        JSONArray jsarr = null;
        JSONObject jsobj = null;

        

        /*if (request.getParameter("benid") != null) {
        int benId = Integer.parseInt(request.getParameter("benid"));
        ben = new DBTBeneficiary(benId);
        edit = 1;
    }
         */
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
                <title>Agriculture, Tripura || Beneficiary Search</title>
                <!-- Latest compiled and minified CSS -->
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
                <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
                <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
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
                                </div>            
                        </div>
                        <jsp:include page="../Web/footer.jsp"></jsp:include>
                        </div>
                </body>
        </html>
        <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>    

<!-- common JavaScript functions-->
<script src ="${pageContext.request.contextPath}/JS/common.js"></script>
<script src="${pageContext.request.contextPath}/JS/validation.js"></script>
<script src="${pageContext.request.contextPath}/JS/ajaxFunctions.js"></script>
