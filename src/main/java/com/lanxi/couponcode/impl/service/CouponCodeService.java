package com.lanxi.couponcode.impl.service;

import com.lanxi.couponcode.impl.entity.CouponCode;

import java.util.List;
import java.util.Optional;

/**
 * 串码服务接口<br>
 * Created by yangyuanjian on 2017/10/31.<br>
 */
public interface CouponCodeService {
    /**
     * 生产串码<br>
     * @param merchantId 商户id<br>
     * @param commodityId   商品id<br>
     * @param merchantName 商户名称<br>
     * @param commodityName 商品名称<br>
     * @param commodityInfo 商品信息<br>
     * @param lifeTime 有效期<br>
     * @return 生产结果 true成功|false失败<br>
     */
    public boolean getnerateCode(Long merchantId,       Long commodityId,
                                 String merchantName,   String commodityName,
                                 String commodityInfo,  Integer lifeTime);

    /**
     * 核销串码<br>
     * @param merchantId 商户id<br>
     * @param shopId    门店id<br>
     * @param commodityId 商品id<br>
     * @param operaterId 核销人员id<br>
     * @param merchantName 商户名称<br>
     * @param shopName 门店名称<br>
     * @param commodityName 商品名称<br>
     * @param operaterPhone 核销人员手机号码<br>
     * @param verificationType 核销方式<br>
     * @param code 串码<br>
     * @param shopInfo 门店信息<br>
     * @return 核销结果 true成功|false失败<br>
     */
    public boolean destroyCode(Long merchantId,         Long shopId,        Long commodityId,       Long operaterId,
                               String merchantName,     String shopName,    String commodityName,   String operaterPhone,
                               String verificationType, Long code,          String shopInfo
    );

//    /**
//     * 查询商品id<br>
//     * @param merchantId 商户id<br>
//     * @param code 串码<br>
//     * @return 商品id<br>
//     */
//    public String quryCommodityId(Long merchantId, Long code);
//
//    /**
//     * 查询商品信息<br>
//     * @param merchantId 商户id<br>
//     * @param code 串码<br>
//     * @return 商品信息<br>
//     */
//    public String queryCommodityInfo(Long merchantId,Long code);

    /**
     * 串码过期<br>
     * @param merchantId 商户id<br>
     * @param code 串码<br>
     * @return 过期结果 true成功|false失败<br>
     */
    public boolean overtimeCode(Long merchantId,Long code);

    /**
     * 串码延期<br>
     * @param merchantId 商户id<br>
     * @param code 串码<br>
     * @param operaterId 操作人员id<br>
     * @param operaterInfo 操作人员信息<br>
     * @return 延期结果 true成功|false失败<br>
     */
    public boolean postponeCode(Long merchantId,Long code,Long operaterId,String operaterInfo,String operaterPhone);

    /**
     * 销毁串码<br>
     * @param merchantId 商户id<br>
     * @param code 串码<br>
     * @param operaterId 操作人员id<br>
     * @param operaterInfo 操作人员信息<br>
     * @return 销毁结果 true 成功|false 失败<br>
     */
    public boolean cancelCode(Long merchantId,Long code,Long operaterId,String operaterInfo,String operaterPhone);

    /**
     * 查询单个串码
     * @param merchantId 商户id
     * @param code 串码
     * @return 串码查询结果,可能为Optional.empty();
     */
    public Optional<CouponCode> queryCouponCode(Long merchantId, Long code);

    /**
     * 根据条件查询串码
     * @param createTimeStart
     * @param createTimeEnd
     * @param merchantName
     * @param commodityName
     * @param code
     * @param codeId
     * @param codeStatus
     * @param commodityId
     * @param merchantId
     * @param overTimeStart
     * @param overTimeEnd
     * @return
     */
    public List<CouponCode> queryCouponCode(String createTimeStart, String createTimeEnd, String merchantName,
                                            String commodityName, Long code, Long codeId,
                                            String codeStatus   , Long commodityId, Long merchantId,
                                            String overTimeStart, String overTimeEnd
    );
}
