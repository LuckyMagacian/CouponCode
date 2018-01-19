package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.MerchantPics;
import com.lanxi.util.entity.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;

/**
 * @author yangyuanjian
 * @Date: Created in 1/18/2018 8:35 PM
 * @Modifer:
 */
@Service("merchantPicsService")
public class MerchantPicsServiceImpl implements MerchantPicsService{
    @Resource
    private SqlSessionFactory ssf;

    @Override public boolean testExist(Long merchantId) {
        if(merchantId==null)
            throw new NullPointerException("merchantId is null !");
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="select count(1) from merchant_pics where merchant_id = "+merchantId;
            PreparedStatement statement=conn.prepareStatement(sql);
            ResultSet resultSet=statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1)>0;
        }catch(SQLException e){
            LogFactory.error(this,"校验商户["+merchantId+"]图片数据是否存在时发生异常",e);
            return false;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public Blob getOrganizingInstitutionBarCodePic(Long merchantId) {
        if(merchantId==null)
            throw new NullPointerException("merchantId is null !");
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="select organizing_institution_bar_code_pic from merchant_pics where merchant_id = "+merchantId;
            ResultSet resultSet=conn.prepareStatement(sql).executeQuery();
            if(resultSet.next()){
                return resultSet.getBlob(1);
            }else{
                return null;
            }
        }catch(SQLException e){
            LogFactory.error(this,"获取商户["+merchantId+"]组织机构代码证图片时发生异常",e);
            return null;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public Blob getBusinessLicensePic(Long merchantId) {
        if(merchantId==null)
            throw new NullPointerException("merchantId is null !");
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="select business_license_pic from merchant_pics where merchant_id = "+merchantId;
            ResultSet resultSet=conn.prepareStatement(sql).executeQuery();
            if(resultSet.next()){
                return resultSet.getBlob(1);
            }else{
                return null;
            }
        }catch(SQLException e){
            LogFactory.error(this,"获取商户["+merchantId+"]工商营业执照证明图片时发生异常",e);
            return null;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public Blob getotherPic(Long merchantId) {
        if(merchantId==null)
            throw new NullPointerException("merchantId is null !");
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="select other_pic from merchant_pics where merchant_id = "+merchantId;
            ResultSet resultSet=conn.prepareStatement(sql).executeQuery();
            if(resultSet.next()){
                return resultSet.getBlob(1);
            }else{
                return null;
            }
        }catch(SQLException e){
            LogFactory.error(this,"获取商户["+merchantId+"]其他证明图片时发生异常",e);
            return null;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public boolean addMerchantPics(MerchantPics merchantPics) {
        if(merchantPics==null|merchantPics.getMerchantId()==null)
            return false;
        if(testExist(merchantPics.getMerchantId()))
            return false;
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="insert into merchant_pics (merchant_id,organizing_institution_bar_code_pic,business_license_pic,other_pic)values(?,?,?,?)";
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setLong(1,merchantPics.getMerchantId());
            preparedStatement.setBlob(2,merchantPics.getOrganizingInstitutionBarCodePic());
            preparedStatement.setBlob(3,merchantPics.getBusinessLicensePic());
            preparedStatement.setBlob(4,merchantPics.getOtherPic());
            preparedStatement.executeUpdate();
            return true;

        }catch(SQLException e){
            LogFactory.error(this,"获取商户["+merchantPics==null?null:merchantPics.getMerchantId()+"]其他证明图片时发生异常",e);
            return false;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public boolean updateOrganizingInstitutionBarCodePic(Long merchantId, Blob pic) {
        if(merchantId==null)
            throw new NullPointerException("merchantId is null !");
        if(!testExist(merchantId))
            return false;
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="update merchant_pics set organizing_institution_bar_code_pic = ? where merchant_id="+merchantId ;
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setBlob(1,pic);
            return preparedStatement.executeUpdate()>0;
        }catch(SQLException e){
            LogFactory.error(this,"更新商户["+merchantId+"]组织机构代码证明图片时发生异常",e);
            return false;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public boolean updateBusinessLicensePic(Long merchantId, Blob pic) {
        if(merchantId==null)
            throw new NullPointerException("merchantId is null !");
        if(!testExist(merchantId))
            return false;
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="update merchant_pics set business_license_pic = ? where merchant_id="+merchantId ;
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setBlob(1,pic);
            return preparedStatement.executeUpdate()>0;
        }catch(SQLException e){
            LogFactory.error(this,"更新商户["+merchantId+"]营业执照证明图片时发生异常",e);
            return false;
        }finally{
            closeConnection(conn);
        }
    }

    @Override public boolean updateotherPic(Long merchantId, Blob pic) {
        if(merchantId==null)
            if(merchantId==null)
                throw new NullPointerException("merchantId is null !");
        if(!testExist(merchantId))
            return false;
        Connection conn=null;
        try{
            conn=ssf.openSession().getConnection();
            String sql="update merchant_pics set other_pic = ? where merchant_id="+merchantId ;
            PreparedStatement preparedStatement=conn.prepareStatement(sql);
            preparedStatement.setBlob(1,pic);
            boolean result= preparedStatement.executeUpdate()>0;
            return result;
        }catch(SQLException e){
            LogFactory.error(this,"更新商户["+merchantId+"]其他证明图片时发生异常",e);
            return false;
        }finally{
            closeConnection(conn);
        }
    }


    private void closeConnection(Connection conn){
        if(conn!=null){
            try{
                conn.close();
            }catch(SQLException e){
                LogFactory.error(this,"尝试关闭数据库连接时发生异常",e);
            }
        }
    }

//    private <T> T query(String sql,Object... args) throws SQLException {
//        Connection conn=null;
//        try{
//            conn=ssf.openSession().getConnection();
//            ResultSet resultSet=conn.prepareStatement(sql).executeQuery();
//            if(resultSet.next()){
//                return (T)resultSet.getBlob(1);
//            }else{
//                return null;
//            }
//        }finally{
//            closeConnection(conn);
//        }
//    }
}
