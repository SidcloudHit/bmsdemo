<%-- 
    Document   : agriBasicReportEntry
    Created on : 7 Apr, 2020, 7:26:03 PM
    Author     : Nibedita
--%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="Master.Data.AdminUser"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page import="Master.Data.MasAgriculture"%>
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
        if (true || cm.isUrlAllowed(deptId, roleId, urlId)) {
            JSONArray jsarr = null, jsarr1 = null;
            JSONObject jsobj = null, jsobj1 = null;
            jsarr = cm.getUserJuridictiondetailsAgri(userId, usr.getUser_department_id());

            String edit = "0";
            String regMsg = "";
            String type = "light";
            //int edit = 0;
            String ddlDist = "";
            String ddlOffice = "";
            String ddlIrriCat = "";
            String ddlCropType = "";
            String ddlCropSeason = "";
            int distId = 0;
            int saStoreId = 0;
            
            
            if (jsarr != null) {
                for (int n = 0; n < jsarr.length(); n++) {
                    jsobj = jsarr.getJSONObject(n);
                    ddlDist = cm.getDistrict(jsobj.getInt("dist_code"), "-Please Select-");
                    distId = jsobj.getInt("dist_code");
                    saStoreId = jsobj.getInt("store_id");
                }
            }

            
            ddlCropType = cm.getCropType("-Select-");
            ddlCropSeason = cm.getCropSeason("-Select-");
            ddlIrriCat = cm.getIrrigationCat("-Select-");
            String gpId = ""; //254031
            String fYear = ""; //2018-2019
            String d = "", sd = "", l = "", b = "";

            if (request.getParameter("gpId") != null) {
                gpId = Security.SecurityClass.killchar(request.getParameter("gpId"));
            }

            if (request.getParameter("fYear") != null) {
                fYear = Security.SecurityClass.killchar(request.getParameter("fYear"));
            }
            MasAgriculture agri = new MasAgriculture(gpId, fYear);

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

            if ((!gpId.equals("")) && (!fYear.equals(""))) {
                if (agri.getWard_code() != null) {
                    edit = "1";
                } else {
                    d = String.valueOf(distId); //Security.SecurityClass.killchar(request.getParameter("d"));
                    sd = String.valueOf(saStoreId);//Security.SecurityClass.killchar(request.getParameter("sd"));
                    //l = Security.SecurityClass.killchar(request.getParameter("l"));
                    //b = Security.SecurityClass.killchar(request.getParameter("b"));
                    edit = "2";
                }

            }
            
            ddlOffice = cm.getOffice(officeId, "-Please Select-");
            
            if (session.getAttribute("RegMsg") != null) {
                regMsg = session.getAttribute("RegMsg").toString();
                session.removeAttribute("RegMsg");
            }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Agriculture || GP Wise Crop Data Entry </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">


        <!-- <script language="javascript" type="text/javascript">-->
        <script>

            window.onload = function () {
                var edit = '<%=edit%>';
                if (parseInt(edit) === 1) {
                    $('#ddlDistrict1').val('<%= distId%>');
                    setSelectedName("ddlDistrict1");
                    dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSaStore');
                    // dependentDropDown('ddlDistrict1', 'ddlPs', 'fillPoliceStation');
                    //resetBlock(1);
                    $('#ddlSubDiv1 option[value!="<%= saStoreId%>"]').remove();
                    $('#ddlSubDiv1').val('<%= agri.getSubdiv_code()%>');
                    setSelectedName("ddlSubDiv1");
                    //  dependentDropDown('ddlSubDiv1', 'ddlPo', 'fillPostOffice');
                    //resetBlock(2);
                    /*$('#ddlLocType').val(' agri.getLocation_type() '); */   //   to be modified if loctype added             
                    //showHideBlock();
                    //dependentDropDown1('ddlSubDiv1', 'ddlLocType', 'ddlUrbRurBody', 'fillUrbRurBody');
                    //dependentDropDown('ddlSubDiv1', 'ddlUrbRurBody', 'fillBlock');
                    dependentDropDown('ddlSubDiv1', 'ddlWardGp', 'fillSaGp');
                    //resetBlock(3);
                    //$('#ddlUrbRurBody').val('<%= agri.getBlock_code()%>');
                    //setSelectedName("ddlUrbRurBody");
                    //dependentDropDown1('ddlUrbRurBody', 'ddlLocType', 'ddlWardGp', 'fillWardGp');
                    //dependentDropDown('ddlUrbRurBody', 'ddlWardGp', 'fillGp');
                    $('#ddlWardGp').val('<%= agri.getWard_code()%>');
                    setSelectedName("ddlWardGp");
                    $('#ddlFYear').val('<%= agri.getFyear()%>');


                    document.getElementById("txtFemale").value = '<%= agri.getNofemale()%>';
                    document.getElementById("txtMale").value = '<%= agri.getNomale()%>';
                    document.getElementById("txtSC").value = '<%= agri.getNosc()%>';
                    document.getElementById("txtST").value = '<%= agri.getNost()%>';
                    document.getElementById("txtOther").value = '<%= agri.getNoother()%>';
                    document.getElementById("txtNoFarmer").value = '<%= agri.getNofarmer()%>';
                    document.getElementById("txtPattaHolder").value = '<%= agri.getNofamily()%>';
                    document.getElementById("txtKCC").value = '<%= agri.getKccsponsored()%>';
                    document.getElementById("txtKCCSanc").value = '<%= agri.getKccsanction()%>';
                    //document.getElementById("txtRoFR").value = '<%= agri.getFamlycover()%>';
                    document.getElementById("txtAmtDisbursed").value = '<%= agri.getAmountdisbursed()%>';
                    document.getElementById("txtMarginalNo").value = '<%= agri.getMarginalfarmerno()%>';
                    document.getElementById("txtMarginalArea").value = '<%= agri.getMarginalfarmerarea()%>';
                    document.getElementById("txtSmallNo").value = '<%= agri.getSmallfarmerno()%>';
                    document.getElementById("txtSmallArea").value = '<%= agri.getSmallfarmerarea()%>';
                    document.getElementById("txtMediumNo").value = '<%= agri.getMediumfarmerno()%>';
                    document.getElementById("txtMediumArea").value = '<%= agri.getMediumfarmerarea()%>';
                    document.getElementById("txtLargeNo").value = '<%= agri.getLargefarmerno()%>';
                    document.getElementById("txtLargeArea").value = '<%= agri.getLargefarmerarea()%>';
                    document.getElementById("txtNoFarmFamilies").value = '<%= agri.getFarmfamilyno()%>';
                    document.getElementById("txtAgriLabourNo").value = '<%= agri.getNoagrilabour()%>';
                    document.getElementById("txtShareNo").value = '<%= agri.getSharecroppers()%>';
                    document.getElementById("txtForestArea").value = '<%= agri.getForestarea()%>';
                    document.getElementById("txtSownArea").value = '<%= agri.getSownarea()%>';
                    document.getElementById("txtSingleCrop").value = '<%= agri.getSinglecrop()%>';
                    document.getElementById("txtDoubleCrop").value = '<%= agri.getDoublecrop()%>';
                    document.getElementById("txtTripleCrop").value = '<%= agri.getTriplecrop()%>';
                    document.getElementById("txtWasteLand").value = '<%= agri.getWasteland()%>';
                    document.getElementById("txtNonAgri").value = '<%= agri.getNonagriarea()%>';
                    document.getElementById("txtBarren").value = '<%= agri.getBarrenarea()%>';
                    document.getElementById("txtFallowLand").value = '<%= agri.getFallowland()%>';
                    document.getElementById("txtGazingLand").value = '<%= agri.getGrazingland()%>';
                    document.getElementById("txtCroppingIntensity").value = '<%= agri.getCropping_intensity()%>';
                    document.getElementById("txtNetIrrigatedArea").value = '<%= agri.getNet_irrigated_area()%>';

                    //crop details value
                    getCropDetails();
                    //irrigation details value
                    getIrrigationDetails();


                } else if (parseInt(edit) === 2) {

                    $('#ddlDistrict1').val('<%= d%>');
                    setSelectedName("ddlDistrict1");
                    dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSaStore');

                    // dependentDropDown('ddlDistrict1', 'ddlPs', 'fillPoliceStation');
                    //resetBlock(1);
                    $('#ddlSubDiv1 option[value!="<%= saStoreId%>"]').remove();
                    $('#ddlSubDiv1').val('<%= sd%>');
                    setSelectedName("ddlSubDiv1");
                    //  dependentDropDown('ddlSubDiv1', 'ddlPo', 'fillPostOffice');
                    //resetBlock(2);
                    /*$('#ddlLocType').val(' agri.getLocation_type() '); */   //   to be modified if loctype added             
                    //showHideBlock();
                    //dependentDropDown1('ddlSubDiv1', 'ddlLocType', 'ddlUrbRurBody', 'fillUrbRurBody');
                    //dependentDropDown('ddlSubDiv1', 'ddlUrbRurBody', 'fillBlock');
                    dependentDropDown('ddlSubDiv1', 'ddlWardGp', 'fillSaGp');
                    //resetBlock(3);
                    //$('#ddlUrbRurBody').val('<%= b%>');
                    //setSelectedName("ddlUrbRurBody");
                    //dependentDropDown1('ddlUrbRurBody', 'ddlLocType', 'ddlWardGp', 'fillWardGp');
                    //dependentDropDown('ddlUrbRurBody', 'ddlWardGp', 'fillGp');
                    $('#ddlWardGp').val('<%= gpId%>');
                    setSelectedName("ddlWardGp");
                    $('#ddlFYear').val('<%= fYear%>');
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">

                    <%if (!regMsg.equals("")) {
                    %>
                    <div class="alert alert-<%=type%>">
                        <strong><%=regMsg%></strong>
                    </div>
                    <%
                        }
                    %>
                    <form id="frmCropDetails" action="../CropDetailsSave" method="post">
                        <div class="row">                                                            
                            <div class="col-md-12 mt-2 mb-3">
                                <div class="card mb-3">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white">Basic Information on Gram Panchayat (GP)/ Village Council (VC) at a Glance</h4>
                                    </div>
                                    <div class="card-body bg-form">
                                        <div class="row">                                        
                                            <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                <label for="ddlDistrict1">District</label>
                                                <div class="input-group mb-3">
                                                    <select id="ddlDistrict1" name="ddlDistrict1" class="form-control form-control-sm" onchange="resetBlock(8);
                                                            setSelectedName(this.id);
                                                            dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSaStore');
                                                            $('#ddlSubDiv1 option:not([value = \'-1\'],[value=<%= saStoreId%>])').remove();">
                                                        <%=ddlDist%>                                                    
                                                    </select>
                                                    <input type="hidden" id="ddlDistrict1Name" name="ddlDistrict1Name" value="" />
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2 ">
                                                <label for="ddlSubDiv1">Agri Sub-Division</label>
                                                <div class="input-group mb-3">
                                                    <select id="ddlSubDiv1" name="ddlSubDiv1" class="form-control form-control-sm" onchange="setSelectedName(this.id);dependentDropDown('ddlSubDiv1', 'ddlWardGp', 'fillSaGp');">
                                                        <option value="0">-Please Select-</option>                                                 
                                                    </select>
                                                    <input type="hidden" id="ddlSubDiv1Name" name="ddlSubDiv1Name" value="" />
                                                </div>
                                            </div>
                                            
                                            <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                <label for="ddlWardGp">Ward/GP/VC</label>
                                                <div class="input-group mb-3">                                                    
                                                    <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" onchange="refreshAgriBasicReportEntryPage();setSelectedName(this.id);" required="required">
                                                        <option value="0">-Please Select-</option>                                                 
                                                    </select>
                                                    <input type="hidden" id="ddlWardGpName" name="ddlWardGpName" value="" />
                                                </div>
                                            </div>

                                            <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                <label for="ddlFYear">Select Financial Year :</label>                                                 
                                                <div class="input-group mb-3">                                                    
                                                    <select id="ddlFYear" name="ddlFYear" class="form-control form-control-sm" required="required" onchange="refreshAgriBasicReportEntryPage();">
                                                        <option value="0">--Select--</option>
                                                        <%= finYear%>
                                                    </select>
                                                </div>                           

                                            </div>
                                        </div>
                                        <hr/>
                                        <h5 class="text-left">Population Statistics</h5>
                                        <div class="row">                                            
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtMale">Number of Male :</label>
                                                <div class="input-group mb-3">       
                                                    <input type="number" id="txtMale" name="txtMale" class="form-control form-control-sm" placeholder="Number of Male"/>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtFemale">Number of Female :</label>
                                                <div class="input-group mb-3">        
                                                    <input type="number" id="txtFemale" name="txtFemale" class="form-control form-control-sm" placeholder="Number of Female"/>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtSC">Number of SC :</label>
                                                <div class="input-group mb-3">       
                                                    <input type="number" id="txtSC" name="txtSC" class="form-control form-control-sm" placeholder="Number of SC"/>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtST">Number of ST :</label>
                                                <div class="input-group mb-3">       
                                                    <input type="number" id="txtST" name="txtST" class="form-control form-control-sm" placeholder="Number of ST"/>
                                                </div>
                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtOther">Number of Others :</label>
                                                <div class="input-group mb-3">       
                                                    <input type="number" id="txtOther" name="txtOther" class="form-control form-control-sm" placeholder="Number of Others"/>
                                                </div>
                                            </div>
                                        </div>
                                        <hr/>
                                        <h5 class="text-left">Farmers Statistics (in Numbers)</h5>
                                        <div class="row">
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtNoFarmer">Farmers registered under PM-KISAN</label>
                                                <div class="input-group mb-3">
                                                    <input type="number" id="txtNoFarmer" name="txtNoFarmer" class="form-control form-control-sm" placeholder="No of Farmers"/>
                                                </div>

                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtPattaHolder">RoFR family (Patta Holders)</label>
                                                <div class="input-group mb-3">
                                                    <input type="number" id="txtPattaHolder" name="txtPattaHolder" class="form-control form-control-sm" placeholder="No of Patta Holders"/>
                                                </div>
                                            </div>
                                        </div>
                                        <hr/>
                                        <h5 class="text-left">Kisan Credit Card Statistics (in Numbers)</h5>
                                        <div class="row">
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtKCC">KCC Sponsored</label>
                                                <div class="input-group mb-3">
                                                    <input type="number" id="txtKCC" name="txtKCC" class="form-control form-control-sm" placeholder="KCC Sponsored"/>
                                                </div>

                                            </div>

                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtKCCSanc">KCC Sanctioned</label>
                                                <div class="input-group mb-3">
                                                    <input type="number" id="txtKCCSanc" name="txtKCCSanc" class="form-control form-control-sm" placeholder="KCC Sanctioned"/>
                                                </div>

                                            </div>
                                            <!--<div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtRoFR">RoFR families covered</label>
                                                <div class="input-group mb-3">
                                                    <input type="number" id="txtRoFR" name="txtRoFR" class="form-control form-control-sm" placeholder="RoFR families covered"/>
                                                </div>

                                            </div>-->
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtAmtDisbursed">Amount Disbursed</label>
                                                <div class="input-group mb-3">
                                                    <input type="number" id="txtAmtDisbursed" name="txtAmtDisbursed" class="form-control form-control-sm" placeholder="Amount Disbursed"/>
                                                </div>
                                            </div>
                                        </div>
                                        <hr/>

                                        <div class="row  vdivide">
                                            <div class="col-sm-12 col-md-6 col-lg-6">
                                                <div class="row"><h5 class="text-left ml-2">Agriculture Work-force</h5></div>
                                                <table class="table table-sm table-striped table-bordered small" style="width:100%">
                                                    <tr>
                                                        <th></th>
                                                        <th>No</th>
                                                        <th>Area (Ha)</th>
                                                    </tr>
                                                    <tr>
                                                        <td>Farmer Marginal (< 1ha.)</td>
                                                        <td><input type="number" id="txtMarginalNo" name="txtMarginalNo" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <td><input type="text" id="txtMarginalArea" name="txtMarginalArea" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Farmer Small (1-2 Ha.)</td>
                                                        <td><input type="number" id="txtSmallNo" name="txtSmallNo" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <td><input type="text" id="txtSmallArea" name="txtSmallArea" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Farmer Medium (2-5Ha.)</td>
                                                        <td><input type="number" id="txtMediumNo" name="txtMediumNo" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <td><input type="text" id="txtMediumArea" name="txtMediumArea" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Farmer Large (>5Ha.)</td>
                                                        <td><input type="number" id="txtLargeNo" name="txtLargeNo" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <td> <input type="text" id="txtLargeArea" name="txtLargeArea" size="120" class="form-control form-control-sm" placeholder=""/> 
                                                    </tr>
                                                    <tr>
                                                        <td>No. of Farm families</td>
                                                        <td><input type="number" id="txtNoFarmFamilies" name="txtNoFarmFamilies" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <!-- <td><input type="text" id="txtNoFamiliesArea" name="txtNoFamiliesArea" size="120" class="form-control form-control-sm" placeholder=""/></td> -->
                                                    </tr>
                                                    <tr>
                                                        <td>Agricultural Laboures</td>
                                                        <td><input type="number" id="txtAgriLabourNo" name="txtAgriLabourNo" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <!-- <td><input type="text" id="txtAgriLabourArea" name="txtAgriLabourArea" size="120" class="form-control form-control-sm" placeholder=""/></td> -->
                                                    </tr>
                                                    <tr>
                                                        <td>Share Croppers /  Tenant Farmers</td>
                                                        <td><input type="number" id="txtShareNo" name="txtShareNo" size="120" class="form-control form-control-sm" placeholder=""/></td>
                                                        <!--  <td><input type="text" id="txtShareArea" name="txtShareArea" size="120" class="form-control form-control-sm" placeholder=""/></td> -->
                                                    </tr>
                                                </table>
                                            </div> 
                                            <div class="col-sm-12 col-md-6 col-lg-6">
                                                <div class="row"><h5 class="text-left ml-2">Land Usage Statistics</h5></div>
                                                <div class="row">                                                    
                                                    <div class="col-4">
                                                        <label for="txtForestArea">Forest area</label>
                                                    </div>
                                                    <div class="col-8">                                                            
                                                        <input type="text" id="txtForestArea" name="txtForestArea" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>                                                    
                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtSownArea">Net Sown Area</label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtSownArea" name="txtSownArea" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtSingleCrop">Single crop</label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtSingleCrop" name="txtSingleCrop" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtDoubleCrop">Double crop </label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtDoubleCrop" name="txtDoubleCrop" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtTripleCrop">Triple crop </label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtTripleCrop" name="txtTripleCrop" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtCroppingIntensity">Cropping Intensity(%) </label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtCroppingIntensity" name="txtCroppingIntensity" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtWasteLand">Cultivable Waste land</label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtWasteLand" name="txtWasteLand" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtNonAgri">Area Not Available (Non-Agri Use eg. Tea/Rubber etc)</label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtNonAgri" name="txtNonAgri" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtBarren">Area Not Available (Barren & Un-Cultivable) </label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtBarren" name="txtBarren" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtFallowLand">Fallow Land</label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtFallowLand" name="txtFallowLand" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtGazingLand">Permanent pastures & grazing land </label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtGazingLand" name="txtGazingLand" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="col-4">
                                                        <label for="txtNetIrrigatedArea">Net Irrigated Area </label>
                                                    </div>
                                                    <div class="col-8">  
                                                        <input type="text" id="txtNetIrrigatedArea" name="txtNetIrrigatedArea" size="120" class="form-control form-control-sm" placeholder=""/>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>                                    

                                        <hr/>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <h5 class="text-left"> Crop Statistics</h5>
                                            </div>
                                            <div class="col-md-6">
                                                <button type="button" id="btnAddMore" name="btnAddMore" value ="1" class="btn btn-primary btn-sm addmore float-right">
                                                    <i class="fa fa-plus"> Add More</i>
                                                </button>
                                            </div>
                                        </div>
                                        <div class="row">                                            
                                            <table id="itemList" class="table table-sm table-striped table-bordered small">
                                                <thead>
                                                    <tr>
                                                        <th>Crop Type</th>
                                                        <th>Crop Name</th>
                                                        <th>Season</th>
                                                        <th>Area</th>
                                                        <th>                                                            
                                                            <input type="hidden" id="hdnCropCount" name="hdnCropCount" value="1" />
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody id="itemList_tbody">
                                                    <tr id="1">
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <select id="ddlCropType1" name="ddlCropType1" class="form-control form-control-sm" onchange="dependentDropDown('ddlCropType1', 'ddlCropName1', 'fillCropName');">
                                                                    <%=ddlCropType%>                                                    
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <select id="ddlCropName1" name="ddlCropName1" class="form-control form-control-sm" >
                                                                    <option value="0">-Please Select-</option>                                                 
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <select id="ddlCropSeason1" name="ddlCropSeason1" class="form-control form-control-sm" >
                                                                    <%=ddlCropSeason%>                                                 
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <input type="text" id="txtCropArea1" name="txtCropArea1" class="form-control form-control-sm" />
                                                            </div>
                                                        </td>
                                                        <td></td>
                                                    </tr>
                                                </tbody>
                                            </table>

                                            <div style="display:none;">
                                                <table id="sample_table">
                                                    <tr id="">
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <select id="ddlCropType" name="ddlCropType" class="form-control form-control-sm">
                                                                    <%=ddlCropType%>                                                    
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <select id="ddlCropName" name="ddlCropName" class="form-control form-control-sm" >
                                                                    <option value="0">-Please Select-</option>                                                 
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <select id="ddlCropSeason" name="ddlCropSeason" class="form-control form-control-sm" >
                                                                    <%=ddlCropSeason%>                                                 
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <input type="text" id="txtCropArea" name="txtCropArea" class="form-control form-control-sm" value=""/>
                                                            </div>
                                                        </td>
                                                        <td><a class="btn btn-xs delete-record1" data-id="1"><i class="fa fa-trash"></i></a></td>

                                                    </tr>
                                                </table>
                                            </div>
                                        </div> 
                                        <hr/>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <h5 class="text-left">Irrigation Statistics</h5>
                                            </div>
                                            <div class="col-md-6">
                                                <button type="button" id="btnAddMoreIrr" name="btnAddMoreIrr" value ="1" class="btn btn-primary btn-sm addmore float-right">
                                                    <i class="fa fa-plus"> Add More</i>
                                                </button>
                                            </div>
                                        </div>

                                        <div class="row">                                            
                                            <table id="itemListIrr" class="table table-sm table-striped table-bordered small">
                                                <thead>
                                                    <tr>
                                                        <th>Irrigation Type</th>
                                                        <th>Irrigation Category</th>
                                                        <th>No</th>
                                                        <th>Command Area</th>
                                                        <th>                                                            
                                                            <input type="hidden" id="hdnIrriCount" name="hdnIrriCount" value="1" />
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody id="itemListIrr_tbody">
                                                    <tr id="1">
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <select id="ddlIrrigationType1" name="ddlIrrigationType1" class="form-control form-control-sm">
                                                                    <option value="0">Please Select</option>
                                                                    <option value="1">Public</option>
                                                                    <option value="2">Private</option>
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <select id="ddlIrrigationCat1" name="ddlIrrigationCat1" class="form-control form-control-sm">
                                                                    <%= ddlIrriCat%>
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <input type="text" id="txtIrrigationNo1" name="txtIrrigationNo1" class="form-control form-control-sm" placeholder=""  />
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <input type="text" id="txtcommandArea1" name="txtcommandArea1" class="form-control form-control-sm" placeholder=""  />
                                                            </div>
                                                        </td>
                                                        <td></td>
                                                    </tr>
                                                </tbody>
                                            </table>

                                            <div style="display:none;">
                                                <table id="sample_table_irr">
                                                    <tr id="">
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <select id="ddlIrrigationType" name="ddlIrrigationType" class="form-control form-control-sm">
                                                                    <option value="0">Please Select</option>
                                                                    <option value="1">Public</option>
                                                                    <option value="2">Private</option>
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <select id="ddlIrrigationCat" name="ddlIrrigationCat" class="form-control form-control-sm">
                                                                    <%= ddlIrriCat%>
                                                                </select>
                                                            </div>
                                                        </td>
                                                        <td>                                                                    
                                                            <div class="input-group">
                                                                <input type="text" id="txtIrrigationNo" name="txtIrrigationNo" class="form-control form-control-sm" placeholder=""  />
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <div class="input-group">
                                                                <input type="text" id="txtcommandArea" name="txtcommandArea" class="form-control form-control-sm" placeholder=""  />
                                                            </div>
                                                        </td>
                                                        <td><a class="btn btn-xs delete-record2" data-id="1"><i class="fa fa-trash"></i></a></td>

                                                    </tr>
                                                </table>
                                            </div>

                                        </div> 
                                        <!--<div class="row">
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="lblIrriType">Select Irrigation Type </label>
                                                <select id="ddlIrrigationType0" name="ddlIrrigationType0" class="form-control form-control-sm">
                                                    <option value="0">Please Select</option>
                                                    <option value="1">Public</option>
                                                    <option value="2">Private</option>
                                                </select>
                                            </div>
                                            <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                <label for="lblIrriCat">Select Irrigation Category</label>
                                                <select id="ddlIrrigationCat0" name="ddlIrrigationCat0" class="form-control form-control-sm">
                                        <%= ddlIrriCat%>
                                    </select>
                                </div>
                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="lblIrriNo">No</label>
                                    <div class="input-group mb-3">
                                        <input type="text" id="txtIrrigationNo0" name="txtIrrigationNo0" class="form-control form-control-sm" placeholder=""  />
                                    </div>
                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                    <label for="lblIrriArea">Command Area</label>
                                    <div class="input-group mb-3">
                                        <input type="text" id="txtcommandArea0" name="txtcommandArea0" class="form-control form-control-sm" placeholder=""  />
                                    </div>
                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                    <div class="input-group mb-3  col-md-4">
                                        <input type="button" id = "btnAdd" onclick = "AddIrrigationRecord()" value = "Add more" class="btn btn-primary"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row" id ="dvContainer">
                                <div  class="form-group col-sm-4 col-md-3 col-lg-2" id="d1">

                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2" id="d2">

                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2" id="d3">

                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2" id="d4">

                                </div>
                            </div>
                            <input type="hidden" id="hdnIrriCount" name="hdnIrriCount" value="1" />

                        </div>-->
                                        <%
                                            if (!agri.isFinal_stat()) {
                                        %>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />
                                                <button type="submit" id="btnSubmitDraft" name="btnSubmitDraft" class="btn btn-primary btn-block mt-1">Save Details as Draft</button>                                             
                                            </div>
                                            <div class="col-md-6">
                                                <button type="submit" id="btnSubmit" name="btnSubmit" class="btn btn-primary mt-1 btn-block" onclick="return confirm('Once you submit, You shall no longer be able to edit this page. Are you sure?');">Submit Final Details</button>                                        
                                            </div>
                                        </div>
                                        <%
                                        } else {
                                        %>
                                        <div class="row">
                                            <div class="col-md-12 bg-success">
                                                <span class="font-weight-bold">You have already submitted this page </span>
                                            </div>
                                        </div>
                                        <%
                                            }
                                        %>        
                                    </div>
                                </div>
                            </div>
                        </div>                        
                    </form>
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
                                                        jQuery(document).delegate('#btnAddMore', 'click', function (e) {
                                                            e.preventDefault();
                                                            var content = jQuery('#sample_table tr'),
                                                                    size = jQuery('#itemList >tbody >tr').length + 1,
                                                                    element = null,
                                                                    element = content.clone();
                                                            element.attr('id', size);

                                                            element.find('#ddlCropType').attr('id', 'ddlCropType' + size);
                                                            element.find('#ddlCropType' + size).attr('name', 'ddlCropType' + size);
                                                            element.find('#ddlCropType' + size).attr('onchange', 'dependentDropDown("ddlCropType' + size + '", "ddlCropName' + size + '", "fillCropName")');


                                                            element.find('#ddlCropName').attr('id', 'ddlCropName' + size);
                                                            element.find('#ddlCropName' + size).attr('name', 'ddlCropName' + size);

                                                            element.find('#ddlCropSeason').attr('id', 'ddlCropSeason' + size);
                                                            element.find('#ddlCropSeason' + size).attr('name', 'ddlCropSeason' + size);

                                                            element.find('#txtCropArea').attr('id', 'txtCropArea' + size);
                                                            element.find('#txtCropArea' + size).attr('name', 'txtCropArea' + size);


                                                            element.find('.delete-record').attr('data-id', size);
                                                            element.appendTo('#itemList_tbody');
                                                            $('#hdnCropCount').val(size);
                                                        });
                                                        jQuery(document).delegate('a.delete-record1', 'click', function (e) {
                                                            e.preventDefault();
                                                            var didConfirm = confirm("Are you sure You want to delete");
                                                            if (didConfirm === true) {
                                                                var id = jQuery(this).attr('data-id');
                                                                var rowCount = $('#hdnCropCount').val();
                                                                jQuery('#' + id).remove();

                                                                //regnerate index number on table
                                                                $('#itemList_tbody tr').each(function (index) {
                                                                    if (index + 1 > 0) {
                                                                        if (index + 1 >= id) {
                                                                            $(this).attr('id', index + 1);

                                                                            $(this).find('#ddlCropType' + (index + 2)).attr('id', 'ddlCropType' + (index + 1));
                                                                            $(this).find('#ddlCropType' + (index + 1)).attr('name', 'ddlCropType' + (index + 1));
                                                                            $(this).find('#ddlCropType' + (index + 1)).attr('onchange', 'dependentDropDown("ddlCropType' + (index + 1) + '", "ddlCropName' + (index + 1) + '", "fillCropName")');


                                                                            $(this).find('#ddlCropName' + (index + 2)).attr('id', 'ddlCropName' + (index + 1));
                                                                            $(this).find('#ddlCropName' + (index + 1)).attr('name', 'ddlCropName' + (index + 1));

                                                                            $(this).find('#ddlCropSeason' + (index + 2)).attr('id', 'ddlCropSeason' + (index + 1));
                                                                            $(this).find('#ddlCropSeason' + (index + 1)).attr('name', 'ddlCropSeason' + (index + 1));

                                                                            $(this).find('#txtCropArea' + (index + 2)).attr('id', 'txtCropArea' + (index + 1));
                                                                            $(this).find('#txtCropArea' + (index + 1)).attr('name', 'txtCropArea' + (index + 1));

                                                                            $(this).find('.delete-record').attr('data-id', index + 1);
                                                                        }
                                                                    }
                                                                });
                                                                $('#hdnCropCount').val(rowCount - 1);

                                                                return true;
                                                            } else {
                                                                return false;
                                                            }
                                                        });



                                                        //irigation

                                                        jQuery(document).delegate('#btnAddMoreIrr', 'click', function (e) {
                                                            e.preventDefault();
                                                            var content = jQuery('#sample_table_irr tr'),
                                                                    size = jQuery('#itemListIrr >tbody >tr').length + 1,
                                                                    element = null,
                                                                    element = content.clone();
                                                            element.attr('id', size);

                                                            element.find('#ddlIrrigationType').attr('id', 'ddlIrrigationType' + size);
                                                            element.find('#ddlIrrigationType' + size).attr('name', 'ddlIrrigationType' + size);
                                                            //element.find('#ddlCropType' + size).attr('onchange', 'dependentDropDown("ddlCropType' + size + '", "ddlCropName' + size + '", "fillCropName")');


                                                            element.find('#ddlIrrigationCat').attr('id', 'ddlIrrigationCat' + size);
                                                            element.find('#ddlIrrigationCat' + size).attr('name', 'ddlIrrigationCat' + size);

                                                            element.find('#txtIrrigationNo').attr('id', 'txtIrrigationNo' + size);
                                                            element.find('#txtIrrigationNo' + size).attr('name', 'txtIrrigationNo' + size);

                                                            element.find('#txtcommandArea').attr('id', 'txtcommandArea' + size);
                                                            element.find('#txtcommandArea' + size).attr('name', 'txtcommandArea' + size);


                                                            element.find('.delete-record').attr('data-id', size);
                                                            element.appendTo('#itemListIrr_tbody');
                                                            $('#hdnIrriCount').val(size);
                                                        });
                                                        jQuery(document).delegate('a.delete-record2', 'click', function (e) {
                                                            e.preventDefault();
                                                            var didConfirm = confirm("Are you sure You want to delete");
                                                            if (didConfirm === true) {
                                                                var id = jQuery(this).attr('data-id');
                                                                var rowCount = $('#hdnIrriCount').val();
                                                                jQuery('#' + id).remove();

                                                                //regnerate index number on table
                                                                $('#itemListIrr_tbody tr').each(function (index) {
                                                                    if (index + 1 > 0) {
                                                                        if (index + 1 >= id) {
                                                                            $(this).attr('id', index + 1);

                                                                            $(this).find('#ddlIrrigationType' + (index + 2)).attr('id', 'ddlIrrigationType' + (index + 1));
                                                                            $(this).find('#ddlIrrigationType' + (index + 1)).attr('name', 'ddlIrrigationType' + (index + 1));
                                                                            //$(this).find('#ddlCropType' + (index + 1)).attr('onchange', 'dependentDropDown("ddlCropType' + (index + 1) + '", "ddlCropName' + (index + 1) + '", "fillCropName")');


                                                                            $(this).find('#ddlIrrigationCat' + (index + 2)).attr('id', 'ddlIrrigationCat' + (index + 1));
                                                                            $(this).find('#ddlIrrigationCat' + (index + 1)).attr('name', 'ddlIrrigationCat' + (index + 1));

                                                                            $(this).find('#txtcommandArea' + (index + 2)).attr('id', 'txtcommandArea' + (index + 1));
                                                                            $(this).find('#txtcommandArea' + (index + 1)).attr('name', 'txtcommandArea' + (index + 1));

                                                                            $(this).find('#txtIrrigationNo' + (index + 2)).attr('id', 'txtIrrigationNo' + (index + 1));
                                                                            $(this).find('#txtIrrigationNo' + (index + 1)).attr('name', 'txtIrrigationNo' + (index + 1));

                                                                            $(this).find('.delete-record').attr('data-id', index + 1);
                                                                        }
                                                                    }
                                                                });
                                                                $('#hdnIrriCount').val(rowCount - 1);

                                                                return true;
                                                            } else {
                                                                return false;
                                                            }
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