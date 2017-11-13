package com.lanxi.couponcode.impl.util;

public class ImageUtil {
	
	public static Boolean isImage(String end) {
		String imageType=".bmp.jpg.png.tiff.gif.pcx.tga.exif.fpx.svg.psd.cdr.pcd.dxf.ufo.eps.ai.raw.WMF";
		return imageType.contains(end);
	} 
}
