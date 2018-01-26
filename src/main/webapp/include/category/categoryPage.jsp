<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" isELIgnored="false" import="java.util.*" %>

<title>模仿天猫官网-${c.name}</title>
<div id="category">
    <div class="categoryPageDiv">
        <img src="resources/img/category/${c.id}.jpg">
        <%@include file="sortBar.jsp"%>
        <%@include file="productByCategory.jsp"%>
    </div>
</div>