<%-- 
    Document   : userReportingStructure
    Created on : 30 May, 2020, 11:02:43 AM
    Author     : acer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
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
    String edit = "0";
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
    String ddlUser = "";
    ddlUser = cm.getUser("-Please Select-");


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Agriculture, Tripura</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <!-- Custom CSS -->
        <link href="${pageContext.request.contextPath}/CSS/startmin.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/bootstrap-theme.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/metisMenu.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/timeline.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/morris.css" rel="stylesheet">
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include> 
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">
                        <form id="frmUserRoleUrl" action="../UserReportingStructure" method="post" > 
                            <div class="card mt-2">
                                <div class="card-header">
                                    <h4 class="text-primary">Mapping User Role:</h4>
                                </div>
                            </div>
                            <div class="card-body bg-form">
                                <div class="row">
                                <% if (!regMsg.equals("")) {
                                %>
                                <div class="alert alert-<%=type%>">
                                    <strong><%=regMsg%></strong>
                                </div>
                                <%
                                    }
                                %>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-3">
                                    <label for="txtDepartment">User Department</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                        </div>
                                        <input type="hidden" id="hdnReportId" name="hdnReportId" value="0" />
                                        <select id="ddlDepartment" name="ddlDepartment" class="form-control" required="required" >
                                            <%=ddlDept%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-3">
                                    <label for="txtUserReportee">User Reportee</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                        </div>
                                        <select id="ddlUserReportee" name="ddlUserReportee" class="form-control" required="required" onchange="checkReportee();">
                                            <%=ddlUser%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-md-3">
                                    <label for="txtUserReporter">User Reporter</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-home fa-lg"></i></span>
                                        </div>
                                        <select id="ddlUserReporter" name="ddlUserReporter" class="form-control" required="required" onchange="checkReportee();">
                                            <%=ddlUser%>
                                        </select>
                                    </div>
                                </div>
                                <!-- <div class="form-group col-md-3">
     <label for="txtReporterJurisdiction">Reporter Jurisdiction</label>
     <div class="input-group mb-3">
         <div class="input-group-prepend">
             <span class="input-group-text" id="basic-addon1"><i class="fa fa-globe fa-lg"></i></span>
         </div>
         <select id="ddlReporterJurisdiction" name="ddlReporterJurisdiction" class="form-control" required="required" >

         </select>
     </div>
                                 </div>-->
                            </div>
                            <div class="row">
                                <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                            </div>
                            <div class="row d-none" id="reoprtListDiv">                                        
                                <div class="input-group mb-3">
                                    <table id="reoprtList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                        <thead>
                                            <tr> 
                                                <th>Sl. No.</th>
                                                 <th>Department</th>
                                                <th>Reportee</th>                                 
                                                <th>Reporter</th>  
                                                <th>Edit</th>  

                                            </tr>
                                        </thead> 
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>


                    </form>
                </div>
            </div>
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

        $.ajax({
            url: "${pageContext.request.contextPath}/AjaxServlet?reportList=1",
            type: "POST",
            dataType: "text",

            success: function (result) {
                $("#reoprtListDiv").removeClass("d-none");
                $("#reoprtList tbody").append(result);
                //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
            }
        });


    });
    function edit_reportList(id) {
        var edit = "0";
        $.ajax({
            url: "${pageContext.request.contextPath}/AjaxServlet?reoprtId=" + id,
            type: "POST",
            dataType: "text",
            success: function (result) {
                var objJSON = JSON.parse(result);
                if (typeof objJSON === 'object') {
                    edit = "1";
                    $('#hdnEdit').val(edit);
                    $('#hdnReportId').val(objJSON['map_id']);
                    $('#ddlDepartment').val(objJSON['department_id']);
                    //dependentDropDown('ddlDepartment', 'ddlRole', 'fillRole');
                    $('#ddlUserReportee').val(objJSON['user_id_reportee']);
                    $('#ddlUserReporter').val(objJSON['user_id_reporter']);
                    // $('#ddlJurisdiction').val(objJSON['jurisdiction_level']);
                }
            }
        });
    }
</script> 
