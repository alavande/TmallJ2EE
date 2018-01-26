<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<script>
    $(function () {

        $('textarea, input').focus(function () {
           $(this).siblings('span').text('');
        });


    });

    function checkText() {
        var result = true;
        var error = 'color:red;font-size:10px;padding-left:5px';
        var address = $('textarea[name="address"]').val();
        if (address.length == 0) {
            $('span[name="address"]').attr('style', error).text("收件地址不能为空");
            result = false;
        }
        var post = $('input[name="post"]').val();
        if (post.length == 0) {
            $('span[name="post"]').attr('style', error).text("请输入邮编");
            result = false;
        }
        var receiver = $('input[name="receiver"]').val();
        if (receiver.length == 0) {
            $('span[name="receiver"]').attr('style', error).text("收件人不能为空");
            result = false;
        }
        var mobile = $('input[name="mobile"]').val();
        if (mobile.length == 0) {
            $('span[name="mobile"]').attr('style', error).text("手机号不能为空");
            result = false;
        }

        return result;
    }
</script>

<div class="buyPageDiv">
    <form action="forecreateOrder" method="post" onsubmit="return checkText()">

        <div class="buyFlow">
            <img class="pull-left" src="resources/img/site/simpleLogo.png">
            <img class="pull-right" src="resources/img/site/buyflow.png">
            <div style="clear:both"></div>
        </div>
        <div class="address">
            <div class="addressTip">输入收货地址</div>
            <div>

                <table class="addressTable">
                    <tr>
                        <td class="firstColumn">详细地址<span class="redStar">*</span></td>

                        <td>
                            <textarea name="address" placeholder="建议您如实填写详细收货地址，例如接到名称，门牌好吗，楼层和房间号等信息"></textarea>
                            <span name="address"></span>
                        </td>
                    </tr>
                    <tr>
                        <td>邮政编码</td>
                        <td><input  name="post" placeholder="如果您不清楚邮递区号，请填写000000" type="text"><span name="post"></span></td>
                    </tr>
                    <tr>
                        <td>收货人姓名<span class="redStar">*</span></td>
                        <td><input  name="receiver"  placeholder="长度不超过25个字符" type="text"><span name="receiver"></span></td>
                    </tr>
                    <tr>
                        <td>手机号码 <span class="redStar">*</span></td>
                        <td><input name="mobile"  placeholder="请输入11位手机号码" type="text"><span name="mobile"></span></td>
                    </tr>
                </table>

            </div>

        </div>
        <div class="productList">
            <div class="productListTip">确认订单信息</div>

            <table class="productListTable">
                <thead>
                <tr>
                    <th colspan="2" class="productListTableFirstColumn">
                        <img class="tmallbuy" src="resources/img/site/tmallbuy.png">
                        <a class="marketLink" href="#nowhere">店铺：天猫店铺</a>
                        <a class="wangwanglink" href="#nowhere"> <span class="wangwangGif"></span> </a>
                    </th>
                    <th>单价</th>
                    <th>数量</th>
                    <th>小计</th>
                    <th>配送方式</th>
                </tr>
                <tr class="rowborder">
                    <td  colspan="2" ></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                </thead>
                <tbody class="productListTableTbody">
                <c:forEach items="${ois}" var="oi" varStatus="st" >
                    <tr class="orderItemTR">
                        <td class="orderItemFirstTD"><img class="orderItemImg" src="resources/img/productSingle_middle/${oi.product.firstProductImage.id}.jpg"></td>
                        <td class="orderItemProductInfo">
                            <a  href="foreproduct?pid=${oi.product.id}" class="orderItemProductLink">
                                    ${oi.product.name}
                            </a>

                            <img src="resources/img/site/creditcard.png" title="支持信用卡支付">
                            <img src="resources/img/site/7day.png" title="消费者保障服务,承诺7天退货">
                            <img src="resources/img/site/promise.png" title="消费者保障服务,承诺如实描述">

                        </td>
                        <td>

                            <span class="orderItemProductPrice">￥<fmt:formatNumber type="number" value="${oi.product.promotePrice}" minFractionDigits="2"/></span>
                        </td>
                        <td>
                            <span class="orderItemProductNumber">${oi.number}</span>
                        </td>
                        <td><span class="orderItemUnitSum">
                        ￥<fmt:formatNumber type="number" value="${oi.number*oi.product.promotePrice}" minFractionDigits="2"/>
                        </span></td>
                        <c:if test="${st.count==1}">
                            <td rowspan="5"  class="orderItemLastTD">
                                <label class="orderItemDeliveryLabel">
                                    <input type="radio" value="" checked="checked">
                                    普通配送
                                </label>

                                <select class="orderItemDeliverySelect" class="form-control">
                                    <option>快递 免邮费</option>
                                </select>

                            </td>
                        </c:if>

                    </tr>
                </c:forEach>

                </tbody>

            </table>
            <div class="orderItemSumDiv">
                <div class="pull-left">
                    <span class="leaveMessageText">给卖家留言:</span>
                    <span>
                    <img class="leaveMessageImg" src="resources/img/site/leaveMessage.png">
                </span>
                    <span class="leaveMessageTextareaSpan">
                    <textarea name="userMessage" class="leaveMessageTextarea"></textarea>
                    <div>
                        <span>还可以输入200个字符</span>
                    </div>
                </span>
                </div>

                <span class="pull-right">店铺合计(含运费): ￥<fmt:formatNumber type="number" value="${total}" minFractionDigits="2"/></span>
            </div>

        </div>

        <div class="orderItemTotalSumDiv">
            <div class="pull-right">
                <span>实付款：</span>
                <span class="orderItemTotalSumSpan">￥<fmt:formatNumber type="number" value="${total}" minFractionDigits="2"/></span>
            </div>
        </div>

        <div class="submitOrderDiv">
            <button type="submit" class="submitOrderButton">提交订单</button>
        </div>
    </form>
</div>