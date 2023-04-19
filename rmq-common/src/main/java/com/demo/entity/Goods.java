package com.demo.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/18
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {
    @Serial
    private static final long serialVersionUID = 4497255466995223370L;
    private Long goodsId;
    private Long count;
}
