<%-- 
    Document   : viewCitizenDocumentForUnfreeze
    Created on : 4 Jan, 2022, 6:17:05 PM
    Author     : acer
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="Common.CommonMethod"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String message = "";
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
    String userId = "", userName = "", userDes = "", schemeName = "";

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

        String regMsg = "";
        String logMsg = "";
        String type = "light";
        String applId = "",param="";
        String[] data = null;
        CommonMethod cm = new CommonMethod();
        if (request.getParameter("aid") != null && request.getParameter("aid") != "") {
            data = request.getParameter("aid").split("~");
            applId = data[0]+"~"+data[1]+"~"+data[2];
            param = data[3];
        }
//        if (request.getParameter("aid") != null) {
//            applId = request.getParameter("aid");
//        }
        JSONArray jsarr = null, jsarr1 = null, jsarr2 = null, jsarr_remem = null;
        JSONObject jsobj = null, jsobj1 = null, jsobj2 = null;
        
        jsarr = cm.getCitizenDocument(applId);
        if(param.equals("2")){
        jsarr1 = cm.getCitizenApplInfo(applId);
        }
        else{
         jsarr1 = cm.getCitizenApplInfoQuery(applId);   
        }
        
        jsarr2 = cm.getCitizenApplQueryInfo(applId);

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

%>  
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Citizen Documents</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">        
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-12">
                            <%                                if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>

                            <div class="card">
                                <div class="card-header bg-info">
                                    <h5 class="text-white">Citizen Application (Application ID - <%=applId%> )</h5>
                                </div>
                                <div class="card-body bg-light">
                                    <%
                                        if (jsarr2 != null) {
                                            String action = "";

                                    %>
                                    <div class="row">
                                        <div class="input-group mb-3 col-md-12">
                                            <table id="remarksList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                <thead class="thead bg-dark text-white">
                                                    <tr><th colspan="5" class="text-center"> Query Raised against Application ID - <%=applId%> </th></tr>
                                                    <tr>
                                                        <th>Sl No.</th>
                                                        <th>Action</th>
                                                        <th>Remarks</th>
                                                        <th>Query Raised On</th>
                                                        <th>Query Responded On</th>
                                                    </tr>
                                                </thead> 
                                                <tbody>
                                                    <%
                                                        for (int n = 0; n < jsarr2.length(); n++) {
                                                            jsobj2 = jsarr2.getJSONObject(n);
                                                            if (jsobj2.getInt("action_taken") == 13 || jsobj2.getInt("action_taken") == 15) {
                                                                action = "Defective";
                                                            } else if (jsobj2.getInt("action_taken") == 14 || jsobj2.getInt("action_taken") == 16) {
                                                                action = "Rejected";
                                                            }

                                                    %>
                                                    <tr>
                                                        <td><%= (n + 1)%></td>
                                                        <td><%= action%></td>
                                                        <td><%= jsobj2.getString("remarks")%></td>
                                                        <td><%= cm.changeDateFormat(jsobj2.get("entry_datetime").toString(), "-", "/")%></td>
                                                        <td><% if(!jsobj2.getString("response_datetime").equals("")){
                                                            out.print(cm.changeDateFormat(jsobj2.get("response_datetime").toString(), "-", "/")); }
                                                        
                                                                        %> </td>
                                                    </tr>
                                                    <%
                                                        }
                                                    %>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <%
                                        }
                                    %>

                                    <%
                                        if (jsarr1 != null) {
                                            String name_as_per_rc = "", rc_no = "", rc_mem_id = "";
                                            for (int n = 0; n < jsarr1.length(); n++) {
                                                jsobj1 = jsarr1.getJSONObject(n);

                                                String fullname = jsobj1.getString("fullname");
                                                String guardian_name = jsobj1.getString("guardian_name");
                                                String district_name = jsobj1.getString("district");
                                                String subdivision_name = jsobj1.getString("subdiv");
                                                String block_name = jsobj1.getString("block");
                                                String ward_name = jsobj1.getString("gp");
                                                String scheme_name = jsobj1.getString("scheme_name");
                                                String scheme_specific_info = jsobj1.get("scheme_specific_info").toString();
                                                String gender = jsobj1.getString("gender");
                                                String mother_name = jsobj1.getString("mother_name");
                                                String social_category = jsobj1.getString("social_category");
                                                String dob = jsobj1.getString("dob");
                                                String pin_code = jsobj1.getString("pin_code");
                                                String bank_ac_no = jsobj1.getString("bank_ac_no");
                                                String name_as_per_passbook = jsobj1.getString("name_as_per_passbook");
                                                String bank_name = cm.getBankName(jsobj1.getString("bank_name"));
                                                String ifsc_code = jsobj1.getString("ifsc_code");
                                                //String tot_amt_claim = String.valueOf(jsobj1.getDouble("total_amount_claim"));
                                                String email = jsobj1.getString("email");
                                                String mobile = jsobj1.getString("mobile");
                                               // String dbt_eligible_amt = jsobj1.getString("eligible_dbt_amount");
                                                name_as_per_rc = jsobj1.getString("name_as_per_rc");
                                                rc_no = jsobj1.getString("rc_no");
                                                rc_mem_id = jsobj1.getString("rc_member_id");
                                                int office_id = jsobj1.getInt("office_id");
                                                String office_name = cm.getOfficeName(office_id);


                                    %>

                                    <table id="schemeList" class="table table-sm table-striped table-bordered m-auto" style="width:80%">
                                        <thead class="thead bg-dark text-white">
                                            <tr><th colspan="2" class="text-center"> Application Details </th></tr>                                            
                                        </thead> 
                                        <tbody>
                                            <tr> <td>Application ID</td> <td><%=applId%></td></tr>
                                            <tr>  <td>Scheme Name</td> <td><%=scheme_name%></td></tr>
                                            <tr>  <td>Full Name</td> <td><%=fullname%></td></tr>
                                            <tr>  <td>Name as per RC</td> <td><%=name_as_per_rc%></td></tr>
                                            <tr>  <td>RC Member ID</td> <td><%=rc_mem_id%></td></tr>
                                            <tr>  <td>Email ID</td> <td><%=email%></td></tr>
                                            <tr>  <td>Mobile No.</td> <td><%=mobile%></td></tr>
                                            <tr>  <td>Father/Guardian Name</td> <td><%=guardian_name%></td></tr>
                                            <tr>  <td>Mother Name</td> <td><%=mother_name%></td></tr>
                                            <tr>  <td>Gender</td> <td><%=gender%></td></tr>
                                            <tr>  <td>Social Category</td> <td><%=social_category%></td></tr>
                                            <tr>  <td>Date of Birth</td> <td><%=dob%></td></tr>
                                            <tr>  <td>District</td> <td><%=district_name%></td></tr>
                                            <tr>  <td>Subdivision</td> <td><%=subdivision_name%></td></tr>
                                            <tr>  <td>Block</td> <td><%=block_name%></td></tr>
                                            <tr>  <td>GP/WARD/VC</td> <td><%=ward_name%></td></tr>
                                            <tr>  <td>Pincode</td> <td><%=pin_code%></td></tr>
                                            <tr>  <td>Bank Name</td> <td><%=bank_name%></td></tr>
                                            <tr>  <td>Bank Account Number</td> <td><%=bank_ac_no%></td></tr>
                                            <tr>  <td>IFSC Code</td> <td><%=ifsc_code%></td></tr>
                                            <tr>  <td>Name as per Bank</td> <td><%=name_as_per_passbook%></td></tr>
                                            <tr>  <td>Office Name</td> <td><%=office_name%></td></tr>
                                            <%    if (scheme_specific_info.length() > 2) {%>
                                            <tr><td colspan="2"><h4>Scheme Specific Information</h4></td></tr>
                                            <%    JSONObject jsonObj = new JSONObject(scheme_specific_info);
                                                JSONArray details = new JSONArray(jsonObj.get("field_json_array").toString());

                                                for (int j = 0; j < details.length(); j++) {
                                                    JSONObject tmpOBJ = new JSONObject(details.getJSONObject(j).toString());
                                                    String label = tmpOBJ.optString("label");
                                                    String value = tmpOBJ.optString("value");
                                            %>
                                            <tr>  <td><%=label%></td> <td><%=value%></td></tr>

                                            <%      }
                                                }
                                            %>

                                        </tbody>
                                    </table>  
                                    <%
                                            }
                                        }
                                    %>


                                </div>
                            </div>

                            <div class="card">
                                <div class="card-header bg-info">
                                    <h5 class="text-white">Citizen Documents</h5>
                                </div>
                                <div class="card-body bg-light">

                                    <%
                                        if (jsarr != null) {
                                            int n = 0;
                                            for (n = 0; n < jsarr.length(); n++) {
                                                jsobj = jsarr.getJSONObject(n);


                                    %>

                                    <%                                        if (n % 2 == 0) {
                                    %>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <%
                                            } else if (n % 2 == 1) {
                                            %>

                                            <div class="col-md-6">
                                                <%
                                                    }
                                                %>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="card">
                                                            <div class="card-body bg-dark ">
                                                                <span class="text-white font-weight-bold"><% if (jsobj.getInt("document_id") != 999) {
                                                                        out.print(jsobj.getString("document_name"));
                                                                    } else {%> Photograph <% }%></span>
                                                            </div> 
                                                        </div>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="bg-light">
                                                            <% if (jsobj.getInt("document_id") == 999) {%>
                                                            <img src="${pageContext.request.contextPath}/ViewFile?fId=<%= jsobj.getInt("fid")%>"  width="100%"  height="100%" />
                                                            <%  } else if (jsobj.getInt("document_id") == 1) {%>
                                                            <span class="font-weight-bold">Aadhar Card not to be viewed by Verifier/Approver</span>
                                                            <% } else if (jsobj.getInt("document_id") != 1) {%>
                                                            <object data="${pageContext.request.contextPath}/ViewFile?fId=<%= jsobj.getInt("fid")%>" type="application/pdf" width="100%"  height="600"></object>
                                                            <% }%>

                                                <!--<embed src="${pageContext.request.contextPath}/viewFile?fId=<%= jsobj.getInt("fid")%>" type="application/pdf" width="100%" height="600px"  />-->
                                                        </div>
                                                    </div>
                                                </div>

                                                <%                                        if (n % 2 == 1) {
                                                %>
                                            </div>
                                        </div>
                                        <%
                                        } else if (n % 2 == 0) {
                                        %>
                                    </div>

                                    <%
                                        }
                                    %>

                                    <% }
                                        if (n % 2 == 1) {
                                    %>
                                </div>
                                <%
                                        }

                                    }%>


                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <jsp:include page="../Web/footer.jsp"></jsp:include>
        </div>
    </body>

    <!-- jQuery library -->
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>

<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>



</html>
<%    } else {
        response.sendRedirect("../login.jsp");
    }
%>