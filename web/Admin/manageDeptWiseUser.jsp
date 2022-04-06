<%-- 
    Document   : manageDeptWiseUser
    Created on : May 10, 2020, 8:42:10 PM
    Author     : NIC-Arindam
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page import="Master.Data.AdminUser"%>
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
    String message = "";
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
    String userId = "", userName = "", userDes = "";

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        //officeId = Integer.parseInt(loginObj.getUserOfficeId());

    }

    if (!userId.equals("")) {

        Common.CommonMethod cm = new CommonMethod();
        int urlId = cm.getUrlId(request.getRequestURI().replace("/bms/", ""));
        // int urlId = 0;
        if (cm.isUrlAllowed(deptId, roleId, urlId)) {
            JSONArray jsarr = null, jsarr1 = null;
            JSONObject jsobj = null, jsobj1 = null;
            //jsarr = cm.getUserOfficeDetails(userId, deptId);

            String edit = "0";
            String regMsg = "";
            String logMsg = "";
            String type = "light";
            //String user_id = "test1234";

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

            String ddlDist = "";
            ddlDist = cm.getDistrict("-Please Select-");
            String ddlDept = "";
            //ddlDept = cm.getDepartment("-Please Select-");

            int distId = 0;
            if (jsarr != null) {
                for (int n = 0; n < jsarr.length(); n++) {
                    jsobj = jsarr.getJSONObject(n);
                    distId = jsobj.getInt("dist_code");
                    officeId = jsobj.getInt("user_office");
                    //ddlDist = cm.getDistrict(distId, "-Please Select-");
                    if (roleId == 1) {
                        ddlDept = cm.getDepartment("-Please Select-");
                    } else {
                        ddlDept = cm.getDeprtment(deptId, "-Please Select-");
                    }
                }
            }

            /*String ddlRole = "";
            ddlRole = cm.getDepartment("-Please Select-");
            AdminUser adminUser = new AdminUser(user_id);
            if (adminUser.getUser_id() != null) {
                edit = "1";
            }*/
            String departmentId = request.getParameter("deptId");
            String groupId = request.getParameter("groupId");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Manage Admin Users</title>
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
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>


    <body>
        <div class="container-fluid bg-light">          
            <div class="row">
                <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">

                        <div class="row ">
                            <div class="col-sm-12">
                            <% if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>
                            <div class="card">
                                <!--<div class="card-header bg-info">
                                    <h4 class="text-white">Department wise User Details:</h4>
                                </div>-->
                                <div class="card-body bg-light">
                                    <div class="row"> 
                                        <div class="col">
                                            <form id="frmManageDeptWiseUser" action="../ManageDeptWiseUser" method="post" enctype="multipart/form-data"> 

                                                <div class="row">
                                                    <div class="form-group col-md-6">
                                                        <label for="txtUserName">Employee Name</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <input type="hidden" id="hdnUserGroupId" name="hdnUserGroupId" value="<%=groupId%>" />
                                                            <input type="hidden" id="hdnUserId" name="hdnUserId" value="" />
                                                            <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="<%=departmentId%>" />
                                                            <input type="text" pattern="[^\s][A-Za-z]*[A-Za-z\s\.]*" id="txtEmpName" name="txtEmpName" size="120" class="form-control form-control-sm" placeholder="Type Full Name as given in Aadhaar" required="required"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label for="txtUserName">Employee Code</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <input type="text" id="txtEmpCode" name="txtEmpCode" size="120" class="form-control form-control-sm" placeholder="Type Employee Code" required="required"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-md-6">
                                                        <label for="txtUserName">Employee Designation</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <!--<input type="hidden" id="hdnUserId" name="hdnUserId" value="" />-->
                                                            <input type="text" id="txtEmpDesignation" name="txtEmpDesignation" size="120" class="form-control form-control-sm" placeholder="Type Employee Designation" required="required"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label for="txtEmail">Email ID</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-envelope"></i></span>
                                                            </div>
                                                            <input type="email" id="txtEmail" name="txtEmail" class="form-control form-control-sm" placeholder="Type your Email address" onchange = "return adminEmailCheck();" style="text-transform:lowercase" required="required"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-md-6">

                                                        <label for="txtMobile">Mobile Number</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-mobile"></i></span>
                                                            </div>
                                                            <input type="tel" id="txtMobile" name="txtMobile" pattern="[4-9]{1}[0-9]{9}" class="form-control form-control-sm" placeholder="Type Your 10 Digit Mobile Number" onchange = "return adminMobileCheck();" required="required"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label for="txtDesignation">User Date of Birth</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-calendar fa-lg"></i></span>
                                                            </div>
                                                            <input type="date" id="txtDateOfBirth" name="txtDateOfBirth" class="form-control form-control-sm" placeholder="Select Your Date of Birth" required="required"/>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div  class="form-group col-md-6" >
                                                        <label for="filePhoto">HRMS Code</label><br><span><input type="checkbox" name="sameempcode1" id="sameempcode1" />&nbsp;Same as Employee Code</span>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"></span>
                                                            </div>
                                                            <input type="text" id="txtHrmsCode" size="90" name="txtHrmsCode" class="form-control form-control-sm" placeholder="HRMS Code" required="required"/>
                                                        </div>
                                                    </div>

                                                    <div  class="form-group col-md-6" >
                                                        <!--<label for="filePhoto">Ration Card Member ID</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"></span>
                                                            </div>
                                                            <input type="text" id="txtRcMem" size="90" name="txtRcMem" class="form-control form-control-sm" placeholder="Ration Card Member ID"/>
                                                        </div>-->
                                                        <label for="txtLoginId">Login ID</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-key fa-lg"></i></span>
                                                            </div>
                                                            <input type="text"  id="txtLoginId" name="txtLoginId" size="255" class="form-control form-control-sm" />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <!--<div class="col-md-6" >
                                                        <img id="preview_photo" src="" alt="" height="100px" width="100px" style="border: 1px solid black;" class=" d-none mx-auto"/>
                                                    </div>-->
                                                    <div class="col-md-6 text-right">
                                                        <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                        <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="<%= departmentId%>" />
                                                        <input type="hidden" id="hdnPrevEdit" name="hdnPrevEdit" value="0" />
                                                        <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                    </div>
                                                </div>
                                                <div class="row invisible">

                                                    <!--  <div class="form-group col-md-2">
                                                          <label for="txtPwd">Password</label>
                                                          <div class="input-group mb-3">
                                                              <div class="input-group-prepend">
                                                                  <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock fa-lg"></i></span>
                                                              </div>
                                                              <input type="password" id="txtUserPwd" name="txtUserPwd" class="form-control form-control-sm" placeholder="Type Your Password"  required="required"/>
                                                          </div>
                                                      </div>-->

                                                </div>
                                            </form>
                                        </div>

                                    </div>

                                    <div class="row">
                                        <div class="col">
                                            <div class="row d-none" id="userListDiv">                                        
                                                <div class="input-group mb-3">
                                                    <table id="userList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                        <thead>
                                                            <tr> 

                                                                <th>Name</th>
                                                                <th>Designation</th>
                                                                <th>Employee Code</th>
                                                                <th>Email ID</th>                                                
                                                                <th>Mobile Number</th> 
                                                                <!-- <th>Date of Birth</th>   --> 
                                                                <th>HRMS Code</th>
                                                                <th>Login ID</th>

                                                            </tr>
                                                        </thead> 
                                                        <tbody></tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>        
                            </div>    
                        </div>
                    </div>

                </div>
            </div>

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
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/datatables.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/jquery.dataTables.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.13/js/bootstrap-multiselect.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.13/css/bootstrap-multiselect.css">
</html>
<script>
                                                                $("#sameempcode1").click(function () {
                                                                    if ($(this).prop("checked") == true) {
                                                                        $("#txtHrmsCode").val($("#txtEmpCode").val());
                                                                    } else if ($(this).prop("checked") == false) {
                                                                        $("#txtHrmsCode").val("")
                                                                    }
                                                                });
                                                                $(document).ready(function () {

                                                                    // $("#ddlRole").on('change', function (e) {
                                                                    // var deptId = $('#ddlDepartment option:selected').val();
                                                                    // var roleId = $('#ddlRole option:selected').val();




                                                                    //   $.ajax({
                                                                    // url: "${pageContext.request.contextPath}/AjaxServlet?userList=" + deptId + "&role=" + roleId,
                                                                    //  url: "${pageContext.request.contextPath}/AjaxServlet?userList=1",
                                                                    //  type: "POST",
                                                                    //  dataType: "text",
                                                                    //   success: function (result) {
                                                                    //     $("#userListDiv").removeClass("d-none");
                                                                    //    $("#userList tbody").empty();
                                                                    //   $("#userList tbody").append(result);
                                                                    //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                                    //  }
                                                                    //  });


                                                                    var table;
                                                                    $('#userListDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#userList').DataTable().clear().destroy();
                                                                    var deptId = $('#hdnDeptId').val();

                                                                    table = $('#userList').DataTable({
                                                                        "ajax": {
                                                                            url: "${pageContext.request.contextPath}/AjaxServlet?userList=1&deptId=" + deptId,
                                                                            "type": "POST"
                                                                        },
                                                                        "iDisplayLength": 10,
                                                                        'order': [[1, 'asc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": "emp_name"},
                                                                            {"data": "emp_designation"},
                                                                            {"data": "emp_code"},
                                                                            {"data": "emp_email"},
                                                                            {"data": "emp_mobile"},
                                                                            // {"data": "emp_dob"},
                                                                            {"data": "hrms_code"},
                                                                            {"data": "login_id"}
                                                                        ],

                                                                    });

                                                                    $('#userList_wrapper').addClass("col-md-12");
                                                                    $("select[name='userList_length']").removeClass("custom-select custom-select-sm ");







                                                                    // });
                                                                });

                                                                function edit_userList(id) {
                                                                    var edit = "0";

                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/AjaxServlet?editUser=1&userId=" + id,
                                                                        type: "POST",
                                                                        dataType: "text",
                                                                        success: function (result) {
                                                                            var objJSON = JSON.parse(result);
                                                                            if (typeof objJSON === 'object') {
                                                                                edit = "1";
                                                                                $('#hdnEdit').val(edit);
                                                                                $('#hdnUserId').val(objJSON['user_id']);
                                                                                $('#txtEmpName').val(objJSON['emp_name']);
                                                                                $('#txtEmpCode').val(objJSON['emp_code']);
                                                                                $('#txtEmpDesignation').val(objJSON['emp_designation']);
                                                                                $('#txtEmail').val(objJSON['emp_email']);

                                                                                $('#txtMobile').val(objJSON['emp_mobile']);
                                                                                $('#txtDateOfBirth').val(objJSON['emp_dob']);
                                                                                $('#txtHrmsCode').val(objJSON['hrms_code']);
                                                                                $('#txtLoginId').val(objJSON['login_id']);
                                                                                //$('#radGender').val(objJSON['user_gender']);
                                                                                //$('[name="radGender"]').val([objJSON['user_gender']]);

                                                                                //$('#txtDesignation').val(objJSON['user_designation']);
                                                                                //$('#txtUserLoginId').val(objJSON['user_login_id']);
                                                                                //$('#txtUserLoginId').attr("readonly", "readonly");
                                                                                //$('#txtAltMobile').val(objJSON['user_alt_mobile']);
                                                                                // $('#ddlDepartment').val(objJSON['user_department_id']);
                                                                                // dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');
                                                                                // $('#ddlRole').val(objJSON['user_role']);
                                                                                // dependentDropDown('ddlRole', 'ddlOffice', 'fillOffice');
                                                                                // $('#ddlOffice').val(objJSON['user_office']);
                                                                                //$('#txtAddrLine').val(objJSON['address1_road_landmark']);
                                                                                //$('#txtAddrCity').val(objJSON['address1_city_town_village']);
                                                                                //$('#txtAddrLine').val(objJSON['address1_road_landmark']);
                                                                                //$('#txtAddrPO').val(objJSON['address1_po']);
                                                                                //$('#ddlDistrict').val(objJSON['address1_district']);
                                                                                //$('#txtAddrPIN').val(objJSON['address1_pin']);
                                                                                //$('#txtRcMem').val(objJSON['user_rc_member_id']);

                                                                                //$('#preview_photo').attr("src", "../ViewFile?fId=" + objJSON['user_photo_fid']);
                                                                                //$('#preview_photo').removeClass("d-none");

                                                                                //../ViewFile?fId=


                                                                                //var gender = objJSON['user_gender'];
                                                                                //if (gender === 'Male') {
                                                                                //    $('#radMale').prop("checked", true);
                                                                                //} else if (gender === 'Female') {
                                                                                //    $('#radFemale').prop("checked", true);
                                                                                //}
                                                                                // $('#ddlJurisdiction').val(objJSON['jurisdiction_level']);
                                                                            }


                                                                        }
                                                                    });
                                                                }
</script> 
<%
        } else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>