<%-- 
    Document   : manageApplicationUrl
    Created on : 30 May, 2020, 10:10:46 PM
    Author     : acer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page import="Master.Data.MasterApplicationUrl"%>
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
        String url_id = "";

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

        MasterApplicationUrl url = new MasterApplicationUrl(url_id);
        if (url.getUrl() != null) {
            edit = "1";
        }
        ddlHeaders = cm.getHeaderUrls("-- Select --");
        urlId = url.getUrl();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Manage Application URLs</title>
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
    <script>

        window.onload = function () {
            var edit = '<%=edit%>';

            if (parseInt(edit) === 1) {
                document.getElementById("txtURL").value = '<%= url.getUrl()%>';
                document.getElementById("txtUrlName").value = '<%= url.getUrl_name()%>';
                var header = '<%= url.getIs_header()%>';
                if (header === '1') {
                    document.getElementById("radHeaderYes").checked = true;
                } else if (header === '0') {
                    document.getElementById("radHeaderNo").checked = true;
                }

                var active = '<%= url.getIs_active()%>';
                if (active === '1') {
                    document.getElementById("radActiveYes").checked = true;
                } else if (active === '0') {
                    document.getElementById("radActiveNo").checked = true;
                }

            }
        };

    </script>
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
                            <div class="card" >
                                <div class="card-header bg-info">
                                    <h4 class="text-white">Manage Application URL:</h4>
                                </div>

                                <div class="card-body bg-light">                            
                                    <div class="row"> 
                                        <div class="col-md-5">
                                            <form id="frmApplicationUrl" action="../ManageApplicationURL" method="post" >
                                                <input type="hidden" id="urldName" name="urldName" value="<%= urlId%>" />
                                                <div class="row">
                                                    <div class="form-group col-md-8">
                                                        <label for="txtUserName">URL</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <input type="hidden" id="hdnUrlId" name="hdnUrlId" value="0" />
                                                            <input type="text"  id="txtURL" name="txtURL" size="120" class="form-control form-control-sm"  required="required"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-md-8">
                                                        <label for="txtUserName">URL Name</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <input type="text"  id="txtUrlName" name="txtUrlName" size="120" class="form-control form-control-sm"  required="required"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-md-6">
                                                        <label for="radMale">Header</label>
                                                        <div class="input-group mb-3">
                                                            <div class="radio icheck-material-blue">
                                                                <input type="radio" id="radHeaderYes" name="radHeader" value="1"  required="required">
                                                                <label for="radHeaderYes"><i class="fa fa-yes">&nbsp;</i> Yes </label>
                                                            </div>
                                                            <div class="radio icheck-material-blue  ml-3">
                                                                <input type="radio" id="radHeaderNo" name="radHeader" value="0"  required="required">
                                                                <label for="radHeaderNo"><i class="fa fa-female">&nbsp;</i> No </label>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="form-group col-md-6">
                                                        <label for="radMale">Active</label>
                                                        <div class="input-group mb-3">
                                                            <div class="radio icheck-material-blue">
                                                                <input type="radio" id="radActiveYes" name="radActive" value="1"  required="required">
                                                                <label for="radActiveYes"><i class="fa fa-male">&nbsp;</i> Yes </label>
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
                                                        <label for="txtUrlDesc">URL Description</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>
                                                            <input type="text"  id="txtUrlDesc" name="txtUrlDesc" size="500" class="form-control form-control-sm"  required="required"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-md-8">
                                                        <label for="txtUrlDesc">URL Header Name</label>
                                                        <div class="input-group mb-3">
                                                            <div class="input-group-prepend">
                                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                                            </div>

                                                            <select id="ddlHeaders" name="ddlHeaders" class="form-control form-control-sm"  onchange="setSelectedName(this.id);">
                                                                <%=ddlHeaders%>                                                  
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="form-group col-sm-12">
                                                        <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary">Save Details</button>
                                                        <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                        <div class="col-md-7"> 
                                            <div class="row d-none" id="urlListDiv">    

                                                <div class="input-group mb-3">
                                                    <table id="urlList" class="table table-striped table-bordered  dt-responsive nowrap" style="width:100%;">
                                                        <thead>
                                                            <tr> 
                                                                <th>Sl. No.</th>
                                                                <th>URL</th>                                 
                                                                <th>URL Name</th>                                                
                                                                <th>Header</th>  
                                                                <th>Active</th>  
                                                                <th>Edit</th> 
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


                                                                var table;
                                                                $(document).ready(function () {


                                                                    $('#urlListDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#urlList').DataTable().clear().destroy();
                                                                    // var urlId = $('#urldName').val();
                                                                    //  var groupId = $('#ddlGroup option:selected').val();
                                                                    var k = 0;

                                                                    table = $('#urlList').DataTable({
                                                                        "ajax": {
                                                                            "url": "${pageContext.request.contextPath}/ManageApplicationURL?urlList=1",
                                                                            "type": "POST"
                                                                        },
                                                                        "iDisplayLength": 15,
                                                                        'order': [[0, 'asc'], [3, 'desc'], [4, 'desc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": function (data, type, dataToSet) {
                                                                                    return  ++k;
                                                                                }, "width": "5%"},
                                                                            {"data": "url"},
                                                                            {"data": "url_name"},
                                                                            {"data": "is_header"},
                                                                            {"data": "is_active"},
                                                                            {"data": function (data, type, dataToSet) {
                                                                                    //console.log(data);
                                                                                    //console.log(data[0]);
                                                                                    return  "<button class=\"btn btn-primary btn-sm\" id=\"btnEditOffice\" name=\"btnEditOffice\" onclick=\"edit_urlList(" + data.url_id + ")\")>Edit</button>";
                                                                                }, "width": "20%"}
                                                                            /*,
                                                                             {"data": function (data, type, dataToSet) {
                                                                             return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMappedUser\" name=\"btnMappedUser\" value=\"" + data.department_id + "#" + data.group_id + "#" + data.office_id + "\">Mapped User</button>";
                                                                             }, "width": "20%"}*/
                                                                        ]

                                                                    });

                                                                    $('#urlList_wrapper').addClass("col-md-12");
                                                                    $("select[name='urlList_length']").removeClass("custom-select custom-select-sm ");


                                                                    $('input[type=radio][name=radHeader]').on('change', function() {
                                                                        if ($(this).val() === '1') {
                                                                            $('#ddlHeaders').removeAttr('required');
                                                                        } else if ($(this).val() === '0') {
                                                                            console.log("Field Required");
                                                                            $('#ddlHeaders').attr('required', 'required');
                                                                        }
                                                                    });


                                                                });





//        $(document).ready(function () {
//            var table;
//
//            $.ajax({
//                url: "${pageContext.request.contextPath}/AjaxServlet?urlList=1",
//                type: "POST",
//                dataType: "text",
//                success: function (result) {
//                    $("#urlListDiv").removeClass("d-none");
//                    $("#urlList tbody").append(result);
//                    //$("input[type='radio']").attr("checked","checked").checkboxradio("refresh");
//                    /*table = $('#urlList').DataTable({
//                     "bFilter": true,
//                     "bRetrieve": true,
//                     "bJQueryUI": true,
//                     "iDisplayLength": 10,
//                     "order": [[1, 'asc']],
//                     "responsive": true,
//                     "sPaginationType": "full_numbers",
//                     "dom": 'Bfrtip'
//                     });*/
//                }
//            });


                                                                /*$('#urlListDiv').removeClass("d-none").addClass("d-block");
                                                                 $('#urlList').DataTable().clear().destroy();
                                                                 table = $('#urlList').DataTable({
                                                                 "ajax": {
                                                                 url: "${pageContext.request.contextPath}/AjaxServlet?urlList=1",
                                                                 type: "POST"
                                                                 },
                                                                 "iDisplayLength": 10,
                                                                 'order': [[1, 'asc']],
                                                                 'responsive': true
                                                                 });*/




                                                                //});


                                                                function edit_urlList(id) {
                                                                    var edit = "0";


                                                                    $.ajax({
                                                                        url: "${pageContext.request.contextPath}/AjaxServlet?urlId=" + id,
                                                                        type: "POST",
                                                                        dataType: "text",
                                                                        success: function (result) {
                                                                            var objJSON = JSON.parse(result);
                                                                            if (typeof objJSON === 'object') {
                                                                                edit = "1";
                                                                                $('#hdnEdit').val(edit);
                                                                                $('#hdnUrlId').val(objJSON['url_id']);
                                                                                $('#txtURL').val(objJSON['url']);
                                                                                $('#txtUrlName').val(objJSON['url_name']);
                                                                                //$('#txtUrlName').val(objJSON['url_name']);
                                                                                var headr = objJSON['is_header'];
                                                                                if (headr === '1') {
                                                                                    $('#radHeaderYes').prop("checked", true);
                                                                                } else if (headr === '0') {
                                                                                    $('#radHeaderNo').prop("checked", true);
                                                                                }

                                                                                var actv = objJSON['is_active'];
                                                                                if (actv === '1') {
                                                                                    $('#radActiveYes').prop("checked", true);
                                                                                } else if (actv === '0') {
                                                                                    $('#radActiveNo').prop("checked", true);
                                                                                }
                                                                                $('#txtUrlDesc').val(objJSON['url_desc']);
                                                                                $('#ddlHeaders').val(objJSON['header_id']);
                                                                                // $('#ddlJurisdiction').val(objJSON['jurisdiction_level']);
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
