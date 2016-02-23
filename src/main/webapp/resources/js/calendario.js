/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global PF */

//regexp tests -> numeric digit, backspace, tab, directionals keystrokes and delete key (by andre luiz f r barca)
//<input type="text" name="any_name_you_want" id="any_id_you_want" onkeypress="return number(event);" />
function number(event) {
    var nkey = event.keyCode || event.which;
    allowedKeys = /\d|[\b\t\x23\x24\x25\x26\x27\x28\x2e]/;
    return allowedKeys.test(String.fromCharCode(nkey));
}


function dialogHandler(widgetVar, xhr, status, args) {
    if (args.resultado === true) {
        PF(widgetVar).hide();
    } else {
        PF(widgetVar).jq.effect("highlight", {}, 800);
    }
}

function toggleMenu() {
    //freezes scrolling
    jQuery("#eventoPanel").addClass("eventoPanelFixed");
    if (jQuery(".filters").css("visibility") !== "hidden") {
        jQuery("#eventoPanel").removeClass("eventoPanelFixed");
    }
    jQuery(".filters").toggleClass("toggleMenu", "transitionDefault");
}

function toggleEventDetail(objectId) {
    jQuery("#" + objectId).toggleClass("toggle");
}