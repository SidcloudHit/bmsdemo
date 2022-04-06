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
    String userId = "", userName = "", userDes = "", scheme_name = "", dept_name = "";

    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();

        deptId = Integer.parseInt(loginObj.getUserDeptId());
        userName = loginObj.getUserName();
        userDes = loginObj.getUserDesignation();
        roleId = Integer.parseInt(loginObj.getUserRole());
        //officeId = Integer.parseInt(loginObj.getUserOfficeId());
        scheme_name = loginObj.getUserSchemeName();
        dept_name = loginObj.getUserDeptName();

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

        //String ddlOffice = "";
        //ddlOffice = cm.getOffice("-Please Select-");
        String ddlDist = "";
        ddlDist = cm.getDistrict("-Please Select-");

        //String ddlUser = "";
        //ddlUser = cm.getUserList("-Please Select-");
        //String ddlParentOffice = "";
        //ddlParentOffice = cm.getURL("-Please Select-");

        /* manageUserRoleUrl url = new manageUserRoleUrl(map_id);
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
<!--        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/datatables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/css/responsive.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/css/fixedHeader.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">-->
    </head>

    <body>
        <!-- Modal for User List -->
        <div id="modalUser" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Office - Employee Mapping</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light" id="loadModalBody">

                        <div id="mappedUserList">
                            <div class="row" >
                                <div class="form-group col-md-12" >
                                    <h5 id="mappedUser"></h5>
                                </div>
                            </div>
                            <div class="row" >
                                <div class="form-group col-md-12" >
                                    <button type="button" id="btnMapYes" name="btnMapYes" class="btn btn-primary">Yes</button>
                                </div>
                            </div>
                        </div>
                        <div class="d-none" id="newUserMap">
                            <div class="row">
                                <div class="form-group col-md-8">
                                    <label for="lblGroup">User Group</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                        </div>
                                        <select id="ddlGroup" name="ddlGroup" class="form-control" required="required" onchange="dependentDropDown1('ddlDepartment', 'ddlGroup', 'ddlUser', 'fillGroupUsers');">
                                         
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label for="txtDepartment">Select User</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-user fa-lg"></i></span>
                                        </div>
                                        <input type="hidden" id="hdnUpdate" name="hdnUpdate" value="0" />
                                        <select id="ddlUser" name="ddlUser" class="form-control" required="required">

                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <button type="button" id="btnMap" name="btnMap" class="btn btn-primary">Map</button>
                                </div>
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

        <!-- Modal for Mapped Office List -->
        <div id="modalOffice" class="modal hide fade" role="dialog" aria-labelledby=modalOfficeLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Mapped Office List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="input-group mb-3 col-md-12">
                                <table id="mappedList" class="table table-sm table-striped table-bordered" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Sl No.</th>
                                            <th>User Name</th>
                                            <th>Office</th>
                                            <!--  <th>Role</th>-->
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
                                    <h6 class="text-white">Department: <%= dept_name%></h6>
                                    <h6 class="text-white">Scheme: <%= scheme_name%></h6>
                                </div>

                                <div class="card-body bg-light">

                                    <div class="row"> 
                                        <div class="col-md-5">
                                            <form id="frmUserRoleUrl" action="../ManageRoleBasedOffice" method="post" > 
                                                <input type="hidden" id="ddlDepartment" name="ddlDepartment" value="<%= deptId%>" />
                                                <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="0" />
<!--                                                <input type="hidden" id="hdnGroupId" name="hdnGroupId" value="0" />-->
                                                <input type="hidden" id="hdnOfficeId" name="hdnOfficeId" value="0" />
                                                <!--                                                <div class="row">
                                                                                                    <div class="form-group col-md-8">
                                                                                                        <label for="txtDepartment">User Department</label>
                                                                                                        <div class="input-group mb-3">
                                                                                                            <div class="input-group-prepend">
                                                                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                                                                            </div>
                                                                                                            <input type="hidden" id="hdnDeptId" name="hdnDeptId" value="0" />
                                                                                                            <input type="hidden" id="hdnGroupId" name="hdnGroupId" value="0" />
                                                                                                            <input type="hidden" id="hdnOfficeId" name="hdnOfficeId" value="0" />
                                                                                                            <select id="ddlDepartment" name="ddlDepartment" class="form-control" required="required" onchange="dependentDropDown('ddlDepartment', 'ddlGroup', 'fillGroup');">
                                                <%=ddlDept%>
                                            </select>
                                        </div>
                                    </div>
                                </div>-->
                                                <!--                                                <div class="row">
                                                                                                    <div class="form-group col-md-8">
                                                                                                        <label for="lblGroup">User Group</label>
                                                                                                        <div class="input-group mb-3">
                                                                                                            <div class="input-group-prepend">
                                                                                                                <span class="input-group-text" id="basic-addon1"><i class=""></i></span>
                                                                                                            </div>
                                                                                                            <select id="ddlGroup" name="ddlGroup" class="form-control" required="required"  onchange="dependentDropDown1('ddlDepartment', 'ddlGroup', 'ddlUser', 'fillGroupUsers');">
                                                                                                                onchange="dependentDropDown1('ddlDepartment', 'ddlRole', 'ddlScheme', 'fillScheme');"
                                                                                                                dependentDropDown1('ddlRole', 'ddlDepartment', 'ddlParentOffice', 'fillParentOffice');
                                                                                                            </select>
                                                                                                        </div>
                                                                                                    </div>
                                                                                                </div>-->
                                                <!-- <div class="row">
                                                     <div class="form-group col-md-8">
                                                         <label for="txtURL">Office Name</label>
                                                         <div class="input-group mb-3">
                                                             <div class="input-group-prepend">
                                                                 <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                                             </div>
                                                             <input type="text"  id="txtOfficeName" name="txtOfficeName" size="255" class="form-control form-control-sm" />
                                                         </div>
                                                     </div>
                                                 </div>-->
                                                <div class="row">
                                                    <div class="form-group col-md-8">
                                                        <label for="ddlOfficeName">Office Name</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <!--    <select id="ddlOfficeName" name="ddlOfficeName" class="form-control" required="required" >
                                                            
                                                        </select>-->
                                                            <input type="text"  id="txtOfficeName" name="txtOfficeName" size="255" class="form-control form-control-sm" />
                                                        </div>
                                                    </div>
                                                </div>



                                                <!--                                                <div class="row">
                                                                                                    <div class="form-group col-md-8">
                                                                                                        <div class="input-group mb-3">
                                                                                                            <div class="input-group mb-3">
                                                                                                                <input class="form-check-input form-control-sm" type="checkbox" value="" id="LGDMapping" name="LGDMapping" style="margin-left:5px;">
                                                                                                                <label class="form-check-label" for="flexCheckDefault" style="margin-left:25px;">
                                                                                                                    LGD Mapping Required / Not
                                                                                                                </label>
                                                                                                            </div>
                                                
                                                                                                        </div>
                                                                                                    </div>
                                                                                                </div>-->

                                                <fieldset class="border p-2 form-group col-md-8">
                                                    <legend  class="w-auto">Office Address</legend>
                                                    <div class="row">
                                                        <div class="form-group col-md-5">
                                                            <div class="control-group">
                                                                <label class="control-label input-label" for="District">District :</label>
                                                                <div class="input-group mb-3">
                                                                    <select id="ddlDistrict1" name="ddlDistrict1" required="" class="form-control form-control-sm" onchange="resetBlock(1);
                                                                            setSelectedName(this.id);
                                                                            dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSubDiv');
                                                                            ">
                                                                        <%=ddlDist%>                                                    
                                                                    </select>
                                                                    <input type="hidden" id="ddlDistrict1Name" name="ddlDistrict1Name" value="" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <div class="control-group">
                                                                <label class="control-label input-label" for="Sub-Division">Sub-Division :</label>
                                                                <div class="input-group mb-3">
                                                                    <select id="ddlSubDiv1" name="ddlSubDiv1" class="form-control form-control-sm" onchange="setSelectedName(this.id);
                                                                            resetBlock(2);
                                                                            dependentDropDown('ddlSubDiv1', 'ddlUrbRurBody', 'fillBlock');">
                                                                        <option value="0">-Please Select-</option>                                                 
                                                                    </select>
                                                                    <input type="hidden" id="ddlSubDiv1Name" name="ddlSubDiv1Name" value="" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-md-5">
                                                            <div class="control-group">
                                                                <label class="control-label input-label" for="ddlUrbRurBody">MC/Block/ADC Block :</label>
                                                                <div class="input-group mb-3">
                                                                    <select id="ddlUrbRurBody" name="ddlUrbRurBody" class="form-control form-control-sm" onchange="setSelectedName(this.id);
                                                                            resetBlock(3);
                                                                            dependentDropDown('ddlUrbRurBody', 'ddlWardGp', 'fillGp');" >
                                                                        <option value="0">-Please Select-</option>                                                 
                                                                    </select>
                                                                    <input type="hidden" id="ddlUrbRurBodyName" name="ddlUrbRurBodyName" value="" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-md-6">
                                                            <div class="control-group">
                                                                <label class="control-label input-label" for="ddlWardGp">Ward/GP/VC :</label>
                                                                <div class="input-group mb-3">
                                                                    <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" onchange="setSelectedName(this.id);">
                                                                        <option value="0">-Please Select-</option>                                                 
                                                                    </select>
                                                                    <input type="hidden" id="ddlWardGpName" name="ddlWardGpName" value="" />
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-md-8">
                                                            <label for="txtLastMileAddress">Last Mile Address</label>
                                                            <div class="input-group mb-3">
                                                                <div class="input-group-prepend">
                                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                                </div>
                                                                <input type="text"  id="txtLastMileAddress" name="txtLastMileAddress" size="255" class="form-control form-control-sm" />
                                                            </div>
                                                        </div>
                                                    </div>


                                                </fieldset>

                                                <div class="row">
                                                    <div class="form-group col-sm-12">
                                                        <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                        <a href="${pageContext.request.contextPath}/Admin/manageDeptUserGroup.jsp" class="btn btn-primary">Previous</a>
                                                        <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                    </div>
                                                </div>

                                            </form>
                                        </div>
                                        <div class="col-md-7">
                                            <div class="row d-none" id="officeListDiv">                                        
                                                <div class="input-group mb-3">
                                                    <table id="officeList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                        <thead>
                                                            <tr> 
                                                                <th>Sl. No.</th>
                                                                <th>Department</th>                                 
                                                                <!--                                                                <th>Group</th>                                                -->
                                                                <th>Office Name</th>  
                                                                <!--  <th>Parent Office Name</th>       -->                                                     
                                                                <th>Edit</th> 
                                                                <th>Map Office -  Employee </th>
                                                                <!--<th>View Mapped User</th> -->   
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
                                                                            $('#officeListDiv').removeClass("d-none").addClass("d-block");
                                                                            $('#officeList').DataTable().clear().destroy();
                                                                            var deptId = $('#ddlDepartment').val();
                                                                            //  var groupId = $('#ddlGroup option:selected').val();
                                                                            var k = 0;

                                                                            table = $('#officeList').DataTable({
                                                                                "ajax": {
                                                                                    "url": "${pageContext.request.contextPath}/ManageRoleBasedOffice?officeList=1&deptId=" + deptId,
                                                                                    "type": "POST"
                                                                                },
                                                                                "iDisplayLength": 15,
                                                                                'order': [[1, 'asc']],
                                                                                'responsive': true,
                                                                                "columns": [
                                                                                    {"data": function (data, type, dataToSet) {
                                                                                            return  ++k;
                                                                                        }, "width": "5%"},
                                                                                    {"data": "department_name"},
                                                                                    //{"data": "group_name"},
                                                                                    {"data": "office_name"},
                                                                                    {"data": function (data, type, dataToSet) {
                                                                                            return  "<button class=\"btn btn-primary btn-sm\" id=\"btnEditOffice\" name=\"btnEditOffice\" value=\"" + data.office_id + "\")>Edit</button>";
                                                                                        }, "width": "20%"},
                                                                                    {"data": function (data, type, dataToSet) {
                                                                                            return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMapUser\" name=\"btnMapUser\" value=\"" + data.department_id + "#" + data.office_id + "\">Map User</button>";
                                                                                        }, "width": "20%"}
                                                                                    /*,
                                                                                     {"data": function (data, type, dataToSet) {
                                                                                     return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMappedUser\" name=\"btnMappedUser\" value=\"" + data.department_id + "#" + data.group_id + "#" + data.office_id + "\">Mapped User</button>";
                                                                                     }, "width": "20%"}*/
                                                                                ],

                                                                            });

                                                                            $('#officeList_wrapper').addClass("col-md-12");
                                                                            $("select[name='officeList_length']").removeClass("custom-select custom-select-sm ");



                                                                            //   $.ajax({
                                                                            //   url: "${pageContext.request.contextPath}/AjaxServlet?officeList=" + deptId + "&groupId=" + groupId,
                                                                            //   type: "POST",
                                                                            //   dataType: "text",
                                                                            //   success: function (result) {
                                                                            //    $("#officeListDiv").removeClass("d-none");
                                                                            //   $("#officeList tbody").empty();
                                                                            //   $("#officeList tbody").append(result);
                                                                            //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                                            //   }
                                                                            //  });
                                                                        });

                                                                        $(document).ready(function () {
                                                                             dependentDropDown('ddlDepartment', 'ddlGroup', 'fillGroup');
                                                                            var table;
//                                                                            $("#ddlGroup").on('change', function (e) {
//                                                                              
//                                                                            });


                                                                            $('#officeList tbody').on('click', 'button', function (e) {
                                                                                var btnName = this.name;

                                                                                if (btnName === "btnEditOffice") {
                                                                                    var officeId = this.value;
                                                                                    var edit = "0";
                                                                                    $.ajax({
                                                                                        url: "${pageContext.request.contextPath}/AjaxServlet?editOffice=1&officeId=" + officeId,
                                                                                        type: "POST",
                                                                                        dataType: "text",
                                                                                        success: function (result) {
                                                                                            var objJSON = JSON.parse(result);
                                                                                            if (typeof objJSON === 'object') {
                                                                                                edit = "1";
                                                                                                //sl_no, dept_id, role_id, office_id, office_name, parent_office_id
                                                                                                $('#hdnEdit').val(edit);
                                                                                                $('#hdnOfficeId').val(objJSON['office_id']);
                                                                                                $('#ddlDepartment').val(objJSON['department_id']);
                                                                                                // dependentDropDown('ddlDepartment', 'ddlGroup', 'fillGroup');
                                                                                                // $('#ddlGroup').val(objJSON['group_id']);
                                                                                                //dependentDropDown1('ddlRole', 'ddlDepartment', 'ddlParentOffice', 'fillParentOffice');
                                                                                                $('#txtOfficeName').val(objJSON['office_name']);
                                                                                                //$('#ddlOfficeName').val(objJSON['office_id']);
                                                                                                // $('#ddlParentOffice').val(objJSON['parent_office_id']);
                                                                                                if (objJSON['lgd_mapping_required'] == 't') {
                                                                                                    $('#LGDMapping').prop('checked', true);
                                                                                                } else if (objJSON['lgd_mapping_required'] == 'f') {
                                                                                                    $('#LGDMapping').prop('checked', false);
                                                                                                }

                                                                                                $('#ddlDistrict1').val(objJSON['dist_code']);
                                                                                                resetBlock(1);
                                                                                                dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSubDiv');
                                                                                                $('#ddlSubDiv1').val(objJSON['subdiv_code']);
                                                                                                resetBlock(2);
                                                                                                dependentDropDown('ddlSubDiv1', 'ddlUrbRurBody', 'fillBlock');
                                                                                                $('#ddlUrbRurBody').val(objJSON['block_code']);
                                                                                                resetBlock(3);
                                                                                                dependentDropDown('ddlUrbRurBody', 'ddlWardGp', 'fillGp');
                                                                                                $('#ddlWardGp').val(objJSON['ward_code']);
                                                                                                $('#txtLastMileAddress').val(objJSON['last_mile_address']);
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else if (btnName === "btnMapUser") {
                                                                                    $('#hdnDeptId').val(this.value.split("#")[0]);
                                                                                    // $('#hdnGroupId').val(this.value.split("#")[1]);
                                                                                    $('#hdnOfficeId').val(this.value.split("#")[1]);

                                                                                    var officeId = $("#hdnOfficeId").val();
                                                                                    $.ajax({
                                                                                        // url: "${pageContext.request.contextPath}/ManageRoleBasedOffice?checkUser=1&officeId=" + officeId + "&userId=" +userId,
                                                                                        url: "${pageContext.request.contextPath}/ManageRoleBasedOffice?checkUser=1&officeId=" + officeId,
                                                                                        type: "POST",
                                                                                        async: false,
                                                                                        dataType: "text",
                                                                                        success: function (result) {
                                                                                            if (result != '') {
                                                                                                var objJSON = JSON.parse(result);

                                                                                                if (typeof objJSON === 'object') {
                                                                                                    // alert("This User is already assign for this office...");
                                                                                                    $('#mappedUser').empty();
                                                                                                    $('#modalUser').modal("show");
                                                                                                    $('#newUserMap').removeClass("d-block").addClass("d-none");
                                                                                                    //$('#btnMap').removeClass("d-block").addClass("d-none");
                                                                                                    $('#mappedUser').append("Employee Name - \"" + objJSON['emp_name'] + "\"  is mapped with this Office.. Do You want to Change?");
                                                                                                    //$('#btnMapYes').css("display", "block");
                                                                                                    $('#hdnUpdate').val("1");

                                                                                                }
                                                                                            } else {
                                                                                                $('#btnMapYes').click();
                                                                                                $('#btnMapYes').css('display', 'none');
                                                                                                $('#newUserMap').removeClass("d-block").addClass("d-none");
                                                                                                $('#mappedUser').empty();
                                                                                                $('#modalUser').modal("show");
                                                                                                //$('#btnMapYes').css("display", "none");
                                                                                                $('#newUserMap').removeClass("d-none").addClass("d-block");
                                                                                                //$('#btnMap').removeClass("d-none").addClass("d-block");
                                                                                                $('#hdnUpdate').val("0");
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                } /*else if (btnName === "btnMappedUser") {
                                                                                 $("#mappedList tbody").empty();
                                                                                 var k = 0;
                                                                                 
                                                                                 var deptId = this.value.split("#")[0];
                                                                                 var groupId = this.value.split("#")[1];
                                                                                 
                                                                                 $('#mappedList').DataTable().clear().destroy();
                                                                                 table2 = $('#mappedList').DataTable({
                                                                                 "ajax": {
                                                                                 url: "${pageContext.request.contextPath}/ManageRoleBasedOffice?mappedList=1&groupId=" + groupId + "&deptId=" + deptId,
                                                                                 "type": "POST"
                                                                                 },
                                                                                 "iDisplayLength": 15,
                                                                                 'order': [[1, 'asc']],
                                                                                 'responsive': true,
                                                                                 "columns": [
                                                                                 {"data": function (data, type, dataToSet) {
                                                                                 return  ++k;
                                                                                 }, "width": "5%"},
                                                                                 {"data": "emp_name", },
                                                                                 {"data": "office_name"}
                                                                                 //{"data": "role_name"}
                                                                                 ]
                                                                                 
                                                                                 
                                                                                 });
                                                                                 $('#mappedList_wrapper').addClass("col-md-12");
                                                                                 $("select[name='mappedList_length']").removeClass("custom-select custom-select-sm ");
                                                                                 
                                                                                 $('#modalOffice').modal("show");
                                                                                 }*/
                                                                            });

                                                                            $('#mappedUserList div').on('click', 'button', function (e) {
                                                                                var btnName = this.name;

                                                                                if (btnName === "btnMapYes") {
                                                                                    $('#newUserMap').removeClass("d-none").addClass("d-block");
                                                                                    //$('#mappedUserList').removeClass("d-none").addClass("d-block");
                                                                                }

//                                                                                if (btnName === "btnMap") {
//                                                                                    alert($("#hdnUpdate").val());
//                                                                                    var update = $("#hdnUpdate").val();
//                                                                                    var deptId = $("#hdnDeptId").val();
//                                                                                    var groupId = $("#hdnGroupId").val();
//                                                                                    var officeId = $("#hdnOfficeId").val();
//                                                                                    var userId = $('#ddlUser option:selected').val();
//
//                                                                                    $.ajax({
//                                                                                        url: "${pageContext.request.contextPath}/ManageRoleBasedOffice?MapUserOffice=" + update + "&officeId=" + officeId + "&groupId=" + groupId + "&deptId=" + deptId + "&userId=" + userId,
//                                                                                        type: "POST",
//                                                                                        async: false,
//                                                                                        dataType: "text",
//                                                                                        success: function (result) {
//                                                                                            if (parseInt(result) > 0) {
//                                                                                                $("select#ddlUser").prop('selectedIndex', 0);
//                                                                                                $('#modalUser').modal("hide");
//                                                                                            }
//                                                                                        }
//                                                                                    });
//                                                                                }
                                                                            });






                                                                        });
                                                                        $('#btnMap').click(function () {
                                                                            //alert($("#hdnUpdate").val());
                                                                            var update = $("#hdnUpdate").val();
                                                                            var deptId = $("#hdnDeptId").val();
                                                                            var groupId = $('#ddlGroup option:selected').val();
                                                                            var officeId = $("#hdnOfficeId").val();
                                                                            var userId = $('#ddlUser option:selected').val();

                                                                            $.ajax({
                                                                                url: "${pageContext.request.contextPath}/ManageRoleBasedOffice?MapUserOffice=" + update + "&officeId=" + officeId + "&groupId=" + groupId + "&deptId=" + deptId + "&userId=" + userId,
                                                                                type: "POST",
                                                                                async: false,
                                                                                dataType: "text",
                                                                                success: function (result) {
                                                                                    if (parseInt(result) > 0) {
                                                                                        $("select#ddlUser").prop('selectedIndex', 0);
                                                                                        $('#modalUser').modal("hide");
                                                                                    }
                                                                                }
                                                                            });

                                                                        });
                                                                        



</script> 
<%

    } else {
        response.sendRedirect("../login.jsp");
    }
%>
