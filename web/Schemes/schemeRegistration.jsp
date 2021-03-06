<%-- 
    Document   : schemeRegistration
    Created on : Jul 29, 2021, 2:04:08 PM
    Author     : Administrator
--%>

<%@page import="org.json.JSONObject"%>
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
        <title>BMS || Scheme On-boarding </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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
        <script>
            function getSelectedDept(sel) {
                document.getElementById("dept_name").value = sel.options[sel.selectedIndex].text;
                document.form1.submit();
            }
        </script>    
        <style>
            .form-group.required .control-label:after {
                content: "*";
                color: red;
            }
            legend.scheduler-border {
                width:inherit; /* Or auto */
                padding:0 10px; /* To give a bit of padding on the left and right */
                border-bottom:none;
                font-size: 15px;
            }
            fieldset.scheduler-border {
                border: 1px groove #ddd !important;
                padding: 0 1.4em 1.4em 1.4em !important;
                margin: 0 0 1.5em 0 !important;
                -webkit-box-shadow:  0px 0px 0px 0px #000;
                box-shadow:  0px 0px 0px 0px #000;
            }
        </style>
        <%
            String userId = "", user_name = "", dept_name = "", user_role = "";
            String department_name = "", scheme_code = "", scheme_name = "", component_name = "", beneficiary_financial_status = "", description = "", beneficiary_category = "",
                    pfms_data = "", scheme_periodicity = "", scheme_frequency = "", amount_fixed = "", amount = "", payment_mode = "", payment_config_html = "", required_doc_poi = "", required_doc_poa = "", required_doc_scheme = "",
                    commodity_name = "", subsidy_amt = "", effective_from = "", commodity_matrix = "", launch_date = "", launch_fy = "", active_upto = "", verification_last_date = "", approval_last_date = "", scheme_type = "", scheme_data_source = "";
            String prefilledid = "", pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "",
                    ac_holder_name = "", debit_tran_limit = "", Specific_info_html = "";
            int dept_id = 0;
            int tot_textbox = 0, tot_dropdown = 0, tot_datefield = 0, tot_priority = 0;
            String[] scheme_features = null;
            String[] commodity = null;
            String stakeholder_dept_list = null, commodity_list = null, cond_param_list = null;
            String[] stakeholder_dept = null;
            String[] param_list = null;
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
            String userName = "", userDes = "";

            if (session.getAttribute("loginInfo") != null) {

                SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

                userId = loginObj.getUserId();

                deptId = Integer.parseInt(loginObj.getUserDeptId());
                userName = loginObj.getUserName();
                userDes = loginObj.getUserDesignation();
                if (loginObj.getUserRole() != null && loginObj.getUserRole() != "") {
                    roleId = Integer.parseInt(loginObj.getUserRole());
                }
                if (loginObj.getUserOfficeId() != null && loginObj.getUserOfficeId() != "") {
                    officeId = Integer.parseInt(loginObj.getUserOfficeId());
                }

            }
            if (!userId.equals("")) {
                Common.CommonMethod cm = new CommonMethod();
                String benefit_type = "", department_id = "", scheme_id = "", edit = "";
                if (request.getParameter("data") != null && request.getParameter("data") != "") {
                    String[] data = request.getParameter("data").split("~");
                    if (data.length > 2) {
                        scheme_id = request.getParameter("data").split("~")[2];
                        edit = request.getParameter("data").split("~")[3];
                    }
                    benefit_type = request.getParameter("data").split("~")[0];
                    department_id = request.getParameter("data").split("~")[1];
                    session.setAttribute("scheme_onboarding_dept", department_id);
                }
//                int year = Calendar.getInstance().get(Calendar.YEAR);
//                int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int future_fy = 2;
                int prev_fy = 3;
                String finYear = cm.getFinacialYear(prev_fy, future_fy);
//System.out.println("Financial month : " + month);
//                if (month <= 3) {
//                    finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
//                    finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
//                    finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
//                } else {
//                    finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
//                    finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
//                    finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
//                }

                String master_priority_list = "", ddlprefilledlist = "", ddlDept = "", ddlPeriodicity = "", ddlbeneficiaryCategory = "",
                        ddlParamdropdown = "", ddlStoreColumndropdown = "", ddlSchemeType = "", ddlDeptdropdown = "", ddlDeptdropdown_maker = "", ddlDeptdropdown_checker = "", ddlJurisdictiondropdown = "";

                ddlParamdropdown = cm.getParamdropdown("-Please Select-");
                ddlStoreColumndropdown = cm.getStoreColumn("-Please Select-");
                ddlDeptdropdown = cm.getOnboardedDepartment("-Please Select-");
                ddlDept = cm.getDepartmentName(Integer.parseInt(department_id));
                ddlPeriodicity = cm.getPeriodicity("-Please Select-");
                ddlbeneficiaryCategory = cm.getbeneficiaryCategory();
                String ddldocumentpoi = "", ddldocumentpoa = "", ddldocumentscheme = "", ddlcommodity = "";
                ddldocumentpoi = cm.getDocumentpoi();
                ddldocumentpoa = cm.getDocumentpoa();
                ddldocumentscheme = cm.getDocumentscheme();
                ddlcommodity = cm.getCommodityList("-Please Select-");
                ddlSchemeType = cm.getSchemeType("-Please Select-");
                ddlJurisdictiondropdown = cm.getJurisdiction("-Please Select-");
                ddlprefilledlist = cm.getPrefilledList();
                String ddlCalcType = "";
                ddlCalcType = cm.getCalculationType("-Please Select-");
                String regMsg = "";
                String type = "light";

                if (session.getAttribute("RegMsg") != null) {
                    regMsg = session.getAttribute("RegMsg").toString();
                    session.removeAttribute("RegMsg");
                }

                if (session.getAttribute("type") != null) {
                    type = session.getAttribute("type").toString();
                    session.removeAttribute("type");
                }
                if (!edit.equals("")) {

                    try {
                        String basic_info_json_data = cm.getBasicInfoData(scheme_id);
                        JSONObject obj = new JSONObject(basic_info_json_data);
                        department_name = obj.getString("department_name");
                        scheme_code = obj.getString("scheme_code");
                        scheme_name = obj.getString("scheme_name");
                        if (obj.has("component_name")) {
                            component_name = obj.getString("component_name");
                        }

                        beneficiary_financial_status = obj.getString("beneficiary_financial_status");
                        if (obj.getString("description") != null) {
                            description = obj.getString("description");
                        }
                        launch_date = obj.getString("launch_date");
                        launch_fy = obj.getString("launch_fy");
                        active_upto = obj.getString("active_upto");
                        verification_last_date = obj.getString("last_date_for_verification");
                        approval_last_date = obj.getString("last_date_for_approval");
                        scheme_type = obj.getString("scheme_type");
                        ddlSchemeType = cm.getSchemeType(scheme_type, "-Please Select-");
                        beneficiary_category = obj.getString("beneficiary_category");
                        ddlbeneficiaryCategory = cm.getbeneficiarySelectedCategory(beneficiary_category);
                        scheme_features = obj.getString("scheme_features").split(",");
                        required_doc_poa = obj.getString("required_doc_poa");
                        ddldocumentpoa = cm.getSelectedDocumentpoa(required_doc_poa);
                        required_doc_poi = obj.getString("required_doc_poi");
                        ddldocumentpoi = cm.getSelectedDocumentpoi(required_doc_poi);

                        required_doc_scheme = obj.getString("required_doc_scheme");
                        ddldocumentscheme = cm.getSelectedDocumentscheme(required_doc_scheme);
                        scheme_data_source = obj.getString("scheme_data_source");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    finYear = cm.getSelectedFinancialyear(prev_fy, future_fy, launch_fy);
//                    finYear = "<option value=\"\">---Please Select---</option>";
//                    if (month <= 3) {
//                        if (launch_fy.equals((year - 1) + "-" + year)) {
//                            finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
//                        }
//                        if (launch_fy.equals((year - 2) + "-" + (year - 1))) {
//                            finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\" selected =\"selected\">" + (year - 2) + "-" + (year - 1) + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
//                        }
//                        if (launch_fy.equals((year - 3) + "-" + (year - 2))) {
//                            finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\" selected =\"selected\">" + (year - 3) + "-" + (year - 2) + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
//                        }
//
//                    } else {
//                        if (launch_fy.equals(year + "-" + (year + 1))) {
//                            finYear += "<option value=\"" + year + "-" + (year + 1) + "\" selected =\"selected\">" + year + "-" + (year + 1) + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
//                        }
//                        if (launch_fy.equals((year - 1) + "-" + year)) {
//                            finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
//                        }
//                        if (launch_fy.equals((year - 2) + "-" + (year - 1))) {
//                            finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\" selected =\"selected\">" + (year - 2) + "-" + (year - 1) + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
//                        }
//                        if (launch_fy.equals((year - 3) + "-" + (year - 2))) {
//                            finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\" selected =\"selected\">" + (year - 3) + "-" + (year - 2) + "</option>";
//                        } else {
//                            finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
//                        }
//                    }
                    String payment_info = cm.getSchemePaymentInfo(scheme_id);
                    if (payment_info.length() > 5) {
                        scheme_periodicity = payment_info.split("~")[0];
                        scheme_frequency = payment_info.split("~")[1];
                        amount_fixed = payment_info.split("~")[2];
                        payment_mode = payment_info.split("~")[3];
                    }
                    // if (amount_fixed.equals("Y")) {
                    amount = cm.getFixedAmountHtml(scheme_id, amount_fixed);
                    // }
                    payment_config_html = cm.getPaymentConfigHtml(scheme_id);
//                    cond_param_list = cm.getSchemecondParamHtml(scheme_id);
//                    if (cond_param_list != "") {
//                        param_list = cond_param_list.split(",");
//                    }

                    commodity_list = cm.getSchemeCommodityListHtml(scheme_id);
                    if (commodity_list != "") {
                        commodity = commodity_list.split(",");
                    }
                    stakeholder_dept_list = cm.getSchemestakeholderDeptHtml(scheme_id);
                    if (stakeholder_dept_list != "") {
                        stakeholder_dept = stakeholder_dept_list.split(",");
                    }
                    ddlPeriodicity = cm.getPeriodicity(scheme_periodicity, "-Please Select-");

                    if (payment_mode.equals("2")) {
                        pfms_data = cm.getPfmsPaymentData(scheme_id);
                        pfms_scheme_code = pfms_data.split("~")[0];
                        agency_name = pfms_data.split("~")[1];
                        agency_code = pfms_data.split("~")[2];
                        bank_name = pfms_data.split("~")[3];

                        bank_ac_no = pfms_data.split("~")[4];
                        ifsc_code = pfms_data.split("~")[5];
                        ac_holder_name = pfms_data.split("~")[6];
                        debit_tran_limit = pfms_data.split("~")[7];
                    }

                    Specific_info_html = cm.getSpecific_info_textbox_html(scheme_id);

                    tot_textbox = cm.getTotTextboxCount(scheme_id);
                    tot_dropdown = cm.getTotDropdownCount(scheme_id);
                    tot_datefield = cm.getTotDateFieldCount(scheme_id);
                    tot_priority = cm.getTotPriorityCount(scheme_id) + 1;
                    prefilledid = cm.getPrefilledListId(scheme_id);
                    ddlprefilledlist = cm.getSelectedPrefilledList(prefilledid);
                    master_priority_list = cm.getMasterPriorityList(scheme_id);
                    //    String Specific_info_dropdown_html=cm.getSpecific_info_dropdown_html(scheme_id);
                }
        %>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            

                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">


                        <div class="row">
                            <div class="col-sm-12">

                            <%                        if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>   
                            <div class="row">
                                <div class="col-md-12 col-md-offset-3 msform">
                                    <div id="msform">
                                        <h3 class="text-center">Scheme On-boarding and Configuration</h3>
                                        <!-- progressbar -->
                                        <ul id="progressbar">
                                            <%--                                            <li <% if ((step.equals("1")) || (step.equals("2"))) {%>class="success"<% } else { %>class="active" <% } %>>Basic Information</li>
                                                                                        <li<% if (step.equals("1")) {%> class="active" <% } else if (step.equals("2")) {%> class="success" <% }%>>Payment Information</li>--%>
                                            <li class="active">Basic Information</li>
                                            <li>Payment Information</li>
                                            <li>Stake Holder Department</li>
                                            <li>Scheme Specific Information</li>
                                            <li>Preview & Submit</li>
                                        </ul>
                                        <!-- fieldsets -->
                                        <input type="hidden" name="schemeid_final" id="schemeid_final" />
                                        <fieldset id="basic_info">
                                            <form id="msform1" action="${pageContext.request.contextPath}/savemultistepSchemeRegData" method="post"  enctype="multipart/form-data">
                                                <input type="hidden" name="edit_basic_info" id="edit_basic_info" <% if (!edit.equals("")) {%>value="1" <% } else { %>value="0"<% }%> />

                                                <div class="row"><div class="col-md-4"><h2 class="fs-title">Basic Information</h2></div><div class="col-md-8"><h2 class="fs-title">Department:<%=ddlDept%></h2></div></div>
                                                <div class="form-row">
                                                    <input type="hidden" name="dept" id="dept" value="<%=department_id%>">
                                                    <div class="form-group col-md-4 required">
                                                        <label for="scheme" class="control-label">Scheme Name :</label>
                                                        <input type="text" autocomplete="off" required="" maxlength="500" class="form-control" id="scheme_name" name="scheme_name" placeholder="Scheme Name" value="<%= scheme_name%>">
                                                    </div>
                                                    <div class="form-group col-md-4">
                                                        <label for="scheme" class="control-label">Component Name :</label>
                                                        <input type="text" autocomplete="off" maxlength="500" class="form-control" id="component_name" name="component_name" placeholder="Component Name" value="<%= component_name%>">
                                                    </div>
                                                    <div class="form-group col-md-4 required">
                                                        <label for="scheme_code" class="control-label">Scheme Code :</label>
                                                        <input type="text" autocomplete="off" style="text-transform: uppercase;" required="" onkeypress="return isAlphaNumeric(event);" maxlength="5" class="form-control" id="scheme_code" value="<%= scheme_code%>" name="scheme_code" placeholder="Scheme Code">
                                                        <span id="lblError" style="color: red"></span>
                                                    </div>
                                                </div>
                                                <div class="form-row">

                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label">Scheme Type :</label>
                                                        <select class="custom-select mr-sm-2" id="scheme_type" name="scheme_type" required="" >
                                                            <%=ddlSchemeType%>
                                                        </select>  
                                                        <span id="lblError" style="color: red"></span>
                                                    </div>
                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label">Launch Date :</label>
                                                        <input type="date" required=""  class="form-control" id="launch_date" name="launch_date" placeholder="DD/MM/YYYY" value="<%= launch_date%>">
                                                        <span id="lblError" style="color: red"></span>
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label">Launch Financial Year :</label>
                                                        <select class="custom-select mr-sm-2" id="fy" name="fy" required="">
                                                            <%= finYear%>
                                                        </select>  
                                                        <span id="lblError" style="color: red"></span>
                                                    </div>
                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label">Application Submission Last Date :</label>
                                                        <input type="date" required=""  class="form-control" id="active_upto" name="active_upto" placeholder="DD/MM/YYYY" value="<%= active_upto%>">
                                                    </div>

                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label">Last Date For Verification :</label>
                                                        <input type="date" required=""  class="form-control" id="verification_last_date" name="verification_last_date" placeholder="DD/MM/YYYY" value="<%= verification_last_date%>">
                                                    </div>
                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label">Last Date For Approval :</label>
                                                        <input type="date" required=""  class="form-control" id="approval_last_date" name="approval_last_date" placeholder="DD/MM/YYYY" value="<%= approval_last_date%>">
                                                    </div>

                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="Scheme Benefit" class="control-label">Scheme Benefit Type :</label>
                                                        <select class="custom-select mr-sm-2" id="scheme_benefit" required="" name="scheme_benefit">
                                                            <option value="" selected>-Please Select-</option>
                                                            <option <% if (benefit_type.equals("1")) {%>selected=""<%}%> value="1">Cash</option>
                                                            <option <% if (benefit_type.equals("2")) {%>selected=""<%}%> value="2">Kind</option>
                                                            <option <% if (benefit_type.equals("5")) {%>selected=""<%}%> value="5">Cash and Kind</option>
                                                        </select>
                                                    </div>

                                                    <div class="form-group col-md-6 required">
                                                        <label for="periodicity" class="control-label">Beneficiary Category :(Please use 'CTRL' key for multiple selection)</label>
                                                        <select class="custom-select mr-sm-2" multiple data-live-search="true" id="beneficiary_category" required="" 
                                                                name="beneficiary_category" onchange="checkOtherCatSelected(this.value)">
                                                            <%=ddlbeneficiaryCategory%>
                                                        </select>      
                                                        <div class="form-group col-md-12" id="scheme_extra_cat_fields" style="display:none;">
                                                            <label for="periodicity" class="control-label">Add Extra Category :</label>
                                                            <div class="input-group">
                                                                <input autocomplete="off" type="text" class="form-control" name="other_category"  value="" >
                                                                <button class="btn btn-success" type="button"  onclick="scheme_extra_cat_fields();"> <span class="plus">+</span> </button>

                                                                <div class="input-group-btn">
                                                                </div>
                                                            </div>
                                                            <span id="lblinstallmentError" style="color: red"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-4 required">
                                                        <fieldset class="scheduler-border">
                                                            <legend class="scheduler-border"><label for="installment" class="control-label">Beneficiary Financial Status :</label></legend>
                                                            <div style="display:none; color:red; " id="chk_option_error">
                                                                Please select at least one option.
                                                            </div>
                                                            <div class="control-group">
                                                                <div class="controls bootstrap-timepicker">
                                                                    <div class="row form-check" style="border:none;">
                                                                        <div class="col-md-4">
                                                                            <input type="checkbox" class="form-check-input" id="beneficiary_fy_status_apl" name="beneficiary_fy_status_apl" value="1" <% if (beneficiary_financial_status.equals("1") || beneficiary_financial_status.equals("3")) {%> checked=""<%}%>> APL (Including DeTO)
                                                                        </div>
                                                                        <div class="col-md-4">
                                                                            <input type="checkbox" class="form-check-input" id="beneficiary_fy_status_bpl" name="beneficiary_fy_status_bpl" value="2" <% if (beneficiary_financial_status.equals("2") || beneficiary_financial_status.equals("3")) {%> checked=""<%}%>> BPL
                                                                        </div>
                                                                    </div>
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                            </div>
                                                        </fieldset>




                                                    </div>
                                                    <div class="form-group col-md-3 required">
                                                        <fieldset class="scheduler-border">
                                                            <legend class="scheduler-border"><label for="installment" class="control-label">Beneficiary Data Source :</label></legend>
                                                            <div style="display:none; color:red; " id="chk_option_error1">
                                                                Please select at least one option.
                                                            </div>
                                                            <div class="control-group">
                                                                <div class="controls bootstrap-timepicker">
                                                                    <div class="row form-check" style="border:none;">
                                                                        <div class="col-md-4">
                                                                            <input type="checkbox" class="form-check-input" id="data_entry_from_dept" name="data_entry_from_dept" value="1" <% if (scheme_data_source.equals("1") || scheme_data_source.equals("3")) {%> checked=""<%}%>> Department
                                                                        </div>
                                                                        <div class="col-md-4">
                                                                            <input type="checkbox" class="form-check-input" id="data_entry_from_citizen" name="data_entry_from_citizen" value="2" <% if (scheme_data_source.equals("2") || scheme_data_source.equals("3")) {%> checked=""<%}%>> BMS Portal
                                                                        </div>
                                                                    </div>
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                            </div>
                                                        </fieldset>
                                                    </div>
                                                    <div class="form-group col-md-5 required">
                                                        <label for="periodicity" class="control-label">Scheme Description :</label>
                                                        <textarea class="form-control" required="" id="scheme_desc" name="scheme_desc" rows="3"><%=description%></textarea>                                      
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-12" id="scheme_features_fields">
                                                        <label for="periodicity" class="control-label">Scheme Features (Salient Features):</label>

                                                        <% if (scheme_features != null) {
                                                                for (int i = 0; i < scheme_features.length; i++) {
                                                                    if (i == 0) {%>
                                                        <div class="input-group">
                                                            <input autocomplete="off" type="text" class="form-control" name="scheme_features"  value="<%=scheme_features[i]%>" >
                                                            <button class="btn btn-success" type="button"  onclick="scheme_features_fields();"> <span class="plus">+</span> </button>
                                                            <div class="input-group-btn">
                                                            </div>
                                                        </div>
                                                        <% } else {%>
                                                        <div class="form-group removeclass<%=i%>">
                                                            <div class="input-group mt-2">
                                                                <input autocomplete="off" type="text" class="form-control" name="scheme_features"  value="<%=scheme_features[i]%>" >
                                                                <div class="input-group-btn">
                                                                    <button class="btn btn-danger" type="button"  onclick="remove_scheme_features_fields(<%=i%>);"> <span class="minus">-</span> </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <% }
                                                            }
                                                        } else {%>
                                                        <div class="input-group">
                                                            <input autocomplete="off" type="text" class="form-control" name="scheme_features"  value="" >
                                                            <button class="btn btn-success" type="button"  onclick="scheme_features_fields();"> <span class="plus">+</span> </button>
                                                            <div class="input-group-btn">
                                                            </div>
                                                        </div>
                                                        <% }%>

                                                        <span id="lblinstallmentError" style="color: red"></span>
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6">
                                                        <label for="scheme_code" class="control-label">Proof of Identity :(Please use 'CTRL' key for multiple selection)</label>
                                                        <select class="custom-select mr-sm-2" multiple data-live-search="true" name="required_document_poi">
                                                            <%=ddldocumentpoi%>
                                                        </select>                                      
                                                    </div>

                                                    <div class="form-group col-md-6">
                                                        <label for="scheme_code" class="control-label">Proof of Address :(Please use 'CTRL' key for multiple selection)</label>
                                                        <select class="custom-select mr-sm-2" multiple data-live-search="true" name="required_document_poa">
                                                            <%=ddldocumentpoa%>
                                                        </select>                                      
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-4">
                                                        <label for="scheme_code" class="control-label">Scheme Sanction Letter/Memo :</label>
                                                        <input type="file" class="form-control" name="sanction_letter" value="" />
                                                    </div>
                                                </div>
                                                <input type="button" id="next" name="next" class="next action-button" value="Save & Next"/>
                                            </form>
                                        </fieldset>
                                        <%--                                        <fieldset id="payment_info" <% if (step.equals("1")) {%> style="display:block;" <% } else if (step.equals("2")) {%>style="display:none;" <% }%> >--%>
                                        <fieldset id="payment_info" >

                                            <form id="msform2" action="${pageContext.request.contextPath}/saveSchemePaymentInfo" method="post">
                                                <h2 class="fs-title">Payment Information</h2>
                                                <input type="hidden" name="edit_payment_info" id="edit_payment_info" <% if (!edit.equals("")) {%>value="1" <% } else { %>value="0"<% }%> />
                                                <input type="hidden" name="scheme_id" id="scheme_id" <% if (edit.equals("1")) {%> value="<%=scheme_id%>" <% }%>/>
                                                <input type="hidden" name="benefit_type" value="<%=benefit_type%>" />
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="dept" class="control-label" >Scheme Periodicity :</label>
                                                        <select class="custom-select mr-sm-2" required="" name="scheme_periodicity">
                                                            <%=ddlPeriodicity%>
                                                        </select>
                                                    </div>
                                                    <div class="form-group col-md-6 required">
                                                        <label for="dept" class="control-label" >Scheme Yearly Frequency :</label>
                                                        <select class="custom-select mr-sm-2" required="" name="scheme_yearly_frequency">
                                                            <option value="">--Please Select--</option>
                                                            <option  <% if (scheme_frequency.equals("1")) {%>selected=""<%}%> value="1">Once in a Year</option>
                                                            <option  <% if (scheme_frequency.equals("2")) {%>selected=""<%}%> value="2">Once in every 2 year</option>
                                                            <option  <% if (scheme_frequency.equals("3")) {%>selected=""<%}%> value="3">Once in every 3 Year</option>
                                                            <option  <% if (scheme_frequency.equals("100")) {%>selected=""<%}%> value="100">Once in a lifetime</option>
                                                            <option  <% if (scheme_frequency.equals("200")) {%>selected=""<%}%> value="200">As and when required</option>
                                                            <option  <% if (scheme_frequency.equals("300")) {%>selected=""<%}%> value="300">Not Applicable</option>
                                                        </select>
                                                    </div>

                                                </div>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="scheme_code" class="control-label ">Payment Mode :</label>
                                                        <div class="row form-check" style="border:none;">
                                                            <div class="col-md-12 payment_option_error">
                                                                <input type="radio" class="form-check-input" name="payment_mode" <% if (payment_mode.equals("1")) {%>checked="" <%}%> value="1"  onclick="openPfmsConfig(this.value)"> CTOS(Treasury) <br/>
                                                                <input type="radio" class="form-check-input" name="payment_mode" <% if (payment_mode.equals("2")) {%>checked="" <%}%> value="2" onclick="openPfmsConfig(this.value)" > PFMS <br/>
                                                                <input type="radio" class="form-check-input" name="payment_mode" <% if (payment_mode.equals("3")) {%>checked="" <%}%> value="3"  onclick="openPfmsConfig(this.value)"> NACH <br/>
                                                                <input type="radio" class="form-check-input" name="payment_mode" <% if (payment_mode.equals("4")) {%>checked="" <%}%> value="4"  onclick="openPfmsConfig(this.value)"> e-RUPI 


                                                            </div>
                                                            <!--                                                    <div class="col-md-4">
                                                                                                                </div>
                                                                                                                <div class="col-md-4">
                                                                                                                </div>-->
                                                        </div>
                                                        <span id="lblinstallmentError" style="color: red"></span>
                                                    </div>

                                                </div>
                                                <div id="pfms_config" <% if (payment_mode.equals("2")) {%> style="display:block;" <% } else { %> style="display:none;"<% }%>>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6">
                                                            <label for="dept" class="control-label" >PFMS Scheme Code :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="pfms_scheme_code" name="pfms_scheme_code" value="<%= pfms_scheme_code%>" >
                                                        </div>
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">Agency Name :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="agency_name" name="agency_name" value="<%= agency_name%>">
                                                        </div> 
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">Agency Code :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="agency_code" name="agency_code" value="<%= agency_code%>">
                                                        </div>
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">Bank Name :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="bank_name" name="bank_name" value="<%= bank_name%>">
                                                        </div> 
                                                    </div>

                                                    <div class="form-row">
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">Bank Account Number :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="bank_ac_no" name="bank_ac_no" value="<%= bank_ac_no%>">
                                                        </div>  
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">IFSC Code :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="ifsc_code" name="ifsc_code" value="<%= ifsc_code%>">
                                                        </div>  
                                                    </div>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">Account Holder's Name :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="ac_holder_name" name="ac_holder_name" value="<%= ac_holder_name%>">
                                                        </div>  
                                                        <div class="form-group col-md-6 required">
                                                            <label for="scheme" class="control-label">Maximum Amount of Debit Transaction :</label>
                                                            <input type="text" autocomplete="off" required="" class="form-control" id="debit_transaction_limit" name="debit_transaction_limit" value="<%= debit_tran_limit%>">
                                                        </div>  

                                                    </div>
                                                </div>
                                                <% if (benefit_type.equals("1")) {%>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="installment" class="control-label">Amount Fixed :</label>
                                                        <div class="row form-check" style="border:none;">
                                                            <div class="col-md-4">
                                                                <input type="radio" required="" class="form-check-input" name="amount_fixed" <% if (amount_fixed.equals("Y")) {%> checked=""<%}%> onclick="getCategoryAmountField(this.value)" value="Y" > Yes
                                                            </div>
                                                            <div class="col-md-4">
                                                                <input type="radio" required="" class="form-check-input" name="amount_fixed" <% if (amount_fixed.equals("N")) {%> checked=""<%}%> onclick="getCategoryAmountField(this.value)" value="N" > No
                                                            </div>
                                                        </div>
                                                        <span id="lblinstallmentError" style="color: red"></span>
                                                    </div>
                                                    <% if (!amount.equals("")) {%>
                                                    <div class="form-group col-md-6 <% if (amount_fixed.equals("Y")) { %>required <% }%>" id="amount_div">
                                                        <% if (amount_fixed.equals("Y")) { %>
                                                        <label for="periodicity" class="control-label">Amount :</label>
                                                        <% }%>
                                                        <div id="amt"><%=amount%></div>
                                                    </div>
                                                    <% } else { %>
                                                    <div class="form-group col-md-6" id="amount_div" style="display:none;">
                                                        <!--                                                        <label for="periodicity" class="control-label">Amount :</label>-->
                                                        <div id="amt"></div>
                                                    </div>
                                                    <% } %>
                                                </div>
                                                <% } else if (benefit_type.equals("2")) {%>
                                                <div class="form-row">
                                                    <div class="col-md-12" id="commodity_matrix_field">
                                                        <% if (commodity != null) {
                                                                for (int i = 0; i < commodity.length; i++) {
                                                                    ddlcommodity = cm.getSelectedCommodityList(commodity[i].split("~")[0], "-Please Select-");
                                                                    if (i == 0) {

                                                        %>
                                                        <div class="input-group">
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Commodity Name :</label>
                                                                <select class="custom-select mr-sm-2" required="" name="scheme_commodity">
                                                                    <%=ddlcommodity%>
                                                                </select>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Subsidy Amount :</label>
                                                                <input type="text" autocomplete="off" required="" class="form-control" name="subsidy_amount" value="<%= commodity[i].split("~")[1]%>" /> 
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-3 required">
                                                                <label for="periodicity" class="control-label">Effective From :</label>
                                                                <input type="date" required="" class="form-control" name="effective_from"  value="<%= commodity[i].split("~")[2]%>" />
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="input-group-btn col-md-1">
                                                                <br/><button class="btn btn-success" type="button"  onclick="commodity_matrix_field();"> <span class="plus">+</span> </button>
                                                            </div>
                                                        </div>
                                                        <% } else {%>
                                                        <div class="form-group removeclass<%=i%>">
                                                            <div class="input-group mt-2">
                                                                <div class="form-group col-md-4 required">
                                                                    <label for="installment" class="control-label">Commodity Name :</label>
                                                                    <select class="custom-select mr-sm-2" required="" name="scheme_commodity">
                                                                        <%=ddlcommodity%>
                                                                    </select>
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                                <div class="form-group col-md-4 required">
                                                                    <label for="installment" class="control-label">Subsidy Amount :</label>
                                                                    <input type="text" autocomplete="off" required="" class="form-control" name="subsidy_amount" value="<%= commodity[i].split("~")[1]%>" /> 
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                                <div class="form-group col-md-3 required">
                                                                    <label for="periodicity" class="control-label">Effective From :</label>
                                                                    <input type="date" required="" class="form-control" name="effective_from"  value="<%= commodity[i].split("~")[2]%>" />
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                                <div class="input-group-btn col-md-1">
                                                                    <br/><button class="btn btn-danger" type="button"  onclick="remove_commodity_matrix_field(<%=i%>);">  <span class="minus">-</span> </button>
                                                                </div>

                                                            </div>
                                                        </div>
                                                        <% }
                                                            }
                                                        } else {%>
                                                        <div class="input-group">
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Commodity Name :</label>
                                                                <select class="custom-select mr-sm-2" required="" name="scheme_commodity">
                                                                    <%=ddlcommodity%>
                                                                </select>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Subsidy Amount :</label>
                                                                <input type="text" autocomplete="off" required="" class="form-control" name="subsidy_amount" /> 
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-3 required">
                                                                <label for="periodicity" class="control-label">Effective From :</label>
                                                                <input type="date" required="" class="form-control" name="effective_from" />
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="input-group-btn col-md-1">
                                                                <br/><button class="btn btn-success" type="button"  onclick="commodity_matrix_field();"> <span class="plus">+</span> </button>
                                                            </div>
                                                        </div>
                                                        <% } %>
                                                        <span id="lblinstallmentError" style="color: red"></span>
                                                    </div>
                                                </div>    

                                                <% } else {%>
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="installment" class="control-label">Amount Fixed :</label>
                                                        <div class="row form-check" style="border:none;">
                                                            <div class="col-md-4">
                                                                <input type="radio" required="" class="form-check-input" name="amount_fixed" <% if (amount_fixed.equals("Y")) {%> checked=""<%}%> onclick="getCategoryAmountField()" value="Y" > Yes
                                                            </div>
                                                            <div class="col-md-4">
                                                                <input type="radio" required="" class="form-check-input" name="amount_fixed" <% if (amount_fixed.equals("N")) {%> checked=""<%}%> onclick="HideCategoryAmountField()" value="N" > No
                                                            </div>
                                                        </div>
                                                        <span id="lblinstallmentError" style="color: red"></span>
                                                    </div>
                                                    <% if (!amount.equals("")) {%>
                                                    <div class="form-group col-md-6 <% if (amount_fixed.equals("Y")) {%>required<%}%>" id="amount_div">
                                                        <label for="periodicity" class="control-label">Amount :</label>
                                                        <div id="amt"><%=amount%></div>
                                                    </div>
                                                    <% } else { %>
                                                    <div class="form-group col-md-6" id="amount_div" style="display:none;">
                                                        <label for="periodicity" class="control-label">Amount :</label>
                                                        <div id="amt"></div>
                                                    </div>
                                                    <% } %>
                                                </div>
                                                <div class="form-row">
                                                    <div class="col-md-12" id="commodity_matrix_field">
                                                        <% if (commodity != null) {
                                                                for (int i = 0; i < commodity.length; i++) {
                                                                    ddlcommodity = cm.getSelectedCommodityList(commodity[i].split("~")[0], "-Please Select-");
                                                                    if (i == 0) {

                                                        %>
                                                        <div class="input-group">
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Commodity Name :</label>
                                                                <select class="custom-select mr-sm-2" required="" name="scheme_commodity">
                                                                    <%=ddlcommodity%>
                                                                </select>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Subsidy Amount :</label>
                                                                <input type="text" autocomplete="off" required="" class="form-control" name="subsidy_amount" value="<%= commodity[i].split("~")[1]%>" /> 
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-3 required">
                                                                <label for="periodicity" class="control-label">Effective From :</label>
                                                                <input type="date" required="" class="form-control" name="effective_from"  value="<%= commodity[i].split("~")[2]%>" />
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="input-group-btn col-md-1">
                                                                <br/><button class="btn btn-success" type="button"  onclick="commodity_matrix_field();"> <span class="plus">+</span> </button>
                                                            </div>
                                                        </div>
                                                        <% } else {%>
                                                        <div class="form-group removeclass<%=i%>">
                                                            <div class="input-group mt-2">
                                                                <div class="form-group col-md-4 required">
                                                                    <label for="installment" class="control-label">Commodity Name :</label>
                                                                    <select class="custom-select mr-sm-2" required="" name="scheme_commodity">
                                                                        <%=ddlcommodity%>
                                                                    </select>
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                                <div class="form-group col-md-4 required">
                                                                    <label for="installment" class="control-label">Subsidy Amount :</label>
                                                                    <input type="text" autocomplete="off" required="" class="form-control" name="subsidy_amount" value="<%= commodity[i].split("~")[1]%>" /> 
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                                <div class="form-group col-md-3 required">
                                                                    <label for="periodicity" class="control-label">Effective From :</label>
                                                                    <input type="date" required="" class="form-control" name="effective_from"  value="<%= commodity[i].split("~")[2]%>" />
                                                                    <span id="lblinstallmentError" style="color: red"></span>
                                                                </div>
                                                                <div class="input-group-btn col-md-1">
                                                                    <br/><button class="btn btn-danger" type="button"  onclick="remove_commodity_matrix_field(<%=i%>);">  <span class="minus">-</span> </button>
                                                                </div>

                                                            </div>
                                                        </div>
                                                        <% }
                                                            }
                                                        } else {%>
                                                        <div class="input-group">
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Commodity Name :</label>
                                                                <select class="custom-select mr-sm-2" required="" name="scheme_commodity">
                                                                    <%=ddlcommodity%>
                                                                </select>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-4 required">
                                                                <label for="installment" class="control-label">Subsidy Amount :</label>
                                                                <input type="text" autocomplete="off" required="" class="form-control" name="subsidy_amount" /> 
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="form-group col-md-3 required">
                                                                <label for="periodicity" class="control-label">Effective From :</label>
                                                                <input type="date" required="" class="form-control" name="effective_from" />
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                            <div class="input-group-btn col-md-1">
                                                                <br/><button class="btn btn-success" type="button"  onclick="commodity_matrix_field();"> <span class="plus">+</span> </button>
                                                            </div>
                                                        </div>
                                                        <% } %>
                                                        <span id="lblinstallmentError" style="color: red"></span>
                                                    </div>
                                                </div>            


                                                <% }%>

                                                <div id="payment_config" <% if (!payment_config_html.equals("")) {%> style="display:block;"<% } else {%> style="display:none;"<% } %>>
                                                    <h2 class="fs-title">Payment Configuration</h2>
                                                    <% if (!payment_config_html.equals("")) {%>
                                                    <%=payment_config_html%>
                                                    <% } else {%>
                                                    <div class="form-row">
                                                        <div class="form-group col-md-6 required">
                                                            <label for="dept" class="control-label" >Payment Method :</label>
                                                            <select class="custom-select mr-sm-2" required="" name="payment_method" id="payment_method" onchange="checkMethodData(this.value)">
                                                                <%=ddlCalcType%>
                                                            </select>
                                                        </div>

                                                    </div>
                                                    <% }%>    
                                                    <div class="form-row" id="method2" style="display:none;">
                                                        <div class="form-group col-md-6 required">
                                                            <label for="dept" class="control-label" >Rate :</label>
                                                            <input type="text" name="rate" id="rate" onkeypress="return IsNumeric(event);" class="form-control" />
                                                            <span id="lblError" style="color: red"></span>
                                                        </div>
                                                        <div class="form-group col-md-6 required">
                                                            <label for="dept" class="control-label" >Unit :</label>
                                                            <select class="custom-select mr-sm-2" id="unit" required="" name="unit">
                                                                <option value="" selected>-Please Select-</option>
                                                                <option value="4~Attendence">Attendence</option>
                                                            </select>
                                                        </div>
                                                    </div>  

                                                    <div class="form-row" id="method1" style="display:none;">
                                                        <div class="col-md-12" id="param_field">
                                                            <div class="input-group">
                                                                <div class="form-group col-md-4 required">
                                                                    <label for="scheme" class="control-label">Conditional Parameter :</label>
                                                                    <select class="custom-select mr-sm-2 cond_param" id="cond_param1" name="cond_param" required="">
                                                                        <%= ddlParamdropdown%>
                                                                    </select>  
                                                                </div>
                                                                <div class="form-group col-md-4 required">
                                                                    <label for="scheme" class="control-label">Store Parameter Value In :</label>
                                                                    <select class="custom-select mr-sm-2" id="store_column" name="store_column" required="">
                                                                        <%= ddlStoreColumndropdown%>
                                                                    </select> 
                                                                </div>

                                                                <div class="input-group-btn col-md-1">
                                                                    <br/><button class="btn btn-success" type="button"  onclick="param_field();"> <span class="plus">+</span> </button>
                                                                </div>
                                                            </div>
                                                            <span id="lblinstallmentError" style="color: red"></span>
                                                        </div>
                                                    </div>

                                                </div>
                                                <input type="button" name="previous" id="previous" class="previous action-button-previous" value="Previous"/>
                                                <input type="button" id="submit" name="submit" class="next2 action-button" value="Save & Next"/>
                                            </form>
                                        </fieldset>
                                        <%--                                        <fieldset  id="stackholder_dept" <% if (step.equals("2")) { %>style="display:block;" <% }%>>--%>
                                        <fieldset id="stackholder_dept">
                                            <form id="msform3" action="../saveStackHolderDeptData" method="post">
                                                <input type="hidden" name="edit_stake_holder_dept_info" id="edit_stake_holder_dept_info" <% if (!edit.equals("")) {%>value="1" <% } else { %>value="0"<% }%> />
                                                <input type="hidden" name="dept_id" id="dept_id" value="<%=department_id%>">
                                                <input type="hidden" name="stake_schemeid" id="stake_schemeid" <% if (edit.equals("2")) {%> value="<%=scheme_id%>" <% }%>/>
                                                <input type="hidden" name="benefit_type" value="<%=benefit_type%>" />
                                                <input type="hidden" name="scheme_specific_info" value="1" />
                                                <div class="card">
                                                    <div class="card-header">
                                                        <h4>Beneficiary Selection Process</h4>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="form-row">
                                                            <div class="col-md-12" id="bsp_field">
                                                                <% if (stakeholder_dept != null) {
                                                                        for (int i = 0; i < stakeholder_dept.length; i++) {
                                                                            ddlDeptdropdown_maker = cm.getSelectedDepartment(stakeholder_dept[i].split("~")[0], "-Please Select-");
                                                                            ddlDeptdropdown_checker = cm.getSelectedDepartment(stakeholder_dept[i].split("~")[1], "-Please Select-");
                                                                            if (i == 0) {

                                                                %>
                                                                <div class="input-group">
                                                                    <div class="form-group col-md-4 required">
                                                                        <label for="scheme" class="control-label">Data Creation :</label>
                                                                        <select class="custom-select mr-sm-2" id="bsp_creation_dept" name="bsp_creation_dept" required="">
                                                                            <%= ddlDeptdropdown_maker%>
                                                                        </select>  
                                                                    </div>
                                                                    <div class="form-group col-md-4 required">
                                                                        <label for="scheme" class="control-label">Data Verification :</label>
                                                                        <select class="custom-select mr-sm-2" id="bsp_verification_dept" name="bsp_verification_dept" required="">
                                                                            <%= ddlDeptdropdown_checker%>
                                                                        </select> 
                                                                    </div>
                                                                    <div class="form-group col-md-3 required">
                                                                        <label for="scheme" class="control-label">Data Approval :</label>
                                                                        <input type="text" readonly="" name="bsp_approval_dept" id="bsp_approval_dept" class="form-control" value="<%=ddlDept%>">
                                                                    </div>

                                                                    <div class="input-group-btn col-md-1">
                                                                        <br/><button class="btn btn-success" type="button"  onclick="bsp_field();"> <span class="plus">+</span> </button>
                                                                    </div>
                                                                </div>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                                <% } else {%>
                                                                <div class="form-group removeclass<%=i%>">
                                                                    <div class="input-group mt-2">
                                                                        <div class="input-group">
                                                                            <div class="form-group col-md-4 required">
                                                                                <label for="scheme" class="control-label">Data Creation :</label>
                                                                                <select class="custom-select mr-sm-2" id="bsp_creation_dept" name="bsp_creation_dept" required="">
                                                                                    <%= ddlDeptdropdown_maker%>
                                                                                </select>  
                                                                            </div>
                                                                            <div class="form-group col-md-4 required">
                                                                                <label for="scheme" class="control-label">Data Verification :</label>
                                                                                <select class="custom-select mr-sm-2" id="bsp_verification_dept" name="bsp_verification_dept" required="">
                                                                                    <%= ddlDeptdropdown_checker%>
                                                                                </select> 
                                                                            </div>
                                                                            <div class="form-group col-md-3 required">
                                                                                <label for="scheme" class="control-label">Data Approval :</label>
                                                                                <input type="text" readonly="" name="bsp_approval_dept" id="bsp_approval_dept" class="form-control" value="<%=ddlDept%>">
                                                                            </div>


                                                                            <div class="input-group-btn col-md-1">
                                                                                <br/><button class="btn btn-danger" type="button"  onclick="remove_bsp_field(<%=i%>);">  <span class="minus">-</span> </button>
                                                                            </div>

                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <% }
                                                                    }
                                                                } else {%>
                                                                <div class="input-group">
                                                                    <div class="form-group col-md-4 required">
                                                                        <label for="scheme" class="control-label">Data Creation :</label>
                                                                        <select class="custom-select mr-sm-2" id="bsp_creation_dept" name="bsp_creation_dept" required="">
                                                                            <%= ddlDeptdropdown%>
                                                                        </select>  
                                                                    </div>
                                                                    <div class="form-group col-md-4 required">
                                                                        <label for="scheme" class="control-label">Data Verification :</label>
                                                                        <select class="custom-select mr-sm-2" id="bsp_verification_dept" name="bsp_verification_dept" required="">
                                                                            <%= ddlDeptdropdown%>
                                                                        </select> 
                                                                    </div>
                                                                    <div class="form-group col-md-3 required">
                                                                        <label for="scheme" class="control-label">Data Approval :</label>
                                                                        <input type="text" readonly="" name="bsp_approval_dept" id="bsp_approval_dept" class="form-control" value="<%=ddlDept%>">
                                                                    </div>

                                                                    <div class="input-group-btn col-md-1">
                                                                        <br/><button class="btn btn-success" type="button"  onclick="bsp_field();"> <span class="plus">+</span> </button>
                                                                    </div>
                                                                </div>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                                <% }%>
                                                                <span id="lblinstallmentError" style="color: red"></span>
                                                            </div>
                                                        </div>



                                                        <!--                                                        <div class="form-row">
                                                                                                                    <div class="form-group col-md-6 required">
                                                                                                                        <label for="scheme" class="control-label">Data Creation :</label>
                                                                                                                        <select class="custom-select mr-sm-2" id="bsp_creation_dept" name="bsp_creation_dept" required="">
                                                        <%= ddlDeptdropdown%>
                                                    </select>  
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label for="scheme" class="control-label">Jurisdiction Level (Optional):</label>
                                                    <select class="custom-select mr-sm-2" id="bsp_creation_jurisdiction_level" name="bsp_creation_jurisdiction_level" >
                                                        <%= ddlJurisdictiondropdown%>
                                                    </select>  
                                                </div>
                                            </div>
                                            <div class="form-row">
                                                <div class="form-group col-md-6 required">
                                                    <label for="scheme" class="control-label">Data Verification :</label>
                                                    <select class="custom-select mr-sm-2" id="bsp_verification_dept" name="bsp_verification_dept" required="">
                                                        <%= ddlDeptdropdown%>
                                                    </select> 
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label for="scheme" class="control-label">Jurisdiction Level (Optional) :</label>
                                                    <select class="custom-select mr-sm-2" id="bsp_verification_jurisdiction_level" name="bsp_verification_jurisdiction_level">
                                                        <%= ddlJurisdictiondropdown%>
                                                    </select>  
                                                </div>
                                            </div>
                                            <div class="form-row">
                                                <div class="form-group col-md-6 required">
                                                    <label for="scheme" class="control-label">Data Approval :</label>
                                                    <input type="text" readonly="" name="bsp_approval_dept" id="bsp_approval_dept" class="form-control" value="<%=ddlDept%>">
                                                </div>
                                                <div class="form-group col-md-6">
                                                    <label for="scheme" class="control-label">Jurisdiction Level (Optional):</label>
                                                    <select class="custom-select mr-sm-2" id="bsp_approval_jurisdiction_level" name="bsp_approval_jurisdiction_level">
                                                        <%= ddlJurisdictiondropdown%>
                                                    </select>  
                                                </div>
                                            </div>-->
                                                    </div>
                                                </div>

                                                <!--                                                <div class="card">
                                                                                                    <div class="card-header">
                                                                                                        <h4>Payment Process</h4>
                                                                                                    </div>
                                                                                                    <div class="card-body">
                                                                                                        <div class="form-row">
                                                                                                            <div class="form-group col-md-6">
                                                                                                                <label for="scheme" class="control-label">Data Creation :</label>
                                                                                                                <select class="custom-select mr-sm-2" id="pp_creation_dept" name="pp_creation_dept">
                                                <%= ddlDeptdropdown%>
                                            </select>  
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="scheme" class="control-label">Jurisdiction Level (Optional):</label>
                                            <select class="custom-select mr-sm-2" id="pp_creation_jurisdiction_level" name="pp_creation_jurisdiction_level">
                                                <%= ddlJurisdictiondropdown%>
                                            </select>  
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-6">
                                            <label for="scheme" class="control-label">Data Verification :</label>
                                            <select class="custom-select mr-sm-2" id="pp_verification_dept" name="pp_verification_dept">
                                                <%= ddlDeptdropdown%>
                                            </select>  
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="scheme" class="control-label">Jurisdiction Level (Optional):</label>
                                            <select class="custom-select mr-sm-2" id="pp_verification_jurisdiction_level" name="pp_verification_jurisdiction_level">
                                                <%= ddlJurisdictiondropdown%>
                                            </select>  
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group col-md-6 required">
                                            <label for="scheme" class="control-label">Data Approval :</label>
                                            <input type="text" readonly="" name="pp_approval_dept" id="pp_approval_dept" class="form-control" value="<%=ddlDept%>">
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label for="scheme" class="control-label">Jurisdiction Level (Optional):</label>
                                            <select class="custom-select mr-sm-2" id="pp_approval_jurisdiction_level" name="pp_approval_jurisdiction_level">
                                                <%= ddlJurisdictiondropdown%>
                                            </select>  
                                        </div>
                                    </div>
                                </div>
                            </div>               -->
                                                <input type="button" name="previous2" id="previous2" class="previous action-button-previous" value="Previous"/>
                                                <input type="button" id="submit_stackholder_dept_data" name="submit_stackholder_dept_data" class="next3 action-button" value="Save & Next"/>
                                            </form>
                                        </fieldset>   
                                        <fieldset id="additational_info" <% if (edit.equals("2")) { %>style="display:block;" <% } %>>
                                            <form id="msform4" action="../saveSchemeAdditational" method="post">
                                                <input type="hidden" name="edit_additational_info" id="edit_additational_info" <% if (!edit.equals("")) {%>value="1" <% } else { %>value="0"<% }%>/>
                                                <input type="hidden" name="tot_textbox" id="tot_textbox" <% if (!edit.equals("")) {%>value="<%=tot_textbox%>" <% } else { %>value=""<% }%> />
                                                <input type="hidden" name="tot_dropdown" id="tot_dropdown"  <% if (!edit.equals("")) {%>value="<%=tot_dropdown%>" <% } else { %>value=""<% }%> />
                                                <input type="hidden" name="tot_datefield" id="tot_datefield" <% if (!edit.equals("")) {%>value="<%=tot_datefield%>" <% } else { %>value=""<% }%> />
                                                <input type="hidden" name="tot_priority" id="tot_priority" <% if (!edit.equals("")) {%>value="<%=tot_priority%>" <% } else { %>value="1"<% }%> />

                                                <input type="hidden" name="additational_scheme_id" id="additational_scheme_id" <% if (edit.equals("2")) {%> value="<%=scheme_id%>" <% }%>/>
                                                <input type="hidden" name="benefit_type" value="<%=benefit_type%>" />
                                                <input type="hidden" name="scheme_specific_info" value="1" />
                                                <div class="form-row">
                                                    <h4>Scheme Specific Information</h4>
                                                </div>
                                                <div class="form-row">

                                                    <div class="row" id="scheme_specific_info_field">
                                                        <div class="form-group col-md-12">
                                                            <div class="form-row">
                                                                <div class="form-group col-md-6">
                                                                    <label for="scheme_code" class="control-label">Scheme Specific Document :(Please use 'CTRL' key for multiple selection)</label>
                                                                    <select class="custom-select mr-sm-2" multiple data-live-search="true" name="required_document_scheme">
                                                                        <%=ddldocumentscheme%>
                                                                    </select>                                      
                                                                </div>
                                                            </div>
                                                            <span name="add_textbox" id="add_textbox" class="btn btn-primary mr-2" onclick="openSchemeSpecificInfoHtml(1)">Add Textbox</span>
                                                            <span name="add_dropdown" id="add_textbox" class="btn btn-primary" onclick="openSchemeSpecificInfoHtml(2)">Add Dropdown</span>
                                                            <span name="add_master" id="add_master" class="btn btn-primary" onclick="openSchemeSpecificInfoHtml(3)">Include Prefilled List</span>
                                                            <span name="add_date" id="add_date" class="btn btn-primary" onclick="openSchemeSpecificInfoHtml(4)">Add Datepicker</span>

                                                            <span id="lblinstallmentError" style="color: red"></span>
                                                            <% if (!Specific_info_html.equals("")) {%>
                                                            <%=Specific_info_html%>
                                                            <%   } %>
                                                            <div class="form-row" id="prefilledlist" <% if (!prefilledid.equals("")) {%>style="display:block;" <% } else {%>style="display:none;"<% }%>>
                                                                <div class="form-group col-md-6">
                                                                    <input type="hidden" name="master_priority" id="master_priority" value="<%= master_priority_list%>" />
                                                                    <label for="scheme_code" class="control-label">Prefilled List :(Please use 'CTRL' key for multiple selection)</label>
                                                                    <select class="custom-select mr-sm-2" multiple data-live-search="true" name="prefilled_list" onclick="addPriorityForMaster()">
                                                                        <%=ddlprefilledlist%>
                                                                    </select>                                      
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <input type="button" name="previous3" id="previous3" class="previous action-button-previous" value="Previous"/>
                                                <input type="button" id="submit_specific_data" name="metadata" class="next4 action-button" value="Save & Next"/>
                                            </form>
                                        </fieldset>              
                                        <fieldset  id="preview" <% if (edit.equals("2")) { %>style="display:block;" <% }%>>
                                            <form id="msform5" action="${pageContext.request.contextPath}/saveSchemeConfigurationData" method="post">

                                                <h2 class="fs-title">Preview</h2>
                                                <div class="print" id="printableArea">


                                                </div>
                                                <input type="button" name="previous4" id="previous4" class="previous action-button-previous" value="Previous"/>
                                                <input type="button" id="submit_final_data" name="submit_final_data" class="submit_final_data action-button" value="Final Submit"/>
                                            </form>
                                        </fieldset>
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
    <script charset="utf8" src="${pageContext.request.contextPath}/JS/msform.js"></script>
    <!--    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>-->
    <script src='${pageContext.request.contextPath}/JS/jquery.easing.min.js'></script>
    <script src='${pageContext.request.contextPath}/JS/jquery.validate.js'></script>

    <script>
                                                                        document.getElementById("scheme_name").onkeypress = function (e) {
                                                                            var chr = String.fromCharCode(e.which);
                                                                            if ("></%()&+=\"".indexOf(chr) >= 0) {
                                                                                alert("Special Character Not Allowed!");
                                                                                return false;
                                                                            }
                                                                        };
                                                                        function printDiv(divName) {
                                                                            var printContents = document.getElementById(divName).innerHTML;
                                                                            var originalContents = document.body.innerHTML;

                                                                            document.body.innerHTML = printContents;

                                                                            window.print();

                                                                            document.body.innerHTML = originalContents;
                                                                        }
                                                                        function checkOtherCatSelected(ben_cat) {
                                                                            if (ben_cat === '18') {
                                                                                //  $("#beneficiary_category").val("");
                                                                                $("#scheme_extra_cat_fields").css("display", "block");
                                                                            } else {
                                                                                $("#scheme_extra_cat_fields").css("display", "none");
                                                                            }
                                                                        }
                                                                        function getSchemeSpecificInfoHtml(scheme_id) {
                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/SchemeSpecificInfoHtml?scheme_id=" + scheme_id,
                                                                                type: "POST",
                                                                                success: function (data) {
                                                                                    $("#title").css("display", "block");
                                                                                    $("#specific_info").html(data);
                                                                                }
                                                                            });
                                                                        }
                                                                        function openPfmsConfig(sel_value) {
                                                                            if (sel_value === "2") {
                                                                                $("#pfms_config").css("display", "block");
                                                                            } else {
                                                                                $("#pfms_config").css("display", "none");
                                                                            }

                                                                        }
                                                                        //jQuery time
                                                                        var current_fs, next_fs, other_fs, previous_fs; //fieldsets
                                                                        var left, opacity, scale; //fieldset properties which we will animate
                                                                        var animating; //flag to prevent quick multi-click glitches

                                                                        $(".next").click(function () {
                                                                            if ($('#beneficiary_fy_status_apl:checkbox:checked').length === 0 && $('#beneficiary_fy_status_bpl:checkbox:checked').length === 0) {
                                                                                $("#chk_option_error").css("display", "block");
                                                                                return false;
                                                                            } else {
                                                                                $("#chk_option_error").css("display", "none");
                                                                            }
                                                                            if ($('#data_entry_from_dept:checkbox:checked').length === 0 && $('#data_entry_from_citizen:checkbox:checked').length === 0) {
                                                                                $("#chk_option_error1").css("display", "block");
                                                                                return false;
                                                                            } else {
                                                                                $("#chk_option_error1").css("display", "none");
                                                                            }
                                                                            var txtApplDate = $('#active_upto').val();
                                                                            var txtVeriDate = $('#verification_last_date').val();
                                                                            var txtApprDate = $('#approval_last_date').val();
                                                                            var today = new Date();
                                                                            var dd = String(today.getDate()).padStart(2, '0');
                                                                            var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                                                                            var yyyy = today.getFullYear();

                                                                            today = yyyy + '-' + mm + '-' + dd;

                                                                            if (txtApplDate === "" || txtVeriDate === "" || txtApprDate === "") {
                                                                                alert("All dates are mandetory");
                                                                                return false;
                                                                            }
                                                                            if (txtApplDate < today) {
                                                                                alert("Application Submission date cannot be less than current date. ");
                                                                                return false;
                                                                            }
                                                                            if (txtVeriDate < txtApplDate || txtVeriDate > txtApprDate) {
                                                                                alert("Verification date cannot be less than Application submission date and Verification date cannot be greater than Approval date. ");
                                                                                return false;
                                                                            }
                                                                            if (txtApprDate < txtApplDate || txtApprDate < txtVeriDate) {
                                                                                alert("Approval date cannot be less than Application submission date and Verification date. ");
                                                                                return false;
                                                                            }


                                                                            if ($("#msform1").valid()) {
                                                                                event.preventDefault();

                                                                                var form = $('#msform1')[0];
                                                                                var data = new FormData(form);
                                                                                $.ajax({
                                                                                    url: "${pageContext.request.contextPath}/saveSchemeBasicInfo",
                                                                                    type: "POST",
                                                                                    enctype: 'multipart/form-data',
                                                                                    data: data,
                                                                                    processData: false,
                                                                                    contentType: false,
                                                                                    cache: false,
                                                                                    success: function (data) {
                                                                                        if (Number.isInteger(parseInt(data))) {
                                                                                            $("#scheme_id").val(data);
                                                                                            if (animating)
                                                                                                return false;
                                                                                            animating = true;
                                                                                            current_fs = $("#basic_info");
                                                                                            // next_fs = $(this).parent().next();
                                                                                            next_fs = $("#payment_info");
                                                                                            //alert(next_fs);
                                                                                            //activate next step on progressbar using the index of next_fs
                                                                                            $("#progressbar li").eq(1).addClass("active");
                                                                                            $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
                                                                                            $("#progressbar li").eq($("fieldset").index(current_fs)).addClass("success");
                                                                                            //show the next fieldset
                                                                                            next_fs.show();
                                                                                            //hide the current fieldset with style
                                                                                            current_fs.animate({opacity: 0}, {
                                                                                                step: function (now, mx) {
                                                                                                    //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                                    //1. scale current_fs down to 80%
                                                                                                    scale = 1 - (1 - now) * 0.5;
                                                                                                    //2. bring next_fs from the right(50%)
                                                                                                    left = (now * 50) + "%";
                                                                                                    //3. increase opacity of next_fs to 1 as it moves in
                                                                                                    opacity = 1 - now;
                                                                                                    current_fs.css({
                                                                                                        'transform': 'scale(' + scale + ')',
                                                                                                        'position': 'absolute'
                                                                                                    });
                                                                                                    next_fs.css({'left': left, 'opacity': opacity});
                                                                                                },
                                                                                                duration: 800,
                                                                                                complete: function () {
                                                                                                    current_fs.hide();
                                                                                                    animating = false;
                                                                                                },
                                                                                                //this comes from the custom easing plugin
                                                                                                easing: 'easeInOutBack'
                                                                                            });
                                                                                        } else {
                                                                                            alert(data);
//                                                                                        if (data.length > 5) {
//                                                                                            var scheme_id = data.split("~")[1];
//                                                                                            if (window.confirm('Scheme already exists! Please click ok to see details!'))
//                                                                                            {
//                                                                                                window.open("${pageContext.request.contextPath}/Schemes/registeredSchemelist.jsp?data=" + scheme_id, "_blank");
//                                                                                            }
//
//
//                                                                                        } 
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                        $("#previous").click(function () {
                                                                            $("#edit_basic_info").val("1");
                                                                            if (animating)
                                                                                return false;
                                                                            animating = true;
                                                                            current_fs = $("#payment_info");
                                                                            previous_fs = $("#basic_info");

                                                                            //de-activate current step on progressbar
                                                                            $("#progressbar li").eq(2).removeClass("active");

                                                                            //show the previous fieldset
                                                                            previous_fs.show();
                                                                            //hide the current fieldset with style
                                                                            current_fs.animate({opacity: 0}, {
                                                                                step: function (now, mx) {
                                                                                    //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                    //1. scale previous_fs from 80% to 100%
                                                                                    scale = 0.8 + (1 - now) * 0.2;
                                                                                    //2. take current_fs to the right(50%) - from 0%
                                                                                    left = ((1 - now) * 50) + "%";
                                                                                    //3. increase opacity of previous_fs to 1 as it moves in
                                                                                    opacity = 1 - now;
                                                                                    previous_fs.css({
                                                                                        'transform': 'scale(' + scale + ')',
                                                                                        'position': 'relative',
                                                                                        'opacity': opacity
                                                                                    });
                                                                                    current_fs.css({'left': left, 'opacity': opacity});

//                                                                            current_fs.css({'left': left});
//                                                                            previous_fs.css({'transform': 'scale(' + scale + ')','opacity': opacity});
                                                                                },
                                                                                duration: 800,
                                                                                complete: function () {
                                                                                    current_fs.hide();
                                                                                    animating = false;
                                                                                },
                                                                                //this comes from the custom easing plugin
                                                                                easing: 'easeInOutBack'
                                                                            });
                                                                        });
                                                                        $(".next2").click(function () {

                                                                            var scheme_id = $("#scheme_id").val();
                                                                            var benefit_type = $("#benefit_type").val();
                                                                            if ($("#msform2").valid()) {
                                                                                $.ajax({
                                                                                    url: "${pageContext.request.contextPath}/saveSchemePaymentInfo",
                                                                                    type: "POST",
                                                                                    data: $("#msform2").serialize(),
                                                                                    success: function (data) {
                                                                                        if (Number.isInteger(parseInt(data))) {
                                                                                            $("#stake_schemeid").val(data);
                                                                                            if (animating)
                                                                                                return false;
                                                                                            animating = true;
                                                                                            current_fs = $("#payment_info");
                                                                                            // next_fs = $(this).parent().next();
                                                                                            next_fs = $("#stackholder_dept");
                                                                                            //activate next step on progressbar using the index of next_fs
                                                                                            $("#progressbar li").eq(2).addClass("active");
                                                                                            $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
                                                                                            $("#progressbar li").eq(1).addClass("success");
                                                                                            //show the next fieldset
                                                                                            next_fs.show();
                                                                                            //hide the current fieldset with style
                                                                                            current_fs.animate({opacity: 0}, {
                                                                                                step: function (now, mx) {
                                                                                                    //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                                    //1. scale current_fs down to 80%
                                                                                                    scale = 1 - (1 - now) * 0.2;
                                                                                                    //2. bring next_fs from the right(50%)
                                                                                                    left = (now * 50) + "%";
                                                                                                    //3. increase opacity of next_fs to 1 as it moves in
                                                                                                    opacity = 1 - now;
                                                                                                    current_fs.css({
                                                                                                        'transform': 'scale(' + scale + ')',
                                                                                                        'position': 'absolute'
                                                                                                    });
                                                                                                    next_fs.css({'left': left, 'opacity': opacity});
                                                                                                },
                                                                                                duration: 800,
                                                                                                complete: function () {
                                                                                                    current_fs.hide();
                                                                                                    animating = false;
                                                                                                },
                                                                                                //this comes from the custom easing plugin
                                                                                                easing: 'easeInOutBack'
                                                                                            });
                                                                                        } else {
                                                                                            alert(data);
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                        $("#previous2").click(function () {
                                                                            $("#edit_payment_info").val("1");
                                                                            if (animating)
                                                                                return false;
                                                                            animating = true;
                                                                            current_fs = $("#stackholder_dept");
                                                                            previous_fs = $("#payment_info");

                                                                            //de-activate current step on progressbar
                                                                            $("#progressbar li").eq(3).removeClass("active");

                                                                            //show the previous fieldset
                                                                            previous_fs.show();
                                                                            //hide the current fieldset with style
                                                                            current_fs.animate({opacity: 0}, {
                                                                                step: function (now, mx) {
                                                                                    //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                    //1. scale previous_fs from 80% to 100%
                                                                                    scale = 0.8 + (1 - now) * 0.2;
                                                                                    //2. take current_fs to the right(50%) - from 0%
                                                                                    left = ((1 - now) * 50) + "%";
                                                                                    //3. increase opacity of previous_fs to 1 as it moves in
                                                                                    opacity = 1 - now;
                                                                                    previous_fs.css({
                                                                                        'transform': 'scale(' + scale + ')',
                                                                                        'position': 'relative',
                                                                                        'opacity': opacity
                                                                                    });
                                                                                    current_fs.css({'left': left, 'opacity': opacity});

//                                                                            current_fs.css({'left': left});
//                                                                            previous_fs.css({'transform': 'scale(' + scale + ')', 'opacity': opacity});
                                                                                },
                                                                                duration: 800,
                                                                                complete: function () {
                                                                                    current_fs.hide();
                                                                                    animating = false;
                                                                                },
                                                                                //this comes from the custom easing plugin
                                                                                easing: 'easeInOutBack'
                                                                            });
                                                                        });
                                                                        $(".next3").click(function () {
//                                                                    if ($('#data_entry_from_dept:checkbox:checked').length === 0 && $('#data_entry_from_citizen:checkbox:checked').length === 0) {
//                                                                        $("#chk_option_error1").css("visibility", "visible");
//                                                                        return false;
//                                                                    } else {
//                                                                        $("#chk_option_error1").css("visibility", "hidden");
//                                                                    }
                                                                            var scheme_id = $("#stake_schemeid").val();
                                                                            var benefit_type = $("#benefit_type").val();
                                                                            if ($("#msform3").valid()) {
                                                                                $.ajax({
                                                                                    url: "${pageContext.request.contextPath}/saveStackHolderDeptData",
                                                                                    type: "POST",
                                                                                    data: $("#msform3").serialize(),
                                                                                    success: function (data) {
                                                                                        // alert(data);
                                                                                        $("#schemeid").val(data);
                                                                                        $("#schemeid_final").val(data);
                                                                                        $("#additational_scheme_id").val(data);
                                                                                        if (animating)
                                                                                            return false;
                                                                                        animating = true;
                                                                                        current_fs = $("#stackholder_dept");
                                                                                        // next_fs = $(this).parent().next();
                                                                                        next_fs = $("#additational_info");
                                                                                        //activate next step on progressbar using the index of next_fs
                                                                                        $("#progressbar li").eq(3).addClass("active");
                                                                                        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
                                                                                        $("#progressbar li").eq(2).addClass("success");
                                                                                        //show the next fieldset
                                                                                        next_fs.show();
                                                                                        //hide the current fieldset with style
                                                                                        current_fs.animate({opacity: 0}, {
                                                                                            step: function (now, mx) {
                                                                                                //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                                //1. scale current_fs down to 80%
                                                                                                scale = 1 - (1 - now) * 0.2;
                                                                                                //2. bring next_fs from the right(50%)
                                                                                                left = (now * 50) + "%";
                                                                                                //3. increase opacity of next_fs to 1 as it moves in
                                                                                                opacity = 1 - now;
                                                                                                current_fs.css({
                                                                                                    'transform': 'scale(' + scale + ')',
                                                                                                    'position': 'absolute'
                                                                                                });
                                                                                                next_fs.css({'left': left, 'opacity': opacity});
                                                                                            },
                                                                                            duration: 800,
                                                                                            complete: function () {
                                                                                                current_fs.hide();
                                                                                                animating = false;
                                                                                            },
                                                                                            //this comes from the custom easing plugin
                                                                                            easing: 'easeInOutBack'
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                        $("#previous3").click(function () {
                                                                            $("#edit_stake_holder_dept_info").val("1");
                                                                            if (animating)
                                                                                return false;
                                                                            animating = true;
                                                                            current_fs = $("#additational_info");
                                                                            previous_fs = $("#stackholder_dept");

                                                                            //de-activate current step on progressbar
                                                                            $("#progressbar li").eq(4).removeClass("active");

                                                                            //show the previous fieldset
                                                                            previous_fs.show();
                                                                            //hide the current fieldset with style
                                                                            current_fs.animate({opacity: 0}, {
                                                                                step: function (now, mx) {
                                                                                    //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                    //1. scale previous_fs from 80% to 100%
                                                                                    scale = 0.8 + (1 - now) * 0.2;
                                                                                    //2. take current_fs to the right(50%) - from 0%
                                                                                    left = ((1 - now) * 50) + "%";
                                                                                    //3. increase opacity of previous_fs to 1 as it moves in
                                                                                    opacity = 1 - now;
                                                                                    previous_fs.css({
                                                                                        'transform': 'scale(' + scale + ')',
                                                                                        'position': 'relative',
                                                                                        'opacity': opacity
                                                                                    });
                                                                                    current_fs.css({'left': left, 'opacity': opacity});

//                                                                            current_fs.css({'left': left});
//                                                                            previous_fs.css({'transform': 'scale(' + scale + ')', 'opacity': opacity});
                                                                                },
                                                                                duration: 800,
                                                                                complete: function () {
                                                                                    current_fs.hide();
                                                                                    animating = false;
                                                                                },
                                                                                //this comes from the custom easing plugin
                                                                                easing: 'easeInOutBack'
                                                                            });
                                                                        });

                                                                        $(".next4").click(function () {

                                                                            var scheme_id = $("#additational_scheme_id").val();
                                                                            var benefit_type = $("#benefit_type").val();

                                                                            // $("#msform3").load();
                                                                            if ($("#msform4").valid()) {
                                                                                $.ajax({
                                                                                    url: "${pageContext.request.contextPath}/saveSchemeAdditational",
                                                                                    type: "POST",
                                                                                    data: $("#msform4").serialize(),
                                                                                    success: function (data) {
                                                                                        // alert(data);

                                                                                        //   $("#schemeid_final").val(data);
                                                                                        $("#schemeid").val(data);
                                                                                        if (animating)
                                                                                            return false;
                                                                                        animating = true;
                                                                                        current_fs = $("#additational_info");
                                                                                        // next_fs = $(this).parent().next();
                                                                                        next_fs = $("#preview");
                                                                                        //activate next step on progressbar using the index of next_fs
                                                                                        $("#progressbar li").eq(4).addClass("active");
                                                                                        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
                                                                                        $("#progressbar li").eq(3).addClass("success");
                                                                                        //show the next fieldset
                                                                                        next_fs.show();
                                                                                        //hide the current fieldset with style
                                                                                        current_fs.animate({opacity: 0}, {
                                                                                            step: function (now, mx) {
                                                                                                //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                                //1. scale current_fs down to 80%
                                                                                                scale = 1 - (1 - now) * 0.2;
                                                                                                //2. bring next_fs from the right(50%)
                                                                                                left = (now * 50) + "%";
                                                                                                //3. increase opacity of next_fs to 1 as it moves in
                                                                                                opacity = 1 - now;
                                                                                                current_fs.css({
                                                                                                    'transform': 'scale(' + scale + ')',
                                                                                                    'position': 'absolute'
                                                                                                });
                                                                                                next_fs.css({'left': left, 'opacity': opacity});
                                                                                            },
                                                                                            duration: 800,
                                                                                            complete: function () {
                                                                                                current_fs.hide();
                                                                                                animating = false;
                                                                                            },
                                                                                            //this comes from the custom easing plugin
                                                                                            easing: 'easeInOutBack'
                                                                                        });

                                                                                        $.ajax({
                                                                                            url: "${pageContext.request.contextPath}/SchemePreviewDetails?scheme_id=" + scheme_id,
                                                                                            success: function (data) {
                                                                                                $("#printableArea").html(data);
                                                                                                getSchemeSpecificInfoHtml(scheme_id);
                                                                                                getRequiredDocumntListpoi(scheme_id);
                                                                                                getRequiredDocumntListpoa(scheme_id);
                                                                                                getRequiredDocumntListscheme(scheme_id);
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                        $("#previous4").click(function () {
                                                                            $("#edit_additational_info").val("1");
                                                                            if (animating)
                                                                                return false;
                                                                            animating = true;
                                                                            current_fs = $("#preview");
                                                                            previous_fs = $("#additational_info");

                                                                            //de-activate current step on progressbar
                                                                            $("#progressbar li").eq(5).removeClass("active");

                                                                            //show the previous fieldset
                                                                            previous_fs.show();
                                                                            //hide the current fieldset with style
                                                                            current_fs.animate({opacity: 0}, {
                                                                                step: function (now, mx) {
                                                                                    //as the opacity of current_fs reduces to 0 - stored in "now"
                                                                                    //1. scale previous_fs from 80% to 100%
                                                                                    scale = 0.8 + (1 - now) * 0.2;
                                                                                    //2. take current_fs to the right(50%) - from 0%
                                                                                    left = ((1 - now) * 50) + "%";
                                                                                    //3. increase opacity of previous_fs to 1 as it moves in
                                                                                    opacity = 1 - now;
                                                                                    previous_fs.css({
                                                                                        'transform': 'scale(' + scale + ')',
                                                                                        'position': 'relative',
                                                                                        'opacity': opacity
                                                                                    });
                                                                                    current_fs.css({'left': left, 'opacity': opacity});

//                                                                            current_fs.css({'left': left});
//                                                                            previous_fs.css({'transform': 'scale(' + scale + ')', 'opacity': opacity});
                                                                                },
                                                                                duration: 800,
                                                                                complete: function () {
                                                                                    current_fs.hide();
                                                                                    animating = false;
                                                                                },
                                                                                //this comes from the custom easing plugin
                                                                                easing: 'easeInOutBack'
                                                                            });
                                                                        });
                                                                        $(".submit_final_data").click(function () {
                                                                            var schemeid_final = $("#schemeid_final").val();
                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/saveSchemeFinalData?schemeid_final=" + schemeid_final,
                                                                                type: "POST",
                                                                                //data: $("#schemeid_final").serialize(),
                                                                                success: function (data) {
                                                                                    alert("Data Successfully Submitted");
                                                                                    $(location).attr('href', '${pageContext.request.contextPath}/Schemes/scheme_management.jsp');


                                                                                }
                                                                            });

                                                                        });
//                                                                $(".publish").click(function () {
//                                                                    var scheme_id = $("#scheme_id").val();
//                                                                    var benefit_type = $("#benefit_type").val();
//                                                                    if ($("#msform3").valid()) {
//                                                                        $.ajax({
//                                                                            url: "${pageContext.request.contextPath}/saveSchemeConfigurationData",
//                                                                            type: "POST",
//                                                                            data: $("#msform3").serialize(),
//                                                                            success: function (data) {
//                                                                                alert("Data Successfully Updated");
//                                                                                $(location).attr('href', '${pageContext.request.contextPath}/Schemes/scheme_management.jsp');
//                                                                                // alert(data);
//                                                                                //$("#scheme_id").val(data);
//
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                });
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
                                                                        function isAlphaNumeric(e) {
                                                                            var keyCode = window.e ? e.keyCode : e.which;
                                                                            var lblError = document.getElementById("lblError");
                                                                            lblError.innerHTML = "";
                                                                            //Regex for Valid Characters i.e. Alphabets and Numbers.
                                                                            var regex = /^[A-Za-z0-9]+$/;
                                                                            //Validate TextBox value against the Regex.
                                                                            var isValid = regex.test(String.fromCharCode(keyCode));
                                                                            if (!isValid) {
                                                                                lblError.innerHTML = "Only Alphabets and Numbers allowed.";
                                                                            }

                                                                            return isValid;
                                                                        }
                                                                        function lettersOnly()
                                                                        {
                                                                            var charCode = event.keyCode;
                                                                            if ((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123) || charCode == 8)
                                                                                return true;
                                                                            else
                                                                                return false;
                                                                        }
                                                                        var room = 1;
                                                                        var maxField = 50;
                                                                        var x = 1;
                                                                        function scheme_features_fields() {
                                                                            if (x < maxField) {
                                                                                x++;
                                                                                var scheme_features_array =
                                                                                        room++;
                                                                                var objTo = document.getElementById('scheme_features_fields')
                                                                                var divtest = document.createElement("div");
                                                                                divtest.setAttribute("class", "form-group removeclass" + room);
                                                                                var rdiv = 'removeclass' + room;
                                                                                divtest.innerHTML = '<div class="input-group mt-2"><input type="text" autocomplete="off" class="form-control" name="scheme_features"  value="" ><div class="input-group-btn"> <button class="btn btn-danger" type="button" onclick="remove_scheme_features_fields(' + room + ');"><span class="minus">-</span> </button></div></div></div><div class="clear"></div>';
                                                                                objTo.appendChild(divtest)
                                                                            }
                                                                        }
                                                                        function remove_scheme_features_fields(rid) {
                                                                            $('.removeclass' + rid).remove();
                                                                        }

                                                                        var oth_cat = 1;
                                                                        var maxOthField = 50;
                                                                        var oth = 1;
                                                                        function scheme_extra_cat_fields() {
                                                                            if (oth < maxOthField) {
                                                                                oth++;
                                                                                var scheme_features_array =
                                                                                        oth_cat++;
                                                                                var objTo = document.getElementById('scheme_extra_cat_fields')
                                                                                var divtest = document.createElement("div");
                                                                                divtest.setAttribute("class", "form-group removeclass" + oth_cat);
                                                                                var rdiv = 'removeclass' + oth_cat;
                                                                                divtest.innerHTML = '<div class="input-group mt-2"><input type="text" autocomplete="off" class="form-control" name="other_category"  value="" ><div class="input-group-btn"> <button class="btn btn-danger" type="button" onclick="remove_scheme_other_cat_fields(' + oth_cat + ');"><span class="minus">-</span> </button></div></div></div><div class="clear"></div>';
                                                                                objTo.appendChild(divtest)
                                                                            }
                                                                        }
                                                                        function remove_scheme_other_cat_fields(rid) {
                                                                            $('.removeclass' + rid).remove();
                                                                        }

                                                                        $(document).ready(function () {

                                                                            $("#msform1").validate({
                                                                                rules: {
                                                                                    //  dept: "required",
                                                                                    scheme_yearly_frequency: "required",
                                                                                    scheme_desc: "required"
                                                                                },
                                                                                messages: {
                                                                                    //   dept: "This field is required.",
                                                                                    scheme_benefit: "This field is required.",
                                                                                    scheme_desc: "This field is required."
                                                                                }
                                                                            });
                                                                            $("#msform2").validate({
                                                                                rules: {
                                                                                    scheme_periodicity: "required",
                                                                                    scheme_yearly_frequency: "required",
                                                                                    payment_mode: "required"

                                                                                },
                                                                                messages: {
                                                                                    scheme_periodicity: "This field is required.",
                                                                                    scheme_yearly_frequency: "This field is required.",
                                                                                    payment_mode: "This field is required."
                                                                                },
                                                                                errorPlacement: function (error, element) {
                                                                                    if (element.is(":radio")) {
                                                                                        error.prependTo(element.parent());
                                                                                    } else { // This is the default behavior of the script
                                                                                        error.insertAfter(element);
                                                                                    }
                                                                                }
                                                                            });
                                                                        });
                                                                        function getCategoryAmountField(amount_cond) {

                                                                            var scheme_id = $("#scheme_id").val();
                                                                            if (amount_cond == "Y") {
                                                                                $("#amount_div").css("display", "block");
                                                                                $("#amount_div").addClass("required");
                                                                                $("#payment_config").css("display", "none");
                                                                            } else {
//                                                                                $("#amount_div").removeClass("required");
                                                                                $("#amount_div").css("display", "none");
                                                                                $("#payment_config").css("display", "block");
                                                                            }


                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/AjaxServlet?getcat_financial_status_html=1&scheme_id=" + scheme_id + "&amount_cond=" + amount_cond,
                                                                                type: "POST",
                                                                                success: function (data) {
                                                                                    $("#amt").html(data);
                                                                                }
                                                                            });
                                                                        }
                                                                        function HideCategoryAmountField() {
                                                                            $("#amount_div").css("display", "none");

                                                                        }
                                                                        function showEnroll_date() {
                                                                            $("#enroll_date").css("display", "block");
                                                                        }
                                                                        function hideEnroll_date() {
                                                                            $("#enroll_date").css("display", "none");
                                                                        }

                                                                        var room2 = 1;
                                                                        var maxField2 = 50;
                                                                        var x2 = 1;
                                                                        function commodity_matrix_field() {
                                                                            if (x2 < maxField2) {
                                                                                x2++;
                                                                                var commodity_matrix_array =
                                                                                        room2++;
                                                                                var objTo2 = document.getElementById('commodity_matrix_field')
                                                                                var divtest2 = document.createElement("div");
                                                                                divtest2.setAttribute("class", "form-group removeclass" + room2);
                                                                                var rdiv = 'removeclass' + room2;
                                                                                divtest2.innerHTML = '<div class="input-group mt-2"><div class="form-group col-md-4 required"><label for="installment" class="control-label">Commodity Name :</label><select class="custom-select mr-sm-2" required="" name="scheme_commodity"><%=ddlcommodity%></select><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-4 required"><label for="installment" class="control-label">Subsidy Amount :</label><input type="text" required="" class="form-control" name="subsidy_amount" /><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-3 required"><label for="periodicity" class="control-label">Effective From :</label><input type="date" required="" class="form-control" name="effective_from" /><span id="lblinstallmentError" style="color: red"></span></div><div class="input-group-btn col-md-1"><br/><button class="btn btn-danger" type="button" onclick="remove_commodity_matrix_field(' + room2 + ');"><span class="minus">-</span> </button></div></div></div><div class="clear"></div></div>';
                                                                                objTo2.appendChild(divtest2)
                                                                            }
                                                                        }
                                                                        function remove_commodity_matrix_field(rid) {
                                                                            $('.removeclass' + rid).remove();
                                                                        }



                                                                        var bsp_room = 1;
                                                                        var bsp_maxField = 50;
                                                                        var bsp_x = 1;
                                                                        function bsp_field() {
                                                                            if (bsp_x < bsp_maxField) {
                                                                                bsp_x++;
                                                                                var commodity_matrix_array =
                                                                                        bsp_room++;
                                                                                var objTo2 = document.getElementById('bsp_field')
                                                                                var divtest2 = document.createElement("div");
                                                                                divtest2.setAttribute("class", "form-group removeclass" + bsp_room);
                                                                                var rdiv = 'removeclass' + bsp_room;
                                                                                divtest2.innerHTML = ' <div class="input-group"><div class="form-group col-md-4 required"><label for="scheme" class="control-label">Data Creation :</label><select class="custom-select mr-sm-2" id="bsp_creation_dept" name="bsp_creation_dept" required=""><%= ddlDeptdropdown%></select>  </div><div class="form-group col-md-4 required"><label for="scheme" class="control-label">Data Verification :</label><select class="custom-select mr-sm-2" id="bsp_verification_dept" name="bsp_verification_dept" required=""><%= ddlDeptdropdown%></select> </div><div class="form-group col-md-3 required"><label for="scheme" class="control-label">Data Approval :</label> <input type="text" readonly="" name="bsp_approval_dept" id="bsp_approval_dept" class="form-control" value="<%=ddlDept%>"></div><div class="input-group-btn col-md-1"><br/><button class="btn btn-danger" type="button" onclick="remove_bsp_field(' + bsp_room + ');"><span class="minus">-</span> </button></div></div></div><div class="clear"></div></div>';
                                                                                objTo2.appendChild(divtest2)
                                                                            }
                                                                        }
                                                                        function remove_bsp_field(rid) {
                                                                            $('.removeclass' + rid).remove();
                                                                        }

                                                                        var param_room = 1;
                                                                        var param_maxField = 50;
                                                                        var param_x = 1;
                                                                        function param_field() {
//                                                                            var index = $('#cond_param').get(0).selectedIndex;
//                                                                            $('#cond_param option:eq(' + index + ')').remove();
                                                                            if (param_x < param_maxField) {
                                                                                param_x++;
                                                                                var commodity_matrix_array = param_room++;
                                                                                var objTo2 = document.getElementById('param_field');
                                                                                var divtest2 = document.createElement("div");
                                                                                divtest2.setAttribute("class", "form-group removeclass" + param_room);
                                                                                var rdiv = 'removeclass' + param_room;
                                                                                divtest2.innerHTML = ' <div class="input-group"><div class="form-group col-md-4 required"><label for="scheme" class="control-label">Conditional Parameter :</label><select class="custom-select mr-sm-2 cond_param" id="cond_param' + param_room + '" name="cond_param" required=""><%= ddlParamdropdown%></select>  </div><div class="form-group col-md-4 required"><label for="scheme" class="control-label">Store Parameter Value In :</label><select class="custom-select mr-sm-2" id="store_column" name="store_column" required=""><%= ddlStoreColumndropdown%></select> </div><div class="input-group-btn col-md-1"><br/><button class="btn btn-danger" type="button" onclick="remove_bsp_field(' + param_room + ');"><span class="minus">-</span> </button></div></div></div><div class="clear"></div></div>';
                                                                                objTo2.appendChild(divtest2);
                                                                            }
                                                                            //resetOptions();
                                                                        }
                                                                        function remove_param_field(rid) {
                                                                            $('.removeclass' + rid).remove();
                                                                            param_room--;
                                                                            //resetOptions();
                                                                        }

                                                                        $('select[id*="cond_param"]').change(function () {
                                                                            //resetOptions();
                                                                        });

                                                                        function resetOptions() {
                                                                            var selectedOptions = $('select[id*="cond_param"] option:selected');
                                                                            console.log(selectedOptions);
                                                                            $('select option').removeAttr('disabled');
                                                                            selectedOptions.each(function () {
                                                                                var val = this.value;
                                                                                console.log(val);
                                                                                if (val !== '') {
                                                                                    var id = $(this).parent('select[id*="cond_param"]').prop('id');
                                                                                    console.log(id);
                                                                                    var options = $('select[id*="cond_param"]:not(#' + id + ') option[value=' + val + ']');
                                                                                    options.prop('disabled', 'true');
                                                                                }
                                                                            });
                                                                        }
                                                                        ;


                                                                        function openSchemeSpecificInfoHtml(field_type) {
                                                                            var tot_priority_count = $("#tot_priority").val();
                                                                            if (field_type === 1) {
                                                                                addTextboxConfig(tot_priority_count);
                                                                                var textbox_count = $("#tot_textbox").val();
                                                                                textbox_count++;
                                                                                $("#tot_textbox").val(textbox_count);
                                                                                tot_priority_count++;
                                                                                $("#tot_priority").val(tot_priority_count);
                                                                            } else if (field_type === 2) {
                                                                                addDropdownConfig(tot_priority_count);
                                                                                var dropdown_count = $("#tot_dropdown").val();
                                                                                dropdown_count++;
                                                                                $("#tot_dropdown").val(dropdown_count);
                                                                                tot_priority_count++;
                                                                                $("#tot_priority").val(tot_priority_count);
                                                                            } else if (field_type === 3) {
                                                                                $("#prefilledlist").css("display", "block");
                                                                                var tot_priority_count = $("#tot_priority").val();
                                                                                tot_priority_count++;
                                                                                $("#tot_priority").val(tot_priority_count);

                                                                            } else {
                                                                                addDateFieldConfig(tot_priority_count);
                                                                                var datefield_count = $("#tot_datefield").val();
                                                                                datefield_count++;
                                                                                $("#tot_datefield").val(datefield_count);
                                                                                tot_priority_count++;
                                                                                $("#tot_priority").val(tot_priority_count);
                                                                            }
                                                                        }
                                                                        function addPriorityForMaster() {
                                                                            var tot_priority_count = $("#tot_priority").val();
                                                                            tot_priority_count++;
                                                                            var prev_master_priority = $("#master_priority").val();
                                                                            $("#master_priority").val(prev_master_priority + "," + tot_priority_count);
                                                                            $("#tot_priority").val(tot_priority_count);
                                                                        }
                                                                        function openFieldtypeSelection() {
                                                                            $("#scheme_specific_info_field").append('<div class="form-group col-md-8 required"><div class="input-group"><div class="form-group col-md-3"><label for="installment" class="control-label">Field Type :</label><select id="field_type" class="custom-select mr-sm-2" name="field_type" onchange="openSchemeSpecificInfoHtml(this.value)"><option value="">Please Select</option><option value="1" selected>Textbox</option><option value="2">Dropdown</option></select> <span id="lblinstallmentError" style="color: red"></span></div></div></div>');
                                                                        }

                                                                        var textbox = 0;
                                                                        var maxField = 50;
                                                                        var x = 1;
                                                                        function addTextboxConfig(tot_priority_count) {
                                                                            $("#text_config_headline").css("display", "block");
                                                                            // $("#scheme_specific_info_field").html("");
                                                                            if (x < maxField) {
                                                                                x++;
                                                                                var addTextboxConfig_array =
                                                                                        textbox++;
                                                                                var objTo = document.getElementById('scheme_specific_info_field')
                                                                                var divtest = document.createElement("div");
                                                                                divtest.setAttribute("class", "form-group col-md-12 removeclass_textbox" + textbox);
                                                                                var rdiv = 'removeclass_textbox' + textbox;
                                                                                divtest.innerHTML = '<div class="input-group"> <div class="form-group col-md-3 required"><input type="hidden" name="textbox_priority' + textbox + '" value="' + tot_priority_count + '" /><label for="installment" class="control-label">Text Field Label :</label><input autocomplete="off" type="text" class="form-control" name="textbox_lebel' + textbox + '" value=""><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-3 required"><label for="installment" class="control-label">Maximum Length :</label><input autocomplete="off" type="text" class="form-control" name="textbox_length' + textbox + '" value=""><span id="lblinstallmentError" style="color: red"></span> </div><div class="form-group col-md-2 required"><label for="periodicity" class="control-label">Required:</label><input type="checkbox" class="form-check" value="1" name="textbox_valid' + textbox + '"><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-1 required"><label for="periodicity" class="control-label">Numeric:</label><input type="radio" class="form-check" value="2" name="textbox_numericvalid' + textbox + '"><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-1 required"><label for="periodicity" class="control-label">Decimal:</label><input type="radio" class="form-check" value="3" name="textbox_numericvalid' + textbox + '"><span id="lblinstallmentError" style="color: red"></span></div><div class="input-group-btn col-md-2"><br><button class="btn btn-danger mr-1" type="button" onclick="remove_TextboxConfig_fields(' + textbox + ');"><span class="minus">-</span> </button></div></div></div>';
                                                                                objTo.appendChild(divtest)
                                                                            }
                                                                        }

                                                                        function remove_TextboxConfig_fields(rid) {
                                                                            //alert(rid);
                                                                            if (rid === 2) {

                                                                                $("#text_config_headline").css("display", "none");
                                                                            }
                                                                            $('.removeclass_textbox' + rid).remove();
                                                                        }

                                                                        var datefield = 0;
                                                                        var maxDateField = 50;
                                                                        var x = 1;
                                                                        function addDateFieldConfig(tot_priority_count) {
                                                                            $("#date_config_headline").css("display", "block");
                                                                            // $("#scheme_specific_info_field").html("");
                                                                            if (x < maxDateField) {
                                                                                x++;
                                                                                var addDateConfig_array =
                                                                                        datefield++;
                                                                                var objTo = document.getElementById('scheme_specific_info_field')
                                                                                var divtest = document.createElement("div");
                                                                                divtest.setAttribute("class", "form-group col-md-12 removeclass_datefield" + datefield);
                                                                                var rdiv = 'removeclass_datefield' + datefield;
                                                                                divtest.innerHTML = '<div class="input-group"> <div class="form-group col-md-3 required"><input type="hidden" name="datefield_priority' + datefield + '" value="' + tot_priority_count + '" /><label for="installment" class="control-label">Date Field Label :</label><input autocomplete="off" type="text" class="form-control" name="datefield_lebel' + datefield + '" value=""><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-2"><label for="periodicity" class="control-label">Required:</label><input type="checkbox" class="form-check" value="1" name="datefield_valid' + datefield + '"><span id="lblinstallmentError" style="color: red"></span></div><div class="input-group-btn col-md-2"><br><button class="btn btn-danger mr-1" type="button" onclick="remove_datefieldConfig_fields(' + datefield + ');"><span class="minus">-</span> </button></div></div></div>';
                                                                                objTo.appendChild(divtest)
                                                                            }
                                                                        }

                                                                        function remove_datefieldConfig_fields(rid) {
                                                                            //alert(rid);
                                                                            if (rid === 2) {

                                                                                $("#date_config_headline").css("display", "none");
                                                                            }
                                                                            $('.removeclass_datefield' + rid).remove();
                                                                        }

                                                                        var dropdown = 0;
                                                                        var maxField = 50;
                                                                        var y = 1;
                                                                        function addDropdownConfig(tot_priority_count) {
                                                                            $("#dropdown_config_headline").css("display", "block");
                                                                            //$("#scheme_specific_info_field").html("");
//                                                                alert(y );
//                                                                if (y < maxField) {
//                                                                    y++;
                                                                            var adddropdown_array =
                                                                                    dropdown++;
                                                                            var objTo = document.getElementById('scheme_specific_info_field')
                                                                            var divtest = document.createElement("div");
                                                                            divtest.setAttribute("class", "form-group col-md-12  removeclass_dropdown" + dropdown);
                                                                            var rdiv = 'removeclass_dropdown' + dropdown;
                                                                            divtest.innerHTML = '<div class="input-group"><input type="hidden" name="dropdown_priority' + dropdown + '" value="' + tot_priority_count + '" /><div class="form-group col-md-4 required"> <label for="installment" class="control-label">Field Label :</label><input autocomplete="off" type="text" class="form-control" name="dropdown_field_lebel' + dropdown + '"   value="" ><span id="lblinstallmentError" style="color: red"></span></div><div class="col-md-8" id="scheme_specific_dropdown_field_option' + dropdown + '"><div class="input-group"><div class="form-group col-md-4 required"><label for="installment" class="control-label">Option Name :</label><input autocomplete="off" type="text" class="form-control" name="option_name' + dropdown + '" value="" ><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-4 required"> <label for="periodicity" class="control-label">Option Value:</label><input autocomplete="off" type="text" class="form-control" name="option_value' + dropdown + '" value="" ><span id="lblinstallmentError" style="color: red"></span> </div><div class="form-group col-md-2 required"><label for="periodicity" class="control-label">Required:</label><input type="checkbox" class="form-check" value="1" name="dropdown_valid' + dropdown + '"><span id="lblinstallmentError" style="color: red"></span></div> <div class="input-group-btn col-md-2"><br/><button class="btn btn-success mr-1" type="button"  onclick="adddropdownConfigoption(' + dropdown + ');"> <span class="plus">+</span> </button><button class="btn btn-danger" type="button" onclick="remove_dropdown_fields(' + dropdown + ');"><span class="minus">-</span> </button></button></div> </div></div></div><div class="clear"></div>';
                                                                            objTo.appendChild(divtest)

                                                                            // }

                                                                        }
                                                                        function remove_dropdown_fields(rid) {
                                                                            if (rid === 2) {

                                                                                $("#dropdown_config_headline").css("display", "none");
                                                                            }
                                                                            $('.removeclass_dropdown' + rid).remove();
                                                                        }
                                                                        var dropdown_option = 1;
                                                                        var maxField = 50;
                                                                        var z = 1;
                                                                        function adddropdownConfigoption(dropdowndivID) {
//                                                                if (z < maxField) {
//                                                                    alert(dropdowndivID);
//                                                                    z++;

                                                                            var adddropdown_option_array =
                                                                                    dropdown_option++;
                                                                            var objTo = document.getElementById('scheme_specific_dropdown_field_option' + dropdowndivID);
                                                                            var divtest = document.createElement("div");
                                                                            divtest.setAttribute("class", "form-group removeclass_option" + dropdown_option);
                                                                            var rdiv = 'removeclass_option' + dropdown_option;
                                                                            divtest.innerHTML = '<div class="input-group"><div class="form-group col-md-4 required"><label for="installment" class="control-label">Option Name :</label><input autocomplete="off" type="text" class="form-control" name="option_name' + dropdowndivID + '" value=""><span id="lblinstallmentError" style="color: red"></span></div><div class="form-group col-md-4 required"> <label for="periodicity" class="control-label">Option Value:</label><input autocomplete="off" type="text" class="form-control" name="option_value' + dropdowndivID + '" value=""><span id="lblinstallmentError" style="color: red"></span> </div> <div class="input-group-btn col-md-4"><br><button class="btn btn-danger" type="button" onclick="remove_dropdown_fields_option(' + dropdown_option + ');"><span class="minus">-</span> </button></div></div><div class="clear"></div>';
                                                                            objTo.appendChild(divtest)

                                                                            //   }

                                                                        }
                                                                        function remove_dropdown_fields_option(rid) {
                                                                            $('.removeclass_option' + rid).remove();
                                                                        }

                                                                        function getRequiredDocumntListpoi(scheme_id) {

                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/AjaxServlet?getdocument_list_html_preview=1&doc_type=1&scheme_id=" + scheme_id,
                                                                                type: "POST",
                                                                                success: function (data) {
                                                                                    $("#documnet_poi").html(data);
                                                                                }
                                                                            });
                                                                        }
                                                                        function getRequiredDocumntListpoa(scheme_id) {
                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/AjaxServlet?getdocument_list_html_preview=1&doc_type=2&scheme_id=" + scheme_id,
                                                                                type: "POST",
                                                                                success: function (data) {
                                                                                    $("#documnet_poa").html(data);
                                                                                }
                                                                            });
                                                                        }
                                                                        function getRequiredDocumntListscheme(scheme_id) {

                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/AjaxServlet?getdocument_list_html_preview=1&doc_type=4&scheme_id=" + scheme_id,
                                                                                type: "POST",
                                                                                success: function (data) {
                                                                                    $("#documnet_scheme").html(data);
                                                                                }
                                                                            });
                                                                        }
                                                                        function checkMethodData(method) {
                                                                            //alert(method);
                                                                            if (method.split("~")[0] === '1') {
                                                                                $("#method1").css("display", "block");
//                                                                                $.ajax({
//                                                                                    url: "${pageContext.request.contextPath}/AjaxServlet?getParamDropdown=1&method=" + method.split("~")[0],
//                                                                                    type: "POST",
//                                                                                    success: function (data) {
//                                                                                        $("#unit").html(data);
//                                                                                    }
//                                                                                });
                                                                                $("#method2").css("display", "none");
                                                                            } else if (method.split("~")[0] === '2') {
                                                                                $("#method1").css("display", "none");
                                                                                $("#method2").css("display", "block");
                                                                                $.ajax({
                                                                                    url: "${pageContext.request.contextPath}/AjaxServlet?getUnitDropdown=1&method=" + method.split("~")[0],
                                                                                    type: "POST",
                                                                                    success: function (data) {
                                                                                        $("#unit").html(data);
                                                                                    }
                                                                                });
                                                                            } else {
                                                                                $("#method2").css("display", "none");
                                                                                $("#method1").css("display", "none");
                                                                            }
                                                                        }
//$('select[name*="cond_param"]').change(function() { resetOptions(); });
//var wrapper = $("#param_field");
//$('.cond_param').on('change', 'select', recalcDisabled);
//
//function recalcDisabled() {
//   const selectedValues = $('.cond_param')
//    .map((_, sel) => sel.value)
//    .get()
//    .filter(Boolean); // Filter out the empty string
//    alert(selectedValues);
//  $('.cond_param option').prop('disabled', false);
//  selectedValues.forEach(value => {
//    $('.cond_param[value!="${value}"] option[value="${value}"]')
//      .prop('disabled', true);
//  });
//}

    </script>
</html>
<%

    } else {
        response.sendRedirect("../login.jsp");
    }
%>



