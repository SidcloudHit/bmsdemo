<%-- 
    Document   : legacydataupload
    Created on : 21 Dec, 2020, 8:48:54 AM
    Author     : Banti
--%>

<%@page import="Common.CommonMethod"%>
<%@page import="java.util.Calendar"%>

<%@page import="Master.Data.AdminUser"%>
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
        <title>BMS || Upload Beneficiary Data </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/select2.min.css">
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
                officeId = Integer.parseInt(loginObj.getUserOfficeId());
                schemeId = Integer.parseInt(loginObj.getUserScheme());
            }

            if (!userId.equals("")) {

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

                //String url = request.getRequestURI().replace("/bmslegacydata/", "");
                //AdminUser usr = new AdminUser(userId);
                //dept_id = usr.getUser_department_id();
                Common.CommonMethod cm = new CommonMethod();
                int urlId = cm.getUrlId(request.getRequestURI().replace("/bms/", ""));
                if (cm.isUrlAllowed(deptId, roleId, urlId)) {
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    String finYear = "";
//System.out.println("Financial month : " + month);
                    if (month <= 3) {
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
                        finYear += "<option value=\"" + (year - 4) + "-" + (year - 3) + "\">" + (year - 4) + "-" + (year - 3) + "</option>";

                    } else {
                        finYear += "<option value=\"" + year + "-" + (year + 1) + "\">" + year + "-" + (year + 1) + "</option>";
                        finYear += "<option value=\"" + (year - 1) + "-" + year + "\">" + (year - 1) + "-" + year + "</option>";
                        finYear += "<option value=\"" + (year - 2) + "-" + (year - 1) + "\">" + (year - 2) + "-" + (year - 1) + "</option>";
                        finYear += "<option value=\"" + (year - 3) + "-" + (year - 2) + "\">" + (year - 3) + "-" + (year - 2) + "</option>";
                    }
                    String ddlScheme = "", user_scheme = "", user_scheme_code = "", user_scheme_fy = "", user_fy = "";
                    //ddlScheme = cm.getCashSchemes("-Please Select-", deptId);
                    ddlScheme = cm.getSchemes("-Please Select-", schemeId);
                    //user_scheme = cm.getUserScheme(userId, String.valueOf(dept_id));
//                    int tmp_data_status = cm.getTmpDataStatus(user_name, String.valueOf(dept_id));
//                    if (tmp_data_status == 0) {
//                        if (user_scheme != "") {
//                            ddlScheme = cm.getUserSchemes(String.valueOf(dept_id), user_scheme, "-Please Select-");
                    // user_fy = cm.getUserSchemesfy(userId, user_scheme, "-Please Select-");


        %>
        <div id="loader" class="center"></div>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white">Upload Beneficiary Data</h4>
                                    </div>
                                    <div class="card-body bg-light font-weight-bold">
                                        <form id="frmFileUpload" action="../saveExcelDataToTmp" method="post" enctype="multipart/form-data">
                                            <input type="hidden" name="userid" id="userid" value="<%=userId%>" />
                                        <input type="hidden" name="deptid" value="<%=deptId%>" id="deptid" />
                                        <div class="form-group row mx-auto required">
                                            <label for="scheme"  class="col-sm-3 col-form-label control-label">Select Scheme :</label>
                                            <div class="col-sm-7">
                                                <!--                                        <input type="hidden" name="scheme_name" value="" id="scheme_name" />-->
                                                <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required" onchange="setSelectedName(this.id);">                                            
                                                    <%=ddlScheme%>                                                    
                                                </select>
                                                <input type="hidden" id="ddlSchemesName" name="ddlSchemesName" value="" />
                                            </div>
                                        </div>
                                        <!--                                <div class="form-group row mx-auto required">
                                                                            <label for="FY" class="col-sm-3 col-form-label control-label" >Select FY :</label>
                                                                            <div class="col-sm-7">
                                                                                <select class="custom-select mr-sm-2" id="fy" name="fy" required="">
                                        <%=finYear%>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row mx-auto required">
                                <label for="Installment" class="col-sm-3 col-form-label control-label" >Select Installment :</label>
                                <div class="col-sm-7">
                                    <input type="hidden" name="installment_name" value="" id="installment_name" />
                                    <select class="custom-select mr-sm-2" id="ddlInstallment" name="installment" required="" onchange="getSelectedInstallment(this)">
        
                                    </select>
                                </div>
                            </div>-->
                                        <div class="form-group row mx-auto required">
                                            <input type="hidden" name="csvrow" id="csvrow" value="" />
                                            <label for="Installment" class="col-sm-3 col-form-label control-label" >Upload File :<span class="text-danger-small"><strong>( Max allowed size : 2 MB )</strong></span></label>
                                            <div class="col-sm-7">
                                                <input  accept=".csv"  type="file" name="fileExcel" id="fileExcel" class="form-control" required="" onchange="return ValidateSize(this);"/>
                                            </div>
                                        </div>        
                                        <div class="form-group row mx-auto required">
                                            <label for="scheme_code" class="col-sm-3 col-form-label control-label" >Enter Beneficiary Count in the Uploaded Excel File :</label>
                                            <div class="col-sm-7">
                                                <input type="text" onkeypress="return IsNumeric(event);" class="form-control" maxlength="7" id="benificiary_count" name="benificiary_count" required="" autocomplete="off">
                                                <span id="lblinstallmentError" style="color: red"></span>
                                            </div>
                                        </div>
                                        <div class="form-group row mx-auto">
                                            <div class="col-sm-3 col-form-label" ></div>
                                            <div class="col-sm-7">
                                                <div class="row">
                                                    <div class="col-sm-2"> <button type="submit" class="btn btn-primary upload" id="upload" onclick="this.value = 'Submitting ..';">Submit</button></div>
                                                    <!--<div class="col-sm-6 ml-0 pl-0"> <a href="viewUploadedHist.jsp" class="btn btn-primary">View Previous Uploaded Data</a></div>-->
                                                </div>
                                            </div>
                                        </div>
                                        <input type="hidden" name="validjson" value="" id="validjson" />
                                        <input type="hidden" name="invalidjson" value="" id="invalidjson" />
                                        <input type="hidden" name="headerarr" value="" id="headerarr" />

                                    </form>
                                </div>                        
                            </div>
                        </div>                 
                    </div>
                    <div class="row">
                        <div class="col-md-12 ml-2">
                            <div id="error_list">
                                <h2>Data Upload Error</h2>
                                <hr />
                                <h3 id="header1"></h3>
                                <br />
                                <h4 id="header2"></h4>
                                <br />
                                <div id="csv-display"> </div>
                                <table id="example" class="display table table-responsive table-bordered" style="width:100%;">
                                    <thead>                            
                                        <tr>
                                            <th>Sl no.</th>
                                            <th>Beneficary Name</th>
                                            <th >Aadhaar</th>
                                            <th>Ration card Number</th>
                                            <th>Bank Account No.</th>
                                            <th>IFSC.</th>
                                            <th>District</th>
                                            <th>Sub-Division</th>
                                            <th>Block/ MC</th>
                                            <th>GP/VC/Ward</th>
                                            <th >Mismatched District</th>
                                            <th>Mismatched Sub-Division</th>
                                            <th>Mismatched Block</th>
                                            <th>Mismatched Aadhar</th>
                                            <th>Mismatched RC NO.</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr><td colspan="12" class="text-center"> No data found</td></tr>
                                    </tbody>

                                </table>
                            </div> 
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        <%
//                } else {
//                    out.println("<script type=\"text/javascript\">");
//                    out.println("alert('User is not mapped with Scheme. Please Contact Department Admin for mapping');");
//                    out.println("location='../deptUserHome.jsp';");
//                    out.println("</script>");
//                }
//            } else {
//                out.println("<script type=\"text/javascript\">");
//                out.println("alert('Data upload is not complete. Please accept or reject those data to upload more data');");
//                out.println("location='viewUploadedTmpData.jsp';");
//                out.println("</script>");
//            }
        %>
    </body>

    <!-- jQuery library -->
    <!--    <script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>-->
    <script src="${pageContext.request.contextPath}/JS/common.js"></script>
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>

    <script src="${pageContext.request.contextPath}/JS/select2.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" crossorigin="anonymous"></script>
    <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/JS/arrayToTable.js" type="text/javascript"></script>
    <script>
                                                        document.onreadystatechange = function () {

                                                            if (document.readyState !== "complete") {
                                                                document.querySelector(
                                                                        "body").style.visibility = "hidden";
                                                                document.querySelector(
                                                                        "#loader").style.visibility = "visible";
                                                            } else {
                                                                document.querySelector(
                                                                        "#loader").style.display = "none";
                                                                document.querySelector(
                                                                        "body").style.visibility = "visible";
                                                            }
                                                        };
    </script>
    <script>
        $(document).on("click", ".upload", function () {
            var $this = $(this);
            if ($this.closest("form")[0].checkValidity()) {
                var sel = $("#ddlSchemes").val();
                var scheme_name_array = sel.split("~");
                var scheme_name = "";
//                for (var i = 0; i < scheme_name_array.length; i++) {
//                    scheme_name = scheme_name_array[3];
//                }
                scheme_name = $("#ddlSchemes option:selected").text();
//                var fy = $("#fy").val();
//                var installment = $("#installment_name").val();
                var ben_count = $("#benificiary_count").val();
                var csvrow = $("#csvrow").val();

                var rows = $("#benificiary_count").val();
                if (csvrow !== ben_count) {
                    alert("Input beneficiary Count not matched with CSV Row Count.Please Check.");
                    document.getElementById("benificiary_count").focus();
                    return false;
                } else {
                    var msg = "";
                    msg += "Scheme: " + scheme_name + "\n";
                    msg += "Input beneficiary Count: " + ben_count + "\n";
                    msg += "CSV Row Count : " + csvrow + "\n";
                    var r = confirm(msg + "Are You Sure!");
                    if (r === true) {
                        $this.attr("disabled", true);
                        $this.text("Saving...");
                        $this.closest("form")[0].submit();
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
        function ValidateSize(file) {
            var FileSize = file.files[0].size / 1024 / 1024; // in MB

            //var FileSize = file.files[0].size;
            if (FileSize > 2) {
                alert('File size exceeds 2 MB');
                $("#upload").css("display", "none");
                // $(file).val(''); //for clearing with Jquery
            } else {
                $("#upload").css("display", "block");
            }
        }
        function getSelectedSchemeFy(sel) {

            //    document.getElementById("scheme_name").value = sel.options[sel.selectedIndex].text;
            var scheme_array = $("#ddlSchemes").val();
            var scheme_code = scheme_array.split("~")[1];
            var userid = $("#userid").val();
            //alert(scheme_code);
            //alert("scheme_code" + scheme_code + "&userid=" + userid);

            $.ajax({
                url: "${pageContext.request.contextPath}/AjaxServlet?getfy=1&scheme_code=" + scheme_code + "&userid=" + userid,
                type: "POST",
                success: function (data) {
                    $("#fy").html(data);
                }
            });
        }
    </script>  
    <script>
        function getSelectedInstallment(selallmentinst) {
            document.getElementById("installment_name").value = selallmentinst.options[selallmentinst.selectedIndex].text;
        }
    </script>  
    <script>

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
    <script type="text/javascript">
        function CSVToArray(strData, strDelimiter) {
            strDelimiter = (strDelimiter || ",");
            var objPattern = new RegExp(
                    (
                            "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
                            "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
                            "([^\"\\" + strDelimiter + "\\r\\n]*))"
                            ),
                    "gi"
                    );
            var arrData = [[]];
            var arrMatches = null;
            while (arrMatches = objPattern.exec(strData)) {
                //while (arrMatches === objPattern.exec(strData)) {
                var strMatchedDelimiter = arrMatches[ 1 ];
                if (
                        strMatchedDelimiter.length &&
                        (strMatchedDelimiter !== strDelimiter)
                        ) {
                    arrData.push([]);
                }
                if (arrMatches[ 2 ]) {
                    //alert(arrMatches[]]);
                    var strMatchedValue = arrMatches[ 2 ].replace(new RegExp("\"\"", "g"), "\"").replace(/^\s+|\s+$/g, '');
                } else {
                    var strMatchedValue = arrMatches[ 3 ].replace(/^\s+|\s+$/g, '');
                }
                arrData[ arrData.length - 1 ].push(strMatchedValue);
            }
            return(arrData);
        }

        $(document).ready(function () {
            $("#ddlSchemes").on('change', function (e) {
                var schemeId = $('#ddlSchemes option:selected').val();
                $("#ddlSchemesName").val($('#ddlSchemesName').val());
                $("#ddlInstallment").empty();
                $("#ddlInstallment").append("<option value=\"\">--- Please Select ---</option>");
                $.ajax({
                    url: "${pageContext.request.contextPath}/AjaxServlet?schmp=1&schemeId=" + schemeId,
                    type: "POST",
                    success: function (data) {
                        //console.log(data);
                        var myObj = JSON.parse(data);
                        //console.log(myObj.prd);
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
                                $("#ddlInstallment").append("<option value=\"" + i + "\">" + selArr[i - 1] + " </option>");
                            }
                        }
                    }
                });
            });

            $("#error_list").css("display", "none");
            var htmlarr = [[]];
            var lgddetails_json = [];
            $.ajax({
                type: 'POST',
                url: "${pageContext.request.contextPath}/process_servlet",
                async: true,
                data: {
                    operationtype: "lgddetails"
                },
                success: function (result) {
                    if (result === "null") {
                        alert("Record not found.");
                    } else {
                        lgddetails_json = jQuery.parseJSON(result);
                        console.log(lgddetails_json);
                    }
                },
                error: function () {
                    alert("Error");
                }
            });

            const d = [
                [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
                [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
                [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
                [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
                [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
                [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
                [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
                [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
                [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
                [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
            ];
            const p = [
                [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
                [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
                [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
                [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
                [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
                [4, 2, 8, 6, 5, 7, 3, 9, 0, 1],
                [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
                [7, 0, 4, 6, 9, 1, 3, 2, 5, 8]
            ];

            function validateuid(aadharNumber) {
                let c = 0;
                let invertedArray = aadharNumber.split('').map(Number).reverse();

                invertedArray.forEach((val, i) => {
                    c = d[c][p[(i % 8)][val]];
                });

                return (c === 0);
            }

            function validateIfsc(ifsc) {
                let regex = /^[A-Z]{4}0[A-Z0-9]{6}$/g;
                let found = regex.test(ifsc);
                return found;
            }

            function validateBankAcc(bankAcc) {
                let regex = /^[0-9]{9,18}$/g;
                let found = regex.test(bankAcc);
                return found;
            }

            $('#fileExcel').change(function (e) {

                $("#error_list").css("display", "none");
                var amt = "";
                if ((window.FileReader)) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var lineSplit = e.target.result.split("\n");
                        var commaSplit = lineSplit[0].split(",");
                        var content = "";
                        for (var i = 0; i < commaSplit.length; i++) {
                            var temp = commaSplit[i];
                            content = content.trim() + " " + temp;
                        }
                        var fileContent = reader.result;
                        htmlarr = CSVToArray(fileContent.trim().toString().toUpperCase(), ',');
                        $('#csv-display').empty();
                        //$('#header1').text(htmlarr[0][0]);
                        //

                        //var headercsv='['+JSON.stringify(htmlarr[0])+','+JSON.stringify(htmlarr[1])+']';
                        var headercsvarr = [];
                        headercsvarr[0] = htmlarr[0];
                        headercsvarr[1] = htmlarr[1];
                        console.log(JSON.stringify(headercsvarr));
                        $('#headerarr').val(JSON.stringify(headercsvarr));



                        htmlarr.shift();
                        // console.log(htmlarr[0][0] + htmlarr[0][1] + htmlarr[0][2] + htmlarr[0][3]);
                        //$('#header2').text(htmlarr[0][0] + htmlarr[0][1] + ", " + htmlarr[0][2] + ", " + htmlarr[0][3] + ", " + htmlarr[0][4]);
                        htmlarr.shift();
                        htmlarr.shift(); // header row delete
                        var myJsonString = JSON.stringify(htmlarr);
                        //console.log(myJsonString);
                        //FOR DISTRICT MATCHING
                        var district_flag = 0;
                        var count = 0;

                        for (i = 0; i < htmlarr.length; i = i + 1) {

                            if (htmlarr[i][0] != "") {
                                count++;
                                $("#csvrow").val(count);
                                if (htmlarr[i][4] == "") {
                                    amt = "error";

                                    //$("#error_list").css("display", "block");
                                }
                            }

                            if (htmlarr[i][6] != "") {
                                //htmlarr[i][6]=htmlarr[i][6].toString();
                                for (j = 0; j < lgddetails_json.length; j = j + 1) {

                                    // alert(lgddetails_json[j].dist_name.toUpperCase());
                                    if (htmlarr[i][6] === lgddetails_json[j].dist_name.toUpperCase()) {
                                        //if(dist.localeCompare(lgddetails_json[j].dist_name, undefined, { sensitivity: 'accent' })){
                                        district_flag = 1;
                                        //  htmlarr[i].remove();
                                        break;
                                    }
                                }
                                if (district_flag === 0) {
                                    htmlarr[i].push("District name mismatch");
                                } else {
                                    htmlarr[i].push("");
                                }
                            } else {
                                htmlarr[i].push("");
                            }
                            district_flag = 0;

                        }
                        if (amt == "error") {
                            alert("You have Blank value in Amount. Please Check!");
                            $("#upload").css("display", "none");
                        }
                        //FOR Sub-Division MATCHING
                        var subdiv_name_flag = 0;
                        for (i = 0; i < htmlarr.length; i = i + 1) {
                            if (htmlarr[i][7] !== "") {
                                for (j = 0; j < lgddetails_json.length; j = j + 1) {
                                    if (htmlarr[i][7] === lgddetails_json[j].subdiv_name.toUpperCase()) {
                                        subdiv_name_flag = 1;
                                        break;
                                    }
                                }
                                if (subdiv_name_flag === 0) {
                                    htmlarr[i].push("Sub division name mismatch");
                                } else {
                                    htmlarr[i].push("");
                                }
                            } else {
                                htmlarr[i].push("");
                            }
                            subdiv_name_flag = 0;
                        }

                        //FOR BLOCK MATCHING
                        var block_name_flag = 0;
                        for (i = 0; i < htmlarr.length; i = i + 1) {
                            if (htmlarr[i][8] !== "") {

                                for (j = 0; j < lgddetails_json.length; j = j + 1) {
                                    if (htmlarr[i][8] === lgddetails_json[j].block_name.toUpperCase()) {
                                        block_name_flag = 1;
                                        break;
                                    }
                                }
                                if (block_name_flag === 0) {
                                    htmlarr[i].push("Block name mismatch");
                                } else {
                                    htmlarr[i].push("");
                                }
                            } else {
                                htmlarr[i].push("");
                            }
                            block_name_flag = 0;
                        }

                        //FOR pid Number Length Checking
                        var pid_flag = 0;
                        for (i = 0; i < htmlarr.length; i = i + 1) {
                            if (htmlarr[i][2] !== "") {

                                if (!(validateuid(htmlarr[i][2].toString()))) {
                                    htmlarr[i].push("Aadhar No. Not Valid");
                                } else {
                                    htmlarr[i].push("");
                                }

                            } else {
                                htmlarr[i].push("");
                            }
                        }
                        
                        //FOR RC Number Length Checking
                        var rc_flag = 0;
                        for (i = 0; i < htmlarr.length; i = i + 1) {
                            if (htmlarr[i][3] !== "") {
                                if (htmlarr[i][3].length === "12" || htmlarr[i][3].length === "14") {
                                    rc_flag = 1;
                                    break;
                                }

                                if (htmlarr[i][3].length < "12" || htmlarr[i][3].length > "14") {
                                    htmlarr[i].push("RC No. Not Valid");
                                } else {
                                    htmlarr[i].push("");
                                }
                            } else {
                                htmlarr[i].push("");
                            }
                            rc_flag = 0;
                        }
                        
                        //IFSC Pattern Checking
//                        for (i = 0; i < htmlarr.length; i = i + 1) {
//                            if (htmlarr[i][2] !== "") {
//
//                                if (!(validateuid(htmlarr[i][2].toString()))) {
//                                    htmlarr[i].push("Aadhar No. Not Valid");
//                                } else {
//                                    htmlarr[i].push("");
//                                }
//
//                            } else {
//                                htmlarr[i].push("");
//                            }
//                        }
                        
                        //Bank Account Pattern Checking
//                        for (i = 0; i < htmlarr.length; i = i + 1) {
//                            if (htmlarr[i][2] !== "") {
//
//                                if (!(validateuid(htmlarr[i][2].toString()))) {
//                                    htmlarr[i].push("Aadhar No. Not Valid");
//                                } else {
//                                    htmlarr[i].push("");
//                                }
//
//                            } else {
//                                htmlarr[i].push("");
//                            }
//                        }
//                        
                        
                        
                        
//                          console.log(JSON.stringify(htmlarr));
//                            var values = [];
//                            var finaljson=[];
//                            for (i = 0; i < htmlarr.length-1; i = i + 1) {
//                                var obj = {};
//                                for (j = 0; j < htmlarr[i].length; j = j + 1) {
//
//                                    obj[htmlarr[0][j]] = htmlarr[i+1][j];
//                                }
//                                console.log(obj);
//                                finaljson.push(obj);
//                            }
//                            console.log(finaljson);
                        var finalarray = [];
                        var successarray = [];
                        for (i = 0; i < htmlarr.length; i = i + 1) {
                            if ((htmlarr[i][10] + htmlarr[i][11] + htmlarr[i][12] + htmlarr[i][13] + htmlarr[i][14]) !== "") {
                                finalarray.push(htmlarr[i]);
                            } else {
                                successarray.push(htmlarr[i]);
                            }
                        }
                        $('#validjson').val(JSON.stringify(successarray));
                        $('#invalidjson').val(JSON.stringify(finalarray));
                        console.log(successarray);
                        console.log(finalarray);
                        $('#example').dataTable({
                            "data": finalarray,
                            autoWidth: false,
                            "columnDefs": [
                                {"width": "100px", "targets": 2}
                            ],
//                                rowGroup: [0],
                            destroy: true,
                            bAutoWidth: false,
//                                "columnDefs": [
//                                    {
//                                        "visible": false,
//                                        "targets": 0
//                                    },
//                                ],
                            responsive: true,
                            dom: 'Bfrtip',
                            lengthMenu: [
                                [10, 25, 50, -1],
                                ['10 rows', '25 rows', '50 rows', 'Show all']
                            ],
                            buttons: [
                                'pageLength', 'copy', 'csv', 'excel', 'pdf', 'print'
                            ],
                            "fnRowCallback"
                                    : function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {

                                        if (aData[10] !== "" || aData[11] !== "" || aData[12] !== "" || aData[13] !== "" || aData[14] !== "") {
                                            $('td', nRow).css('background-color', 'Orange');
                                            //alert("Data Mismatch . Please Check");
                                            var deptid = document.getElementById("deptid").value;
                                            //  var deptid=("##deptid").value();
//                                            if (deptid === "58" || deptid === "64" || deptid === "72") {
//                                                $("#upload").css("display", "block");
//                                            } else {

                                            //$("#upload").css("display", "none");
                                            $("#error_list").css("display", "block");

                                            // }
                                        }
                                    }
                        }

                        );
                    };
                    reader.readAsText(e.target.files.item(0));
                }
            });

            $('#ddlSchemes').select2();
        });

    </script>
</html>
<%
        } else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>
