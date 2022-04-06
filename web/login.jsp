<%-- 
    Document   : index
    Created on : 18 Dec, 2019, 5:49:52 PM
    Author     : acer
--%>
<%@page import="java.security.SecureRandom"%>
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
    //Testing Git
    String userId = "";
    if (session.getAttribute("loginInfo") != null) {

        SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = loginObj.getUserId();
    }

    if (!userId.equals("")) {
        response.sendRedirect("User/userHome.jsp");
    }

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

%>


<%    SecureRandom srnd = new SecureRandom();
    int saltLen = srnd.nextInt(10000);
    String strsalt = String.valueOf(saltLen);

    String hashsalt_hidden = "";

    Security.SecurityClass sc = new Security.SecurityClass();
    hashsalt_hidden = sc.gethexString(strsalt);

    session.setAttribute("hashsaltinsession", hashsalt_hidden);

    hashsalt_hidden = sc.gethexString(hashsalt_hidden.trim().concat("NICCIN"));

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Beneficiary Management System</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <!-- Custom CSS -->
        <link href="${pageContext.request.contextPath}/CSS/startmin.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/bootstrap-theme.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/metisMenu.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/timeline.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/morris.css" rel="stylesheet">


        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/sha.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div style="width: 0;height: 0;"> <jsp:include page="/Web/sidebar.jsp"></jsp:include>  </div>

                    <div class="col-sm-12">                        


                    <%                        if (!regMsg.equals("")) {
                    %>
                    <div class="card mt-2">                            
                        <div class="card-body">
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                        </div>                        
                    </div>
                    <%
                        }
                    %>




                    <!-- /.row -->
                    <div class="row mt-2">

                        <!-- /.panel-heading -->
                        <!--<div class="card-body">
                            <div id="morris-area-chart"></div>
                        </div>-->
                        <!-- /.panel-body -->
                        <!--</div>
                    </div>-->
                        <div class="col-lg-4 col-md-4"></div>
                        <div class="col-lg-4 col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h3>Department Login</h3>
                                </div>
                                <div class="card-body">
                                    <%
                                        if (!logMsg.equals("")) {
                                    %>
                                    <div class="alert alert-<%=type%>">
                                        <strong><%=logMsg%></strong>
                                    </div>
                                    <%
                                        }
                                    %>
                                    <form id="frmLogin" action="Login" method="post" onsubmit="return converthash();">
                                        <fieldset>

                                            <div class="form-group">
                                                <label for="txtUserName">Mobile/Email/Login ID</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1">@</span>
                                                    </div>
                                                    <input type="text" id="txtUserName" name="txtUserName" class="form-control form-control-sm" placeholder="User ID or Email ID" required="required"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="txtPwd">Password</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock fa-lg"></i></span>
                                                    </div>
                                                    <input type="password" id="txtUserPwd" name="txtUserPwd" class="form-control form-control-sm" placeholder="Password"  required="required"/>
                                                </div>
                                            </div>
                                            <!--                                            <div class="form-group">
                                                                                            <label for="txtLoginCaptcha">Verification Code</label>
                                                                                            <div><img src="CaptchaGen" border="0" /></div>
                                                                                            <div class="input-group mb-3 mt-1">
                                                                                                <div class="input-group-prepend">
                                                                                                    <span class="input-group-text" id="basic-addon1"><i class="fa fa-key fa-lg"></i></span>
                                                                                                </div>
                                                                                                <input type="text" id="txtLoginCaptcha" name="txtLoginCaptcha" class="form-control form-control-sm" placeholder="Enter Verification Code Here" autocomplete="off" required="required"/>
                                                                                            </div>
                                                                                        </div>-->
                                            <div class="form-group">
                                                <label for="txtLoginCaptcha">Answer the following question.</label>

                                                <div class="row">
                                                    <div id="captcha_refresh" class="col-md-1" onclick="getCaptcha();"><i class="fa fa-refresh" aria-hidden="true"></i></div>
                                                    <div class="col4">
                                                        <span style="background: #000000b0;color: #fff;padding: 5px;" id="captcha"></span>
                                                        <input type="text" id="txtLoginCaptcha" name="txtLoginCaptcha" class="form-control-sm border-1" placeholder="?" required="required"  autocomplete="off" style="width: 70px;border: 1px solid #c2c2c2;"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <input type="hidden" class="form-control"  name="txthashkey" id="txthashkey" value="<%=hashsalt_hidden%>" >
                                                <button type="submit" id="btnLogin" name="btnLogin" class="btn btn-success btn-block">
                                                    <i class="fa fa-sign-in">&nbsp;</i>Login
                                                </button>
<!--                                                <a href="User/forgotPassword.jsp" target="_blank">
                                                    <button type="button" id="btnForgotPassword" name="btnForgotPassword" class="btn btn-danger btn-block mt-1">
                                                        <i class="fa fa-lock">&nbsp;</i>Forgot Password
                                                    </button>
                                                </a>-->
                                            </div>


                                        </fieldset>
                                    </form>


                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4 col-md-4"></div>
                    </div>
                </div>


            </div>                 

            <jsp:include page="Web/footer.jsp"></jsp:include>
            </div>
        </body>
    </html>
    <!-- jQuery library -->
    <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/JS/jquery.redirect.js"></script>

<!-- Popper JS -->
<script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="${pageContext.request.contextPath}/JS/metisMenu.min.js"></script>

<!-- Morris Charts JavaScript -->
<script src="${pageContext.request.contextPath}/JS/raphael.min.js"></script>
<script src="${pageContext.request.contextPath}/JS/morris.min.js"></script>
<script src="${pageContext.request.contextPath}/JS/morris-data.js"></script>

<!-- Custom Theme JavaScript -->
<script src="../js/startmin.js"></script>
<script>
                                                    $(document).ready(function () {
                                                        //$.redirect("${pageContext.request.contextPath}/DEO/beneficiaryCheckFromRC.jsp", null, "POST", "_blank", null, true); //JSON.stringify(params)      
                                                    });
                                                    
                                                    
                                                    $(document).ready(function () {
                                                        //$.redirect("${pageContext.request.contextPath}/DEO/beneficiaryCheckFromRC.jsp", null, "POST", "_blank", null, true); //JSON.stringify(params)      
                                                        getCaptcha();

                                                    });
                                                    function getCaptcha() {
                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/AjaxServlet?captcha=1",
                                                            type: "POST",
                                                            success: function (data) {
                                                                $('#captcha').html(data);
                                                            }
                                                        });
                                                    }
</script>