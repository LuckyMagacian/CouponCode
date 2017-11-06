package com.lanxi.couponcode.impl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.VerificationRecord;

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
    public List<VerificationRecord> queryVerificateRecord(Integer page,Integer size,Long merchantId,
                                                          Long code) {
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        wrapper .eq("merchant_id",merchantId)
                .like("code",code+"");
        List<VerificationRecord> list= daoService.getVerificationRecordDao().selectPage(new Page<VerificationRecord>(page,size),wrapper);
        return list;
    }

//    @Override
    public List<VerificationRecord> queryVerificateRecords(Integer page,    Integer size,           String startTime,   String endTime,
                                                           Long merchantId, Long commodityId,       String shopName,    String operaterPhone,
                                                           Long code,       String commodityName,   String merchantName,String operaterId) {
        EntityWrapper<VerificationRecord> wrapper=new EntityWrapper<>();
        wrapper .like("merchant_id",merchantId+"")
                .like("code",code+"");
        return null;
    }
}
