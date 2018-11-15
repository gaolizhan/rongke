package com.rongke.web.yibaopay;

import com.yeepay.g3.facade.yop.ca.dto.DigitalEnvelopeDTO;
import com.yeepay.g3.facade.yop.ca.dto.DigitalSignatureDTO;
import com.yeepay.g3.facade.yop.ca.enums.DigestAlgEnum;
import com.yeepay.g3.facade.yop.ca.enums.SymmetricEncryptAlgEnum;
import com.yeepay.g3.facade.yop.ca.exceptions.VerifySignFailedException;
import com.yeepay.g3.frame.yop.ca.rsa.RSA;
import com.yeepay.g3.frame.yop.ca.symmetricencryption.SymmetricEncryption;
import com.yeepay.g3.frame.yop.ca.symmetricencryption.SymmetricEncryptionFactory;
import com.yeepay.g3.frame.yop.ca.utils.Encodes;
import com.yeepay.shade.com.google.common.base.Charsets;
import com.yeepay.shade.org.apache.commons.lang3.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;

public class DigitalEnvelopeUtils {
    public static final String SEPERATOR = "$";

    public DigitalEnvelopeUtils() {
    }

    public static DigitalEnvelopeDTO encrypt(DigitalEnvelopeDTO digitalEnvelopeDTO, PrivateKey privateKey, PublicKey publicKey) {
        String source = digitalEnvelopeDTO.getPlainText();
        byte[] data = source.getBytes(Charsets.UTF_8);
        SymmetricEncryptAlgEnum symmetricEncryptAlg = digitalEnvelopeDTO.getSymmetricEncryptAlg();
        SymmetricEncryption symmetricEncryption = SymmetricEncryptionFactory.getSymmetricEncryption(symmetricEncryptAlg);
        byte[] randomKey = symmetricEncryption.generateRandomKey();
        DigestAlgEnum digestAlg = digitalEnvelopeDTO.getDigestAlg();
        byte[] sign = RSA.sign(data, privateKey, digestAlg);
        String signToBase64 = Encodes.encodeUrlSafeBase64(sign);
        data = (source + "$" + signToBase64).getBytes(Charsets.UTF_8);
        byte[] encryptedData = symmetricEncryption.encrypt(data, randomKey);
        String encryptedDataToBase64 = Encodes.encodeUrlSafeBase64(encryptedData);
        byte[] encryptedRandomKey = RSA.encrypt(randomKey, publicKey);
        String encryptedRandomKeyToBase64 = Encodes.encodeUrlSafeBase64(encryptedRandomKey);
        StringBuilder cipherText = new StringBuilder();
        cipherText.append(encryptedRandomKeyToBase64);
        cipherText.append("$");
        cipherText.append(encryptedDataToBase64);
        cipherText.append("$");
        cipherText.append(symmetricEncryptAlg.getValue());
        cipherText.append("$");
        cipherText.append(digestAlg.getValue());
        digitalEnvelopeDTO.setCipherText(cipherText.toString());
        return digitalEnvelopeDTO;
    }

    public static DigitalEnvelopeDTO decrypt(DigitalEnvelopeDTO digitalEnvelopeDTO, PrivateKey privateKey, PublicKey publicKey) {
        String source = digitalEnvelopeDTO.getCipherText();
        String[] args = source.split("\\$");
        if (args.length != 4) {
            throw new RuntimeException("source invalid : " + source);
        } else {
            String encryptedRandomKeyToBase64 = args[0];
            String encryptedDataToBase64 = args[1];
            SymmetricEncryptAlgEnum symmetricEncryptAlg = SymmetricEncryptAlgEnum.parse(args[2]);
            DigestAlgEnum digestAlg = DigestAlgEnum.parse(args[3]);
            digitalEnvelopeDTO.setSymmetricEncryptAlg(symmetricEncryptAlg);
            SymmetricEncryption symmetricEncryption = SymmetricEncryptionFactory.getSymmetricEncryption(symmetricEncryptAlg);
            digitalEnvelopeDTO.setDigestAlg(digestAlg);
            byte[] randomKey = RSA.decrypt(Encodes.decodeBase64(encryptedRandomKeyToBase64), privateKey);
            byte[] encryptedData = symmetricEncryption.decrypt(Encodes.decodeBase64(encryptedDataToBase64), randomKey);
            String data = new String(encryptedData,Charsets.UTF_8);
            String sourceData = StringUtils.substringBeforeLast(data, "$");
            String signToBase64 = StringUtils.substringAfterLast(data, "$");
            boolean verifySign = RSA.verifySign(sourceData, signToBase64, publicKey, digestAlg);
            if (!verifySign) {
                throw new VerifySignFailedException("verifySign fail!", new Object[0]);
            } else {
                digitalEnvelopeDTO.setPlainText(sourceData);
                return digitalEnvelopeDTO;
            }
        }
    }

    public static DigitalSignatureDTO sign(DigitalSignatureDTO digitalSignatureDTO, PrivateKey privateKey) {
        digitalSignatureDTO.setSignature(sign0(digitalSignatureDTO, privateKey));
        return digitalSignatureDTO;
    }

    public static DigitalSignatureDTO verify(DigitalSignatureDTO digitalSignatureDTO, PublicKey publicKey) {
        verify0(digitalSignatureDTO, publicKey);
        return digitalSignatureDTO;
    }

    public static String sign0(DigitalSignatureDTO digitalSignatureDTO, PrivateKey privateKey) {
        String source = digitalSignatureDTO.getPlainText();
        byte[] data = source.getBytes(Charsets.UTF_8);
        DigestAlgEnum digestAlg = digitalSignatureDTO.getDigestAlg();
        byte[] sign = RSA.sign(data, privateKey, digestAlg);
        String signToBase64 = Encodes.encodeUrlSafeBase64(sign);
        StringBuilder cipherText = new StringBuilder();
        cipherText.append(signToBase64);
        cipherText.append("$");
        cipherText.append(digestAlg.getValue());
        return cipherText.toString();
    }

    public static void verify0(DigitalSignatureDTO digitalSignatureDTO, PublicKey publicKey) {
        String signature = digitalSignatureDTO.getSignature();
        String[] args = signature.split("\\$");
        if (args.length != 2) {
            throw new RuntimeException("signature invalid : " + signature);
        } else {
            String signToBase64 = args[0];
            DigestAlgEnum digestAlg = DigestAlgEnum.parse(args[1]);
            digitalSignatureDTO.setDigestAlg(digestAlg);
            boolean verifySign = RSA.verifySign(digitalSignatureDTO.getPlainText(), signToBase64, publicKey, digestAlg);
            if (!verifySign) {
                throw new VerifySignFailedException("verifySign fail!", new Object[0]);
            }
        }
    }
}
