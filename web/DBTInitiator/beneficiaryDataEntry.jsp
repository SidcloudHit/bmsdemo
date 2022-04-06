<%-- 
    Document   : beneficiaryDataEntryNew
    Created on : 18 Dec, 2019, 6:00:22 PM
    Author     : Diptanu
--%>

<%@page import="Master.Data.AdminUser"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.Base64"%>
<%@page import="Security.SecurityClass"%>
<%@page import="java.security.SecureRandom"%>
<%@page import="java.security.SecureRandom"%>
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
    //if (session.getAttribute("user_id") != null) {
    //    response.sendRedirect("User/userHome.jsp");
    //}
    Common.CommonMethod cm = new CommonMethod();
    int edit = 0;
    String ddlScheme = "";
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
        schemeId = Integer.parseInt(loginObj.getUserScheme());

    }

    if (!userId.equals("")) {

        //ddlScheme = cm.getSchemesData("-Please Select-", deptId);
        ddlScheme = cm.getSchemes("-Please Select-", schemeId);

        String ddlDist = "", ddlLoc = "", ddlAdditionalId = "";
        //ddlDist = cm.getDistrict("-Please Select-");
        //ddlLoc = cm.getLocation("-Please Select-");
        //ddlAdditionalId = cm.getAdditionalIdType("-Please Select-");
        String regMsg = "";
        String type = "light";
        String msg = "", schemeCode = "", schemeName = "", officeName = "", finYear = "";
        int instl = 0, blockId = 0, gpId = 0, dbtType = 0;
        DBTBeneficiary ben = null;
        JSONArray jsarr = null;
        JSONObject jsobj = null;

        if (request.getParameter("scmId") != null) {
            String scheme = request.getParameter("scmId");
            schemeId = Integer.parseInt(scheme.split("~")[0]);
            schemeCode = scheme.split("~")[1];
            edit = 1;
        }

        if (request.getParameter("dbtType") != null) {
            dbtType = Integer.parseInt(request.getParameter("dbtType"));
        }

        if (request.getParameter("scmName") != null) {
            schemeName = request.getParameter("scmName");
        }
        if (request.getParameter("pName") != null) {
            officeName = request.getParameter("pName");
        }
        if (request.getParameter("finYr") != null) {
            finYear = request.getParameter("finYr");
        }

        if (request.getParameter("instl") != null) {
            instl = Integer.parseInt(request.getParameter("instl"));
        }
        if (request.getParameter("blockId") != null) {
            blockId = Integer.parseInt(request.getParameter("blockId"));
        }
        if (request.getParameter("gpId") != null) {
            gpId = Integer.parseInt(request.getParameter("gpId"));
        }
        if (request.getParameter("oId") != null) {
            officeId = Integer.parseInt(request.getParameter("oId"));
        }

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
        <title>BMS, Tripura || Beneficiary Data Entry</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">

    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">

                        <div class="row">
                            <div class="col-sm-12">



                                <div class="card">
                                    <div class="card-header bg-info"> 
                                        <div class="row"> <span class="col-md-6"><h4 class="text-white">Add Beneficiary:</h4></span></div>
                                    </div>                                
                                    <div class="card-body">
                                        <form id="frmBeneficiaryData" action="../BeneficiaryDataRecord" method="post"> 
                                            <div class="row">
                                                <div class="form-group col-sm-6 col-md-6 col-lg-6">
                                                    <label for="txtSchemeName" class="font-weight-bold">Scheme</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-female"></i></span>
                                                        </div>

                                                        <select id="ddlScheme" name="ddlScheme" class="form-control form-control-sm js-example-basic-single" onchange="" required=""  onchange="setSelectedName(this.id);"/>

                                                    <%=ddlScheme%>                                                
                                                    </select>
                                                    <input type="hidden" id="ddlSchemeName" name="ddlSchemeName" value="" />

                                                </div>
                                            </div>                                                                  
                                            <div class="form-group col-sm-6 col-md-6 col-lg-6">
                                                <label for="txtRationCardNo" class="font-weight-bold">Ration Card Number</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                    </div>
                                                    <input type="text" id="rc" name="rc" pattern="[0-9]*" maxlength="12" class="form-control form-control-sm" required="required"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group  col-sm-4 col-md-4 col-lg-4 m-auto2">
                                                <%
                                                    //   Srting uniqueid = "202805"; //??
                                                    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                                                    int myInt = sr.nextInt(1000000);
                                                    String reqnumber = String.format("%06d", myInt);

                                                %>

                                                <input type="hidden" name="dept_code" value="">
                                                <input type="hidden" name="userid" value="">
                                                <input type="hidden" name="reqnumber" value="<%= reqnumber%>">
                                                <input type="hidden" name="fullname" value="">
                                                <input type="hidden" name="uniqueid" value="">
                                                <!--<input type="hidden" name="rc" value=""> -->
                                                <input type="hidden" name="dob" value="">
                                                <input type="hidden" name="gender" value="">
                                                <input type="hidden" name="caste" value="">
                                                <input type="hidden" name="address_lgd_gp_vc_ward_code" value="">
                                                <input type="hidden" name="areaname" value="">
                                                <input type="hidden" name="policestation" value="">
                                                <input type="hidden" name="postoffice" value="">
                                                <input type="hidden" name="pincode" value="">
                                                <input type="hidden" name="bankacno" value="">
                                                <input type="hidden" name="ifsc_code" value="">
                                                <input type="hidden" name="beneficiary_category" value="3">
                                                <input type="hidden" name="beneficiary_sub_category" value="4">
                                                <input type="hidden" name="purpose" value="2">
                                                <input type="hidden" name="remarks" value="">
                                                <input type="hidden" name="email" value="">
                                                <input type="hidden" name="mobile" value="">
                                                <input type="hidden" name="additional_param1" value="">
                                                <input type="hidden" name="additional_param2" value="">
                                                <input type="submit" name="submit" value="Search Beneficiary" class="btn btn-primary btn-block" onclick="return go();"/>


                                            </div>

                                        </div>
                                    </form>             
                                </div> 
                            </div>



                            <%
                                try {
                                    String q = "", txt_ref = "";
                                    if (request.getParameter("reqs") != null && request.getParameter("txt_ref") != null) {
                                        q = SecurityClass.stripXSS(request.getParameter("reqs"));
                                        txt_ref = SecurityClass.stripXSS(request.getParameter("txt_ref"));
                                        byte[] decodedURL = Base64.getUrlDecoder().decode(q.getBytes("utf-8"));
                                        q = new String(decodedURL);
                                        byte[] decodedURL2 = Base64.getUrlDecoder().decode(txt_ref.getBytes("utf-8"));
                                        txt_ref = new String(decodedURL2);

                                        Connection con = null;
                                        PreparedStatement ps = null;
                                        ResultSet rs = null;
                                        con = new DBCon.DBSource().connectToAgriDbtDB().getConnection();
                                        String sql = "SELECT * FROM rc_verification where txtrefno=?";
                                        ps = con.prepareStatement(sql);
                                        ps.setString(1, txt_ref);
                                        rs = ps.executeQuery();
                                        String reqid = "", fullreq = "";

                                        while (rs.next()) {
                                            // reqid = rs.getString("reqnumber");
                                            fullreq = rs.getString("full_respose_req");
                                        }
                                        // JSONArray jsonArr = new JSONArray(fullreq);
                                        JSONObject jsonObj = new JSONObject(fullreq);

                                        if (!"Hash Match".equals(q)) {
                            %>
                            <div class="alert alert-danger alert-dismissible ml-2 mr-2 mt-1 mb-1">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                <strong>Alert!</strong> 
                                <%
                                    if (jsonObj.getString("errormsg") != "") {
                                        out.print(q);
                                    } else {
                                        out.print("Some thing wrong. Try again. ");
                                    }
                                %>
                            </div>
                            <%
                            } else {

                            %>
                            <div class="alert alert-success alert-dismissible ml-2 mr-2 mt-1 mb-1">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                <strong>Success!</strong> Your data uploaded.
                            </div>
                            <div class="card">
                                <div class="card-header bg-info">                                
                                    <h5 class="text-white">Beneficiary Response Details:</h5>                                
                                </div> 
                                <div id="collapseBenBankAcc" >
                                    <div class="card-body bg-form2 table-responsive">
                                        <table id='table' class="table table-hover table-bordered">
                                            <tr>
                                                <th>Reqnumber</th>
                                                <th>Ref No</th>
                                                <th>Rationcard Member Id</th>
                                                <th>Full Name</th>
                                                <th>Guardian Name</th>
                                                <th>Relation With Guardian</th>
                                                <th>DOB</th>
                                                <th>Gender</th>
                                                <th>Caste</th>
                                                <th>District</th>
                                                <th>Sub- Division</th>
                                                <th>MC/Block/ADC Block</th>
                                                <th>Ward/GP/VC</th>                                          
                                                <th>Pin</th>
                                                <th>Email</th>
                                                <th>Mobile</th>
                                                <th>Bank Acc.No.</th>
                                                <th>IFSCode</th>
                                                <th>Status</th>
                                            </tr>
                                            <tr>
                                                <%                                            try {

                                                        sql = "SELECT dist_name,  subdiv_name,  block_name,  block_bac_name, gp_vc_ward_name, gp_vc_ward_code FROM lgd.master_dist_sd_block_bac_gp where gp_vc_ward_code  = ?";
                                                        ps = con.prepareStatement(sql);
                                                        ps.setInt(1, jsonObj.getInt("address_lgd_gp_vc_ward_code"));
                                                        rs = ps.executeQuery();
                                                        String district = "", subdiv = "", block = "", ward_gp = "";
                                                        while (rs.next()) {
                                                            district = rs.getString("dist_name");
                                                            subdiv = rs.getString("subdiv_name");
                                                            block = rs.getString("block_name");
                                                            ward_gp = rs.getString("gp_vc_ward_name");
                                                        }

                                                %>

                                                <td><%= jsonObj.getString("reqnumber")%></td>
                                                <td><%= txt_ref%></td>
                                                <td><%= jsonObj.getString("rationcard_memberid")%></td>
                                                <td><%= jsonObj.getString("fullname")%></td>
                                                <td><%= jsonObj.getString("guardian_name")%></td>
                                                <td><%= jsonObj.getString("relation_with_guardian")%></td>
                                                <td><%= jsonObj.getString("dob")%></td>
                                                <td><%= jsonObj.getString("gender")%></td>
                                                <td><%= jsonObj.getString("caste")%></td>
                                                <td><%= district%></td>
                                                <td><%= subdiv%></td>
                                                <td><%= block%></td>
                                                <td><%= ward_gp%></td>
                                                <td><%= jsonObj.getString("pincode")%></td>
                                                <td><%= jsonObj.getString("email")%></td>
                                                <td><%= jsonObj.getString("mobile")%></td>
                                                <td><%= jsonObj.getString("bank_accno")%></td>
                                                <td><%= jsonObj.getString("ifscode")%></td>
                                                <td><%= jsonObj.getString("status")%></td>
                                                <%                       } catch (Exception ex) {
                                                        out.print("<td colspan='6'>something went wrong. Try again.</td>");
                                                    } %>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <%
                                        }
                                    }
                                } catch (Exception ex) {
                                    out.print(" <div class='alert alert-danger alert-dismissible ml-2 mr-2 mt-1 mb-1'> <button type='button'class='close' data-dismiss='alert'>&times;</button> <strong>Alert!</strong> Some thing wrong. Try again. </div>");
                                }
                            %>


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
<script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>

<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>    

<!-- common JavaScript functions-->
<script src ="${pageContext.request.contextPath}/JS/common.js"></script>
<script src="${pageContext.request.contextPath}/JS/validation.js"></script>
<script src="${pageContext.request.contextPath}/JS/ajaxFunctions.js"></script>
<script>
                                                    $(document).ready(function () {

                                                        $('.js-example-basic-single').select2();
                                                        $(".js-example-basic-single").on("select2:open", function () {
                                                            $(".select2-search__field").attr("placeholder", "Search...");
                                                        });
                                                        $(".js-example-basic-single").on("select2:close", function () {
                                                            $(".select2-search__field").attr("placeholder", null);
                                                        });
                                                    });
                                                    function go() {
                                                        var schemename = $("#ddlScheme option:selected").text();
                                                        var rc = $('#rc').val();
                                                        if (schemename === "") {
                                                            alert("Kindly Select Scheme for searching beneficiaries");
                                                            return false;
                                                        }
                                                        if (rc === "") {
                                                            alert("Kindly Enter a Ration card No");
                                                            return false;
                                                        }
                                                        if (isNaN(rc) || rc.length !== 12) {
                                                            alert("Kindly Enter a Valid Ration card No");
                                                            return false;
                                                        }
                                                        var r = confirm("Page will redirect to RC verify Page");
                                                        if (r == true) {
                                                            return true;
                                                        } else {
                                                            return false;
                                                        }
                                                    }


</script>
<%    } else {
        response.sendRedirect("../login.jsp");
    }
%>