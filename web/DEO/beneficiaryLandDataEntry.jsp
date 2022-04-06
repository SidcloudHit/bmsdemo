<%-- 
    Document   : beneficiaryLandDataEntry
    Created on : Feb 12, 2020, 4:02:16 PM
    Author     : NIC-Arindam
--%>

<%@page import="Master.Data.MappenLandInfo"%>
<%@page import="Master.Data.DBTBeneficiary"%>
<%@page import="Common.CommonMethod"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        response.sendRedirect("User/userHome.jsp");
    }
    Common.CommonMethod cm = new CommonMethod();
    int edit = 0, benId = 0, landId = 0;
    String ddlDist = "";
    ddlDist = cm.getDistrict("-Please Select-");
    DBTBeneficiary ben = null;
    MappenLandInfo land = null;

    if (request.getParameter("benid") != null) {
        benId = Integer.parseInt(request.getParameter("benid"));
        ben = new DBTBeneficiary(benId);
        //edit = 1;
    } else {
        benId = 1;
        ben = new DBTBeneficiary(benId);
    }

    if (request.getParameter("landid") != null) {
        landId = Integer.parseInt(request.getParameter("landid"));
        land = new MappenLandInfo(landId);
        //edit = 1;
    }

    String regMsg = "";
    String type = "light";

    if (session.getAttribute("RegMsg") != null) {
        regMsg = session.getAttribute("RegMsg").toString();
        session.removeAttribute("RegMsg");
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
        <title>Agriculture, Tripura || Beneficiary mapping with Land Records data</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">

                    <%                        if (!regMsg.equals("")) {
                    %>
                    <div class="alert alert-<%=type%>">
                        <strong><%=regMsg%></strong>
                    </div>
                    <%
                        }
                    %>
                    <div class="card">
                        <div class="card-header">
                            <h4 class="text-primary"> Beneficiary Information:</h4>
                        </div>
                        <div class="card-body bg-page">
                            <div class="row">
                                <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                <input type="hidden" id="hdnBenId" name="hdnBenId" value="<%= benId%>" />
                                <div class="col-md-2 text-primary" >Beneficiary Name:</div><div class="col-md-2 bg-light" > <%= ben.getBeneficiary_name()%></div>
                                <div class="col-md-2 text-primary" >Father's Name:</div><div class="col-md-2 bg-light" > <%=  ben.getFather_name()%></div>
                                <div class="col-md-2 text-primary" >Mobile Number:</div><div class="col-md-2 bg-light" > <%=  ben.getMobile()%></div>
                            </div>

                        </div>
                    </div>
                    <div class="card">
                        <div class="card-header">
                            <h4 class="text-primary">Beneficiary Land Records Information:</h4>
                        </div> 
                        <div class="card-body bg-page">
                            <div class="form-group">                                    
                                <div class="row">
                                    <div class="form-group col-md-2">
                                        <label for="ddlDistrict2">District</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"></span>
                                            </div>
                                            <select id="ddlDistrict2" name="ddlDistrict2" class="form-control form-control-sm" required="required" onchange="dependentDropDown('ddlDistrict2', 'ddlSubDiv2', 'fillSubDiv');">
                                                <%=ddlDist%>                                                    
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="radUr">Sub-Division</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"></span>
                                            </div>
                                            <select id="ddlSubDiv2" name="ddlSubDiv2" class="form-control form-control-sm" required="required" onchange="dependentDropDown('ddlSubDiv2', 'ddlRevCircle', 'fillRevCircle');">
                                                <option value="0">-Please Select-</option>                                                 
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="radUr">Revenue Circle</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"></span>
                                            </div>
                                            <select id="ddlRevCircle" name="ddlRevCircle" class="form-control form-control-sm" required="required" onchange="dependentDropDown('ddlRevCircle', 'ddlTehsil', 'fillTehsil');">
                                                <option value="0">-Please Select-</option>                                                 
                                            </select>
                                        </div> 
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="radUr">Tehsil</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"></span>
                                            </div>
                                            <select id="ddlTehsil" name="ddlTehsil" class="form-control form-control-sm" required="required" onchange="dependentDropDown('ddlTehsil', 'ddlRevVill', 'fillRevVill');">
                                                <option value="0">-Please Select-</option>                                                
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="radUr">Revenue Village / Mouza</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"></span>
                                            </div>
                                            <select id="ddlRevVill" name="ddlRevVill" class="form-control form-control-sm" required="required" >
                                                <option value="0">-Please Select-</option>                                                 
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-1">
                                        <label for="radUr">Khatian No.</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"></span>
                                            </div>
                                            <input type="text" id="txtKhatianNo" name="txtKhatianNo" class="form-control form-control-sm" required="required" />
                                        </div>
                                    </div>
                                    <div class="form-group col-md-1">
                                        <div class="input-group mb-3 mt-4">
                                            <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                                <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="row d-none" id="plotListDiv">                                        
                                    <div class="input-group mb-3">
                                        <table id="plotList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                            <thead>
                                                <tr> 
                                                    <th>Sl. No.</th>
                                                    <th>Plot No.</th>                                 
                                                    <th>Plot Area(in Acre)</th>                                                
                                                    <th>Plot Land Class/ Subclass</th>                                                
                                                    <th>Ownership Type</th>
                                                    <th>Beneficiary Share</th>
                                                    <th>Owners (% Share per Owner) </th>  
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

                    <div class="card d-none" id="landMappedDiv">
                        <div class="card-header">
                            <h4 class="text-primary"> Land Mapped:</h4>
                        </div>
                        <div class="card-body bg-page">
                            <div class="row">
                                <table id="mappedPlotList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                    <thead>
                                        <tr> 
                                            <th>Sl. No.</th>
                                            <th>Plot Location.</th>
                                            <th>Plot No.</th>                                 
                                            <th>Plot Area(in Acre)</th>                                                
                                            <th>Plot Land Class/ Subclass</th>                                                
                                            <th>Ownership Type</th>
                                            <th>Beneficiary Share</th>
                                            <th>Owners (% Share per Owner) </th>  
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
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>
    </html>
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
<script>
                                                $(document).ready(function () {
                                                    var benId = '<%= benId%>';

                                                    if (parseInt(benId) > 0) {
                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/LRDataCheck?searchMapping=1&beneficiary_id=" + benId,
                                                            type: "POST",
                                                            dataType: "text",
                                                            success: function (result) {
                                                                //alert(result);
                                                                $("#landMappedDiv").removeClass("d-none");
                                                                $("#mappedPlotList tbody").empty();
                                                                $("#mappedPlotList tbody").append(result);
                                                                //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                            }
                                                        });
                                                    }

                                                    $('#btnSearch').on('click', function (e) {
                                                        var villCode = $('#ddlRevVill  option:selected').val();
                                                        var khatiyan = $('#txtKhatianNo').val();
                                                        if (villCode === "" && khatiyan === "") {
                                                            alert("Kindly Select Revenue Village /Mouza and Enter Khatiyan No to search");
                                                            return false;
                                                        }

                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/LRDataCheck?searchKhatiyan=1&villCode=" + villCode + "&khatiyan=" + khatiyan,
                                                            type: "POST",
                                                            dataType: "text",
                                                            success: function (result) {
                                                                //alert(result);
                                                                $("#plotListDiv").removeClass("d-none");
                                                                $("#plotList tbody").empty();
                                                                $("#plotList tbody").append(result);
                                                                //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
                                                            }
                                                        });
                                                    });

                                                });
</script>