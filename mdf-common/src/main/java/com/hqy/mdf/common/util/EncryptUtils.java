package com.hqy.mdf.common.util;


import com.hqy.mdf.common.bean.KeyDTO;
import com.hqy.mdf.common.enums.EncryptTypeEnum;
import com.hqy.mdf.common.enums.EncryptionErrorEnum;
import com.hqy.mdf.common.exception.BaseException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author hqy
 */
public class EncryptUtils {

    private static final String INIT_CHECK_DATA = "MDF";

    public static final String SM2_PROVIDER_NAME = "BC";


    static {
        //添加支持国密算法 SM2/SM3/SM4
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void initCheck() throws Exception {
        for (EncryptTypeEnum encryptTypeEnum : EncryptTypeEnum.values()) {
            switch (encryptTypeEnum.getEncryptGroupEnum()) {
                case SYMMETRY:
                    String key = keyGenerateWithSymmetry(encryptTypeEnum.getAlgorithm());
                    String encryptDataWithSymmetry = encryptWithSymmetry(encryptTypeEnum.getAlgorithm(), key, INIT_CHECK_DATA);
                    String decryptDataWithSymmetry = decryptWithSymmetry(encryptTypeEnum.getAlgorithm(), key, encryptDataWithSymmetry);
                    if (!INIT_CHECK_DATA.equals(decryptDataWithSymmetry)) {
                        throw new BaseException(EncryptionErrorEnum.INIT_CHECK_ENCRYPT_ERROR);
                    }
                    break;
                case ASYMMETRY:
                    KeyDTO keyDTO = EncryptUtils.KeyGenerateWithASymmetry(encryptTypeEnum.getAlgorithm());
                    String encryptDataWithASymmetry = encryptWithASymmetry(encryptTypeEnum.getAlgorithm(), keyDTO.getPublicKey(), INIT_CHECK_DATA);
                    String decryptDataWithASymmetry = decryptWithASymmetry(encryptTypeEnum.getAlgorithm(), keyDTO.getPrivateKey(), encryptDataWithASymmetry);
                    if (!INIT_CHECK_DATA.equals(decryptDataWithASymmetry)) {
                        throw new BaseException(EncryptionErrorEnum.INIT_CHECK_ENCRYPT_ERROR);
                    }
                    break;
                case DIGEST:
                    String digestData = digest(encryptTypeEnum.getAlgorithm(), null, INIT_CHECK_DATA);
                    if (digestData == null || digestData.length() <= 0) {
                        throw new BaseException(EncryptionErrorEnum.INIT_CHECK_ENCRYPT_ERROR);
                    }
            }
        }
    }


    /**
     * 对称加密秘钥生成
     * 支持算法： DES(16*4位)、3DES(48*4位)、AES(32*4位)、SM4(32*4位)
     */
    public static String keyGenerateWithSymmetry(String algorithm) throws Exception {
        // 生成密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        return ByteUtils.toHexString(keyBytes);
    }

    /**
     * 加密-对称加密算法
     * 支持算法： DES、3DES、AES、SM4
     */
    public static String encryptWithSymmetry(String algorithm,String keyStr, String data)throws Exception {
        // 转换密钥
        Key key = new SecretKeySpec(ByteUtils.fromHexString(keyStr), algorithm);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 加密
        byte[] result = cipher.doFinal(data.getBytes());
        return ByteUtils.toHexString(result);
    }

    /**
     * 解密-对称加密算法
     * 支持算法： DES、3DES、AES、SM4
     */
    public static String decryptWithSymmetry(String algorithm, String keyStr, String data) throws Exception {
        // 转换密钥
        Key key = new SecretKeySpec(ByteUtils.fromHexString(keyStr), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        byte[] result = cipher.doFinal(ByteUtils.fromHexString(data));
        return new String(result);
    }




    /**
     * 非对称加密秘钥生成
     * 支持算法： RSA(公钥：324*4位,私钥：1266*4位)、SM2（公钥：128*4位，私钥：300*4位）
     */
    public static KeyDTO KeyGenerateWithASymmetry(String algorithm) throws Exception {
        KeyPairGenerator keyPairGenerator = null;
        if (EncryptTypeEnum.ASYMMETRY_SM2.getAlgorithm().equals(algorithm)){
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm,Security.getProvider(SM2_PROVIDER_NAME));
            //239-256之间 越大运算越慢
            keyPairGenerator.initialize(256);
        }else {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            //512以上，越大运算越慢 一般默认1024
            keyPairGenerator.initialize(1024);
        }
        KeyDTO keyDTO = new KeyDTO();
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        keyDTO.setPublicKey(ByteUtils.toHexString(publicKey.getEncoded()));
        keyDTO.setPrivateKey(ByteUtils.toHexString(privateKey.getEncoded()));
        return keyDTO;
    }

    /**
     * 加密-非对称加密算法
     * 支持算法： RSA、SM2
     */
    public static String encryptWithASymmetry(String algorithm,String publicKey, String data) throws Exception {
        // 转换密钥
        KeySpec keySpec = new X509EncodedKeySpec(ByteUtils.fromHexString(publicKey));
        if (EncryptTypeEnum.ASYMMETRY_SM2.getAlgorithm().equals(algorithm)){
            PublicKey key = KeyFactory.getInstance(algorithm, Security.getProvider(SM2_PROVIDER_NAME)).generatePublic(keySpec);
            ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(key);
            CipherParameters pubKeyParameters = new ParametersWithRandom(ecPublicKeyParameters);
            SM2Engine engine = new SM2Engine();
            engine.init(true, pubKeyParameters);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] processBlockData = engine.processBlock(bytes, 0, bytes.length);
            return ByteUtils.toHexString(processBlockData);
        }else {
            PublicKey key = KeyFactory.getInstance(algorithm).generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            // 加密
            byte[] result = cipher.doFinal(bytes);
            return ByteUtils.toHexString(result);
        }

    }

    /**
     * 解密-非对称加密算法
     * 支持算法： RSA、SM2
     */
    public static String decryptWithASymmetry(String algorithm, String privateKey, String data) throws Exception {
        // 转换密钥
        KeySpec keySpec = new PKCS8EncodedKeySpec(ByteUtils.fromHexString(privateKey));
        if (EncryptTypeEnum.ASYMMETRY_SM2.getAlgorithm().equals(algorithm)){
            PrivateKey key = KeyFactory.getInstance(algorithm,Security.getProvider(SM2_PROVIDER_NAME)).generatePrivate(keySpec);
            ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(key);
            SM2Engine engine = new SM2Engine();
            engine.init(false, ecPrivateKeyParameters);
            byte[] bytes = ByteUtils.fromHexString(data);
            byte[] processBlockData = engine.processBlock(bytes, 0, bytes.length);
            return new String(processBlockData);
        }else {
            PrivateKey key = KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = ByteUtils.fromHexString(data);
            // 解密
            byte[] result = cipher.doFinal(bytes);
            return new String(result);
        }
    }

    /**
     * 获取摘要-摘要算法
     * 支持算法： MD5、SHA1、SHA256、SM3
     */
    public static String digest(String algorithm,String salt,String data) throws Exception {
        String saltData = data;
        if (salt != null) {
            saltData = data + salt;
        }
        byte[] bytes = saltData.getBytes();
        //判断是否是SM3
        if (EncryptTypeEnum.DIGEST_SM3.getAlgorithm().equals(algorithm)) {
            SM3Digest digest = new SM3Digest();
            digest.update(bytes, 0, bytes.length);
            byte[] resultBytes = new byte[digest.getDigestSize()];
            digest.doFinal(resultBytes, 0);
            return ByteUtils.toHexString(resultBytes);
        }else {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            //使用srcBytes更新摘要
            messageDigest.update(bytes);
            //完成哈希计算，得到result
            byte[] resultBytes = messageDigest.digest();
            return ByteUtils.toHexString(resultBytes);
        }

    }

    private static class ByteUtils{

        private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        /**
         * Convert a byte array to the corresponding hexstring.
         *
         * @param input the byte array to be converted
         * @return the corresponding hexstring
         */
        public static String toHexString(byte[] input)
        {
            String result = "";
            for (int i = 0; i < input.length; i++)
            {
                result += HEX_CHARS[(input[i] >>> 4) & 0x0f];
                result += HEX_CHARS[(input[i]) & 0x0f];
            }
            return result;
        }

        /**
         * Convert a string containing hexadecimal characters to a byte-array.
         *
         * @param s a hex string
         * @return a byte array with the corresponding value
         */
        public static byte[] fromHexString(String s)
        {
            char[] rawChars = s.toUpperCase().toCharArray();

            int hexChars = 0;
            for (int i = 0; i < rawChars.length; i++)
            {
                if ((rawChars[i] >= '0' && rawChars[i] <= '9')
                        || (rawChars[i] >= 'A' && rawChars[i] <= 'F'))
                {
                    hexChars++;
                }
            }

            byte[] byteString = new byte[(hexChars + 1) >> 1];

            int pos = hexChars & 1;

            for (int i = 0; i < rawChars.length; i++)
            {
                if (rawChars[i] >= '0' && rawChars[i] <= '9')
                {
                    byteString[pos >> 1] <<= 4;
                    byteString[pos >> 1] |= rawChars[i] - '0';
                }
                else if (rawChars[i] >= 'A' && rawChars[i] <= 'F')
                {
                    byteString[pos >> 1] <<= 4;
                    byteString[pos >> 1] |= rawChars[i] - 'A' + 10;
                }
                else
                {
                    continue;
                }
                pos++;
            }

            return byteString;
        }
    }
}
