package com.lanxi.couponcode.impl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.config.ConstConfig;
import com.lanxi.couponcode.impl.entity.OperateRecord;
import com.lanxi.couponcode.impl.entity.VerificationRecord;
import com.lanxi.couponcode.spi.consts.enums.OperateTargetType;
import com.lanxi.util.entity.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 串码操作记录服务实现类<br>
 * Created by yangyuanjian on 2017/11/3.<br>
 */
@Service("codeOperateRecordService")
public class CodeOperateRecordServiceImpl implements CodeOperateRecordService{
    @Resource
    private DaoService daoService;
    public List<OperateRecord> queryCodeOperateRecord(Integer page,    Integer size,           String startTime,   String endTime,
                                                      Long merchantId, Long commodityId,       String shopName,    String operaterPhone,
                                                      Long code,       String commodityName,   String merchantName,String operaterId,
                                                      Long recordId,boolean isManager){
        String locker="page["+page+"], " +
                "size["+size+"], " +
                "startTime["+startTime+"], " +
                "endTime["+endTime+"]," +
                "merchantId["+page+"]," +
                "commodityId["+page+"]," +
                "shopName["+page+"]," +
                "operaterPhone["+page+"]," +
                "code["+page+"]," +
                "commodityName["+page+"]," +
                "merchantName["+page+"]," +
                "operaterId["+page+"]," +
                "recordId["+page+"]," +
                "isManager["+page+"],";
        List<OperateRecord> list=null;
        try {
            Page<OperateRecord> pageObj=null;
            EntityWrapper<OperateRecord> wrapper=new EntityWrapper<>();
            if(page!=null){
                //配置分页信息
                size=size==null? ConstConfig.DEFAULT_PAGE_SIZE:size;
                pageObj=new Page<>(page,size);
            }
            //组装查询条件
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
            //因为是串码操作所以操作目标类型固定为code
            wrapper.eq("target_type", OperateTargetType.code);
            //查询数据
            list=daoService.getOperateRecordDao().selectPage(pageObj,wrapper);
            //记录日志
            LogFactory.info(this,"条件装饰结果wrapper["+wrapper+"]"+locker);
            LogFactory.debug(this,"查询结果list["+list+"]"+locker);
            LogFactory.info(this,"查询到的记录数list.size["+list.size()+"]"+locker);
            return list;
        } catch (Exception e) {
            LogFactory.error(this,"查询串码核销记录列表时发生异常!",e);
            return list;
        }


    }
}
