<%@page pageEncoding="UTF-8" isELIgnored="false" import="java.util.*"
        contentType="text/html;charset=UTF-8" language="java" %>

<script>
    $(function(){
        var stock = ${p.stock};
        $('.productNumberSetting').keyup(function () {
            var num = $(this).val();
            if (num.length > 0) {
                num = parseInt(num);
                if (isNaN(num) || num < 1)
                    num = 1;
                else if (num > stock)
                    num = stock;
                $(this).val(num);
            }
        });

       $('.increaseNumber').click(function () {
           var num = $('.productNumberSetting').val();
           if (num < stock && num >= 0)
               num++;
           else if (num < 0)
               num = 1
           else
               num = stock;
           $('.productNumberSetting').val(num);
       });

       $('.decreaseNumber').click(function () {
           var num = $('.productNumberSetting').val();
           if (num > 1 && num <= stock)
               num--;
           else if (num > stock)
               num = stock;
           else
               num = 1;
           $('.productNumberSetting').val(num);
       });

       $('.smallImage').mouseenter(function () {
            var url = $(this).attr("bigImageURL");
            $('.bigImg').attr('src', url) ;
       });

       $('.addCartLink').click(function () {
           var page = 'forecheckLogin';
           $.get(
               page,
               function (result){
                   if ('true' == result){
                       var num = $('.productNumberSetting').val();
                       var pid = ${p.id};
                       var path = 'foreaddCart';
                       $.get(
                           path,
                           {
                             "pid":pid,
                             "num":num
                           },
                           function (result) {
                                if ('true' == result){
                                    $('.addCartButton').html('已加入购物车')
                                        .attr('disabled', 'disabled')
                                        .css('background-color', 'lightgray')
                                        .css('border-color', 'lightgray')
                                        .css('color', 'black');
                                    $('#cartNum').html(parseInt($('#cartNum').html()) + parseInt(num));
                                } else {

                                }
                       });
                   } else {
                       $('#loginModal').modal('show');
                   }
               }
           );
       });

       $('.buyLink').click(function () {
           var page = "forecheckLogin";
           $.get(
               page,
               function (result) {
                   if ('true' == result) {
                       var num = $('.productNumberSetting').val();
                       location.href = $('.buyLink').attr('href') + '&num=' + num;
                   } else {
                       $('#loginModal').modal('show');
                   }
           });
       });

       $('.loginSubmitButton').click(function () {
           var name = $('#name').val();
           var password = $('#password').val();
           if (name.length == 0 || password.length == 0){
               $('.errorMessage').html("请输入账号密码");
               $('.loginErrorMessageDiv').show();

           } else {
               var page = 'foreloginModal';
               $.get(
                   page,
                   {
                       "name": name,
                       "password": password
                   },
                   function (result) {
                         if ('true' == result){
                             location.reload();
                         } else {
                             $('.errorMessage').html("账号密码错误");
                             $('.loginErrorMessageDiv').show();
                         }
               });
           }
       });
    });
</script>

<div class="imgAndInfo">

    <div class="imgInimgAndInfo">
        <img src="resources/img/productSingle/${p.firstProductImage.id}.jpg" class="bigImg">
        <div class="smallImageDiv">
            <c:forEach items="${p.productSingleImages}" var="pi">
                <img src="resources/img/productSingle_small/${pi.id}.jpg" bigImageURL="resources/img/productSingle/${pi.id}.jpg" class="smallImage">
            </c:forEach>
        </div>
        <div class="img4load hidden" ></div>
    </div>

    <div class="infoInimgAndInfo">

        <div class="productTitle">
            ${p.name}
        </div>
        <div class="productSubTitle">
            ${p.subTitle}
        </div>

        <div class="productPrice">
            <div class="juhuasuan">
                <span class="juhuasuanBig" >聚划算</span>
                <span>此商品即将参加聚划算，<span class="juhuasuanTime">1天19小时</span>后开始，</span>
            </div>
            <div class="productPriceDiv">
                <div class="gouwujuanDiv"><img height="16px" src="resources/img/site/gouwujuan.png">
                    <span> 全天猫实物商品通用</span>

                </div>
                <div class="originalDiv">
                    <span class="originalPriceDesc">价格</span>
                    <span class="originalPriceYuan">¥</span>
                    <span class="originalPrice">

                        <fmt:formatNumber type="number" value="${p.originalPrice}" minFractionDigits="2"/>
                    </span>
                </div>
                <div class="promotionDiv">
                    <span class="promotionPriceDesc">促销价 </span>
                    <span class="promotionPriceYuan">¥</span>
                    <span class="promotionPrice">
                        <fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/>
                    </span>
                </div>
            </div>
        </div>
        <div class="productSaleAndReviewNumber">
            <div>销量 <span class="redColor boldWord"> ${p.saleCount}</span></div>
            <div>累计评价 <span class="redColor boldWord"> ${p.reviewCount}</span></div>
        </div>
        <div class="productNumber">
            <span>数量</span>
            <span>
                <span class="productNumberSettingSpan">
                    <input class="productNumberSetting" type="text" value="1" style="outline:none;padding-left:5px">
                </span>
                <span class="arrow">
                    <a href="#nowhere" class="increaseNumber">
                    <span class="updown">
                            <img src="resources/img/site/increase.png">
                    </span>
                    </a>

                    <span class="updownMiddle"> </span>
                    <a href="#nowhere"  class="decreaseNumber">
                    <span class="updown">
                            <img src="resources/img/site/decrease.png">
                    </span>
                    </a>

                </span>

            件</span>
            <span>库存${p.stock}件</span>
        </div>
        <div class="serviceCommitment">
            <span class="serviceCommitmentDesc">服务承诺</span>
            <span class="serviceCommitmentLink">
                <a href="#nowhere">正品保证</a>
                <a href="#nowhere">极速退款</a>
                <a href="#nowhere">赠运费险</a>
                <a href="#nowhere">七天无理由退换</a>
            </span>
        </div>

        <div class="buyDiv">
            <a class="buyLink" href="forebuyone?pid=${p.id}"><button class="buyButton">立即购买</button></a>
            <a href="#nowhere" class="addCartLink"><button class="addCartButton"><span class="glyphicon glyphicon-shopping-cart"></span>加入购物车</button></a>
        </div>
    </div>

    <div style="clear:both"></div>

</div>