package backup;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.lanxi.couponcode.impl.entity.Account;
import com.lanxi.couponcode.impl.entity.MerchantPics;

import java.sql.Blob;

/**
 * Created by yangyuanjian on 2017/10/26.
 */
public interface MerchantPicsDao {

    Blob getOrganizingInstitutionBarCodePic(Long merchantId);

    Blob getBusinessLicensePic(Long merchantId);

    Blob getotherPic(Long merchantId);

    Blob addMerchantPics(MerchantPics merchantPics);

    void updateOrganizingInstitutionBarCodePic(Long merchantId, Blob pic);

    void updateBusinessLicensePic(Long merchantId, Blob pic);

    void updateotherPic(Long merchantId, Blob pic);

    Integer getCount(Long merchantId);
}
