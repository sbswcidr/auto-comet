package org.auto.comet.example.chat.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class JsonTools {

	/**
	 * Convert object into String
	 *
	 * @param obj
	 *            the object
	 * @return
	 */
	public static String convertString(Object obj) {
		String rel = "";

		if (obj instanceof Object) {
			rel = obj.toString();
		}

		return rel;
	}

	public static String nullToEmpty(Object obj) {
		String rel = "";

		if (obj instanceof Object) {
			rel = obj.toString();
		}

		return rel;
	}


	/**
	 *
	 * @param name
	 * @return
	 */
	public static String toFirstLetterUpperCase(String name){
		String rel = nullToEmpty(name);
		if(rel.length()>0){
			rel = nullToEmpty(name).substring(0,1).toUpperCase()+nullToEmpty(name).substring(1);
		}
		return rel;
	}

	/**
	 *
	 * @param t
	 */
	private static java.util.Map<String, Object> parseParameterizedTypeForListAndMap(Type t){
		if(t == null) return null;
		if(t instanceof Class){
			java.util.Map<String, Object> rel = new java.util.HashMap<String, Object>();
			rel.put("class", (Class<?>)t);
			return rel;
	    }
		else if(t instanceof java.lang.reflect.GenericArrayType){
			return null;
	    }
		else if(t instanceof java.lang.reflect.ParameterizedType){
	    	ParameterizedType pt = (ParameterizedType) t;
	    	java.util.Map<String, Object> rel = new java.util.HashMap<String, Object>();
			rel.put("class", (Class<?>)pt.getRawType());

	    	java.util.List<Object> childLst = new java.util.ArrayList<Object>();
	    	//System.out.println("ParameterizedType :"+(Class<?>)pt.getRawType()+", pt.getRawType():"+java.util.Map.class.isAssignableFrom((Class<?>)pt.getRawType()));
            Type[] childTypeArr = pt.getActualTypeArguments();
            if(childTypeArr!=null && childTypeArr.length>0){
         	   for(int i=0; i<childTypeArr.length; i++){
         		  java.util.Map<String, ?> map = parseParameterizedTypeForListAndMap(childTypeArr[i]);
         		  if(map!=null){
         			 childLst.add(map);
         		  }
         	   }
            }
            if(!childLst.isEmpty()){
            	rel.put("children", childLst);
            }

            return rel;
	    }
	    else if(t instanceof java.lang.reflect.TypeVariable){
	    	return null;
	    }
	    else if(t instanceof java.lang.reflect.WildcardType){
	    	return null;
	    }
	    else{
	    	return null;
	    }
	}

	/**
	 * The basic field type would be ignore.
	 *
	 * @param objClass
	 * @return
	 */
	private static java.util.Map<String, Object> parseParameterizedTypeForObject(Class<?> objClass){
		java.util.Map<String, Object> parameterizedLst = new java.util.HashMap<String,Object>();

		Field[] fs = objClass.getDeclaredFields();
		for(Field f : fs) {
		    Class<?> fieldClazz = f.getType();

		    if(fieldClazz.isPrimitive())  continue;

		    if(fieldClazz.getName().startsWith("java.lang")){
		    	parameterizedLst.put(f.getName(), fieldClazz);
		    }
		    //Only prepared for List&Map generic type for now
		    else if(java.util.List.class.isAssignableFrom(fieldClazz) || java.util.Map.class.isAssignableFrom(fieldClazz)) {
		    	parameterizedLst.put(f.getName(), parseParameterizedTypeForListAndMap(f.getGenericType()));
		    }
		    else {
		    	parameterizedLst.put(f.getName(), fieldClazz);
		    }
		}

		return parameterizedLst;
	}

	/**
	 * Format String into defined type.
	 *
	 * @param <T>
	 * @param objClass
	 * @param val
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static <T> T getObjWithString(Class<T> objClass, String val) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		T rel = null;
		if(objClass.getConstructor(String.class)!=null){
			rel = objClass.getConstructor(String.class).newInstance(val);
		}
		return rel;
	}
	/**
	 * Format String into defined type.
	 *
	 * @param <T>
	 * @param objClass
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public static java.util.List getObjWithJSONArrayForList(Class<?> objClass, JSONArray json, java.util.Map<String, ?> parameterizeMap) throws Exception{
		java.util.List rel = null;

		Class<?> parameterizeClass = (Class<?>)parameterizeMap.get("class");
		if(parameterizeClass.isAssignableFrom(objClass) && java.util.List.class.isAssignableFrom(objClass)){
			java.util.List<java.util.Map<String, ?>> childLst = (java.util.List<java.util.Map<String, ?>>)parameterizeMap.get("children");
			if(childLst!=null && childLst.size() == 1){
				java.util.Map<String, ?> childMap = childLst.get(0);
				Class<?> subChildClass = (Class<?>)childMap.get("class");

				try {
					rel = new java.util.ArrayList();
				} catch (Exception e) {
					throw e;
				}
				java.util.Iterator jsonIterator = json.iterator();
				while(jsonIterator.hasNext()){
					Object fieldObj = jsonIterator.next();

					Object valueObj = null;

					if(java.util.Map.class.isAssignableFrom(subChildClass)){
						valueObj = getObjWithJSONObjectForMap(subChildClass, (JSONObject)fieldObj, childMap);
					}
					else if(java.util.List.class.isAssignableFrom(subChildClass)){
						valueObj = getObjWithJSONArrayForList(subChildClass, (JSONArray)fieldObj, childMap);
					}
					else if(subChildClass.getName().startsWith("java.lang")){
						valueObj = getObjWithString(subChildClass, fieldObj.toString());
					}
					else {
						valueObj = getObjWithJson(subChildClass, (JSONObject)fieldObj);
					}
					rel.add(valueObj);
				}
			}
		}

		return rel;
	}
	/**
	 * Format String into defined type.
	 *
	 * @param <T>
	 * @param objClass
	 * @param val
	 * @return
	 * @throws Exception
	 */
	public static java.util.Map getObjWithJSONObjectForMap(Class<?> objClass, JSONObject json, java.util.Map<String, ?> parameterizeMap) throws Exception{
		java.util.Map rel = null;
		Class<?> parameterizeClass = (Class<?>)parameterizeMap.get("class");
		if(parameterizeClass.isAssignableFrom(objClass) && java.util.Map.class.isAssignableFrom(objClass)){
			java.util.List<java.util.Map<String, ?>> childLst = (java.util.List<java.util.Map<String, ?>>)parameterizeMap.get("children");
			if(childLst!=null && childLst.size() == 2){
				java.util.Map<String, ?> subKeyMap = childLst.get(0);
				java.util.Map<String, ?> subChildMap = childLst.get(1);

				Class<?> keyClass   = (Class<?>)subKeyMap.get("class");
				Class<?> valueClass = (Class<?>)subChildMap.get("class");


				try {
					rel = new java.util.HashMap();
				} catch (Exception e) {
					throw e;
				}
				java.util.Iterator<String> keys = json.keys();
				while(keys.hasNext()){
					String key = keys.next();
					Object fieldObj = json.opt(key);

					Object keyObj = getObjWithString(keyClass, key);
					Object valueObj = null;

					if(java.util.Map.class.isAssignableFrom(valueClass)){
						valueObj = getObjWithJSONObjectForMap(valueClass, (JSONObject)fieldObj, subChildMap);
					}
					else if(java.util.List.class.isAssignableFrom(valueClass)){
						valueObj = getObjWithJSONArrayForList(valueClass, (JSONArray)fieldObj, subChildMap);
					}
					else if(valueClass.getName().startsWith("java.lang")){
						valueObj = getObjWithString(valueClass, fieldObj.toString());
					}
					else {
						valueObj = getObjWithJson(valueClass, (JSONObject)fieldObj);
					}
					rel.put(keyObj, valueObj);
				}
			}
		}


		return rel;
	}


	/**
	 * Handle simplely way convert.
	 *
	 * @param <T>
	 * @param objClass
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	private static <T> T getObjWithSimpleType(Class<T> objClass, Object json) throws JSONException{
		T rel = null;
		if(java.util.List.class.isAssignableFrom(objClass)){
			java.util.List relLst = new java.util.ArrayList();
			JSONArray jsonArr = (JSONArray)json;
			for(int i=0; i<jsonArr.size(); i++){
				Object subJson = jsonArr.get(i);

				Object valueObj = null;

				if(subJson instanceof JSONArray){
					valueObj = getObjWithSimpleType(java.util.List.class, subJson);
				}
				else if(subJson instanceof JSONObject){
					valueObj = getObjWithSimpleType(java.util.Map.class, subJson);
				}
				else {
					valueObj = subJson.toString();
				}
				relLst.add(valueObj);
			}
			rel = (T)relLst;
		}
		else if(java.util.Map.class.isAssignableFrom(objClass)){
			java.util.Map relMap = new java.util.HashMap();
			JSONObject jsonObj = (JSONObject)json;
			java.util.Iterator<String> keys = ((JSONObject)json).keys();
			while(keys.hasNext()){
				String key = keys.next();
				Object subJson = jsonObj.opt(key);

				Object valueObj = null;

				if(subJson instanceof JSONArray){
					valueObj = getObjWithSimpleType(java.util.List.class, subJson);
				}
				else if(subJson instanceof JSONObject){
					valueObj = getObjWithSimpleType(java.util.Map.class, subJson);
				}
				else {
					valueObj = subJson.toString();
				}
				relMap.put(key, valueObj);
			}
			rel = (T)relMap;
		}
		return rel;
	}


	/**
	 * Generate object with json object and specified Class name.
	 *
	 * @param <T>
	 * @param objClass
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static <T> T getObjWithJson(Class<T> objClass, Object json ) throws Exception{
		T rel = null;
		if(java.util.List.class.isAssignableFrom(objClass) || java.util.Map.class.isAssignableFrom(objClass)){
			rel = getObjWithSimpleType(objClass, json);
		}
		else {
			java.util.Map<String, Object> fieldMap = parseParameterizedTypeForObject(objClass);

			try {
				rel = objClass.getConstructor().newInstance();
			} catch (Exception e) {
				throw e;
			}
			java.util.Iterator<String> keys = ((JSONObject)json).keys();
			while(keys.hasNext()){
				String key = keys.next();
				Object fieldObj = ((JSONObject)json).opt(key);

				try{
					String setMethodName = "set" + toFirstLetterUpperCase(key);
					String getMethodName = "get" + toFirstLetterUpperCase(key);
					Class<?>[] parameters = objClass.getMethod(setMethodName, objClass.getMethod(getMethodName).getReturnType()).getParameterTypes();
					if(parameters!=null && parameters.length == 1){
						Class<?> paraClass = parameters[0];
						Object obj = null;
						//Basic type
						if(paraClass.isPrimitive()){
							throw new Exception("The primitive type is not supposed to be here!");
						}
						else if(fieldObj.getClass().isAssignableFrom(paraClass)){
							obj = fieldObj;
						}
						else if( paraClass.getName().startsWith("java.lang")){
							obj = getObjWithString(parameters[0], fieldObj.toString());
						}
						//TODO:
						else if(java.util.Map.class.isAssignableFrom(paraClass)){
							obj = getObjWithJSONObjectForMap(java.util.Map.class, (JSONObject)fieldObj, (java.util.Map<String, ?>)fieldMap.get(key) );
						}
						else if(java.util.List.class.isAssignableFrom(paraClass)){
							obj = getObjWithJSONArrayForList(paraClass, (JSONArray)fieldObj, (java.util.Map<String, ?>)fieldMap.get(key) );
						}
						else {
							obj = getObjWithJson(paraClass, (JSONObject)fieldObj );
						}
						objClass.getMethod(setMethodName, parameters[0]).invoke(rel, obj);
					}
				}catch(Exception e){
					throw e;
				}
			}
		}
		return rel;
	}


	/**
	 * Generate object from json string.
	 *
	 * @param <T>
	 * @param objClass
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public static <T> T parseJsonString(Class<T> objClass, String jsonString) throws Exception {
		T rel = null;
		Object json = null;
		try {
			json = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			json = JSONArray.fromObject(jsonString);
		}
		rel = getObjWithJson(objClass, json);
		return rel;
	}
}
