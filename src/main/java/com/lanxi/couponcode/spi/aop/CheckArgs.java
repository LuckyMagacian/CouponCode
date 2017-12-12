package com.lanxi.couponcode.spi.aop;

import com.lanxi.couponcode.spi.assist.PredicateAssist;
import com.lanxi.couponcode.spi.assist.RetMessage;
import com.lanxi.couponcode.spi.consts.enums.RetCodeEnum;
import com.lanxi.util.entity.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * Created by yangyuanjian on 2017/11/10.
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+20)
public class CheckArgs {
//    @Resource
//    private DaoService dao;
    @Pointcut("@annotation(com.lanxi.couponcode.spi.consts.annotations.CheckArg)")
    private void checkArg(){}

    @Pointcut("@within(com.lanxi.couponcode.spi.consts.annotations.CheckArg)")
    private void checkArgClass(){}

    private static final BiFunction<Parameter[],Integer,String> parametersToName=(p,i)->p[i].getName();

    private static final String idRegex="(id)||([a-zA-Z0-9]+Id)";
    private static final String timeRegex="(startTime)||(stopTime)||(endTime)";
    private static final String phoneRegex="(phone)||([a-zA-Z0-9]+Phone)";
    private static final String nameRegex="(name)||([a-zA-Z0-9]+Name)";
    private static final String pageRegex="(pageNum)||(pageSize)";
    private static final String codeRegex="(code)||(couponCode)";
    private static final String validateCodeRegex="(validateCode)||(validationCode)";


    @Around("checkArg()||checkArgClass()")
    public <T>  RetMessage checkParams(ProceedingJoinPoint joinPoint) {
        LogFactory.debug(this,"---------------------------aop checkargs is working---------------------------");
        AopJob<RetMessage> job = (clazz, method, args, point) -> {
            T t;
            LogFactory.debug(this.getClass(), "try checkarg for class:[" + clazz.getName() + "] method:[" + method + "] arg:[" + Arrays.asList(args) + "]");
            RetMessage message = new RetMessage();
            try {
                Parameter[] parameters = method.getParameters();

                for (int i = 0; i < parameters.length; i++) {
                    //校验手机号
                    String parameterName=parametersToName.apply(parameters,i);
                    if(parameterName.matches(phoneRegex)){
                        if(args[i]==null)
                            continue;
                        if(args[i] instanceof String){
                            if(((String)args[i]).isEmpty())
                                continue;
                            if(PredicateAssist.isPhone.negate().test((String) args[i])){
                                message.setRetCode(RetCodeEnum.fail);
                                message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                                return message;
                            }
                        }
                    }
                    //校验时间
                    if(parameterName.matches(timeRegex)) {
                        if(args[i]==null)
                            continue;
                        if (args[i] instanceof String) {
                            if(((String)args[i]).isEmpty())
                                continue;
                            if (PredicateAssist.notTime.test((String) args[i])) {
                                message.setRetCode(RetCodeEnum.fail);
                                message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                                return message;
                            }
                        }
                    }
                    //校验名称
                    if(parameterName.matches(nameRegex)){
                        if(args[i]==null)
                            continue;
                        if(args[i] instanceof String){
                            if(((String)args[i]).isEmpty())
                                continue;
                            if(PredicateAssist.notAddressOrName.test((String) args[i])){
                                message.setRetCode(RetCodeEnum.fail);
                                message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                                return message;
                            }
                        }
                    }
                    //校验分页参数
                    if(parameterName.matches(pageRegex)){
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
                    //校验串码
                    if(parameterName.matches(codeRegex)){
                        if(args[i]==null)
                            continue;
                        String arg=null;
                        if(args[i] instanceof String){
                            if(((String)args[i]).isEmpty())
                                continue;
                            arg= (String) args[i];
                        }else if(args[i] instanceof Long || args[i].getClass().equals(long.class))
                            arg=args[i]+"";
                        if(PredicateAssist.notCode.test(arg)){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("参数[" + parameters[i].getName() + "]校验不通过!");
                            return message;
                        }
                    }
                    //校验id
                    if(parameterName.matches(idRegex)){
                        if(parameters.equals("operaterId")&&args[i]==null){
                            message.setRetCode(RetCodeEnum.fail);
                            message.setRetMessage("参数["+parameters[i].getName()+"]校验不通过!");
                            return message;
                        }
                        if(args[i]==null)
                            continue;
                        String arg=null;
                        if(args[i] instanceof String){
                            if(((String)args[i]).isEmpty())
                                continue;
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
                message.setAll(RetCodeEnum.error,"校验参数时发生异常!",null);
                LogFactory.error(this,"exception occurred in ["+this.getClass().getName()+"] [checkParam] when invoke class:["+clazz.getName()+"],method:["+method.getName()+"],args:["+(args==null?null:Arrays.asList(args))+"]",e);
                return message;
            }
        };
        return AopJob.workJob(job,joinPoint);
    }
}
