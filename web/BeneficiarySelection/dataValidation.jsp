<%-- 
    Document   : dataValidation
    Created on : Jan 5, 2021, 1:41:43 PM
    Author     : NIC-Arindam
--%>

<%@page import="java.util.Base64"%>
<%@page import="Security.SecurityClass"%>
<%@page import="Master.Data.AdminUser"%>
<%@page import="java.security.SecureRandom"%>
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
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/datatables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/css/responsive.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/css/fixedHeader.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">
        <link rel="stylesheet" type="text/css" href="../DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">



        <style>
            .form-group.required .control-label:after {
                content:"*";
                color:red;
            }
        </style>
    </head>
    <body>

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
                schemeId = Integer.parseInt(loginObj.getUserScheme());
            }

            if (!userId.equals("")) {

                String regMsg = "";
                String type = "light";
                if (session.getAttribute("RegMsg") != null) {
                    regMsg = session.getAttribute("RegMsg").toString();
                    session.removeAttribute("RegMsg");
                }
                //AdminUser usr = new AdminUser(userId);
                //dept_id = usr.getUser_department_id();
                Common.CommonMethod cm = new CommonMethod();
                String fy = "";
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                String ddlScheme = "";
                //ddlScheme = cm.getSchemesData("-Please Select-", deptId);//finYear = cm.getUserSchemesfy(userId,user_scheme, "-Please Select-");
                ddlScheme = cm.getSchemes("-Please Select-", schemeId);
                String finYear = "<option value=\"\">---Please Select---</option>";
                if (month >= 3) {
                    if (fy.equals((year - 1) + "-" + year)) {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                    }
                    if (fy.equals((year - 2) + "-" + (year - 1))) {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\" selected =\"selected\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    }
                    if (fy.equals((year - 3) + "-" + (year - 2))) {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\" selected =\"selected\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    }

                } else {
                    if (fy.equals(year + "-" + (year + 1))) {
                        finYear += "<option value=\"" + year + "-" + (year + 1) + "\" selected =\"selected\">" + year + "-" + (year + 1) + "</option>";
                    } else {
                        finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
                    }
                    if (fy.equals((year - 1) + "-" + year)) {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                    }
                    if (fy.equals((year - 2) + "-" + (year - 1))) {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\" selected =\"selected\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    }
                    if (fy.equals((year - 3) + "-" + (year - 2))) {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\" selected =\"selected\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    }
                }
        %>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">  

                        <div class="row">
                            <div class="col">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <div class="row"> <span class="col-md-6"><h4 class="text-white">View Data Validation</h4></span></div>
                                    </div>
                                    <div class="card-body font-weight-bold2">
                                        <form id="frmFileUpload" action="dataValidation.jsp"  method="post">
                                            <div class="form-group row mx-auto required">
                                                <label for="scheme"  class="col-sm-3 col-form-label control-label font-weight-bold">Select Scheme :</label>
                                                <div class="col-sm-7">
                                                    <select class="js-example-basic-single custom-select mr-sm-2" id="ddlSchemes" name="scheme" onchange="getSelectedScheme(this);" required="">
                                                    <%= ddlScheme%>
                                                </select>
                                            </div>
                                        </div>
                                        <!--  <div class="form-group row mx-auto required">
                                              <label for="FY" class="col-sm-3 col-form-label control-label" >Select FY :</label>
                                              <div class="col-sm-7">
                                                  <select class="custom-select mr-sm-2" id="fy" name="fy" required="">
                                        <%= finYear%>
                                    </select>
                                </div>
                            </div>
                                        -->
                                        <!--  <div class="form-group row mx-auto required">
                                              <label for="Installment" class="col-sm-3 col-form-label control-label" >Select Installment :</label>
                                              <div class="col-sm-7">
                                                  <input type="hidden" name="installment_name"  id="installment_name" value="" />
                                                  <select class="custom-select mr-sm-2" id="ddlInstallment" name="installment" required="" onchange="selectInstallmentName(this);">
          
                                                  </select>
                                              </div>
                                          </div>
                                        -->
                                        <div class="form-group row mx-auto required">
                                            <label for="validate_type" class="col-sm-3 col-form-label control-label font-weight-bold" >Select Data Validation Type :</label>
                                            <div class="col-sm-7">
                                                <select class="js-example-basic-single2 custom-select mr-sm-2" id="validate_type" name="validate_type" required="" onchange="showMatchingBox(this.value)">
                                                    <option value="">---Please Select---</option>
                                                    <%-- <option value="1" <% if (validate_rule.equals("1")) { %> selected="" <% } %> >Duplicate in RC</option>   --%>
                                                    <option value="2"  >RC No. Not Found</option>
                                                    <option value="6" >Pending</option>
                                                    <option value="7" >Disapproved by Checker</option>
                                                    <option value="8" >Probable Duplicate</option>
                                                    <%--   <option value="3" <% if (validate_rule.equals("3")) { %> selected="" <% } %>>Beneficiary Name Match</option>
                                                       <option value="4" <% if (validate_rule.equals("4")) { %> selected="" <% } %>>Beneficiary Name Mismatch</option>--%>

                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group row mx-auto" >
                                            <label for="matching" class="col-sm-3 col-form-label control-label font-weight-bold" id="matching_label"  style="display:none;">Matching Strength :</label>
                                            <div class="col-sm-7">
                                                <select class="custom-select mr-sm-2"  name="match_strength" id="match_strength"  style="display:none;">
                                                    <option value="">---Please Select---</option>
                                                    <option value="1">90%</option>
                                                    <option value="2" >80%</option>
                                                    <option value="3" >70%</option>
                                                    <option value="4" >60%</option>
                                                    <option value="5" >50%</option>
                                                    <option value="999" >Less Than 50%</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group row mx-auto">
                                            <div class="col-sm-3 col-form-label" ></div>
                                            <div class="col-sm-7">
                                                <button type="button" id="btnSearch" class="btn btn-primary" >
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i>Search</button>
                                                <!--  &nbsp; <button type="submit" class="btn btn-primary"  onclick="form.action = '../validateDataReportexcel', form.setAttribute('target', '_blank');">Download (In Excel)</button>-->
                                            </div>
                                        </div>
                                    </form>
                                </div>                        
                            </div>
                        </div>                 
                    </div>

                    <%
                        String qrystr = "", err = "";
                        if (request.getParameter("reqs") != null && request.getParameter("err") != null) {
                            qrystr = SecurityClass.stripXSS(request.getParameter("reqs"));
                            byte[] decodedURL = Base64.getUrlDecoder().decode(qrystr.getBytes("utf-8"));
                            qrystr = new String(decodedURL);
                            err = SecurityClass.stripXSS(request.getParameter("err"));
                            byte[] decodedURL2 = Base64.getUrlDecoder().decode(err.getBytes("utf-8"));
                            err = new String(decodedURL2);

                            if (!err.equals("")) {
                    %>
                    <div class="alert alert-danger alert-dismissible ml-2 mr-2 mt-1 mb-1">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        <strong>Alert!</strong> <%= qrystr%>
                    </div>                      

                    <%
                    } else {

                    %>
                    <div class="alert alert-success alert-dismissible ml-2 mr-2 mt-1 mb-1">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        <strong>Info!</strong> <%= qrystr%>
                    </div>
                    <% }
                        }
                    %>
                    <%
                        Connection con = null;
                        PreparedStatement ps = null;
                        PreparedStatement ps1 = null;
                        ResultSet rs = null;


                    %> 
                    <div class="d-none " id="benListDiv">
                        <div class="table-responsive-sm">
                            <table class="table table-striped table-bordered" id="contenttable" style="width: 100%;">
                                <thead class="thead-dark">
                                    <tr>
                                        <th>SL. No.</th>
                                        <th>Name</th>
                                        <th>District</th>
                                        <th>Subdivision</th>
                                        <th>Block</th>
                                        <th>RC Validation Details </th>                                  
                                        <th>Matching Strength</th>  
                                        <th>Action</th>
                                    </tr>
                                </thead>                        
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>

            <div id="loader" style="display:none;width:105px;height:150px;border:1px solid black;position:fixed;text-align: center;  top: 40%; right: 0; left: 43%; z-index: 9999999; filter: alpha(opacity=70); opacity: 0.9; background-color: #999 ">
                <img src='../Images/progress.gif' width="100" height="100" /><br>Loading Please Wait..</div>

        </body>
    <%            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        int myInt = sr.nextInt(1000000);
        String reqnumber = String.format("%06d", myInt);

    %>   

    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>
    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>    

    <!-- common JavaScript functions-->
    <script src ="${pageContext.request.contextPath}/JS/common.js"></script>
    <script src="${pageContext.request.contextPath}/JS/validation.js"></script>
    <script src="${pageContext.request.contextPath}/JS/ajaxFunctions.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/datatables.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/jquery.dataTables.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>

    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.html5.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.print.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.buttons.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/jszip.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/pdfmake.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/vfs_fonts.js"></script>


    <script>
                                                    $(document).ready(function () {
                                                        // selectInstallment($("#ddlSchemes").val());



                                                        $('.js-example-basic-single').select2();
                                                        $(".js-example-basic-single").on("select2:open", function () {
                                                            $(".select2-search__field").attr("placeholder", "Search...");
                                                        });
                                                        $(".js-example-basic-single").on("select2:close", function () {
                                                            $(".select2-search__field").attr("placeholder", null);
                                                        });

        <%            String schemenameSelected = "", validationtypeSelected = "", strengthSelected = "";
            if (request.getParameter("reqs") != null) {
                if (session.getAttribute("schemenameSelected") != null) {
                    schemenameSelected = session.getAttribute("schemenameSelected").toString();
                }
                if (session.getAttribute("validationtypeSelected") != null) {
                    validationtypeSelected = session.getAttribute("validationtypeSelected").toString();
                }
                if (session.getAttribute("strengthSelected") != null) {
                    strengthSelected = session.getAttribute("strengthSelected").toString();
                }

        %>
                                                        var theText1 = "<%= schemenameSelected%>";
                                                        var theText2 = "<%= validationtypeSelected%>";
                                                        var theText3 = "<%= strengthSelected%>";
                                                        let $element = $('#ddlSchemes')
                                                        let val = $element.find("option:contains('" + theText1 + "')").val()
                                                        $element.val(val).trigger('change.select2');
                                                        $('#validate_type').val(theText2).trigger('change');
                                                        $('#match_strength').val(theText3);
                                                        // $("#btnSearch").trigger('click'); 




        <%  }
        %>
                                                        $('#btnSearch').on('click', function (e) {
                                                            var i = 0;
                                                            var schemeid = $('#ddlSchemes option:selected').val();
                                                            var schemename = $("#ddlSchemes option:selected").text();
                                                            var validate_type = $('#validate_type option:selected').val();
                                                            var strength = $('#match_strength option:selected').val();
                                                            if (schemeid === "") {
                                                                alert("Kindly Select Scheme for searching beneficiaries");
                                                                return false;
                                                            }
                                                            if (validate_type === "") {
                                                                alert("Kindly Select Validation type for searching beneficiaries");
                                                                return false;
                                                            }

                                                            $('#benListDiv').removeClass("d-none").addClass("d-block");
                                                            $('#contenttable').DataTable().clear().destroy();
                                                            var table = $('#contenttable').DataTable({
                                                                dom: 'Bfrtip',
                                                                "scrollX": true,
                                                                "ajax": {
                                                                    "url": "${pageContext.request.contextPath}/AjaxMethod?dbtvalidate=1&schemeid=" + schemeid + "&validate_type=" + validate_type + "&match_strength=" + strength,
                                                                    "type": "POST",
                                                                    "dataSrc": function (data) { //alert(data['aaData']);
                                                                        // console.log(data['aaData']);
                                                                        if (data['aaData'] == "[]") {
                                                                            return [];
                                                                        } else {
                                                                            return data['aaData'];
                                                                        }
                                                                    },
                                                                    beforeSend: function () {
                                                                        $('#loader').show();
                                                                        $(".card").css("opacity", 0.2);
                                                                        $(".card").find("input, button, submit, textarea, select").attr("disabled", "disabled");
                                                                        $("#loader").css({"display": "block"});
                                                                    },
                                                                    complete: function () {
                                                                        if (strength === "") {
                                                                            table.columns([6]).visible(false);
                                                                        }
                                                                        $('#loader').hide();
                                                                        $(".card").css("opacity", 1.0);
                                                                        $(".card").find("input, button, submit, textarea, select").removeAttr("disabled", "disabled");
                                                                    }
                                                                },
                                                                "columnDefs": [
                                                                    {
                                                                        "render": function (data, type, full, meta) {
                                                                            return  meta.row + 1;
                                                                        },
                                                                        targets: 0
                                                                    },
                                                                    {
                                                                        render: function (data, type, full, meta) {
                                                                            if (full['district_name'] === "" || full['district_name'] === null)
                                                                                return "";
                                                                            else
                                                                                return full['district_name'];
                                                                        },
                                                                        targets: 2
                                                                    },
                                                                    {
                                                                        render: function (data, type, full, meta) {
                                                                            if (full['subdivision_name'] === "" || full['subdivision_name'] === null)
                                                                                return "";
                                                                            else
                                                                                return full['subdivision_name'];
                                                                        },
                                                                        targets: 3
                                                                    },
                                                                    {
                                                                        render: function (data, type, full, meta) {
                                                                            if (full['block_name'] === "" || full['block_name'] === null)
                                                                                return "";
                                                                            else
                                                                                return full['block_name'];
                                                                        },
                                                                        targets: 4
                                                                    },
                                                                    {
                                                                        render: function (data, type, full, meta) {
                                                                            return "<div class='text-wrap'><strong>Name: </strong>" + full['postrcvalidation_fullname'] + " <br>\n\
                                                                            <strong>RC Member ID: </strong>" + full['postrcvalidation_rc_member_id'] + "</div>";
                                                                        },
                                                                        targets: 5
                                                                    },
                                                                    {
                                                                        render: function (data, type, full, meta) {
                                                                            var strnlen = full['benef_name_matchresult_dist_leven'];

                                                                            var match_percentage = "";
                                                                            if (strnlen === 1) {
                                                                                match_percentage = "90%";
                                                                            } else if (strnlen === 2) {
                                                                                match_percentage = "80%";
                                                                            } else if (strnlen === 3) {
                                                                                match_percentage = "70%";
                                                                            } else if (strnlen === 4) {
                                                                                match_percentage = "60%";
                                                                            } else if (strnlen === 5) {
                                                                                match_percentage = "50%";
                                                                            } else {
                                                                                match_percentage = "Less Than 50%";
                                                                            }
                                                                            return  match_percentage;

                                                                        },
                                                                        targets: 6
                                                                    },
                                                                    {
                                                                        render: function (data, type, full, meta) {

                                                                            return ' <form method="post" action="../RCDatavalidation"> \n\
                                                <input type="hidden" name="dept_code" value=""> <input type="hidden" name="userid" value="">  \n\
                                                <input type="hidden" name="beneficiary_category" value=""> <input type="hidden" name="reqnumber" value="<%= reqnumber%>"> \n\
                                               <input type="hidden" name="fullname"  value="' + full['full_name'] + '">\n\
                                                <input type="hidden" name="uniqueid" value="' + full['tempid'] + '">    \n\
                                                <input type="hidden" name="rc" value="' + full['postrcvalidation_rc_no'] + '">\n\
                                                 <input type="hidden" name="dob" value=""><input type="hidden" name="gender" value="' + full['gender'] + '">    \n\
                                                <input type="hidden" name="caste"  value="' + full['caste'] + '"><input type="hidden" name="address_lgd_gp_vc_ward_code" value=  "' + full['postrcvalidation_gplgdcode'] + '"> \n\
                                                <input type="hidden" name="areaname" value=""    ><input type="hidden" name="policestation" value=""><input type="hidden" name="postoffice" value=""   >    <input type="hidden" name="pincode" value=""><input type="hidden" name="bankacno" value="' + full['bank_account_number'] + '"><input type="hidden" name=        "ifsc_code" value="' + full['ifsc_code'] + '">    <input type="hidden" name="purpose" value="1"> <input type="hidden" name="remarks" value=""> <input type="hidden" name="email" value="">\n\
                                                <input type="hidden" name="mobile" value="">  \n\
                                                <input type="hidden" name="additional_param1" value="' + full['scheme_code'] + '">\n\
                                                <input type="hidden" name="additional_param2" value="' + schemeid + '">\n\
                                                <input type="hidden" name="schemename" value="' + schemename + '">\n\
                                                <input type="hidden" name="validationtype" value="' + validate_type + '">\n\
                                                <input type="hidden" name="strength" value="' + strength + '">\n\
                                                <input type="submit" name="submit" value="RC Validate" class="btn btn-danger"  onclick="return confirm(\'You will redirect to  RC Verify page.Are you sure you want to continue?\')"/>\n\
                                                </form>\n\
                                               <form method="post" action="../RCDatavalidation"> \n\
                                               <input type="hidden" name="dept_code" value=""> <input type="hidden" name="userid" value="">  \n\
                                               <input type="hidden" name="beneficiary_category" value=""> <input type="hidden" name="reqnumber" value="<%= reqnumber%>"> \n\
                                               <input type="hidden" name="fullname"  value="' + (full['full_name'] != null ? full['full_name'] : '') + '">\n\
                                                <input type="hidden" name="uniqueid" value="' + full['tempid'] + '">    \n\
                                                <input type="hidden" name="rc" value="' + (full['postrcvalidation_rc_no'] != null ? full['postrcvalidation_rc_no'] : '') + '">\n\
                                                 <input type="hidden" name="dob" value=""><input type="hidden" name="gender" value="' + (full['gender'] ? full['gender'] : '') + '">    \n\
                                                <input type="hidden" name="caste"  value="' + (full['caste'] ? full['caste'] : '') + '">  \n\
                                                <input type="hidden" name="address_lgd_gp_vc_ward_code" value=  "' + (full['postrcvalidation_gplgdcode'] ? full['postrcvalidation_gplgdcode'] : '') + '"> \n\
                                                <input type="hidden" name="areaname" value=""  ><input type="hidden" name="policestation" value=""><input type="hidden" name="postoffice" value=""   >    \n\
                                                <input type="hidden" name="pincode" value="">\n\
                                                <input type="hidden" name="bankacno" value="' + (full['bank_account_number'] ? full['bank_account_number'] : '') + '"><input type="hidden" name="ifsc_code" value="' + (full['ifsc_code'] ? full['ifsc_code'] : '') + '">    \n\
                                                <input type="hidden" name="purpose" value="3"> <input type="hidden" name="remarks" value=""> <input type="hidden" name="email" value="">\n\
                                                <input type="hidden" name="mobile" value="">  \n\
                                                <input type="hidden" name="additional_param1" value="' + full['scheme_code'] + '">\n\
                                                <input type="hidden" name="additional_param2" value="' + schemeid + '">\n\
                                                <input type="hidden" name="schemename" value="' + schemename + '">\n\
                                                <input type="hidden" name="validationtype" value="' + validate_type + '">\n\
                                                <input type="hidden" name="strength" value="' + strength + '">\n\
                                                <input type="submit" name="submit" value="Adance Search" class="btn btn-warning mt-2"  onclick="return confirm(\'You will redirect to  RC Advance Search Page.Are you sure you want to continue?\')"/>\n\
                                                </form>';
                                                                        },
                                                                        targets: 7
                                                                    },
                                                                ],
                                                                "select": {
                                                                    'style': 'multi'
                                                                },
                                                                // "iDisplayLength": 50,
                                                                'order': [[0, 'asc']],
                                                                'responsive': true,
                                                                "language": {
                                                                    "emptyTable": "No records to display."
                                                                },
                                                                "columns": [
                                                                    {"data": "sl"},
                                                                    {"data": "full_name"},
                                                                    {"data": "district_name"},
                                                                    {"data": "subdivision_name"},
                                                                    {"data": null},
                                                                    {"data": null},
                                                                    {"data": null},
                                                                    {"data": null},
                                                                ],
                                                                "buttons": [
                                                                    'excel',
                                                                    'csv',
                                                                    'print',
                                                                    'pageLength'
                                                                ],
                                                                "lengthMenu": [[-1, 10, 25, 50, 100], ["All", 10, 25, 50, 100]],
                                                            });
                                                            $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                        });
                                                    });
                                                    
                                                    function getSelectedScheme(sel) {
                                                        //  $("#fy").val("");
                                                        //  $("#installment_id").val("");
                                                        $("#match_strength").val("");
                                                        $("#validate_type").val("");
                                                    }
                                                    function showMatchingBox(validate_type) {
                                                        if (validate_type == "6") {
                                                            $("#match_strength").css("display", "block");
                                                            $("#matching_label").css("display", "block");
                                                        } else {
                                                            $("#match_strength").css("display", "none");
                                                            $("#matching_label").css("display", "none");
                                                        }
                                                    }
                                                    function selectInstallmentName(sel) {
                                                        document.getElementById("installment_name").value = sel.options[sel.selectedIndex].text;
                                                    }
                                                    function selectInstallment(schemeId) {
                                                        var installment_id = $("#installment_id").val();
                                                        $("#ddlInstallment").empty();
                                                        $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + encodeURIComponent(schemeId),
                                                            type: "POST",
                                                            success: function (data) {
                                                                // console.log(data);
                                                                if (data.length > 1) {
                                                                    var myObj = JSON.parse(data);
                                                                    //  console.log(myObj.prd);
                                                                    //alert(data);
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
                                                                            if (i == installment_id) {
                                                                                $("#ddlInstallment").append("<option value=\"" + i + "~" + selArr[i - 1] + "\" selected =\"selected\">" + selArr[i - 1] + " </option>");
                                                                            } else {
                                                                                $("#ddlInstallment").append("<option value=\"" + i + "~" + selArr[i - 1] + "\" >" + selArr[i - 1] + " </option>");
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        })
                                                    }
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

<%    } else {
        response.sendRedirect("../login.jsp");
    }
%>

