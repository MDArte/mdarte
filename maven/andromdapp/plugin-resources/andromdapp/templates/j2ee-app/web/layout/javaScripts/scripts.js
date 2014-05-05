function setaAcao(nomeAcao, fcValidacao, valida , nomeForm) {
    setaAcaoForm(nomeAcao, fcValidacao, valida, nomeForm, document.forms[0]);
}

function setaAcaoForm(nomeAcao, fcValidacao, valida , nomeForm, formObj ) {
    formObj.action = "";

    if (nomeAcao.indexOf("/") == 0) {
        formObj.action = getPath() + nomeAcao + '.do';
    } else {
        formObj.action = getPath() + '/' + nomeAcao + '.do';
    }

    formObj.name = nomeForm;

    if (true && valida) {
        if (eval(fcValidacao + '(formObj)')) {
            formObj.submit();
        }
    } else {
        formObj.submit();
    }

    mostrarAguarde('conteudoPrincipal', 'testeAguarde', 'Processando', document.body.clientWidth , window.screen.height , 0, 0);
}

function setaLocation(nomeAcao) {
    var action;
    if (nomeAcao.indexOf("/") == 0) {
        action=getPath() + nomeAcao + '.do';
    } else {
        action=getPath() + '/' + nomeAcao + '.do';
    }

    window.open(action, '', 'width=700,height=500,top=50,left=100');
}

function adicionaElemento(idElementoEntrada, idElementoSaida) {
    var entrada = document.getElementById(idElementoEntrada);
    var oOption = document.createElement("OPTION");
    var saida = document.getElementById(idElementoSaida);

    if (!(entrada.value == "")) {
        saida.options.add(oOption);
        oOption.innerText = saida.options.length;
        oOption.value = entrada.value;
        oOption.text = entrada.value;
        entrada.value = "";
    }
}

function removeElemento(idElemento) {
    var elemento = document.getElementById(idElemento);
    elemento.options[elemento.options.selectedIndex] = null;
}

function armazenaElementos(idElemento, idElementoAuxiliar) {
    var elemento = document.getElementById(idElemento);
    var aux = document.getElementById(idElementoAuxiliar);

    if(elemento.length > 0) {
       var i;
       aux.value = elemento.options[0].value;
       for(i = 1; i < elemento.length; i++) {
          aux.value += ", " + elemento.options[i].value;
       }
    }
}

function desabilitaTodosOutrosCampos(idCampo) {
    var i;
    var campo = document.getElementById(idCampo);
    var form  = document.forms[0];
    
    for(i=0; i < form.elements.length; i++) {
    if(!(campo.name == form.elements[i].name) && (campo.value.length > 0)) {
            if(!(form.elements[i].type == "submit") && !(form.elements[i].type == "button") && !(form.elements[i].type == "reset")){
                form.elements[i].disabled = true;
            }
    }
    else {
            document.forms[0].elements[i].disabled = false;
    }
    }
}


var originalDivHTML = "";
var over = false;
var divMsgAguardeImg="";
var novaDiv;
var contDiv;
var bodyHtml = document.getElementsByTagName("body")[0];

function cancelarAcao(event)   // cancelar AGUARDE
{
    var tecla;
    
    if(window.event) // Internet Explorer
        tecla = window.event.keyCode;
    else // Outros
        tecla = event.which;

    if (tecla == 27){ // tecla ESC pressionada
       destruirDiv();
    }
}

function destruirDiv() //destruir AGUARDE
{
    
     divMsgAguardeImg.innerHTML = "";
    contDiv = document.getElementById("ConteudoDiv");
    contDiv.appendChild(divMsgAguardeImg);
    bodyHtml = document.getElementsByTagName("body")[0];
    
    novaDiv = document.getElementById("conteudoPrincipal");
    bodyHtml.removeChild(novaDiv);
    
}

//Div aguarde

function criarMsgAguarde(){
    bodyHtml = document.getElementsByTagName("body")[0];
    bodyHtml.setAttribute("onKeyDown","javascript:return cancelarAcao(event);");
    bodyHtml.onkeydown=cancelarAcao;
    divMsgAguardeImg = document.createElement('div');
    divMsgAguardeImg.setAttribute("id","divMsgAguardeImg");
    divMsgAguardeImg.id="divMsgAguardeImg";
    divMsgAguardeImg.innerHTML = "<img src='/${application.id}/layout/default/imagens/loading.gif'/> <p>Aguarde enquanto os dados est&atilde;o sendo processados...</p>";
    contDiv = document.getElementById("ConteudoDiv");
    contDiv.appendChild(divMsgAguardeImg);
}

function criarDiv()
{
    bodyHtml = document.getElementsByTagName("body")[0];
    novaDiv = document.createElement("div");
    novaDiv.setAttribute("style","width: 0; height: 0;visibility:hidden");
    novaDiv.style.width="0";
    novaDiv.style.height="0";
    novaDiv.style.visibility="hidden";
    novaDiv.setAttribute("id","conteudoPrincipal");
    novaDiv.id="conteudoPrincipal";
    novaDiv.innerHTML = "<div class='conteudoDimming' id='testeAguarde'></div>";
    bodyHtml.appendChild(novaDiv);    
}

function contadorSessao()
{
    
    var contador = document.forms[0].elements["invalidatePageControlCounter"];
    var contAtual;
    var contDepois;

    if (contador != null)
    {
        contAtual = eval(contador.value);
        contDepois = eval(contAtual + 2);
        contador.value = contDepois;
    }

    
}

function mostrarAguarde(divId, conteudoId, title, width, height, left, top)
{
    contadorSessao();
    criarDiv();
    criarMsgAguarde();
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
                   '<td style="width:18px" align="right"></td>' +
                   '<td style="width:18px" align="right"></td></tr></table>';
   

    // add to your div an header   
    document.getElementById(divId).innerHTML = addHeader + originalDivHTML;

    document.getElementById(divId).className = 'dimming';
    var divID = document.getElementById(divId);
    divID.setAttribute("style","opacity:0.0;filter:alpha(opacity=0);top:0px;position:absolute;background:white");
    document.getElementById(divId).style.opacity="0.0";
    document.getElementById(divId).style.filter="alpha(opacity=0)";
    document.getElementById(divId).style.top="0px";
    document.getElementById(divId).style.position="absolute";
    document.getElementById(divId).style.background="white";

    if (document.getElementById(conteudoId) == null){

        criarDiv();
        document.getElementById(divId).style.width = width + 'px';
        document.getElementById(divId).style.height = height + 'px';
        document.getElementById(divId).style.left = left + 'px';
        document.getElementById(divId).style.top = top + 'px';
        document.getElementById(divId).onmousedown = ResizeMouseDown;
    }
    document.getElementById(conteudoId).style.height = (height - document.getElementById("header").offsetHeight) + 'px';
    document.getElementById(conteudoId).style.width = document.getElementById(divId).style.width;
   
    document.getElementById(divId).style.visibility = "visible";


}

/*
 * Struts 2
 */

//Usado na atualizacao de combobox via ajax
var resetedCombos;

//Usado para gravar o id do elemento que deve ser atualizado
var gravaId;

function setaAcaoAjaxStruts2(nomeAcao , nomeForm, formato, idElemento) {
    resetedCombos = new Array();
    document.forms[0].action = "";
    if (nomeAcao.indexOf("/") == 0) {
        document.forms[0].action = getPath() + nomeAcao + formato;
    } else {
        document.forms[0].action = getPath() + '/' + nomeAcao + formato;
    }
    document.forms[0].name = nomeForm;
    var options;
    if (idElemento != null && idElemento != "") {
        gravaId = idElemento;
        options = {
            datatype: 'html',
            success: function(resposta){
                document.getElementById(gravaId).innerHTML = resposta;
                atualizaElementos();
            }
        };
    } else {
        options = {
            datatype: 'xml',
            success: function(xmlResposta){
                $(xmlResposta).find('raiz').children().each(function() {
                    var nomeNo = (this).nodeName;
                    var element = document.getElementById(nomeNo);
                    if (element.tagName == "select" || element.tagName == "SELECT") {
                        //Verifica se ja removeu os options desse select
                        if (!resetedCombos[nomeNo]) {
                            element.innerHTML = '';
                            resetedCombos[nomeNo] = true;
                        }
                        var option = document.createElement("option");
                        option.text = $(this).text();
                        option.value = $(this).attr('chave');
                        try {
                            // for IE earlier than version 8
                            element.add(option,element.options[null]);
                        } catch (e) {
                            element.add(option,null);
                        }
                    } else {
                        element.innerHTML = $(this).text();
                    }
                });
            }
        };
    }
    $(document.forms[0]).ajaxSubmit(options);
}

function setaAcaoStruts2(nomeAcao , nomeForm, formato) {
    document.forms[0].action = "";
    if (nomeAcao.indexOf("/") == 0) {
        document.forms[0].action = getPath() + nomeAcao + formato;
    } else {
        document.forms[0].action = getPath() + '/' + nomeAcao + formato;
    }
    document.forms[0].name = nomeForm;

    document.forms[0].submit();
}