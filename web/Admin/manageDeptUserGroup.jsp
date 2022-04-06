<%-- 
    Document   : manageDeptUserGroup
    Created on : 17 Sep, 2021, 6:10:25 PM
    Author     : acer
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
    String userId = "", userName = "", userDes = "", scheme_name = "", dept_name = "";

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        schemeId = Integer.parseInt(loginObj.getUserScheme());
        scheme_name = loginObj.getUserSchemeName();
        dept_name = loginObj.getUserDeptName();
        //officeId = Integer.parseInt(loginObj.getUserOfficeId());

    }

    if (!userId.equals("") && schemeId != 0) {
        String regMsg = "";
        String logMsg = "";
        String type = "light";
        String edit = "0";

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

        String ddlRole = "", role_id_list = "", dept_abbr = "";
        int onboarding_scheme_id = 0;
        onboarding_scheme_id = cm.getOnboardedSchemeId(schemeId);
        role_id_list = cm.getdept_roleid_list(onboarding_scheme_id, deptId);
        ddlRole = cm.getRole(role_id_list, "-Please Select-");
        dept_abbr = cm.getDepartmentAbbr(deptId);
        // ddlRole = cm.getRole(role_id_list,"-Please Select-");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Manage Departmental Users Group</title>
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

    </head>
    <body>
        <!-- Modal for New User -->
        <div id="myModal1" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Add New Employee</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light" id="loadModalBody">


                    </div>
                    <div class="modal-footer">
                        <!--<input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal for Mapped Scheme List -->
        <div id="modalScheme" class="modal hide fade" role="dialog" aria-labelledby=modalSchemeLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Mapped Scheme List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="input-group mb-3 col-md-12">
                                <table id="itemList" class="table table-sm table-striped table-bordered" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Sl No.</th>
                                            <th>Scheme Name</th>
                                            <th>Department</th>
                                            <th>Role</th>
                                        </tr>
                                    </thead> 
                                    <tbody>

                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <!--<input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal for Mapped User List -->
        <div id="modalUser" class="modal hide fade" role="dialog" aria-labelledby=modalUserLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Mapped Employee List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="input-group mb-3 col-md-12">
                                <table id="mappedUserList" class="table table-sm table-striped table-bordered" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Sl No.</th>
                                            <th>Employee Name</th>
                                            <th>Employee ID</th>
                                            <th>Group</th>
                                        </tr>
                                    </thead> 
                                    <tbody>

                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <!--<input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal for Create User Group -->
        <div id="modalGroup" class="modal hide fade" role="dialog" aria-labelledby=modalGroupLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Create Group</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="col-md-12">
                                <form id="frmUserRole" action="../ManageDeptUserGroup" method="post" > 
                                    <input type="hidden" id="ddlDepartment" name="ddlDepartment" value="<%=deptId%>" />

                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <label for="txtGroupName">Group Name</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <!--                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-user fa-lg"></i></span>-->
                                                    <input type="hidden" name="dept_abbr" id="dept_abbr" value="<%=dept_abbr%>" />
                                                    <span class="input-group-text" id="basic-addon1"><%=dept_abbr%></span>
                                                </div>
                                                <input type="hidden" id="hdnGroupId" name="hdnGroupId" value="0" />
                                                <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="0" />
                                                <input type="text" id="txtGroupName" name="txtGroupName" size="120" class="form-control form-control-sm"  required="required"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="form-group col-md-12">
                                            <button type="submit" id="btnAddDeptGroup" name="btnAddDeptGroup" class="btn btn-primary">Create Group</button>
                                            <input type="hidden" id="hdnEditDeptGroup" name="hdnEditDeptGroup" value="<%= edit%>" />
                                        </div>
                                    </div>
                                </form>
                            </div>                            
                        </div>
                    </div>
                    <div class="modal-footer">
                        <!--<input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal for Select Role -->
        <div id="modalRole" class="modal hide fade" role="dialog" aria-labelledby=modalRoleLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Assign Role</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label for="ddlRole">Select Role</label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                    </div>
                                    <select id="ddlRole" name="ddlRole" class="form-control" required="required">
                                        <%= ddlRole%>
                                    </select>
                                </div>
                            </div>  <input type="hidden" id="schemeId" name="schemeId" value="<%=schemeId%>" />
                            <input type="hidden" id="ddlWorkflow" name="ddlWorkflow" />
                            <input type="hidden" id="ddlWorkflow" name="ddlWorkflow" />
                            <!--                                        <div class="form-group col-md-4">
                                                                        <label for="ddlWorkflow">Select Workflow</label>
                                                                        <div class="input-group mb-3">
                                                                            <div class="input-group-prepend">
                                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                                            </div>
                                                                            <select id="ddlWorkflow" name="ddlWorkflow" class="form-control" required="required">
                                                                                <option value="">-Please Select-</option> 
                                                                                <option value="1">Beneficiary Workflow</option> 
                                                                                <option value="2">Payment Workflow</option> 
                                                                                <option value="3">Both</option> 
                                                                            </select>
                                                                        </div>
                                                                    </div>-->



                            <!--                                    <div class="row d-none" id="schemeListDiv">
                                                                    <div class="col-md-12">
                                                                        <div class="row">
                                                                            <div class="input-group mb-3 col-md-12">
                                                                                <table id="schemeList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                                                    <thead>
                                                                                        <tr>
                                                                                            <th></th>
                                                                                            <th>Scheme Name</th>
                                                                                            <th>Component Name</th>
                                                                                             <th>type</th>
                                                                                             <th>Beneficiary</th>
                                                                                             <th>Payment</th>
                                                                                        </tr>
                                                                                    </thead>                                        
                                                                                </table>
                                                                            </div>
                                                                        </div> -->
                            <div class="row" id="divSchemeMapping"> 
                                <div class="input-group mb-3 col-md-12">

                                    <button type="button" id="btnMapScheme" name="btnMapScheme" class="btn btn-success btn-block" >
                                        <strong>Save</strong>
                                    </button>
                                </div>
                                <div id="msgDiv" class="col-md-4"></div>
                            </div>
                            <div class="modal-footer">
                                <!--<input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                                <button type="button" id="btnEsign1" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign&nbsp;&nbsp;</button>-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
                                    <h4 class="text-white">Departmental User Group:</h4>
                                    <h6 class="text-white">Department: <%= dept_name%></h6>
                                    <h6 class="text-white">Scheme: <%= scheme_name%></h6>
                                </div>

                                <div class="card-body bg-light">
                                    <div class="input-group mb-3 col-md-4">
                                        <button type="button" id="btnAddNewGroup" name="btnAddNewGroup" class="btn btn-success btn-block">
                                            <strong>Add New Group</strong>
                                        </button>
                                    </div>
                                    <h3>Existing Group</h3>
                                    <div class="row"> 


                                        <div class="col-md-12">
                                            <div class="row d-none" id="groupListDiv">                                        
                                                <div class="input-group mb-3">
                                                    <table id="groupList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                        <thead>
                                                            <tr> 
                                                                <th>Sl. No.</th>
                                                                <th>Group Name</th>                                 
                                                                <th>Department</th> 
                                                                <!--                                                                <th>Edit</th>-->
                                                                <th>Add Employee</th>
                                                                <th>Employee List</th>
                                                                <th>Role</th>
                                                                <th>Mapped Scheme List</th>
                                                                <!-- <th>Map Schemes</th>
                                                                 <th>View Mapped Schemes</th>-->
                                                            </tr>
                                                        </thead> 
                                                        <tbody></tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/Admin/manageRoleBasedOffice.jsp" class="btn btn-primary">Next</a>
                                </div>
                            </div>
                            <div class="card d-none" id="deptUserListDiv">
                                <div class="card-header bg-info">
                                    <h4 class="text-white">Departmental User Group - Employee Mapping <button type="button" class="close" id="btnCloseDeptUserListDiv">&times;</button></h4>                                    
                                </div>

                                <div class="card-body bg-light">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="deptUserList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                        <thead>
                                                            <tr>
                                                                <th></th>                                                         
                                                                <th>Employee Code</th>
                                                                <th>Employee Name</th>
                                                            </tr>
                                                        </thead> 
                                                        <tbody>

                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="row" id="divSchemeMapping"> 
                                                <div class="input-group mb-3 col-md-4">
                                                    <button type="button" id="btnMapUser" name="btnMapUser" class="btn btn-success btn-block">
                                                        <strong>Map Existing Employee</strong>
                                                    </button>
                                                </div>
                                                <div class="input-group mb-3 col-md-4">
                                                    <button type="button" id="btnAddNewUser" name="btnAddNewUser" class="btn btn-success btn-block">
                                                        <strong>Add New Employee</strong>
                                                    </button>
                                                </div>
                                                <div id="msgDiv" class="col-md-4"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--                            <div class="card d-none" id="drpSchemeListDiv">
                                                            <div class="card-header bg-info">
                                                                <h4 class="text-white">Departmental User Group - Scheme Mapping <button type="button" class="close" id="btnCloseDrpSchemeListDiv">&times;</button></h4>
                                                            </div>
                            
                                                            <div class="card-body bg-light">
                            
                                                                
                                                                    </div>
                                                                </div>               -->
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
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/datatables.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/jquery.dataTables.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>

</html>
<script>
    $(document).ready(function () {
        var table;
        //$("#ddlDepartment").on('change', function (e) {
        var deptId = $('#ddlDepartment').val();
        $('#groupListDiv').removeClass("d-none").addClass("d-block");
        $('#groupList').DataTable().clear().destroy();
        var k = 0;

        table = $('#groupList').DataTable({
            "ajax": {
                "url": "${pageContext.request.contextPath}/AjaxServlet?groupList=1&deptId=" + deptId,
                "type": "POST"
            },
            "iDisplayLength": 10,
            'order': [[0, 'asc']],
            'responsive': true,
            "columns": [
                {"data": function (data, type, dataToSet) {
                        return  ++k;
                    }, "width": "5%"},
                {"data": "group_name"},
                {"data": "department_name"},
//                {"data": function (data, type, dataToSet) {
//                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnEditGroup\" name=\"btnEditGroup\" value=\"" + data.group_id + "\">Edit</button>";
//                    }, "width": "20%"},
                {"data": function (data, type, dataToSet) {
                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnAddUser\" name=\"btnAddUser\"  value=\"" + data.group_id + "#" + data.department_id + "\">Add Employee</button>";
                    }, "width": "20%"},
                {"data": function (data, type, dataToSet) {
                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMappedUserList\" name=\"btnMappedUserList\" value=\"" + data.group_id + "#" + data.department_id + "\">View Employee List</button>";
                    }, "width": "20%"},
                {"data": function (data, type, dataToSet) {
                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMapScheme\" name=\"btnMapScheme\" value=\"" + data.group_id + "#" + data.department_id + "\">Assign Role</button>";
                    }, "width": "20%"},
                {"data": function (data, type, dataToSet) {
                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMappedSchemeList\" name=\"btnMappedSchemeList\" value=\"" + data.group_id + "#" + data.department_id + "\">View Mapped Scheme</button>";
                    }, "width": "20%"}
            ]

        });

        $('#groupList_wrapper').addClass("col-md-12");
        $("select[name='groupList_length']").removeClass("custom-select custom-select-sm ");

        //});

        $('#groupList tbody').on('click', 'button', function (e) {
            var btnName = this.name;
            if (btnName === "btnEditGroup") {
                var groupId = this.value;
                var edit = "0";
                $.ajax({
                    url: "${pageContext.request.contextPath}/AjaxServlet?groupEdit=1&groupId=" + groupId,
                    type: "POST",
                    dataType: "text",
                    success: function (result) {
                        var objJSON = JSON.parse(result);
                        if (typeof objJSON === 'object') {
                            edit = "1";
                            $('#hdnEditDeptGroup').val(edit);
                            $('#hdnGroupId').val(objJSON['group_id']);
                            $('#txtGroupName').val(objJSON['group_name']);
                            $('#ddlDepartment').val(objJSON['department_id']);
                        }
                    }
                });
            } else if (btnName === "btnAddUser") {
                var groupId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];
                $('#hdnDeptId').val(deptId);
                $('#hdnGroupId').val(groupId);
                $('#myModal1').modal("show");
                $("#loadModalBody").load("manageDeptWiseUser.jsp?deptId=" + deptId + "&groupId=" + groupId);
//                $('#btnAddNewUser').click();

//                $('#deptUserListDiv').removeClass("d-none").addClass("d-block");
//                $('#drpSchemeListDiv').removeClass("d-block").addClass("d-none");
//                $('#deptUserList').DataTable().clear().destroy();
//                table = $('#deptUserList').DataTable({
//                    "ajax": {
//                        "url": "${pageContext.request.contextPath}/ManageDeptUserGroup?deptUserList=1&groupId=" + groupId + "&deptId=" + deptId,
//                        "type": "POST"
//                    },
//                    "columnDefs": [
//                        {
//                            'targets': 0,
//                            'className': 'select-checkbox',
//                            'checkboxes': {
//                                'selectRow': true
//                            }
//                        }
//                    ],
//                    "select": {
//                        'style': 'multi'
//                    },
//                    "iDisplayLength": 10,
//                    'order': [[1, 'asc']],
//                    'responsive': true,
//                    "columns": [
//                        {"data": "user_id", "width": "5%"},
//                        {"data": "user_id", "width": "40%"},
//                        {"data": "emp_name"}
//                    ]
//
//                });
//
//                $('#deptUserList_wrapper').addClass("col-md-12");
//                $("select[name='deptUserList_length']").removeClass("custom-select custom-select-sm ");

            } else if (btnName === "btnMappedUserList") {
                $("#itemList tbody").empty();
                var k = 0;
                var groupId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];

                $('#mappedUserList').DataTable().clear().destroy();
                table2 = $('#mappedUserList').DataTable({
                    "ajax": {
                        url: "${pageContext.request.contextPath}/ManageDeptUserGroup?mappedUserList=1&groupId=" + groupId + "&deptId=" + deptId,
                        "type": "POST"
                    },
                    "iDisplayLength": 10,
                    'order': [[0, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": function (data, type, dataToSet) {
                                return  ++k;
                            }, "width": "5%"},
                        {"data": "emp_name"},
                        {"data": "user_id"},
                        {"data": "group_name"}
                    ]


                });
                $('#mappedUserList_wrapper').addClass("col-md-12");
                $("select[name='mappedUserList_length']").removeClass("custom-select custom-select-sm ");

                $('#modalUser').modal("show");

            } else if (btnName === "btnMapScheme") {
                $('#ddlRole').val("");
                $('#ddlWorkflow').val("3");


                var groupId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];

                $('#hdnDeptId').val(deptId);
                $('#hdnGroupId').val(groupId);
                $('#drpSchemeListDiv').removeClass("d-none").addClass("d-block");
                $('#deptUserListDiv').removeClass("d-block").addClass("d-none");

                var roleId = $('#ddlRole option:selected').val();
                var WorkId = $('#ddlWorkflow option:selected').val();
                var deptId = $("#hdnDeptId").val();
//                    $("#ddlWorkflow").on('change', function (e) {
//                    $('#schemeListDiv').removeClass("d-none").addClass("d-block");
//                    $('#schemeList').DataTable().clear().destroy();
//                    table = $('#schemeList').DataTable({
//                        "ajax": {
//                            "url": "${pageContext.request.contextPath}/ManageDeptUserGroup?mapRoleScheme=1&roleId=" + roleId + "&deptId=" + deptId + "&workflow=" + WorkId,
//                            "type": "POST"
//                        },
//                        "columnDefs": [
//                            {
//                                'targets': 0,
//                                'className': 'select-checkbox',
//                                'checkboxes': {
//                                    'selectRow': true
//                                }
//                            }
//                        ],
//                        "select": {
//                            'style': 'multi'
//                        },
//                        "iDisplayLength": 10,
//                        'order': [[1, 'asc']],
//                        'responsive': true,
//                        "columns": [
//                            {"data": "id", "width": "5%"},
//                            {"data": "scheme_name", "width": "20%"},
//                            {"data": "component_name", "width": "30%"}
//                        ]
//
//                    });
//
//                    $('#schemeList_wrapper').addClass("col-md-12");
//                    $("select[name='schemeList_length']").removeClass("custom-select custom-select-sm ");
//
//
//                });
                $('#modalRole').modal("show");
            } else if (btnName === "btnMappedSchemeList") {
                $("#itemList tbody").empty();
                var k = 0;
                var groupId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];

                $('#itemList').DataTable().clear().destroy();
                table2 = $('#itemList').DataTable({
                    "ajax": {
                        url: "${pageContext.request.contextPath}/ManageDeptUserGroup?mappedRoleScheme=1&groupId=" + groupId + "&deptId=" + deptId,
                        "type": "POST"
                    },
                    "iDisplayLength": 10,
                    'order': [[0, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": function (data, type, dataToSet) {
                                return  ++k;
                            }, "width": "5%"},
                        {"data": "scheme_name"},
                        {"data": "department_name"},
                        {"data": "role_name"}
                    ]


                });
                $('#itemList_wrapper').addClass("col-md-12");
                $("select[name='itemList_length']").removeClass("custom-select custom-select-sm ");

                $('#modalScheme').modal("show");
            }

        });

        //add new user
        //function createUser()
        $('#btnAddNewUser').on('click', function (e) {
            var deptId = $("#hdnDeptId").val();
            $('#myModal1').modal("show");
            $("#loadModalBody").load("manageDeptWiseUser.jsp?deptId=" + deptId);
        });
        //create New Group
        $('#btnAddNewGroup').on('click', function (e) {
            // var deptId = $("#de").val();
            $('#modalGroup').modal("show");
            // $("#loadModalBody").load("manageDeptWiseUser.jsp?deptId=" + deptId);
        });

        //code for map user -> group

        $('#btnMapUser').on('click', function (e) {
            var deptId = $("#hdnDeptId").val();
            var groupId = $("#hdnGroupId").val();
            var rows_selected = table.column(0).checkboxes.selected();
            var user_id = [];
            $.each(rows_selected, function (index, rowId) {
                var rowData = rows_selected[index];
                alert("s" + rowData);
                var b = table
                        .rows(function (idx, data, node) {
                            return data.user_id === rowData ? true : false;
                        })
                        .data();
                console.log(b[0]);
                user_id.push(b[0]);


            });

            if (user_id.length === 0) {
                alert("No User Selected");
                return false;
            }

            var json = JSON.stringify(user_id);
            alert(json);
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?MapUserGroup=1&groupId=" + groupId + "&deptId=" + deptId,
                type: "POST",
                async: false,
                dataType: 'json',
                data: {json: json},
                success: function (result) {
                    if (parseInt(result) > 0) {
                        // $("select#ddlDepartment").prop('selectedIndex', 0);
                        $('#deptUserListDiv').removeClass("d-block").addClass("d-none");
                        $('#deptUserList').DataTable().clear().destroy();
                        // $('#roleListDiv').removeClass("d-block").addClass("d-none");
                        //$('#roleList').clear().destroy();

                    }
                }
            });

        });

        $('#btnCloseDeptUserListDiv').on('click', function (e) {
            $('#deptUserListDiv').removeClass("d-block").addClass("d-none");
        });
        $('#btnCloseDrpSchemeListDiv').on('click', function (e) {
            $('#drpSchemeListDiv').removeClass("d-block").addClass("d-none");
        });



        //code for map scheme
        $('#btnMapScheme').on('click', function (e) {
            var deptId = $('#hdnDeptId').val();
            var roleId = $('#ddlRole option:selected').val();
            var workflowType = $('#ddlWorkflow').val();
            var groupId = $('#hdnGroupId').val();
            var scheme_id = $('#schemeId').val();
//            var rows_selected = table.column(0).checkboxes.selected();
//            // var cb1 = "";
//            // var cb2 = "";
//            var type = 0;
//            var id = [];
//            var type1 = [];
//            $.each(rows_selected, function (index, rowId) {
//                var rowData = rows_selected[index];
//                var b = table
//                        .rows(function (idx, data, node) {
//                            return data.id === rowData ? true : false;
//                        })
//                        .data();
//                id.push(b[0]);
//
//
//            });


            if (scheme_id.length === 0) {
                alert("No Scheme Selected");
                return false;
            }

            var json = JSON.stringify(scheme_id);
            // var json = JSON.stringify(type1);
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?MapScheme=1&roleId=" + roleId + "&deptId=" + deptId + "&groupId=" + groupId + "&workflowType=" + workflowType + "&schemeId=" + scheme_id,
                type: "POST",
                async: false,
                dataType: 'json',
                data: {json: json},
                success: function (result) {
                    if (parseInt(result) === 1) {

                        //$('#ddlDepartment option[value=0]').prop('selected', true);
                        $("select#ddlDepartment").prop('selectedIndex', 0);
                        $('#schemeListDiv').removeClass("d-block").addClass("d-none");
                        $('#schemeList').DataTable().clear().destroy();
                        // $('#roleListDiv').removeClass("d-block").addClass("d-none");
                        //$('#roleList').clear().destroy();
                        $('#drpSchemeListDiv').removeClass("d-block").addClass("d-none");
                        alert("Scheme Mapped Successfully");

                    }
                    if (parseInt(result) === 3) {
                        alert("Scheme Already Mapped");
                    }
                }
            });

        });




    });


</script> 
<%
    } else {
        response.sendRedirect("../login.jsp");
    }
%>