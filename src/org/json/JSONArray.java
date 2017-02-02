/*      */ package org.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Array;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONArray
/*      */   implements Iterable<Object>
/*      */ {
/*      */   private final ArrayList<Object> myArrayList;
/*      */   
/*      */   public JSONArray()
/*      */   {
/*   92 */     this.myArrayList = new ArrayList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(JSONTokener x)
/*      */     throws JSONException
/*      */   {
/*  104 */     this();
/*  105 */     if (x.nextClean() != '[') {
/*  106 */       throw x.syntaxError("A JSONArray text must start with '['");
/*      */     }
/*  108 */     if (x.nextClean() != ']') {
/*  109 */       x.back();
/*      */       for (;;) {
/*  111 */         if (x.nextClean() == ',') {
/*  112 */           x.back();
/*  113 */           this.myArrayList.add(JSONObject.NULL);
/*      */         } else {
/*  115 */           x.back();
/*  116 */           this.myArrayList.add(x.nextValue());
/*      */         }
/*  118 */         switch (x.nextClean()) {
/*      */         case ',': 
/*  120 */           if (x.nextClean() == ']') {
/*  121 */             return;
/*      */           }
/*  123 */           x.back();
/*      */         }
/*      */       }
/*  126 */       return;
/*      */       
/*  128 */       throw x.syntaxError("Expected a ',' or ']'");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(String source)
/*      */     throws JSONException
/*      */   {
/*  145 */     this(new JSONTokener(source));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(Collection<?> collection)
/*      */   {
/*  155 */     this.myArrayList = new ArrayList();
/*  156 */     if (collection != null) {
/*  157 */       for (Object o : collection) {
/*  158 */         this.myArrayList.add(JSONObject.wrap(o));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray(Object array)
/*      */     throws JSONException
/*      */   {
/*  170 */     this();
/*  171 */     if (array.getClass().isArray()) {
/*  172 */       int length = Array.getLength(array);
/*  173 */       for (int i = 0; i < length; i++) {
/*  174 */         put(JSONObject.wrap(Array.get(array, i)));
/*      */       }
/*      */     } else {
/*  177 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public Iterator<Object> iterator()
/*      */   {
/*  184 */     return this.myArrayList.iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object get(int index)
/*      */     throws JSONException
/*      */   {
/*  197 */     Object object = opt(index);
/*  198 */     if (object == null) {
/*  199 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/*  201 */     return object;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getBoolean(int index)
/*      */     throws JSONException
/*      */   {
/*  216 */     Object object = get(index);
/*  217 */     if (!object.equals(Boolean.FALSE)) { if ((object instanceof String))
/*      */       {
/*  219 */         if (!((String)object).equalsIgnoreCase("false")) {} }
/*  220 */     } else return false;
/*  221 */     if (!object.equals(Boolean.TRUE)) { if ((object instanceof String))
/*      */       {
/*  223 */         if (!((String)object).equalsIgnoreCase("true")) {} }
/*  224 */     } else { return true;
/*      */     }
/*  226 */     throw new JSONException("JSONArray[" + index + "] is not a boolean.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDouble(int index)
/*      */     throws JSONException
/*      */   {
/*  240 */     Object object = get(index);
/*      */     try
/*      */     {
/*  243 */       return (object instanceof Number) ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
/*      */     } catch (Exception e) {
/*  245 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <E extends Enum<E>> E getEnum(Class<E> clazz, int index)
/*      */     throws JSONException
/*      */   {
/*  262 */     E val = optEnum(clazz, index);
/*  263 */     if (val == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  268 */       throw new JSONException("JSONObject[" + JSONObject.quote(Integer.toString(index)) + "] is not an enum of type " + JSONObject.quote(clazz.getSimpleName()) + ".");
/*      */     }
/*      */     
/*  271 */     return val;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal getBigDecimal(int index)
/*      */     throws JSONException
/*      */   {
/*  285 */     Object object = get(index);
/*      */     try {
/*  287 */       return new BigDecimal(object.toString());
/*      */     } catch (Exception e) {
/*  289 */       throw new JSONException("JSONArray[" + index + "] could not convert to BigDecimal.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigInteger getBigInteger(int index)
/*      */     throws JSONException
/*      */   {
/*  305 */     Object object = get(index);
/*      */     try {
/*  307 */       return new BigInteger(object.toString());
/*      */     } catch (Exception e) {
/*  309 */       throw new JSONException("JSONArray[" + index + "] could not convert to BigInteger.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getInt(int index)
/*      */     throws JSONException
/*      */   {
/*  324 */     Object object = get(index);
/*      */     try
/*      */     {
/*  327 */       return (object instanceof Number) ? ((Number)object).intValue() : Integer.parseInt((String)object);
/*      */     } catch (Exception e) {
/*  329 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray getJSONArray(int index)
/*      */     throws JSONException
/*      */   {
/*  344 */     Object object = get(index);
/*  345 */     if ((object instanceof JSONArray)) {
/*  346 */       return (JSONArray)object;
/*      */     }
/*  348 */     throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject getJSONObject(int index)
/*      */     throws JSONException
/*      */   {
/*  362 */     Object object = get(index);
/*  363 */     if ((object instanceof JSONObject)) {
/*  364 */       return (JSONObject)object;
/*      */     }
/*  366 */     throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getLong(int index)
/*      */     throws JSONException
/*      */   {
/*  380 */     Object object = get(index);
/*      */     try
/*      */     {
/*  383 */       return (object instanceof Number) ? ((Number)object).longValue() : Long.parseLong((String)object);
/*      */     } catch (Exception e) {
/*  385 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString(int index)
/*      */     throws JSONException
/*      */   {
/*  399 */     Object object = get(index);
/*  400 */     if ((object instanceof String)) {
/*  401 */       return (String)object;
/*      */     }
/*  403 */     throw new JSONException("JSONArray[" + index + "] not a string.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNull(int index)
/*      */   {
/*  414 */     return JSONObject.NULL.equals(opt(index));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String join(String separator)
/*      */     throws JSONException
/*      */   {
/*  429 */     int len = length();
/*  430 */     StringBuilder sb = new StringBuilder();
/*      */     
/*  432 */     for (int i = 0; i < len; i++) {
/*  433 */       if (i > 0) {
/*  434 */         sb.append(separator);
/*      */       }
/*  436 */       sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
/*      */     }
/*  438 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int length()
/*      */   {
/*  447 */     return this.myArrayList.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object opt(int index)
/*      */   {
/*  459 */     return (index < 0) || (index >= length()) ? null : this.myArrayList.get(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean optBoolean(int index)
/*      */   {
/*  472 */     return optBoolean(index, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean optBoolean(int index, boolean defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  488 */       return getBoolean(index);
/*      */     } catch (Exception e) {}
/*  490 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double optDouble(int index)
/*      */   {
/*  504 */     return optDouble(index, NaN.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double optDouble(int index, double defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  520 */       return getDouble(index);
/*      */     } catch (Exception e) {}
/*  522 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int optInt(int index)
/*      */   {
/*  536 */     return optInt(index, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int optInt(int index, int defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  552 */       return getInt(index);
/*      */     } catch (Exception e) {}
/*  554 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index)
/*      */   {
/*  568 */     return optEnum(clazz, index, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, int index, E defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  585 */       Object val = opt(index);
/*  586 */       if (JSONObject.NULL.equals(val)) {
/*  587 */         return defaultValue;
/*      */       }
/*  589 */       if (clazz.isAssignableFrom(val.getClass()))
/*      */       {
/*      */ 
/*  592 */         return (Enum)val;
/*      */       }
/*      */       
/*  595 */       return Enum.valueOf(clazz, val.toString());
/*      */     } catch (IllegalArgumentException e) {
/*  597 */       return defaultValue;
/*      */     } catch (NullPointerException e) {}
/*  599 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigInteger optBigInteger(int index, BigInteger defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  617 */       return getBigInteger(index);
/*      */     } catch (Exception e) {}
/*  619 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal optBigDecimal(int index, BigDecimal defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  636 */       return getBigDecimal(index);
/*      */     } catch (Exception e) {}
/*  638 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray optJSONArray(int index)
/*      */   {
/*  651 */     Object o = opt(index);
/*  652 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject optJSONObject(int index)
/*      */   {
/*  665 */     Object o = opt(index);
/*  666 */     return (o instanceof JSONObject) ? (JSONObject)o : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long optLong(int index)
/*      */   {
/*  679 */     return optLong(index, 0L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long optLong(int index, long defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  695 */       return getLong(index);
/*      */     } catch (Exception e) {}
/*  697 */     return defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String optString(int index)
/*      */   {
/*  711 */     return optString(index, "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String optString(int index, String defaultValue)
/*      */   {
/*  725 */     Object object = opt(index);
/*      */     
/*  727 */     return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(boolean value)
/*      */   {
/*  738 */     put(value ? Boolean.TRUE : Boolean.FALSE);
/*  739 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(Collection<?> value)
/*      */   {
/*  751 */     put(new JSONArray(value));
/*  752 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(double value)
/*      */     throws JSONException
/*      */   {
/*  765 */     Double d = new Double(value);
/*  766 */     JSONObject.testValidity(d);
/*  767 */     put(d);
/*  768 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int value)
/*      */   {
/*  779 */     put(new Integer(value));
/*  780 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(long value)
/*      */   {
/*  791 */     put(new Long(value));
/*  792 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(Map<?, ?> value)
/*      */   {
/*  804 */     put(new JSONObject(value));
/*  805 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(Object value)
/*      */   {
/*  818 */     this.myArrayList.add(value);
/*  819 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, boolean value)
/*      */     throws JSONException
/*      */   {
/*  836 */     put(index, value ? Boolean.TRUE : Boolean.FALSE);
/*  837 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, Collection<?> value)
/*      */     throws JSONException
/*      */   {
/*  853 */     put(index, new JSONArray(value));
/*  854 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, double value)
/*      */     throws JSONException
/*      */   {
/*  871 */     put(index, new Double(value));
/*  872 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, int value)
/*      */     throws JSONException
/*      */   {
/*  889 */     put(index, new Integer(value));
/*  890 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, long value)
/*      */     throws JSONException
/*      */   {
/*  907 */     put(index, new Long(value));
/*  908 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, Map<?, ?> value)
/*      */     throws JSONException
/*      */   {
/*  925 */     put(index, new JSONObject(value));
/*  926 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray put(int index, Object value)
/*      */     throws JSONException
/*      */   {
/*  946 */     JSONObject.testValidity(value);
/*  947 */     if (index < 0) {
/*  948 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*      */     }
/*  950 */     if (index < length()) {
/*  951 */       this.myArrayList.set(index, value);
/*      */     } else {
/*  953 */       while (index != length()) {
/*  954 */         put(JSONObject.NULL);
/*      */       }
/*  956 */       put(value);
/*      */     }
/*  958 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object remove(int index)
/*      */   {
/*  971 */     return (index >= 0) && (index < length()) ? this.myArrayList.remove(index) : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean similar(Object other)
/*      */   {
/*  983 */     if (!(other instanceof JSONArray)) {
/*  984 */       return false;
/*      */     }
/*  986 */     int len = length();
/*  987 */     if (len != ((JSONArray)other).length()) {
/*  988 */       return false;
/*      */     }
/*  990 */     for (int i = 0; i < len; i++) {
/*  991 */       Object valueThis = get(i);
/*  992 */       Object valueOther = ((JSONArray)other).get(i);
/*  993 */       if ((valueThis instanceof JSONObject)) {
/*  994 */         if (!((JSONObject)valueThis).similar(valueOther)) {
/*  995 */           return false;
/*      */         }
/*  997 */       } else if ((valueThis instanceof JSONArray)) {
/*  998 */         if (!((JSONArray)valueThis).similar(valueOther)) {
/*  999 */           return false;
/*      */         }
/* 1001 */       } else if (!valueThis.equals(valueOther)) {
/* 1002 */         return false;
/*      */       }
/*      */     }
/* 1005 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject toJSONObject(JSONArray names)
/*      */     throws JSONException
/*      */   {
/* 1021 */     if ((names == null) || (names.length() == 0) || (length() == 0)) {
/* 1022 */       return null;
/*      */     }
/* 1024 */     JSONObject jo = new JSONObject();
/* 1025 */     for (int i = 0; i < names.length(); i++) {
/* 1026 */       jo.put(names.getString(i), opt(i));
/*      */     }
/* 1028 */     return jo;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 1044 */       return toString(0);
/*      */     } catch (Exception e) {}
/* 1046 */     return null;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String toString(int indentFactor)
/*      */     throws JSONException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: new 117	java/io/StringWriter
/*      */     //   3: dup
/*      */     //   4: invokespecial 118	java/io/StringWriter:<init>	()V
/*      */     //   7: astore_2
/*      */     //   8: aload_2
/*      */     //   9: invokevirtual 119	java/io/StringWriter:getBuffer	()Ljava/lang/StringBuffer;
/*      */     //   12: dup
/*      */     //   13: astore_3
/*      */     //   14: monitorenter
/*      */     //   15: aload_0
/*      */     //   16: aload_2
/*      */     //   17: iload_1
/*      */     //   18: iconst_0
/*      */     //   19: invokevirtual 120	org/json/JSONArray:write	(Ljava/io/Writer;II)Ljava/io/Writer;
/*      */     //   22: invokevirtual 60	java/lang/Object:toString	()Ljava/lang/String;
/*      */     //   25: aload_3
/*      */     //   26: monitorexit
/*      */     //   27: areturn
/*      */     //   28: astore 4
/*      */     //   30: aload_3
/*      */     //   31: monitorexit
/*      */     //   32: aload 4
/*      */     //   34: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1063	-> byte code offset #0
/*      */     //   Java source line #1064	-> byte code offset #8
/*      */     //   Java source line #1065	-> byte code offset #15
/*      */     //   Java source line #1066	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	JSONArray
/*      */     //   0	35	1	indentFactor	int
/*      */     //   7	10	2	sw	java.io.StringWriter
/*      */     //   13	18	3	Ljava/lang/Object;	Object
/*      */     //   28	5	4	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   15	27	28	finally
/*      */     //   28	32	28	finally
/*      */   }
/*      */   
/*      */   public Writer write(Writer writer)
/*      */     throws JSONException
/*      */   {
/* 1079 */     return write(writer, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Writer write(Writer writer, int indentFactor, int indent)
/*      */     throws JSONException
/*      */   {
/*      */     try
/*      */     {
/* 1100 */       boolean commanate = false;
/* 1101 */       int length = length();
/* 1102 */       writer.write(91);
/*      */       
/* 1104 */       if (length == 1) {
/* 1105 */         JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
/*      */       }
/* 1107 */       else if (length != 0) {
/* 1108 */         int newindent = indent + indentFactor;
/*      */         
/* 1110 */         for (int i = 0; i < length; i++) {
/* 1111 */           if (commanate) {
/* 1112 */             writer.write(44);
/*      */           }
/* 1114 */           if (indentFactor > 0) {
/* 1115 */             writer.write(10);
/*      */           }
/* 1117 */           JSONObject.indent(writer, newindent);
/* 1118 */           JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
/*      */           
/* 1120 */           commanate = true;
/*      */         }
/* 1122 */         if (indentFactor > 0) {
/* 1123 */           writer.write(10);
/*      */         }
/* 1125 */         JSONObject.indent(writer, indent);
/*      */       }
/* 1127 */       writer.write(93);
/* 1128 */       return writer;
/*      */     } catch (IOException e) {
/* 1130 */       throw new JSONException(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\JSONArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */