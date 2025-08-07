CREATE TABLE boot_order
(
    id         BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID',
    orderno    VARCHAR(255) NOT NULL COMMENT '订单流水号',
    status     INT(11) NOT NULL COMMENT '订单状态：1(未支付)，2(已支付)',
    createtime LONG         NOT NULL COMMENT '创建时间',
    updatetime LONG         NOT NULL COMMENT '修改时间',
    isdelete   INT          NOT NULL DEFAULT 0 COMMENT '是否删除：0(否)，1(是)',
    PRIMARY KEY (id)
) COMMENT = '订单表';

CREATE TABLE boot_pay
(
    id         BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID',
    orderid    BIGINT       NOT NULL COMMENT '订单ID',
    payno      VARCHAR(255) NOT NULL COMMENT '支付流水号',
    paymoney   INT          NOT NULL DEFAULT 0 COMMENT '支付金额：单位为分',
    createtime LONG         NOT NULL COMMENT '创建时间',
    updatetime LONG         NOT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
) COMMENT = '支付表';