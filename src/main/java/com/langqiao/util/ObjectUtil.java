package com.langqiao.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtil {

	/**
	 * 对象转化为byte[]
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] objectToBytes(Object obj) throws Exception{
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(obj);
		byte[] bytes = bo.toByteArray();
		return bytes;
	}
	
	/**
	 * byte[]转化为对象
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static Object bytesToObject(byte[] bytes) throws Exception{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream sin = new ObjectInputStream(bais);
		return sin.readObject();
	}
}
