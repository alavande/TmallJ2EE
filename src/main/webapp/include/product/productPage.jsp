<%@page pageEncoding="UTF-8" isELIgnored="false" import="java.util.*"
        contentType="text/html;charset=UTF-8" language="java" %>

<title>模仿天猫官网 ${p.name}</title>
<div class="categoryPictureInProductPageDiv">
    <img class="categoryPictureInProductPage" scr="resources/img/category/${p.category.id}.jpg">
</div>

<div class="productPageDiv">
    <%@include file="imgAndInfo.jsp"%>
    <%@include file="productReview.jsp"%>
    <%@include file="productDetail.jsp"%>
</div>