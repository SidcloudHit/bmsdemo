<%-- 
    Document   : manageAdminUsers
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
        officeId = Integer.parseInt(loginObj.getUserOfficeId());

    }

    if (!userId.equals("")) {
        

        Common.CommonMethod cm = new CommonMethod();
        int urlId = cm.getUrlId(request.getRequestURI().replace("/bms/", ""));
        if (cm.isUrlAllowed(deptId, roleId, urlId)) {
            JSONArray jsarr = null, jsarr1 = null;
            JSONObject jsobj = null, jsobj1 = null;
            jsarr = cm.getUserOfficeDetails(userId, deptId);

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

            // String ddlRole = "";
            // ddlRole = cm.getDepartment("-Please Select-");
            /* AdminUser adminUser = new AdminUser(user_id);
    if (adminUser.getUser_id() != null) {
        edit = "1";
    }*/

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
        <!--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">-->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>


    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
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
                                <div class="card-header bg-info">
                                    <h4 class="text-white">Admin User Details:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <div class="row"> 
                                        <div class="col-md-6">
                                            <form id="frmAdminUser" action="../ManageAdminUser" method="post" enctype="multipart/form-data"> 

                                                <div class="row">
                                                    <div class="form-group col-md-6">
                                                        <label for="txtDepartment">User Department</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-home"></i></span>
                                                            </div>
                                                            <select id="ddlDepartment" name="ddlDepartment" class="form-control form-control-sm" required="required" onchange="dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');">
                                                                <%=ddlDept%>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <div class="form-group"> 
                                                            <label for="lblRole">User Role</label>
                                                            <div class="input-group mb-3">
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                                                </div>
                                                                <select id="ddlRole" name="ddlRole" class="form-control form-control-sm" required="required"  onchange="dependentDropDown('ddlRole', 'ddlOffice', 'fillOffice');">

                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="form-group col-md-6">
                                                        <div class="form-group"> 
                                                            <label for="lblOffice">User Office</label>
                                                            <div class="input-group mb-3">
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                                                </div>
                                                                <select id="ddlOffice" name="ddlOffice" class="form-control form-control-sm" required="required" >

                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <div class="form-group"> 
                                                            <label for="lblOffice">Login ID</label>
                                                            <div class="input-group mb-3">
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                                                </div>
                                                                <input type="text" pattern="^[A-Za-z0-9][A-Za-z0-9@#$]{6,19}$" id="txtUserLoginId" name="txtUserLoginId" onchange = "return loginIdCheck();"  class="form-control form-control-sm" placeholder="User Login ID" required="required" title="User Login ID must be Alphanumeric which starts with an alphabet, must be minimum 8 to maximum 20 characters long and may contain special characters @ # $" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-md-6">
                                                        <label for="txtUserName">User Name</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <input type="hidden" id="hdnUserId" name="hdnUserId" value="" />
                                                            <input type="text" pattern="^[A-Za-z][A-Za-z0-9\s]*" id="txtUserName" name="txtUserName" size="120" class="form-control form-control-sm" placeholder="Type Full Name as given in Aadhaar" required="required"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label for="txtEmail">Email ID</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-envelope"></i></span>
                                                            </div>
                                                            <input type="email" id="txtEmail" name="txtEmail" class="form-control form-control-sm" placeholder="" onchange = "return emailCheck();" style="text-transform:lowercase"/>
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
                                                            <input type="tel" id="txtMobile" name="txtMobile" pattern="[4-9]{1}[0-9]{9}" class="form-control form-control-sm" placeholder="Type Your 10 Digit Mobile Number" onchange = "return mobileCheck();" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6">
                                                        <label for="txtDesignation">User Designation</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-briefcase fa-lg"></i></span>
                                                            </div>
                                                            <input type="text" id="txtDesignation" name="txtDesignation"  class="form-control form-control-sm" placeholder="User Designation" />
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6" >
                                                        <img id="preview_photo" src="" alt="" height="100px" width="100px" style="border: 1px solid black;" class=" d-none mx-auto"/>
                                                    </div>
                                                    <div class="col-md-6 text-right">
                                                        <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                        <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                    </div>
                                                </div>

                                                <div class="row invisible">
                                                    <div class="form-group col-md-6">
                                                        <label for="radMale">Gender</label>
                                                        <div class="input-group mb-3">
                                                            <div class="radio icheck-material-blue">
                                                                <input type="radio" id="radMale" name="radGender" value="Male"  >
                                                                <label for="radMale"><i class="fa fa-male">&nbsp;</i> Male </label>
                                                            </div>
                                                            <div class="radio icheck-material-blue  ml-3">
                                                                <input type="radio" id="radFemale" name="radGender" value="Female"  >
                                                                <label for="radFemale"><i class="fa fa-female">&nbsp;</i> Female </label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-md-6 ">
                                                        <label for="txtAltMobile">Alternate Mobile Number</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-mobile"></i></span>
                                                            </div>
                                                            <input type="tel" id="txtAltMobile" name="txtAltMobile" pattern="[4-9]{1}[0-9]{9}" class="form-control form-control-sm" placeholder="Type Your Alternate Mobile Number" />
                                                        </div>
                                                    </div>
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

                                                <div class="row invisible">
                                                    <div class="input-group mb-3 col-md-6">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-road"></i></span>
                                                        </div>
                                                        <input type="text" id="txtAddrLine" size="200" name="txtAddrLine" class="form-control form-control-sm" placeholder="House No., Road, Landmark etc.."/>
                                                    </div>
                                                    <div class="input-group mb-3 col-md-6">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <input type="text" id="txtAddrCity" size="200" name="txtAddrCity" class="form-control form-control-sm" placeholder="City, Town, Village etc.."/>
                                                    </div>                            
                                                </div>
                                                <div class="row invisible">
                                                    <div class="input-group mb-3 col-md-4">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-building"></i></span>
                                                        </div>
                                                        <input type="text" id="txtAddrPO" size="90" name="txtAddrPO" class="form-control form-control-sm" placeholder="Post Office"/>
                                                    </div>
                                                    <div class="input-group mb-3 col-md-4">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlDistrict" name="ddlDistrict" class="form-control form-control-sm" >
                                                            <%=ddlDist%>
                                                        </select>
                                                    </div>
                                                    <div class="input-group mb-3 col-md-4">
                                                        <div class="input-group-prepen    d">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-map-pin"></i></span>                    
                                                        </div>
                                                        <input type="text" pattern="[1-9]{1}[0-9]{5}" id="txtAddrPIN" size="6" name="txtAddrPIN" class="form-control form-control-sm" placeholder="PIN Code"/>
                                                    </div>
                                                </div>

                                                <div class="row invisible">  
                                                    <div  class="form-group col-md-6" >
                                                        <label for="filePhoto">Upload Photo<span class="text-danger-small"><strong>( Max allowed size : 250Kb )</strong></span></label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-photo"></i></span>
                                                            </div>
                                                            <input type="file" id="filePhoto" name="filePhoto" class="form-control form-control-sm" accept="image/jpg,image/jpeg,image/png"  placeholder="Upload Your Recent Passport size Photo"  onchange="return ValidateSize(this, 250, 'photo');" />                                                             
                                                        </div>
                                                    </div>
                                                    <div  class="form-group col-md-6" >
                                                        <label for="filePhoto">Ration Car Member ID</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"></span>
                                                            </div>
                                                            <input type="text" id="txtRcMem" size="90" name="txtRcMem" class="form-control form-control-sm" placeholder="Ration Car Member ID"/>
                                                        </div>
                                                    </div>
                                                </div>


                                            </form>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="row d-none" id="userListDiv">                                        
                                                <div class="input-group mb-3">
                                                    <table id="userList" class="table table-striped table-bordered small table-responsive" style="width:100%">
                                                        <thead>
                                                            <tr> 
                                                                <th>Sl. No.</th>
                                                                <th>User Name</th> 
                                                                <th>Login ID</th>
                                                                <th>Email ID</th>                                                
                                                                <th>Mobile Number</th> 
                                                                <th>User Department</th>                                                            
                                                                <th>User Designation</th>
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
</html>
<script>
                                                                $(document).ready(function () {

                                                                    $("#ddlRole").on('change', function (e) {
                                                                        var deptId = $('#ddlDepartment option:selected').val();
                                                                        var roleId = $('#ddlRole option:selected').val();


                                                                        $.ajax({
                                                                            url: "${pageContext.request.contextPath}/AjaxServlet?userList=" + deptId + "&role=" + roleId,
                                                                            type: "POST",
                                                                            dataType: "text",
                                                                            success: function (result) {
                                                                                $("#userListDiv").removeClass("d-none");
                                                                                $("#userList tbody").empty();
                                                                                $("#userList tbody").append(result);
                                                                                //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                                            }
                                                                        });

                                                                    });
                                                                });

                                                                function edit_userList(id) {
                                                                    var edit = "0";

                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/AjaxServlet?userId=" + id,
                                                                        type: "POST",
                                                                        dataType: "text",
                                                                        success: function (result) {
                                                                            var objJSON = JSON.parse(result);
                                                                            if (typeof objJSON === 'object') {
                                                                                edit = "1";
                                                                                $('#hdnEdit').val(edit);
                                                                                $('#hdnUserId').val(objJSON['user_id']);
                                                                                $('#txtUserName').val(objJSON['user_name']);
                                                                                $('#txtEmail').val(objJSON['user_email']);

                                                                                $('#txtMobile').val(objJSON['user_mobile']);
                                                                                //$('#radGender').val(objJSON['user_gender']);
                                                                                //$('[name="radGender"]').val([objJSON['user_gender']]);
                                                                                
                                                                                $('#txtDesignation').val(objJSON['user_designation']);
                                                                                $('#txtUserLoginId').val(objJSON['user_login_id']);
                                                                                $('#txtUserLoginId').attr("readonly", "readonly");
                                                                                //$('#txtAltMobile').val(objJSON['user_alt_mobile']);
                                                                                $('#ddlDepartment').val(objJSON['user_department_id']);
                                                                                dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');
                                                                                $('#ddlRole').val(objJSON['user_role']);
                                                                                dependentDropDown('ddlRole', 'ddlOffice', 'fillOffice');
                                                                                $('#ddlOffice').val(objJSON['user_office']);
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