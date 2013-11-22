<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Tag Advisor</title>
<link href="http://fonts.googleapis.com/css?family=Oxygen:400,700,300" rel="stylesheet" type="text/css" />
<link href="style.css" rel="stylesheet" type="text/css" media="screen" />

    <!--[if lt IE 9]><script type="text/javascript" src="excanvas.js"></script><![endif]-->
    <script src="tagcanvas.js" type="text/javascript"></script>
    <script type="text/javascript">
      window.onload = function() {
        try {
            TagCanvas.Start('myCanvas','tags',{
            textColour: '#0000ff',
			radiusX:0.1,
			radiusY:0.1,
			radiusZ:0.1,
			initial:[0.3,0.15],
            outlineColour: '#ff00ff',
            reverse: true,
            depth: 0.4,
            maxSpeed: 0.05,
            frontSelect:true,
            shuffleTags:true,
	    	lock:"xy",
			weightMode:"both",
			weightSize:1.0,
			weightGradient:{0:'#FF0000',0.10:'#FF0066', 0.20:'#009900',0.30:'#009999',0.40:'#FF6600',0.50:'#0000FF',0.60:'#800000', 0.70:'#47008F',0.80:'#FFFF00', 0.90:'#00FF00',1:'#006699'},
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
<div id="wrapper">
	<div id="logo" class="container">
		<h3>Tag Advisor System </h3>	
	</div>
	<div id="menu-wrapper">
		<div id="menu" class="container">
			<ul>
			</ul>
		</div>
	</div>
	
	<div id="page" class="container">
		<div id="content">
			<div class="post">
				<h2 class="title"><a href="#"><b>Recommended Tag Cloud </b></a></h2>
				<div class="entry">

<h4 align="center">URL : &nbsp;&nbsp;&nbsp; <%=request.getAttribute("url")%></h4>
<h4 align="center">Query : &nbsp;&nbsp;&nbsp; <%=request.getAttribute("query")%></h4>

<br>
<br>

<%
	ArrayList list = (ArrayList)request.getAttribute("tags");
	request.setAttribute("tag", list);	
%>

<div id="myCanvasContainer">
      <canvas width="700" height="400" id="myCanvas">
        <p>Anything in here will be replaced on browsers that support the canvas element</p>
      </canvas>
    </div>
        <div id="tags">
      <ul>
      <%
      	int i=0,per=300;
      	
      while(i<list.size() && i<3)
      {      
    	  if(!list.get(i).equals(" "))
    	  {
      %>
              <li style="font-size:<%=per%>%"><a href="" target="_blank"><%=list.get(i)%></a></li>
      <% 
    	  }
      i++;
	  per-=30;
      }
      
      while(i<list.size() && i<8)
      {      
    	  if(!list.get(i).equals(" "))
    	  {
      %>
              <li style="font-size:<%=per%>%"><a href="" target="_blank"><%=list.get(i)%></a></li>
      <% 
    	  }
      i++;
      per-=3;
      }
      
      per=170;
      while(i<list.size() && i<14)
      {      
    	  if(!list.get(i).equals(" "))
    	  {
      %>
              <li style="font-size:<%=per%>%"><a href="" target="_blank"><%=list.get(i)%></a></li>
      <% 
    	  }
      i++;
      per-=3;
      }
      
      per=130;
      while(i<list.size() && i<=20)
      {      
    	  if(!list.get(i).equals(" "))
    	  {
      %>
              <li style="font-size:<%=per%>%"><a href="" target="_blank"><%=list.get(i)%></a></li>
      <% 
    	  }
      i++;
      per-=3;
      }
      
      %>

    </ul>
    
    </div>
    				</div>
			</div>
			<div style="clear: both;">&nbsp;</div>
		</div>
		<!-- end #content -->
		<div style="clear: both;">&nbsp;</div>
	</div>
	<!-- end #page --> 
	<a href="index.jsp">Back..</a>
</div>
<div id="footer-bg">
	<div id="footer-content" class="container">
		<div id="column1">
			<h2>Information Retrieval & Extraction</h2>
			<ul class="list-style2">
				<li class="first">Course Instructor : Dr. Vasudeva Varma</li>
				<li>Mentor : Ajay Dubey</li>
			</ul>
		</div>
	</div>
</div>
<div id="footer">
	<p>© Team 13- Juhi Kulshreshtha, Pallavi Gupta, Sandeep Sharma </p>
</div>
<!-- end #footer -->
</body>
</html>
