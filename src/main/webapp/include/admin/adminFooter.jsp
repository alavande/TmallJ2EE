<%-- 公共页面，页脚--%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false"
         language="java" import="java.util.*" pageEncoding="UTF-8" %>
<script>
    $(function () {
        $('span.year').text(new Date().getFullYear());
    })
</script>
<%--TODO--%>
<footer class="navbar navbar-default navbar-fixed-bottom navbar-inverse" style="margin-top:30px;bottom:0;right:0;height:40px">
    <div style="padding-top:12px;margin-left:20px">
        <p style="color:#999">Copyright ©
            <span class="year" style="color:#999"></span>
            renminxi All Rights Reserved | 京ICP备 XXXXXXXX号-X
        </p>
    </div>
</footer>
</body>
</html>