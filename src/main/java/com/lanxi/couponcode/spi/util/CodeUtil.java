package com.lanxi.couponcode.spi.util;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by yangyuanjian on 2017/10/30.
 */
public class CodeUtil {
    public CodeUtil() {
    }

    ;
    private static final Supplier<LocalDateTime> DATE_TIME_SUPPLIER = LocalDateTime::now;
    private static final Supplier<Instant> TIME_STAMP_SUPPLIER = Instant::now;
    private static final Supplier<Integer> DAY_NUMBER_OF_YEAR = DATE_TIME_SUPPLIER.get().toLocalDate()::getDayOfYear;

    private static final Supplier<Integer> SECOND_NUMBER_OF_YEAR = () -> {
        return null;
    };

    @Test
    public void test1() throws InterruptedException {
        System.out.println(DAY_NUMBER_OF_YEAR.get());
        System.out.println(SECOND_NUMBER_OF_YEAR.get());
    }


    @Test
    public void test3() {

        int max = 100000;
        List<Integer> list = new ArrayList();
        int now = -1;
        List<Integer> nums = IntStream.range(2, max).parallel().mapToObj(e -> Integer.valueOf(e)).collect(Collectors.toList());
        now = nums.get(0);
        for (int i = 0; ; ) {
            if (now < 2)
                now++;
            else {
                final int temp = now;
                list.add(now);
                nums = nums.parallelStream().filter(e -> e % temp != 0).collect(Collectors.toList());
                now = i < nums.size() ? nums.get(i++) : -1;
                if (-1 == now)
                    break;
            }
        }
        List<Integer> small = list.parallelStream().filter(e -> (e + "").length() < 5).collect(Collectors.toList());
        List<Integer> middle = list.parallelStream().filter(e -> (e + "").length() == 3).collect(Collectors.toList());
        List<Integer> big = list.parallelStream().filter(e -> (e + "").length() == 5).collect(Collectors.toList());
        System.out.println(small);
        System.out.println(middle);
        System.out.println(big);
    }
}
