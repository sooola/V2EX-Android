package com.sola.v2ex_android.util;
import android.content.Context;
import android.content.SharedPreferences;

import com.sola.v2ex_android.ui.base.application.MyApplication;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 * @author
 *
 */
public class SharedPreferencesUtils
{
	private static Context mContext;

	/**
	 * 在MyApplication里初始化，避免再重复传Context
	 * @param context
     */
	public static void init(Context context)
	{
		mContext = context;
	}


	/**
	 * 获取偏好文件
	 * @param spName
	 */
	public static SharedPreferences getSharedPreferences(String spName)
	{
		return mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
	}



	/**
	 * 保存在手机里面的文件名
	 */
	public static final String FILE_NAME = "data";

	public static void setParam(String key, Object object) {
		setParam(MyApplication.getContextObject(), key, object);
	}

	public static Object getParam(String key, Object defaultObject) {
		return getParam(MyApplication.getContextObject(), key, defaultObject);
	}

	public static void remove(String key) {
		remove(MyApplication.getContextObject(), key);
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void setParam(Context context , String key, Object object){

		if (ValidateUtil.isEmpty(object)){
			return;
		}
//		String type = object.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if(object instanceof String){
			editor.putString(key, (String) object);
		}
		else if(object instanceof Integer){
			editor.putInt(key, (Integer) object);
		}
		else if(object instanceof Boolean ){
			editor.putBoolean(key, (Boolean)object);
		}
		else if(object instanceof Float){
			editor.putFloat(key, (Float)object);
		}
		else if(object instanceof Long){
			editor.putLong(key, (Long)object);
		}

		editor.commit();
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object getParam(Context context , String key, Object defaultObject){
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

		if("String".equals(type)){
			return sp.getString(key, (String)defaultObject);
		}
		else if("Integer".equals(type)){
			return sp.getInt(key, (Integer)defaultObject);
		}
		else if("Boolean".equals(type)){
			return sp.getBoolean(key, (Boolean)defaultObject);
		}
		else if("Float".equals(type)){
			return sp.getFloat(key, (Float)defaultObject);
		}
		else if("Long".equals(type)){
			return sp.getLong(key, (Long)defaultObject);
		}

		return null;
	}
	public static boolean getParam(Context context , String key, boolean defaultObject){
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultObject);
	}

	public static void remove(Context context ,String key){
		SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().remove(key).apply();
	}

	public static void clear(){
		SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().clear();
	}

}
