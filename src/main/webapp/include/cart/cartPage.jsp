<%@page pageEncoding="UTF-8" isELIgnored="false" import="java.util.*"
        contentType="text/html;charset=UTF-8" language="java" %>

<script>
    $(function () {
        root = '<%=request.getSession().getServletContext().getContextPath()%>';

        $('.cartProductTable').ready(function () {

            $('.orderItemNumberSetting').each(function () {
                stock = parseInt($(this).siblings('.numberPlus').attr('stock'));
                $(this).keyup(function () {
                    var num = $(this).val();
                    var oiid = $(this).attr('oiid');
                    stock = $(this).siblings('.numberPlus').attr('stock');
                    var price = $(this).siblings('span.orderItemPromotePrice').text();
                    num = parseInt(num);
                    if (isNaN(num) || num < 1)
                        num = 1;
                    if (num > stock)
                        num = stock;

                    $(this).val(num);
                    var pid = $(this).attr('pid');
                    $('.cartProductItemSmallSumPrice[pid='+ pid +']').text('￥' + formatMoney(parseInt(price) * parseInt(num)));
                    $('.cartProductItemSmallSumPrice[pid='+ pid +']').attr('price', parseInt(price) * parseInt(num));

                    updateNum(oiid, num);
                    disableButton();
                    changeTotal();
                });

                disableButton();
            });
        });

        $('.numberMinus').click(function () {
            $in = $(this).siblings('input');

            var price = $(this).siblings('span.orderItemPromotePrice').text();
            var pid = $(this).attr('pid');

            var num = $in.val();
            if (num > 1){
                num--;
                $in.val(num);
                $('.cartProductItemSmallSumPrice[pid='+ pid +']').text('￥' + formatMoney(parseInt(price) * parseInt(num)));
                $('.cartProductItemSmallSumPrice[pid='+ pid +']').attr('price', parseInt(price) * parseInt(num));

            }
            updateNum($in.attr('oiid'), num)
            disableButton();
            changeTotal();
        });

        $('.numberPlus').click(function () {
            $in = $(this).siblings('input');

            var stock = parseInt($(this).siblings('span.orderItemStock').text());
            var price = $(this).siblings('span.orderItemPromotePrice').text();
            var pid = $(this).attr('pid');
            var itemPrice = $('.cartProductItemSmallSumPrice[pid='+ pid +']');

            var num = $in.val();
            if (num < stock){
                num++;
                $in.val(num);
                $(itemPrice).text('￥' + formatMoney(parseInt(price) * parseInt(num)));
                $(itemPrice).attr('price', parseInt(price) * parseInt(num));
            }
            updateNum($in.attr('oiid'), num);
            disableButton();
            changeTotal();
        });

        $('.selectAllItem').click(function () {
            var selectit = $(this).attr('selectit');
            var path = $(this).attr('src').split('/');

            totalNum = 0;
            totalPrice = 0;

            if ('false' == selectit){
                path[3] = 'cartSelected.png';
                $('.selectAllItem').attr('selectit', 'true');
                $('.cartProductItemIfSelected').attr('selectit', 'true');


                $('input.orderItemNumberSetting').each(function () {
                    totalNum += parseInt($(this).val());
                });
                $('span.cartProductItemSmallSumPrice').each(function () {
                    totalPrice += parseInt($(this).attr('price'));
                });

                $('tr.cartProductItemTR').css("background-color","#FFF8E1");
                $('button.createOrderButton').attr('disabled', false).attr('style', 'background-color:#C40000');
            } else {
                path[3] = 'cartNotSelected.png';
                $('.selectAllItem').attr('selectit', 'false');
                $('.cartProductItemIfSelected').attr('selectit', 'false');
                $('tr.cartProductItemTR').css("background-color","#fff");
                $('button.createOrderButton').attr('disabled', true).attr('style', '');
            }

            $('.cartSumNumber').text(totalNum);
            $('.cartSumPrice').text('￥' + formatMoney(parseInt(totalPrice)));
            $('.cartTitlePrice').text('￥' + formatMoney(parseInt(totalPrice)));

            $('.selectAllItem').attr('src', path.join('/'));
            $('.cartProductItemIfSelected').attr('src', path.join('/'));
        });

        $('.cartProductItemIfSelected').click(function () {
           var selected = $(this).attr('selectit');
           var path = $(this).attr('src').split('/');
           var oiid = $(this).attr('oiid');
           var count = 0;
           var items = $('.cartProductItemIfSelected');
           var num = parseInt($('.orderItemNumberSetting[oiid='+oiid+']').val());
           var totalNum = parseInt($('.cartSumNumber').text());

           var totalPrice = parseInt($('.cartSumPrice').attr('totalPrice'));
           var itemPrice = parseInt($('.cartProductItemSmallSumPrice[oiid='+ oiid +']').attr('price'));
           for(var i = 0; i < items.length; i++){
               if($(items[i]).attr('selectit') == 'true' && $(items[i]).attr('oiid') != oiid)
                   count++;
           }

           if ('false' == selected){
               path[3] = 'cartSelected.png';
               selected = 'true';
               totalPrice += itemPrice;
               totalNum += num;
               $(this).parents("tr.cartProductItemTR").css("background-color","#FFF8E1");
           } else {
               path[3] = 'cartNotSelected.png';
               selected = 'false';
               totalPrice -= itemPrice;
               totalNum -= num;
               $(this).parents("tr.cartProductItemTR").css("background-color","#fff");
           }

           if (count == 0) {
               if ('true' == selected)
                    $('button.createOrderButton').attr('disabled', false).attr('style', 'background-color:#C40000');
               else
                   $('button.createOrderButton').attr('disabled', true).attr('style', '');
           }

           if (count == 2){
               if ('true' == selected)
                   $('.selectAllItem').attr('selectit', 'true').attr('src', path.join('/'));
               else
                   $('.selectAllItem').attr('selectit', 'false').attr('src', path.join('/'));
           }

            $('.cartSumNumber').text(totalNum);
            $('.cartSumPrice').attr('totalPrice', totalPrice).text('￥' + formatMoney(totalPrice));
            $('.cartTitlePrice').text('￥' + formatMoney(totalPrice));

           $(this).attr('selectit', selected).attr('src', path.join('/'));
        });

        $('.createOrderButton').click(function () {
            var ois = '?';
            var selectedItems = $('.cartProductItemIfSelected[selectit="true"]');
            for (var i = 0; i < selectedItems.length; i++){
                var oiid = $(selectedItems[i]).attr('oiid');
                ois += ("oiid=" + oiid);
                if (i < selectedItems.length - 1)
                    ois += '&';
            }
            var path = 'forebuy';
            location.href = path + ois;
        });

        $('.deleteOrderItem').click(function () {
            oiid = $(this).attr('oiid');
            layer.confirm(
                '是否从购物车删除？',
                {button:['确认', '取消']},
                function () {
                    var path = '/forecartController';
                    $.post(
                        root + path,
                        {'oiid': oiid, 'action': 'delete'},
                        function (result) {
                            if ('true' == result)
                                location.reload();
                        }
                    );
                },
                function () {
                });
        });
    });

    function updateNum(oiid, num){
        var path = '/forecartController';
        $.post(
            root + path,
            {'oiid': oiid, 'num': num, 'action': 'update'},
            function (result) {
                if('true' == result){
                    var num = 0;
                    $('.orderItemNumberSetting').each(function () {
                        num += parseInt($(this).val());
                    });
                    $('#cartNum').text(num);
                }
            }
        );
    }

    function disableButton(){
       var inputs = $('.orderItemNumberSetting');
       for (var i = 0; i < inputs.length; i++){
           $in = $(inputs[i]);
           var num = $in.val();
           if(num == $in.next().attr('stock'))
               $in.next().attr('disabled', true).attr('style', 'color:#e7e7e7');
           else if (num == 1)
               $in.prev().attr('disabled', true).attr('style', 'color:#e7e7e7');
           else {
               $in.next().attr('disabled', false).attr('style', 'color:#000000');
               $in.prev().attr('disabled', false).attr('style', 'color:#000000');
           }
       }
    }

    function changeTotal(){
        var selectItem = $('.cartProductItemIfSelected[selectit="true"]');
        var totalPrice = 0;
        var totalNum = 0;
        for (var i = 0; i < selectItem.length; i++){
            var oiid = $(selectItem[i]).attr('oiid');
            var num = parseInt($('.orderItemNumberSetting[oiid='+oiid+']').val());
            var price = parseInt($('.cartProductItemSmallSumPrice[oiid='+oiid+']').attr('price'));
            totalPrice += price;
            totalNum += num;
        }

        $('.cartSumNumber').text(totalNum);
        $('.cartSumPrice').attr('totalPrice', totalPrice).text('￥' + formatMoney(totalPrice));
        $('.cartTitlePrice').text('￥' + formatMoney(totalPrice));
    }
</script>

<title>购物车</title>
<div class="cartDiv">
    <div class="cartTitle pull-right">
        <span>已选商品  (不含运费)</span>
        <span class="cartTitlePrice">￥0.00</span>
        <button class="createOrderButton" disabled="disabled">结 算</button>
    </div>

    <div class="cartProductList">
        <table class="cartProductTable">
            <thead>
            <tr>
                <th class="selectAndImage">
                    <img selectit="false" class="selectAllItem" src="resources/img/site/cartNotSelected.png">
                    全选

                </th>
                <th>商品信息</th>
                <th>单价</th>
                <th>数量</th>
                <th width="120px">金额</th>
                <th class="operation">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${ois }" var="oi">
                <tr oiid="${oi.id}" class="cartProductItemTR">
                    <td>
                        <img selectit="false" oiid="${oi.id}" class="cartProductItemIfSelected" src="resources/img/site/cartNotSelected.png">
                        <a style="display:none" href="#nowhere"><img src="resources/img/site/cartSelected.png"></a>
                        <img class="cartProductImg"  src="resources/img/productSingle_middle/${oi.product.firstProductImage.id}.jpg">
                    </td>
                    <td>
                        <div class="cartProductLinkOutDiv">
                            <a href="foreproduct?pid=${oi.product.id}" class="cartProductLink">${oi.product.name}</a>
                            <div class="cartProductLinkInnerDiv">
                                <img src="resources/img/site/creditcard.png" title="支持信用卡支付">
                                <img src="resources/img/site/7day.png" title="消费者保障服务,承诺7天退货">
                                <img src="resources/img/site/promise.png" title="消费者保障服务,承诺如实描述">
                            </div>
                        </div>

                    </td>
                    <td>
                        <span class="cartProductItemOringalPrice">￥${oi.product.originalPrice}</span>
                        <span  class="cartProductItemPromotionPrice">￥${oi.product.promotePrice}</span>

                    </td>
                    <td>

                        <div class="cartProductChangeNumberDiv">
                            <span class="hidden orderItemStock " pid="${oi.product.id}">${oi.product.stock}</span>
                            <span class="hidden orderItemPromotePrice " pid="${oi.product.id}">${oi.product.promotePrice}</span>
                            <a pid="${oi.product.id}" class="numberMinus" href="#nowhere">-</a>
                            <input pid="${oi.product.id}" oiid="${oi.id}" class="orderItemNumberSetting" autocomplete="off" value="${oi.number}" style="padding-left:5px;margin:-1px">
                            <a stock="${oi.product.stock}" pid="${oi.product.id}" class="numberPlus" href="#nowhere">+</a>
                        </div>

                    </td>
                    <td >
                            <span class="cartProductItemSmallSumPrice" oiid="${oi.id}" pid="${oi.product.id}" price="${oi.product.promotePrice*oi.number}">
                            ￥<fmt:formatNumber type="number" value="${oi.product.promotePrice*oi.number}" minFractionDigits="2"/>
                            </span>

                    </td>
                    <td>
                        <a class="deleteOrderItem" oiid="${oi.id}"  href="#nowhere">删除</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>

    <div class="cartFoot">
        <img selectit="false" class="selectAllItem" src="resources/img/site/cartNotSelected.png">
        <span>全选</span>
        <!--         <a href="#">删除</a> -->

        <div class="pull-right">
            <span>已选商品 <span class="cartSumNumber" >0</span> 件</span>

            <span>合计 (不含运费): </span>
            <span class="cartSumPrice" totalPrice="0">￥0.00</span>
            <button class="createOrderButton" disabled="disabled" >结  算</button>
        </div>

    </div>

</div>