<%-- 
    Document   : setPassword
    Created on : 26 Oct, 2021, 3:39:12 PM
    Author     : acer
--%>

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
    int deptId = 0, schemeId = 0, roleId = 0, officeId = 0;
    String userId = "", userName = "", userDes = "", user_type="";

    if (session.getAttribute("firstloginuserid") != null) {

       // SessionPackage.LoginClass loginObj = (SessionPackage.LoginClass) session.getAttribute("loginInfo");

        userId = session.getAttribute("firstloginuserid").toString();
        user_type= session.getAttribute("userType").toString();
//        deptId = Integer.parseInt(loginObj.getUserDeptId());
//        userName = loginObj.getUserName();
//        userDes = loginObj.getUserDesignation();
//        roleId = Integer.parseInt(loginObj.getUserRole());
//        officeId = Integer.parseInt(loginObj.getUserOfficeId());

    }

    if (!userId.equals("")) {
        

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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>BMS || Set Password </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">
                    <div style="width: 0;height: 0;"> <jsp:include page="../Web/sidebar.jsp"></jsp:include>  </div>
                    <div class="col-md-12">

                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header bg-info">
                                        <h4 class="text-white"> Set New Password</h4>
                                    </div>
                                    <div class="card-body bg-light">
                                    <%                                if (!regMsg.equals("")) {
                                    %>
                                    <div class="alert alert-<%=type%>">
                                        <strong><%=regMsg%></strong>
                                    </div>
                                    <%
                                        }
                                    %>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <form id="frmChangePassword" action="../setPassword">

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
                                                <div class="form-group">
                                                    <label for="txtLoginCaptcha">Verification Code</label>
                                                    <div><img src="../CaptchaGen" border="0" /></div>
                                                    <div class="input-group mb-3">
                                                        <div class="input-group-prepend">
                                                            <span class="input-group-text" id="basic-addon1"><i class="fa fa-key fa-lg"></i></span>
                                                        </div>
                                                        <input type="text" id="txtLoginCaptcha" name="txtLoginCaptcha" class="form-control form-control-sm" placeholder="Enter Verification Code Here" required="required"/>
                                                    </div>
                                                </div>                                
                                                <div class="form-group">
                                                    <input type="hidden" class="form-control"  name="txtUid" id="txtUid" value="<%=userId%>" >
                                                    <input type="hidden" class="form-control"  name="txtUType" id="txtUType" value="<%=user_type%>" >
                                                    <button type="submit" id="btnResetPassword" name="btnResetPassword" class="btn btn-primary">
                                                        Change Password
                                                    </button>
                                                  
                                                </div> 
                                            </form>
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
        <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>

    <!-- common JavaScript functions-->
    <script type="text/javascript">
        $(document).ready(function () {

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
<%    } else {
        response.sendRedirect("../login.jsp");
    }
%>
