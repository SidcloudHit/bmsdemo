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
    <!-- Modal for Mapped Beneficiary List -->
    <div id="myModal1" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
                                                        <input type="hidden" id="hdnRoleId" name="hdnRoleId" value="0" />
                                                        <input type="text" id="txtRoleName" name="txtRoleName" size="120" class="form-control form-control-sm"  required="required"/>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-sm-12">
                                                    <button type="submit" id="btnAddRole" name="btnAddRole" class="btn btn-primary">Save Details</button>
                                                    <input type="hidden" id="hdnEditRole" name="hdnEditRole" value="<%= edit%>" />
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row d-none" id="roleListDiv">                                        
                                            <div class="input-group mb-3">
                                                <table id="roleList" class="table table-striped table-bordered  dt-responsive nowrap" style="width:100%">
                                                    <thead>
                                                        <tr> 
                                                            <th>Sl. No.</th>
                                                            <th>Role Name</th>                                 
                                                            <th>Department</th>                                                
                                                            <th>Level</th>  
                                                            <th>Edit</th>
                                                            <th>Map Schemes</th>
                                                            <th>View Mapped Schemes</th>
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
                                                <table id="schemeList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                    <thead>
                                                        <tr>
                                                            <th></th>
                                                            <th>Scheme Name</th>
                                                            <th>Component Name</th>
                                                            <!-- <th>type</th>-->
                                                            <th>Beneficiary Workflow</th>
                                                            <th>Payment Workflow</th>
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
            //alert($('#roleId').val());
            var btnName = this.name;
            if (btnName === "btnEdit") {
                var roleId = this.value;
                var edit = "0";
                $.ajax({
                    url: "${pageContext.request.contextPath}/AjaxServlet?roleEdit=1&roleId=" + roleId,
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
            } else if (btnName === "btnSchemeList") {
                var roleId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];
                $('#hdnRoleId').val(roleId);
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
                        }/*,
                        {
                            'targets': 3,
                            'className': 'select-checkbox',
                            'checkboxes': {
                                'selectRow': true
                            }
                        },
                        {
                            'targets': 4,
                            'className': 'select-checkbox',
                            'checkboxes': {
                                'selectRow': true
                            }
                        }*/
                    ],
                    "select": {
                        'style': 'multi'
                    },
                    "iDisplayLength": 15,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": "id", "width": "5%"},
                        {"data": "scheme_name", "width": "45%"},
                        {"data": "component_name", "width": "30%"},
                        //{"data": "type", "width": "10%"},
                        {"data": function (data, type, dataToSet) {
                                if (data.type === 1 || data.type === 3) {
                                    return  "<input type=\"checkbox\"  value=\"Beneficiary\" id=\"cbBen" + data.id + "\" checked=\"checked\" disabled>";

                                    //alert();
                                    // $("#cbBen"+ data.id).prop("checked", true);
                                } else {
                                    return  "<input type=\"checkbox\"  value=\"Beneficiary\" id=\"cbBen" + data.id + "\">";
                                }
                            }, "width": "10%"},
                        {"data": function (data, type, dataToSet) {
                                if (data.type === 2 || data.type === 3) {
                                    return  "<input type=\"checkbox\"  value=\"Payment\" id=\"cbPay" + data.id + "\" checked=\"checked\" disabled>";
                                } else {
                                    return  "<input type=\"checkbox\"  value=\"Payment\" id=\"cbPay" + data.id + "\">";
                                }
                            }, "width": "10%"}
                    ]

                });

                //if(data.type === '2'){}

                $('#schemeList_wrapper').addClass("col-md-12");
                $("select[name='schemeList_length']").removeClass("custom-select custom-select-sm ");
            } else if (btnName === "btnMappedSchemeList") {
                $("#itemList tbody").empty();
                var k = 0;
                var roleId = this.value.split("#")[0];
                var deptId = this.value.split("#")[1];

                $('#itemList').DataTable().clear().destroy();
                table2 = $('#itemList').DataTable({
                    "ajax": {
                        url: "${pageContext.request.contextPath}/ManageUserRole?mappedRoleScheme=1&roleId=" + roleId + "&deptId=" + deptId,
                        "type": "POST"
                    },
                    "iDisplayLength": 50,
                    'order': [[1, 'asc']],
                    'responsive': true,
                    "columns": [
                        {"data": function (data, type, dataToSet) {
                                return  ++k;
                            }, "width": "5%"},
                        {"data": "scheme_name", },
                        {"data": "department_name"},
                        {"data": "role_name"}
                    ]


                });
                $('#itemList_wrapper').addClass("col-md-12");
                $("select[name='itemList_length']").removeClass("custom-select custom-select-sm ");

                $('#myModal1').modal("show");
            }
        });


        $('#btnMapScheme').on('click', function (e) {
            var deptId = $('#ddlDepartment option:selected').val();
            var roleId = $('#hdnRoleId').val();
            var rows_selected = table.column(0).checkboxes.selected();
            var cb1 = "";
            var cb2 = "";
            var type = 0;
            var id = [];
            var type1 = [];
            $.each(rows_selected, function (index, rowId) {
                var rowData = rows_selected[index];
                var b = table
                        .rows(function (idx, data, node) {
                            return data.id === rowData ? true : false;
                        })
                        .data();
                console.log(b[0]);
                id.push(b[0]);
                cb1 = "cbBen" + b[0].id;
                cb2 = "cbPay" + b[0].id;


                if ($('#' + cb1).is(':checked')) {
                    if ($('#' + cb2).is(':checked')) {
                        type = 3;
                    } else {
                        type = 1;
                    }
                } else if ($('#' + cb2).is(':checked')) {
                    if ($('#' + cb1).is(':checked')) {
                        type = 3;
                    } else {
                        type = 2;
                    }
                } else {
                    alert("Please Check either Beneficiary or Payment");
                    return false;
                }

                type1.push(type);
                //alert(type1.valueOf());

            });


            if (id.length === 0) {
                alert("No Scheme Selected");
                return false;
            }

            var json = JSON.stringify(id);
            // var json = JSON.stringify(type1);
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?MapScheme=1&roleId=" + roleId + "&deptId=" + deptId + "&type=" + type1,
                type: "POST",
                async: false,
                dataType: 'json',
                data: {json: json},
                success: function (result) { 
                    if (parseInt(result) > 0) {
                        //$('#ddlDepartment option[value=0]').prop('selected', true);
                        $("select#ddlDepartment").prop('selectedIndex', 0);
                        $('#schemeListDiv').removeClass("d-block").addClass("d-none");
                        $('#schemeList').DataTable().clear().destroy();
                        $('#roleListDiv').removeClass("d-block").addClass("d-none");
                        //$('#roleList').clear().destroy();
                        
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