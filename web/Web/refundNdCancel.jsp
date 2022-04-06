<%-- 
    Document   : refundNdCancel
    Created on : 15 Jun, 2016, 1:32:41 PM
    Author     : S.Lodh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>TRBT || Refund and Cancellation Policy  </title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
    </head>
    <body>
        <div class="container">
            <jsp:include page="../Web/header.jsp"></jsp:include>
                <div class="row">                    
                    <div class="col mt-2 mb-3">                    
                        <div class="card">
                            <div class="card-header">
                                <h4 class="text-primary">Refund and Cancellation Policy:</h4>
                            </div>
                            <div class="card-body bg-page">
                                <ul>
                                    <li>The fees once paid will not be refunded under any circumstances for applications which are successfully submitted or payments successfully completed.</li>

                                    <li>The loss on this account shall not be borne either by TRBT or by the Banks / Payment Gateways.</li>

                                    <li>However automatic refund for any unsuccessful transaction is entirely depends on the case to case basis and TRBT or TRBT Online Application portal or the Government or the Banks / Payment Gateways could never be held responsible for any kind of failure regarding this.</li>
                                </ul>
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
    <script src="${pageContext.request.contextPath}/JS/common.js"></script>

</html>
