<%-- 
    Document   : matchData
    Created on : Feb 17, 2021, 12:48:39 PM
    Author     : Administrator
--%>



<%@page import="java.util.Base64"%>
<%@page import="Security.SecurityClass"%>
<%@page import="java.security.SecureRandom"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="Common.CommonMethod"%>
<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Match Data </title>
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
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/DataTables/buttons.dataTables.min.css">
        <link rel="stylesheet" type="text/css" href="../DataTables/DataTables-1.10.20/css/jquery.dataTables.min.css">
        <style>
            .form-group.required .control-label:after {
                content:"*";
                color:red;
            }
        </style>
    </head>
    <body>

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
                schemeId = Integer.parseInt(loginObj.getUserScheme());

            }

            if (!userId.equals("")) {

                String regMsg = "";
                if (session.getAttribute("RegMsg") != null) {
                    regMsg = session.getAttribute("RegMsg").toString();
                    session.removeAttribute("RegMsg");
                }
                //AdminUser usr = new AdminUser(userId);

                Common.CommonMethod cm = new CommonMethod();
                String fy = "";

                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                String ddlScheme = "";
                //ddlScheme = cm.getSchemesData("-Please Select-", deptId);
                ddlScheme = cm.getSchemes("-Please Select-", schemeId);
                String finYear = "<option value=\"\">---Please Select---</option>";
                if (month <= 3) {
                    if (fy.equals((year - 1) + "-" + year)) {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                    }
                    if (fy.equals((year - 2) + "-" + (year - 1))) {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\" selected =\"selected\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    }
                    if (fy.equals((year - 3) + "-" + (year - 2))) {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\" selected =\"selected\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    }

                } else {
                    if (fy.equals(year + "-" + (year + 1))) {
                        finYear += "<option value=\"" + year + "-" + (year + 1) + "\" selected =\"selected\">" + year + "-" + (year + 1) + "</option>";
                    } else {
                        finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
                    }
                    if (fy.equals((year - 1) + "-" + year)) {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\" selected =\"selected\">" + (year - 1) + "-" + year + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                    }
                    if (fy.equals((year - 2) + "-" + (year - 1))) {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\" selected =\"selected\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                    }
                    if (fy.equals((year - 3) + "-" + (year - 2))) {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\" selected =\"selected\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    } else {
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    }
                }

        %>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">  

                        <div class="row">
                            <div class="col">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <div class="row"> <span class="col-md-6"><h4 class="text-white">Match Data</h4></span></div>
                                    </div>
                                    <div class="card-body bg-light ">
                                        <form id="frmFileUpload" action="matchData.jsp"  method="post">
                                            <div class="form-group row mx-auto required">
                                                <label for="scheme"  class="col-sm-3 col-form-label control-label font-weight-bold">Select Scheme :</label>
                                                <div class="col-sm-7">
                                                    <select class="js-example-basic-single custom-select mr-sm-2" id="ddlSchemes" name="scheme" required="" >
                                                    <%= ddlScheme%>
                                                </select>
                                            </div>
                                        </div>
                                        <!--   <div class="form-group row mx-auto required">
                                               <label for="FY" class="col-sm-3 col-form-label control-label" >Select FY :</label>
                                               <div class="col-sm-7">
                                                   <select class="custom-select mr-sm-2" id="fy" name="fy" required="">
                                        <%= finYear%>
                                    </select>
                                </div>
                            </div>
                                        -->
                                        <!---
                                        <div class="form-group row mx-auto required">
                                            <input type="hidden" name="installment_name"  id="installment_name" value="" />
                                            <label for="Installment" class="col-sm-3 col-form-label control-label" >Select Installment :</label>
                                            <div class="col-sm-7">
                                                <select class="custom-select mr-sm-2" id="ddlInstallment" name="installment" required="" onchange="selectInstallmentName(this);">
        
                                                </select>
                                            </div>
                                        </div>
                                        -->
                                        <div class="form-group row mx-auto">
                                            <label for="matching" class="col-sm-3 col-form-label control-label font-weight-bold" >Matching Strength :</label>
                                            <div class="col-sm-7">
                                                <select class="custom-select mr-sm-2" id="match_strength" name="match_strength">
                                                    <option value="">---Please Select---</option>
                                                    <option value="1" >90%</option>
                                                    <option value="2" >80%</option>
                                                    <option value="3" >70%</option>
                                                    <option value="4" >60%</option>
                                                    <option value="5" >50%</option>
                                                    <option value="999" >Less Than 50%</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group row mx-auto">
                                            <div class="col-sm-3 col-form-label" ></div>
                                            <div class="col-sm-7">
                                                <button type="button" id="btnSearch"  class="btn btn-primary" >
                                                    <i class="fa fa-search">&nbsp;&nbsp;</i>Search</button>
                                                <!-- <button type="submit" class="btn btn-primary"  onclick="form.action = '../matchDataReport', form.setAttribute('target', '_blank');">View Report As Pdf</button>&nbsp; 
                                                 <button type="submit" class="btn btn-primary"  onclick="form.action = '../matchdataReportexcel', form.setAttribute('target', '_blank');">Download (In Excel)</button>
                                                -->
                                            </div>
                                        </div>
                                    </form>
                                </div>                        
                            </div>
                        </div>                 
                    </div>

                    <div id= "returnMsg">
                        <div id = "msgFail" style="display: none;" class="alert alert-danger alert-dismissible ml-2 mr-2 mt-1 mb-1">

                            <strong>Alert!</strong> <span id="msgfail"></span>
                        </div>

                        <div id = "msgSucccess" style="display: none;" class="alert alert-success alert-dismissible ml-2 mr-2 mt-1 mb-1">

                            <strong>Success!</strong> <span id="msgsuccess"></span>
                        </div>
                    </div>



                    <div class="d-none" id="benListDiv">
                        <div class="table-responsive-sm ">

                            <table id="contenttable" class="table table-striped table-bordered" style="width:100%">
                                <thead class="bg-dark text-white">
                                    <tr >  
                                        <th style="width: 10%">SL. No.</th>
                                        <th style="width: 30%">Name</th>
                                        <th style="width: 30%">RC Validation Details </th>
                                        <th style="width: 10%">Matching Strength</th>
                                        <th style="width: 20%">Action</th>
                                    </tr>
                                </thead>                                        
                            </table>

                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>


            <div id="loader" style="display:none;width:105px;height:150px;border:1px solid black;position:fixed;text-align: center;  top: 40%; right: 0; left: 43%; z-index: 9999999; filter: alpha(opacity=70); opacity: 0.9; background-color: #999 ">
                <img src='../Images/progress.gif' width="100" height="100" /><br>Loading Please Wait..</div>

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

    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.html5.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/buttons.print.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.buttons.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/jszip.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/pdfmake.min.js"></script>
    <script charset="utf8" src="${pageContext.request.contextPath}/DataTables/pdfmake-0.1.36/vfs_fonts.js"></script>

    <script>
        $(document).ready(function () {
            //  selectInstallment($("#ddlSchemes").val());


            $('.js-example-basic-single').select2();
            $(".js-example-basic-single").on("select2:open", function () {
                $(".select2-search__field").attr("placeholder", "Search...");
            });
            $(".js-example-basic-single").on("select2:close", function () {
                $(".select2-search__field").attr("placeholder", null);
            });
            $('#btnSearch').on('click', function (e) {
                var i = 0;
                var schemeid = $('#ddlSchemes option:selected').val();
                var strength = $('#match_strength option:selected').val();
                if (schemeid === "") {
                    alert("Kindly Select Scheme for searching beneficiaries");
                    return false;
                }

                $('#benListDiv').removeClass("d-none").addClass("d-block");
                $('#contenttable').DataTable().clear().destroy();
                var table = $('#contenttable').DataTable({
                    dom: 'Bfrtip',
                    "scrollX": true,
                    "ajax": {
                        "url": "${pageContext.request.contextPath}/AjaxMethod?dbtmatch=1&schemeid=" + schemeid + "&match_strength=" + strength,
                        "type": "POST",
                        "dataSrc": function (data) { //alert(data['aaData']);
                            // console.log(data['aaData']);
                            if (data['aaData'] == "[]") {
                                return [];
                            } else {
                                return data['aaData'];
                            }
                        },
                        beforeSend: function () {
                            $('#loader').show();
                            $(".card").css("opacity", 0.2);
                            $(".card").find("input, button, submit, textarea, select").attr("disabled", "disabled");
                            $("#loader").css({"display": "block"});
                            $("#msgSucccess").css("display", "none");
                            $("#msgFail").css("display", "none");
                        },
                        complete: function () {
                            $('#loader').hide();
                            $(".card").css("opacity", 1.0);
                            $(".card").find("input, button, submit, textarea, select").removeAttr("disabled", "disabled");
                        },
                    },
                    "columnDefs": [
                        {
                            "render": function (data, type, full, meta) {
                                return  meta.row + 1;
                            },
                            targets: 0,
                        },
                        {
                            render: function (data, type, full, meta) {
                                return "<div class='text-wrap'><strong>Name: </strong>" + full['postrcvalidation_fullname'] + " <br>\n\
                              <strong>RC Member ID: </strong>" + full['postrcvalidation_rc_member_id'] + "</div>";
                            },
                            targets: 2,
                        },
                        {
                            render: function (data, type, full, meta) {
                                var strnlen = full['benef_name_matchresult_dist_leven'];
                                var match_percentage = "";
                                if (strnlen == 1) {
                                    match_percentage = "90%";
                                } else if (strnlen == 2) {
                                    match_percentage = "80%";
                                } else if (strnlen == 3) {
                                    match_percentage = "70%";
                                } else if (strnlen == 4) {
                                    match_percentage = "60%";
                                } else if (strnlen == 5) {
                                    match_percentage = "50%";
                                } else {
                                    match_percentage = "Less Than 50%";
                                }
                                return  match_percentage;
                            },
                            targets: 3
                        },
                        {
                            render: function (data, type, full, meta) {
                                return '<input type="hidden"  id="benef_name_matchresult_dist_leven' + meta.row + '" value="' + full['benef_name_matchresult_dist_leven'] + '" />\n\
                                                                    <input type="hidden" id="benef_name' + meta.row + '" value="' + full['full_name'] + '" />\n\
                                                                 <input type="hidden" id="benef_rc_mem' + meta.row + '" value="' + full['postrcvalidation_rc_member_id'] + '" />\n\
                                                                 <button type="button" class="btn btn-primary mb-2 matchdata" value="M~' + full['sl_no'] + '~' + full['scheme_code'] + '~' + full['postrcvalidation_rc_no'] + '~' + full['tempid'] + '~' + schemeid + '">Verify</button>\n\
                                                                 <button type="button" class="btn btn-warning mb-2 pendingdata" value="O~' + full['sl_no'] + '~' + full['scheme_code'] + '~' + full['postrcvalidation_rc_no'] + '~' + full['tempid'] + '~' + schemeid + '">Keep Pending</button>';
                            },
                            targets: 4
                        }
                    ],
                    "select": {
                        'style': 'multi'
                    },
                    // "iDisplayLength": 50,
                    'order': [[0, 'asc']],
                    'responsive': true,
                    "language": {
                        "emptyTable": "No records to display."
                    },
                    "columns": [
                        {"data": "sl"},
                        {"data": "full_name"},
                        {"data": null},
                        {"data": "benef_name_matchresult_dist_leven"},
                        {"data": null}
                    ],
                    "buttons": [
                        'excel',
                        'csv',
                        'print',
                        'pageLength'
                    ],
                    "lengthMenu": [[-1, 10, 25, 50, 100], ["All", 10, 25, 50, 100]]
                });
                $('#contenttable_wrapper').addClass("col-md-12");
                $('#contenttable tbody').unbind().on('click', 'button.matchdata', function () {
                    // get the current row

                    var currentRow = $(this).closest("tr");
                    var col1 = currentRow.find("td:eq(0)").html(); // get current row 1st table cell TD value                    
                    // alert(col1);
                    var row_id = col1 - 1;
                    var benef_name_matchresult_dist_leven = $("#benef_name_matchresult_dist_leven" + row_id).val();
                    var benef_name = $("#benef_name" + row_id).val();
                    var benef_rc_mem = $("#benef_rc_mem" + row_id).val();
                    var r = confirm("Are you sure to verify this benefciary?\n\nName : " + benef_name + "\nRC Member Id : " + benef_rc_mem);
                    if (r == true) {
                        var row = $('#contenttable').DataTable().row($(this).parents('tr'));
                        var match_val = $(this).val();
                        $.ajax({
                            url: "${pageContext.request.contextPath}/saveMatchdata?match_val=" + match_val,
                            type: "POST",
                            beforeSend: function () {
                                $('#loader').show();
                                $(".card").css("opacity", 0.2);
                                $(".card").find("input, button, submit, textarea, select").attr("disabled", "disabled");
                                $("#loader").css({"display": "block"});
                            },
                            complete: function () {
                                $('#loader').hide();
                                $(".card").css("opacity", 1.0);
                                $(".card").find("input, button, submit, textarea, select").removeAttr("disabled", "disabled");
                            },
                            success: function (data) {
                                if (data == "ok") {
                                    $('#contenttable').DataTable().row(row).remove().draw();
                                    // alert("Data Updated Success Fully");
                                    $("#msgSucccess").css("display", "block");
                                    $("#msgsuccess").text("Data Verified Successfully.\nName: " + benef_name + ". \nRC Memberid: " + benef_rc_mem);
                                    $("#msgFail").css("display", "none");
                                    $("#msgfail").text("");
                                    $("html, body").animate({scrollTop: $("#returnMsg").offset().top}, "slow");
                                } else {
                                    // alert("No Data Updated");
                                    $("#msgSucccess").css("display", "none");
                                    $("#msgsuccess").text("");
                                    $("#msgFail").css("display", "block");
                                    $("#msgfail").text("No Data Verified. Try again.");
                                    $("html, body").animate({scrollTop: $("#returnMsg").offset().top}, "slow");
                                }
                            }

                        });
                    } else {
                        txt = "You pressed Cancel!";
                    }

                });
                $('#contenttable tbody').on('click', 'button.pendingdata', function () {

                    // get the current row
                    var currentRow = $(this).closest("tr");
                    var col1 = currentRow.find("td:eq(0)").html(); // get current row 1st table cell TD value                    
                    // alert(col1);

                    var row = $('#contenttable').DataTable().row($(this).parents('tr'));
                    // var row_id = $('#contenttable').DataTable().row($(this).parents('tr')).id();
                    var match_val = $(this).val();
                    var row_id = col1 - 1;
                    var benef_name = $("#benef_name" + row_id).val();
                    var benef_rc_mem = $("#benef_rc_mem" + row_id).val();
                    var r = confirm("Are you sure to keep pending this benefciary?\n\nName : " + benef_name + "\nRC Member Id : " + benef_rc_mem);
                    if (r == true) {
                        $.ajax({
                            url: "${pageContext.request.contextPath}/saveMatchdata?match_val=" + match_val,
                            type: "POST",
                            beforeSend: function () {
                                $('#loader').show();
                                $(".card").css("opacity", 0.2);
                                $(".card").find("input, button, submit, textarea, select").attr("disabled", "disabled");
                                $("#loader").css({"display": "block"});
                            },
                            complete: function () {
                                $('#loader').hide();
                                $(".card").css("opacity", 1.0);
                                $(".card").find("input, button, submit, textarea, select").removeAttr("disabled", "disabled");
                            },
                            success: function (data) {
                                if (data == "ok") {
                                    $('#contenttable').DataTable().row(row).remove().draw();
                                    // alert("Data Updated Success Fully");
                                    $("#msgSucccess").css("display", "block");
                                    $("#msgsuccess").text("Data Updated as Keep Pending Successfully.\nName: " + benef_name + ". \nRC Memberid: " + benef_rc_mem);
                                    $("#msgFail").css("display", "none");
                                    $("#msgfail").text("");
                                    $("html, body").animate({scrollTop: $("#returnMsg").offset().top}, "slow");
                                } else {
                                    // alert("No Data Updated");
                                    $("#msgSucccess").css("display", "none");
                                    $("#msgsuccess").text("");
                                    $("#msgFail").css("display", "block");
                                    $("#msgfail").text("No Data Updated as Keep Pending. Try again");
                                    $("html, body").animate({scrollTop: $("#returnMsg").offset().top}, "slow");
                                }
                            }

                        });
                    } else {
                        txt = "You pressed Cancel!";
                    }

                });
            });
        }
        );
        function getSelectedScheme(sel) {
            document.getElementById("scheme_name").value = sel.options[sel.selectedIndex].text;
            $("#fy").val("");
            $("#installment_id").val("");
            $("#match_strength").val("");
        }
        function selectInstallmentName(sel) {
            document.getElementById("installment_name").value = sel.options[sel.selectedIndex].text;
        }

        function selectInstallment(schemeId) {
            var installment_id = $("#installment_id").val();
            $("#ddlInstallment").empty();
            $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + encodeURIComponent(schemeId),
                type: "POST",
                success: function (data) {
                    console.log(data);
                    if (data.length > 1) {
                        var myObj = JSON.parse(data);
                        console.log(myObj.prd);
                        //alert(data);
                        if (parseInt(myObj.prd) > 0) {
                            var num = parseInt(myObj.prd);
                            var yearly = ['April-March'];
                            var halfyearly = ['April-September', 'October-March'];
                            var triannualy = ['April-July', 'August-November', 'December-March'];
                            var quarterly = ['April-June', 'July-September', 'October-December', 'January-March'];
                            var monthly = ['April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March'];
                            var selArr;
                            if (num === 1) {
                                selArr = yearly;
                            } else if (num === 2) {
                                selArr = halfyearly;
                            } else if (num === 3) {
                                selArr = triannualy;
                            } else if (num === 4) {
                                selArr = quarterly;
                            } else if (num === 12) {
                                selArr = monthly;
                            }
                            //var instOpts = "";
                            for (var i = 1; i <= num; i++) {
                                if (i == installment_id) {
                                    $("#ddlInstallment").append("<option value=\"" + i + "~" + selArr[i - 1] + "\" selected =\"selected\">" + selArr[i - 1] + " </option>");
                                } else {
                                    $("#ddlInstallment").append("<option value=\"" + i + "~" + selArr[i - 1] + "\" >" + selArr[i - 1] + " </option>");
                                }
                            }
                        }
                    }
                }
            })
        }
        function IsNumeric(e) {
            var key = window.e ? e.keyCode : e.which;
            if (e.keyCode === 8 || e.keyCode === 46) {
                return true;
            } else if (key < 48 || key > 57) {
                lblinstallmentError.innerHTML = "Only Numbers are Allowed.";
//alert("Only Numbers are Allowed");
                return false;
            } else {
                lblinstallmentError.innerHTML = "";
                return true;
            }
        }
    </script>
</html>

<%         //   } else {
        //      response.sendRedirect("../unauthorisedmessage.jsp");
        //}
    } else {
        response.sendRedirect("../login.jsp");
    }
%>

