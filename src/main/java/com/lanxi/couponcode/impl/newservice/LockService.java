package com.lanxi.couponcode.impl.newservice;

import java.util.List;

/**
 * Created by yangyuanjian on 11/28/2017.
 */
public interface LockService {
    Boolean lock(Object obj);
    List<Boolean> lock(List list);
    Boolean unlock(Object obj);
    List<Boolean> unlock(List list);
}
