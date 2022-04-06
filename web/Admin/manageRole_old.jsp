<%-- 
    Document   : manageRole
    Created on : 30 May, 2020, 11:23:00 AM
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
        String regMsg = "";
        String logMsg = "";
        String type = "light";

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
        String ddlURL = "";
        ddlURL = cm.getURL("-Please Select-");


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Manage User Roles</title>
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
                                    <h4 class="text-white">User Role:</h4>
                                </div>
                            </div>
                            <div class="card-body bg-light">

                                <div class="row"> 
                                    <div class="col-md-6">
                                        <form id="frmUserRole" action="../ManageUserRole" method="post" > 
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">Department</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlDepartment" name="ddlDepartment" class="form-control" required="required">
                                                            <%=ddlDept%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtJuriLevel">Role Level</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-globe fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlJurisdiction" name="ddlJurisdiction" class="form-control" required="required" >
                                                            <option value="0">-Please Select-</option> 
                                                            <option value="Approver">Approver</option> 
                                                            <option value="Checker">Checker</option> 
                                                            <option value="Maker">Maker</option> 
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtRoleName">Role Name</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-user fa-lg"></i></span>
                                                        </div>
                                                        <input type="hidden" id="hdnRoleId" name="hdnRoleId" value="" />
                                                        <input type="text" id="txtRoleName" name="txtRoleName" size="120" class="form-control form-control-sm"  required="required"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-sm-12">
                                                    <button type="submit" id="btnAddRole" name="btnAddRole" class="btn btn-primary">Save Details</button>
                                                    <input type="hidden" id="hdnEditRole" name="hdnEditRole" value="" />
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row d-none" id="roleListDiv">                                        
                                            <div class="input-group mb-3">
                                                <table id="roleList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                    <thead>
                                                        <tr> 
                                                            <th>Sl. No.</th>
                                                            <th>Role Name</th>                                 
                                                            <th>Department</th>                                                
                                                            <th>Level</th>  
                                                            <th>Edit</th>
                                                            <th>Map Schemes</th>
                                                        </tr>
                                                    </thead> 
                                                    <tbody></tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row d-none" id="schemeListDiv">
                                    <div class="col-md-12">
                                        <div class="row">
                                            <div class="input-group mb-3 col-md-12">
                                                <table id="schemeList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                                    <thead>
                                                        <tr>
                                                            <th></th>                                                            
                                                            <th>Scheme Name</th>
                                                            <th>Component Name</th>                                                            
                                                        </tr>
                                                    </thead>                                        
                                                </table>
                                            </div>
                                        </div>
                                        <div class="row" id="divSchemeMapping"> 
                                            <div class="input-group mb-3 col-md-4">
                                                <button type="button" id="btnMapScheme" name="btnMapScheme" class="btn btn-success btn-block">
                                                    <strong>Map Schemes</strong>
                                                </button>
                                            </div>
                                            <div id="msgDiv" class="col-md-4"></div>
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
        var table;
        $("#ddlDepartment").on('change', function (e) {
            var deptId = $('#ddlDepartment option:selected').val();
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?roleList=1&deptId=" + deptId,
                type: "POST",
                dataType: "text",
                success: function (result) {
                    //alert(result);
                    $("#roleListDiv").removeClass("d-none");
                    $("#roleList tbody").empty();
                    $("#roleList tbody").append(result);
                }
            });
        });


        $('#roleList tbody').on('click', 'button', function (e) {
            var btnName = this.name;
            if (btnName === "btnEdit") {
                var edit = "0";
                $.ajax({
                    url: "${pageContext.request.contextPath}/AjaxServlet?roleId=" + id,
                    type: "POST",
                    dataType: "text",
                    success: function (result) {
                        var objJSON = JSON.parse(result);
                        if (typeof objJSON === 'object') {
                            edit = "1";
                            $('#hdnEditRole').val(edit);
                            $('#hdnRoleId').val(objJSON['role_id']);
                            $('#txtRoleName').val(objJSON['role_name']);
                            $('#ddlDepartment').val(objJSON['department_id']);
                            $('#ddlJurisdiction').val(objJSON['role_level']);
                        }
                    }
                });
            } else if (btnName === "btnMapScheme") {
                var roleId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];

                $('#schemeListDiv').removeClass("d-none").addClass("d-block");


                $('#schemeList').DataTable().clear().destroy();
                table = $('#schemeList').DataTable({
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/ManageUserRole?mapRoleScheme=1&roleId=" + roleId + "&deptId=" + deptId,
                        "type": "POST"
                    },
                    "columnDefs": [
                        {
                            'targets': 0,
                            'className': 'select-checkbox',
                            'checkboxes': {
                                'selectRow': true
                            }
                        }
                    ],
                    "select": {
                        'style': 'multi'
                    },
                    "iDisplayLength": 15,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": "scheme_id", "width": "5%"},
                        {"data": "scheme_name", "width": "20%"},
                        {"data": "component_name", "width": "30%"}
                    ]


                });
                $('#schemeList_wrapper').addClass("col-md-12");
                $("select[name='schemeList_length']").removeClass("custom-select custom-select-sm ");
            }
        });




    });


</script> 
<%

    } else {
        response.sendRedirect("../login.jsp");
    }
%>