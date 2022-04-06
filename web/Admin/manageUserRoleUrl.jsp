<%-- 
    Document   : manageUserRoleUrl
    Created on : 30 May, 2020, 10:00:04 AM
    Author     : acer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page import="Master.Data.MasterUserRoleUrl"%>
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
    String userId = "", userName = "", userDes = "", mapping_id = "";

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
        String edit = "0";
        String regMsg = "";
        String logMsg = "";
        String type = "light";
        // String mapping_id = "";

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

        String ddlScheme = "";
        ddlScheme = cm.getSchemesDept(deptId, "-Please Select-");

        String ddlURL = "";
        ddlURL = cm.getURL("-Please Select-");

        String ddlHeaders = "";
        ddlHeaders = cm.getHeaderUrls("-Please Select-");

//        MasterUserRoleUrl map = new MasterUserRoleUrl(mapping_id);
////        
//// manageUserRoleUrl url = new manageUserRoleUrl(map_id);
//            if (map.getMapping_id() != 0) {
//                edit = "1";
//            }
//            int mappingId = map.getMapping_id();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Map User Roles with Application URLs</title>
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
                                    <h4 class="text-white">Mapping User Role with Application URLs:</h4>
                                </div>
                            </div>
                            <div class="card-body bg-light">

                                <div class="row"> 
                                    <div class="col-md-6">
                                        <form id="frmUserRoleUrl" action="../ManageUserRoleURL" method="post" > 
                                            <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="<%= deptId%>" />
                                            <input type="hidden" id="hdnUserId" name="hdnUserId" value="<%= userId%>" />
                                            <input type="hidden" id="hdnMapId" name="hdnMapId" value="" />
                                            <!--<input type="hidden" id="hdnMapId" name="hdnMapId" value="" />-->
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">Select Department</label>
                                                    <div class="input-group mb-3">
                                                        
                                                        <!--<input type="hidden" id="hdnUrlRoleId" name="hdnUrlRoleId" value="" />-->
                                                        <select id="ddlDepartment" name="ddlDepartment" class="form-control form-control-sm" required="required" onchange="dependentDropDownDebug('ddlDepartment', 'ddlScheme', 'fillUrlPermSchemes');">
                                                            <%=ddlDept%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Select Scheme Dorpdown -->

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtDepartment">Select Scheme</label>
                                                    <div class="input-group mb-3">
                                                        

                                                        <select id="ddlScheme" name="ddlScheme" class="form-control form-control-sm" required="required" onchange="dependentDropDown1('ddlDepartment', 'ddlScheme', 'ddlRole', 'fillSchemesOnRoles');" >
                                                            <option value="0">-Please Select-</option>   
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="lblRole">Select Role</label>
                                                    <div class="input-group mb-3">
                                                        
                                                        <select id="ddlRole" name="ddlRole" class="form-control form-control-sm" required="required" >
                                                            <option value="0">-Please Select-</option>   
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Select URL Header Dorpdown -->    

                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtURL">URL Header</label>
                                                    <div class="input-group mb-3">
                                                        
                                                        <select id="ddlHeaders" name="ddlHeaders" class="form-control form-control-sm" required="required" onchange="dependentDropDown('ddlHeaders', 'ddlURL', 'fillHeadersOnUrl');">
                                                            <%=ddlHeaders%>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>




                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtURL">URL</label>
                                                    <div class="input-group mb-3">
                                                        
                                                        <select id="ddlURL" name="ddlURL" class="form-control form-control-sm">
                                                            <option value="0">-Please Select-</option>   
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <!--                                                <div class="form-group col-md-6">
                                                                                                    <label for="radMainPage">Main Page</label>
                                                                                                    <div class="input-group mb-3">
                                                                                                        <div class="radio icheck-material-blue">
                                                                                                            <input type="radio" id="radMainPageYes" name="radMainPage" value="1"  required="required">
                                                                                                            <label for="radMainPageYes"><i class="fa fa-yes">&nbsp;</i> Yes </label>
                                                                                                        </div>
                                                                                                        <div class="radio icheck-material-blue  ml-3">
                                                                                                            <input type="radio" id="radMainPageNo" name="radMainPage" value="0"  required="required">
                                                                                                            <label for="radMainPageNo"><i class="fa fa-female">&nbsp;</i> No </label>
                                                                                                        </div>
                                                                                                    </div>
                                                                                                </div>-->

                                                <div class="form-group col-md-8">
                                                    <label for="radActive">Active</label>
                                                    <div class="input-group mb-3">
                                                        <div class="radio icheck-material-blue">
                                                            <input type="radio" id="radActiveYes" name="radActive" value="1"  required="required">
                                                            <label for="radActiveYes"><i class="fa fa-yes">&nbsp;</i> Yes </label>
                                                        </div>
                                                        <div class="radio icheck-material-blue  ml-3">
                                                            <input type="radio" id="radActiveNo" name="radActive" value="0"  required="required">
                                                            <label for="radActiveNo"><i class="fa fa-female">&nbsp;</i> No </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="form-group col-md-8">
                                                    <label for="txtPriority">URL Priority</label>
                                                    <div class="input-group mb-3">
                                                        
                                                        <input type="number"  id="txtPriority" name="txtPriority" size="2" class="form-control form-control-sm" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group col-sm-12">
                                                    <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                    <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                    <input type="hidden" id="hdnEditHeader" name="hdnEditHeader" value="" />
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="row d-none" id="urlRoleListDiv">                                        
                                            <div class="input-group mb-3">
                                                <table id="urlRoleList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                    <thead>
                                                        <tr> 
                                                            <th>Sl. No.</th>
                                                            <th>Scheme</th>   
                                                            <th>URL</th>  
                                                            <th>User Role</th>    

                                                            <th>Active</th>
                                                            <th>Priority</th>
                                                            <th>Header</th>
                                                            <th></th> 
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
<script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
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



                                                            var table;
                                                            $(document).ready(function () {
                                                                
                                                                $('#ddlDepartment').select2();

                                                                $("#ddlRole").on('change', function (e) {

                                                                    var schemeId = $('#ddlScheme option:selected').val();
                                                                    var roleId = $('#ddlRole option:selected').val();

                                                                    $('#urlRoleListDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#urlRoleList').DataTable().clear().destroy();
                                                                    //var deptId = $('#ddlDepartment').val();
                                                                    // var urlId = $('#urldName').val();
                                                                    //  var groupId = $('#ddlGroup option:selected').val();
                                                                    var k = 0;

                                                                    table = $('#urlRoleList').DataTable({
                                                                        "ajax": {
                                                                            "url": "${pageContext.request.contextPath}/ManageUserRoleURL?urlRoleList=1&schemeId=" + schemeId + "&roleId=" + roleId,
                                                                            "type": "POST"
                                                                        },
                                                                        "iDisplayLength": 15,
                                                                        //'order': [[0, 'asc'], [3, 'desc'], [4, 'desc']],
                                                                        'order': [[1, 'asc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": function (data, type, dataToSet) {
                                                                                    return  ++k;
                                                                                }, "width": "5%"},
                                                                            //{"data": "mapping_id"},
                                                                            {"data": "scheme_name"},
                                                                            {"data": "url_name"},
                                                                            {"data": "role_name"},
                                                                            //
                                                                            {"data": "is_active"},
                                                                            {"data": "priority_no"},
                                                                            {"data": "is_header"},
                                                                            {"data": function (data, type, dataToSet) {
                                                                                    //console.log(data);
                                                                                    //console.log(data[0]);
                                                                                    return  "<button class=\"btn btn-primary btn-sm\" id=\"btnEditOffice\" name=\"btnEditOffice\" onclick=\"edit_urlRoleList(" + data.mapping_id + "," + data.is_header + ")\")>Edit</button>";
                                                                                }, "width": "20%"}
                                                                            /*,
                                                                             {"data": function (data, type, dataToSet) {
                                                                             return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMappedUser\" name=\"btnMappedUser\" value=\"" + data.department_id + "#" + data.group_id + "#" + data.office_id + "\">Mapped User</button>";
                                                                             }, "width": "20%"}*/
                                                                        ]

                                                                    });

                                                                    $('#urlRoleList_wrapper').addClass("col-md-12");
                                                                    $("select[name='urlList_length']").removeClass("custom-select custom-select-sm ");


                                                                });

                                                            });






//                                                    $(document).ready(function () {
//
//                                                        $("#ddlRole").on('change', function (e) {
//                                                            var deptId = $('#ddlDepartment option:selected').val();
//                                                            var roleId = $('#ddlRole option:selected').val();
//
//                                                            $.ajax({
//                                                                url: "${pageContext.request.contextPath}/AjaxServlet?urlRoleList=" + deptId + "&role=" + roleId,
//                                                                type: "POST",
//                                                                dataType: "text",
//                                                                success: function (result) {
//                                                                    $("#urlRoleListDiv").removeClass("d-none");
//                                                                    $("#urlRoleList tbody").empty();
//                                                                    $("#urlRoleList tbody").append(result);
//                                                                    //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
//                                                                }
//                                                            });
//                                                        });
//
//                                                    });

//                                                    function edit_urlRoleList(id) {
//                                                        var edit = "0";
//                                                        $.ajax({
//                                                            url: "${pageContext.request.contextPath}/AjaxServlet?urlRoleId=" + id,
//                                                            type: "POST",
//                                                            dataType: "text",
//                                                            success: function (result) {
//                                                                var objJSON = JSON.parse(result);
//                                                                if (typeof objJSON === 'object') {
//                                                                    edit = "1";
//                                                                    $('#hdnEdit').val(edit);
//                                                                    $('#hdnUrlRoleId').val(objJSON['map_id']);
//                                                                    $('#ddlDepartment').val(objJSON['department_id']);
//                                                                    dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');
//                                                                    $('#ddlRole').val(objJSON['role_id']);
//                                                                    $('#ddlURL').val(objJSON['url_id']);
//                                                                    var mainPg = objJSON['is_main_pg'];
//                                                                    if (mainPg === '0') {
//                                                                        $('#radMainPageYes').prop("checked", true);
//                                                                    } else if (mainPg === '1') {
//                                                                        $('#radMainPageNo').prop("checked", true);
//                                                                    }
//                                                                    var active = objJSON['is_active'];
//                                                                    if (active === '0') {
//                                                                        $('#radActiveYes').prop("checked", true);
//                                                                    } else if (active === '1') {
//                                                                        $('#radActiveNo').prop("checked", true);
//                                                                    }
//                                                                    $('#txtPriority').val(objJSON['priority_no']);
//
//
//                                                                    // $('#ddlJurisdiction').val(objJSON['jurisdiction_level']);
//                                                                }
//                                                            }
//                                                        });
//                                                    }
//                                                    


                                                            function edit_urlRoleList(id, header) {
                                                                var edit = "0";
                                                                //alert(id);
                                                                //alert(header);
                                                                if (parseInt(header) === 1) {
                                                                    $('#ddlURL').removeAttr("required");
                                                                    //$('#ddlURL').prop("readonly", "readonly");
                                                                    $('#ddlURL').prop( "disabled", true ); 
                                                                } else {
                                                                    $('#ddlURL').prop("required", "required");
                                                                    //$('#ddlURL').removeAttr("readonly");
                                                                    $('#ddlURL').prop( "disabled", false ); 
                                                                }

                                                                $.ajax({
                                                                    url: "${pageContext.request.contextPath}/ManageUserRoleURL?urlRoleEdit=" + id,
                                                                    type: "POST",
                                                                    dataType: "text",
                                                                    success: function (result) {
                                                                        var objJSON = JSON.parse(result);
                                                                        
                                                                        if (typeof objJSON === 'object') {
                                                                            edit = "1";
                                                                            $('#hdnEdit').val(edit);
                                                                            $('#hdnEditHeader').val(header);
                                                                            $('#hdnMapId').val(id);
                                                                            var headerId = objJSON.aaData[0].header_id;
                                                                            
                                                                            if (parseInt(headerId) > 0) {
                                                                                $('#ddlHeaders').val(headerId);
                                                                                dependentDropDown('ddlHeaders', 'ddlURL', 'fillHeadersOnUrl');
                                                                                $('#ddlURL').val(objJSON.aaData[0].url_id);
                                                                            } else {
                                                                                $('#ddlHeaders').val(objJSON.aaData[0].url_id);
                                                                                dependentDropDown('ddlHeaders', 'ddlURL', 'fillHeadersOnUrl');
                                                                                $('#ddlURL').val('');
                                                                            }

                                                                            var actv = objJSON.aaData[0].is_active;
                                                                            if ( parseInt(actv) === 1) {
                                                                                $('#radActiveYes').prop("checked", true);
                                                                            } else if (parseInt(actv) === 0) {
                                                                                $('#radActiveNo').prop("checked", true);
                                                                            }
                                                                            $('#txtPriority').val(objJSON.aaData[0].priority_no);

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
