package com.zj.chat05.demo1.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailModel {
    private Integer id;
    private Integer orderId;
    private Integer goodsId;
    private Integer num;
    private Double totalPrice;
}
