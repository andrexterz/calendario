/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function initialize() {
    play();
    activateTab();
}

//ativa tabs na pag. home
function activateTab(obj) {
    if (obj !== undefined) {
        tab = document.getElementById(obj.value);
        tabs = document.getElementsByClassName("tab");
        for (t = 0; t < tabs.length; t++) {
            if (tabs[t].id === tab.id) {
                tab.style.display = "block";
            } else {
                tabs[t].style.display = "none";
            }
        }
    } else {
        document.forms['formTabs'].reset();
    }
}

//ativa carrossel ao atualizar pagina
function play() {
    jQuery("#owlSlider").owlCarousel(
            {
                items: 1,
                singleItem: true,
                pagination: false,
                navigation: true,
                autoPlay: false,
                navigationText: ["&lt;", "&gt;"]
            }
    );
}

//marca dias especificos no calendario
//implementar modo de adicionar dados a <jsonData> c/ jQuery.getJSON(...);
function highlightDays(date) {
    try {
        dates = jsonData[date.getMonth() + 1];
        for (var i = 0; i < dates.length; i++) {
            d = new Date(dates[String(i)]);
            if (date.getTime() === d.getTime()) {
                return [true, 'highlightDay'];
            }
        }
    } catch (error) {
        if (window.console) {
            console.log(error);
        }
    }
    return [true, ''];
}


function toggleCalendarMenu() {
    jQuery('#calendarSelector').toggleClass("calendarSelectorToggle");
}