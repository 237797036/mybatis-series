package com.zj.chat03.demo3.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderModel {
    private Long id;
    private Long user_id;
    private Double price;
}
