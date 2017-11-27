package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.CodeAlgorithm;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.util.utils.TimeUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yangyuanjian on 2017/11/15.
 */
@Deprecated
public class CodeServiceImplOld implements CodeServiceOld {
    @Resource
    private RedisService redis;
    @Resource
    private RedisEnhancedService enhancedRedis;
    @Resource
    private ConfigService config;
    @Resource
    private DaoService daoService;
    @Resource
    private CodeAlgorithmService algorithmService;
    @Resource
    private RedisCodeService redisCodeService;
    /**
     * 串码长度
     */
    private static final int CODE_LENGTH=10;
    /**
     * 根据商户编号获取对应的code
     * @param merchantId 商户编号
     * @return code
     */
    private Long generateCode(Long merchantId){
        CodeAlgorithm codeAlgorithm=algorithmService.getCodeAlgorithm(merchantId);
        Long var=redisCodeService.getCodeVar(merchantId);
        long code=codeAlgorithm.getCode(var);
        if(CODE_LENGTH!=(code+"").length()) {
            return generateCode(merchantId);
        }
        if(var!=null){
            if(redisCodeService.checkCodeExists(merchantId,code)){
                return generateCode(merchantId);
            }
        }
        if(new CouponCode().isExisted(merchantId,code)){
            return generateCode(merchantId);
        }
           return code;

    }
    @Override
    public Boolean checkCodeExists(Long codeId){
        CouponCode code=queryCode(codeId);
        if(code==null){
            redisCodeService.delCode(code);
            return false;
        }else{
            return true;
        }
    }
    @Override
    public Boolean checkCodeExists(Long merchantId,Long code){
        Boolean redisEX = redisCodeService.checkCodeExists(merchantId,code);
        Boolean dbEx=new CouponCode().isExisted(merchantId,code);
        if(redisEX==null)
            return dbEx;
        if(dbEx==null)
            return null;

        if(redisEX&&!dbEx){
            redisCodeService.delCode(merchantId,code);
            return dbEx;
        }
        if(dbEx&&!redisEX) {
            redisCodeService.addCode(merchantId, code);
            return dbEx;
        }
        return null;
    }
    @Override
    public CouponCode generateCode(CouponCode code){
        if(code.getMerchantId()==null)
            return null;
        if(code.getCode()!=null)
            return code;
        synchronized (code) {
            code.setCode(generateCode(code.getMerchantId()));
            code.insertOrUpdate();
            return code;
        }
    }

//    public Boolean updateCode(CouponCode code){
//        synchronized (code){
//            Boolean result=code.updateById();
//            return result;
//        }
//    }
    @Override
    public Boolean delCode(CouponCode code){
        synchronized (code){
            Boolean redisDel=redisCodeService.delCode(code);
            code.setCodeStatus(CouponCodeStatus.cancellation);
            return code.updateById();
        }
    }
    @Override
    public Boolean verificateCode(CouponCode code){
        synchronized (code){
            Boolean redisDel=redisCodeService.delCode(code);
            code.setCodeStatus(CouponCodeStatus.destroyed);
            return code.updateById();
        }
    }
    @Override
    public Boolean overTimeCode(CouponCode code){
        Boolean redisDel=redisCodeService.delCode(code);
        code.setCodeStatus(CouponCodeStatus.overtime);
        return code.updateById();
    }
    @Override
    public Boolean postoneCode(CouponCode code){
        synchronized (code){
            code.setLifeTime(code.getLifeTime()*2);
            Date date= TimeUtil.parseDateTime(code.getOverTime());
            date.setDate(date.getDay()+code.getLifeTime());
            code.setOverTime(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
            return code.updateById();
        }
    }
    @Override
    public Optional<CouponCode> queryCode(Long merchantId , Long code){
        EntityWrapper<CouponCode> wrapper=new EntityWrapper<>();
        wrapper .eq("merchant_id",merchantId)
                .eq("code",code);
        List<CouponCode> list=queryCodes(wrapper,null);
        if(list==null||list.isEmpty())
            return Optional.empty();
        else if(list.size()>1)
            return null;
        else
            return Optional.of(list.get(0));

    }
    @Override
    public CouponCode queryCode(Long codeId){
        return daoService.getCouponCodeDao().selectById(codeId);
    }
    @Override
    public List<CouponCode> queryCodes(Wrapper<CouponCode> wrapper,Page<CouponCode> page){
        if(page==null)
            return daoService.getCouponCodeDao().selectList(wrapper);
        else
            return daoService.getCouponCodeDao().selectPage(page,wrapper);
    }

}
