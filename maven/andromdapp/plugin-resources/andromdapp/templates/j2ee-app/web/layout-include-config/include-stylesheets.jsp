<%@ page session="false" %>

<%
pageContext.setAttribute("layout",
org.andromda.presentation.bpm4struts.LayoutConfiguration.instance().getLayoutConfiguration());
%>

<!-- 
Adicionar css usando o seguinte formato:
<link rel="stylesheet" type="text/css" media="screen" href="/contexto/layout/${layout}/<nome_arquivo>.css"/>"></link>
-->

<link rel="stylesheet" type="text/css" media="screen" href="/${projectId}/layout/${layout}/default-application.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="/${projectId}/layout/${layout}/default-calendar.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="/${projectId}/layout/${layout}/dimming/dimming.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="/${projectId}/layout/${layout}/default/style-default.css"></link>
<link rel="stylesheet" type="text/css" media="screen" href="/${projectId}/layout/${layout}/menu/menu.css"></link>


