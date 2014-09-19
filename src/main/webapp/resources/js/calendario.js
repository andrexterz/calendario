/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function toggleMenu() {
    jQuery("#content").toggleClass("toggle");
    jQuery("#menuLeft").toggleClass("toggle");    
}

function toggleEventDetail(objectId) {
    //jQuery(objectId).next().slideToggle();
    jQuery(objectId).next().toggleClass("toggle");
}