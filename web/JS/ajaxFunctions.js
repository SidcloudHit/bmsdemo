/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function dependentDropDown(sourceDD, targetDD, category) {
    var i;

//    var sources = sourceDD.split("~");
//    if (sources.length === 1) {
//        url += sources.length + "&keyword=" + sources[0].value;
//    } else if (sources.length > 1) {
//        url += sources.length;
//        for (j = 0; j < sources.length; j++) {
//            url += "&keyword_" + (j + 1) + "=" + sources[j].value;
//        }
//    }

    var sourceOBJ = document.getElementById(sourceDD);
    var targetOBJ = document.getElementById(targetDD);
    var url = "../AjaxServlet?category=" + category + "&value=" + sourceOBJ.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        var browserName = navigator.appName;
        var sSelect = targetOBJ;
        //console.log(targetOBJ);
        sSelect.innerHTML = "";
        var oOption1 = document.createElement("option");
        var browser_type = "m";
        oOption1.text = "-Please Select-";
        oOption1.value = "-1";

        if (browserName === "Microsoft Internet Explorer")
        {
            browser_type = "e";
            sSelect.add(oOption1);
            
        } else
        {
            
            sSelect.add(oOption1, null); 
        }
        
        if (xmlhttp.responseText === "No Data") {
            var oOption = document.createElement("option");
            oOption.text = targetOBJ.options[targetOBJ.selectedIndex].text;
            oOption.value = targetOBJ.value;
            if (browser_type === "e")
            {
                sSelect.add(oOption);
            } else
            {
                sSelect.add(oOption, null);
            }
        } else { /*wlistArray and wlistIDArray are declared inside the AjaxServlet*/
            
            for (i = 0; i < wlistArray.length; i++)
            {
                var oOption = document.createElement("option");
                oOption.text = wlistArray[i];
                oOption.value = wlistIDArray[i];

                if (browser_type === "e")
                {
                    sSelect.add(oOption);
                } else
                {
                    sSelect.add(oOption, null);
                }
            }
        }
    }
}


function dependentDropDownDebug(sourceDD, targetDD, category) {
    var i;

//    var sources = sourceDD.split("~");
//    if (sources.length === 1) {
//        url += sources.length + "&keyword=" + sources[0].value;
//    } else if (sources.length > 1) {
//        url += sources.length;
//        for (j = 0; j < sources.length; j++) {
//            url += "&keyword_" + (j + 1) + "=" + sources[j].value;
//        }
//    }

    var sourceOBJ = document.getElementById(sourceDD);
    var targetOBJ = document.getElementById(targetDD);
    console.log(targetOBJ);
            
    var url = "../AjaxServlet?category=" + category + "&value=" + sourceOBJ.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        var browserName = navigator.appName;
        var sSelect = targetOBJ;
        console.log(sSelect);
        sSelect.innerHTML = "";
        var oOption1 = document.createElement("option");
        var browser_type = "m";
        oOption1.text = "-Please Select-";
        oOption1.value = "-1";

        if (browserName === "Microsoft Internet Explorer")
        {
            browser_type = "e";
            sSelect.add(oOption1);
            console.log(browserName);
        } else
        {
            console.log(oOption1.text);
            console.log(oOption1.value);
            console.log(browserName);
            sSelect.add(oOption1, null); 
        }
        
        if (xmlhttp.responseText === "No Data") {
            var oOption = document.createElement("option");
            oOption.text = targetOBJ.options[targetOBJ.selectedIndex].text;
            oOption.value = targetOBJ.value;
            if (browser_type === "e")
            {
                sSelect.add(oOption);
            } else
            {
                sSelect.add(oOption, null);
            }
        } else { /*wlistArray and wlistIDArray are declared inside the AjaxServlet*/
            console.log(wlistArray.length);
            for (i = 0; i < wlistArray.length; i++)
            {
                var oOption = document.createElement("option");
                oOption.text = wlistArray[i];
                oOption.value = wlistIDArray[i];

                if (browser_type === "e")
                {
                    sSelect.add(oOption);
                } else
                {
                    sSelect.add(oOption, null);
                }
            }
        }
    }
}



function dependentDropDown1(sourceDD, sourceDD1, targetDD, category) {
    var i;
//    var sources = sourceDD.split("~");
//    if (sources.length === 1) {
//        url += sources.length + "&keyword=" + sources[0].value;
//    } else if (sources.length > 1) {
//        url += sources.length;
//        for (j = 0; j < sources.length; j++) {
//            url += "&keyword_" + (j + 1) + "=" + sources[j].value;
//        }
//    }

    var sourceOBJ = document.getElementById(sourceDD);
    var sourceOBJ1 = document.getElementById(sourceDD1);
    var targetOBJ = document.getElementById(targetDD);
    var url = "../AjaxServlet?category=" + category + "&value=" + sourceOBJ.value + "&value1=" + sourceOBJ1.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        //alert(xmlhttp.responseText);
        eval(xmlhttp.responseText);
        var browserName = navigator.appName;
        var sSelect = targetOBJ;
        sSelect.innerHTML = "";
        var oOption1 = document.createElement("option");
        var browser_type = "m";
        oOption1.text = "-Please Select-";
        oOption1.value = "-1";

        if (browserName === "Microsoft Internet Explorer")
        {
            browser_type = "e";
            sSelect.add(oOption1);
        } else
        {
            sSelect.add(oOption1, null);
        }
        if (xmlhttp.responseText === "No Data") {
          alert(xmlhttp.responseText);
            var oOption = document.createElement("option");
            oOption.text = targetOBJ.options[targetOBJ.selectedIndex].text;
            oOption.value = targetOBJ.value;
            if (browser_type === "e")
            {
                sSelect.add(oOption);
            } else
            {
                sSelect.add(oOption, null);
            }
        } else { /*wlistArray and wlistIDArray are declared inside the AjaxServlet*/
            for (i = 0; i < wlistArray.length; i++)
            {
                
                var oOption = document.createElement("option");
                oOption.text = wlistArray[i];
                oOption.value = wlistIDArray[i];

                if (browser_type === "e")
                {
                    sSelect.add(oOption);
                } else
                {
                    sSelect.add(oOption, null);
                }
            }
        }
    }
}

function dependentDropDown2(sourceDD, sourceDD1, sourceDD2, targetDD, category) {
    var i;

//    var sources = sourceDD.split("~");
//    if (sources.length === 1) {
//        url += sources.length + "&keyword=" + sources[0].value;
//    } else if (sources.length > 1) {
//        url += sources.length;
//        for (j = 0; j < sources.length; j++) {
//            url += "&keyword_" + (j + 1) + "=" + sources[j].value;
//        }
//    }

    var sourceOBJ = document.getElementById(sourceDD);
    var sourceOBJ1 = document.getElementById(sourceDD1);
    var sourceOBJ2 = document.getElementById(sourceDD2);
    var targetOBJ = document.getElementById(targetDD);
    var url = "../AjaxServlet?category=" + category + "&value=" + sourceOBJ.value + "&value1=" + sourceOBJ1.value + "&value2=" + sourceOBJ2.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        var browserName = navigator.appName;
        var sSelect = targetOBJ;
        sSelect.innerHTML = "";
        var oOption1 = document.createElement("option");
        var browser_type = "m";
        oOption1.text = "-Please Select-";
        oOption1.value = "-1";

        if (browserName === "Microsoft Internet Explorer")
        {
            browser_type = "e";
            sSelect.add(oOption1);
        } else
        {
            sSelect.add(oOption1, null);
        }
        if (xmlhttp.responseText === "No Data") {
            var oOption = document.createElement("option");
            oOption.text = targetOBJ.options[targetOBJ.selectedIndex].text;
            oOption.value = targetOBJ.value;
            if (browser_type === "e")
            {
                sSelect.add(oOption);
            } else
            {
                sSelect.add(oOption, null);
            }
        } else { /*wlistArray and wlistIDArray are declared inside the AjaxServlet*/
            for (i = 0; i < wlistArray.length; i++)
            {
                var oOption = document.createElement("option");
                oOption.text = wlistArray[i];
                oOption.value = wlistIDArray[i];

                if (browser_type === "e")
                {
                    sSelect.add(oOption);
                } else
                {
                    sSelect.add(oOption, null);
                }
            }
        }
    }
}

function dependentDropDownBatch(sourceDD, sourceDD1, sourceDD2, targetDD, category) {
    var i;

    var sourceOBJ = document.getElementById(sourceDD);
    var sourceOBJ1 = document.getElementById(sourceDD1);
    var sourceOBJ2 = document.getElementById(sourceDD2);
    var targetOBJ = document.getElementById(targetDD);
    var url = "../AjaxServlet?schmb=" + category + "&schemeId=" + sourceOBJ.value + "&finYr=" + sourceOBJ1.value + "&instl=" + sourceOBJ2.value;
    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        var browserName = navigator.appName;
        var sSelect = targetOBJ;
        sSelect.innerHTML = "";
        var oOption1 = document.createElement("option");
        var browser_type = "m";
        oOption1.text = "-Please Select-";
        oOption1.value = "-1";

        if (browserName === "Microsoft Internet Explorer")
        {
            browser_type = "e";
            sSelect.add(oOption1);
        } else
        {
            sSelect.add(oOption1, null);
        }
        if (xmlhttp.responseText === "No Data") {
            var oOption = document.createElement("option");
            oOption.text = targetOBJ.options[targetOBJ.selectedIndex].text;
            oOption.value = targetOBJ.value;
            if (browser_type === "e")
            {
                sSelect.add(oOption);
            } else
            {
                sSelect.add(oOption, null);
            }
        } else { /*wlistArray and wlistIDArray are declared inside the AjaxServlet*/
            for (i = 0; i < wlistArray.length; i++)
            {
                var oOption = document.createElement("option");
                oOption.text = wlistArray[i];
                oOption.value = wlistIDArray[i];

                if (browser_type === "e")
                {
                    sSelect.add(oOption);
                } else
                {
                    sSelect.add(oOption, null);
                }
            }
        }
    }
}


function addLandtoBeneficiary(sl_no, plot_no, plot_area, plot_area_unit, plot_land_class, plot_land_subclass, owners_info_from_lr) {
    $("#landMappedDiv").removeClass("d-none");
    var beneficiary_id = $('#hdnBenId').val();
    var state_code = 16;
    var dist_code = $('#ddlDistrict2  option:selected').val();
    var dist_name = $('#ddlDistrict2  option:selected').text();
    var subdiv_code = $('#ddlSubDiv2  option:selected').val();
    var subdiv_name = $('#ddlSubDiv2  option:selected').text();
    var rev_circle_code = $('#ddlRevCircle  option:selected').val();
    var rev_circle_name = $('#ddlRevCircle  option:selected').text();
    var tehsil_code = $('#ddlTehsil  option:selected').val();
    var tehsil_name = $('#ddlTehsil  option:selected').text();
    var rev_vill_code = $('#ddlRevVill  option:selected').val();
    var rev_vill_name = $('#ddlRevVill  option:selected').text();
    var khatian_no = $('#txtKhatianNo').val();
    var ownership_type = $('input[name="radOwnershipType_' + sl_no + '"]:checked').val();
    //var ownership_type = $("input[name='radOwnershipType_" + sl_no + "']:checked").val();
    alert(ownership_type);
    var ownership_share = $('#txtOwnersPlotShare_' + sl_no).val();

    var url = "../LRDataCheck?beneficiary_id=" + beneficiary_id + "&state_code=" + state_code + "&dist_code=" + dist_code + "&subdiv_code=" + subdiv_code + "&rev_circle_code=" + rev_circle_code + "&tehsil_code=" + tehsil_code;
    url += "&rev_vill_code=" + rev_vill_code + "&dist_name=" + dist_name + "&subdiv_name=" + subdiv_name + "&rev_circle_name=" + rev_circle_name + "&tehsil_name=" + tehsil_name;
    url += "&rev_vill_name=" + rev_vill_name + "&khatian_no=" + khatian_no + "&plot_no=" + plot_no + "&plot_area=" + plot_area + "&plot_area_unit=" + plot_area_unit + "&plot_land_class=" + plot_land_class;
    url += "&plot_land_subclass=" + plot_land_subclass + "&ownership_type=" + ownership_type + "&ownership_share=" + ownership_share + "&owners_info_from_lr=" + owners_info_from_lr + "&mapPlot=1";

    if (xmlhttp) {
        xmlhttp.open("POST", url, false);
        xmlhttp.send("");
        eval(xmlhttp.responseText);
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            /* if (msg === "no") {
             return false;
             } else {
             return true;
             }*/
            alert(xmlhttp.responseText);
        }
    }
}



