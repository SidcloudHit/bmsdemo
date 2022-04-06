<%-- 
    Document   : workflowMapping
    Created on : 21 Sep, 2021, 8:43:19 PM
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
    String userId = "", userName = "", userDes = "",scheme_name = "", dept_name = "";
    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        //officeId = Integer.parseInt(loginObj.getUserOfficeId());
        schemeId = Integer.parseInt(loginObj.getUserScheme());
        scheme_name = loginObj.getUserSchemeName();
        dept_name = loginObj.getUserDeptName();

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
        ddlDept = cm.getDeprtment(deptId, "-Please Select-");
        String ddlUser = "";
        ddlUser = cm.getUserList("-Please Select-");

        String ddlScheme = "";
        //ddlScheme = cm.getRoleBasedSchemeList("-Please Select-", deptId);
        ddlScheme = cm.getScheme("-Please Select-", schemeId);
        //String ddlURL = "";
        // ddlURL = cm.getURL("-Please Select-");

        //String ddlScheme = "";
        // ddlScheme = cm.getRoleBasedOffice("-Please Select-");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Workflow Mapping</title>
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
                                    <h4 class="text-white">Workflow Mapping</h4>
                                    <h6 class="text-white">Department: <%= dept_name%></h6>
                                    <h6 class="text-white">Scheme: <%= scheme_name%></h6>
                                </div>
                            </div>
                            <div class="card-body bg-light">
                                <div class="row"> 
                                    <div class="col-md-5">
                                        <form id="frmUserOfficeMapping" action="../WorkflowMapping" method="post" > 
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">User Department</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="0" />                                                        
                                                        <select id="ddlDepartment" name="ddlDepartment" class="form-control" required="required" > <!-- onchange="dependentDropDown('ddlDepartment', 'ddlScheme', 'fillDeptScheme');" -->
                                                            <%=ddlDept%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtScheme">Scheme</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-sticky-note fa-lg"></i></span>
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
                                                        <input class="form-check-input" type="radio" name="radBenPay" id="radBen" value="1" onclick="dependentDropDown2('ddlDepartment', 'ddlScheme', 'radBen', 'ddlGroup', 'fillSchemeGroup');">
                                                        <label class="form-check-label" for="radBen">Beneficiary Workflow</label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" name="radBenPay" id="radPay" value="2" onclick="dependentDropDown2('ddlDepartment', 'ddlScheme', 'radPay', 'ddlGroup', 'fillSchemeGroup');">
                                                        <label class="form-check-label" for="radPay">Payment Workflow</label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input class="form-check-input" type="radio" name="radBenPay" id="radBoth" value="3" onclick="dependentDropDown2('ddlDepartment', 'ddlScheme', 'radPay', 'ddlGroup', 'fillSchemeGroup');">
                                                        <label class="form-check-label" for="radBoth">Both Workflow</label>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="lblGroup">Departmental User Group</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-users"></i></span>
                                                        </div>
                                                        <select id="ddlGroup" name="ddlGroup" class="form-control" required="required" onchange="dependentDropDown('ddlGroup', 'ddlOffice', 'fillGroupOffices');dependentDropDown1('ddlScheme', 'ddlGroup', 'ddlParentDepartment', 'fillMapParentDepartment');">

                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtScheme">Office</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlOffice" name="ddlOffice" class="form-control" required="required">
                                                            <option value="">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">Parent Department</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                        </div>
                                                        <select id="ddlParentDepartment" name="ddlParentDepartment" class="form-control" required="required" onchange="dependentDropDown2('ddlScheme', 'ddlGroup', 'ddlParentDepartment', 'ddlParentGroup', 'fillMapParentGroup');"> 

                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="lblGroup">Parent User Group</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-users"></i></span>
                                                        </div>
                                                        <select id="ddlParentGroup" name="ddlParentGroup" class="form-control" required="required" onchange="dependentDropDown('ddlParentGroup', 'ddlParentOffice', 'fillGroupOffices');">

                                                        </select>
                                                    </div>
                                                </div>
                                            </div>         
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="ddlParentOffice">Parent Office</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home"></i></span>
                                                        </div>
                                                        <select id="ddlParentOffice" name="ddlParentOffice" class="form-control" required="required" >

                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <!--<div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtLoginId">Login ID</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-key fa-lg"></i></span>
                                                        </div>
                                                        <input type="text"  id="txtLoginId" name="txtLoginId" size="255" class="form-control form-control-sm" />
                                                    </div>
                                                </div>
                                            </div>-->

                                            <div class="row">
                                                <div class="form-group col-sm-12">
                                                    <button type="button" id="btnAddWorkflowDetails" name="btnAddWorkflowDetails" class="btn btn-primary">Save Details</button>
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
                                                            <th>Workflow</th>
                                                            <th>Department Name</th>
                                                            <th>Role Name</th>                                                            
                                                            <th>Office Name</th>
                                                            <th>Parent Department Name</th>
                                                            <th>Parent Office</th>
                                                            <!--<th>Login ID</th>-->
                                                        </tr>
                                                    </thead> 
                                                    <tbody></tbody>
                                                </table>
                                            </div>
                                        </div>

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

                                                                var table;

                                                                $("#ddlGroup").on('change', function (e) {
                                                                    var deptId = $('#ddlDepartment option:selected').val();
                                                                    var schemeId = $('#ddlScheme option:selected').val();
                                                                    var workflow = $('input[name=radBenPay]:checked').val();
                                                                    var groupId = $('#ddlGroup option:selected').val();
                                                                    var j = 0;
                                                                    $('#schemeOfficeMapListDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#schemeOfficeMapList').DataTable().clear().destroy();

                                                                    table = $('#schemeOfficeMapList').DataTable({
                                                                        "ajax": {
                                                                            url: "${pageContext.request.contextPath}/AjaxServlet?schemeOfficeMapping=1&deptId=" + deptId + "&schemeId=" + schemeId + "&workflow=" + workflow + "&groupId=" + groupId,
                                                                            "type": "POST"
                                                                        },
                                                                        "iDisplayLength": 10,
                                                                        'order': [[1, 'asc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": "scheme_name"},
                                                                            {"data": function (data, type, dataToSet) {
                                                                                    if (parseInt(data.mapping_type) === 1) {
                                                                                        return  "Beneficiary Workflow";
                                                                                    } else if (parseInt(data.mapping_type) === 2) {
                                                                                        return "Payment Workflow";
                                                                                    } else if (parseInt(data.mapping_type) === 3) {
                                                                                        return "Both Workflow";
                                                                                    }

                                                                                }},
                                                                            {"data": "departmet_name"},
                                                                            {"data": "role_name"},
                                                                            {"data": "office_name"},
                                                                            {"data": "parent_departmet_name"},
                                                                            {"data": "parent_office_name"}
                                                                            //{"data": "login_id"}
                                                                        ]
                                                                    });
                                                                    $('#schemeOfficeMapList_wrapper').addClass("col-md-12");
                                                                    $("select[name='schemeOfficeMapList_length']").removeClass("custom-select custom-select-sm ");
                                                                });



                                                                $('#btnAddWorkflowDetails').on('click', function (e) {

                                                                    var deptId = $('#ddlDepartment option:selected').val();
                                                                    var schemeId = $('#ddlScheme option:selected').val();
                                                                    var workflow = $('input[name="radBenPay"]:checked').val();
                                                                    var groupId = $('#ddlGroup option:selected').val();
                                                                    var officeId = $('#ddlOffice option:selected').val(); //.toArray().map(item => item.text).join();

                                                                    var pDeptId = $('#ddlParentDepartment option:selected').val();
                                                                    var pGroupId = $('#ddlParentGroup option:selected').val();
                                                                    var pOfficeId = $('#ddlParentOffice option:selected').val();
                                                                    var loginId = "NA";//$('#txtLoginId').val();
                                                                    
                                                                    //console.log(loginId);
                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/WorkflowMapping?workflowmap=1&deptId=" + deptId + "&schemeId=" + schemeId + "&workflow=" + workflow + "&groupId=" + groupId + "&officeId=" + officeId + "&pDeptId=" + pDeptId + "&pGroupId=" + pGroupId + "&pOfficeId=" + pOfficeId + "&loginId=" + loginId,
                                                                        type: "POST",
                                                                        async: false,
                                                                        //dataType: "text",
                                                                        success: function (result) {
                                                                            if (parseInt(result) > 0) {
                                                                                alert("Saved Successfully");
                                                                                $('#ddlGroup').change();
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