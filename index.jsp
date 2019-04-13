<%--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
  
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
request.setAttribute("hostPrefix","http://"+request.getServerName()+":80");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
request.setAttribute("year", sdf.format(new java.util.Date()));
request.setAttribute("tomcatUrl", "https://tomcat.apache.org/");
request.setAttribute("tomcatDocUrl", "/docs/");
request.setAttribute("tomcatExamplesUrl", "/examples/");

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
response.setHeader("Expires", "0"); // Proxies.

%>

<!DOCTYPE html>
<html lang="en"> 
    <head>
     <meta charset="UTF-8" /> 
        <script type="text/javascript" src="${hostPrefix}/js/rr.js"></script>
        <script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
        <script type="text/javascript" src="/js/loan-amortization.js"></script>
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/superagent/4.1.0/superagent.js"></script>
        <link href="favicon.ico" rel="icon" type="image/x-icon" />
        <link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
        <link href="${hostPrefix}/css/style.css" rel="stylesheet" type="text/css" />
	
	${jsSnippet}
    </head>
    <body>
     <div id="wrapper">
           <nav id="nav-wrap"><ul id="navigation" class="curved container">
                <li><a href="/">Home</a></li>
                <li><a href="/about">About</a></li>
                <li><a href="/forum">Forum</a></li>
                <li><a href="/signup">Signup</a></li>
                <li><a href="/login">Login</a></li>
                
            </ul></nav>
        <div id="content">
		<jsp:include page="WEB-INF/${contentFile}"></jsp:include>
		</div>  
            
    </div>
    </body>

</html>