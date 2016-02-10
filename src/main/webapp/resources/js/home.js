/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//pure js function
function activateTab(obj) {
    tab = document.getElementById(obj.value);
    tabs = document.getElementsByClassName("tab");
    for (t = 0; t < tabs.length; t++) {
        if (tabs[t].id === tab.id) {
            tab.style.display = "block";
        } else {
            tabs[t].style.display = "none";
        }
    }
}
