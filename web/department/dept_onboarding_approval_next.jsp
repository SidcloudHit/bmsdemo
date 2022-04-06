<%-- 
    Document   : dept_onboarding_approval_next
    Created on : Oct 16, 2021, 1:27:46 PM
    Author     : NIC-Arindam
--%>

<%@page import="org.json.JSONArray"%>
<%@page import="java.util.Base64"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="org.json.JSONObject"%>
<%@page import="Security.SecurityClass"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.security.SecureRandom"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Department Nodal Officer Details </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/daterangepicker.css">
        <script>
            function getSelectedDept(sel) {
                document.getElementById("dept_name").value = sel.options[sel.selectedIndex].text;
                document.form1.submit();
            }
        </script>    
        <style>
            .form-group.required .control-label:after {
                content:"*";
                color:red;
            }
        </style>
        <%
            String uniqueid = "", user_id = "", user_dept = "", username = "", userrole = "", schemneid = "", application_id = "";

            String useremail = "", user_name = "", dept_name = "", user_role = "";
            int dept_id = 0;
            Common.CommonMethod cm = new CommonMethod();
            if (request.getParameter("data") != null && request.getParameter("data") != "") {

                dept_id = Integer.parseInt(request.getParameter("data"));
            }
            String msg = "", empName1 = "", email1 = "", mobile1 = "", empCode1 = "", empDesignation1 = "", hrmsCode1 = "", empName2 = "", email2 = "", mobile2 = "", empCode2 = "", empDesignation2 = "", hrmsCode2 = "",
                    created_by = "", dob1 = "", dob2 = "", rcMemId = "", user_password = "", managed_by = "", login_id = "";
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
            try {
//                    String sql = "SELECT * FROM citizen.registration INNER JOIN citizen.scheme_enrollment ON entry_by=email"
//                            + " where email=? AND application_id=?";
                String sql = "SELECT * FROM department_onboarding.nodal_officer_data where dept_id=?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, dept_id);
                rs = ps.executeQuery();

                while (rs.next()) {
                    // reqid = rs.getString("reqnumber");
                    empName1 = rs.getString("nodal1_name");
                    email1 = rs.getString("nodal1_email");
                    mobile1 = rs.getString("nodal1_mobile");
                    empDesignation1 = rs.getString("nodal1_designation");

                    empName2 = rs.getString("nodal2_name");
                    email2 = rs.getString("nodal2_email");
                    mobile2 = rs.getString("nodal2_mobile");
                    empDesignation2 = rs.getString("nodal2_designation");

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            msg = "Your Login Credentials has been sent to your Email Id and Mobile. Please Check and login.";
            dept_name = cm.getDepartmentName(dept_id);
        %>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
             <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                <div class="msform">
                    <fieldset>
                        <a class="btn btn-success" href="dept_onboarding_approval.jsp">Back</a>
                        <div class="print" id="printableArea">
                            <!--                        <h3>Details</h3>-->
                            <!--                <div class="row " id="schemeListDiv">
                                                <div class="input-group mb-3 col-md-12">-->
                            <table id="schemeList" class="table table-sm table-striped table-bordered small" style="width:100%">

                                <tr>  <td>Department Name</td> <td><%=dept_name%></td></tr>

                            <tr><td colspan="2"><h5>First Nodal Officer Details</h5></td></tr>
                            <tr>  <td>Name</td> <td><%=empName1%></td></tr>
                            <tr>  <td>Designation</td> <td><%=empDesignation1%></td></tr>
                            <tr>  <td>Email ID</td> <td><%=email1%></td></tr>
                            <tr>  <td>Mobile</td> <td><%=mobile1%></td></tr>

                            <tr><td colspan="2"><h5>Second Nodal Officer Details</h5></td></tr>
                            <tr>  <td>Name</td> <td><%=empName2%></td></tr>
                            <tr>  <td>Designation</td> <td><%=empDesignation2%></td></tr>
                            <tr>  <td>Email ID</td> <td><%=email2%></td></tr>
                            <tr>  <td>Mobile</td> <td><%=mobile2%></td></tr>

                        </table>  
                        <p><button class="btn btn-primary" onclick="approveDepartment(<%=dept_id%>)">Approve</button></p>
                    </div>
                </fieldset>
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>

        </body>

        <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/moment.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/daterangepicker.min.js"></script>
    <script>
                            function approveDepartment(dept_id) {
                                $.ajax({
                                    url: "${pageContext.request.contextPath}/AjaxServlet?approve_dept=1&dept_id=" + dept_id,
                                    type: "POST",
                                    success: function (data) {
                                        if (data == '1') {
                                            alert("Approved Successfully.Your Login Credentials has been sent to your Email Id and Mobile. Please Check and login.");
                                            window.location.href = "${pageContext.request.contextPath}/department/dept_onboarding_approval.jsp";

                                        }
                                        else{
                                           alert("Error!"); 
                                        }
                                    }
                                });
                            }
    </script>
</html>
