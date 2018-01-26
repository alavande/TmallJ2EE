<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*" isELIgnored="false" pageEncoding="UTF-8" %>

<script>
    $(function () {
        $('.sortBarPrice').keyup(function () {
            var num = $(this).val();
            num = parseInt(num);
            if (isNaN(num) || num < 1){
                num = '';
            }

            $(this).val(num);
        });

        $('#priceLimit').click(function () {
            var start = $('.beginPrice').val();
            var end = $('.endPrice').val();
            $('.productUnit').hide();
            $('.productUnit').each(function () {
                var price = $(this).attr('price');
                if (start.length > 0 && end.length > 0){
                    if (price >= start && price <= end)
                        $(this).show();
                } else if(start.length == 0 && end.length > 0){
                    if (price <= end)
                        $(this).show();
                } else if (end.length == 0 && start.length > 0){
                    if (price >= start)
                        $(this).show();
                } else {
                    $(this).show();
                }
            });
        });
    })
</script>

<div class="categorySortBar">

    <table class="categorySortBarTable categorySortTable">
        <tr>
            <td <c:if test="${'all'==param.sort||empty param.sort}">class="grayColumn"</c:if> >
                <a href="?cid=${c.id}&sort=all">综合<span class="glyphicon glyphicon-arrow-down"></span></a>
            </td>
            <td <c:if test="${'review'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=review">人气<span class="glyphicon glyphicon-arrow-down"></span></a>
            </td>
            <td <c:if test="${'date'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=date">新品<span class="glyphicon glyphicon-arrow-down"></span></a>
            </td>
            <td <c:if test="${'saleCount'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=saleCount">销量<span class="glyphicon glyphicon-arrow-down"></span></a>
            </td>
            <td <c:if test="${'price'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=price&orientation=${orientation == 'down'? 'up':'down'}">价格<span class="glyphicon glyphicon-resize-vertical"></span></a>
            </td>
        </tr>
    </table>

    <table class="categorySortBarTable">
        <tr>
            <td><input class="sortBarPrice beginPrice" type="text" placeholder="请输入"></td>
            <td class="grayColumn priceMiddleColumn">-</td>
            <td><input class="sortBarPrice endPrice" type="text" placeholder="请输入"></td>
        </tr>
    </table>

    <table class="categorySortBarTable">
        <tr>
            <td>
                <a href="#nowhere" id="priceLimit" style="text-decoration:none">确定</a>
            </td>
        </tr>
    </table>

</div>