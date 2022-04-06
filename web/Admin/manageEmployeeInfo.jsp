<%-- 
    Document   : manageEmployeeInfo
    Created on : 27 Jan, 2022, 12:13:35 PM
    Author     : acer
--%>

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
    String userId = "", userName = "", userDes = "", ddlHeaders = "", urlId = "";

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


%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Manage Office Information</title>
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

                                  <div class="card">
                                      <div class="card-header bg-info">
                                          
                                      </div>
                                      <div class="card-body bg-light">
                                          <div class="row"> 
                                              <div class="col-md-12">
                                                  
                                              <form id="frmManageEmployeeInfo" action="../ManageEmployeeInfo" method="post"> 
                                                  

                                                  <div class="card">
                                                      <div class="card-header">Employee Details</div>
                                                       <div class="card-body">
                                                     
                                                          <div class="row">
                                                              <div class="form-group col-md-6 required">
                                                                  <label for="txtInstituteName" class="control-label">Employee Account Number</label>
                                                                  <input id="hiddenUserId" name="hiddenUserId" type="hidden" value=<%= userId%>/>
                                                                
                                                                  <input type="text" id="txtAccount" name="txtAccount" size="120" class="form-control form-control-sm" placeholder="Enter Account Number"  required="required"/>
                                                              </div>
                                                              <div class="form-group col-md-6 required">
                                                                  <label for="txtIfsc" class="control-label">Confirm Account Number</label>
                                                                  <input type="text" id="txtConfirmAccount" name="txtConfirmAccount" class="form-control form-control-sm" placeholder="Confirm Account Number"  required="required"/>
                                                              </div>
                                                          </div>
                                                            <div class="row">
                                                              <div class="form-group col-md-6 required">
                                                                  <label for="txtIfsc" class="control-label"> IFSC Code</label>
                                                                  <input type="text" id="txtIfsc" name="txtIfsc" class="form-control form-control-sm" placeholder="Enter IFSC Code"  required="required"/>
                                                              </div>
                                                              <div class="form-group col-md-6 required">
                                                                  <label for="txtBankName" class="control-label">Bank Name</label>
                                                                  <!--<input type="hidden" id="hdnUserId" name="hdnUserId" value="" />-->
                                                                  <input type="text" id="txtBankName" name="txtBankName" size="120" class="form-control form-control-sm" placeholder="Enter Bank Name"  required="required"/>
                                                              </div>
                                                            
                                                          </div>
                                                         
                                                      </div>
                                                  </div>

                                                  
                                                  <div class="row mt-2">

                                                      <!--<div class="col-md-6" >
                                                          <img id="preview_photo" src="" alt="" height="100px" width="100px" style="border: 1px solid black;" class=" d-none mx-auto"/>
                                                      </div>-->
                                                      <div class="col-md-6 text-right">
                                                          <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                          <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                      </div>
                                                  </div>
                                                  <div class="row invisible">


                                                  </div>
                                              </form>
                                          </div>
                                      </div>
                                      <div class="row">
                                        <div class="col">
                                            <div class="row d-none" id="employeeListDiv">                                        
                                                <div class="input-group mb-3">
                                                    <table id="employeeList" class="table table-striped table-bordered small  dt-responsive nowrap" style="width:100%">
                                                        <thead>
                                                            <tr> 

                                                                <th>Employee ID</th>
                                                                <th>Employee Account Number</th>
                                                                <th>IFSC Code</th>
                                                                                                      
                                                                <th>Bank Name</th> 
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
              </div>
             <jsp:include page="../Web/footer.jsp"></jsp:include>
        </div>
        
        
       
    </body>
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>
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
    <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
</html>
<script>


                                                 
                                                                $(document).ready(function () {
                                                                    
                                                                
                                                                        $("#txtConfirmAccount").focusout(function () {
                                                                            var account = $("#txtAccount").val();
                                                                            var confirmAccount = $("#txtConfirmAccount").val();
                                                                            if (account !== confirmAccount){
                                                                                alert("Account Number Didnot Match!!!!");
                                                                            }
                                                                              

                                                                        });
                                                                        
                                                                        
                                                                        var table;
                                                                        $('#employeeListDiv').removeClass("d-none").addClass("d-block");
                                                                        $('#employeeList').DataTable().clear().destroy();
                                                                        var userId = $('#hiddenUserId').val();

                                                                        table = $('#employeeList').DataTable({
                                                                            "ajax": {
                                                                                url: "${pageContext.request.contextPath}/ManageEmployeeInfo?employeeList=1",
                                                                                "type": "POST"
                                                                            },
                                                                            "iDisplayLength": 10,
                                                                            'order': [[1, 'asc']],
                                                                            'responsive': true,
                                                                            "columns": [
                                                                                {"data": "user_id"},
                                                                                {"data": "account_number"},
                                                                                {"data": "ifsc"},
                                                                                {"data": "bank_name"},

                                                                                {"data": function (data, type, dataToSet) {
                                                                                        //console.log(data);
                                                                                        //console.log(data[0]);
                                                                                        return  "<button class=\"btn btn-primary btn-sm\" id=\"btnEditUser\" name=\"btnEditUser\" value=\"" +data.user_id + "\" )>Edit</button>";
                                                                                    }, "width": "20%"}

                                                                            ],

                                                                        });

                                                                        $('#employeeList_wrapper').addClass("col-md-12");
                                                                        $("select[name='employeeList_length']").removeClass("custom-select custom-select-sm ");

                                                                        $('#employeeList tbody').on('click', 'button', function (e) {
                                                                            var btnName = this.name;
                                                                            //var getTxtValue = this.value; // this gives textbox value   

                                                                            if (btnName === "btnEditUser") {

                                                                                var userId=this.value;
                                                                                //console.log(userId);
                                                                                var edit = "0";


                                                                                $.ajax({
                                                                                    contentType: "application/josn",
                                                                                    type: "GET",
                                                                                    url: "${pageContext.request.contextPath}/ManageEmployeeInfo?userId=" +userId,
                                                                                    dataType: "text",

                                                                                    success: function (result) {
                                                                                         var objJSON = JSON.parse(result);
                                                                                         if (typeof objJSON === 'object') {
                                                                                             edit = "1";
                                                                                             $('#hdnEdit').val(edit);
                                                                                             $('#hiddenUserId').val(objJSON['user_id']);
                                                                                             $('#txtAccount').val(objJSON['account_number']);
                                                                                             $('#txtIfsc').val(objJSON['ifsc']);
                                                                                             $('#txtBankName').val(objJSON['bank_name']);
                                                                                             
                                                                                         }

                                                                                      }



                                                                                });
                                                                            }

                                                                });

                                                         
                                 


                                                                 

                                                               });




</script> 
<%

    } else {
        response.sendRedirect("../index.jsp");
    }
%>