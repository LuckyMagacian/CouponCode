package com.lanxi.couponcode.spi.config;

public interface ConstConfig {
    //-------------------------------about project---------------------------------
    /**
     * 公司名称
     */
    String ORG_NAME = "com.lanxi";
    /**
     * 项目名称
     */
    String APP_NAME = "couponcode";
    /**
     * 版本
     */
    String VERSION = "1.0";
    /**
     * 项目唯一
     */
    String ARTIFCAT = ORG_NAME + "-" + APP_NAME + "-" + VERSION + "-";
    //-----------------------------------------------------------------------------
    /**
     * 配置文件路径
     */
    String STATIC_CONFIG_PATH = "classpath:properties";
    //--------------------------------id key-------------------------------------
//	/**redis中账户key*/
//	String ACCOUNT_ID_KEY=ARTIFCAT+"accountID";
//	/**redis中商品key*/
//	String COMMDITY_ID_KEY=ARTIFCAT+"commdityID";
//	/**redis中串码key*/
//	String COUPON_ID_KEY=ARTIFCAT+"couponID";
//	/**redis中商户key*/
//	String MERCHANT_ID_KEY=ARTIFCAT+"merchantID";
//	/**redis中消息key*/
//	String MESSAGE_ID_KEY=ARTIFCAT+"messageID";
//	/**redis中操作记录key*/
//	String OPERATE_RECORD__ID_KEY=ARTIFCAT+"operateRecordID";
//	/**redis中请求key*/
//	String REQUEST_ID_KEY=ARTIFCAT+"requestID";
//	/**redis中门店key*/
//	String SHOP_ID_KEY=ARTIFCAT+"shopID";
//	/**redis中统计key*/
//	String STATISTIC_ID_KEY=ARTIFCAT+"statisticID";
//	/**redis中用户key*/
//	String USER_ID_KEY=ARTIFCAT+"userID";
//	/**redis中核销记录key*/
//	String VERIFICATION_ID_KEY=ARTIFCAT+"verificationID";
    //----------------------------------fun key---------------------------------------

    //----------------------------------default value---------------------------------
    /**
     * id默认值
     */
    long ID_DEFAULT_VALUE = 1000000L;
    /**
     * 机器编号  默认值
     */
    String MACHINE_NUMER_DEFAULT_VALUE = "1";
    /**
     * 默认分页参数
     */
    Integer DEFAULT_PAGE = 1;
    Integer DEFAULT_PAGE_SIZE = 10;

    String priKey="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCZKc8jauD3YOEsXo+fAeirbCB4x8peSHO/imycW43us83yFapHMJq2tpRWlCVNaY134iRylCA39kSwOS6LG2suo97LKWBMuZm7Qz6GFun60+EjZw52+q94gcjf/QSteKlzQ6QCZfYeX64RyXneOzepQvO4fMFWSGUPYLGAoIUKutm47Z+iJyxeXU50e3tkL7F8VL43SYI92JvAqoFTiXkRNL1V2o3OyfPfOHOHHNECU3AxDtSExWL7fMaocUwwapEUwHEowVOkXCaHkRqrGnSTDb9Lyk2c2wguP1pgXoiBULf6DtGT+bOEFdOKZBrzX6SvV+BxHdp062pqxNbTBfmRAgMBAAECggEAfI4zlWnlaUev1pVP1s+Gl2FnRtGzENl6x9maWc1arzQp1yUnRJ2SM+Rm3cG8VCfyk/ukCEnNqbTsRyM5Zob1swgwtqb/WWXWTyNwRTt9LC4CePfmG3viFu30nbL1it0FcGchiZHBhRHqTSy7nq4ppcx8xxstXy2ggdDwiNoBV6c19fH4lKqJSVquY3f6sbDqIB9jjJQ6mjRMl7MxUVA752Fs/Rgg+U8n+EP9gitYXuMAIAJRpS0qU4Tpd2ynkIUnpxmK44zZFLRmARfwqtbVD8wtsjC2b5IBm5avvQiQOzCezXJf5cO10lDDyrNRL7Zek+Ikbv8VF5kx0URG1fvgAQKBgQDo7fqnRt5ybkvFqyVZusxLgUYslbmkaQR/fCJBd0GDfRPCQF01knkTf7Jjr7pgo1QEvW2numD8k+V7SDhGW0KfLCECj3s82w3G2ZKpcNwgxxgjFTT8hjrkk6VTa+ewD2oIrodssiOK4ikqBARY4FufuUC6fvcKTWzCAeoIbwaSsQKBgQCoVVMfDxtkdSkDr0CfCmYbvCURxAgvYXntovyQEv5YwqNhddb3KZAe+Sx/3VYrJ00L/Bw1vxmzVP78FJG3iD27b/TiRxR1J8WFxdQWX3tF2rWaYGY9pJxeGeiYHSgpC+l694WjofQnJLKvYze8rhID1gn3XfXqGUj3+tj/6XrM4QKBgQClq6YM1g4fi61fEYSGVqM5zN+xlGiutHcbiZn/wCebsxmbnxXspZpyMPAbkQBZ/bEjkevtUVXsL/EXh11nRT+UYNm1rYG03gTlexiiZYBb29+iDFJcjchTjWQYenYWtk/vjtsJh4ZaINOwa5z/7cudRjDwtJ9W94y0TJI9n2IocQKBgQCjd0hizu7Q3R1ZIKTmTNjfchOlbvJr4ILhC/eriWZJl6nFCQQZvNjh2yMlgDEfcujVDJvwbYVtqsp6zRa8XadlD9GEs3XgVyDvSS8uEsbJDRxxr89ZaTMYBpziqt3HDnRLH3c9chaAtJh4zSbhKUEBTKcc+rfF/4vmlEiKK5c+wQKBgQDGcu5D6r5BVnzveRB0JI8QthWvSJoz7m37lRl7itMQTlFZfFPKOgK+hM57i/fumeXiN83BXY3BaBE8hEAK5YzsQGvCwJmY3XJRxD1WrXy2BrNauLaPf55mR4TAvay+acMdvKRP+KpXoi0EW3Sse71UBCgrc0wk/Ky15DPLSsywHQ==";
    String pubKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmSnPI2rg92DhLF6PnwHoq2wgeMfKXkhzv4psnFuN7rPN8hWqRzCatraUVpQlTWmNd+IkcpQgN/ZEsDkuixtrLqPeyylgTLmZu0M+hhbp+tPhI2cOdvqveIHI3/0ErXipc0OkAmX2Hl+uEcl53js3qULzuHzBVkhlD2CxgKCFCrrZuO2foicsXl1OdHt7ZC+xfFS+N0mCPdibwKqBU4l5ETS9VdqNzsnz3zhzhxzRAlNwMQ7UhMVi+3zGqHFMMGqRFMBxKMFTpFwmh5Eaqxp0kw2/S8pNnNsILj9aYF6IgVC3+g7Rk/mzhBXTimQa81+kr1fgcR3adOtqasTW0wX5kQIDAQAB";
}

