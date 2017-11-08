package com.lanxi.couponcode.impl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.lanxi.couponcode.impl.entity.CodeAlgorithm;
import com.lanxi.couponcode.impl.entity.CouponCode;
import com.lanxi.util.entity.LogFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lanxi.couponcode.impl.config.ConstConfig.ARTIFCAT;
import static com.lanxi.couponcode.impl.config.FunName.CODE_VALUE;
import static com.lanxi.couponcode.impl.config.FunName.MERCHANT_COUDE_VAR;
import static com.lanxi.couponcode.impl.config.ConstConfig.*;
/**
 * redis串码生产接口实现类
 * Created by yangyuanjian on 2017/10/31.
 */
@Service("codeService")
public class RedisCodeServiceImpl implements RedisCodeService {
    /**
     * redis服务
     */
//    @Resource
//    private RedisCacheServiceInterface redis;
    @Resource
    private DaoService dao;
    @Resource
    private ConfigService config;
    /**
     * 默认的code变量值
     */
    private static final long REDIS_DEFAULT_CODE_VAR = 2;
    /**
     * 默认的本地缓存code变量值
     */
    private static final long LOCAL_DEFAULT_CODE_VAR = 500000000L;
    /**
     * 串码长度
     */
    private static final int CODE_LENGTH=10;
    /**
     * code变量增加的步进
     */
    private static final long CODE_VAR_STEP = 1;
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
     * redis连接池
     */
    private JedisPool pool;
    /**
     * redis默认端口号
     */
    private static final int REDIS_DEFAULT_PORT=6379;
    /**
     * 串码状态-自由态
     */
    private static final String CODE_STATUS_FREE="-1";

    public RedisCodeServiceImpl(){
        init();
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

    /**
     * 初始化质数相关的变量
     */
    private void init(){
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
     * 初始化redis连接池
     * 由于redis连接无法通过dubbo获取,改为本地redis连接池提供,
     */
    private void redisInit(){
        String url=config.getValue("redis","url");
        int port=config.getValue("redis","port")==null?REDIS_DEFAULT_PORT:Integer.parseInt(config.getValue("redis","port"));
        String password=config.getValue("redis","password");
        JedisPoolConfig poolConfig=new JedisPoolConfig();
//		config.setMaxWaitMillis(1000);
        poolConfig.setMaxTotal(Integer.parseInt(config.getValue("redis","maxTotal")));
        poolConfig.setMaxIdle(Integer.parseInt(config.getValue("redis","maxIdle")));
        poolConfig.setMinIdle(Integer.parseInt(config.getValue("redis","minIdle")));
        poolConfig.setMaxWaitMillis(Integer.parseInt(config.getValue("redis","maxWaitMillis")));
        poolConfig.setTestOnBorrow(Boolean.parseBoolean(config.getValue("redis","testOnBorrow")));
        poolConfig.setBlockWhenExhausted(Boolean.parseBoolean(config.getValue("redis","blockWhenExhausted")));
        if(password!=null&&!password.isEmpty()){
            LogFactory.info(this, "init redis pool by password !");
            pool=new JedisPool(poolConfig, url, port, 10000, password);
        }
        else
            pool=new JedisPool(poolConfig, url, port);
    }

    /**
     * 获取redis连接
     * @return jedis
     */
    public Jedis getRedisConn(){
        if(pool==null)
            redisInit();
        return pool.getResource();
    }

    /**
     * 根据商户号获取对应的code算法实体
     * @param merchantId merchant编号
     * @return code算法实体
     */
    private CodeAlgorithm getCodeAlgorithm(long merchantId){
        CodeAlgorithm codeAlgorithm=codeAlgorithmMap.get(merchantId);
        if(codeAlgorithm==null){
            codeAlgorithm=dao.getCodeAlgorithmDao().selectById(merchantId);
        }
        if(codeAlgorithm==null){
            int hash=(merchantId+"").hashCode()>0?(merchantId+"").hashCode():(-(merchantId+"").hashCode());
            int middleSize=middle.size();
            int bigSize=big.size();
            codeAlgorithm=new CodeAlgorithm(merchantId,middle.get(hash%middleSize),big.get(hash%big.size()),big.get(((hash/2)%bigSize)),LOCAL_DEFAULT_CODE_VAR);
            dao.getCodeAlgorithmDao().insert(codeAlgorithm);
        }
        return codeAlgorithm;
    }

//    public CodeAlgorithm testGetCodeAlgorithm(String mer){
//        CodeAlgorithm codeAlgorithm=codeAlgorithmMap.get(mer);
//        int hash=mer.hashCode()>0?mer.hashCode():(-mer.hashCode());
//        int middleSize=middle.size();
//        int bigSize=big.size();
//        codeAlgorithm=new CodeAlgorithm(mer,middle.get(hash%middleSize),big.get(hash%big.size()),big.get(((hash/2)%bigSize)),LOCAL_DEFAULT_CODE_VAR);
//        return codeAlgorithm;
//    }

    /**
     * 根据商户编号获取对应的code
     * @param merchantId 商户编号
     * @return code
     */
    @Override
    public long getCode(Long merchantId){
        CodeAlgorithm codeAlgorithm=getCodeAlgorithm(merchantId);
        long var=getCodeVar(merchantId);
        long code=codeAlgorithm.getCode(var);
        LogFactory.debug(this,"商户code计算结果["+code+"],merchantId["+merchantId+"]");
        if(CODE_LENGTH!=(code+"").length()) {
            LogFactory.debug(this,"商户code长度不为["+CODE_LENGTH+"],需要重新生成,merchantId["+merchantId+"],code["+code+"]");
            return getCode(merchantId);
        }
        try{
            if(checkCodeExist(merchantId,code)||new CouponCode().isExisted(merchantId,code)){
                LogFactory.debug(this,"商户code已存在!重新生成code!,merchantId["+merchantId+"],code["+code+"]");
                return getCode(merchantId);
            }else{
                LogFactory.info(this,"商户code已生成!code["+code+"],merchantId["+merchantId+"],添加到redis中["+(addCode(merchantId,code))+"]");
                return code;
            }
        }catch (Exception e){
            LogFactory.warn(this,"生成商户code时发生异常,merchantId["+merchantId+"]",e);
            if(e.getMessage().contains("redis")||e.getMessage().contains("jedis")||e.getMessage().contains("商户")) {
                LogFactory.warn(this,"redis可能存在异常,仅校验数据库中code有效性!,merchantId["+merchantId+"]");
                List<CouponCode> codes = dao.getCouponCodeDao().selectList(new EntityWrapper<CouponCode>().eq("merchant_id", merchantId).eq("code", code));
                if (codes == null || codes.isEmpty()){
                    LogFactory.info(this,"商户code已生成!code["+code+"],merchantId["+merchantId+"]");
                    return code;
                }
                else{
                    LogFactory.debug(this,"商户codes["+codes+"]已存在!重新生成code!,merchantId["+merchantId+"],code["+code+"]");
                    return getCode(merchantId);
                }
            }else{
                return INVALID_LONG;
            }
        }
    }

    /**
     * 根据商户id获取对应的codeVar
     * @param merchantId
     * @return codeVar
     */
    @Override
    public long getCodeVar(Long merchantId) {
        String key = ARTIFCAT + MERCHANT_COUDE_VAR;
        Jedis conn = null;
        try {
            LogFactory.info(this,"尝试获取商户code计数器,merchantId[" + merchantId + "],key["+key+"]");
            conn = this.getRedisConn();
            long var = conn.hincrBy(key, merchantId+"", CODE_VAR_STEP);
            LogFactory.info(this,"获取商户code计数器,merchantId[" + merchantId + "]结果["+var+"]");
            return var;
        } catch (Exception e) {
            LogFactory.error(this,"获取商户code计数器,merchantId[" + merchantId + "]时发生异常", e);
            return INVALID_LONG;
//            throw new RuntimeException("获取商户code计数器,merchantId[" + merchantId + "]时发生异常", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    /**
     * 添加对应商户的code到hmap中
     *
     * @param merchantId 商户id
     * @param code       串码
     * @return true->添加成功
     * false->添加失败
     */
    @Override
    public boolean addCode(Long merchantId, Long code) {
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试添加商户code[" + merchantId + "]code["+code+"],key["+key+"]");
            conn = this.getRedisConn();
            long result = conn.hsetnx(key, code+"", CODE_STATUS_FREE);
            LogFactory.info(this," 添加商户code,merchantId[" + merchantId + "]code["+code+"],添加结果["+(result==1)+"]");
            return result == 1;
        } catch (Exception e) {
            LogFactory.error(this,"添加商户code,merchantId[" + merchantId + "]code[" + code + "]时发生异常", e);
            throw new RuntimeException("添加商户code,merchantId[" + merchantId + "]code[" + code + "]时发生异常", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    /**
     * 校验对应商户的code是否存在
     *
     * @param merchantId 商户id
     * @param code       串码
     * @return true->存在
     * false->不存在
     */
    @Override
    public boolean checkCodeExist(Long merchantId, Long code) {
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试校验商户code,merchantId[" + merchantId + "]code["+code+"],key["+key+"]");
            conn = this.getRedisConn();
            String temp = conn.hget(key, code+"");
            LogFactory.info(this,"校验商户code,merchantId[" + merchantId + "]code["+code+"]结果["+(temp!=null)+"]");
            return temp != null;
        } catch (Exception e) {
            LogFactory.error(this,"校验商户code,merchantId[" + merchantId + "],code[" + code + "]时发生异常", e);
            throw new RuntimeException("校验商户code,merchantId[" + merchantId + "],code[" + code + "]时发生异常", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    /**
     * 锁定对应商户的code
     * @param merchantId 商户id
     * @param code 串码
     * @param locker 锁定者
     * @return true->锁定成功
     * false->锁定失败
     */
    @Override
    public boolean lockCode(Long merchantId, Long code,String locker) {
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试锁定商户code,merchantId[" + merchantId + "]code["+code+"],locker["+locker+"],key["+key+"]");
            conn = this.getRedisConn();
            String temp = conn.hget(key, code+"");
            if(temp==null){
                LogFactory.info(this,"code不存在,锁定商户code失败!,merchantId[" + merchantId + "],code["+code+"],locker["+locker+"]]");
                return false;
            }
            if (!temp.equals(CODE_STATUS_FREE))
                return temp.equals(locker);
            long result = conn.hset(key, code+"", locker);
            LogFactory.info(this,"锁定商户code,merchantId[" + merchantId + "],code["+code+"],locker["+locker+"]结果["+(result==0)+"]]");
            return result == 0;
        } catch (Exception e) {
            LogFactory.error(this,"锁定商户code,merchantId[" + merchantId + "],code[" + code + "],locker["+locker+"]时发生异常", e);
            throw new RuntimeException("锁定商户code,merchantId[" + merchantId + "],code[" + code + "],locker["+locker+"]时发生异常", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    @Override
    public boolean lockCodeForce(Long merchantId, Long code, String locker) {
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试强制锁定商户code,merchantId[" + merchantId + "]code["+code+"],locker["+locker+"],key["+key+"]");
            conn = this.getRedisConn();
            String temp = conn.hget(key, code+"");
            if (temp == null){
                LogFactory.info(this,"强制锁定商户code失败,code不存在!,merchantId[" + merchantId + "],code["+code+"],locker["+locker+"]");
                return false;
            }
            long result = conn.hset(key, code+"", locker);
            LogFactory.info(this,"强制锁定商户code结果["+(result == 0?"成功":"失败")+"],merchantId[" + merchantId + "],code["+code+"],locker["+locker+"]");
            return result == 0;
        } catch (Exception e) {
            LogFactory.error(this,"强制锁定商户code时发生异常,merchantId[" + merchantId + "],code["+code+"],locker["+locker+"]", e);
            throw new RuntimeException("强制解锁商户code时发生异常,merchantId[" + merchantId + "],code["+code+"],locker["+locker+"]", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    /**
     * 解除对应商户code的锁定
     * @param merchantId 商户id
     * @param code 串码
     * @param unlocker 锁定者
     * @return true -> 解除成功
     * false->解除失败
     */
    @Override
    public boolean unlockCode(Long merchantId, Long code,String unlocker) {
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试解锁商户code,merchantId[" + merchantId + "]code["+code+"],unlocker["+unlocker+"],key["+key+"]");
            conn = this.getRedisConn();
            String temp = conn.hget(key, code+"");
            if (temp == null){
                LogFactory.info(this,"解锁商户code失败,code不存在!,merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]]");
                return false;
            }
            if(temp.equals(unlocker)) {
                long result = conn.hset(key, code+"", CODE_STATUS_FREE);
                LogFactory.info(this,"解锁商户code结果["+(result == 0?"成功":"失败")+"],锁定者与解锁者一致,merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]");
                return result == 0;
            }
            LogFactory.info(this,"解锁商户code失败,锁定者与解锁者不一致,merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]结果["+(false)+"]]");
            return false;
        } catch (Exception e) {
            LogFactory.error(this,"解锁商户code时发生异常,merchantId[" + merchantId + "],code[" + code + "],unlocker["+unlocker+"]", e);
            throw new RuntimeException("解锁商户code时发生异常,merchantId[" + merchantId + "],code[" + code + "],unlocker["+unlocker+"]", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }
    /**
     * 强制解除对应商户code的锁定
     * @param merchantId 商户id
     * @param code 串码
     * @param unlocker 锁定者
     * @return true -> 解除成功
     * false->解除失败
     */
    public boolean unlockCodeForce(Long merchantId,Long code,String unlocker){
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试强制解锁商户code,merchantId[" + merchantId + "]code["+code+"],unlocker["+unlocker+"],key["+key+"]");
            conn = this.getRedisConn();
            String temp = conn.hget(key, code+"");
            if (temp == null){
                LogFactory.info(this,"强制解锁商户code失败,code不存在!,merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]");
                return false;
            }
            long result = conn.hset(key, code+"", CODE_STATUS_FREE);
            LogFactory.info(this,"强制解锁商户code结果["+(result == 0?"成功":"失败")+"],merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]");
            return result == 0;
        } catch (Exception e) {
            LogFactory.error(this,"强制解锁商户code时发生异常,merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]", e);
            throw new RuntimeException("强制解锁商户code时发生异常,merchantId[" + merchantId + "],code["+code+"],unlocker["+unlocker+"]", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    /**
     * 删除hmap中对应商户的codes
     * @param merchantId 商户id
     * @param codes 多个串码
     * @return true->删除成功
     * false->删除失败
     */
    @Override
    public boolean delCode(Long merchantId, String... codes) {
        Jedis conn = null;
        String key = ARTIFCAT + CODE_VALUE + merchantId;
        try {
            LogFactory.info(this,"尝试删除商户code,merchantId[" + merchantId + "],codes["+Arrays.asList(codes)+"],key["+key+"]");
            conn = this.getRedisConn();
            long result = conn.hdel(key,codes);
            LogFactory.info(this,"删除商户code结果["+(result == 1?"成功":"失败")+"],merchantId[" + merchantId + "],codes["+Arrays.asList(codes)+"],");
            return result == 1;
        } catch (Exception e) {
            LogFactory.error(this,"删除商户code时发生异常,merchantId[" + merchantId + "],codes[" + Arrays.asList(codes) + "]", e);
            throw new RuntimeException("删除商户code时发生异常,merchantId[" + merchantId + "],codes[" + Arrays.asList(codes) + "]", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

    /**
     * 更新对应商户的code计数器
     * @param merchantId 商户id
     * @param codeVar code计数器的新值
     * @return true->更新成功
     * false->更新失败
     */
    @Override
    public boolean updateCodeVar(Long merchantId, Long codeVar) {
        String key = ARTIFCAT + MERCHANT_COUDE_VAR;
        Jedis conn = null;
        try {
            LogFactory.info(this,"尝试更新商户code计数器,merchantId[" + merchantId + "],codeVar["+codeVar+"],key["+key+"]");
            if(codeVar<2){
                LogFactory.info(this,"更新商户code计数器,codeVar小于2,设置为默认值["+REDIS_DEFAULT_CODE_VAR+"],merchantId[" + merchantId + "],codeVar["+codeVar+"]");
                codeVar = REDIS_DEFAULT_CODE_VAR;
            }
            conn = this.getRedisConn();
            String nowStr=conn.hget(key,merchantId+"");
            long now=Long.parseLong(nowStr);
            LogFactory.info(this,"更新商户code计数器,当前redis中的codeVar["+now+"],merchantId[" + merchantId + "],codeVar["+codeVar+"],key["+key+"]");
            if(codeVar<now){
                LogFactory.info(this,"更新商户code计数器,当前redis中的codeVar["+now+"]大于更新的值,更新失败,merchantId[" + merchantId + "],codeVar["+codeVar+"]");
                return false;
            }
            long result = conn.hset(key, merchantId+"", codeVar + "");
            LogFactory.info(this,"更新商户code计数器结果["+(result == 0?"成功":"失败")+"],merchantId[" + merchantId + "],codeVar["+codeVar+"],");
            return result == 0;
        } catch (Exception e) {
            LogFactory.error(this,"更新商户code计数器时发生异常,merchantId[" + merchantId + "],codeVar["+codeVar+"]", e);
            throw new RuntimeException("更新商户code计数器时发生异常,merchantId[" + merchantId + "],codeVar["+codeVar+"]", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                }catch (Exception e) {
                    LogFactory.warn(this,"关闭redis连接时发生异常,jedis["+conn+"]",e);
                }
            }
        }
    }

}
