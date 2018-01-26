<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" isELIgnored="false" import="java.util.*" %>

<title>模仿天猫官网-${param.keyword}</title>

<div id="searchResult">
    <div class="searchResultDiv">
        <%@include file="productsBySearch.jsp"%>
    </div>
</div>