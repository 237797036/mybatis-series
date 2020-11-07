package com.zj.chat05.demo7.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private Integer id;
    private Integer userId;
    private Long createTime;
    private Long upTime;

    private UserModel userModel;

    private List<OrderDetailModel> orderDetailModelList;
}
