package com.hpu.commun.utils;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * DES�����㷨
 * 
 * @author xst
 * 
 */
public class DES {
	/**
	 * ����
	 */
	public static final String ENCODE = "ENCODE";
	/**
	 * ����
	 */
	public static final String DECODE = "DECODE";

	public DES() {
	}

	public static DES getInstance(String key) throws NoSuchPaddingException,
			NoSuchAlgorithmException {
		return getInstance(getKeyByStr(key));
	}

	public static DES getInstance(byte key[]) throws NoSuchPaddingException,
			NoSuchAlgorithmException {
		DES des = new DES();
		if (des.key == null) {
			SecretKeySpec spec = new SecretKeySpec(key, "DES");
			des.key = spec;
		}
		des.cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		return des;
	}

	public byte[] encrypt(byte b[]) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException,
			IllegalStateException {
		byte byteFina[] = null;
		cipher.init(1, key);
		byteFina = cipher.doFinal(b);
		return byteFina;
	}

	public byte[] decrypt(byte b[]) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException,
			IllegalStateException {
		byte byteFina[] = null;
		cipher.init(2, key);
		byteFina = cipher.doFinal(b);
		return byteFina;
	}

	public static byte[] getKeyByStr(String str) {
		byte bRet[] = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			Integer itg = new Integer(16 * getChrInt(str.charAt(2 * i))
					+ getChrInt(str.charAt(2 * i + 1)));
			bRet[i] = itg.byteValue();
		}
		return bRet;
	}

	private static int getChrInt(char chr) {
		int iRet = 0;
		if (chr == "0".charAt(0))
			iRet = 0;
		if (chr == "1".charAt(0))
			iRet = 1;
		if (chr == "2".charAt(0))
			iRet = 2;
		if (chr == "3".charAt(0))
			iRet = 3;
		if (chr == "4".charAt(0))
			iRet = 4;
		if (chr == "5".charAt(0))
			iRet = 5;
		if (chr == "6".charAt(0))
			iRet = 6;
		if (chr == "7".charAt(0))
			iRet = 7;
		if (chr == "8".charAt(0))
			iRet = 8;
		if (chr == "9".charAt(0))
			iRet = 9;
		if (chr == "A".charAt(0))
			iRet = 10;
		if (chr == "B".charAt(0))
			iRet = 11;
		if (chr == "C".charAt(0))
			iRet = 12;
		if (chr == "D".charAt(0))
			iRet = 13;
		if (chr == "E".charAt(0))
			iRet = 14;
		if (chr == "F".charAt(0))
			iRet = 15;
		return iRet;
	}

	private Key key;
	private Cipher cipher;

	/**
	 * @interpret ����base64���ܲ���
	 * @param text
	 * @param keyString
	 * @return String
	 */
	public String encrypt(String text, String keyString) {
		String body = null;
		try {
			DES des = DES.getInstance(keyString);

			byte[] b = des.encrypt(text.getBytes("UTF8"));
			body = new String(Base64.encodeBase64(b));
		} catch (Exception ex) {

		}
		return body;
	}

	/**
	 * @interpret ����base64���н���
	 * @param text
	 * @param keyString
	 * @return String
	 */
	public String decrypt(String text, String keyString) {
		String body = null;
		keyString = format16(keyString);
		try {
			DES des = DES.getInstance(keyString);

			byte[] b = Base64.decodeBase64(text.getBytes());

			body = new String(des.decrypt(b), "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 
	 * @param content
	 *            ����
	 * @param operation
	 *            ���ܻ���� DES.ENCODE OR DES.DECODE
	 * @param key
	 *            ʹ�õ�����Կ:�̶�����
	 * @return
	 */
	public String authcode(String content, String operation, String key) {

		String encontent = null;
		key = format16(key);
		if (operation != null && operation.equals(ENCODE)) {
			encontent = encrypt(content, key);
		} else if (operation != null && operation.equals(DECODE)) {
			encontent = decrypt(content, key);
		}
		return encontent;
	}

	private static String format16(String str) {
		if (str != null && str.length() > 0) {
			if (str.length() < 16) {
				StringBuffer sb = new StringBuffer(str);
				for (int i = 0; i < 16 - str.length(); i++) {
					sb.append("h");
				}
				return sb.toString().trim();
			} else {
				return str.substring(0, 16).trim();
			}
		}
		return null;
	}
}