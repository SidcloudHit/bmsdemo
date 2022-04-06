<%-- 
    Document   : manageRoleBasedOffice
    Created on : Jul 9, 2021, 1:25:36 PM
    Author     : NIC-Arindam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        String edit = "0";
        String regMsg = "";
        String logMsg = "";
        String type = "light";
        // String map_id = "1";

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
        Common.CommonMethod cm = new CommonMethod();

        String ddlDept = "";
        ddlDept = cm.getDepartment("-Please Select-");
        //String ddlParentOffice = "";
        //ddlParentOffice = cm.getURL("-Please Select-");

        /*  manageUserRoleUrl url = new manageUserRoleUrl(map_id);
    if (url.getRole_id() != 0) {
        edit = "1";
    }*/

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Manage Role Based Office</title>
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
                                    <h4 class="text-white">Manage Role Based Office:</h4>
                                </div>
                            </div>
                            <div class="card-body bg-light">

                                <div class="row"> 
                                    <div class="col-md-6">
                                        <form id="frmUserRoleUrl" action="../ManageRoleBasedOffice" method="post" > 
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">User Department</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <input type="hidden" id="hdnOfficeId" name="hdnOfficeId" value="0" />
                                                        <select id="ddlDepartment" name="ddlDepartment" class="form-control" required="required" onchange="dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');">
                                                            <%=ddlDept%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="lblRole">User Role</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                                        </div>
                                                        <select id="ddlRole" name="ddlRole" class="form-control" required="required"  onchange="dependentDropDown1('ddlRole', 'ddlDepartment', 'ddlParentOffice', 'fillParentOffice');">

                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtURL">Office Name</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <input type="text"  id="txtOfficeName" name="txtOfficeName" size="255" class="form-control form-control-sm" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="ddlParentOffice">Parent Office</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                        </div>
                                                        <select id="ddlParentOffice" name="ddlParentOffice" class="form-control" required="required" >

                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-sm-12">
                                                    <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                    <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row d-none" id="officeListDiv">                                        
                                            <div class="input-group mb-3">
                                                <table id="officeList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                    <thead>
                                                        <tr> 
                                                            <th>Sl. No.</th>
                                                            <th>Department</th>                                 
                                                            <th>User Role</th>                                                
                                                            <th>Office Name</th>  
                                                            <th>Parent Office Name</th>                                                            
                                                            <th>Edit</th> 
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
                                                                        url: "${pageContext.request.contextPath}/AjaxServlet?officeList=" + deptId + "&role=" + roleId,
                                                                        type: "POST",
                                                                        dataType: "text",
                                                                        success: function (result) {
                                                                            $("#officeListDiv").removeClass("d-none");
                                                                            $("#officeList tbody").empty();
                                                                            $("#officeList tbody").append(result);
                                                                            //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                                        }
                                                                    });
                                                                });

                                                            });

                                                            function edit_officeList(id) {
                                                                var edit = "0";
                                                                $.ajax({
                                                                    url: "${pageContext.request.contextPath}/AjaxServlet?officeId=" + id,
                                                                    type: "POST",
                                                                    dataType: "text",
                                                                    success: function (result) {
                                                                        var objJSON = JSON.parse(result);
                                                                        if (typeof objJSON === 'object') {
                                                                            edit = "1";
                                                                            //sl_no, dept_id, role_id, office_id, office_name, parent_office_id
                                                                            $('#hdnEdit').val(edit);
                                                                            $('#hdnOfficeId').val(objJSON['office_id']);
                                                                            $('#ddlDepartment').val(objJSON['dept_id']);
                                                                            dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');
                                                                            $('#ddlRole').val(objJSON['role_id']);
                                                                            dependentDropDown1('ddlRole', 'ddlDepartment', 'ddlParentOffice', 'fillParentOffice');
                                                                            $('#txtOfficeName').val(objJSON['office_name']);                                                                            
                                                                            $('#ddlParentOffice').val(objJSON['parent_office_id']);


                                                                            // $('#ddlJurisdiction').val(objJSON['jurisdiction_level']);
                                                                        }
                                                                    }
                                                                });
                                                            }
</script> 
<%

    } else {
        response.sendRedirect("../login.jsp");
    }
%>
