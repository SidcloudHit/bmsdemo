<%-- 
    Document   : forgotPassChange
    Created on : 16 Apr, 2020, 12:04:34 AM
    Author     : Arindam Mukherjee
--%>

<%@page import="Common.CommonMethod"%>
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
<%
    String user_email = "";
    if (session.getAttribute("email") != null) {
        user_email = session.getAttribute("email").toString();

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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Reset Password Page </title>
        <!-- Latest compiled and minified CSS -->
        <link href="${pageContext.request.contextPath}/CSS/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/CSS/font-awesome.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row mt-2 mb-2">

                    <div class="col-sm-6 mt-2 mb-3 ml-auto mr-auto">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="text-primary">Reset User Password</h4>
                            </div>
                            <div class="card-body bg-form">
                            <%                                if (!regMsg.equals("")) {
                            %>
                            <div class="alert alert-<%=type%>">
                                <strong><%=regMsg%></strong>
                            </div>
                            <%
                                }
                            %>
                            <form id="frmChangePassword" action="../forgotChangePassword">
                                <input type="hidden" name="user_email" value="<%= user_email%>" />   
                                <div class="form-group">
                                    <label for="txtPwd">New Password</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock fa-lg"></i></span>
                                        </div>
                                        <input type="password" id="txtPwd" name="txtPwd" class="form-control form-control-sm" data-toggle="popover" placeholder="Type Your Password" required="required" title="Password Strength" data-content="Enter Password..."/>
                                    </div>                                     
                                </div>
                                <div class="form-group">
                                    <label for="txtCnfPwd">Confirm Password</label>
                                    <div class="input-group mb-3">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-lock fa-lg"></i></span>
                                        </div>
                                        <input type="password" id="txtCnfPwd" name="txtCnfPwd" class="form-control form-control-sm" placeholder="Re-Type Your Password"  data-toggle="popover"  required="required"  title="Password Match" data-content="Enter Confirm Password..."/>
                                    </div>
                                </div>
                                <!--                                <div class="form-group">
                                                                    <label for="txtLoginCaptcha">Verification Code</label>
                                                                    <div><img src="../CaptchaGen" border="0" /></div>
                                                                    <div class="input-group mb-3">
                                                                        <div class="input-group-prepend">
                                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-key fa-lg"></i></span>
                                                                        </div>
                                                                        <input type="text" id="txtLoginCaptcha" name="txtLoginCaptcha" class="form-control form-control-sm" placeholder="Enter Verification Code Here" required="required"/>
                                                                    </div>
                                                                </div>                                -->
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
                                    <button type="submit" id="btnChangePassword" name="btnChangePassword" class="btn btn-primary">
                                        Change Password
                                    </button>
                                </div> 
                            </form>
                        </div>
                    </div>
                </div>

            </div>
            <jsp:include page="../Web/footer.jsp"></jsp:include>
            </div>
        </body>
        <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>

    <!-- common JavaScript functions-->
    <script type="text/javascript">
                                            function getCaptcha() {
                                                        $.ajax({
                                                            url: "${pageContext.request.contextPath}/AjaxServlet?captcha=1",
                                                            type: "POST",
                                                            success: function (data) {
                                                                $('#captcha').html(data);
                                                            }
                                                        });
                                                    }
                                                    $(document).ready(function () {
                                                     getCaptcha();
                                                //minimum 8 characters
                                                var bad = /(?=.{8,}).*/;
                                                //Alpha Numeric plus minimum 8
                                                var good = /^(?=\S*?[a-z])(?=\S*?[0-9])\S{8,}$/;
                                                //Must contain at least one upper case letter, one lower case letter and (one number OR one special char).
                                                var better = /^(?=\S*?[A-Z])(?=\S*?[a-z])((?=\S*?[0-9])|(?=\S*?[^\w\*]))\S{8,}$/;
                                                //Must contain at least one upper case letter, one lower case letter and (one number AND one special char).
                                                var best = /^(?=\S*?[A-Z])(?=\S*?[a-z])(?=\S*?[0-9])(?=\S*?[^\w\*])\S{8,}$/;

                                                $('#txtPwd').on('keyup', function () {
                                                    var password = $(this);
                                                    var pass = password.val();
                                                    //var passLabel = $('[for="txtPwd"]');
                                                    var stength = 'Weak';
                                                    var pclass = 'danger';
                                                    if (best.test(pass) === true) {
                                                        stength = 'Very Strong';
                                                        pclass = 'success';
                                                    } else if (better.test(pass) === true) {
                                                        stength = 'Strong';
                                                        pclass = 'warning';
                                                    } else if (good.test(pass) === true) {
                                                        stength = 'Almost Strong';
                                                        pclass = 'warning';
                                                    } else if (bad.test(pass) === true) {
                                                        stength = 'Weak';
                                                    } else {
                                                        stength = 'Very Weak';
                                                    }

                                                    var popover = password.attr('data-content', stength).data('bs.popover');
                                                    popover.setContent();
                                                    popover.$tip.addClass(popover.options.placement).removeClass('danger success info warning primary').addClass(pclass);

                                                });


                                                $('#txtCnfPwd').on('keyup', function () {
                                                    var match = "No match";
                                                    var cnfPwd = $(this).val();
                                                    var pwd = $('#txtPwd').val();
                                                    if (cnfPwd === pwd) {
                                                        match = "Match";
                                                    } else {
                                                        match = "No match";
                                                    }
                                                    var popover = $(this).attr('data-content', match).data('bs.popover');
                                                    popover.setContent();
                                                    popover.$tip.addClass(popover.options.placement);

                                                });

                                                $('input[data-toggle="popover"]').popover({
                                                    placement: 'right',
                                                    trigger: 'focus'
                                                });

                                            });
    </script>

</html>
<% } else {
          response.sendRedirect("../index.jsp");
      }%>