<%-- 
    Document   : dataValidation
    Created on : Jan 5, 2021, 1:41:43 PM
    Author     : NIC-Arindam
--%>

<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="Common.CommonMethod"%>
<%@page import="java.util.Calendar"%>
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
        <title>BMS || View Beneficiary Data </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <style>
            .form-group.required .control-label:after {
                content:"*";
                color:red;
            }
        </style>
    </head>
    <body>
        <%
            int deptId = 0, roleId = 0, officeId = 0;
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

            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            String finYear = "";
//System.out.println("Financial month : " + month);
            if (month <= 3) {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
            } else {
                finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
            }
            Common.CommonMethod cm = new CommonMethod();
            String ddlScheme = "";
            ddlScheme = cm.getSchemes("-Please Select-");
        %>
        <div class="container bg-light">
            <jsp:include page="Web/header.jsp"></jsp:include>
                <div class="row mt-2 mb-2">
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <div class="row"> <span class="col-md-6"><h4 class="text-primary">View Data Validation</h4></span><span class="col-md-6 text-right"><a class="btn btn-primary" href="viewLegacyData.jsp">View Beneficiary Data</a></span></div>
                            </div>
                            <div class="card-body bg-page font-weight-bold">
                                <form id="frmFileUpload" action="" method="post">
                                    <div class="form-group row mx-auto required">
                                        <label for="scheme"  class="col-sm-3 col-form-label control-label">Select Scheme :</label>
                                        <div class="col-sm-7">
                                            <select class="custom-select mr-sm-2" id="ddlSchemes" name="scheme" required="">
                                            <%= ddlScheme%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group row mx-auto required">
                                    <label for="FY" class="col-sm-3 col-form-label control-label" >Select FY :</label>
                                    <div class="col-sm-7">
                                        <select class="custom-select mr-sm-2" id="fy" name="fy" required="">
                                            <%= finYear%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group row mx-auto required">
                                    <label for="Installment" class="col-sm-3 col-form-label control-label" >Select Installment :</label>
                                    <div class="col-sm-7">
                                        <select class="custom-select mr-sm-2" id="ddlInstallment" name="installment" required="">

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group row mx-auto">
                                    <div class="col-sm-3 col-form-label" ></div>
                                    <div class="col-sm-7">
                                        <button type="submit" class="btn btn-primary">Submit</button>
                                    </div>
                                </div>
                            </form>
                        </div>                        
                    </div>
                </div>                 
            </div>
            <%
                //Class.forName("org.postgresql.Driver");
                Connection con = null;
                PreparedStatement ps = null;
                PreparedStatement ps1 = null;
                ResultSet rs = null;
                if (request.getParameter("scheme") != null && request.getParameter("fy") != null && request.getParameter("installment") != null) { %>                       
            <div class="table-responsive-sm">
                <table class="table table-striped table-bordered" id="contenttable">
                    <thead>
                        <tr>
                            <th>SL. No.</th>
                            <th>Name</th>
                            <th>Guardian Name</th>
                            <th>Aadhar No.</th>
                            <th>PAN</th>
                            <th>Rc No/RC Member ID.</th>
                            <th>Address</th>
                            <th>IFSC</th>
                            <th>AC No</th>
                            <th>Amount</th>
                            <th>District</th>
                            <th>Sub Division</th>
                            <th>Block</th>
                            <th>GP/Ward Name</th>
                            <th>Validate Full Name</th>
                            <th>Duplicate In RC</th>
                            <th>Missing In RC</th>
                            <th>RC Member ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            try {

                                String fy = "", benificiary_count = "", scheme = "", installment = "";
                                int cnt = 0;
                                int count = 1;
                                if (request.getParameter("fy") != null) {
                                    fy = request.getParameter("fy");
                                }
                                if (request.getParameter("installment") != null) {
                                    installment = request.getParameter("installment");
                                }
                                if (request.getParameter("scheme") != null) {
                                    scheme = request.getParameter("scheme");
                                }

                                int schemeId = Integer.parseInt(scheme.split("~")[0]);
                                String schemeCode = scheme.split("~")[1];

                                String query_set = "SELECT * FROM legacy_data.legacy_data_dump"
                                        + " Where scheme_id='" + schemeId + "' AND scheme_code='" + schemeCode + "' AND fy='" + fy + "' AND installment='" + installment + "' AND postrcvalidation_output IS NOT NULL order by sl_no";
                                //con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BMS?useUnicode=true&characterEncoding=utf8", "postgres", "postgres");       
                                con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                ps = con.prepareStatement(query_set);
                                rs = ps.executeQuery();
                                while (rs.next()) {
                                    cnt = 1;
                                    int sl_no = count;
                                    String name = "", guardians_name = "", dob = "", gender = "", caste = "", aadhar = "", pan = "", rc_mem = "", bank_name = "", ifsc = "", district_name = "", subdivision_name = "", block_name = "", gp_name = "", last_mile_address = "";
                                    String bank_account_no = "";
                                    String amount = "", postrcvalidation_fullname = "", postrcvalidation_duplicate_in_rc = "", postrcvalidation_missing_in_rc = "", postrcvalidation_rc_member_id = "";

                                    if (rs.getString("full_name") != "") {
                                        name = rs.getString("full_name");
                                    }
                                    if (rs.getString("guardians_name") != "") {
                                        guardians_name = rs.getString("guardians_name");
                                    }
                                    if (rs.getString("gender") != "") {
                                        gender = rs.getString("gender");
                                    }
                                    if (rs.getString("caste") != "") {
                                        caste = rs.getString("caste");
                                    }
                                    if (rs.getString("dob") != "") {
                                        dob = rs.getString("dob");
                                    }
                                    if (rs.getString("aadhar") != "") {
                                        aadhar = rs.getString("aadhar");
                                    }
                                    if (rs.getString("pan") != "") {
                                        pan = rs.getString("pan");
                                    }

                                    if (rs.getString("rc_mem") != "") {
                                        rc_mem = rs.getString("rc_mem");
                                    }
                                    if (rs.getString("bank_name") != "") {
                                        bank_name = rs.getString("bank_name");
                                    }
                                    if (rs.getString("ifsc") != "") {
                                        ifsc = rs.getString("ifsc");
                                    }
                                    if (rs.getString("district_name") != "") {
                                        district_name = rs.getString("district_name");
                                    }
                                    if (rs.getString("subdivision_name") != "") {
                                        subdivision_name = rs.getString("subdivision_name");
                                    }
                                    if (rs.getString("block_name") != "") {
                                        block_name = rs.getString("block_name");
                                    }
                                    if (rs.getString("bank_account_no") != "") {
                                        bank_account_no = rs.getString("bank_account_no");
                                    }
                                    if (rs.getString("amount") != "") {
                                        amount = rs.getString("amount");
                                    }
                                    if (rs.getString("last_mile_address") != "") {
                                        last_mile_address = rs.getString("last_mile_address");
                                    }
                                    if (rs.getString("gp_name") != "") {
                                        gp_name = rs.getString("gp_name");
                                    }
                                    if (rs.getString("postrcvalidation_fullname") != "") {
                                        postrcvalidation_fullname = rs.getString("postrcvalidation_fullname");
                                    }
                                    if (rs.getString("postrcvalidation_duplicate_in_rc") != "") {
                                        if (rs.getString("postrcvalidation_duplicate_in_rc").equals("false")) {
                                            postrcvalidation_duplicate_in_rc = "No";
                                        } else {
                                            postrcvalidation_duplicate_in_rc = "Yes";
                                        }
                                    }
                                    if (rs.getString("postrcvalidation_missing_in_rc") != "") {
                                        if (rs.getString("postrcvalidation_missing_in_rc").equals("false")) {
                                            postrcvalidation_missing_in_rc = "No";
                                        } else {
                                            postrcvalidation_missing_in_rc = "Yes";
                                        }
                                    }
                                    if (rs.getString("postrcvalidation_rc_member_id") != "") {
                                        postrcvalidation_rc_member_id = rs.getString("postrcvalidation_rc_member_id");
                                    }


                        %>

                        <tr>
                            <td><%=sl_no%></td>
                            <td><%=name + "<br>" + gender + "<br>" + caste + "<br>" + dob%></td>
                            <td><%=guardians_name%></td>
                            <td><%=aadhar%></td>
                            <td><%=pan%></td>
                            <td><%=rc_mem%></td>
                            <td><%=last_mile_address%></td>
                            <td><%=ifsc%></td>
                            <td><%=bank_account_no%></td>
                            <td><%=amount%></td>
                            <td><%=district_name%></td>
                            <td><%=subdivision_name%></td>
                            <td><%=block_name%></td>
                            <td><%=gp_name%></td>
                            <td><%=postrcvalidation_fullname%></td>
                            <td><%=postrcvalidation_duplicate_in_rc%></td>
                            <td><%=postrcvalidation_missing_in_rc%></td>
                            <td><%=postrcvalidation_rc_member_id%></td>
                        </tr>

                        <%
                                count++;
                            }
                            if (cnt < 1) {%>
                        <tr><td colspan="13">No Data Found</td></tr>

                        <% }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        %>


                    </tbody>
                </table>

                <%   rs.close();

                        ps.close();

                        con.close();
                    }


                %>                           
                <jsp:include page="Web/footer.jsp"></jsp:include>
                </div>
        </body>

        <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
    <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
    <script>
        $(document).ready(function () {
            $('#contenttable').DataTable({
                "scrollX": true
            });
        });
        $("#ddlSchemes").on('change', function (e) {

            var schemeId = $('#ddlSchemes option:selected').val();
            $("#ddlInstallment").empty();
            $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + schemeId,
                type: "POST",
                success: function (data) {
                    console.log(data);
                    var myObj = JSON.parse(data);
                    console.log(myObj.prd);
                    if (parseInt(myObj.prd) > 0) {
                        var num = parseInt(myObj.prd);
                        var yearly = ['April-March'];
                        var halfyearly = ['April-September', 'October-March'];
                        var triannualy = ['April-July', 'August-November', 'December-March'];
                        var quarterly = ['April-June', 'July-September', 'October-December', 'January-March'];
                        var monthly = ['April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March'];
                        var selArr;
                        if (num === 1) {
                            selArr = yearly;
                        } else if (num === 2) {
                            selArr = halfyearly;
                        } else if (num === 3) {
                            selArr = triannualy;
                        } else if (num === 4) {
                            selArr = quarterly;
                        } else if (num === 12) {
                            selArr = monthly;
                        }

                        //var instOpts = "";
                        for (var i = 1; i <= num; i++) {
                            $("#ddlInstallment").append("<option value=\"" + i + "\">" + selArr[i - 1] + " </option>");
                        }
                    }

                }
            });
        });
        function IsNumeric(e) {
            var key = window.e ? e.keyCode : e.which;
            if (e.keyCode === 8 || e.keyCode === 46) {
                return true;
            } else if (key < 48 || key > 57) {
                lblinstallmentError.innerHTML = "Only Numbers are Allowed.";
//alert("Only Numbers are Allowed");
                return false;
            } else {
                lblinstallmentError.innerHTML = "";
                return true;
            }
        }
    </script>
</html>



