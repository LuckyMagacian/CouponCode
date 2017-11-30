package com.lanxi.couponcode.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lanxi.couponcode.spi.consts.annotations.SetUtf8;
import com.lanxi.couponcode.spi.consts.enums.ShopStatus;
import com.lanxi.couponcode.spi.service.ShopService;
import com.lanxi.util.entity.LogFactory;

/**
 * 门店管理端
 * Created by yangyuanjian on 2017/11/20.
 */
@Controller
@RequestMapping("shop")
public class ShopController {
	@Resource
	private ShopService shopService;
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String addShop(HttpServletRequest req, HttpServletResponse res) {
		String shopName=req.getParameter("shopName");
		String shopAddress=req.getParameter("shopAddress");
		String minuteShopAddress=req.getParameter("minuteShopAddress");
		String serviceTel=req.getParameter("serviceTel");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return shopService.addShop(shopName, shopAddress, minuteShopAddress, serviceTel, merchantId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String importShops(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		String result=null;
		try {
			InputStream is=req.getInputStream();
			File file=new File("file");
			OutputStream os=new FileOutputStream(file);
			byte temp[]=new byte[1024];
			int size=-1;
			while((size=is.read(temp))!=-1) {
				os.write(temp, 0, size);
			}
			os.close();
			result=shopService.importShops(file, merchantId, operaterId).toJson();
		} catch (Exception e) {
			LogFactory.error(this,"批量导入门店的时候发生异常",e);
		}
		return result;
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String freezeShop(HttpServletRequest req, HttpServletResponse res) {
		String shopIdStr=req.getParameter("shopId");
		String operaterIdStr=req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long shopId = Long.valueOf(shopIdStr);
		return shopService.freezeShop(shopId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String unfreezeShop(HttpServletRequest req, HttpServletResponse res) {
		String shopIdStr=req.getParameter("shopId");
		String operaterIdStr=req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long shopId = Long.valueOf(shopIdStr);
		return shopService.unfreezeShop(shopId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String modifyShop(HttpServletRequest req, HttpServletResponse res) {
		String shopName=req.getParameter("shopName");
		String shopAddress=req.getParameter("shopAddress");
		String minuteShopAddress=req.getParameter("minuteShopAddress");
		String serviceTel=req.getParameter("serviceTel");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return shopService.modifyShop(shopName, shopAddress, minuteShopAddress, serviceTel, merchantId, operaterId).toJson();
	}
	
	@SetUtf8
	@ResponseBody
	@RequestMapping(value = "", produces = "application/json;charset=utf-8")
	public String queryShop(HttpServletRequest req, HttpServletResponse res) {
		String merchantName=req.getParameter("merchantName");
		String shopName=req.getParameter("shopName");
		String shopAddress=req.getParameter("shopAddress");
		String shopStatusStr=req.getParameter("shopStatus");
		String pageNumStr=req.getParameter("pageNum");
		String pageSizeStr=req.getParameter("pageSize");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		ShopStatus shopStatus=ShopStatus.getType(shopStatusStr);
		Integer pageNum=Integer.valueOf(pageNumStr);
		Integer pageSize=Integer.valueOf(pageSizeStr);
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		return shopService.queryShop(merchantName, shopName, shopStatus, shopAddress, pageNum, pageSize, merchantId, operaterId).toJson();
		
	}
	
	@ResponseBody
	@RequestMapping(value = "")
	public void queryShopsExport(HttpServletRequest req, HttpServletResponse res) {
		String shopName=req.getParameter("shopName");
		String shopAddress=req.getParameter("shopAddress");
		String shopStatusStr=req.getParameter("shopStatus");
		String pageNumStr=req.getParameter("pageNum");
		String pageSizeStr=req.getParameter("pageSize");
		String operaterIdStr = req.getParameter("operaterId");
		String merchantIdStr = req.getParameter("merchantId");
		ShopStatus shopStatus=ShopStatus.getType(shopStatusStr);
		Integer pageNum=Integer.valueOf(pageNumStr);
		Integer pageSize=Integer.valueOf(pageSizeStr);
		Long operaterId = Long.valueOf(operaterIdStr);
		Long merchantId = Long.valueOf(merchantIdStr);
		File file=(File)shopService.queryShopsExport(shopName, shopAddress, shopStatus, pageNum, pageSize, merchantId, operaterId).getDetail();
		try {
			if (file!=null) {
				InputStream is=new FileInputStream(file);
				OutputStream os=res.getOutputStream();
				byte temp[] = new byte[1024];
				int size = -1;
				while ((size = is.read(temp)) != -1) {
					os.write(temp, 0, size);
				}
				is.close();
				os.close();
			}
		} catch (Exception e) {
			LogFactory.error(this,"导出Excel文件时发生异常",e);
		}
	}
	@ResponseBody
	@RequestMapping(value = "")
	public void downloadExcelTemplate(HttpServletRequest req, HttpServletResponse res) {
		String operaterIdStr = req.getParameter("operaterId");
		Long operaterId = Long.valueOf(operaterIdStr);
		File file=(File)shopService.downloadExcelTemplate(operaterId).getDetail();
		try {
			if (file!=null) {
				InputStream is=new FileInputStream(file);
				OutputStream os=res.getOutputStream();
				byte temp[] = new byte[1024];
				int size = -1;
				while ((size = is.read(temp)) != -1) {
					os.write(temp, 0, size);
				}
				is.close();
				os.close();
			}
			
		} catch (Exception e) {
			LogFactory.error(this,"下载Excel模板时发生异常",e);
		}
	}
}
