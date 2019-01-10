package com.iot.common.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：RSA算法随机生成密钥对
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月10日 上午9:45:43
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月10日 上午9:45:43
 */
public class RSAKeyGenerator {

    /**
     * RSA算法key size
     */
    private static final int RSA_KEY_SIZE = 2048;

    /**
     * RSA算法
     */
    private static final String ALGORITHM_RSA = "RSA";

    /**
     * 描述：获取KeyPair
     *
     * @return KeyPair
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年8月23日 上午9:45:58
     * @since
     */
    public static KeyPair getRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSAKeyGenerator.ALGORITHM_RSA);
        keyPairGen.initialize(RSAKeyGenerator.RSA_KEY_SIZE);
        return keyPairGen.generateKeyPair();
    }

    /**
     * 描述：获取密钥对
     *
     * @return RSAKey[] [0]=RSAPublicKey,[1]=RSAPrivateKey
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年8月23日 上午9:47:59
     * @since
     */
    public static RSAKey[] getRSAKey() throws Exception {
        KeyPair keyPair = getRSAKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey[]{rsaPublicKey, rsaPrivateKey};
    }

    /**
     * 描述：获取密钥对
     *
     * @return String[] [0]=RSAPublicKeyString,[1]=RSAPrivateKeyString
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年8月23日 上午9:47:59
     * @since
     */
    public static String[] getRSAStringKey() throws Exception {
        RSAKey[] keys = getRSAKey();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keys[0];
        String pubKey = CommonUtil.encodeBase64(rsaPublicKey.getEncoded());
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keys[1];
        String priKey = CommonUtil.encodeBase64(rsaPrivateKey.getEncoded());
        return new String[]{pubKey, priKey};
    }

    /**
     * 描述：测试代码
     *
     * @param args
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年8月23日 上午9:55:11
     * @since
     */
    public static void main(String[] args) throws Exception {
        String[] keys = getRSAStringKey();
        System.out.println("生成的公钥：\r\n" + keys[0]);
        System.out.println("生成的私钥：\r\n" + keys[1]);
    }

}