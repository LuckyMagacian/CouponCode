package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Request;

import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
public interface RequestService {
    Boolean addRequest(Request request);
    Boolean passRequest(Request request);
    Boolean rejectRequest(Request request);
    Request queryRequestInfo(Long requestId);
    List<Request> queryRequestInfos(Wrapper<Request> wrapper, Page<Request> page);
}
