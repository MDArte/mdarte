function getElementValueById(id)
{
    var element = getDiv(id);
    if(element.value)
        return element.value

    return false;
}

function getDiv(divID)
{
    if( document.layers ) //Netscape layers
        return document.layers[divID];

    if( document.getElementById ) //DOM; IE5, NS6, Mozilla, Opera
        return document.getElementById(divID);

    if( document.all ) //Proprietary DOM; IE4
        return document.all[divID];

    if( document[divID] ) //Netscape alternative
        return document[divID];

    return false;
}

function openWindow(href,name,centered,attributes,width,height)
{
    var w = (width) ? width : 400;
    var h = (height) ? height : 300;

    var windowWidth = (document.all) ? document.body.clientWidth : window.outerWidth;
    var windowHeight = (document.all) ? document.body.clientHeight : window.outerHeight;

    x = (windowWidth-w) / 2;
    y = (windowHeight-h) / 2;

    var properties = (attributes)
        ? "toolbar=yes,directories=yes,status=yes,menubar=yes,copyhistory=yes,location=yes"
        : "toolbar=no,directories=no,status=no,menubar=no,copyhistory=no,location=no";

    properties = "width="+w+",height="+h+",top="+y+",left="+x+",scrollbars=yes,resizable=yes," + properties;

    window.open(href,name,properties);
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

documentall = document.all;

function formatamoney(milhar, decimal, c) {
    var t = this; if(c == undefined) c = 2;		
    var p, d = (t=t.split(milhar))[1].substr(0, c);
    for(p = (t=t[0]).length; (p-=3) >= 1;) {
	        t = t.substr(0,p) + milhar + t.substr(p);
    }
    return t + decimal + d + Array(c + 1 - d.length).join(0);
}

String.prototype.formatCurrency=formatamoney

function demaskvalue(valor, currency, milhar, decimal){
/*
* Se currency ?false, retorna o valor sem apenas com os nmeros. Se ?true, os dois ltimos caracteres s? considerados as 
* casas decimais
*/
var val2 = '';
var strCheck = '0123456789';
var len = valor.length;
	if (len== 0){
		return 0.00;
	}

	if (currency ==true){	
		/* Elimina os zeros ?esquerda 
		* a vari?el  <i> passa a ser a localiza?o do primeiro caractere ap? os zeros e 
		* val2 cont? os caracteres (descontando os zeros ?esquerda)
		*/
		
		for(var i = 0; i < len; i++)
			if ((valor.charAt(i) != '0') && (valor.charAt(i) != decimal)) break;
		
		for(; i < len; i++){
			if (strCheck.indexOf(valor.charAt(i))!=-1) val2+= valor.charAt(i);
		}

		if(val2.length==0) return "0.00";
		if (val2.length==1)return "0.0" + val2;
		if (val2.length==2)return "0." + val2;
		
		var parte1 = val2.substring(0,val2.length-2);
		var parte2 = val2.substring(val2.length-2);
		var returnvalue = parte1 + milhar + parte2;
		return returnvalue;
		
	}
	else{
			/* currency ?false: retornamos os valores COM os zeros ?esquerda, 
			* sem considerar os ltimos 2 algarismos como casas decimais 
			*/
			val3 ="";
			for(var k=0; k < len; k++){
				if (strCheck.indexOf(valor.charAt(k))!=-1) val3+= valor.charAt(k);
			}			
	return val3;
	}
}

function reais(obj,event) {
	verificaMoeda(obj,event,".",",");
}

function dolar(obj,event) {
	verificaMoeda(obj,event,",",".");
}

function verificaMoeda(obj,event,milhar,decimal){

var whichCode = (window.Event) ? event.which : event.keyCode;
/*
Executa a formata?o ap? o backspace nos navegadores !document.all
*/
if (whichCode == 8 && !documentall) {	
/*
Previne a a?o padr? nos navegadores
*/
	if (event.preventDefault){ //standart browsers
			event.preventDefault();
		}else{ // internet explorer
			event.returnValue = false;
	}
	var valor = obj.value;
	var x = valor.substring(0,valor.length-1);
	obj.value= demaskvalue(x,true,milhar,decimal).formatCurrency(milhar, decimal);
	return false;
}
/*
Executa o Formata Reais e faz o format currency novamente ap? o backspace
*/
Formata(obj,milhar,decimal,event);
} // end reais

function backspaceReais(obj,event) {
	backspace(obj,event,".",",");
}

function backspaceDolar(obj,event) {
	backspace(obj,event,",",".");
}

function backspace(obj,event, milhar, decimal) {
/*
Essa funcaoo basicamente altera o  backspace nos input com m?cara reais para os navegadores IE e opera.
O IE nao detecta o keycode 8 no evento keypress, por isso, tratamos no keydown.
Como o opera suporta o infame document.all, tratamos dele na mesma parte do codigo.
*/
	var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
	var whichCode = (window.Event) ? event.which : event.keyCode;

	if ((is_chrome && whichCode == 8) || (!is_chrome && whichCode == 8 && documentall)) {
		var valor = obj.value;
		var x = valor.substring(0, valor.length - 1);
		var y = demaskvalue(x, true, milhar, decimal).formatCurrency(milhar, decimal);

		obj.value = ""; //necessario para o opera
		obj.value += y;

		if (event.preventDefault) { //standart browsers
			event.preventDefault();
		}else { // internet explorer
			event.returnValue = false;
		}
		return false;
	}// end if
}// end backspace

function Formata(fld, milSep, decSep, e) {
var sep = 0;
var key = '';
var i = j = 0;
var len = len2 = 0;
var strCheck = '0123456789';
var aux = aux2 = '';
var whichCode = (window.Event) ? e.which : e.keyCode;

//if (whichCode == 8 ) return true; //backspace - estamos tratando disso em outra fun?o no keydown
if (whichCode == 0 ) return true;
if (whichCode == 9 ) return true; //tecla tab
if (whichCode == 13) return true; //tecla enter
if (whichCode == 16) return true; //shift internet explorer
if (whichCode == 17) return true; //control no internet explorer
if (whichCode == 27 ) return true; //tecla esc
if (whichCode == 34 ) return true; //tecla end
if (whichCode == 35 ) return true;//tecla end
if (whichCode == 36 ) return true; //tecla home

/*
O trecho abaixo previne a a?o padr? nos navegadores. N? estamos inserindo o caractere normalmente, mas via script
*/

if (e.preventDefault){ //standart browsers
		e.preventDefault()
	}else{ // internet explorer
		e.returnValue = false
}

var key = String.fromCharCode(whichCode);  // Valor para o c?igo da Chave
if (strCheck.indexOf(key) == -1) return false;  // Chave inv?ida

/*
Concatenamos ao value o keycode de key, se esse for um nmero
*/
fld.value += key;

var len = fld.value.length;
var bodeaux = demaskvalue(fld.value,true,milSep,decSep).formatCurrency(milSep, decSep);
fld.value=bodeaux;

/*
Essa parte da fun?o t? somente move o cursor para o final no opera. Atualmente n? existe como mov?lo no konqueror.
*/
  if (fld.createTextRange) {
    var range = fld.createTextRange();
    range.collapse(false);
    range.select();
  }
  else if (fld.setSelectionRange) {
    fld.focus();
    var length = fld.value.length;
    fld.setSelectionRange(length, length);
  }
  return false;

}

function replaceAll(string, token, newtoken) {
	while (string.indexOf(token) != -1) {
 		string = string.replace(token, newtoken);
	}
	return string;
}
function moedaToNumber(strValor){
  if (strValor.indexOf(",") != -1){
  	strValor = replaceAll(strValor,".","");
  	strValor = replaceAll(strValor,",",".");
  }
  return strValor;
}

function transformaCampos(){
  document.all.valorUnitario.value = moedaToNumber(document.all.valorUnitario.value);
  return true; 
}

/*REAL*/
function isValorNumericoBR(pVal)
{
  var reTipo = /^[+-]?((\d+|\d{1,3}(\.\d{3})+)(\,\d{2})?|\,\d+)$/;
  return reTipo.test(pVal);
}

/*DOLLAR*/
function isValorNumericoUS(pVal)
{
  var reTipo = /^[+-]?((\d+|\d{1,3}(\,\d{3})+)(\.\d{2})?|\.\d+)$/;
  return reTipo.test(pVal);
}

function limpaStringParaMoney(element, currency) {
	
	var esperado = "";
	if (currency == undefined || currency == "BR")
	  {
	  	if(isValorNumericoBR(element.value))
	  		return true;
	  	esperado = "1.234,56";
	  }
	else if (currency == "US") {
	  if(isValorNumericoUS(element.value))
		  return true;
	  esperado = "1,234.56";
  }
  else {
	  alert ('Moeda inválida. Use BR ou US.');
	  element.value = '';
	  return false;
  }

  alert ('Valor inválido: ' + element.value + '\n\nFormato esperado: ' + esperado);
  element.value = '';
  return false;
}

/*fim moeda*/

/* Máscara para números decimais */

function floatMask(event, name) {
	var tecla = (window.event) ? window.event.keyCode : event.which;
	var valor = String.fromCharCode(tecla);
	var element = $(name);
	if (element.length != 0) {
		var newValor = element.val() + valor;
		var regex = /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d*)?$/g
		//var regex = /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\,\d*)?$/g --> regex para float que usam virgula como separador decimal
		if (regex.test(newValor)) return true;
		else {
			if (tecla == 8 || tecla == 9 || tecla == 0 || tecla == 37 || tecla == 39 || tecla == 127) return true;
			return false;
		}
	}
}

