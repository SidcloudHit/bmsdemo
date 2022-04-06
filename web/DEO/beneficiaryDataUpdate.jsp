<%-- 
    Document   : beneficiaryDataUpdate
    Created on : 15 Mar, 2020, 1:31:11 AM
    Author     : Arindam Mukherjee
--%>

<%@page import="Master.Data.DBTBeneficiary"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
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
    int edit = 0, benId = 0;
    String ddlDist = "", ddlLoc = "", ddlAdditionalId = "";
    ddlDist = cm.getDistrict("-Please Select-");
    ddlLoc = cm.getLocation("-Please Select-");
    ddlAdditionalId = cm.getAdditionalIdType("-Please Select-");
    String regMsg = "";
    String type = "light";
    String rcm = "", msg = "";
    DBTBeneficiary ben = null;

    if (request.getParameter("benid") != null) {
        benId = Integer.parseInt(request.getParameter("benid"));
        ben = new DBTBeneficiary(benId);
        edit = 2;
    }
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
        <title>Agriculture, Tripura || Beneficiary Data Entry</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/icheck-material.css">
        <script language="javascript" type="text/javascript">
            window.onload = function () {
                var edit = '<%= edit%>';
                //alert(edit);
                //alert(parseInt(edit));
                if (parseInt(edit) === 2) {
                    $("#hdnBenId").val('<%= ben.getBeneficiary_id()%>');
                    $("#txtBeneficiaryName").val('<%= ben.getBeneficiary_name()%>');
                    $("#txtFathersName").val('<%= ben.getFather_name()%>');
                    $("#txtDateOfBirth").val('<%= ben.getDob()%>');
                    var gender = '<%= ben.getGender()%>';
                    if (gender === 'M' || gender === 'Male') {
                        $('[name="radGender"]').val(['Male']);
                    } else if (gender === 'F' || gender === 'Female') {
                        $('[name="radGender"]').val(['Female']);
                    } else if (gender === 'T' || gender === 'Trans') {
                        $('[name="radGender"]').val(['Trans']);
                    }

                    var cat = '<%= ben.getCaste()%>';
                    $('[name="radCategory"]').val([cat]);

                    $("#txtMobile").val('<%= ben.getMobile()%>');
                    $("#txtRationCardNo").val('<%= ben.getRation_card_no()%>');
                    $("#txtRationCardMem").val('<%= ben.getRationcard_memberid()%>');
                    var hof = '<%= ben.isHead_of_family()%>';
                    if (hof === 'true') {
                        $('[name="radHof"]').val(['true']);
                    } else if (hof === 'false') {
                        $('[name="radHof"]').val(['false']);
                    }

                    $("#txtUIDCardNo").val('<%= ben.getAadhaar()%>');
                    var pmkisanId = '<%= ben.getPmkisan_id()%>';
                    console.log(pmkisanId);
                    console.log(pmkisanId !== "");
                    console.log(pmkisanId !== "null");
                    if (pmkisanId !== "" && pmkisanId !== "null") {
                        $('[name="radPmkisanAvl"]').val(['true']);
                        isPmkisanIdAvl();
                        $("#txtPMKISANId").val(pmkisanId);
                    } else {
                        $('[name="radPmkisanAvl"]').val(['false']);
                    }

                    $('#ddlDistrict1').val('<%= ben.getDist_code()%>');
                    dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSubDiv');
                    dependentDropDown('ddlDistrict1', 'ddlPs', 'fillPoliceStation');
                    resetBlock(1);
                    $('#ddlSubDiv1').val('<%= ben.getSubdiv_code()%>');
                    dependentDropDown('ddlSubDiv1', 'ddlPo', 'fillPostOffice');
                    resetBlock(2);
                    $('#ddlLocType').val('<%= ben.getLocation_type()%>');
                    //showHideBlock();
                    dependentDropDown1('ddlSubDiv1', 'ddlLocType', 'ddlUrbRurBody', 'fillUrbRurBody');
                    resetBlock(3);
                    $('#ddlUrbRurBody').val('<%= ben.getBlock_code()%>');
                    dependentDropDown1('ddlUrbRurBody', 'ddlLocType', 'ddlWardGp', 'fillWardGp');
                    $('#ddlWardGp').val('<%= ben.getWard_code()%>');
                    $("#txtAddrOth").val('<%= ben.getLandmark()%>');
                    $('#ddlPs').val('<%= ben.getPs_code()%>');
                    $('#ddlPo').val('<%= ben.getPo_code()%>#<%= ben.getPin()%>');
                    $("#txtAddrPIN").val('<%= ben.getPin()%>');

                    $("#txtEmail").val('<%= ben.getEmail()%>');
                    $("#ddlQual").val('<%= ben.getEducation_qly()%>');
                    $("#txtAnnualIncome").val('<%= ben.getAnnual_income()%>');
                    $("#txtOccupation").val('<%= ben.getOccupation()%>');

                    /*$("#txtPANCardNo").val('<%= ben.getPan()%>');
                     $("#txtEpicNo").val('<%= ben.getEpic_no()%>');
                     $("#txtDrivingLicenseNo").val('<%= ben.getDl_no()%>');
                     $("#txtSoilHealthCardNo").val('<%= ben.getSoil_health_card_no()%>');
                     $("#txtCropInsuranceNo").val('<%= ben.getInsurance_no()%>');
                     $("#txtKisanCreditCardNo").val('<%= ben.getKisan_credit_card_no()%>');
                     $("#txtMNREGAJobCardNo").val('<%= ben.getMgnrega_jobcard_no()%>');
                     $("#txtMNREGAJobCardMem").val('<%= ben.getMgnrega_jobcard_member_id()%>');*/
                     
                     getAddlIdDetails('<%= ben.getBeneficiary_id()%>');

                    $("#txtBankName").val('<%= ben.getBank_name()%>');
                    $("#txtAccNo").val('<%= ben.getBank_account()%>');
                    $("#txtIfsCode").val('<%= ben.getIfsc()%>');
                    
                    
                }
            };
        </script>
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
                    <form id="frmBeneficiaryData" action="../SaveBeneficiaryDetails" method="post"> 


                        <div class="card">
                            <div class="card-header bg-info"> 
                                <h5 class="text-white">Personal Details:</h5>
                            </div>                                
                            <div class="card-body bg-form">
                                <div class="row">
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtBeneficiaryName">Beneficiary Name</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-female"></i></span>
                                            </div>
                                            <input type="hidden" id="hdnEdit" name="hdnEdit" value="<%= edit%>" />                                            
                                            <input type="hidden" id="hdnBenId" name="hdnBenId" value="<%= benId%>" />
                                            <input type="text" pattern="[^\s][A-Za-z][A-Za-z\s\.]*" id="txtBeneficiaryName" name="txtBeneficiaryName" size="120" class="form-control form-control-sm" required="required"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtFathersName">Father's Name</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-male"></i></span>
                                            </div>                                            
                                            <input type="text" pattern="[^\s][A-Za-z][A-Za-z\s\.]*" id="txtFathersName" name="txtFathersName" size="120" class="form-control form-control-sm" required="required"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtMobile">Date of Birth</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-calendar fa-lg"></i></span>
                                            </div>
                                            <input type="date" id="txtDateOfBirth" name="txtDateOfBirth" class="form-control form-control-sm" required="required"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="radMale">Gender</label>
                                        <div class="input-group mb-3">
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio"id="radMale" name="radGender" value="Male"  required="required">
                                                <label for="radMale"><i class="fa fa-male">&nbsp;</i> Male </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radFemale" name="radGender" value="Female"  required="required">
                                                <label for="radFemale"><i class="fa fa-female">&nbsp;</i> Female </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radTrans" name="radGender" value="Trans"  required="required">
                                                <label for="radTrans">Transgender </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="radUr">Caste</label>
                                        <div class="input-group mb-3">                                            
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radSc" name="radCategory" value="SC"  required="required">
                                                <label for="radSc"> SC </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radSt" name="radCategory" value="ST"  required="required">
                                                <label for="radSt"> ST </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radObc" name="radCategory" value="OBC"  required="required">
                                                <label for="radObc"> OBC </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radOth" name="radCategory" value="Others"  required="required">
                                                <label for="radOth"> Others </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtMobile">Mobile Number</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-mobile fa-lg"></i></span>
                                            </div>
                                            <input type="tel" id="txtMobile" name="txtMobile" pattern="[6-9]{1}[0-9]{9}" class="form-control form-control-sm" required="required"/>
                                        </div> 
                                    </div>
                                </div>
                                <div class="row">                                    
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtRationCardNo">Ration Card Number</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                            </div>
                                            <input type="text" id="txtRationCardNo" name="txtRationCardNo" pattern="[0-9]*" class="form-control form-control-sm" required="required"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtRationCardMem">Ration Card Member ID</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                            </div>
                                            <input type="text" id="txtRationCardMem" name="txtRationCardMem" pattern="[0-9]*" class="form-control form-control-sm" required="required"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="radHofYes">Head of the Family</label>
                                        <div class="input-group mb-3">
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio"id="radHofYes" name="radHof" value="true"  required="required">
                                                <label for="radHofYes"> Yes </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radHofNo" name="radHof" value="false"  required="required">
                                                <label for="radHofNo"> No </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="txtUIDCardNo">ID Card Number</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                            </div>
                                            <input type="text" id="txtUIDCardNo" name="txtUIDCardNo" class="form-control form-control-sm"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                        <label for="radUr">Is PMKISAN ID Available</label>
                                        <div class="input-group mb-3">                                            
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radYes" name="radPmkisanAvl" value="true" onclick="return isPmkisanIdAvl();">
                                                <label for="radYes"> Yes </label>
                                            </div>
                                            <div class="radio icheck-material-blue mr-1">
                                                <input type="radio" id="radNo" name="radPmkisanAvl" value="false" onclick="return isPmkisanIdAvl();">
                                                <label for="radNo"> No </label>
                                            </div>                                                
                                        </div>
                                    </div>
                                    <div id="pmkisaIdDiv" class="form-group  col-sm-4 col-md-3 col-lg-2 d-none">
                                        <label for="txtPMKISANId">PMKISAN ID</label>
                                        <div class="input-group mb-3">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                            </div>
                                            <input type="text" id="txtPMKISANId" name="txtPMKISANId" class="form-control form-control-sm" />
                                        </div>
                                    </div>
                                </div>
                            </div>                                              
                        </div>
                        <div id="accordion">
                            <div class="card">
                                <div class="card-header bg-info">
                                    <a class="card-link" data-toggle="collapse" href="#collapseBenAddr">
                                        <h5 class="text-white">Beneficiary Address Details:</h5>
                                    </a>
                                </div> 
                                <div id="collapseBenAddr" class="collapse" data-parent="#accordion">
                                    <div class="card-body bg-form">
                                        <div class="form-group">                                    
                                            <div class="row">                                        
                                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                    <label for="ddlDistrict1">District</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlDistrict1" name="ddlDistrict1" class="form-control form-control-sm" onchange="dependentDropDown('ddlDistrict1', 'ddlSubDiv1', 'fillSubDiv');
                                                                dependentDropDown('ddlDistrict1', 'ddlPs', 'fillPoliceStation');
                                                                resetBlock(1);">
                                                            <%=ddlDist%>                                                    
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group col-sm-4 col-md-3 col-lg-2 ">
                                                    <label for="ddlSubDiv1">Sub-Division</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlSubDiv1" name="ddlSubDiv1" class="form-control form-control-sm" onchange="dependentDropDown('ddlSubDiv1', 'ddlPo', 'fillPostOffice');
                                                                resetBlock(2);">
                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                    <label for="ddlLocType">Location Type</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>                                                    
                                                        <select id="ddlLocType" name="ddlLocType" class="form-control form-control-sm" onchange="dependentDropDown1('ddlSubDiv1', 'ddlLocType', 'ddlUrbRurBody', 'fillUrbRurBody');
                                                                resetBlock(3);">
                                                            <%=ddlLoc%>                                                 
                                                        </select>
                                                    </div>
                                                </div>

                                                <div class="form-group col-sm-4 col-md-3 col-lg-2 ">
                                                    <label for="ddlUrbRurBody">MC/Block/ADC Block</label>   
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlUrbRurBody" name="ddlUrbRurBody" class="form-control form-control-sm" onchange="dependentDropDown1('ddlUrbRurBody', 'ddlLocType', 'ddlWardGp', 'fillWardGp');" >
                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                    <label for="ddlWardGp">Ward/GP/VC</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlWardGp" name="ddlWardGp" class="form-control form-control-sm" >
                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                            </div> 
                                            <div class="row">                                                
                                                <div class="form-group  col-sm-4 col-md-3 col-lg-2 ">
                                                    <label for="txtAddrOth">Area Name/ House No.</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <input type="text" id="txtAddrOth" name="txtAddrOth" class="form-control form-control-sm" />
                                                    </div>
                                                </div>
                                                <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                    <label for="ddlPs">Police Station</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlPs" name="ddlPs" class="form-control form-control-sm">
                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                                    <label for="ddlPo">Post Office</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <select id="ddlPo" name="ddlPo" class="form-control form-control-sm" onchange="setPin();">
                                                            <option value="0">-Please Select-</option>                                                 
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                                    <label for="txtAddrPIN">PIN</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"></span>
                                                        </div>
                                                        <input type="text" id="txtAddrPIN" name="txtAddrPIN" class="form-control form-control-sm" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>                                
                                    </div>   
                                </div>
                            </div> 
                        </div>
                        <div class="card">
                            <div class="card-header bg-info">
                                <a class="card-link" data-toggle="collapse" href="#collapseBenBankAcc">
                                    <h5 class="text-white">Beneficiary Bank Account Details:</h5>
                                </a>
                            </div> 
                            <div id="collapseBenBankAcc" class="collapse" data-parent="#accordion">
                                <div class="card-body bg-form">
                                    <div class="form-group">                                    
                                        <div class="row">
                                            <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtAddrPIN">Bank Name</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1"></span>
                                                    </div>
                                                    <input type="text" id="txtBankName" name="txtBankName" class="form-control form-control-sm" />
                                                </div>
                                            </div>
                                            <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtAddrPIN">Account Number</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1"></span>
                                                    </div>
                                                    <input type="text" id="txtAccNo" name="txtAccNo" class="form-control form-control-sm" />
                                                </div>
                                            </div>     
                                            <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                                <label for="txtAddrPIN">IFS Code</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1"></span>
                                                    </div>
                                                    <input type="text" id="txtIfsCode" name="txtIfsCode" class="form-control form-control-sm" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card">
                            <div class="card-header bg-info">
                                <a class="card-link" data-toggle="collapse" href="#collapseIdentification">
                                    <h5 class="text-white">Other Identification Details:</h5>
                                </a>
                            </div>
                            <div id="collapseIdentification" class="collapse" data-parent="#accordion">
                                <div class="card-body bg-form">
                                    <div class="row">                                            
                                        <div class="col-md-12">
                                            <button type="button" id="btnAddMoreId" name="btnAddMoreId" value ="1" class="btn btn-primary btn-sm addmore float-right">
                                                <i class="fa fa-plus"> Add More</i>
                                            </button>
                                        </div>
                                    </div>
                                    <div class="row">                                            
                                        <table id="itemListAddlIds" class="table table-sm table-striped table-bordered small">
                                            <thead>
                                                <tr>
                                                    <th>Additional ID Type</th>
                                                    <th>Value</th>                                                        
                                                    <th>                                                            
                                                        <input type="hidden" id="hdnAddlIdCount" name="hdnAddlIdCount" value="1" />
                                                    </th>
                                                </tr>
                                            </thead>
                                            <tbody id="itemListAddlIds_tbody">
                                                <tr id="1">                                                        
                                                    <td>
                                                        <div class="input-group">
                                                            <select id="ddlAddlIdType1" name="ddlAddlIdType1" class="form-control form-control-sm">
                                                                <%= ddlAdditionalId%>
                                                            </select>
                                                        </div>
                                                    </td>
                                                    <td>                                                                    
                                                        <div class="input-group">
                                                            <input type="text" id="txtAddlIdValue1" name="txtAddlIdValue1" class="form-control form-control-sm" placeholder=""  />
                                                        </div>
                                                    </td>                                                        
                                                    <td></td>
                                                </tr>
                                            </tbody>
                                        </table>

                                        <div style="display:none;">
                                            <table id="sample_table_addl_ids">
                                                <tr id="">
                                                    <td>
                                                        <div class="input-group">
                                                            <select id="ddlAddlIdType" name="ddlAddlIdType" class="form-control form-control-sm">
                                                                <%= ddlAdditionalId%>
                                                            </select>
                                                        </div>
                                                    </td>
                                                    <td>                                                                    
                                                        <div class="input-group">
                                                            <input type="text" id="txtAddlIdValue" name="txtAddlIdValue" class="form-control form-control-sm" placeholder=""  />
                                                        </div>
                                                    </td>
                                                    <td><a class="btn btn-xs delete-record2" data-id="1"><i class="fa fa-trash"></i></a></td>
                                                </tr>
                                            </table>
                                        </div>

                                    </div> 


                                    <!-- <div class="row">
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtPANCardNo">PAN Card Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtPANCardNo" name="txtPANCardNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                        <div class="form-group col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtEpicNo">EPIC Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtEpicNo" name="txtEpicNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtDrivingLicenseNo">Driving License Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtDrivingLicenseNo" name="txtDrivingLicenseNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtMNREGAJobCardNo">MNREGA Job Card Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtMNREGAJobCardNo" name="txtMNREGAJobCardNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtMNREGAJobCardMem">MNREGA Job Card Member ID</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtMNREGAJobCardMem" name="txtMNREGAJobCardMem" class="form-control form-control-sm"/>
                                            </div>
                                        </div>                                     

                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtKisanCreditCardNo">Kisan Credit Card Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtKisanCreditCardNo" name="txtKisanCreditCardNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">                                           

                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtSoilHealthCardNo">Soil Health Card Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtSoilHealthCardNo" name="txtSoilHealthCardNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtCropInsuranceNo">Crop Insurance Number</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-sort-numeric-asc"></i></span>
                                                </div>
                                                <input type="text" id="txtCropInsuranceNo" name="txtCropInsuranceNo" class="form-control form-control-sm"/>
                                            </div>
                                        </div> 
                                    </div> -->
                                </div>
                            </div>
                        </div>  

                        <div class="card">
                            <div class="card-header bg-info">
                                <a class="card-link" data-toggle="collapse" href="#collapseOthPersonal">
                                    <h5 class="text-white">Other Personal Details:</h5>
                                </a>
                            </div> 
                            <div id="collapseOthPersonal" class="collapse" data-parent="#accordion">
                                <div class="card-body bg-form">
                                    <div class="row">
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtEmail">Email ID</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-envelope"></i></span>
                                                </div>
                                                <input type="email" id="txtEmail" name="txtEmail" class="form-control form-control-sm" onchange = "return emailCheck();"/>
                                            </div> 
                                        </div>                                    
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="ddlQual">Educational Qualification</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-graduation-cap"></i></span>
                                                </div>
                                                <select id="ddlQual" name="ddlQual" class="form-control form-control-sm" >
                                                    <option value="0">--Select--</option>
                                                    <option value="Pre Matriculation">Pre Matriculation</option>
                                                    <option value="Matriculation">Matriculation</option>
                                                    <option value="Higher Secondary">Higher Secondary</option>
                                                    <option value="Graduation">Graduation</option>  
                                                    <option value="Post Graduation">Post Graduation</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtAnnualIncome">Annual Income</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-mobile fa-lg"></i></span>
                                                </div>
                                                <input type="text" id="txtAnnualIncome" name="txtAnnualIncome" class="form-control form-control-sm"/>
                                            </div> 
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtOccupation">Occupation</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-envelope"></i></span>
                                                </div>
                                                <input type="text" id="txtOccupation" name="txtOccupation" class="form-control form-control-sm"/>
                                            </div> 
                                        </div>
                                        <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                            <label for="txtReligion">Religion</label>
                                            <div class="input-group mb-3">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-globe"></i></span>
                                                </div>
                                                <input type="text" pattern="[^\s][A-Za-z]+" id="txtReligion" name="txtReligion" size="30" class="form-control form-control-sm"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row mt-2">
                            <div class="form-group  col-sm-4 col-md-3 col-lg-2">
                                <button type="submit" id="btnRegister" name="btnRegister" class="btn btn-primary">
                                    <i class="fa fa-user-plus">&nbsp;&nbsp;</i>Save
                                </button>
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
                                                        $('#txtPMKISANId').on('change', function (e) {
                                                            var pmkisanid = $('#txtPMKISANId').val();
                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/AjaxServlet?searchBenAccByPmkisan=1&pmkisanId=" + pmkisanid,
                                                                type: "POST",
                                                                success: function (result) {
                                                                    if (result !== '') {
                                                                        var obj = $.parseJSON(result);
                                                                        if (!($('#collapseBenBankAcc').hasClass("show"))) {
                                                                            $('#collapseBenBankAcc').addClass("show");
                                                                        }
                                                                        $('#txtAccNo').val(obj[0].bank_account);
                                                                        $('#txtIfsCode').val(obj[0].ifsc);
                                                                    }

                                                                }
                                                            });
                                                        });
                                                        
                                                        
                                                        
                                                        $(document).delegate('#btnAddMoreId', 'click', function (e) {
                                                            e.preventDefault();
                                                            var content = jQuery('#sample_table_addl_ids tr'),
                                                                    size = jQuery('#itemListAddlIds >tbody >tr').length + 1,
                                                                    element = null,
                                                                    element = content.clone();
                                                            element.attr('id', size);


                                                            element.find('#ddlAddlIdType').attr('id', 'ddlAddlIdType' + size);
                                                            element.find('#ddlAddlIdType' + size).attr('name', 'ddlAddlIdType' + size);

                                                            element.find('#txtAddlIdValue').attr('id', 'txtAddlIdValue' + size);
                                                            element.find('#txtAddlIdValue' + size).attr('name', 'txtAddlIdValue' + size);

                                                            element.find('.delete-record').attr('data-id', size);
                                                            element.appendTo('#itemListAddlIds_tbody');
                                                            $('#hdnAddlIdCount').val(size);
                                                        });
                                                        
                                                        $(document).delegate('a.delete-record2', 'click', function (e) {
                                                            e.preventDefault();
                                                            var didConfirm = confirm("Are you sure You want to delete");
                                                            if (didConfirm === true) {
                                                                var id = jQuery(this).attr('data-id');
                                                                var rowCount = $('#hdnAddlIdCount').val();
                                                                jQuery('#' + id).remove();

                                                                //regnerate index number on table
                                                                $('#itemListAddlIds_tbody tr').each(function (index) {
                                                                    if (index + 1 > 0) {
                                                                        if (index + 1 >= id) {
                                                                            $(this).attr('id', index + 1);

                                                                           
                                                                            $(this).find('#ddlAddlIdType' + (index + 2)).attr('id', 'ddlAddlIdType' + (index + 1));
                                                                            $(this).find('#ddlAddlIdType' + (index + 1)).attr('name', 'ddlAddlIdType' + (index + 1));

                                                                            $(this).find('#txtAddlIdValue' + (index + 2)).attr('id', 'txtAddlIdValue' + (index + 1));
                                                                            $(this).find('#txtAddlIdValue' + (index + 1)).attr('name', 'txtAddlIdValue' + (index + 1));
                                                                            
                                                                            $(this).find('.delete-record').attr('data-id', index + 1);
                                                                        }
                                                                    }
                                                                });
                                                                $('#hdnAddlIdCount').val(rowCount - 1);

                                                                return true;
                                                            } else {
                                                                return false;
                                                            }
                                                        });
                                                        
                                                        
                                                        
                                                        
                                                    }
                                                    );
</script>
<%
    // }

%>