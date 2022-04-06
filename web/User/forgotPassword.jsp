<%-- 
    Document   : forgetPassword.jsp
    Created on : 13 Feb, 2021, 12:15:15 PM
    Author     : Banti
--%>

<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
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
    String user_email = "", user_mobile = "", mask_mobile = "";
//    if (session.getAttribute("email") != null) {
//        user_email = session.getAttribute("email").toString();
//    }
//    if (session.getAttribute("mobile") != null) {
//        String regex = ".(?=.{4})";
//        String subst = "*";
//        user_mobile = session.getAttribute("mobile").toString();
//        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
//        Matcher matcher = pattern.matcher(user_mobile);
//        mask_mobile = matcher.replaceAll(subst);
//    }

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
    SecureRandom srnd = new SecureRandom();
    int saltLen = srnd.nextInt(10000);
    String strsalt = String.valueOf(saltLen);


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Beneficiary Management System</title>
        <!-- Latest compiled and minified CSS -->
        <link href="${pageContext.request.contextPath}/CSS/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/font-awesome.min.css" rel="stylesheet">
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
            <jsp:include page="../Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">
                        <div class="row">
                        </div>
                        <div class="card mt-2 d-none">
                            <div class="card-header">
                                <h4 class="text-primary"></h4>
                            </div>
                            <div class="card-body">
                            </div>                        
                        </div>
                        <!-- /.row -->
                        <div class="row mt-2">

                            <div class="col-lg-4 col-md-4"></div>
                            <div class="col-lg-4 col-md-4">
                                <div class="card">
                                    <div class="card-header">
                                        <h3>Forgot Password</h3>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-body bg-form">
                                        <%                                if (!regMsg.equals("")) {
                                        %>
                                        <div class="alert alert-<%=type%>">
                                            <strong><%=regMsg%></strong>
                                        </div>
                                        <%
                                            }
                                        %>
                                        <div id="error"></div>
                                        <div id="user_panel">
                                            <form id="forget" action="" method="post">
                                                <input type="hidden" name="flag" value="1" />
                                                <div class="form-group">
                                                    <label for="txtUserName">Email ID or Mobile No.</label>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1">@</span>
                                                        </div>
                                                        <input type="text" id="user_email"  name="user_email" class="form-control form-control-sm" placeholder="Enter email id or mobile no." required="required"  autocomplete="off"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <button type="button" id="btnForget" name="btnForget" class="btn btn-success btn-block" onclick="getUserDetails();">
                                                        <i class="fa fa-sign-in">&nbsp;</i>Next
                                                    </button>

                                                </div>
                                            </form>
                                        </div>

                                        <form id="forgetNext" action="../forgetPassword" method="post" style="display:none;">
                                            <input type="hidden" name="flag" value="2" />
                                            <input type="hidden" name="user_email" value="" id="user_email_id" />
                                            <div class="alert" id="success_msg">
                                                <!--                                                    <strong>OTP has been sent to your email id: <span id="email_otp"></span>. and your Mobile No: <span id="mobile_otp"></span>.</strong>-->
                                            </div>
                                            <div class="form-group">
                                                <label for="txtUserName">Email OTP</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1">@</span>
                                                    </div>
                                                    <input type="password" id="txtemailotp" name="txtemailotp" class="form-control form-control-sm" data-toggle="popover" placeholder="Enter Email OTP" required="required"  autocomplete="off"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="txtPwd">Mobile OTP</label>
                                                <div class="input-group mb-3">
                                                    <div class="input-group-prepend">
                                                        <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock fa-lg"></i></span>
                                                    </div>
                                                    <input type="password" id="txtmobileotp" name="txtmobileotp" class="form-control form-control-sm" data-toggle="popover" placeholder="Enter Mobile OTP"  required="required"  autocomplete="off"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <button type="submit" id="btnForgetotp" name="btnForgetotp" class="btn btn-success btn-block">
                                                    <i class="fa fa-sign-in">&nbsp;</i>Submit
                                                </button>

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4"></div>
                    </div>


                </div>                 
            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inputmask/3.3.4/jquery.inputmask.bundle.min.js"></script>


<!-- Custom Theme JavaScript -->
<script src="${pageContext.request.contextPath}/JS/startmin.js"></script>
<script>
                                                        function getUserDetails() {
                                                            $("#btnForget").prop('disabled', true);
                                                            var user_email = $("#user_email").val();
                                                            var flag = 1;
                                                            $.ajax({
                                                                url: "${pageContext.request.contextPath}/forgetPassword?flag=" + flag + "&user_email=" + user_email,
                                                                type: "POST",
                                                                success: function (data) {
                                                                    if (data == "notok") {
                                                                        $("#error").html("Email Id Not Found");
                                                                        $("#forgetNext").css("display", "none");
                                                                        $("#forget").css("display", "block");

                                                                    } else {
                                                                        $("#forget").css("display", "none");
                                                                        $("#forgetNext").css("display", "block");
                                                                        $("#user_email_id").val(user_email);
                                                                        // $("#email_otp").html(user_email);
//                                                                            var mobile_no = data;
//                                                                            var first = mobile_no.substring(0, 6);
//                                                                            var last = mobile_no.substring(mobile_no.length - 4);
//
//                                                                            var mask = mobile_no.substring(0, mobile_no.length - 4).replace(/\d/g, "*");
//                                                                           // alert($("#mobile_otp").inputmask(data));
//                                                                            $("#mobile_otp").html(mask + last);
                                                                        $("#success_msg").html(data);

                                                                    }
                                                                }
                                                            });
                                                        }
</script>
