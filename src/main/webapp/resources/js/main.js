$.fn.replaceAttr = function(aName, rxString, repString) {
    return this.attr(
        aName,
        function() {
            return jQuery(this).attr(aName).replace(rxString, repString);
        }
    );
};