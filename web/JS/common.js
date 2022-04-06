/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//<![CDATA[
function getXMLObject() {  //XML OBJECT
    var xmlHttp = false;
    try {
        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP"); // For Old Microsoft Browsers
    } catch (e) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP"); // For Microsoft IE 6.0+
        } catch (e2) {
            xmlHttp = false; // No Browser accepts the XMLHTTP Object then false
        }
    }
    if (!xmlHttp && typeof XMLHttpRequest !== 'undefined') {
        xmlHttp = new XMLHttpRequest(); //For Mozilla, Opera Browsers
    }
    return xmlHttp; // Mandatory Statement returning the ajax object created
}

var xmlhttp = new getXMLObject();


function emailCheck() {
    var email = document.getElementById("txtEmail");
    var url = "AjaxServlet?email=" + email.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            if (msg === "no") {
                var r = confirm("This Email ID is already registered with us.Please try with another Email ID");
                if (r === true) {
                    email.value = '';
                    email.style.border = "1px solid #ff0000";
                    email.focus();
                } else {
                    email.value = '';
                    email.style.border = "1px solid #ff0000";
                    email.focus();
                }
                return false;
            } else {
                return true;
            }
        }
    }
}


function mobileCheck() {
    var mobile = document.getElementById("txtMobile");
    var url = "AjaxServlet?mobile=" + mobile.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            if (msg === "no") {
                var r = confirm("This Mobile Number is already registered with us.Please try with another one");
                if (r === true) {
                    mobile.value = '';
                    mobile.style.border = "1px solid #ff0000";
                    mobile.focus();
                } else {
                    mobile.value = '';
                    mobile.style.border = "1px solid #ff0000";
                    mobile.focus();
                }
                return false;
            } else {
                return true;
            }
        }
    }
}

function setSelected(obj, val) {
    var ddlobj = document.getElementById(obj);
    var i;
    for (i = 0; i < ddlobj.length; i++)
    {
        if (ddlobj.options[i].value === val)
            ddlobj.options[i].selected = true;
    }
}


function converthash() {
    // alert('ok');
    var key = '';
    var hashkey = '';
    var msg = '';
    var hash = '';
    //alert('1');
    key = document.getElementById("txtUserPwd").value;
    hashkey = document.getElementById("txthashkey").value;
    var shaObj = new jsSHA("SHA-256", "TEXT");
    shaObj.update(key);
    key = shaObj.getHash("HEX");
    //alert(key);
    msg = key.trim().concat(hashkey.trim());
    var shaObj1 = new jsSHA("SHA-256", "TEXT");
    shaObj1.update(msg.trim());
    hash = shaObj1.getHash("HEX");
    document.getElementById("txtUserPwd").value = hash;
    return true;
}


function readURL(input, id) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#preview_' + id).attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function isNumeric(value) {
    return /^\d+$/.test(value);
}


function setSelectedName(elem) {
    //console.log(elem);
    var sel = document.getElementById(elem);
    //console.log(sel.value);
    //console.log(sel.selectedIndex);
    //console.log(sel.options[sel.selectedIndex].text);
    //var text = sel.options[sel.selectedIndex].text;
    document.getElementById(elem + "Name").value = sel.options[sel.selectedIndex].text;

}

function refreshAgriBasicReportEntryPage() {
    var d = document.getElementById("ddlDistrict1").value;
    var sd = document.getElementById("ddlSubDiv1").value;
    //var l = document.getElementById("ddlLocType").value;
    //var b = document.getElementById("ddlUrbRurBody").value;

    var gpId = document.getElementById("ddlWardGp").value;
    var fYear = document.getElementById("ddlFYear").value;

    if (gpId !== "0" && gpId !== "-1" && fYear !== "0") {
        window.location.replace("agriBasicReportEntry.jsp?d=" + d + "&sd=" + sd + "&gpId=" + gpId + "&fYear=" + fYear);
    }
}

function setAgriBasicReport() {
    var d = document.getElementById("ddlDistrict1").value;
    var sd = document.getElementById("ddlSubDiv1").value;
    //var l = document.getElementById("ddlLocType").value;
    //var b = document.getElementById("ddlUrbRurBody").value;
    var gpId = document.getElementById("ddlWardGp").value;
    var fYear = document.getElementById("ddlFYear").value;

    if (fYear === "0") {
        alert("Please Select Financial Year...");
        document.getElementById("ddlFYear").style.border = "1px solid #ff0000";
        document.getElementById("ddlFYear").focus();
        return false;
    }

    if (gpId !== "0" && gpId !== "-1" && fYear !== "0") {
        window.location.replace("agriBasicReportView.jsp?d=" + d + "&sd=" + sd + "&gpId=" + gpId + "&fYear=" + fYear);
    } else if (sd !== "0" && sd !== "-1") {
        window.location.replace("agriBasicReportView.jsp?d=" + d + "&sd=" + sd + "&fYear=" + fYear);
    } else if (d !== "0" && d !== "") {
        window.location.replace("agriBasicReportView.jsp?d=" + d + "&fYear=" + fYear);
    } else {
        window.location.replace("agriBasicReportView.jsp?fYear=" + fYear);
    }


    /**
     else if (b !== "0" && b !== "-1") {
     window.location.replace("agriBasicReportView.jsp?d=" + d + "&sd=" + sd + "&b=" + b + "&fYear=" + fYear);
     }
     **/
}

function popup(data) {
    var mywindow = window.open('', 'my div', 'height=400,width=1000');
    mywindow.document.write('<html><head><title></title>');
    mywindow.document.write('<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">');
    mywindow.document.write('<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">');
    mywindow.document.write('<link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:100,200,300,400,500,600,700,800,900" rel="stylesheet">');
    mywindow.document.write('<link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800,800i" rel="stylesheet">');
    mywindow.document.write('<link rel="stylesheet" href="../CSS/style.css">');
    mywindow.document.write('<link rel="stylesheet" href="../CSS/icheck-material.css">');
    mywindow.document.write('</head><body>'); //<style type="text/css">.test { color:red; } </style>
    mywindow.document.write(data);
    mywindow.document.write('</body></html>');
    mywindow.document.close();
    mywindow.print();
}


function showHideElement(obj1, obj2, obj3, val) {
    var selObj = document.getElementById(obj1);
    var showObj = obj2.split('~');
    var hideObj = obj3.split('~');
//    var valList;
    var type = selObj.tagName.toLowerCase();
//    if (val !== "") {
//        valList = val.split('~');
//    }
    var i = 0, j = 0, k = 0;

    if (type === "checkbox" || type === "radio") {
        if (selObj.checked) {
            for (i = 0; i < showObj.length; i++) {
                var cls = document.getElementById(showObj[i]).className;
                document.getElementById(showObj[i]).className = cls.replace("d-none", "d-block");
            }
            for (j = 0; j < hideObj.length; j++) {
                var cls = document.getElementById(hideObj[i]).className;
                document.getElementById(hideObj[i]).className = cls.replace("d-block", "d-none");
            }
        }
    } else if (type === "select") {
//        for (k = 0; k < valList.length; i++) {
//            if (selObj.value === valList[k]) {
        if (selObj.value === val) {
            for (i = 0; i < showObj.length; i++) {
                var cls = document.getElementById(showObj[i]).className;
                document.getElementById(showObj[i]).className = cls.replace("d-none", "d-block");
            }
            for (j = 0; j < hideObj.length; j++) {
                var cls = document.getElementById(hideObj[i]).className;
                document.getElementById(hideObj[i]).className = cls.replace("d-block", "d-none");
            }
        }
//            }
//        }
    }

}


function showHideElement(obj1, obj2, obj3) {
    var selObj = document.getElementById(obj1);
    var showValObj = obj2.split('~');
    var hideValObj = obj3.split('~');
    var i = 0, j = 0, k = 0;

    for (i = 0; i < showValObj.length; i++) {
        var val = showValObj[i].split('#')[0];
        var obj = showValObj[i].split('#')[1];
        if (selObj.value === val) {
            var cls = document.getElementById(obj).className;
            document.getElementById(obj).className = cls.replace("d-none", "d-block");
        }
    }
    for (j = 0; j < hideValObj.length; j++) {
        var val = hideValObj[i].split('#')[0];
        var obj = hideValObj[i].split('#')[1];
        if (selObj.value === val) {
            var cls = document.getElementById(obj).className;
            document.getElementById(obj).className = cls.replace("d-block", "d-none");
        }
    }

}


function checkHeader(obj1,obj2){
    var matches = document.getElementsByClassName(obj1);

    for (var i=0; i<matches.length; i++) {
      if(matches[i].val === 1){
          document.getElementsByClassName(obj2).removeAttribute("required");    
                
            }
     else if(matches[i].val === 0){
         document.getElementsByClassName(obj2).setAttribute("required", "");
     }
    }
}

function showHideBlock() {
    var locType = document.getElementById('ddlLocType');
    var urbanDiv = document.getElementById('urbanDiv');
    var ruralDiv = document.getElementById('ruralDiv');
    var ruralAdcDiv = document.getElementById('ruralAdcDiv');

    if (locType.value === '1' || locType.value === '2' || locType.value === '3') {
        var cls1 = urbanDiv.className;
        urbanDiv.className = cls1.replace("d-none", "");
        if (!ruralDiv.className.includes("d-none")) {
            ruralDiv.className += " d-none";
        }
        if (!ruralAdcDiv.className.includes("d-none")) {
            ruralAdcDiv.className += " d-none";
        }

    } else if (locType.value === '4') {
        var cls1 = ruralDiv.className;
        ruralDiv.className = cls1.replace("d-none", "");
        if (!urbanDiv.className.includes("d-none")) {
            urbanDiv.className += " d-none";
        }

        if (!ruralAdcDiv.className.includes("d-none")) {
            ruralAdcDiv.className += " d-none";
        }

    } else if (locType.value === '5') {
        var cls1 = ruralAdcDiv.className;
        ruralAdcDiv.className = cls1.replace("d-none", "");
        if (!ruralDiv.className.includes("d-none")) {
            ruralDiv.className += " d-none";
        }

        if (!urbanDiv.className.includes("d-none")) {
            urbanDiv.className += " d-none";
        }

    } else if (locType.value === '') {
        if (!ruralAdcDiv.className.includes("d-none")) {
            ruralAdcDiv.className += " d-none";
        }

        if (!ruralDiv.className.includes("d-none")) {
            ruralDiv.className += " d-none";
        }

        if (!urbanDiv.className.includes("d-none")) {
            urbanDiv.className += " d-none";
        }

    }
}

function resetBlock(seq) {
    if (seq === 1) {
        //setSelected("ddlLocType", "");
        //showHideBlock();  
        $('#ddlSubDiv1').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlUrbRurBody').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlWardGp').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 2) {
        //setSelected("ddlLocType", "");
        //showHideBlock();
        $('#ddlUrbRurBody').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlWardGp').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 3) {
        $('#ddlWardGp').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 4) {
        //setSelected("ddlLocType", "");
        //showHideBlock();  
        $('#ddlSubDiv').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlUrbRurBody').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlVlwStore').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 5) {
        //setSelected("ddlLocType", "");
        //showHideBlock();
        $('#ddlUrbRurBody').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlVlwStore').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 6) {
        $('#ddlVlwStore').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 7) {
        //setSelected("ddlLocType", "");
        //showHideBlock();  
        $('#ddlSubDiv').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlUrbRurBody').empty().append('<option value="0">-Please Select-</option>');
        //$('#ddlVlwStore').empty().append('<option value="0">-Please Select-</option>');
    } else if (seq === 8) {
        //setSelected("ddlLocType", "");
        //showHideBlock();  
        $('#ddlSubDiv1').empty().append('<option value="0">-Please Select-</option>');
        $('#ddlWardGp').empty().append('<option value="0">-Please Select-</option>');
        //$('#ddlVlwStore').empty().append('<option value="0">-Please Select-</option>');
    }


}

function setPin() {
    var selPo = document.getElementById("ddlPo");

    if (selPo.value === "" || selPo.value === "-1" || selPo.value === "0") {
        document.getElementById("txtAddrPIN").value = "";
    } else {
        var pin = selPo.value.split('#')[1];
        document.getElementById("txtAddrPIN").value = pin;
    }
}

function filterFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("myDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}


function isSchemeAmountFixed() {
    var avl = document.getElementById("scheme_inst_type").value;

    var instlAmntDiv = document.getElementById("instlAmntDiv");
    var installment = document.getElementById("installment");

    if (avl === '1') {
        var cls = instlAmntDiv.className;
        instlAmntDiv.className = cls.replace("d-none", "d-block");
        installment.setAttribute("required", "required");
    } else if (avl === '0') {
        installment.removeAttribute("required");
        var cls = instlAmntDiv.className;
        instlAmntDiv.className = cls.replace("d-block", "d-none");

    }
}


function isPmkisanIdAvl() {
    var avl = document.querySelector('input[name="radPmkisanAvl"]:checked').value;

    var pmkisanDiv = document.getElementById("pmkisaIdDiv");
    var pmkisanField = document.getElementById("txtPMKISANId");

    if (avl === '1') {
        var cls = pmkisanDiv.className;
        pmkisanDiv.className = cls.replace("d-none", "d-block");
        pmkisanField.setAttribute("required", "required");
    } else if (avl === '0') {
        pmkisanField.removeAttribute("required");
        var cls = pmkisanDiv.className;
        pmkisanDiv.className = cls.replace("d-block", "d-none");

    }
}

function isOwner(num) {
    var ownership = document.querySelector('input[name="radOwnershipType_' + num + '"]:checked').value;
    var share = document.getElementById("txtOwnersPlotShare_" + num);

    if (ownership === '1') {
        share.value = "100";
        share.removeAttribute("readonly");
    } else if (ownership === '2') {
        share.value = "";
        share.removeAttribute("readonly");
    } else if (ownership === '3') {
        share.value = "0";
        share.setAttribute("readonly", "readonly");
    }
}



function editLandMapping(landId) {

}

function loadKhatiyanSearch() {

}

function getCropDetails() {
    var gp = document.getElementById('ddlWardGp').value;
    var year = document.getElementById('ddlFYear').value;
    if (xmlhttp) {
        xmlhttp.open("POST", "../AjaxServlet?cropdata=0&gp=" + gp + "&year=" + year + " ", false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        $(document).ready(function () {
            if (xmlhttp.responseText !== "No Data") {
                for (var i = 1; i < cropTypeArray.length; i++)
                {

                    if (i === 1) {
                        $('#ddlCropType' + i).val(cropTypeArray[i]);
                        dependentDropDown('ddlCropType' + i, 'ddlCropName' + i, 'fillCropName');
                        $('#ddlCropName' + i).val(cropNameArray[i]);
                        //$('#ddlIrrigationCat' + i).val(irNameArray[i]);
                        $('#ddlCropSeason' + i).val(cropSeasonArray[i]);
                        $('#txtCropArea' + i).val(cropAreaArray[i]);
                    } else {
                        console.log(i + ',' + cropTypeArray[i]);
                        $('#btnAddMore').trigger('click');
                        $('#ddlCropType' + i).val(cropTypeArray[i]);
                        dependentDropDown('ddlCropType' + i, 'ddlCropName' + i, 'fillCropName');
                        $('#ddlCropName' + i).val(cropNameArray[i]);
                        //$('#ddlIrrigationCat' + i).val(irNameArray[i]);
                        $('#ddlCropSeason' + i).val(cropSeasonArray[i]);
                        $('#txtCropArea' + i).val(cropAreaArray[i]);
                    }
                }

            } else {
            }
        });
    }
}

function getIrrigationDetails() {
    var gp = document.getElementById('ddlWardGp').value;
    var year = document.getElementById('ddlFYear').value;
    if (xmlhttp) {
        xmlhttp.open("POST", "../AjaxServlet?irrdata=0&gp=" + gp + "&year=" + year + " ", false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        $(document).ready(function () {
            if (xmlhttp.responseText !== "No Data") {
                for (var i = 1; i < irTypeArray.length; i++)
                {
                    if (i === 1) {
                        $('#ddlIrrigationType' + i).val(irTypeArray[i]);
                        $('#ddlIrrigationCat' + i).val(irNameArray[i]);
                        $('#txtIrrigationNo' + i).val(irNoArray[i]);
                        $('#txtcommandArea' + i).val(irAreaArray[i]);

                    } else {
                        $('#btnAddMoreIrr').trigger('click');
                        $('#ddlIrrigationType' + i).val(irTypeArray[i]);
                        $('#ddlIrrigationCat' + i).val(irNameArray[i]);
                        $('#txtIrrigationNo' + i).val(irNoArray[i]);
                        $('#txtcommandArea' + i).val(irAreaArray[i]);
                    }
                }
            } else {
            }
        });
    }
}


function getAddlIdDetails(benId) {

    if (xmlhttp) {
        xmlhttp.open("POST", "../AjaxServlet?addliddata=0&benId=" + benId, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        $(document).ready(function () {
            if (xmlhttp.responseText !== "No Data") {
                for (var i = 1; i < addlIdTypeArray.length; i++)
                {
                    if (i === 1) {

                        $('#ddlAddlIdType' + i).val(addlIdTypeArray[i]);
                        $('#txtAddlIdValue' + i).val(addlIdTypeValArray[i]);

                    } else {
                        $('#btnAddMoreId').trigger('click');

                        $('#ddlAddlIdType' + i).val(addlIdTypeArray[i]);
                        $('#txtAddlIdValue' + i).val(addlIdTypeValArray[i]);
                    }
                }
            } else {
            }
        });
    }
}


function checkReportee() {
    var reportee = document.getElementById('ddlUserReportee').value;
    var reporter = document.getElementById('ddlUserReporter').value;
    if (reportee === reporter) {
        alert("Reporter and Reportee should be different");
        $('#ddlUserReportee').prop('selectedIndex', 0);
        $('#ddlUserReporter').prop('selectedIndex', 0);
        return false;
    }
}

function changeDateFormat(dateString) {
    var fullDate = new Date(dateString);
    var twoDigitMonth = (fullDate.getMonth() > 8) ? (fullDate.getMonth() + 1) : '0' + (fullDate.getMonth() + 1);
    var currentDate = fullDate.getDate() + "/" + twoDigitMonth + "/" + fullDate.getFullYear();
    return currentDate;
}


  
