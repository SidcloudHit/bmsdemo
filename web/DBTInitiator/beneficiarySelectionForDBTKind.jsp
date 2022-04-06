<%-- 
    Document   : beneficiarySelectionForDBT
    Created on : 28 Jan, 2020, 2:37:03 PM
    Author     : acer
--%>

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
            String ddlSchemes = "", ddlDist = "", ddlOffice = "", ddlItemCats = "";
            ddlSchemes = cm.getKindSchemes("-Please Select-");//cm.getSchemes("-Please Select-");
            ddlItemCats = cm.getDbtItemCategories("-Please Select-");

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

            //String ddlDept = "";
            //ddlDept = cm.getDepartment("-Please Select-");
            
            int distId = 0;
            //if (jsarr != null) {
            //    for (int n = 0; n < jsarr.length(); n++) {
            //        jsobj = jsarr.getJSONObject(n);
            //        distId = jsobj.getInt("dist_code");
            
            //ddlDist = cm.getDistrict(distId, "-Please Select-");
            ddlOffice = cm.getOffice(officeId, "-Please Select-");

            //if (usr.getUser_role() == 1) {
            //    ddlDept = cm.getDepartment("-Please Select-");
            //} else {
            //    ddlDept = cm.getDeprtment(usr.getUser_department_id(), "-Please Select-");
            //}
            //    }
            //}

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Beneficiary Selection for DBT Kind Schemes</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.min.css">
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
                        <div class="card-header bg-info">
                            <h4 class="text-white">Beneficiary Selection for DBT in Kind:</h4>
                        </div>
                        <div class="card-body bg-form">
                            <div class="row">
                                <div class="form-group  col-sm-4 col-md-4 col-lg-4">
                                    <label for="ddlSchemes">Scheme</label>
                                    <div class="input-group mb-3">                                                                                
                                        <select id="ddlSchemes" name="ddlSchemes" class="form-control form-control-sm" required="required">                                            
                                            <%=ddlSchemes%>                                                    
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-4 col-md-4 col-lg-4">
                                    <label for="ddlFinYear">Financial Year</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlFinYear" name="ddlFinYear" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                            <%= finYear%>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-4 col-md-4 col-lg-4">
                                    <label for="ddlInstallment">Installment</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlInstallment" name="ddlInstallment" class="form-control form-control-sm" >
                                            <option value="">--Select--</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group  col-sm-3 col-md-3 col-lg-3">
                                    <label for="ddlItemCat">Item Category</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlItemCat" name="ddlItemCat" class="form-control form-control-sm" onchange="dependentDropDown('ddlItemCat', 'ddlItem', 'fillDbtItems');">
                                            <%=ddlItemCats%>          
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group  col-sm-3 col-md-3 col-lg-3">
                                    <label for="ddlItem">Item Name</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlItem" name="ddlItem" class="form-control form-control-sm" onchange="return setSelected('ddlItemUnit', (this.value === '-1') ? '-1' : this.value.split('#')[1]);">
                                            <option value="0#0">--Please Select--</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group  col-sm-3 col-md-3 col-lg-3">
                                    <label for="ddlItemCat">Item Quantity</label>
                                    <div class="input-group mb-3">
                                        <input type="text" id="txtItemQuan" name="txtItemQuan" class="form-control form-control-sm" required="required"/>
                                        <div class="input-group-append">
                                            <select id="ddlItemUnit" name="ddlItemUnit" class="form-control form-control-sm" >
                                                <option value="-1">--Select Unit--</option>
                                                <option value="gm">gm</option>
                                                <option value="KG">KG</option>
                                                <option value="ml">ml</option>
                                                <option value="Ltr">Ltr</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group  col-sm-3 col-md-3 col-lg-3">
                                    <label for="txtItemRate">Rate per Unit</label>
                                    <div class="input-group mb-3">
                                        <input type="text" id="txtItemRate" name="txtItemRate" pattern="[0-9]*" class="form-control form-control-sm" required="required"/>
                                    </div>
                                </div>
                            </div>


                            <div class="row">
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">My Office</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlOffice" name="ddlOffice" class="form-control form-control-sm" required="required" onchange="resetBlock(2); dependentDropDown('ddlOffice', 'ddlUrbRurBody', 'fillOfficeBlock');">
                                            <%=ddlOffice%>                                                  
                                        </select>
                                    </div> 
                                </div>

                                <!--<div class="form-group col-md-2">
                                    <label for="ddlSubDiv">Subdivision</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlSubDiv" name="ddlSubDiv" class="form-control form-control-sm" required="required" onchange="resetBlock(2);dependentDropDown('ddlSubDiv', 'ddlUrbRurBody', 'fillBlock');">
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>-->
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">MC/NP/Block</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlUrbRurBody" name="ddlUrbRurBody" class="form-control form-control-sm" onchange="dependentDropDown('ddlUrbRurBody', 'ddlVlwStore', 'fillvlw');" ><!--onchange="dependentDropDown1('ddlUrbRurBody', 'ddlOffice', 'ddlWardGp', 'fillOfficeGp');"--> >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>
                                <div class="form-group col-md-2">
                                    <label for="ddlSubDiv">VLW Store</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlVlwStore" name="ddlVlwStore" class="form-control form-control-sm" >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>
                                <!--<div class="form-group col-md-2">
                                    <label for="ddlSubDiv">Ward/GP</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div> 
                                </div>-->
                                <div class="form-group col-md-2">
                                    <label for=""></label>
                                    <div class="input-group mb-3">
                                        <div class="checkbox icheck-material-blue">
                                            <input type="checkbox" id="chkBxAllowNoLand" name="chkBxAllowNoLand" value="1" >
                                            <label for="chkBxAllowNoLand">Allow Beneficiary with no Land Records</label>                                            
                                        </div>  
                                    </div> 
                                </div>
                                <div class="form-group col-md-2 align-self-end">
                                    <div class="input-group mb-3">
                                        <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary">
                                            <i class="fa fa-search">&nbsp;&nbsp;</i>Search
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="row d-none" id="benListDiv">
                                <div class="input-group mb-3 col-md-12">
                                    <table id="beneficiaryList" class="table table-sm table-striped table-bordered small" style="width:100%">
                                        <thead class="bg-warning">
                                            <tr>  
                                                <th></th>
                                                <th>Beneficiary ID</th>
                                                <th>Beneficiary Name</th>
                                                <th>Father's Name</th>
                                                <th>Mobile</th>
                                                <th>Scheme Name</th>
                                            </tr>
                                        </thead>                                        
                                    </table>
                                </div>
                            </div>
                            <div class="row" id="divSchemeMapping">                                
                                <div class="input-group mb-3 col-md-4">
                                    <button type="button" id="btnAdd" name="btnAdd" class="btn btn-primary">
                                        <i class="fa fa-search">&nbsp;&nbsp;</i>Add Beneficiaries for DBT
                                    </button>
                                </div>
                                <div id="msgDiv" class="col-md-4"></div>
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

<!-- common JavaScript functions-->
<script src ="${pageContext.request.contextPath}/JS/common.js"></script>
<script src="${pageContext.request.contextPath}/JS/validation.js"></script>
<script src="${pageContext.request.contextPath}/JS/ajaxFunctions.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/datatables.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/DataTables-1.10.20/js/dataTables.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/dataTables.checkboxes.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/dataTables.responsive.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/Responsive-2.2.3/js/responsive.bootstrap4.min.js"></script>
<script charset="utf8" src="${pageContext.request.contextPath}/DataTables/FixedHeader-3.1.6/js/dataTables.fixedHeader.min.js"></script>
<script>
                                            $(document).ready(function () {
                                                var table;
                                                $('#btnSearch').on('click', function (e) {
                                                    var i = 0;
                                                    //var distId = $('#ddlDistrict option:selected').val();
                                                    //var subdivId = $('#ddlSubDiv option:selected').val();
                                                    var blockId = $('#ddlUrbRurBody option:selected').val();
                                                    var storeId = $('#ddlVlwStore option:selected').val();
                                                    //var gpId = $('#ddlWardGp option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    var finYr = $('#ddlFinYear option:selected').val();
                                                    var instl = $('#ddlInstallment  option:selected').val();
                                                    var itemCat = $('#ddlItemCat  option:selected').val();
                                                    var item = $('#ddlItem  option:selected').val().split('#')[0];
                                                    var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;
                                                    console.log(item);

                                                    if ((blockId === "" || blockId === "-1") || (storeId === "" || storeId === "-1") || schemeId === "" || finYr === "" || instl === "" || itemCat === "" || item === "") {
                                                        alert("Kindly Select Scheme, Block, Vlw Store, Financial Year, Installment,Item Category and Item for searching beneficiaries");
                                                        return false;
                                                    }

                                                    $('#benListDiv').removeClass("d-none").addClass("d-block");

                                                    $('#beneficiaryList').DataTable().clear().destroy();
                                                    table = $('#beneficiaryList').DataTable({
                                                        "ajax": {
                                                            "url": "${pageContext.request.contextPath}/BeneficiaryDataAll?dbtin=2&blockId=" + blockId + "&storeId=" + storeId + "&schemeId=" + schemeId + "&finYr=" + finYr + "&instl=" + instl + "&itemCat=" + itemCat + "&item=" + item + "&land=" + land,
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
                                                        "iDisplayLength": 50,
                                                        'order': [[1, 'asc']],
                                                        'responsive': true,
                                                        "columns": [
                                                            {"data": "rc_mem"},
                                                            {"data": "rc_mem"},
                                                            {"data": "full_name"},
                                                            {"data": "guardian_name"},
                                                            {"data": "mobile"},
                                                            {"data": "scheme_name"}
                                                        ],
                                                        "drawCallback": function (settings) {

                                                            $('input:checkbox').each(function () {
                                                                $(this).attr('checked', true);
                                                                //$(this).prop('readonly', true);
                                                            });
                                                        }
                                                    });

                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                });


                                                //Set Scheme Data
                                                $("#ddlSchemes").on('change', function (e) {
                                                    var schemeId = $('#ddlSchemes option:selected').val();
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
                                                                console.log(num);
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
                                                    }
                                                    );
                                                });


                                                // Handle form submission event
                                                $('#btnAdd').on('click', function (e) {


                                                    var elem = $('#divSchemeMapping');
                                                    var finYr = $('#ddlFinYear option:selected').val();
                                                    var instl = $('#ddlInstallment  option:selected').val();

                                                    var item = $('#ddlItem option:selected').val().split('#')[0];
                                                    var itemQty = $('#txtItemQuan').val();
                                                    var itemUnit = $('#ddlItemUnit option:selected').val();
                                                    var itemRate = $('#txtItemRate').val();
                                                    var storeId = $('#ddlVlwStore option:selected').val();
                                                    var schemeId = $('#ddlSchemes option:selected').val();
                                                    //alert($('#chkBxAllowNoLand').is(":checked"));
                                                    var land = $('#chkBxAllowNoLand').is(":checked") ? true : false;

                                                    if (schemeId === "" || finYr === "" || instl === "") {
                                                        alert("Kindly Select Scheme,Financial Year and Installment for adding beneficiaries");
                                                        return false;
                                                    }

                                                    if (item === "" || itemUnit === "-1") {
                                                        alert("Kindly Select Item Name and Unit for adding beneficiaries");
                                                        return false;
                                                    }

                                                    if (itemQty === "" || itemQty === "0") {
                                                        alert("Kindly Enter Item Quantity for adding beneficiaries");
                                                        return false;
                                                    }
                                                    if (itemRate === "") {
                                                        alert("Kindly Enter Rate per Unit for adding beneficiaries");
                                                        return false;
                                                    }

                                                    if (storeId === "" || storeId === "0" || storeId === "-1") {
                                                        alert("Kindly Select VLW Store for adding beneficiaries");
                                                        return false;
                                                    }

                                                    //var table = $('#beneficiaryList').DataTable();
                                                    $('#beneficiaryList_wrapper').addClass("col-md-12");
                                                    //var params = table.$('input,select,textarea').serializeArray();

                                                    table.column(0).checkboxes.select();
                                                    var rows_selected = table.column(0).checkboxes.selected();

                                                    /*var tblData = table.rows('.selected').data();
                                                     var tmpData;
                                                     $.each(tblData, function (i, val) {
                                                     tmpData = tblData[i];
                                                     console.log(tmpData);
                                                     });*/

                                                    var id = [];
                                                    // Iterate over all selected checkboxes
                                                    //$.each(rows_selected, function (index, rowId) {
                                                    $.each(rows_selected, function (index, rowId) {
                                                        /*if (!$.contains(document, elem[this])) {
                                                         // Create a hidden element
                                                         elem.append(
                                                         $('<input>')
                                                         .attr('type', 'hidden')
                                                         .attr('name', 'bid_' + i)
                                                         .val(this)
                                                         );
                                                         i++;
                                                         }*/
                                                        //var rowData = table.row($(this).parents('tr')).data();
                                                        //console.log(table.row($(this).closest('tr')).data());
                                                        // console.log(table.row($(this).parents('tr')).data());
                                                        //alert(this);
                                                        //alert(rows_selected['pmkisan_id']);


                                                        /*working code for entire row selection start*/
                                                        var rowData = rows_selected[index];
                                                        var b = table
                                                                .rows(function (idx, data, node) {
                                                                    return data.rc_mem === rowData ? true : false;
                                                                })
                                                                .data();
                                                        console.log(b[0]);
                                                        id.push(b[0]);
                                                        /*working code for entire row selection end*/
                                                        //id.push(this);

                                                    });

                                                    if (id.length === 0) {
                                                        alert("No Beneficiary Selected");
                                                        return false;
                                                    }

                                                    var json = JSON.stringify(id);

                                                    $.ajax({
                                                        //url: "${pageContext.request.contextPath}/DBTFileGeneration?finYr=" + finYr + "&instl=" + instl + "&amount=" + amount + "&distId=" + distId + "&subdivId=" + subdivId + "&schemeId=" + schemeId + "&land=" + land,
                                                        url: "${pageContext.request.contextPath}/DBTInitiateKind?finYr=" + finYr + "&instl=" + instl + "&item=" + item + "&itemQty=" + itemQty + "&itemUnit=" + itemUnit + "&itemRate=" + itemRate + "&storeId=" + storeId + "&schemeId=" + schemeId + "&land=" + land,
                                                        type: "POST",
                                                        dataType: 'json',
                                                        data: {json: json},
                                                        success: function (result) {
                                                            $("#msgDiv").html("Number of DBT beneficiary successfully sent for verification is " + result);
                                                            $('#btnSearch').click();
                                                        }
                                                    });
                                                });



                                                //$(document).ready(function () {
                                                $('#ddlSchemes').select2();
                                                //});

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
