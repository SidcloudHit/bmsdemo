<%-- 
    Document   : user_role_scheme_office_mapping
    Created on : 20 Aug, 2021, 2:02:43 PM
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
        //String dept_id = session.getAttribute("user_dept").toString();
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
        String ddlUser = "";
        ddlUser = cm.getUserList("-Please Select-");

        String ddlScheme = "";
        ddlScheme = cm.getRoleBasedSchemeList("-Please Select-", Integer.parseInt(deptId));
        //String ddlURL = "";
        // ddlURL = cm.getURL("-Please Select-");

        //String ddlScheme = "";
        // ddlScheme = cm.getRoleBasedOffice("-Please Select-");

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
                                    <h4 class="text-white">Mapping:</h4>
                                </div>
                            </div>
                            <div class="card-body bg-light">
                                <div class="row"> 
                                    <div class="col-md-5">
                                        <form id="frmUserOfficeMapping" action="../userRoleSchemeOfficeMapping" method="post" > 

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtScheme">Scheme</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlScheme" name="ddlScheme" class="form-control" required="required">
                                                            <!--<option value="0">-Please Select-</option> -->
                                                            <%=ddlScheme%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" name="radBenPay" id="radBen" value="1" onclick="dependentDropDown1('ddlScheme', 'radBen', 'ddlRole', 'fillSchemeRole');">
                                                        <label class="form-check-label" for="radBen">Beneficiary Workflow</label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" name="radBenPay" id="radPay" value="2" onclick="dependentDropDown1('ddlScheme', 'radPay', 'ddlRole', 'fillSchemeRole');">
                                                        <label class="form-check-label" for="radPay">Payment Workflow</label>
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
                                                        <select id="ddlRole" name="ddlRole" class="form-control" required="required" onchange="dependentDropDown1('ddlRole', 'ddlScheme', 'ddlOffice', 'fillMultiOffice');
                                                                dependentDropDown1('ddlRole', 'ddlScheme', 'ddlParentOffice', 'fillMapParentOffice');">

                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <!--<div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">Department</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
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
                                        <select id="ddlRole" name="ddlRole" class="form-control" required="required"  
                                                onchange=" resetBlock(9);
                                                        dependentDropDown1('ddlRole', 'ddlDepartment', 'ddlScheme', 'fillScheme');
                                                        dependentDropDown1('ddlRole', 'ddlDepartment', 'ddlOffice', 'fillMultiOffice');">
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div>
                                </div>
                            </div>-->
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtScheme">Office</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlOffice" name="ddlOffice" class="form-control" required="required" multiple="multiple">
                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
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
                                                <div class="form-group col-md-8">
                                                    <label for="txtUser">User</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlUser" name="ddlUser" class="form-control" required="required">
                                                            <%=ddlUser%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
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
                                                <div class="form-group col-sm-12">
                                                    <button type="submit" id="btnAddDetails" name="btnAddDetails" class="btn btn-primary">Save Details</button>
                                                    <input  type="hidden" id="hdnEditDetails" name="hdnEditDetails" value="<%= edit%>" />
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="col-md-7">
                                        <div class="row d-none" id="schemeOfficeMapListDiv">
                                            <div class="input-group mb-3">
                                                <table id="schemeOfficeMapList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">

                                                    <thead>
                                                        <tr>
                                                            <th>Scheme Name</th>
                                                            <th>User Name</th>
                                                            <!-- <th>Department Name</th>-->
                                                            
                                                            <th>Role Name</th>                                                            
                                                            <th>Office</th>
                                                            <th>Parent Office</th>
                                                        </tr>
                                                    </thead> 
                                                    <tbody></tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <!--<div class="row" id="divSchemeMapping"> 
                                            <div class="input-group mb-3 col-md-4">
                                                <button type="button" id="btnMapScheme" name="btnMapScheme" class="btn btn-success btn-block">
                                                    <strong>Map Schemes</strong>
                                                </button>
                                            </div>
                                            <div id="msgDiv" class="col-md-4"></div>
                                        </div>-->
                                    </div>
                                </div>
                                </form>
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.13/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/0.9.13/css/bootstrap-multiselect.css">


</html>
<script>
                                                            $(document).ready(function () {

                                                                /*$('#ddlOffice').multiselect({
                                                                 includeSelectAllOption: true,
                                                                 });*/

                                                                var table;
                                                                $("#ddlScheme").on('change', function (e) {
                                                                    // var deptId = $('#ddlDepartment option:selected').val();
                                                                    var schemeId = $('#ddlScheme option:selected').val();
                                                                    var j = 0;
                                                                    $('#schemeOfficeMapListDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#schemeOfficeMapList').DataTable().clear().destroy();

                                                                    table = $('#schemeOfficeMapList').DataTable({
                                                                        "ajax": {
                                                                            url: "${pageContext.request.contextPath}/AjaxServlet?schemeOfficeMapping=1&schemeId=" + schemeId,
                                                                            "type": "POST"
                                                                        },
                                                                        "iDisplayLength": 50,
                                                                        'order': [[1, 'asc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": "scheme_name"},
                                                                            {"data": "emp_name"},
                                                                            {"data": "role_name"},
                                                                            {"data": "office_name"},
                                                                            {"data": "parent_office"}
                                                                        ],
                                                                        

                                                                    });

                                                                    $('#schemeOfficeMapList_wrapper').addClass("col-md-12");
                                                                    $("select[name='schemeOfficeMapList_length']").removeClass("custom-select custom-select-sm ");
                                                                    //$("#ddlOffice option[value='" + data.office_id + "']").remove();
                                                                    



                                                                    //   $.ajax({
                                                                    // url: "${pageContext.request.contextPath}/AjaxServlet?schemeOfficeMapping=1&userId=" + userId,
                                                                    //   type: "POST",
                                                                    //   dataType: "text",
                                                                    //   success: function (result) {
                                                                    //      $("#schemeOfficeMapListDiv").removeClass("d-none");
                                                                    //      $("#schemeOfficeMapList tbody").empty();
                                                                    //     $("#schemeOfficeMapList tbody").append(result);


                                                                    //$("#ddlList option[value='2']").remove();
                                                                    //   }
                                                                    // });
                                                                });


                                                            });


</script>
<%

    } else {
        response.sendRedirect("../login.jsp");
    }
%>