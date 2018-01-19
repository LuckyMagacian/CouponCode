package com.lanxi.couponcode.impl.newservice;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.lanxi.couponcode.impl.entity.Shop;

import java.io.File;
import java.util.List;

/**
 * 门店操作类
 * 冻结所有门店
 * 修改门店状态
 * 根据所属商户id查询门店
 * 单个添加门店
 *
 * @author wuxiaobo
 */
 public interface ShopService {
    //	/*根据所属商户id查询门店*/
//	 List<Shop> queryPossessShopByMerchantId(Integer page,Integer size,Long merchantId,Long operaterId,
//	String operaterInfo,String operaterPhone);
    /*冻结所有门店*/
     Boolean freezeAllShop(Long merchantId,
                                 Long operaterId,
                                 String operaterInfo,
                                 String operaterPhone);

    /*单个添加门店*/
     Boolean addShop(Shop shop);

    /*导入门店*/
     List<String> importShops(File file,
                                    Long merchantId,
                                    Long operaterId,
                                    String username,
                                    String merchantName,
                                    String merchantStatus);

    /*冻结门店*/
     Boolean freezeShop(Shop shop);

    /*开启门店*/
     Boolean unfreezeShop(Shop shop);

    /*查询门店分页*/
     List<Shop> queryShop(EntityWrapper<Shop> wrapper,
                                Page<Shop> pageObj
    );

    /*查询门店*/
     List<Shop> queryShop(EntityWrapper<Shop> wrapper);

    /*修改门店*/
     Boolean modifyShop(Shop shop);

    /*导出*/
     File queryShopsExport(EntityWrapper<Shop> wrapper,
                                 Page<Shop> pageObj);

    /*通过shopId查询门店详情*/
     Shop queryShopInfo(Long shopId);

    /*下载批量导入门店模板*/
     File downloadExcelTemplate();

    /*通过merchantId查询商户下属所有门店*/
     List<Shop> queryShops(Long merchantId, Page<Shop> pageObj);

    /**
     * @param shopName
     * @param merchantId
     * @return true可用
     * false不可用
     */
     Boolean isRepeat(String shopName, Long merchantId);

    /**
     * @param shopName
     * @param shopId
     * @param merchantId
     * @return true可用
     * false不可用
     */
     Boolean isRepeat(Long shopId, String shopName, Long merchantId);

     List<Shop> adminQueryShop(EntityWrapper<Shop> wrapper,
                                     Page<Shop> pageObj);

     List<Shop> queryAllShop(Long merchantId);
}
