package com.lanxi.couponcode.impl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.spi.consts.enums.CouponCodeStatus;
import com.lanxi.couponcode.spi.consts.enums.VerificationType;
import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.TimeUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * 串码核销记录服务接口实现类<br>
 * Created by yangyuanjian on 2017/11/3.<br>
 */
public class VerificateRecordServiceImpl implements VerificateRecordService{
    @Resource
    private DaoService daoService;
    @Resource
    private CouponCodeService codeService;
//    @Override
    public List<VerificationRecord> queryVerificateRecord(Integer page,Integer size,Long merchantId, Long code) {
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        Page<VerificationRecord> pageObj=null;
        if(page!=null){
            size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
            pageObj=new Page<>(page,size);
        }
        if(merchantId!=null)
            wrapper .eq("merchant_id",merchantId);
        if(code!=null)
            wrapper.like("code",code+"");
        List<VerificationRecord> list= daoService.getVerificationRecordDao().selectPage(new Page<VerificationRecord>(page,size),wrapper);
        return list;
    }

    @Override
    public List<VerificationRecord> queryVerificateRecords(Integer page,    Integer size,           String startTime,   String endTime,
                                                           Long merchantId, Long commodityId,       String shopName,    String operaterPhone,
                                                           Long code,       String commodityName,   String merchantName,String operaterId,
                                                           Long recordId,boolean isManager) {
        Page<VerificationRecord> pageObj=null;
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        if(page!=null){
            size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
            pageObj=new Page<>(page,size);
        }
        if(startTime!=null&&!startTime.isEmpty()){
            while(startTime.length()<14)
                startTime+="0";
            wrapper.ge("create_time",startTime);
        }
        if(endTime!=null&&!endTime.isEmpty()){
            while(endTime.length()<14)
                endTime+="9";
            wrapper.le("create_time",endTime);
        }
        //若是管理员操作,对merchantId进行模糊查询
        if(isManager&&merchantId!=null)
            wrapper .like("merchant_id",merchantId+"");
        if(!isManager&&merchantId!=null)
            wrapper.eq("merchant_id",merchantId);

        if(commodityId!=null)
            wrapper.like("commodity_id",commodityId+"");
        if(shopName!=null)
            wrapper.like("commodity_name",commodityName);
        if(operaterPhone!=null)
            wrapper.like("operater_phone",operaterPhone);
        if(code!=null)
            wrapper.like("code",code+"");
        if(merchantName!=null&&!merchantName.isEmpty())
            wrapper.like("merchant_name",merchantName);
        if(commodityName!=null&&!commodityName.isEmpty())
            wrapper.like("commodity_name",commodityName);
        if(operaterId!=null)
            wrapper.eq("operater_id",operaterId);
        if(recordId!=null)
            wrapper.eq("record_id",recordId+"");
        List<VerificationRecord> list=daoService.getVerificationRecordDao().selectPage(pageObj,wrapper);
        return list;
    }
}
