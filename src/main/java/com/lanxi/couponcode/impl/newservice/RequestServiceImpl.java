package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Request;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.RequestStatus;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yangyuanjian on 2017/11/20.
 */
@Service ("cequestService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class RequestServiceImpl implements RequestService {
    @Resource
    private DaoService daoService;

    @Override
    public Boolean addRequest(Request request) {
        return request.insert();
    }

    @Override
    public Boolean passRequest(Request request) {
        request.setStatus(RequestStatus.pass);
        return request.updateById();
    }

    @Override
    public Boolean rejectRequest(Request request) {
        request.setStatus(RequestStatus.reject);
        return null;
    }

    @Override
    public Request queryRequestInfo(Long requestId) {
        return daoService.getRequestDao().selectById(requestId);
    }

    @Override
    public List<Request> queryRequestInfos(Wrapper<Request> wrapper, Page<Request> page) {
        if (page == null)
            return daoService.getRequestDao().selectList(wrapper);
        else
            return daoService.getRequestDao().selectPage(page, wrapper);
    }
}
