package com.lanxi.couponcode.spi.defaultInterfaces;

import com.lanxi.util.interfaces.FromBean;
import com.lanxi.util.interfaces.FromMap;
import com.lanxi.util.interfaces.ToMap;

public interface CommonDefaultMethodOfEntity extends GetFieldValue, SetFieldValue, ToMap, FromMap, ToJson, FromBean, ReEquals {

}
