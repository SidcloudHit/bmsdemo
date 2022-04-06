<%-- 
    Document   : userSettings
    Created on : Sep 30, 2021, 1:44:07 PM
    Author     : NIC-Arindam
--%>

<%@page import="User.UserSettingsClass"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="User.UserSettings"%>
<%@page import="Common.CommonMethod"%>
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
    int roleId = 0;
    String userId = "";
    String ddlSchemes = "";
    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();
        if (loginObj.getUserRole() != null) {
            roleId = Integer.parseInt(loginObj.getUserRole());
        }

    }

    if (!userId.equals("")) {
        if (roleId == 0) {

            //CommonMethod cm = new CommonMethod();
            UserSettingsClass settings = new UserSettingsClass();

            ddlSchemes = settings.getUserSchemeList(userId, "-- Please Select Scheme --");

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

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Select Scheme</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/datatables.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/dataTables.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/css/responsive.bootstrap4.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/css/fixedHeader.bootstrap4.min.css">
        <!--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">-->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">
        <!-- Custom CSS -->




        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row">
                    <div style="width: 0;height: 0;"> </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-12">
                            <%                        if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>
                            <div class="card ml-0">
                                <div class="card-header bg-info">
                                    <h5 class="text-white">User Scheme Selection</h5>
                                </div>
                                <div class="card-body bg-light"> <div class="row">
                                        <div class="form-group col-md-3">
                                            <label for="ddlSchemes">Select Scheme</label>
                                            <div class="input-group mb-3">                                                                                
                                                <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                                    <%=ddlSchemes%>                                                    
                                                </select>
                                                <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                            </div>
                                        </div> 
                                    </div>

                                    <div id="userSchemeListDiv" class="row d-none">
                                        <div class="col-md-12 col-sm-12 col-lg-12 m-auto">

                                            <table id="userSchemeList" class="table table-striped table-bordered dt-responsive nowrap" style="width:100%">
                                                <thead>
                                                    <tr>
                                                        <th>Scheme Name</th>
                                                        <th>Role</th>
                                                        <th>Office Name</th>                                                            
                                                        <th>Select</th>
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


            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>
    </html>
    <!-- jQuery library -->
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>    

<script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>
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
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.rowsGroup.min.js"></script>
<script>
                                                    $(document).ready(function () {
                                                        var table;
                                                        var i = 1;

                                                        $("#ddlSchemes").on('change', function (e) {
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            if (schemeId === "") {
                                                                $('#userSchemeListDiv').removeClass("d-block").addClass("d-none");
                                                            } else {
                                                                $('#userSchemeListDiv').removeClass("d-none").addClass("d-block");
                                                            }
                                                            //$('#userSchemeListDiv').removeClass("d-none").addClass("d-block");

                                                            $('#userSchemeList').DataTable().clear().destroy();
                                                            table = $('#userSchemeList').DataTable({
                                                                "ajax": {
                                                                    url: "${pageContext.request.contextPath}/UserSettings?getUserSchemes=1&schemeId=" + schemeId,
                                                                    "type": "POST"
                                                                },
                                                                "iDisplayLength": 10,
                                                                'order': [[1, 'asc']],
                                                                'responsive': true,
//            "bRetrieve": true,
//            "bJQueryUI": true,
//            "bSort": false,
//                                                                "rowsGroup": [0, 1],
                                                                "columns": [

                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  "<input type=\"hidden\" id=\"hdnSchemeId_" + i + "\" name=\"hdnSchemeId_" + i + "\" value=\"" + data.scheme_id + "\" />" + data.scheme_name;
                                                                            i++;
                                                                        }, "width": "40%"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  "<input type=\"hidden\" id=\"hdnRoleId_" + i + "\" name=\"hdnRoleId_" + i + "\" value=\"" + data.role_id + "\" />" + data.role_name;
                                                                        }, "width": "30%"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            return  "<input type=\"hidden\" id=\"hdnOfficeId_" + i + "\" name=\"hdnOfficeId_" + i + "\" value=\"" + data.office_id + "\" />" + data.office_name;
                                                                        }, "width": "10%"},
                                                                    {"data": function (data, type, dataToSet) {

                                                                            if (parseInt(data.workflow) > 0) {
                                                                                var params = data.department_id + "~" + data.scheme_id + "~" + data.role_id + "~" + data.role_name + "~" + data.office_id;
                                                                                return  "<button class='btn btn-primary btn-sm' value='" + params + "'>Select</button>";
                                                                            } else {
                                                                                return "No Workflow Created";
                                                                            }

                                                                        }, "width": "20%"}
                                                                ]
                                                            });
                                                            $('#userSchemeList_wrapper').addClass("col-md-12");
                                                            $("select[name='userSchemeList_length']").removeClass("custom-select custom-select-sm ");
                                                        });


                                                        $('#userSchemeList tbody').on('click', 'button', function (e) {
                                                            if (this.value !== '') {
//                console.log(table.row($(this).parents('tr')).data());
                                                                var rowData = this.value.split("~");//table.row($(this).parents('tr')).data();
                                                                //console.log(rowData[0] + "~" + rowData[1] + "~" + rowData[2] + "~" + rowData[3] + "~" + rowData[4]);
//                if (parseInt(rowData['scheme_id']) > 0) {
//                    $.redirect("${pageContext.request.contextPath}/User/userHome.jsp?deptId=" + rowData['department_id'] + "&schemeId=" + rowData['scheme_id'] + "&roleId=" + rowData['role_id'] + "&roleName=" + rowData['role_name'] + "&officeId=" + rowData['office_id'] + "", null, "POST", "_top", null, true);
//                }
                                                                if (parseInt(rowData[1]) > 0) {

                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/saveSchemeManagementData?rolewisedatecheck=1&scheme_id=" + rowData[1] + "&dept_id=" + rowData[0] + "&roleId=" + rowData[2],
                                                                        type: "POST",
                                                                        success: function (data) {
                                                                            var today = new Date();
                                                                            var dd = String(today.getDate()).padStart(2, '0');
                                                                            var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                                                                            var yyyy = today.getFullYear();

                                                                            today = yyyy + '-' + mm + '-' + dd;
                                                                            if (data >= today || data === "") {
                                                                                $.redirect("${pageContext.request.contextPath}/User/userHome.jsp?deptId=" + rowData[0] + "&schemeId=" + rowData[1] + "&roleId=" + rowData[2] + "&roleName=" + rowData[3] + "&officeId=" + rowData[4] + "", null, "POST", "_top", null, true);
                                                                            } else {
                                                                                if (rowData[2] === "4") {
                                                                                    alert("Approval date has been expired.");
                                                                                } else if (rowData[2] === "5") {
                                                                                    alert("Verification date has been expired.");
                                                                                } else {
                                                                                    alert("Beneficiary data uploading date has been expired.");
                                                                                }
                                                                            }
                                                                        }

                                                                    });

                                                                }
                                                            }
                                                        });
                                                        
                                                        $('#ddlSchemes').select2();
                                                        
                                                    });

</script>   
<%
        } else {
            response.sendRedirect("userHome.jsp");
        }
    } else {
        response.sendRedirect("../login.jsp");
    }

%>
