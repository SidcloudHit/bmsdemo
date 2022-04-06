<%-- 
    Document   : testexcelupload
    Created on : Dec 23, 2020, 7:20:33 PM
    Author     : NIC-Arindam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
      <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>upload </title>
        <script type="text/javascript">

            function ValidateSize(file) {
                var FileSize = file.files[0].size / 1024 / 1024; // in MB
                if (FileSize > 4) {
                    alert('File size exceeds 4 MB');
                    // $(file).val(''); //for clearing with Jquery
                } else {

                }
            }
        </script>



    </head>
    <body>
        <h1>Browse and upload  Excel</h1>
        <form name="form2" id="form2"  method="post" action="uploadfileservlet" method="post"  enctype="multipart/form-data">

            <input type="file" class="form-control" accept=".xlsx" name="gfile" id="gfile" onchange="ValidateSize(this)" required="required"/>
            <input type="submit"  name="btnconfirm" id="btnconfirm" value="Submit"/>   
        </form>
    </body>
</html>
