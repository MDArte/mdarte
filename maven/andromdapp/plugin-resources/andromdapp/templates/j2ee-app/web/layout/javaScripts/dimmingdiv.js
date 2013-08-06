var isMozilla;
var objDiv = null;
var originalDivHTML = "";
var DivID = "";
var ConteudoID = "";
var over = false;

var folga = 10;
var canResize = false;
var resizing = false;
var resizeType = 0;

var minHeight = 200;
var minWidth = 200;

var lant, tant, want, hant;
var maximizado = false;


function displayFloatingDiv(divId, conteudoId, title, width, height, left, top, path) 
{

    DivID = divId;
    ConteudoID = conteudoId;
    document.getElementById(divId).style.width = width + 'px';
    document.getElementById(divId).style.height = height + 'px';
    document.getElementById(divId).style.left = left + 'px';
    document.getElementById(divId).style.top = top + 'px';
    document.getElementById(divId).onmousedown = ResizeMouseDown;
	
	var addHeader;
	
	if (originalDivHTML == "")
	    originalDivHTML = document.getElementById(divId).innerHTML;
	
	addHeader = '<table style="width:' + width + 'px" class="floatingHeader" id="header">' +
	            '<tr><td ondblclick="void(0);" onmouseover="over=true;" onmouseout="over=false;"  style="cursor:move;height:18px">' + title + '</td>' + 
	           	'<td style="width:18px" align="right"><a href="javascript:resizeFloatingDiv(\'' + divId + '\');void(0);">' + 
	            '<img alt="Resize..." title="Resize..." src=\"' + path +  'dimming/maximizar.gif\"border="0"></a></td>' +
	           	'<td style="width:18px" align="right"><a href="javascript:hiddenFloatingDiv(\'' + divId + '\');void(0);">' + 
	            '<img alt="Close..." title="Close..." src=\"' + path +  'dimming/close.gif\"border="0"></a>' + 
	            '</td></tr></table>';
	

    // add to your div an header	
	document.getElementById(divId).innerHTML = addHeader + originalDivHTML;
	
	
	document.getElementById(divId).className = 'dimming';
	
	document.getElementById(conteudoId).style.height = (height - document.getElementById("header").offsetHeight) + 'px';
	document.getElementById(conteudoId).style.width = document.getElementById(divId).style.width;
	
	document.getElementById(divId).style.visibility = "visible";


}


//
//
//
function hiddenFloatingDiv(divId) 
{
	document.getElementById(divId).innerHTML = originalDivHTML;
	document.getElementById(divId).style.visibility='hidden';
	
	
	DivID = "";
}

function resizeFloatingDiv(divId)
{
	if(!maximizado){
		lant = document.getElementById(divId).style.left;
		tant = document.getElementById(divId).style.top;
		want = document.getElementById(divId).offsetWidth;
		hant = document.getElementById(divId).offsetHeight;
		
		document.getElementById(divId).style.left = folga + 'px';
		document.getElementById(divId).style.top = folga + 'px';
		document.getElementById(ConteudoID).style.width = (document.body.clientWidth - 4*folga) + 'px';
		document.getElementById(ConteudoID).style.height = (document.body.clientHeight - document.getElementById("header").offsetHeight - 4*folga) + 'px';
		document.getElementById(divId).style.width = (document.body.clientWidth - 4*folga) + 'px';
		document.getElementById(divId).style.height = (document.body.clientHeight - 4*folga) + 'px';
		
		maximizado = true;
	}else{
		document.getElementById(divId).style.left = lant;
		document.getElementById(divId).style.top = tant;
		document.getElementById(ConteudoID).style.width = want + 'px';
		document.getElementById(ConteudoID).style.height = (hant - document.getElementById("header").offsetHeight) + 'px';
		document.getElementById(divId).style.width = want + 'px';
		document.getElementById(divId).style.height = hant + 'px';
		
		maximizado = false;
	}
		
	document.getElementById("header").style.width = document.getElementById(DivID).style.width;
}

function getXCoord(el) {
	x=0;
	while(el){
		x+=el.offsetLeft;
		el=el.offsetParent;
	}
	return x;
}

function getYCoord(el) {
	y=0;
	while(el){
		y+=el.offsetTop;
		el=el.offsetParent;
	}
	return y;
}

function ResizeMouseUp(){
	canResize = false;
	resizing = false;
	resizeType = 0;
}

function ResizeMouseDown(e){
	if(canResize)
		resizing = true;
}

function ResizeMouseMove(e){
	if (resizing && document.getElementById(DivID))
	{
		l = getXCoord(document.getElementById(DivID));
		t = getYCoord(document.getElementById(DivID));
		w = document.getElementById(DivID).offsetWidth;
		h = document.getElementById(DivID).offsetHeight;
	
		var x, y;
	
		if (isMozilla) {
			x = e.pageX;
			y = e.pageY;
		} else {
			x = event.clientX + document.body.scrollLeft;
			y = event.clientY + document.body.scrollTop;
		}

		var newWidth = (x - l - folga);
		var newHeight = (y - t - folga);

		if(newWidth <= minWidth) newWidth = minWidth;
		if(newHeight <= minHeight) newHeight = minHeight;

		switch(resizeType){
			case 1:
				document.getElementById(ConteudoID).style.width = newWidth + 'px';
				document.getElementById(ConteudoID).style.height = (newHeight - document.getElementById("header").offsetHeight) + 'px';
				document.getElementById(DivID).style.width = newWidth + 'px';
				document.getElementById(DivID).style.height = newHeight + 'px';
				break;
			case 2:
				document.getElementById(ConteudoID).style.width = newWidth + 'px';
				document.getElementById(DivID).style.width = newWidth + 'px';
				break;
			case 3:
				document.getElementById(ConteudoID).style.height = (newHeight - document.getElementById("header").offsetHeight) + 'px';
				document.getElementById(DivID).style.height = newHeight + 'px';
				break;
		}

		document.getElementById("header").style.width = document.getElementById(DivID).style.width;				
	}
}

function CheckResizeMouseMove(e){
	if(!resizing && document.getElementById(DivID)){

		l = getXCoord(document.getElementById(DivID));
		t = getYCoord(document.getElementById(DivID));
		w = document.getElementById(DivID).offsetWidth;
		h = document.getElementById(DivID).offsetHeight;
	
		var x, y;
	
		if (isMozilla) {
			x = e.pageX;
			y = e.pageY;
		} else {
			x = event.clientX + document.body.scrollLeft;
			y = event.clientY + document.body.scrollTop;
		}

		if (x >= l && x <= l + w && y >= t && y <= t + h)
		{
			if((x >= l + w - folga && y >= t + h - folga)){
				resizeType = 1;
				canResize = true;
				document.body.style.cursor = 'nw-resize';
			}
			else if(x >= l + w - folga){
				resizeType = 2;
				canResize = true;
				document.body.style.cursor = 'w-resize';
			}
			else if(y >= t + h - folga){
				resizeType = 3;
				canResize = true;
				document.body.style.cursor = 's-resize';
			}else if(resizing == false){
				resizeType = 0;
				document.body.style.cursor = 'default';	
			}
		}else if(resizing == false){
			resizeType = 0;
			document.body.style.cursor = 'default';	
		}
	}
}


//
//
//
function MouseDown(e) 
{
    if (over)
    {
        if (isMozilla) {
            objDiv = document.getElementById(DivID);
            X = e.layerX;
            Y = e.layerY;
            return false;
        }
        else {
            objDiv = document.getElementById(DivID);
            objDiv = objDiv.style;
            X = event.offsetX;
            Y = event.offsetY;
        }
    }
    
}


//
//
//
function MouseMove(e) 
{ 	
    	
    if (objDiv) {
        if (isMozilla) {
			objDiv.style.top = (e.pageY-Y) + 'px';
            objDiv.style.left = (e.pageX-X) + 'px';
            return false;
        }
        else 
        {
            objDiv.pixelLeft = event.clientX-X + document.body.scrollLeft;
            objDiv.pixelTop = event.clientY-Y + document.body.scrollTop;
            return false;
        }
    }
}

//
//
//
function MouseUp() 
{
    	
    objDiv = null;
}


//
//
//
function init()
{
    // check browser
    isMozilla = (document.all) ? 0 : 1;


    if (isMozilla) 
    {
	document.releaseEvents(Event.MOUSEMOVE | Event.MOUSEDOWN | Event.MOUSEUP); 
        document.captureEvents(Event.MOUSEMOVE | Event.MOUSEDOWN | Event.MOUSEUP);
    }

    var old = (document.onmousedown) ? document.onmousedown : function () {};
    document.onmousedown = function (event) {old(); MouseDown(event)};	
    
    var old = (document.onmousemove) ? document.onmousemove : function (event) {};
    document.onmousemove = function (event) {old(event); MouseMove(event); CheckResizeMouseMove(event); ResizeMouseMove(event);};	
    
    var old = (document.onmouseup) ? document.onmouseup : function () {};
    document.onmouseup = function () {old(); MouseUp(); ResizeMouseUp();};	
    
}

// call init
init();
