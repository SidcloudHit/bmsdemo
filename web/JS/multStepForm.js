/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var currentTab = 0; // Current tab is set to be the first tab (0)
showTab(currentTab); // Display the current tab

function showTab(n) {
    // This function will display the specified tab of the form ...
    var x = document.getElementsByClassName("tab");
    x[n].style.display = "block";
    // ... and fix the Previous/Next buttons:
    if (n === 0) {
        document.getElementById("prevBtn").style.display = "none";
    } else {
        document.getElementById("prevBtn").style.display = "inline";
    }
    if (n === (x.length - 1)) {
        document.getElementById("nextBtn").innerHTML = "Submit";
    } else {
        document.getElementById("nextBtn").innerHTML = "Next";
    }
    // ... and run a function that displays the correct step indicator:
    fixStepIndicator(n);

    if (typeof (document.getElementById("frmApplication")) !== 'undefined' && document.getElementById("frmApplication") !== null) {

        if (x[n].id === "resDet") {

            if (document.querySelector('input[name="chkBxResDeclaration"]').checked) {
                document.getElementById("nextBtn").removeAttribute("disabled");
            } else {
                document.getElementById('nextBtn').setAttribute("disabled", "disabled");
            }
        } else if (x[n].id === "prevApplDet") {
            if (document.querySelectorAll('input[name="chkbxSeatDeclaration"][value="1"]:checked,input[name="chkbxSeatDeclaration"][value="2"]:checked')) {
                document.getElementById("nextBtn").removeAttribute("disabled");
            } else {
                document.getElementById('nextBtn').setAttribute("disabled", "disabled");
            }
        } else if (x[n].id === "othDet") {

            if (document.querySelector('input[name="chkBxApplDeclaration"]').checked) {
                document.getElementById("nextBtn").removeAttribute("disabled");
            } else {
                document.getElementById('nextBtn').setAttribute("disabled", "disabled");
            }
        }

    }
}

function nextPrev(n) {
    // This function will figure out which tab to display
    var x = document.getElementsByClassName("tab");
    // Exit the function if any field in the current tab is invalid:
    if (n === 1 && !validateForm()) //
        return false;
    // Hide the current tab:
    x[currentTab].style.display = "none";
    // Increase or decrease the current tab by 1:
    currentTab = currentTab + n;
    // if you have reached the end of the form... :
    
    if (currentTab >= x.length) {
        //...the form gets submitted:
        if (typeof (document.getElementById("frmProfileEdit")) !== 'undefined' && document.getElementById("frmProfileEdit") !== null) {            
            document.getElementById("frmProfileEdit").submit();
        } else if (typeof (document.getElementById("frmApplication")) !== 'undefined' && document.getElementById("frmApplication") !== null) {
            document.getElementById("frmApplication").submit();
        }

        return false;
    }
    // Otherwise, display the correct tab:
    showTab(currentTab);
}

function validateForm() {
    // This function deals with validation of the form fields
    var x, y, z, t, i, j, valid = true;

    x = document.getElementsByClassName("tab");
    y = x[currentTab].getElementsByTagName("input");

    //specialValidations();
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

    z = x[currentTab].getElementsByTagName("select");
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

    t = x[currentTab].getElementsByTagName("textarea");
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
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    return valid; // return the valid status
}

function fixStepIndicator(n) {
    // This function removes the "active" class of all steps...
    var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        x[i].className = x[i].className.replace(" active", "");
    }
    //... and adds the "active" class to the current step:
    x[n].className += " active";
}

function specialValidations() {

    if (typeof (document.getElementById("frmProfileEdit")) !== 'undefined' && document.getElementById("frmProfileEdit") !== null) {
//        var father = document.getElementById("txtFathersName");
//        var mother = document.getElementById("txtMothersName");
//        var guardian = document.getElementById("txtGuardiansName");
//        var relation = document.getElementById("txtGuardiansRelation");
//
//        if (father.value === "" && mother.value === "") {
//            guardian.setAttribute("required", "required");
//            relation.setAttribute("required", "required");
//        } else {
//            guardian.value = "";
//            relation.value = "";
//            guardian.removeAttribute("required");
//            relation.removeAttribute("required");
//            var cls1 = guardian.className;
//            guardian.className = cls1.replace("invalid", "");
//            var cls2 = relation.className;
//            relation.className = cls2.replace("invalid", "");
//        }
    } else if (typeof (document.getElementById("frmApplication")) !== 'undefined' && document.getElementById("frmApplication") !== null) {

    }



}
