/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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


function loginIdCheck() {
    //var edit = document.getElementById("hdnEdit");
    
    var loginId = document.getElementById("txtUserLoginId");
    var url = "AjaxServlet?loginId=" + loginId.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            if (msg === "no") {
                var r = confirm("This Login ID already exists. Please try with another one");
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


function ValidateSize(file, sizeinkb, id) {
    var FileSize = file.files[0].size / 1024; // in KB
    if (FileSize > sizeinkb) {
        alert('File size exceeds ' + sizeinkb + ' KB');
    } else {
        readURL(file, id);
    }
}


function validateFormType1() {
    // This function deals with validation of the form fields
    var x, y, z, t, i, j, valid = true;

    x = document.getElementsByClassName("form");
    y = x[0].getElementsByTagName("input");

    valid &= specialValidations();

    // A loop that checks every input field in the current tab:
    for (i = 0; i < y.length; i++) {
        // If a field is empty...
        if (y[i].type === "text" || y[i].type === "file") {
            if (y[i].required) {
                if (y[i].value === "") {
                    // add an "invalid" class to the field:
                    if (!y[i].className.includes("invalid")) {
                        y[i].className += " invalid";
                    }
                    // and set the current valid status to false:
                    valid &= false;
                } else {
                    var cls = y[i].className;
                    y[i].className = cls.replace("invalid", "");
                }
            }
        } else if (y[i].type === "radio" || y[i].type === "checkbox") {
            if (y[i].required) {
                var elem = document.getElementsByName(y[i].name);
                var invalid = true;
                for (j = 0; j < elem.length; j++) {
                    if (elem[j].checked === true) {
                        invalid &= false;
                    }
                }
                if (invalid) {
                    for (j = 0; j < elem.length; j++) {
                        if (!elem[j].parentElement.className.includes("invalid")) {
                            elem[j].parentElement.className += " invalid";
                        }
                    }
                    // and set the current valid status to false:
                    valid &= false;
                } else {

                    for (j = 0; j < elem.length; j++) {
                        var cls = elem[j].parentElement.className;
                        elem[j].parentElement.className = cls.replace("invalid", "");
                    }
                }
            }
        }
    }

    z = x[0].getElementsByTagName("select");
    // A loop that checks every input field in the current tab:   
    for (i = 0; i < z.length; i++) {
        // If a field is empty...
        if (z[i].required) {
            if (z[i].value === "" || z[i].value === "0" || z[i].value === "-1") {
                // add an "invalid" class to the field:
                if (!z[i].className.includes("invalid")) {
                    z[i].className += " invalid";
                }
                // and set the current valid status to false:
                valid &= false;
            } else {
                var cls = z[i].className;
                z[i].className = cls.replace("invalid", "");
            }
        }
    }

    t = x[0].getElementsByTagName("textarea");
    // A loop that checks every input field in the current tab:   
    for (i = 0; i < t.length; i++) {
        // If a field is empty...
        if (t[i].required) {
            if (t[i].value === "") {
                // add an "invalid" class to the field:
                if (!t[i].className.includes("invalid")) {
                    t[i].className += " invalid";
                }
                // and set the current valid status to false:
                valid &= false;
            } else {
                var cls = t[i].className;
                t[i].className = cls.replace("invalid", "");
            }
        }
    }

    // If the valid status is true, mark the step as finished and valid:
//    if (valid) {
//        document.getElementsByClassName("step")[currentTab].className += " finish";
//    }
    return valid; // return the valid status

}


function specialValidations() {
    var type = document.getElementById("hdnFormType");

    if (parseInt(type.value) === 1) {
        for (var seq = 1; seq <= 3; seq++) {
            var chkBox = document.getElementById("chkBxQ" + seq + "Cgpa");
            if (chkBox.checked) {
                var percent = document.getElementById("txtQ" + seq + "Percent");
                var cutoff = document.getElementById("txtQ" + seq + "CutOff");

                if ((parseInt(percent.value) > 0) && (parseInt(percent.value) < parseInt(cutoff.value))) {
                    if (confirm("Since cut-off percentage is " + cutoff.value + " , We cannot accept your application.")) {
                        percent.value = "";
                        return false;
                    } else {
                        percent.value = "";
                        return false;
                    }
                } else {
                    return true;
                }
            }
        }
    }
}

function specialValidationsOnChange(seq) {
    var type = document.getElementById("hdnFormType");

    if (parseInt(type.value) === 1) {
        var chkBox = document.getElementById("chkBxQ" + seq + "Cgpa");
        if (chkBox.checked) {
            var percent = document.getElementById("txtQ" + seq + "Percent");
            var cutoff = document.getElementById("txtQ" + seq + "CutOff");

            if ((parseInt(percent.value) > 0) && (parseInt(percent.value) < parseInt(cutoff.value))) {
                if (confirm("Since cut-off percentage is " + cutoff.value + " , We cannot accept your application.")) {
                    percent.value = "";
                    return false;
                } else {
                    percent.value = "";
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
