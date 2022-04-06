<%-- 
    Document   : dept_onboarding
    Created on : May 10, 2020, 8:42:10 PM
    Author     : NIC-Arindam
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
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
    Common.CommonMethod cm = new CommonMethod();

    String edit = "0";
    String regMsg = "";
    String logMsg = "";
    String type = "light";
    String user_id = "test1234";

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

    String ddlDist = "", onboarded_dept_id_list = "''";
    //ddlDist = cm.getDistrict("-Please Select-");
    onboarded_dept_id_list = cm.getonboarded_dept_id_list();
    String ddlDept = "";
    ddlDept = cm.getDepartmentListForOnboarding(onboarded_dept_id_list, "-Please Select-");


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Department On-boarding</title>
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
        <style>
            .form-group.required .control-label:after {
                content: "*";
                color: red;
            }
        </style>
    </head>


    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>

                    <div class="col-md-12">

                        <div class="row ">
                            <div class="col-sm-12">

                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white">Department On-boarding:</h4>
                                    </div>
                                    <div class="card-body bg-light">
                                        <div class="row"> 
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="txtUserName" class="control-label">Department</label>
                                                        <select class="custom-select" name="dept_id" id="dept_id" required onchange="chechDataAvailability(this.value)">
                                                        <%=ddlDept%>
                                                    </select>

                                                </div>
                                                <div class="form-group col-md-3">
                                                    <label for="txtUserName" class="control-label">&nbsp;</label>
                                                    <a href="DeptOnboardedList.jsp" target="_blank" class="btn btn-primary form-control">View List of On-boarded Departments</a></div>
                                            </div>
                                            <form id="frmManageDeptWiseUser" action="../saveDeptOnboardingData" method="post" enctype="multipart/form-data" onSubmit = "return checkSameValueValidation(this)"> 
                                                <input type="hidden" id="hdnEdit" name="hdnEdit" />
                                                <input type="hidden" id="dept" name="dept" />

                                                <div class="card">
                                                    <div class="card-header">First Nodal Officer Details</div>
                                                    <div class="card-body">
                                                        <div class="row">
                                                            <div class="form-group col-md-6 required">
                                                                <label for="txtUserName" class="control-label">Employee Name</label>
                                                                <input type="hidden" id="hdnUserId" name="hdnUserId" value="" />
                                                                <input type="text" pattern="^[A-Za-z][A-Za-z0-9\s]*" id="txtEmpName1" name="txtEmpName1" size="120" class="form-control form-control-sm" placeholder="Enter Full Name"  required="required"/>
                                                            </div>
                                                            <div class="form-group col-md-6 required">
                                                                <label for="txtUserName" class="control-label">Employee Code</label>
                                                                <input type="text" id="txtEmpCode1" name="txtEmpCode1" size="120" class="form-control form-control-sm" placeholder="Enter Employee Code"  required="required"/>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="form-group col-md-6 required">
                                                                <label for="txtUserName" class="control-label">Employee Designation</label>
                                                                <!--<input type="hidden" id="hdnUserId" name="hdnUserId" value="" />-->
                                                                <input type="text" id="txtEmpDesignation1" name="txtEmpDesignation1" size="120" class="form-control form-control-sm" placeholder="Enter Employee Designation"  required="required"/>
                                                            </div>
                                                            <div class="form-group col-md-6 required">
                                                                <label for="txtEmail" class="control-label">Email ID</label>
                                                                <input type="email" id="txtEmail1" name="txtEmail1" class="form-control form-control-sm" placeholder="Enter Email address"  required="required"/>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="form-group col-md-6 required">

                                                                <label for="txtMobile" class="control-label">Mobile Number</label>
                                                                <input type="tel" id="txtMobile1" name="txtMobile1" pattern="[4-9]{1}[0-9]{9}" maxlength="10" class="form-control form-control-sm" placeholder="Enter 10 Digit Mobile Number"  required="required"/>
                                                            </div>
                                                            <div class="form-group col-md-6 required">
                                                                <label for="txtDesignation" class="control-label">Date of Birth</label>
                                                                <input type="date" id="txtDateOfBirth1" name="txtDateOfBirth1" class="form-control form-control-sm" placeholder="Select Date of Birth"  required="required"/>
                                                            </div>

                                                        </div>
                                                        <div class="row">
                                                            <div  class="form-group col-md-6" >
                                                                <label for="filePhoto">HRMS Code</label><br><span><input type="checkbox" name="sameempcode1" id="sameempcode1" />&nbsp;Same as Employee Code</span>
                                                                <div class="input-group mb-3">
                                                                    <input type="text" id="txtHrmsCode1" size="90" name="txtHrmsCode1" class="form-control form-control-sm" placeholder="Enter HRMS Code"/>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="card mt-2">
                                                    <div class="card-header">Second Nodal Officer Details</div>
                                                    <div class="card-body">
                                                        <div class="row">
                                                            <div class="form-group col-md-6">
                                                                <label for="txtUserName" class="control-label">Employee Name</label>
                                                                <input type="hidden" id="hdnUserId" name="hdnUserId" value="" />
                                                                <input type="text" pattern="^[A-Za-z][A-Za-z0-9\s]*" id="txtEmpName2" name="txtEmpName2" size="120" class="form-control form-control-sm" placeholder="Enter Full Name" />
                                                            </div>
                                                            <div class="form-group col-md-6">
                                                                <label for="txtUserName" class="control-label">Employee Code</label>
                                                                <input type="text" id="txtEmpCode2" name="txtEmpCode2" size="120" class="form-control form-control-sm" placeholder="Enter Employee Code" />
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="form-group col-md-6">
                                                                <label for="txtUserName" class="control-label">Employee Designation</label>
                                                                <!--<input type="hidden" id="hdnUserId" name="hdnUserId" value="" />-->
                                                                <input type="text" id="txtEmpDesignation2" name="txtEmpDesignation2" size="120" class="form-control form-control-sm" placeholder="Enter Employee Designation" />
                                                            </div>
                                                            <div class="form-group col-md-6">
                                                                <label for="txtEmail" class="control-label">Email ID</label>
                                                                <input type="email" id="txtEmail2" name="txtEmail2" class="form-control form-control-sm" placeholder="Enter Email address" />
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="form-group col-md-6">

                                                                <label for="txtMobile" class="control-label">Mobile Number</label>
                                                                <input type="tel" id="txtMobile2" name="txtMobile2" pattern="[4-9]{1}[0-9]{9}" maxlength="10" class="form-control form-control-sm" placeholder="Enter 10 Digit Mobile Number" />
                                                            </div>
                                                            <div class="form-group col-md-6">
                                                                <label for="txtDesignation" class="control-label">Date of Birth</label>
                                                                <input type="date" id="txtDateOfBirth2" name="txtDateOfBirth2" class="form-control form-control-sm" placeholder="Select Date of Birth" />
                                                            </div>

                                                        </div>
                                                        <div class="row">
                                                            <div  class="form-group col-md-6" >
                                                                <label for="filePhoto" class="mr-2">HRMS Code</label><br><span><input type="checkbox" name="sameempcode2" id="sameempcode2" />&nbsp;Same as Employee Code</span>
                                                                <input type="text" id="txtHrmsCode2" size="90" name="txtHrmsCode2" class="form-control form-control-sm" placeholder="Enter HRMS Code" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row mt-2">
                                                    <div class="form-group col-md-6 required">
                                                        <label for="txtLoginCaptcha"  class="control-label">Verification Code</label>
                                                        <div><img src="../CaptchaGen" border="0" /></div>
                                                        <div class="input-group mb-3 mt-1">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-key fa-lg"></i></span>
                                                            </div>
                                                            <input type="text" id="txtLoginCaptcha" name="txtLoginCaptcha" class="form-control form-control-sm" placeholder="Enter Verification Code Here" autocomplete="off" required="required"/>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="row mt-2">

                                                    <!--<div class="col-md-6" >
                                                        <img id="preview_photo" src="" alt="" height="100px" width="100px" style="border: 1px solid black;" class=" d-none mx-auto"/>
                                                    </div>-->
                                                    <div class="col-md-6 text-right">
                                                        <button type="button" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                    </div>
                                                </div>
                                                <div class="row invisible">


                                                </div>
                                            </form>
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

    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js'></script>
    <script src='${pageContext.request.contextPath}/JS/jquery.validate.js'></script>
</html>
<script>
                                                    function chechDataAvailability(dept_id) {
                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/AjaxServlet?isDeptAvl=1&dept_id=" + dept_id,
                                                            type: "POST",
                                                            // data: ,
                                                            success: function (data) {

                                                                if (data === '0') {
                                                                    edit_deptNodalOfficerDetails(dept_id);
                                                                    $("#dept").val(dept_id);
                                                                } else {

                                                                    $("#frmManageDeptWiseUser")[0].reset();
                                                                    $("#dept").val(dept_id);
                                                                }
                                                            }
                                                        });
                                                    }

                                                    $("#btnSubmit").click(function () {

                                                        if ($("#frmManageDeptWiseUser").valid()) {

                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/saveDeptOnboardingData",
                                                                type: "POST",
                                                                data: $("#frmManageDeptWiseUser").serialize(),
                                                                success: function (data) {

                                                                    if (data == 1) {
                                                                        alert("Data Submitted Successfully");
                                                                        window.location.href = "${pageContext.request.contextPath}/department/acknowledgement_slip.jsp?data=" + $("#dept_id").val();
                                                                        $("#frmManageDeptWiseUser")[0].reset();
                                                                        $("#dept_id").val("");
                                                                    } else if (data == 2)
                                                                    {
                                                                        alert("Wrong Verification Code");
                                                                    } else {
                                                                        alert("Error in Saving");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                    $("#sameempcode1").click(function () {
                                                        if ($(this).prop("checked") == true) {
                                                            $("#txtHrmsCode1").val($("#txtEmpCode1").val());
                                                        } else if ($(this).prop("checked") == false) {
                                                            $("#txtHrmsCode1").val("")
                                                        }
                                                    });
                                                    $("#sameempcode2").click(function () {
                                                        if ($(this).prop("checked") == true) {
                                                            $("#txtHrmsCode2").val($("#txtEmpCode2").val());
                                                        } else if ($(this).prop("checked") == false) {
                                                            $("#txtHrmsCode2").val("")
                                                        }
                                                    });
                                                    jQuery.validator.addMethod("notEqual", function (value, element, param) {
                                                        return this.optional(element) || value !== $(param).val();
                                                    }, "Please specify a different value");
                                                    $('#frmManageDeptWiseUser').validate({
                                                        rules: {

                                                            txtEmail2: {notEqual: "#txtEmail1"},
                                                            txtMobile2: {notEqual: "#txtMobile1"},
                                                            txtEmpCode2: {notEqual: "#txtEmpCode1"},
                                                            txtHrmsCode2: {notEqual: "#txtHrmsCode1"}
                                                        }
                                                    });
                                                    $(document).ready(function () {

                                                        // $("#ddlRole").on('change', function (e) {
                                                        // var deptId = $('#ddlDepartment option:selected').val();
                                                        // var roleId = $('#ddlRole option:selected').val();


                                                        $.ajax({
                                                            // url: "${pageContext.request.contextPath}/AjaxServlet?userList=" + deptId + "&role=" + roleId,
                                                            url: "${pageContext.request.contextPath}/AjaxServlet?userList=1",
                                                            type: "POST",
                                                            dataType: "text",
                                                            success: function (result) {
                                                                $("#userListDiv").removeClass("d-none");
                                                                $("#userList tbody").empty();
                                                                $("#userList tbody").append(result);
                                                                //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                            }
                                                        });
                                                        // });
                                                    });
                                                    function edit_deptNodalOfficerDetails(id) {
                                                        var edit = "0";
                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/AjaxServlet?&nodel_officer_edit=1&dept_id=" + id,
                                                            type: "POST",
                                                            dataType: "text",
                                                            success: function (result) {
                                                                var objJSON = JSON.parse(result);
                                                                if (typeof objJSON === 'object') {
                                                                    edit = "1";
                                                                    $('#hdnEdit').val(edit);
                                                                    $('#txtEmpName1').val(objJSON['nodal1_name']);
                                                                    $('#txtEmpCode1').val(objJSON['nodal1_code']);
                                                                    $('#txtEmpDesignation1').val(objJSON['nodal1_designation']);
                                                                    $('#txtEmail1').val(objJSON['nodal1_email']);
                                                                    $('#txtMobile1').val(objJSON['nodal1_mobile']);
                                                                    $('#txtDateOfBirth1').val(objJSON['nodal1_dob']);
                                                                    $('#txtHrmsCode1').val(objJSON['nodal1_hrms_code']);

                                                                    $('#txtEmpName2').val(objJSON['nodal2_name']);
                                                                    $('#txtEmpCode2').val(objJSON['nodal2_code']);
                                                                    $('#txtEmpDesignation2').val(objJSON['nodal2_designation']);
                                                                    $('#txtEmail2').val(objJSON['nodal2_email']);
                                                                    $('#txtMobile2').val(objJSON['nodal2_mobile']);
                                                                    $('#txtDateOfBirth2').val(objJSON['nodal2_dob']);
                                                                    $('#txtHrmsCode2').val(objJSON['nodal2_hrms_code']);
                                                                }


                                                            }
                                                        });
                                                    }
</script> 