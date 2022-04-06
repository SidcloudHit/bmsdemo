<%-- 
    Document   : uploadRecommendations
    Created on : Jun 7, 2021, 4:44:14 PM
    Author     : NIC-Arindam
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="Master.Data.AdminUser"%>
<%@page import="java.util.Calendar"%>
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
    String message = "";
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
        schemeId = Integer.parseInt(loginObj.getUserScheme());

    }

    if (!userId.equals("")) {

        Common.CommonMethod cm = new CommonMethod();
        int urlId = cm.getUrlId(request.getRequestURI().replace("/bms/", ""));
        if (cm.isUrlAllowed(deptId, roleId, urlId)) {
            //String roleLevel = cm.getUserRoleLevel(usr.getUser_role());
            //JSONArray jsarr = null, jsarr1 = null;
            //JSONObject jsobj = null, jsobj1 = null;
            //jsarr = cm.getUserOfficeDetails(userId,usr.getUser_department_id());

            int edit = 0;
            String ddlScheme = "", ddlDist = "", ddlOffice = "";
            //ddlSchemes = cm.getCashSchemes("-Please Select-");//cm.getSchemes("-Please Select-");            
            ddlScheme = cm.getSchemes("-Please Select-", schemeId);

            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            String finYear = "";
            //System.out.println("Financial month : " + month);
            if (month <= 3) {
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                //finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
            } else {
                finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
                finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                //finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
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
        <title>BMS || Beneficiary Management</title>
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

        <!-- Modal for unsigned files -->
        <div id="myModal" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-esign">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Beneficiary List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="pdfViewDiv" class="bg-light">
                                    <object data="" type="application/pdf" width="100%" height="600">

                                    </object>
                                    <!--<embed src="" type="application/pdf" width="100%" height="700px" />-->
                                </div>                            
                                <div id="eSignForm" class="bg-light d-none">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" id="hdnEsignApplId" name="hdnEsignApplId" value="" />
                        <button type="button" id="btnEsign" class="btn btn-success"><i class="fa fa-save"></i>&nbsp;&nbsp;eSign &nbsp;&nbsp;</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal for Mapped Beneficiary List -->
        <div id="myModal1" class="modal hide fade" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h4 class="modal-title">Beneficiary List</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body bg-light">
                        <div class="row">
                            <div class="input-group mb-3 col-md-12">
                                <table id="itemList" class="table table-sm table-striped table-bordered" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Sl No.</th>
                                            <th>Beneficiary ID</th>
                                            <th>Beneficiary Name</th>
                                            <th>Guardian's Name</th>
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

                        <div class="row">
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
                                <div class="card-header bg-info">
                                    <h4 class="text-white">Verified Beneficiary List:</h4>
                                </div>
                                <div class="card-body bg-light">
                                    <div class="row">
                                        <div class="form-group col-md-3">
                                            <label for="ddlSchemes">Scheme</label>
                                            <div class="input-group mb-3">                                                                                
                                                <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                                    <%=ddlScheme%>                                                    
                                                </select>
                                                <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                            </div>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label for="recomFile">Select File <span class="text-danger-small"><strong>( Max : 2 MB )</strong></span></label>
                                            <div class="input-group mb-3">                                                                                
                                                <input type="file" id="recomFile" name="recomFile" class="form-control form-control-sm" accept="image/jpg,image/jpeg,image/png,application/pdf"  placeholder=""  onchange="return ValidateSize(this, 2048, 'photo');" />                                                                                                  
                                            </div>
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label for="txtRecomFrom">Recommendation From</label>
                                            <div class="input-group mb-3">
                                                <input type="text" id="txtRecomFrom" name="txtRecomFrom" class="form-control form-control-sm" required="required" />
                                            </div> 
                                        </div>
                                        <div class="form-group col-md-2">
                                            <label for="txtRecomBenCount">Beneficiary Count</label>
                                            <div class="input-group mb-3">
                                                <input type="number" id="txtRecomBenCount" name="txtRecomBenCount" class="form-control form-control-sm" required="required" />
                                            </div> 
                                        </div>

                                        <!--<div class="form-group col-md-3  align-self-end">
                                            <div id="progress-wrp">
                                                <div class="progress-bar"></div>
                                                <div class="status">0%</div>
                                            </div>
                                        </div>-->
                                        <div class="form-group col-md-2 align-self-end">
                                            <div class="input-group mb-3">
                                                <input type="hidden" id="hdnOfficeId" value="<%= officeId%>" >
                                                <button type="button" id="btnUpload" name="btnUpload" class="btn btn-warning btn-block">
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i><strong>Upload</strong>
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row d-none" id="fileListDiv">
                                        <div class="col-md-12">                                    
                                            <div class="row">
                                                <div class="input-group mb-3 col-md-12">
                                                    <table id="fileList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                        <thead>
                                                            <tr>
                                                                <th>Scheme Name</th>
                                                                <th>Recommendation From</th>
                                                                <th>Beneficiary Count </th>                                                        
                                                                <th>Uploaded On</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>                                        
                                                    </table>
                                                </div>
                                            </div>                                    
                                        </div>
                                    </div>
                                    <div class="row d-none" id="benMapDiv">
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div id="pdfViewDiv" class="bg-light">
                                                        <object data="" type="application/pdf" width="100%" height="700">

                                                        </object>
                                                        <!--<embed src="" type="application/pdf" width="100%" height="700px" />-->
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="input-group mb-3">
                                                        <table id="beneficiaryList" class="table table-sm table-striped table-bordered" style="width:100%">
                                                            <thead>
                                                                <tr>
                                                                    <th></th>
                                                                    <th>Beneficiary ID</th>
                                                                    <th>Beneficiary Name</th>
                                                                    <th>Guardian's Name</th>                                    
                                                                </tr>
                                                            </thead>                                        
                                                        </table>
                                                    </div>
                                                    <div class="input-group mb-3">
                                                        <button type="button" id="btnMapBeneficiaries" name="btnMapBeneficiaries" class="btn btn-success btn-block">
                                                            <strong>Map Beneficiaries with Recommendation</strong>
                                                        </button>
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
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>
    </html>
    <!-- jQuery library -->
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
<script>
                                                    $(document).ready(function () {

                                                        var table, table1, table2;

                                                        $("#btnUpload").on("click", function (e) {
                                                            //var file = $(this)[0].files[0];
                                                            //console.log(file);
                                                            var file = $('input[type=file]')[0].files[0];
                                                            //console.log(file1);
                                                            var upload = new Upload(file);

                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var recomFrom = $('#txtRecomFrom').val();
                                                            var benCount = $('#txtRecomBenCount').val();

                                                            if (schemeId === "" || recomFrom === "" || benCount === "") {
                                                                alert("Kindly Select Scheme, Recommendation From, Beneficiary Count to upload recommendation file ");
                                                                return false;
                                                            }

                                                            if (!(upload.getSize() > 0)) {
                                                                alert("Kindly Select Recommendation file to upload ");
                                                                return false;
                                                            }
                                                            // maby check size or type here with upload.getSize() and upload.getType()

                                                            // execute upload
                                                            upload.doUpload(schemeId, recomFrom, benCount);
                                                        });
                                                        var Upload = function (file) {
                                                            this.file = file;
                                                        };
                                                        Upload.prototype.getType = function () {
                                                            return this.file.type;
                                                        };
                                                        Upload.prototype.getSize = function () {
                                                            return this.file.size;
                                                        };
                                                        Upload.prototype.getName = function () {
                                                            return this.file.name;
                                                        };
                                                        Upload.prototype.doUpload = function (scheme_id, recom_from, ben_count) {
                                                            var that = this;
                                                            var formData = new FormData();
                                                            // add assoc key values, this will be posts values
                                                            formData.append("file", this.file, this.getName());
                                                            formData.append("upload_file", true);
                                                            console.log(ben_count);
                                                            $.ajax({
                                                                type: "POST",
                                                                url: "../RecommendationFileUpload?recomFileUpload=1&schemeId=" + scheme_id + "&recomFrom=" + recom_from + "&benCount=" + ben_count,
                                                                xhr: function () {
                                                                    var myXhr = $.ajaxSettings.xhr();
                                                                    if (myXhr.upload) {
                                                                        myXhr.upload.addEventListener('progress', that.progressHandling, false);
                                                                    }
                                                                    return myXhr;
                                                                },
                                                                success: function (data) {
                                                                    alert("Uploaded");

                                                                    $('#ddlSchemes').change();
                                                                    // your callback here
                                                                },
                                                                error: function (error) {
                                                                    // handle error
                                                                    alert("Error");
                                                                },
                                                                async: true,
                                                                data: formData,
                                                                cache: false,
                                                                contentType: false,
                                                                processData: false,
                                                                timeout: 60000
                                                            });
                                                        };

                                                        Upload.prototype.progressHandling = function (event) {
                                                            var percent = 0;
                                                            var position = event.loaded || event.position;
                                                            var total = event.total;
                                                            var progress_bar_id = "#progress-wrp";
                                                            if (event.lengthComputable) {
                                                                percent = Math.ceil(position / total * 100);
                                                            }
                                                            // update progressbars classes so it fits your code -- hidden 
                                                            //$(progress_bar_id + " .progress-bar").css("width", +percent + "%");
                                                            //$(progress_bar_id + " .status").text(percent + "%");
                                                        };

                                                        $('#ddlSchemes').select2();

                                                        $("#ddlSchemes").on('change', function (e) {
                                                            var schemeId = $('#ddlSchemes option:selected').val();
                                                            var officeId = $('#hdnOfficeId').val();
                                                            var i = 0;
                                                            $('#fileListDiv').removeClass("d-none").addClass("d-block");
                                                            $('#fileList').DataTable().clear().destroy();
                                                            table = $('#fileList').DataTable({
                                                                "ajax": {
                                                                    url: "${pageContext.request.contextPath}/RecommendationFileUpload?recomFileList=1&schemeId=" + schemeId,
                                                                    "type": "POST"
                                                                },
                                                                "iDisplayLength": 10,
                                                                'order': [[1, 'asc']],
                                                                'responsive': true,
                                                                "columns": [
                                                                    {"data": function (data, type, dataToSet) {
                                                                            i++;
                                                                            return  data.scheme_name;
                                                                        }, "width": "15%"},
                                                                    {"data": "recommendation_from", "width": "15%"},
                                                                    {"data": "beneficiary_count", "width": "15%"},
                                                                    {"data": "upload_date", "width": "15%"},
                                                                    {"data": function (data, type, dataToSet) {
                                                                            //return  "<a href=\"${pageContext.request.contextPath}/ViewFile?fId=" + data.recomendation_file_id + "\" target=\"_blank\"><i class=\"fa fa-file-pdf-o\"></i></a>";

                                                                            if (parseInt(data.recomended_beneficiary) === 0) {
                                                                                return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMapBeneficiary\" name=\"btnMapBeneficiary\" value=\"" + data.recomendation_file_id + "#" + schemeId + "#" + officeId + "#" + data.beneficiary_count + "#" + data.list_id + "\">Map New Beneficiary</button> \n\
                                                                             <button class=\"btn btn-primary btn-sm\" id=\"btnMappedBeneficiary\" name=\"btnMappedBeneficiary\" value=\"" + schemeId + "#" + data.list_id + "\" data-toggle=\"modal\" data-target=\"#myModal1\" class=\"mt-2\">View Mapped Beneficiary</button>";
                                                                            } else if (data.recomended_beneficiary === data.beneficiary_count) {
                                                                                return  "<button class=\"btn btn-primary btn-sm\" id=\"btnMappedBeneficiary\" name=\"btnMappedBeneficiary\" value=\"" + schemeId + "#" + data.list_id + "\" data-toggle=\"modal\" data-target=\"#myModal1\" class=\"mt-2\">View Mapped Beneficiary</button>";
                                                                            }

                                                                        }, "width": "40%"}
                                                                ]

                                                            });
                                                            $('#fileList_wrapper').addClass("col-md-12");
                                                            $("select[name='fileList_length']").removeClass("custom-select custom-select-sm ");


                                                            //$('#btnMapBeneficiary').on('click', function (e) {
                                                            $('#fileList tbody').on('click', 'button', function (e) {
                                                                var btnName = this.name;

                                                                if (btnName === "btnMapBeneficiary") {
                                                                    var fid = this.value.split("#")[0];
                                                                    var schemeId = this.value.split("#")[1];
                                                                    var officeId = this.value.split("#")[2];
                                                                    var benCount = this.value.split("#")[3];
                                                                    var listId = this.value.split("#")[4];
                                                                    var status = 2;


                                                                    var fileUrl = "${pageContext.request.contextPath}/ViewFile?fId=" + fid;
                                                                    $('#benMapDiv').removeClass("d-none").addClass("d-block");
                                                                    $('#pdfViewDiv object').attr('data', fileUrl);

                                                                    $('#beneficiaryList').DataTable().clear().destroy();
                                                                    table1 = $('#beneficiaryList').DataTable({
                                                                        "ajax": {
                                                                            "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?maprecomben=1&officeId=" + officeId + "&schemeId=" + schemeId + "&status=" + status,
                                                                            "type": "POST"
                                                                        },
                                                                        "columnDefs": [
                                                                            {
                                                                                'targets': 0,
                                                                                'className': 'select-checkbox',
                                                                                'checkboxes': {
                                                                                    'selectRow': true
                                                                                }
                                                                            }
                                                                        ],
                                                                        "select": {
                                                                            'style': 'multi'
                                                                        },
                                                                        "iDisplayLength": 15,
                                                                        'order': [[1, 'asc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": "rc_mem", "width": "5%"},
                                                                            {"data": "rc_mem", "width": "20%"},
                                                                            {"data": "full_name", "width": "30%"},
                                                                            {"data": "guardian_name"}
                                                                        ]


                                                                    });
                                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                                    $("select[name='beneficiaryList_length']").removeClass("custom-select custom-select-sm ");

                                                                    $('#btnMapBeneficiaries').on('click', function (e) {

                                                                        //confirmation needs to be taken from user before mapping

                                                                        var rows_selected = table1.column(0).checkboxes.selected();
                                                                        var id = [];
                                                                        $.each(rows_selected, function (index, rowId) {
                                                                            var rowData = rows_selected[index];
                                                                            var b = table1
                                                                                    .rows(function (idx, data, node) {
                                                                                        return data.rc_mem === rowData ? true : false;
                                                                                    })
                                                                                    .data();
                                                                            console.log(b[0]);
                                                                            id.push(b[0]);
                                                                        });

                                                                        if (id.length === 0) {
                                                                            alert("No Beneficiary Selected");
                                                                            return false;
                                                                        } else if (id.length > benCount) {
                                                                            alert("Selected Beneficiary count is larger than the number of recomended beneficiaries");
                                                                            return false;
                                                                        } else if (id.length < benCount) {
                                                                            alert("Selected Beneficiary count is less than the number of recomended beneficiaries. ");
                                                                            return false;
                                                                        }

                                                                        var json = JSON.stringify(id);

                                                                        $.ajax({
                                                                            url: "${pageContext.request.contextPath}/RecommendationFileUpload?recomFileBenMapping=1&schemeId=" + schemeId + "&listId=" + listId,
                                                                            type: "POST",
                                                                            async: false,
                                                                            dataType: 'json',
                                                                            data: {json: json},
                                                                            success: function (result) {
                                                                                console.log(result);
                                                                                if (parseInt(result) > 0) {
                                                                                    alert(result + " No of beneficiaries mapped successfully");

                                                                                    if (parseInt(result) === parseInt(benCount)) {                                                                                        
                                                                                        $('#pdfViewDiv object').attr('data', "");
                                                                                        $('#beneficiaryList').DataTable().clear().destroy();
                                                                                        $('#benMapDiv').removeClass("d-block").addClass("d-none");
                                                                                        
                                                                                    } else if (parseInt(result) < parseInt(benCount)) {
                                                                                        $('#pdfViewDiv object').attr('data', "");
                                                                                        $('#beneficiaryList').DataTable().clear().destroy();
                                                                                        $('#benMapDiv').removeClass("d-block").addClass("d-none");
                                                                                    }
                                                                                }
                                                                                //alert for successfull mapping to be given
                                                                                //reload the beneficiary list 
                                                                                //disable the new beneficiary map button if recommended no of beneficiaries are already mapped

                                                                            }
                                                                        });

                                                                    });
                                                                } else if (btnName === "btnMappedBeneficiary") {
                                                                    //$('#btnMappedBeneficiary').on('click', function (e) {

                                                                    //$('#myModal1').show();
                                                                    $("#itemList tbody").empty();
                                                                    var k = 0;
                                                                    var schemeId = this.value.split("#")[0];
                                                                    var listId = this.value.split("#")[1];

                                                                    $('#itemList').DataTable().clear().destroy();
                                                                    table2 = $('#itemList').DataTable({
                                                                        "ajax": {
                                                                            url: "${pageContext.request.contextPath}/RecommendationFileUpload?mappedBenList=1&schemeId=" + schemeId + "&listId=" + listId,
                                                                            "type": "POST"
                                                                        },
                                                                        "iDisplayLength": 50,
                                                                        'order': [[1, 'asc']],
                                                                        'responsive': true,
                                                                        "columns": [
                                                                            {"data": function (data, type, dataToSet) {
                                                                                    return  ++k;
                                                                                }, "width": "5%"},
                                                                            {"data": "rc_mem", "width": "15%"},
                                                                            {"data": "full_name"},
                                                                            {"data": "guardian_name"}
                                                                        ]


                                                                    });
                                                                    $('#itemList_wrapper').addClass("col-md-12");
                                                                    $("select[name='itemList_length']").removeClass("custom-select custom-select-sm ");

                                                                    $('#myModal1').modal("show");


                                                                    // });
                                                                }


                                                            });





                                                        });

                                                    });


</script>
<%
        } else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>