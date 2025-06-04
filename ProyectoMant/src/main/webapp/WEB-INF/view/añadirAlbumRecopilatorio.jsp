<%@ page import="es.taw.primerparcial.dto.RecopilatorioDTO" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: guzman
  Date: 3/9/21
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <title>Title</title>
</head>
<body>
<header>
</header>
<h1>Nuevo Álbum Recopilatorio</h1>


<form:form method="post" action="/crearRecopilatorio" modelAttribute="recopilatorioDTO">
    <div>
    <form:label path="nombreRecopilatorio">Nombre del recopilatorio:</form:label>
    <form:input path="nombreRecopilatorio" size="50"></form:input>
    </div>
    <div>
        <form:label path="nombreAlbum">Nombre del álbum:</form:label>
        <form:input path="nombreAlbum" size="50"></form:input>
    </div>

    <div>
        <form:label path="idCancionesRecopilatorio">Canciones:</form:label>
        <form:select path="idCancionesRecopilatorio"
        items="${canciones}"
        itemValue="cancionId"
        itemLabel="cancionName"
        size="50"></form:select>
    </div>
    <form:button>Guardar</form:button>
</form:form>

<form:form method="get" action="/añadirAlbumConFiltro" modelAttribute="filtro">
    <div>
        <form:label path="idGenero"></form:label>
        <form:radiobuttons path="idGenero"

        items="${generos}"
        itemValue="generoId"
        itemLabel="generoName"
        ></form:radiobuttons>
        <form:label path="fechaInicio">Fecha Inicio:</form:label>
        <form:input path="fechaInicio"></form:input>
        <form:label path="fechaFin">Fecha Fin:</form:label>
        <form:input path="fechaFin"></form:input>
        <form:button>Filtrar</form:button>
    </div>
</form:form>




</body>
</html>
