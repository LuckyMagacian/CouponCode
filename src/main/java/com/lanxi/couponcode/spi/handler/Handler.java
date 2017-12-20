package com.lanxi.couponcode.spi.handler;

import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yangyuanjian on 2017/10/27.
 */
@Deprecated
public interface Handler {
    <T> String  handle(HttpServletRequest req, HttpServletResponse res);
    <T> void handle(HttpRequest req, HttpServletResponse res, boolean fileWanted);
}
