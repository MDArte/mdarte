/* 
 * Copyright (c) 2010 CompuCloud Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Author: Jevon Gill
 */
(function($){

    $.fn.displayTagAjax = function() {
        var links = new Array();
        var container = this;
        addItemsToArray(this.find("table .sortable a"),links);
        addItemsToArray(this.find(".pagelinks a"),links);
        $.each(links,function()
            {
                    var url = $(this).attr("href");
                    addClickEvent(container, this,url);
                    $(this).attr("href","#");
            }
        );
        return true;
    };

  function addClickEvent(ctx, element,url){
        $(element).click(
            function(){
                jQuery.ajax(
                {
                    url: url,
                    success: function(data){
                       filteredResponse =  $(data).find(this.selector);
                       if(filteredResponse.size() == 1){
                            $(this).html(filteredResponse);
                       }else{
                            $(this).html(data);
                       }
                       $(this).displayTagAjax();
                    } ,
                    data: ({'time':new Date().getTime()}),
                    context: ctx
                });
            }
        );
    }

   function addItemsToArray(items,array){
        $.each(items,function()
            {
                    array.push(this);
            }
        );        
    }
    
})(jQuery);



