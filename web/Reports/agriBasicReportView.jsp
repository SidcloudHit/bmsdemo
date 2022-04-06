<%-- 
    Document   : agriBasicReportView
    Created on : 10 Apr, 2020, 4:03:01 PM
    Author     : Nibedita
--%>
<%@page import="Master.Data.AdminUser"%>
<%@page import="java.util.Calendar"%>
<%@page import="Common.CommonMethod"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="Master.Data.MasAgriculture"%>
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
        Common.CommonMethod cm = new CommonMethod();
        int urlId = cm.getUrlId(request.getRequestURI().replace("/agriculture/", ""));
        if (true || cm.isUrlAllowed(deptId, roleId, urlId)) {
            String message = "";
            int edit = 0;
            int distId = 0;
            int saStoreId = 0;
            String ddlDist = "";
            String ddlLoc = "";
            String d = "", sd = "", l = "", b = "";
            JSONArray jsarr = null, jsarr1 = null, jsarr2 = null;
            JSONObject jsobj = null, jsobj1 = null, jsobj2 = null;
            jsarr2 = cm.getUserJuridictiondetailsAgri(userId, deptId);
            if (jsarr2 != null) {
                for (int n = 0; n < jsarr2.length(); n++) {
                    jsobj2 = jsarr2.getJSONObject(n);
                    ddlDist = cm.getDistrict(jsobj2.getInt("dist_code"), "-Please Select-");
                    distId = jsobj2.getInt("dist_code");
                    saStoreId = jsobj2.getInt("store_id");
                }
            }
            //ddlDist = cm.getDistrict("-Please Select-");
            ddlLoc = cm.getLocation("-Please Select-");

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

            String gpId = ""; //254031
            String fYear = ""; //2018-2019

            if (request.getParameter("fYear") != null) {
                fYear = Security.SecurityClass.killchar(request.getParameter("fYear"));
                edit = 2;
            }
            if (request.getParameter("gpId") != null) {
                gpId = Security.SecurityClass.killchar(request.getParameter("gpId"));
            }
            /*if (request.getParameter("b") != null) {
                b = Security.SecurityClass.killchar(request.getParameter("b"));
            }
            if (request.getParameter("l") != null) {
                l = Security.SecurityClass.killchar(request.getParameter("l"));
            }*/
            if (request.getParameter("sd") != null) {
                sd = String.valueOf(saStoreId);//Security.SecurityClass.killchar(request.getParameter("sd"));
            }
            if (request.getParameter("d") != null) {
                d = String.valueOf(distId);//Security.SecurityClass.killchar(request.getParameter("d"));
            }

            MasAgriculture agri = new MasAgriculture();

            if ((!gpId.equals("")) && (!fYear.equals(""))) {
                agri = new MasAgriculture(gpId, fYear);
                jsarr = cm.getCropDetails(gpId, fYear);
                jsarr1 = cm.getIrrigationDetails(gpId, fYear);
                if (agri.getWard_code() != null && jsarr != null && jsarr1 != null) {
                    edit = 1;
                }

            } else if ((!sd.equals("")) && (!fYear.equals(""))) {
                agri = new MasAgriculture("sd", sd, fYear);
                jsarr = cm.getCropDetails("sd", sd, fYear);
                jsarr1 = cm.getIrrigationDetails("sd", sd, fYear);
                if (agri.getSubdiv_code() != null && jsarr != null && jsarr1 != null) {
                    edit = 1;
                }
            } else if ((!d.equals("")) && (!fYear.equals(""))) {
                agri = new MasAgriculture("d", d, fYear);
                jsarr = cm.getCropDetails("d", d, fYear);
                jsarr1 = cm.getIrrigationDetails("d", d, fYear);
                if (agri.getDist_code() != null && jsarr != null && jsarr1 != null) {
                    edit = 1;
                }
            } else if ((!fYear.equals(""))) {
                agri = new MasAgriculture("s", "16", fYear);
                jsarr = cm.getCropDetails("s", "16", fYear);
                jsarr1 = cm.getIrrigationDetails("s", "16", fYear);
                if (jsarr != null && jsarr1 != null) {
                    edit = 1;
                }
            }


            /*
          else if ((!b.equals("")) && (!fYear.equals(""))) {
                agri = new MasAgriculture("b", b, fYear);
                jsarr = cm.getCropDetails("b", b, fYear);
                jsarr1 = cm.getIrrigationDetails("b", b, fYear);
                if (agri.getBlock_code() != null && jsarr != null && jsarr1 != null) {
                    edit = 1;
                }
            }  
             */

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Agriculture || View GP Wise Crop Data Entry</title>
        <!-- Latest compiled and minified CSS -->

<!--<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/normalize.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/skeleton.css">-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">

        <script>
            window.onload = function () {
                var edit = '<%= edit%>';
                var rpDta = document.getElementById("reportData");
                var msgD = document.getElementById("msgDiv");
                if (parseInt(edit) === 1) {
                    if (rpDta.className.includes("d-none")) {
                        rpDta.className = rpDta.className.replace("d-none", "d-block");
                    }
                    if (msgD.className.includes("d-block")) {
                        msgD.className = msgD.className.replace("d-block", "d-none");
                    }
                } else if (parseInt(edit) === 2) {
                    if (rpDta.className.includes("d-block")) {
                        rpDta.className = rpDta.className.replace("d-block", "d-none");
                    }
                    if (msgD.className.includes("d-none")) {
                        msgD.className = msgD.className.replace("d-none", "d-block");
                    }
                } else {
                    if (rpDta.className.includes("d-block")) {
                        rpDta.className = rpDta.className.replace("d-block", "d-none");
                    }
                    if (msgD.className.includes("d-block")) {
                        msgD.className = msgD.className.replace("d-block", "d-none");
                    }
                }

            }
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-12 mt-2 mb-3">
                    <div class="card mb-3">
                        <div class="card-body bg-form">
                            <div class="row">                                        
                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="ddlDistrict1">District</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlDistrict1" name="ddlDistrict1" class="form-control form-control-sm" onchange="resetBlock(8);dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSaStore');
                                            $('#ddlSubDiv1 option:not([value = \'-1\'],[value=<%= saStoreId%>])').remove();">
                                            <%=ddlDist%>                                                    
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="ddlSubDiv1">Agri Sub-Division</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlSubDiv1" name="ddlSubDiv1" class="form-control form-control-sm" onchange="dependentDropDown('ddlSubDiv1', 'ddlWardGp', 'fillSaGp');">
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div>
                                </div>
                                <!--<div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="ddlLocType">Location Type</label>
                                    <div class="input-group mb-3">
                                        <select id="ddlLocType" name="ddlLocType" class="form-control form-control-sm" onchange="dependentDropDown1('ddlSubDiv1', 'ddlLocType', 'ddlUrbRurBody', 'fillUrbRurBody');
                                                resetBlock(3);">
                                                                                             
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="ddlUrbRurBody">MC/NP/Block</label>   
                                    <div class="input-group mb-3">
                                        <select id="ddlUrbRurBody" name="ddlUrbRurBody" class="form-control form-control-sm" onchange="dependentDropDown('ddlUrbRurBody', 'ddlWardGp', 'fillGp');" >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div>
                                </div>-->
                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="ddlWardGp">Ward/GP/VC</label>
                                    <div class="input-group mb-3">                                                    
                                        <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" >
                                            <option value="0">-Please Select-</option>                                                 
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                    <label for="ddlFYear">Select Financial Year :</label>                                                 
                                    <div class="input-group mb-3">                                                    
                                        <select id="ddlFYear" name="ddlFYear" class="form-control form-control-sm" required="required" >
                                            <option value="0">--Select--</option>
                                            <%= finYear%>
                                        </select>
                                    </div>                           

                                </div>
                            </div>
                        </div> 
                        <div class="card-footer">
                            <div class="row float-right">
                                <div class="form-group col-md-12 mb-0">                                
                                    <div class="input-group">
                                        <button type="button" id="btnSearch" name="btnSearch" class="btn btn-primary" onclick="setAgriBasicReport();">
                                            <i class="fa fa-search">&nbsp;&nbsp;</i>View Report
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="row d-none" id="msgDiv">
                                <div class="form-group col-md-12 mb-0">                                
                                    <div class="input-group">
                                        <%= message%>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card mb-3 d-none" id="reportData">
                        <div class="card-header bg-info">
                            <div class="row">
                                <div class="col-md-10">
                                    <h4 class="text-white">Basic Information on Gram Panchayat (GP)/ Village Council (VC) at a Glance:</h4>
                                </div>
                                <div class="col-md-2 float-right">
                                    <button type="button" id="btnPrint" name="btnPrint" class="btn btn-primary float-right">
                                        <i class="fa fa-print">&nbsp;&nbsp;</i>Print Report
                                    </button>
                                </div>

                            </div>
                        </div>
                        <div class="card-body bg-page">
                            <div class="row">
                                <%
                                    if (!gpId.equals("")) {
                                %>
                                <div class="col-md-3 text-primary">District:</div><div class="col-md-3"><%= agri.getDist_name()%></div>                                      
                                <div class="col-md-3 text-primary" >Agri Sub-Division:</div><div class="col-md-3" ><%= agri.getSubdiv_name()%></div>
                                <!--<div class="col-md-3 text-primary">Block/NP/MC/ADC Block:</div><div class="col-md-3"><%= agri.getBlock_name()%></div>-->
                                <div class="col-md-3 text-primary" >GP/Ward/VC:</div><div class="col-md-3" ><%= agri.getWard_name()%></div>
                                <%
                                } else if (!sd.equals("")) {
                                %>
                                <div class="col-md-3 text-primary">District:</div><div class="col-md-3"><%= agri.getDist_name()%></div>                                      
                                <div class="col-md-3 text-primary" >Sub-Division:</div><div class="col-md-3" ><%= agri.getSubdiv_name()%></div>
                                <%
                                } else if (!d.equals("")) {
                                %>
                                <div class="col-md-3 text-primary">District:</div><div class="col-md-3"><%= agri.getDist_name()%></div>
                                <%
                                    }
                                %>                                
                                <div class="col-md-3 text-primary" >Financial Year:</div><div class="col-md-3" ><%= agri.getFyear()%></div>
                            </div>
                            <div class="row"><div  class="col-md-12"><hr class="separator" /></div></div>

                            <div class="row  vdivide">
                                <div class="col-sm-12 col-md-4 col-lg-4">
                                    <h5 class="text-primary">Population statistics</h5>
                                    <div class="row">
                                        <div class="col-md-8 text-primary">SC:</div><div class="col-md-2" ><%= agri.getNosc()%></div>
                                    </div>     
                                    <div class="row">
                                        <div class="col-md-8 text-primary">ST:</div><div class="col-md-2"><%= agri.getNost()%></div>
                                    </div>     
                                    <div class="row">
                                        <div class="col-md-8 text-primary">Other:</div><div class="col-md-2"><%= agri.getNoother()%></div>
                                    </div>     
                                    <div class="row">
                                        <div class="col-md-8 text-primary">Total Population (SC+ST+Other)</div><div class="col-md-2"><%= agri.getNosc() + agri.getNoother() + agri.getNoother()%></div>
                                    </div>     
                                    <div class="row">
                                        <div class="col-md-8 text-primary" >Male:</div><div class="col-md-2" ><%= agri.getNomale()%></div>
                                    </div>     
                                    <div class="row">
                                        <div class="col-md-8 text-primary" >Female:</div><div class="col-md-2" ><%= agri.getNofemale()%></div>
                                    </div>     
                                    <div class="row">
                                        <div class="col-md-8 text-primary" >Total Population(Male+Female);</div><div class="col-md-2" ><%= agri.getNomale() + agri.getNofemale()%></div>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-8 col-lg-8">
                                    <div class="row"><h5 class="text-primary text-left ml-2">Farmers Statistics (in Numbers)</h5></div>
                                    <div class="row">
                                        <div class="col-md-4 text-primary" >No. of farmers registered under PM-KISAN :</div><div class="col-md-2" ><%= agri.getNofarmer()%></div>
                                        <div class="col-md-4 text-primary" >No. of RoFR family (Patta Holders) :</div><div class="col-md-2" ><%=  agri.getNofamily()%></div>
                                    </div><br/>
                                    <h5 class="text-primary">Kisan Credit Card Details</h5>
                                    <div class="row">
                                        <div class="col-md-4 text-primary" >KCC Sponsored :</div><div class="col-md-2" ><%= agri.getKccsponsored()%></div>
                                        <div class="col-md-4 text-primary" >KCC Sanctioned :</div><div class="col-md-2" ><%= agri.getKccsanction()%></div>
                                        <div class="col-md-4 text-primary" >RoFR families covered :</div><div class="col-md-2" ><%= agri.getFamlycover()%></div>
                                        <div class="col-md-4 text-primary" >Amount Disbursed :</div><div class="col-md-2" ><%= agri.getAmountdisbursed()%></div>
                                    </div>


                                    <h5 class="text-primary">Irrigation Facilities</h5>
                                    <div class="row">
                                        <div class="col-md-4 text-primary" >Net Irrigated Area (Ha.) :</div><div class="col-md-2" ></div>
                                        <div class="col-md-4 text-primary" >Gross Irrigated Area (Ha.) :</div><div class="col-md-2" ></div>
                                    </div>
                                </div>
                            </div>


                            <div class="row"><div  class="col-md-12"><hr class="separator" /></div></div>
                            <div class="row  vdivide">
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <h5 class="text-primary">Agriculture Work Force</h5>
                                    <div class="row">
                                        <table class="table table-sm table-bordered">
                                            <tr>
                                                <th>Farmers details</th>
                                                <th>No</th>
                                                <th>Area (Ha)</th>
                                            </tr>
                                            <tr>
                                                <td>Farmer Marginal (< 1ha.)</td>
                                                <td><%= agri.getMarginalfarmerno()%></td>
                                                <td><%= agri.getMarginalfarmerarea()%></td>
                                            </tr>
                                            <tr>
                                                <td>Farmer Small (1-2 Ha.)</td>
                                                <td><%= agri.getSmallfarmerno()%></td>
                                                <td><%= agri.getSmallfarmerarea()%></td>
                                            </tr>
                                            <tr>
                                                <td>Farmer Medium (2-5Ha.)</td>
                                                <td><%= agri.getMediumfarmerno()%></td>
                                                <td><%= agri.getMediumfarmerarea()%></td>
                                            </tr>
                                            <tr>
                                                <td>Farmer Large (>5Ha.)</td>
                                                <td><%= agri.getLargefarmerno()%></td>
                                                <td><%= agri.getLargefarmerarea()%></td> 
                                            </tr>
                                            <tr>
                                                <td>No. of Farm families</td>
                                                <td><%= agri.getFarmfamilyno()%></td>
                                            </tr>
                                            <tr>
                                                <td>No. of Agricultural Labourers</td>
                                                <td><%= agri.getNoagrilabour()%></td>
                                            </tr>
                                            <tr>
                                                <td>No. of Farm families</td>
                                                <td><%= agri.getSharecroppers()%></td>
                                            </tr>
                                            <tr>
                                                <td>Total Farmers</td>
                                                <td><%= agri.getMarginalfarmerno() + agri.getSmallfarmerno() + agri.getMediumfarmerno() + agri.getLargefarmerno()%></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="col-sm-12 col-md-6 col-lg-6">
                                    <div class="row"><h5 class="text-primary text-left ml-2">Land Usage Statistics</h5></div>
                                    <div class="row">                                                    
                                        <div class="col-8">
                                            <label for="txtForestArea">Forest area</label>
                                        </div>
                                        <div class="col-4">                                                            
                                            <%= agri.getForestarea()%>
                                        </div>                                                    
                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtSownArea">Net Sown Area</label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getSownarea()%>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtSingleCrop">Single crop</label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getSinglecrop()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtDoubleCrop">Double crop </label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getDoublecrop()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtTripleCrop">Triple crop </label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getTriplecrop()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtTripleCrop">Cropping Intensity(%) </label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getCropping_intensity()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtWasteLand">Cultivable Waste land</label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getWasteland()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtNonAgri">Area Not Available (Non-Agri Use eg. Tea/Rubber etc)</label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getNonagriarea()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtBarren">Area Not Available (Barren & Un-Cultivable) </label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getBarrenarea()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtFallowLand">Fallow Land</label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getFallowland()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtGazingLand">Permanent pastures & grazing land </label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getGrazingland()%>
                                        </div>

                                    </div>
                                    <div class="row">
                                        <div class="col-8">
                                            <label for="txtGazingLand">Net Irrigated Area </label>
                                        </div>
                                        <div class="col-4">  
                                            <%= agri.getNet_irrigated_area()%>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <div class="row"><div  class="col-md-12"><hr class="separator" /></div></div>

                            <h5 class="text-primary">Crop Details</h5>
                            <div class="row">
                                <table class="table table-sm table-bordered" width="100%">
                                    <tr>
                                        <th>Crop Type</th>
                                        <th>Crop Name</th>
                                        <th>Season</th>
                                        <th>Area (Ha)</th>
                                    </tr>
                                    <%
                                        if (jsarr != null) {
                                            for (int n = 0; n < jsarr.length(); n++) {
                                                jsobj = jsarr.getJSONObject(n);
                                    %>
                                    <tr>
                                        <td><%= jsobj.getString("crop_type_name")%></a></td>
                                        <td><%= jsobj.getString("crop_name")%></td>
                                        <td><%= jsobj.getString("season_name")%></td>
                                        <td><%= jsobj.getDouble("crop_area")%></td>


                                    </tr>
                                    <% }
                                        }
                                    %>


                                </table>
                            </div>
                            <div class="row"><div  class="col-md-12"><hr class="separator" /></div></div>
                            <h5 class="text-primary">Source of irrigation (No)</h5>
                            <div class="row">
                                <table class="table table-sm table-bordered" width="100%">
                                    <tr>
                                        <th>Irrigation Type</th>
                                        <th>Irrigation Category</th>
                                        <th>No</th>
                                        <th>Command Area (Ha)</th>
                                    </tr>
                                    <%
                                        if (jsarr1 != null) {
                                            for (int n = 0; n < jsarr1.length(); n++) {
                                                jsobj1 = jsarr1.getJSONObject(n);
                                    %>
                                    <tr>
                                        <td><%= jsobj1.getString("irrigation_type")%></a></td>
                                        <td><%= jsobj1.getString("category")%></td>
                                        <td><%= jsobj1.getInt("irrigation_no")%></td>
                                        <td><%= jsobj1.getDouble("irrigation_area")%></td>


                                    </tr>
                                    <% }
                                        }
                                    %>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>    
            </div>
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
<script src="${pageContext.request.contextPath}/JS/printThis.js"></script>
<script>
                                            $(document).ready(function () {
                                                $('#btnPrint').on('click', function (e) {
                                                    //popup($('#reportData').html());
                                                    $('#reportData').printThis({
                                                        importCSS: true,
                                                        header: "",
                                                        base: ""
                                                    });
                                                });
                                            });

</script>
<%
        }else {
            response.sendRedirect("../User/userHome.jsp");
        }

    } else {
        response.sendRedirect("../login.jsp");
    }
%>