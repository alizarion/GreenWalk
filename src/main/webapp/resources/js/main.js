$.fn.replaceAttr = function(aName, rxString, repString) {
    return this.attr(
        aName,
        function() {
            return jQuery(this).attr(aName).replace(rxString, repString);
        }
    );
};
function imagesHover(){
    var div = $('div.sc_menu').each(
        function(index){
            ul=   $(this).find('div.sc_submenu');
            ulPadding = 0;
            //Get menu width
            var divWidth =  $(this).width();

            //Remove scrollbars
            $(this).css({overflow: 'hidden'});

            //Find last image container
            var lastLi = ul.find('a:last-child');
            //When user move mouse over menu
            $(this).mousemove(function(e){
                //As images are loaded ul width increases,
                //so we recalculate it each time
                if( lastLi[0] != undefined){
                    var ulWidth = lastLi[0].offsetLeft + lastLi.outerWidth() + ulPadding;
                    var left = (e.pageX -  $(this).offset().left) * (ulWidth-divWidth) / divWidth;
                    $(this).scrollLeft(left);
                }
            });
        }
    )

}
