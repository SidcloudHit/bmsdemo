<%-- 
    Document   : payment_configuration
    Created on : 7 Mar, 2022, 5:37:03 PM
    Author     : acer
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
            String department_name = "", scheme_code = "", scheme_name = "", beneficiary_financial_status = "", description = "", beneficiary_category = "",
                    pfms_data = "", scheme_periodicity = "", scheme_frequency = "", amount_fixed = "", amount = "", payment_mode = "", required_doc_poi = "", required_doc_poa = "", required_doc_scheme = "",
                    commodity_name = "", subsidy_amt = "", effective_from = "", commodity_matrix = "", launch_date = "", launch_fy = "", active_upto = "", verification_last_date = "", approval_last_date = "", scheme_type = "", scheme_data_source = "";
            String prefilledid = "", pfms_scheme_code = "", agency_name = "", agency_code = "", bank_name = "", bank_ac_no = "", ifsc_code = "",
                    ac_holder_name = "", debit_tran_limit = "", Specific_info_html = "";
            int dept_id = 0;
            int tot_textbox = 0, tot_dropdown = 0, tot_datefield = 0, tot_priority = 0;
            String[] scheme_features = null;
            String[] commodity = null;
            String stakeholder_dept_list = null, commodity_list = null;
            String[] stakeholder_dept = null;
            int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
            String userName = "", userDes = "";
            String scheme_id = request.getParameter("data");
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
                String ddlCalcType = "";
                ddlCalcType = cm.getCalculationType("-Please Select-");
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
                                        <!--                                        <h3 class="text-center">Payment Configuration</h3>-->

                                        <input type="hidden" name="schemeid_final" id="schemeid_final" />
                                        <fieldset id="payment_config" >

                                            <form id="msform" action="${pageContext.request.contextPath}/saveSchemePaymentConfig" method="post">
                                                <h2 class="fs-title">Payment Configuration</h2>
                                                <input type="hidden" name="scheme_id" id="scheme_id" value="<%=scheme_id%>" />
                                                <div class="form-row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="dept" class="control-label" >Payment Method :</label>
                                                        <select class="custom-select mr-sm-2" required="" name="payment_method" id="payment_method" onchange="checkMethodData(this.value)">
                                                            <%=ddlCalcType%>
                                                        </select>
                                                    </div>

                                                </div>
                                                <div class="form-row" id="method2">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="dept" class="control-label" >Rate :</label>
                                                        <input type="text" name="rate" id="rate" onkeypress="return IsNumeric(event);" class="form-control" />
                                                        <span id="lblError" style="color: red"></span>
                                                    </div>
                                                    <div class="form-group col-md-6 required">
                                                        <label for="dept" class="control-label" >Unit :</label>
                                                        <select class="custom-select mr-sm-2" id="unit" required="" name="unit">
                                                            <option value="" selected>-Please Select-</option>
                                                            <option value="4">Attendence</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <input type="button" id="submit" name="payment_config_submit" class="next action-button" value="Save"/>
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
                                                            $(document).ready(function () {
                                                                $("#method2").css("display", "none");
                                                            });

                                                            function checkMethodData(method) {
                                                                if (method === '2') {
                                                                    $("#method2").css("display", "block");
                                                                } else {
                                                                    $("#method2").css("display", "none");
                                                                }
                                                            }
                                                            $(".next").click(function () {
                                                                var payment_method = $("#payment_method").val();
                                                                var scheme_id = $("#scheme_id").val();
                                                                var rate = $("#rate").val();
                                                                var unit = $("#unit").val();
                                                                $.ajax({
                                                                    url: "${pageContext.request.contextPath}/saveSchemePaymentConfigurationInfo?scheme_id=" + scheme_id + "&method=" + payment_method + "&rate=" + rate + "&unit=" + unit,
                                                                    type: "POST",
                                                                    data: $("#msform").serialize(),
                                                                    success: function (data) {
                                                                        alert("Configured Sccessfully");
                                                                    }
                                                                });
                                                            });
                                                            function IsNumeric(e) {
                                                                var key = window.e ? e.keyCode : e.which;
                                                                if (e.keyCode === 8 || e.keyCode === 46) {
                                                                    return true;
                                                                } else if (key < 48 || key > 57) {
                                                                    lblError.innerHTML = "Only Numbers are Allowed.";
                                                                    //alert("Only Numbers are Allowed");
                                                                    return false;
                                                                } else {
                                                                    lblError.innerHTML = "";
                                                                    return true;
                                                                }
                                                            }

    </script>
</html>
<%
    } else {
        response.sendRedirect("../login.jsp");
    }
%>



