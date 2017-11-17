package com.lanxi.couponcode.impl.newservice;

import com.lanxi.couponcode.impl.entity.CodeAlgorithm;
import com.lanxi.couponcode.impl.service.ConfigService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by yangyuanjian on 2017/11/15.
 */
@Service("codeAlgorithmService")
@Order(20000)
public class CodeAlgorithmServiceImpl implements CodeAlgorithmService{
    @Resource
    DaoService daoService;
    @Resource
    RedisService redis;
    @Resource
    RedisEnhancedService enhancedRedis;
    @Resource
    ConfigService config;
    /**
     * 串码算法结构map
     */
    private Map<String,CodeAlgorithm> codeAlgorithmMap=new HashMap<>();
    /**
     * 质数list
     */
    private List<Integer> primes=null;
    /**
     * 介于100到180之间的质数的list
     */
    private List<Integer> middle=null;
    /**
     * 介于50000到100000之间的质数的list
     */
    private List<Integer> big=null;
    /**
     * 质数起点只能设置为2否则将导致基于筛选算法的质数算法结果错误
     */
    private static final int PRIMES_START=2;
    /**
     * 质数终点
     */
    private static final int PRIMES_END=100000;
    /**
     * 默认的本地缓存code变量值
     */
    private static final long LOCAL_DEFAULT_CODE_VAR = 500000000L;


    public CodeAlgorithmServiceImpl(){

    }

    /***
     * 判断一个数是否是素数
     * @param num
     * @return true->是素数,false->不是素数
     */
    private boolean isPrime(int num){
        double factor=Math.sqrt(num);
        int times=(int)(factor+1);
        for(int i=2;i<factor;i++){
            if(num%i==0) {
                return false;
            }
        }
        return true;
    }

    private void initConfig(){

    }

    /**
     * 初始化质数相关的变量
     */
    private void initPrimes(){
        primes=new ArrayList<>();
        int now=-10;
        List<Integer> nums= IntStream.range(PRIMES_START,PRIMES_END).parallel().mapToObj(e->Integer.valueOf(e)).collect(Collectors.toList());
        now =nums.parallelStream().filter(this::isPrime).findAny().orElse(-1);
        if(now==-1)
            throw new IllegalArgumentException("no prime found between ["+PRIMES_START+"] and ["+PRIMES_END+"] !");
        while(true){
            final int temp=now;
            primes.add(now);
            nums=nums.parallelStream().filter(e->e%temp!=0).collect(Collectors.toList());
            if(nums.size()>0)
                now=nums.get(0);
            else
                break;
        }
        middle=primes.parallelStream().filter(e->e<180&&(e+"").length()==3).collect(Collectors.toList());
        big=primes.parallelStream().filter(e->e>50000&&(e+"").length()==5).collect(Collectors.toList());
    }

    /**
     * 根据商户号获取对应的code算法实体
     * @param merchantId merchant编号
     * @return code算法实体
     */
    @Override
    public CodeAlgorithm getCodeAlgorithm(long merchantId){
        CodeAlgorithm codeAlgorithm=codeAlgorithmMap.get(merchantId);
        if(codeAlgorithm==null){
            codeAlgorithm=daoService.getCodeAlgorithmDao().selectById(merchantId);
        }
        if(codeAlgorithm==null) {
            addCodeAlgorithm(merchantId);
            codeAlgorithm=daoService.getCodeAlgorithmDao().selectById(merchantId);
        }
        return codeAlgorithm;
    }
    /**添加算法实体*/
    private void addCodeAlgorithm(Long merchantId){
        CodeAlgorithm codeAlgorithm;
        int hash=(merchantId+"").hashCode()>0?(merchantId+"").hashCode():(-(merchantId+"").hashCode());
        int middleSize=middle.size();
        int bigSize=big.size();
        codeAlgorithm=new CodeAlgorithm(merchantId,middle.get(hash%middleSize),big.get(hash%big.size()),big.get(((hash/2)%bigSize)),LOCAL_DEFAULT_CODE_VAR);
        daoService.getCodeAlgorithmDao().insert(codeAlgorithm);
    }

    public void setConfig(ConfigService config) {
        this.config = config;
        initConfig();
    }

    public void setRedis(RedisService redis) {
        this.redis = redis;
        initPrimes();
    }
}
