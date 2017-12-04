package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.assist.PredicateAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Parameter;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
@Deprecated
public class CheckArgs {
//    @Resource
//    private DaoService dao;
    @Pointcut("within(com.lanxi.couponcode.spi.consts.annotations.CheckArg)")
    private void checkArg(){}

    public <T>  RetMessage checkParams(ProceedingJoinPoint joinPoint) {

        AopJob<RetMessage> job = (clazz, method, args, point) -> {
            T t = null;
            RetMessage message = new RetMessage();
            try {
                //记录参数
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {

                    if(parameters[i].getName().contains("time")||parameters[i].getName().contains("Time")) {
                        if(args[i]==null)
                            continue;
                        if (args[i] instanceof String) {
                            if (PredicateAssist.notTime.test((String) args[i])) {
                                message.setRetCode(RetCodeEnum.fail);
                                message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                                return message;
                            }
                        }
                    }
                    if(parameters[i].getName().contains("name")||parameters[i].getName().contains("Name")){
                        if(args[i]==null)
                            continue;
                        if(args[i] instanceof String){
                            if(PredicateAssist.notAddressOrName.test((String) args[i])){
                                message.setRetCode(RetCodeEnum.fail);
                                message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                                return message;
                            }
                        }
                    }

                    if(parameters[i].getName().contains("page")||parameters[i].getName().contains("Page")){
                        if(args[i]==null){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("分页参数["+parameters[i].getName()+"]校验不通过!");
                            return message;
                        }
                        String arg=null;
                        if(args[i] instanceof String){
                            args= (Object[]) args[i];
                        }else if(args[i].getClass().equals(int.class)||args[i].getClass().equals(Integer.class)){
                            arg=args[i]+"";
                        }
                        if(PredicateAssist.notPage.test(arg)){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("分页参数[" + parameters[i].getName() + "]校验不通过!");
                            return message;
                        }
                    }

                    if(parameters[i].getName().equals("code")||parameters[i].getName().equals("couponCode")){
                        if(args[i]==null)
                            continue;
                        String arg=null;
                        if(args[i] instanceof String){
                            arg= (String) args[i];
                        }else if(args[i] instanceof Long || args[i].getClass().equals(long.class))
                            arg=args[i]+"";
                        if(PredicateAssist.notCode.test(arg)){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                            return message;
                        }
                    }

                    if(parameters[i].getName().contains("id")||parameters[i].getName().contains("Id")){
                        if(parameters.equals("operaterId")&&args[i]==null){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("分页参数["+parameters[i].getName()+"]校验不通过!");
                            return message;
                        }
                        if(args[i]==null)
                            continue;
                        String arg=null;
                        if(args[i] instanceof String){
                            arg= (String) args[i];
                        }else if(args[i] instanceof Long || args[i].getClass().equals(long.class))
                            arg=args[i]+"";
                        if(PredicateAssist.notId.test(arg)){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                            return message;
                        }
                    }

                }
                //调用方法
                t = (T) joinPoint.proceed(args);
                return (RetMessage) t;
            } catch (Throwable e) {
                return message;
            }
        };
        return AopJob.workJob(job,joinPoint);
    }
}
