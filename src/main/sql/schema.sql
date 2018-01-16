-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE tmall;
-- 使用数据库
use tmall;

-- 创建用户表
CREATE TABLE user (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '登陆密码',
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户表';
-- 创建分类表
CREATE TABLE category(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `name` varchar(255) DEFAULT NULL COMMENT '类别名称',
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '商品分类表';
-- 创建商品属性表
CREATE TABLE property (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `cid` int(11) DEFAULT NULL COMMENT '类别ID',
  `name` varchar(255) DEFAULT NULL COMMENT '属性名，如颜色、重量',
  PRIMARY KEY (id),
  CONSTRAINT fk_property_category FOREIGN KEY (cid) REFERENCES category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '商品属性表，存放属性名称';
-- 创建商品表
CREATE TABLE product (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(255) DEFAULT NULL COMMENT '商品名',
  `subTitle` varchar(255) DEFAULT NULL COMMENT '小标题',
  `orignalPrice` float DEFAULT NULL COMMENT '原始价格',
  `promotePrice` float DEFAULT NULL COMMENT '优惠价格',
  `stock` int(11) DEFAULT NULL COMMENT '库存',
  `cid` int(11) DEFAULT NULL COMMENT '所属类别',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (id),
  CONSTRAINT fk_product_category FOREIGN KEY (cid) REFERENCES category (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '商品表';
-- 创建属性值表
CREATE TABLE property_value (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
  `pid` int(11) DEFAULT NULL COMMENT '商品ID',
  `ptid` int(11) DEFAULT NULL COMMENT '属性ID',
  `value` varchar(255) DEFAULT NULL COMMENT '属性值内容',
  PRIMARY KEY (id),
  CONSTRAINT fk_property_value_property FOREIGN KEY (ptid) REFERENCES property (id),
  CONSTRAINT fk_property_value_product FOREIGN KEY (pid) REFERENCES product (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '属性值表，包含具体属性内容';
-- 创建商品图片表
CREATE TABLE product_image (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `pid` int(11) DEFAULT NULL COMMENT '商品ID',
  `type` varchar(255) DEFAULT NULL COMMENT '图片类别，分为单个图片与详情图片两种',
  PRIMARY KEY (id),
  CONSTRAINT fk_product_image_product FOREIGN KEY (pid) REFERENCES product (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '商品图片表';
-- 创建评价表
CREATE TABLE review (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `content` varchar(4000) DEFAULT NULL COMMENT '评价内容',
  `uid` int(11) DEFAULT NULL COMMENT '评价用户ID',
  `pid` int(11) DEFAULT NULL COMMENT '评价商品ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (id),
  CONSTRAINT fk_review_product FOREIGN KEY (pid) REFERENCES product (id),
  CONSTRAINT fk_review_user FOREIGN KEY (uid) REFERENCES user (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '评价表，包含商品评价';
-- 创建订单表
CREATE TABLE order_ (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `orderCode` varchar(255) DEFAULT NULL COMMENT '订单号',
  `address` varchar(255) DEFAULT NULL COMMENT '收件地址',
  `post` varchar(255) DEFAULT NULL COMMENT '收件地址邮编',
  `receiver` varchar(255) DEFAULT NULL COMMENT '收货人信息',
  `mobile` varchar(255) DEFAULT NULL COMMENT '收货人手机号',
  `userMessage` varchar(255) DEFAULT NULL COMMENT '用户备注信息',
  `createDate` datetime DEFAULT NULL COMMENT '订单创建日期',
  `payDate` datetime DEFAULT NULL COMMENT '支付日期',
  `deliveryDate` datetime DEFAULT NULL COMMENT '发货日期',
  `confirmDate` datetime DEFAULT NULL COMMENT '确认收货日期',
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `status` varchar(255) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (id),
  CONSTRAINT fk_order_user FOREIGN KEY (uid) REFERENCES user (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '订单表';
-- 创建订单项表
CREATE TABLE order_item (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `pid` int(11) DEFAULT NULL COMMENT '商品ID',
  `oid` int(11) DEFAULT NULL COMMENT '订单ID，无外键约束，创建订单项时可能订单还未创建',
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `number` int(11) DEFAULT NULL COMMENT '商品数量',
  PRIMARY KEY (id),
  CONSTRAINT fk_orderitem_user FOREIGN KEY (uid) REFERENCES user (id),
  CONSTRAINT fk_orderitem_product FOREIGN KEY (pid) REFERENCES product (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '订单项表，包含订单具体内容';
