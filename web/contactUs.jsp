<%-- 
    Document   : contactus
    Created on : 21 Jul, 2018, 6:37:56 PM
    Author     : acer
--%>

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
        <title>TRBT || Contact Us </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    </head>
    <body>
        <div class="container bg-light">
            <jsp:include page="Web/header.jsp"></jsp:include>
                <div class="row mt-2 mb-2 vdivide">
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="text-primary">Contact Us</h4>
                            </div>
                            <div class="card-body bg-page font-weight-bold">
                                <p class="card-text">Teachers' Recruitement Board, Tripura </p>
                                <p class="card-text">Education (School) Department, Government of Tripura</p>
                                <p class="card-text">Address: Shiksha Bhavan, Office Lane</p>
                                <p class="card-text">Agartala 799001</p>                                
                                <p class="card-text">Phone: <a href="callto:03812304868">0381-2304868</a></p>
                                <p class="card-text">Email: <a href="mailto:trbtripura@rediffmail.com">trbtripura[AT]rediffmail[DOT]com</a></p>
                            </div>                        
                        </div>
                    </div>                 
                </div>
            <jsp:include page="Web/footer.jsp"></jsp:include>
        </div>
    </body>
    
    <!-- jQuery library -->
        <script src="${pageContext.request.contextPath}/JS/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="${pageContext.request.contextPath}/JS/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="${pageContext.request.contextPath}/JS/bootstrap.min.js"></script>
</html>
