package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.assist.TimeAssist;
import com.lanxi.couponcode.spi.consts.annotations.EasyLog;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.util.utils.LoggerUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lanxi.couponcode.spi.assist.CheckAssist.notNUll;

/**
 * Created by yangyuanjian on 2017/11/23.
 */
@Service ("codeService")
@EasyLog (LoggerUtil.LogLevel.INFO)
public class CodeServiceImpl implements CodeService {
    @Resource
    private DaoService daoService;

    private interface CheckJob {
        Boolean checkJob();
    }

    private Boolean doCheckJob(CheckJob job, CouponCode code) {
        if (!CouponCodeStatus.undestroyed.equals(code.getCodeStatus()))
            return null;
        if (job == null)
            throw new IllegalArgumentException("arg : job can't be null !");
        return job.checkJob();
    }


    @Override
    public Boolean addCode(CouponCode code) {
        Boolean[] temp = new Boolean[1];
        notNUll(code).ifPresent(e -> temp[0] = (e.insert()));
        return temp[0];
    }

    @Override
    public Boolean checkCodeExists(Long codeId) {
        return notNUll(daoService.getCouponCodeDao().selectCount(new EntityWrapper<CouponCode>().eq("code_id", codeId))).orElse(0) > 0;
    }

    @Override
    public Boolean checkCodeExists(Long merchantId, Long code) {
        return notNUll(daoService.getCouponCodeDao().selectCount(new EntityWrapper<CouponCode>().eq("merchant_id", merchantId).eq("code", code))).orElse(0) > 0;
    }

    @Override
    public Boolean delCode(CouponCode code) {
        CheckJob job = () -> {
            code.setCodeStatus(CouponCodeStatus.cancellation);
            return code.updateById();
        };
        return doCheckJob(job, code);
    }

    @Override
    public Boolean verificateCode(CouponCode code) {
        CheckJob job = () -> {
            code.setCodeStatus(CouponCodeStatus.destroyed);
            return code.updateById();
        };
        return doCheckJob(job, code);
    }

    @Override
    public Boolean overTimeCode(CouponCode code) {
        CheckJob job = () -> {
            code.setCodeStatus(CouponCodeStatus.overtime);
            return code.updateById();
        };
        return doCheckJob(job, code);
    }

    @Override
    public Boolean postoneCode(CouponCode code) {
        CheckJob job = () -> {
            LocalDateTime overTime = TimeAssist.parseToDateTime(code.getOverTime());
            overTime = overTime.plusDays(30);
            code.setOverTime(TimeAssist.formatToyyyyMMddHHmmss(overTime));
            code.setLifeTime(code.getLifeTime()+30);
            return code.updateById();
        };
        return doCheckJob(job, code);
    }

    @Override
    public Optional<CouponCode> queryCode(Long merchantId, Long code) {
        EntityWrapper<CouponCode> wrapper = new EntityWrapper<>();
        wrapper.eq("merchant_id", merchantId)
                .eq("code", code);
        List<CouponCode> list = queryCodes(wrapper, null);
        if (list == null || list.isEmpty())
            return Optional.empty();
        else if (list.size() > 1)
            return null;
        else
            return Optional.ofNullable(list.get(0));
    }

    @Override
    public CouponCode queryCode(Long codeId) {
        return daoService.getCouponCodeDao().selectById(codeId);
    }

    @Override
    public CouponCode queryCodeInfo(Long codeId, Long merchantId) {
        EntityWrapper<CouponCode> wrapper = new EntityWrapper<>();
        wrapper.eq("code_id", codeId);
        if (merchantId != null)
            wrapper.eq("merchant_id", merchantId);
        List<CouponCode> list = queryCodes(wrapper, null);
        if (list == null || list.isEmpty())
            return null;
        else if (list.size() > 1)
            return null;
        else
            return list.get(0);
    }

    @Override
    public List<CouponCode> queryCodes(Wrapper<CouponCode> wrapper, Page<CouponCode> page) {
        if (page == null)
            return daoService.getCouponCodeDao().selectList(wrapper);
        else
            return daoService.getCouponCodeDao().selectPage(page, wrapper);
    }
}
