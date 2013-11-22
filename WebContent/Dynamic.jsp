<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <title>TagCanvas example</title>
    <!--[if lt IE 9]><script type="text/javascript" src="excanvas.js"></script><![endif]-->
    <script src="tagcanvas.js" type="text/javascript"></script>
    <script type="text/javascript">
      window.onload = function() {
        try {
          TagCanvas.Start('myCanvas','tags',{
            textColour: '#0000ff',
radiusX:0.2,
radiusY:0.2,
radiusZ:0.2,

            outlineColour: '#ff00ff',
            reverse: true,
            depth: 0.7,
            maxSpeed: 0.15,
	    
weightMode:"both",
weightSize:1.0,
weightGradient:{0:'#FF0000', 0.20:'#FF66FF',0.40:'#304073',0.60:'#006600', 0.80:'#996633', 1:'#993333'},
zoom: 1,
shape: "hcylinder",
dragControl: true,
weight: true
          });
        } catch(e) {
          // something went wrong, hide the canvas container
          document.getElementById('myCanvasContainer').style.display = 'none'
        }
      };
    </script>
  </head>
  <body>
 <%
	ArrayList list = (ArrayList)session.getAttribute("tag");
%>

<%=list.size() %>
	
    <h1>TagCanvas example page</h1>
    <div id="myCanvasContainer">
      <canvas width="600" height="600" id="myCanvas">
        <p>Anything in here will be replaced on browsers that support the canvas element</p>
      </canvas>
    </div>
    <div id="tags">
      <ul>
        <li style="font-size:200%"><a href="http://www.google.com" target="_blank">Google</a></li>
        <li style="font-size:150%"><a href="/fish">Fish</a></li>
        <li style="font-size:100%"><a href="/chips">Chips</a></li>
        <li style="font-size:75%"><a href="/salt">Salt</a></li>
        <li style="font-size=50%"><a href="/vinegar">Vinegar</a></li>
	<li style="font-size=30%"><a href="/vinegar">Sandeep</a></li>
	<li style="font-size:200%"><a href="http://www.google.com" target="_blank">Google</a></li>
        <li style="font-size:150%"><a href="/fish">Fish</a></li>
        <li style="font-size:100%"><a href="/chips">Chips</a></li>
        <li style="font-size:75%"><a href="/salt">Salt</a></li>
        <li style="font-size=50%"><a href="/vinegar">Vinegar</a></li>
	<li style="font-size=30%"><a href="/vinegar">Sandeep</a></li>
	<li style="font-size:200%"><a href="http://www.google.com" target="_blank">Google</a></li>
        <li style="font-size:150%"><a href="/fish">Fish</a></li>
        <li style="font-size:100%"><a href="/chips">Chips</a></li>
        <li style="font-size:75%"><a href="/salt">Salt</a></li>
        <li style="font-size=50%"><a href="/vinegar">Vinegar</a></li>
	<li style="font-size=30%"><a href="/vinegar">Sandeep</a></li>
      </ul>
    </div>
  </body>
</html>
