package com.demo.mock;

import com.demo.entity.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/4/19
 */
@Component
@Slf4j
public class GoodsDB {

    private static final Map<Long, Long> goods = new HashMap<>();

    static {
        goods.put(1L, 10L);
        goods.put(2L, 10L);
        goods.put(3L, 10L);
        goods.put(4L, 10L);
        goods.put(5L, 10L);
    }

    public static List<Goods> getAllGoods() {
        return goods.entrySet().stream().map(entry -> new Goods(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static long getStore(Long goodsId) {
        return goods.get(goodsId);
    }

    public static void addGoods(Long gId, Long count) {
        goods.put(gId, count);
    }

    public static boolean reduce(Long gId, Long count) {
        Long amount = goods.get(gId);
        if (amount - count >= 0) {
            long left = amount - count;
            goods.put(gId, left);
            return true;
        }
        return false;
    }

}
