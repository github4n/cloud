package com.iot.common.util;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.ExceptionEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.UUID;

/**
 * 项目名称：IOT云平台
 * 模块名称：常用工具
 * 功能描述：安全工具类
 * 创建人： mao2080@sina.com
 * 创建时间：2017年4月5日 下午7:40:37
 * 修改人： mao2080@sina.com
 * 修改时间：2017年4月5日 下午7:40:37
 */
public class SecurityUtil {

    /**
     * jwt-baseKey
     */
    private static final String BASE_KEY = "0fe3027dbe354abee58b37350d41ce4b";

    /**
     * jwt-encodedKey
     */
    private static final byte[] ENCODE_KEY = Base64.decodeBase64(BASE_KEY);

    /**
     * jwt-secretKey
     */
    private static final SecretKey SECRET_KEY = new SecretKeySpec(ENCODE_KEY, 0, ENCODE_KEY.length, "JWT");

    /**
     * AES算法key size
     */
    private static final int AES_KEY_SIZE = 128;

    /**
     * AES算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * AES-charset
     */
    private static final String AES_CHARSET = "UTF-8";

    /**
     * RSA算法
     */
    private static final String ALGORITHM_RSA = "RSA";

    /**
     * RSA-charset
     */
    private static final String RSA_CHARSET = "UTF-8";

    /**
     * 描述：创建jwt令牌-可用于登录令牌，防止重复提交令牌
     *
     * @param id      id
     * @param subject 正文
     * @param time    过期时间（相对于当前时间，单位毫秒）
     * @return
     * @throws Exception
     * @author mao2080@sina.com
     * @created 2017年4月6日 下午3:28:59
     * @since
     */
    public static String createJWT(String id, String subject, long time) throws BusinessException {
        if (StringUtil.isBlank(id)) {
            throw new BusinessException(ExceptionEnum.JWT_ARGUMENT_ID_EMPTY_EXCEPTION);
        }
        if (StringUtil.isBlank(subject)) {
            throw new BusinessException(ExceptionEnum.JWT_ARGUMENT_SUBJECT_EMPTY_EXCEPTION);
        }
        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder();
        builder.setId(id); //JWTID
        builder.setIssuedAt(new Date(nowMillis)); //代表这个JWT的签发时间
        builder.setSubject(subject); //令牌body
        builder.signWith(SignatureAlgorithm.HS384, SECRET_KEY); //加密算法
        if (time >= 0) {
            builder.setExpiration(new Date(nowMillis + time)); //设置过期时间
        }
        return builder.compact();
    }

    /**
     * 描述：解密jwt
     *
     * @param jwt jwt类型字符串
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月6日 下午3:29:56
     * @since
     */
    public static Claims parseJWT(String jwt) throws BusinessException {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ExceptionEnum.JWT_EXPIRED_EXCEPTION, e);
        } catch (MalformedJwtException e) {
            throw new BusinessException(ExceptionEnum.JWT_MALFORMED_EXCEPTION, e);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 描述：生成随机token（基于UUID）
     *
     * @return 返回32位随机token
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月6日 下午4:43:47
     * @since
     */
    public static String generateToken() {
        return EncryptByMd5(UUID.randomUUID().toString());
    }

    /**
     * 描述：将字符串通过md5算法加密
     *
     * @param string 字符串
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月5日 下午7:41:50
     * @since
     */
    public static String EncryptByMd5(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(string.getBytes("utf-8"));
            return CommonUtil.parseByte2HexStr(result);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.MD5_ENCRYPTION_FAILED, e);
        }
    }

    /**
     * 描述：将字符串通过AES算法加密
     *
     * @param content 需要加密的内容
     * @param strkey  密钥
     * @return 加密后字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月7日 上午11:00:51
     * @since
     */
    public static String EncryptByAES(String content, String strkey) throws BusinessException {
        if (StringUtil.isBlank(strkey)) {
            throw new BusinessException(ExceptionEnum.AES_KEY_EMPTY_EXCEPTION);
        }
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(SecurityUtil.ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strkey.getBytes());
            kgen.init(SecurityUtil.AES_KEY_SIZE, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, SecurityUtil.ALGORITHM);
            Cipher cipher = Cipher.getInstance(SecurityUtil.ALGORITHM); // 创建密码器
            byte[] byteContent = content.getBytes(SecurityUtil.AES_CHARSET);
            cipher.init(Cipher.ENCRYPT_MODE, key); //初始化
            byte[] result = cipher.doFinal(byteContent); //加密
            return CommonUtil.parseByte2HexStr(result);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.AES_ENCRYPTION_FAILED, e);
        }
    }

    /**
     * 描述：将字符串通过AES算法解密
     *
     * @param content 需要解密的内容
     * @param strkey  密钥
     * @return
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月7日 上午11:18:51
     * @since
     */
    public static String DecryptAES(String content, String strkey) throws BusinessException {
        if (StringUtil.isBlank(strkey)) {
            throw new BusinessException(ExceptionEnum.AES_KEY_EMPTY_EXCEPTION);
        }
        try {
            byte[] decryptFrom = CommonUtil.parseHexStr2Byte(content);
            KeyGenerator kgen = KeyGenerator.getInstance(SecurityUtil.ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strkey.getBytes());
            kgen.init(SecurityUtil.AES_KEY_SIZE, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, SecurityUtil.ALGORITHM);
            Cipher cipher = Cipher.getInstance(SecurityUtil.ALGORITHM); //创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key); //初始化
            return new String(cipher.doFinal(decryptFrom), "utf-8");
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.AES_DECRYPT_FAILED, e);
        }
    }

    /**
     * 描述：将字符串通过RSA算法公钥加密
     *
     * @param content 需要加密的内容
     * @param pubKey  公钥
     * @return 加密后字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月9日 上午09:18:51
     * @since
     */
    public static String EncryptByRSAPubKey(String content, String pubKey) throws BusinessException {
        if (StringUtil.isBlank(pubKey)) {
            throw new BusinessException(ExceptionEnum.RSA_PUB_KEY_EMPTY_EXCEPTION);
        }
        try {
            PublicKey publicKey = SecurityUtil.getRSAPubKey(pubKey);
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipher.update(content.getBytes(SecurityUtil.RSA_CHARSET));
            return CommonUtil.encodeBase64(cipher.doFinal());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.RSA_ENCRYPTION_FAILED, e);
        }
    }

    /**
     * 描述：将字符串通过RSA算法公钥解密
     *
     * @param content 需要解密的内容
     * @param pubKey  公钥
     * @return 解密后字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月9日 上午09:18:51
     * @since
     */
    public static String DecryptByRSAPubKey(String content, String pubKey) throws BusinessException {
        if (StringUtil.isBlank(pubKey)) {
            throw new BusinessException(ExceptionEnum.RSA_PUB_KEY_EMPTY_EXCEPTION);
        }
        try {
            PublicKey publicKey = SecurityUtil.getRSAPubKey(pubKey);
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            cipher.update(CommonUtil.decodeBase64(content));
            return new String(cipher.doFinal(), RSA_CHARSET);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.RSA_DECRYPT_FAILED, e);
        }
    }

    /**
     * 描述：将字符串通过RSA算法私钥加密
     *
     * @param content 需要加密的内容
     * @param priKey  私钥
     * @return 加密后字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月9日 上午09:18:51
     * @since
     */
    public static String EncryptByRSAPriKey(String content, String priKey) throws BusinessException {
        if (StringUtil.isBlank(priKey)) {
            throw new BusinessException(ExceptionEnum.RSA_PRI_KEY_EMPTY_EXCEPTION);
        }
        try {
            PrivateKey privateKey = SecurityUtil.getRSAPriKey(priKey);
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            cipher.update(content.getBytes(SecurityUtil.RSA_CHARSET));
            return CommonUtil.encodeBase64(cipher.doFinal());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.RSA_ENCRYPTION_FAILED, e);
        }
    }

    /**
     * 描述：将字符串通过RSA算法私钥解密
     *
     * @param content 需要解密的内容
     * @param priKey  私钥
     * @return 解密后字符串
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月9日 上午09:18:51
     * @since
     */
    public static String DecryptByRSAPriKey(String content, String priKey) throws BusinessException {
        if (StringUtil.isBlank(priKey)) {
            throw new BusinessException(ExceptionEnum.RSA_PRI_KEY_EMPTY_EXCEPTION);
        }
        try {
            PrivateKey privateKey = SecurityUtil.getRSAPriKey(priKey);
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipher.update(CommonUtil.decodeBase64(content));
            return new String(cipher.doFinal(), SecurityUtil.RSA_CHARSET);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.RSA_DECRYPT_FAILED, e);
        }
    }

    /**
     * 描述：获取RSA公钥
     *
     * @return PublicKey
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月9日 上午09:18:51
     * @since
     */
    private static PublicKey getRSAPubKey(String pubKey) throws BusinessException {
        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(CommonUtil.decodeBase64(pubKey));
            KeyFactory keyFactory = KeyFactory.getInstance(SecurityUtil.ALGORITHM_RSA);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.RSA_PUB_KEY_GENERATE_EXCEPTION, e);
        }
    }

    /**
     * 描述：获取RSA私钥
     *
     * @param priKey 私钥
     * @return PrivateKey
     * @throws BusinessException
     * @author mao2080@sina.com
     * @created 2017年4月9日 上午09:18:51
     * @since
     */
    private static PrivateKey getRSAPriKey(String priKey) throws BusinessException {
        try {
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(CommonUtil.decodeBase64(priKey));
            KeyFactory keyFactory = KeyFactory.getInstance(SecurityUtil.ALGORITHM_RSA);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.RSA_PRI_KEY_GENERATE_EXCEPTION, e);
        }
    }

    public static void main(String[] args) throws BusinessException {
        System.out.println("sss:"+EncryptByAES("2","Leedarson"));
        System.out.println("sss:"+DecryptAES("336a9dd522a24c4360669cbae3b4b085","Leedarson"));
        System.out.println(CommonUtil.getSystemLog("MD5"));
        System.out.println("md5加密：" + EncryptByMd5("Leedarson@"));
        System.out.println("随机token：" + generateToken());

        System.out.println(CommonUtil.getSystemLog("JWT"));
        String jwt = createJWT("1234", "Leedarson", 10000);
        System.out.println("生成jwt-token：" + jwt);
        System.out.println("解析jwt-token：" + parseJWT(jwt).toString());

        System.out.println(CommonUtil.getSystemLog("AES"));
        String aes = EncryptByAES("leedarson", "1234567812345678");
        System.out.println("AES加密：" + aes);
        System.out.println("AES解密：" + DecryptAES(aes, "1234567812345678"));

        System.out.println(CommonUtil.getSystemLog("RSA"));
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYU/+I0+z1aBl5X6DUUOHQ7FZpmBSDbKTtx89JEcB64jFCkunELT8qiKly7fzEqD03g8ALlu5XvX+bBqHFy7YPJJP0ekE2X3wjUnh2NxlqpH3/B/xm1ZdSlCwDIkbijhBVDjA/bu5BObhZqQmDwIxlQInL9oVz+o6FbAZCyHBd7wIDAQAB";
        String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhT/4jT7PVoGXlfoNRQ4dDsVmmYFINspO3Hz0kRwHriMUKS6cQtPyqIqXLt/MSoPTeDwAuW7le9f5sGocXLtg8kk/R6QTZffCNSeHY3GWqkff8H/GbVl1KULAMiRuKOEFUOMD9u7kE5uFmpCYPAjGVAicv2hXP6joVsBkLIcF3vAgMBAAECgYBvZHWoZHmS2EZQqKqeuGr58eobG9hcZzWQoJ4nq/CarBAjw/VovUHE490uK3S9ht4FW7Yzg3LV/MB06Huifh6qf/X9NQA7SeZRRC8gnCQk6JuDIEVJOud5jU+9tyumJakDKodQ3Jf2zQtNr+5ZdEPluwWgv9c4kmpjhAdyMuQmYQJBANn6pcgvyYaia52dnu+yBUsGkaFfwXkzFSExIbi0MXTkhEb/ER/DrLytukkUu5S5ecz/KBa8U4xIslZDYQbLz5ECQQCy5dutt7RsxN4+dxCWn0/1FrkWl2G329Ucewm3QU9CKu4D+7Kqdj+Ha3lXP8F0Etaaapi7+EfkRUpukn2ItZV/AkEAlk+I0iphxT1rCB0Q5CjWDY5SDf2B5JmdEG5Y2o0nLXwG2w44OLct/k2uD4cEcuITY5Dvi/4BftMCZwm/dnhEgQJACIktJSnJwxLVo9dchENPtlsCM9C/Sd2EWpqISSUlmfugZbJBwR5pQ5XeMUqKeXZYpP+HEBj1nS+tMH9u2/IGEwJAfL8mZiZXan/oBKrblAbplNcKWGRVD/3y65042PAEeghahlJMiYquV5DzZajuuT0wbJ5xQuZB01+XnfpFpBJ2dw==";
        String content = "Leedarson RSA test";
        String s = EncryptByRSAPubKey(content, pubKey);
        System.out.println("公钥加密后内容：" + s);
        System.out.println("私钥解密后内容：" + DecryptByRSAPriKey(s, priKey));
        s = EncryptByRSAPriKey(content, priKey);
        System.out.println("私钥加密后内容：" + s);
        System.out.println("公钥解密后内容：" + DecryptByRSAPubKey(s, pubKey));
    }
}
