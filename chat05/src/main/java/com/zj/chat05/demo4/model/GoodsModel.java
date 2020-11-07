package com.zj.chat05.demo4.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsModel {
    private Integer id;
    private String name;
    private Double price;
}
