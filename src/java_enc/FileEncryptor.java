package java_enc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor {
	private final static String ALGORITHM = "AES";
	
	public static String decryptFile(String filename, String password) throws Exception, WarningException {
		Key encKey = generateKey(encryptPassword(encryptPassword(password)));
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, encKey);
		File in = new File(filename);
		String decryptedf = "";
		if (in.exists()) {
			try {
				FileInputStream fi = new FileInputStream(filename);
				byte[] read = new byte[(int) in.length()];
				fi.read(read);
				decryptedf = new String(c.doFinal(read));
				fi.close();
			} catch (Exception e) {
				System.out.println("Password incorrect.");
				throw new WarningException();
			} 
		}
		return decryptedf;
	}
	public static byte[] encryptText(String text, String password) throws Exception {
		Key encKey = generateKey(encryptPassword(encryptPassword(password)));
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, encKey);
		byte[] encValue = c.doFinal(text.getBytes("UTF-8"));
		FileOutputStream fo = new FileOutputStream("stringdata.out");
		fo.write(encValue);
		return encValue;
	}
	public static void encryptAndWriteText(String text, String password, String fileout) throws Exception {
		Key encKey = generateKey(encryptPassword(encryptPassword(password)));
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, encKey);
		byte[] encValue = c.doFinal(text.getBytes("UTF-8"));
		FileOutputStream fo = new FileOutputStream(fileout);
		fo.write(encValue);
	}
	private static String encryptPassword(String password)
	{
		String sha1 = "";
		try
		{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static Key generateKey(String keyValue) throws Exception {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		Key key = new SecretKeySpec(Arrays.copyOf(sha.digest(keyValue.getBytes("UTF-8")), 16), ALGORITHM);
		return key;
	}
}
