<%-- 
    Document   : index
    Created on : 18 Dec, 2019, 5:49:52 PM
    Author     : acer
--%>

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



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Agriculture, Tripura</title>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/style.css">
        <!-- Custom CSS -->
        
        <link href="${pageContext.request.contextPath}/CSS/bootstrap-theme.min.css" rel="stylesheet">


        
        <script type="text/javascript" src="${pageContext.request.contextPath}/JS/common.js"></script>
    </head>
    <body>
        <div class="container-fluid bg-light">
            <jsp:include page="Web/header.jsp"></jsp:include>            
                <div class="row mt-2 mb-2">
                    <div class="col-sm-12">

                        <div class="row">

                            <div class="col-12">
                                <div class="form-group">                                    
                                    <a href="DBTInitiator/beneficiarySchemeMapping.jsp" target="_blank">
                                        <button type="button" id="btnBeneficiarySelectionForScheme" name="btnBeneficiarySelectionForScheme" class="btn btn-primary btn-block">
                                            Beneficiary Selection for Scheme
                                        </button></a>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-group">                                    
                                    <a href="DBTInitiator/beneficiarySelectionForDBTCash.jsp" target="_blank">
                                        <button type="button" id="btnBeneficiarySelectionForDBTCash" name="btnBeneficiarySelectionForDBTCash" class="btn btn-primary btn-block">
                                            Beneficiary Selection for DBT Cash
                                        </button></a>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-group">                                    
                                    <a href="DBTVerifier/beneficiaryVerificationforDBTCash.jsp" target="_blank">
                                        <button type="button" id="btnBeneficiaryVerificationforDBTCash" name="btnBeneficiaryVerificationforDBTCash" class="btn btn-primary btn-block">
                                            Verify Beneficiaries for DBT
                                        </button></a>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="form-group">                                    
                                    <a href="DBTApprover/beneficiaryApprovalforDBTCash.jsp" target="_blank">
                                        <button type="button" id="btnBeneficiaryApprovalForDBT" name="btnBeneficiaryApprovalForDBT" class="btn btn-primary btn-block">
                                            Approve Beneficiaries for DBT
                                        </button></a>
                                </div>
                            </div>                            
                            <div class="col-12">
                                <div class="form-group">                                    
                                    <a href="Reports/dbtStatusReport.jsp" target="_blank">
                                        <button type="button" id="btnDBTStatusReport" name="btnDBTStatusReport" class="btn btn-primary btn-block">
                                            DBT Status Report
                                        </button></a>
                                </div>
                            </div>
                        </div>


                        <!-- /.row -->

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