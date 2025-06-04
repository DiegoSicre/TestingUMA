<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="java.util.List" %>
<%@ page import="es.taw.primerparcial.entity.Artista" %><%--
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
<% List<Artista> listaArtistas = (List<Artista>) request.getAttribute("artistas");%>

<body>
<header>
    <h3>Filtrar por nombre:</h3>
    <form:form method="get" action="/filtrarArtistaPorNombre" modelAttribute="filtro">
        <form:label path="cadenaFiltroNombre">Contiene:</form:label>
        <form:input path="cadenaFiltroNombre"></form:input>
        <form:button>Filtrar</form:button>

    </form:form>
</header>


<h1>Lista de artistas de la BD</h1>

<table border="1px">



    <thead>
    <tr>
        <th>NOMBRE</th>
        <th>NÚM.ÁLBUMES</th>
        <th>NÚM.CANCIONES</th>
        <th>NÚM.COLABORACIONES</th>

    </tr>
    </thead>
    <tbody>
    <% //Aquí vamos a tener tantas columnas como artistas hayan
    for(Artista aux : listaArtistas){
    %>
    <tr>
        <td><%=aux.getArtistaName()%></td>
        <td><%=aux.getAlbumList().size()%></td>
        <td><%=aux.getCancionesPropias().size()%></td>
        <td><%=aux.getCancionList().size()%></td>
    </tr>
    <% }%>
    </tbody>

</table>



<a href="/añadirAlbum">Añadir álbum</a>

</body>
<br>
</html>
