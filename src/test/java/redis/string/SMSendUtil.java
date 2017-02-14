package redis.string;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SMSendUtil {

	private static byte[] LOCK = new byte[0];
	private static SMSendUtil smSendUtil;
	private static ConcurrentHashMap<Long, Byte[]> lockStore = new ConcurrentHashMap<Long,Byte[]>();
	
	private SMSendUtil(){
		
	}
	
	public static SMSendUtil getInstance(){
		if(smSendUtil != null){
			return smSendUtil;
		}
		
		synchronized (LOCK) {
			if(smSendUtil == null){
				smSendUtil = new SMSendUtil();
			}
		}
		return smSendUtil;
	}
	
	/**
     * 发送登陆验证短信(使用全局锁synchronized避免并发请求)
     */
    public synchronized Map<String, Object> sendSms1(String smsPhone) {
        /** 查询最近一次发送时间 **/
        /** 一分钟时间限制判断 **/
        /** 发送短信 **/
        /** 记录发送日志 */
    	return null;
    }
    
    public Map<String, Object> sendSms2(String smsPhone){
    	synchronized (getPhoneNumberLock(Long.parseLong(smsPhone))) {
    		/** 查询最近一次发送时间 **/
            /** 一分钟时间限制判断 **/
            /** 发送短信 **/
            /** 记录发送日志 */
		}
    	return null;
    }
    
    public static Object getPhoneNumberLock(Long phoneNumber){
    	lockStore.put(phoneNumber, new Byte[]{});
    	Byte[] ret = lockStore.get(phoneNumber);
    	return ret;
    }
}
