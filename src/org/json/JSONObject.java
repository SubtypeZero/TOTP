/*      */ package org.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Set;
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
/*      */ public class JSONObject
/*      */ {
/*      */   private final Map<String, Object> map;
/*      */   
/*      */   private static final class Null
/*      */   {
/*      */     protected final Object clone()
/*      */     {
/*  114 */       return this;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object object)
/*      */     {
/*  127 */       return (object == null) || (object == this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String toString()
/*      */     {
/*  136 */       return "null";
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
/*      */ 
/*  151 */   public static final Object NULL = new Null(null);
/*      */   
/*      */ 
/*      */ 
/*      */   public JSONObject()
/*      */   {
/*  157 */     this.map = new HashMap();
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
/*      */   public JSONObject(JSONObject jo, String[] names)
/*      */   {
/*  171 */     this();
/*  172 */     for (int i = 0; i < names.length; i++) {
/*      */       try {
/*  174 */         putOnce(names[i], jo.opt(names[i]));
/*      */       }
/*      */       catch (Exception ignore) {}
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
/*      */   public JSONObject(JSONTokener x)
/*      */     throws JSONException
/*      */   {
/*  190 */     this();
/*      */     
/*      */ 
/*      */ 
/*  194 */     if (x.nextClean() != '{') {
/*  195 */       throw x.syntaxError("A JSONObject text must begin with '{'");
/*      */     }
/*      */     for (;;) {
/*  198 */       char c = x.nextClean();
/*  199 */       switch (c) {
/*      */       case '\000': 
/*  201 */         throw x.syntaxError("A JSONObject text must end with '}'");
/*      */       case '}': 
/*  203 */         return;
/*      */       }
/*  205 */       x.back();
/*  206 */       String key = x.nextValue().toString();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  211 */       c = x.nextClean();
/*  212 */       if (c != ':') {
/*  213 */         throw x.syntaxError("Expected a ':' after a key");
/*      */       }
/*  215 */       putOnce(key, x.nextValue());
/*      */       
/*      */ 
/*      */ 
/*  219 */       switch (x.nextClean()) {
/*      */       case ',': 
/*      */       case ';': 
/*  222 */         if (x.nextClean() == '}') {
/*  223 */           return;
/*      */         }
/*  225 */         x.back();
/*      */       }
/*      */     }
/*  228 */     return;
/*      */     
/*  230 */     throw x.syntaxError("Expected a ',' or '}'");
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
/*      */   public JSONObject(Map<?, ?> map)
/*      */   {
/*  243 */     this.map = new HashMap();
/*  244 */     if (map != null) {
/*  245 */       for (Map.Entry<?, ?> e : map.entrySet()) {
/*  246 */         Object value = e.getValue();
/*  247 */         if (value != null) {
/*  248 */           this.map.put(String.valueOf(e.getKey()), wrap(value));
/*      */         }
/*      */       }
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
/*      */   public JSONObject(Object bean)
/*      */   {
/*  276 */     this();
/*  277 */     populateMap(bean);
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
/*      */   public JSONObject(Object object, String[] names)
/*      */   {
/*  295 */     this();
/*  296 */     Class<?> c = object.getClass();
/*  297 */     for (int i = 0; i < names.length; i++) {
/*  298 */       String name = names[i];
/*      */       try {
/*  300 */         putOpt(name, c.getField(name).get(object));
/*      */       }
/*      */       catch (Exception ignore) {}
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
/*      */   public JSONObject(String source)
/*      */     throws JSONException
/*      */   {
/*  319 */     this(new JSONTokener(source));
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
/*      */   public JSONObject(String baseName, Locale locale)
/*      */     throws JSONException
/*      */   {
/*  333 */     this();
/*  334 */     ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, 
/*  335 */       Thread.currentThread().getContextClassLoader());
/*      */     
/*      */ 
/*      */ 
/*  339 */     Enumeration<String> keys = bundle.getKeys();
/*  340 */     while (keys.hasMoreElements()) {
/*  341 */       Object key = keys.nextElement();
/*  342 */       if (key != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  348 */         String[] path = ((String)key).split("\\.");
/*  349 */         int last = path.length - 1;
/*  350 */         JSONObject target = this;
/*  351 */         for (int i = 0; i < last; i++) {
/*  352 */           String segment = path[i];
/*  353 */           JSONObject nextTarget = target.optJSONObject(segment);
/*  354 */           if (nextTarget == null) {
/*  355 */             nextTarget = new JSONObject();
/*  356 */             target.put(segment, nextTarget);
/*      */           }
/*  358 */           target = nextTarget;
/*      */         }
/*  360 */         target.put(path[last], bundle.getString((String)key));
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject accumulate(String key, Object value)
/*      */     throws JSONException
/*      */   {
/*  385 */     testValidity(value);
/*  386 */     Object object = opt(key);
/*  387 */     if (object == null) {
/*  388 */       put(key, (value instanceof JSONArray) ? new JSONArray()
/*  389 */         .put(value) : value);
/*      */     }
/*  391 */     else if ((object instanceof JSONArray)) {
/*  392 */       ((JSONArray)object).put(value);
/*      */     } else {
/*  394 */       put(key, new JSONArray().put(object).put(value));
/*      */     }
/*  396 */     return this;
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
/*      */   public JSONObject append(String key, Object value)
/*      */     throws JSONException
/*      */   {
/*  415 */     testValidity(value);
/*  416 */     Object object = opt(key);
/*  417 */     if (object == null) {
/*  418 */       put(key, new JSONArray().put(value));
/*  419 */     } else if ((object instanceof JSONArray)) {
/*  420 */       put(key, ((JSONArray)object).put(value));
/*      */     } else {
/*  422 */       throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
/*      */     }
/*      */     
/*  425 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String doubleToString(double d)
/*      */   {
/*  437 */     if ((Double.isInfinite(d)) || (Double.isNaN(d))) {
/*  438 */       return "null";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  443 */     String string = Double.toString(d);
/*  444 */     if ((string.indexOf('.') > 0) && (string.indexOf('e') < 0) && 
/*  445 */       (string.indexOf('E') < 0)) {
/*  446 */       while (string.endsWith("0")) {
/*  447 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  449 */       if (string.endsWith(".")) {
/*  450 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     }
/*  453 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object get(String key)
/*      */     throws JSONException
/*      */   {
/*  466 */     if (key == null) {
/*  467 */       throw new JSONException("Null key.");
/*      */     }
/*  469 */     Object object = opt(key);
/*  470 */     if (object == null) {
/*  471 */       throw new JSONException("JSONObject[" + quote(key) + "] not found.");
/*      */     }
/*  473 */     return object;
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
/*      */   public <E extends Enum<E>> E getEnum(Class<E> clazz, String key)
/*      */     throws JSONException
/*      */   {
/*  489 */     E val = optEnum(clazz, key);
/*  490 */     if (val == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  495 */       throw new JSONException("JSONObject[" + quote(key) + "] is not an enum of type " + quote(clazz.getSimpleName()) + ".");
/*      */     }
/*      */     
/*  498 */     return val;
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
/*      */   public boolean getBoolean(String key)
/*      */     throws JSONException
/*      */   {
/*  512 */     Object object = get(key);
/*  513 */     if (!object.equals(Boolean.FALSE)) { if ((object instanceof String))
/*      */       {
/*  515 */         if (!((String)object).equalsIgnoreCase("false")) {} }
/*  516 */     } else return false;
/*  517 */     if (!object.equals(Boolean.TRUE)) { if ((object instanceof String))
/*      */       {
/*  519 */         if (!((String)object).equalsIgnoreCase("true")) {} }
/*  520 */     } else { return true;
/*      */     }
/*  522 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
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
/*      */   public BigInteger getBigInteger(String key)
/*      */     throws JSONException
/*      */   {
/*  537 */     Object object = get(key);
/*      */     try {
/*  539 */       return new BigInteger(object.toString());
/*      */     } catch (Exception e) {
/*  541 */       throw new JSONException("JSONObject[" + quote(key) + "] could not be converted to BigInteger.");
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
/*      */   public BigDecimal getBigDecimal(String key)
/*      */     throws JSONException
/*      */   {
/*  557 */     Object object = get(key);
/*      */     try {
/*  559 */       return new BigDecimal(object.toString());
/*      */     } catch (Exception e) {
/*  561 */       throw new JSONException("JSONObject[" + quote(key) + "] could not be converted to BigDecimal.");
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
/*      */   public double getDouble(String key)
/*      */     throws JSONException
/*      */   {
/*  577 */     Object object = get(key);
/*      */     try
/*      */     {
/*  580 */       return (object instanceof Number) ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
/*      */     } catch (Exception e) {
/*  582 */       throw new JSONException("JSONObject[" + quote(key) + "] is not a number.");
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
/*      */   public int getInt(String key)
/*      */     throws JSONException
/*      */   {
/*  598 */     Object object = get(key);
/*      */     try
/*      */     {
/*  601 */       return (object instanceof Number) ? ((Number)object).intValue() : Integer.parseInt((String)object);
/*      */     } catch (Exception e) {
/*  603 */       throw new JSONException("JSONObject[" + quote(key) + "] is not an int.");
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
/*      */   public JSONArray getJSONArray(String key)
/*      */     throws JSONException
/*      */   {
/*  618 */     Object object = get(key);
/*  619 */     if ((object instanceof JSONArray)) {
/*  620 */       return (JSONArray)object;
/*      */     }
/*  622 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
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
/*      */   public JSONObject getJSONObject(String key)
/*      */     throws JSONException
/*      */   {
/*  636 */     Object object = get(key);
/*  637 */     if ((object instanceof JSONObject)) {
/*  638 */       return (JSONObject)object;
/*      */     }
/*  640 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
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
/*      */   public long getLong(String key)
/*      */     throws JSONException
/*      */   {
/*  655 */     Object object = get(key);
/*      */     try
/*      */     {
/*  658 */       return (object instanceof Number) ? ((Number)object).longValue() : Long.parseLong((String)object);
/*      */     } catch (Exception e) {
/*  660 */       throw new JSONException("JSONObject[" + quote(key) + "] is not a long.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getNames(JSONObject jo)
/*      */   {
/*  671 */     int length = jo.length();
/*  672 */     if (length == 0) {
/*  673 */       return null;
/*      */     }
/*  675 */     Iterator<String> iterator = jo.keys();
/*  676 */     String[] names = new String[length];
/*  677 */     int i = 0;
/*  678 */     while (iterator.hasNext()) {
/*  679 */       names[i] = ((String)iterator.next());
/*  680 */       i++;
/*      */     }
/*  682 */     return names;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getNames(Object object)
/*      */   {
/*  691 */     if (object == null) {
/*  692 */       return null;
/*      */     }
/*  694 */     Class<?> klass = object.getClass();
/*  695 */     Field[] fields = klass.getFields();
/*  696 */     int length = fields.length;
/*  697 */     if (length == 0) {
/*  698 */       return null;
/*      */     }
/*  700 */     String[] names = new String[length];
/*  701 */     for (int i = 0; i < length; i++) {
/*  702 */       names[i] = fields[i].getName();
/*      */     }
/*  704 */     return names;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString(String key)
/*      */     throws JSONException
/*      */   {
/*  717 */     Object object = get(key);
/*  718 */     if ((object instanceof String)) {
/*  719 */       return (String)object;
/*      */     }
/*  721 */     throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean has(String key)
/*      */   {
/*  732 */     return this.map.containsKey(key);
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
/*      */   public JSONObject increment(String key)
/*      */     throws JSONException
/*      */   {
/*  748 */     Object value = opt(key);
/*  749 */     if (value == null) {
/*  750 */       put(key, 1);
/*  751 */     } else if ((value instanceof BigInteger)) {
/*  752 */       put(key, ((BigInteger)value).add(BigInteger.ONE));
/*  753 */     } else if ((value instanceof BigDecimal)) {
/*  754 */       put(key, ((BigDecimal)value).add(BigDecimal.ONE));
/*  755 */     } else if ((value instanceof Integer)) {
/*  756 */       put(key, ((Integer)value).intValue() + 1);
/*  757 */     } else if ((value instanceof Long)) {
/*  758 */       put(key, ((Long)value).longValue() + 1L);
/*  759 */     } else if ((value instanceof Double)) {
/*  760 */       put(key, ((Double)value).doubleValue() + 1.0D);
/*  761 */     } else if ((value instanceof Float)) {
/*  762 */       put(key, ((Float)value).floatValue() + 1.0F);
/*      */     } else {
/*  764 */       throw new JSONException("Unable to increment [" + quote(key) + "].");
/*      */     }
/*  766 */     return this;
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
/*      */   public boolean isNull(String key)
/*      */   {
/*  779 */     return NULL.equals(opt(key));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<String> keys()
/*      */   {
/*  788 */     return keySet().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> keySet()
/*      */   {
/*  797 */     return this.map.keySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int length()
/*      */   {
/*  806 */     return this.map.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONArray names()
/*      */   {
/*  817 */     JSONArray ja = new JSONArray();
/*  818 */     Iterator<String> keys = keys();
/*  819 */     while (keys.hasNext()) {
/*  820 */       ja.put(keys.next());
/*      */     }
/*  822 */     return ja.length() == 0 ? null : ja;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String numberToString(Number number)
/*      */     throws JSONException
/*      */   {
/*  835 */     if (number == null) {
/*  836 */       throw new JSONException("Null pointer");
/*      */     }
/*  838 */     testValidity(number);
/*      */     
/*      */ 
/*      */ 
/*  842 */     String string = number.toString();
/*  843 */     if ((string.indexOf('.') > 0) && (string.indexOf('e') < 0) && 
/*  844 */       (string.indexOf('E') < 0)) {
/*  845 */       while (string.endsWith("0")) {
/*  846 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  848 */       if (string.endsWith(".")) {
/*  849 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     }
/*  852 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object opt(String key)
/*      */   {
/*  863 */     return key == null ? null : this.map.get(key);
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
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key)
/*      */   {
/*  876 */     return optEnum(clazz, key, null);
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
/*      */   public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  893 */       Object val = opt(key);
/*  894 */       if (NULL.equals(val)) {
/*  895 */         return defaultValue;
/*      */       }
/*  897 */       if (clazz.isAssignableFrom(val.getClass()))
/*      */       {
/*      */ 
/*  900 */         return (Enum)val;
/*      */       }
/*      */       
/*  903 */       return Enum.valueOf(clazz, val.toString());
/*      */     } catch (IllegalArgumentException e) {
/*  905 */       return defaultValue;
/*      */     } catch (NullPointerException e) {}
/*  907 */     return defaultValue;
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
/*      */   public boolean optBoolean(String key)
/*      */   {
/*  920 */     return optBoolean(key, false);
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
/*      */   public boolean optBoolean(String key, boolean defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  936 */       return getBoolean(key);
/*      */     } catch (Exception e) {}
/*  938 */     return defaultValue;
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
/*      */   public double optDouble(String key)
/*      */   {
/*  952 */     return optDouble(key, NaN.0D);
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
/*      */   public BigInteger optBigInteger(String key, BigInteger defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  968 */       return getBigInteger(key);
/*      */     } catch (Exception e) {}
/*  970 */     return defaultValue;
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
/*      */   public BigDecimal optBigDecimal(String key, BigDecimal defaultValue)
/*      */   {
/*      */     try
/*      */     {
/*  987 */       return getBigDecimal(key);
/*      */     } catch (Exception e) {}
/*  989 */     return defaultValue;
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
/*      */   public double optDouble(String key, double defaultValue)
/*      */   {
/*      */     try
/*      */     {
/* 1006 */       return getDouble(key);
/*      */     } catch (Exception e) {}
/* 1008 */     return defaultValue;
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
/*      */   public int optInt(String key)
/*      */   {
/* 1022 */     return optInt(key, 0);
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
/*      */   public int optInt(String key, int defaultValue)
/*      */   {
/*      */     try
/*      */     {
/* 1038 */       return getInt(key);
/*      */     } catch (Exception e) {}
/* 1040 */     return defaultValue;
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
/*      */   public JSONArray optJSONArray(String key)
/*      */   {
/* 1053 */     Object o = opt(key);
/* 1054 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JSONObject optJSONObject(String key)
/*      */   {
/* 1066 */     Object object = opt(key);
/* 1067 */     return (object instanceof JSONObject) ? (JSONObject)object : null;
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
/*      */   public long optLong(String key)
/*      */   {
/* 1080 */     return optLong(key, 0L);
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
/*      */   public long optLong(String key, long defaultValue)
/*      */   {
/*      */     try
/*      */     {
/* 1096 */       return getLong(key);
/*      */     } catch (Exception e) {}
/* 1098 */     return defaultValue;
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
/*      */   public String optString(String key)
/*      */   {
/* 1112 */     return optString(key, "");
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
/*      */   public String optString(String key, String defaultValue)
/*      */   {
/* 1126 */     Object object = opt(key);
/* 1127 */     return NULL.equals(object) ? defaultValue : object.toString();
/*      */   }
/*      */   
/*      */   private void populateMap(Object bean) {
/* 1131 */     Class<?> klass = bean.getClass();
/*      */     
/*      */ 
/*      */ 
/* 1135 */     boolean includeSuperClass = klass.getClassLoader() != null;
/*      */     
/*      */ 
/* 1138 */     Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
/* 1139 */     for (int i = 0; i < methods.length; i++) {
/*      */       try {
/* 1141 */         Method method = methods[i];
/* 1142 */         if (Modifier.isPublic(method.getModifiers())) {
/* 1143 */           String name = method.getName();
/* 1144 */           String key = "";
/* 1145 */           if (name.startsWith("get")) {
/* 1146 */             if (("getClass".equals(name)) || 
/* 1147 */               ("getDeclaringClass".equals(name))) {
/* 1148 */               key = "";
/*      */             } else {
/* 1150 */               key = name.substring(3);
/*      */             }
/* 1152 */           } else if (name.startsWith("is")) {
/* 1153 */             key = name.substring(2);
/*      */           }
/* 1155 */           if ((key.length() > 0) && 
/* 1156 */             (Character.isUpperCase(key.charAt(0))) && 
/* 1157 */             (method.getParameterTypes().length == 0)) {
/* 1158 */             if (key.length() == 1) {
/* 1159 */               key = key.toLowerCase();
/* 1160 */             } else if (!Character.isUpperCase(key.charAt(1)))
/*      */             {
/* 1162 */               key = key.substring(0, 1).toLowerCase() + key.substring(1);
/*      */             }
/*      */             
/* 1165 */             Object result = method.invoke(bean, (Object[])null);
/* 1166 */             if (result != null) {
/* 1167 */               this.map.put(key, wrap(result));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception ignore) {}
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
/*      */   public JSONObject put(String key, boolean value)
/*      */     throws JSONException
/*      */   {
/* 1188 */     put(key, value ? Boolean.TRUE : Boolean.FALSE);
/* 1189 */     return this;
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
/*      */   public JSONObject put(String key, Collection<?> value)
/*      */     throws JSONException
/*      */   {
/* 1204 */     put(key, new JSONArray(value));
/* 1205 */     return this;
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
/*      */   public JSONObject put(String key, double value)
/*      */     throws JSONException
/*      */   {
/* 1220 */     put(key, new Double(value));
/* 1221 */     return this;
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
/*      */   public JSONObject put(String key, int value)
/*      */     throws JSONException
/*      */   {
/* 1236 */     put(key, new Integer(value));
/* 1237 */     return this;
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
/*      */   public JSONObject put(String key, long value)
/*      */     throws JSONException
/*      */   {
/* 1252 */     put(key, new Long(value));
/* 1253 */     return this;
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
/*      */   public JSONObject put(String key, Map<?, ?> value)
/*      */     throws JSONException
/*      */   {
/* 1268 */     put(key, new JSONObject(value));
/* 1269 */     return this;
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
/*      */   public JSONObject put(String key, Object value)
/*      */     throws JSONException
/*      */   {
/* 1287 */     if (key == null) {
/* 1288 */       throw new NullPointerException("Null key.");
/*      */     }
/* 1290 */     if (value != null) {
/* 1291 */       testValidity(value);
/* 1292 */       this.map.put(key, value);
/*      */     } else {
/* 1294 */       remove(key);
/*      */     }
/* 1296 */     return this;
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
/*      */   public JSONObject putOnce(String key, Object value)
/*      */     throws JSONException
/*      */   {
/* 1311 */     if ((key != null) && (value != null)) {
/* 1312 */       if (opt(key) != null) {
/* 1313 */         throw new JSONException("Duplicate key \"" + key + "\"");
/*      */       }
/* 1315 */       put(key, value);
/*      */     }
/* 1317 */     return this;
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
/*      */   public JSONObject putOpt(String key, Object value)
/*      */     throws JSONException
/*      */   {
/* 1335 */     if ((key != null) && (value != null)) {
/* 1336 */       put(key, value);
/*      */     }
/* 1338 */     return this;
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
/*      */   public static String quote(String string)
/*      */   {
/* 1352 */     StringWriter sw = new StringWriter();
/* 1353 */     synchronized (sw.getBuffer()) {
/*      */       try {
/* 1355 */         return quote(string, sw).toString();
/*      */       }
/*      */       catch (IOException ignored) {
/* 1358 */         return "";
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static Writer quote(String string, Writer w) throws IOException {
/* 1364 */     if ((string == null) || (string.length() == 0)) {
/* 1365 */       w.write("\"\"");
/* 1366 */       return w;
/*      */     }
/*      */     
/*      */ 
/* 1370 */     char c = '\000';
/*      */     
/*      */ 
/* 1373 */     int len = string.length();
/*      */     
/* 1375 */     w.write(34);
/* 1376 */     for (int i = 0; i < len; i++) {
/* 1377 */       char b = c;
/* 1378 */       c = string.charAt(i);
/* 1379 */       switch (c) {
/*      */       case '"': 
/*      */       case '\\': 
/* 1382 */         w.write(92);
/* 1383 */         w.write(c);
/* 1384 */         break;
/*      */       case '/': 
/* 1386 */         if (b == '<') {
/* 1387 */           w.write(92);
/*      */         }
/* 1389 */         w.write(c);
/* 1390 */         break;
/*      */       case '\b': 
/* 1392 */         w.write("\\b");
/* 1393 */         break;
/*      */       case '\t': 
/* 1395 */         w.write("\\t");
/* 1396 */         break;
/*      */       case '\n': 
/* 1398 */         w.write("\\n");
/* 1399 */         break;
/*      */       case '\f': 
/* 1401 */         w.write("\\f");
/* 1402 */         break;
/*      */       case '\r': 
/* 1404 */         w.write("\\r");
/* 1405 */         break;
/*      */       default: 
/* 1407 */         if ((c < ' ') || ((c >= '') && (c < ' ')) || ((c >= ' ') && (c < '℀')))
/*      */         {
/* 1409 */           w.write("\\u");
/* 1410 */           String hhhh = Integer.toHexString(c);
/* 1411 */           w.write("0000", 0, 4 - hhhh.length());
/* 1412 */           w.write(hhhh);
/*      */         } else {
/* 1414 */           w.write(c);
/*      */         }
/*      */         break; }
/*      */     }
/* 1418 */     w.write(34);
/* 1419 */     return w;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object remove(String key)
/*      */   {
/* 1431 */     return this.map.remove(key);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean similar(Object other)
/*      */   {
/*      */     try
/*      */     {
/* 1444 */       if (!(other instanceof JSONObject)) {
/* 1445 */         return false;
/*      */       }
/* 1447 */       Set<String> set = keySet();
/* 1448 */       if (!set.equals(((JSONObject)other).keySet())) {
/* 1449 */         return false;
/*      */       }
/* 1451 */       Iterator<String> iterator = set.iterator();
/* 1452 */       while (iterator.hasNext()) {
/* 1453 */         String name = (String)iterator.next();
/* 1454 */         Object valueThis = get(name);
/* 1455 */         Object valueOther = ((JSONObject)other).get(name);
/* 1456 */         if ((valueThis instanceof JSONObject)) {
/* 1457 */           if (!((JSONObject)valueThis).similar(valueOther)) {
/* 1458 */             return false;
/*      */           }
/* 1460 */         } else if ((valueThis instanceof JSONArray)) {
/* 1461 */           if (!((JSONArray)valueThis).similar(valueOther)) {
/* 1462 */             return false;
/*      */           }
/* 1464 */         } else if (!valueThis.equals(valueOther)) {
/* 1465 */           return false;
/*      */         }
/*      */       }
/* 1468 */       return true;
/*      */     } catch (Throwable exception) {}
/* 1470 */     return false;
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
/*      */   public static Object stringToValue(String string)
/*      */   {
/* 1483 */     if (string.equals("")) {
/* 1484 */       return string;
/*      */     }
/* 1486 */     if (string.equalsIgnoreCase("true")) {
/* 1487 */       return Boolean.TRUE;
/*      */     }
/* 1489 */     if (string.equalsIgnoreCase("false")) {
/* 1490 */       return Boolean.FALSE;
/*      */     }
/* 1492 */     if (string.equalsIgnoreCase("null")) {
/* 1493 */       return NULL;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1501 */     char initial = string.charAt(0);
/* 1502 */     if (((initial >= '0') && (initial <= '9')) || (initial == '-')) {
/*      */       try {
/* 1504 */         if ((string.indexOf('.') > -1) || (string.indexOf('e') > -1) || 
/* 1505 */           (string.indexOf('E') > -1) || 
/* 1506 */           ("-0".equals(string))) {
/* 1507 */           Double d = Double.valueOf(string);
/* 1508 */           if ((!d.isInfinite()) && (!d.isNaN())) {
/* 1509 */             return d;
/*      */           }
/*      */         } else {
/* 1512 */           Long myLong = new Long(string);
/* 1513 */           if (string.equals(myLong.toString())) {
/* 1514 */             if (myLong.longValue() == myLong.intValue()) {
/* 1515 */               return Integer.valueOf(myLong.intValue());
/*      */             }
/* 1517 */             return myLong;
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (Exception ignore) {}
/*      */     }
/* 1523 */     return string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void testValidity(Object o)
/*      */     throws JSONException
/*      */   {
/* 1535 */     if (o != null) {
/* 1536 */       if ((o instanceof Double)) {
/* 1537 */         if ((((Double)o).isInfinite()) || (((Double)o).isNaN())) {
/* 1538 */           throw new JSONException("JSON does not allow non-finite numbers.");
/*      */         }
/*      */       }
/* 1541 */       else if (((o instanceof Float)) && (
/* 1542 */         (((Float)o).isInfinite()) || (((Float)o).isNaN()))) {
/* 1543 */         throw new JSONException("JSON does not allow non-finite numbers.");
/*      */       }
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
/*      */ 
/*      */   public JSONArray toJSONArray(JSONArray names)
/*      */     throws JSONException
/*      */   {
/* 1562 */     if ((names == null) || (names.length() == 0)) {
/* 1563 */       return null;
/*      */     }
/* 1565 */     JSONArray ja = new JSONArray();
/* 1566 */     for (int i = 0; i < names.length(); i++) {
/* 1567 */       ja.put(opt(names.getString(i)));
/*      */     }
/* 1569 */     return ja;
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
/*      */   public String toString()
/*      */   {
/*      */     try
/*      */     {
/* 1586 */       return toString(0);
/*      */     } catch (Exception e) {}
/* 1588 */     return null;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public String toString(int indentFactor)
/*      */     throws JSONException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: new 180	java/io/StringWriter
/*      */     //   3: dup
/*      */     //   4: invokespecial 181	java/io/StringWriter:<init>	()V
/*      */     //   7: astore_2
/*      */     //   8: aload_2
/*      */     //   9: invokevirtual 182	java/io/StringWriter:getBuffer	()Ljava/lang/StringBuffer;
/*      */     //   12: dup
/*      */     //   13: astore_3
/*      */     //   14: monitorenter
/*      */     //   15: aload_0
/*      */     //   16: aload_2
/*      */     //   17: iload_1
/*      */     //   18: iconst_0
/*      */     //   19: invokevirtual 215	org/json/JSONObject:write	(Ljava/io/Writer;II)Ljava/io/Writer;
/*      */     //   22: invokevirtual 15	java/lang/Object:toString	()Ljava/lang/String;
/*      */     //   25: aload_3
/*      */     //   26: monitorexit
/*      */     //   27: areturn
/*      */     //   28: astore 4
/*      */     //   30: aload_3
/*      */     //   31: monitorexit
/*      */     //   32: aload 4
/*      */     //   34: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1607	-> byte code offset #0
/*      */     //   Java source line #1608	-> byte code offset #8
/*      */     //   Java source line #1609	-> byte code offset #15
/*      */     //   Java source line #1610	-> byte code offset #28
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	JSONObject
/*      */     //   0	35	1	indentFactor	int
/*      */     //   7	10	2	w	StringWriter
/*      */     //   13	18	3	Ljava/lang/Object;	Object
/*      */     //   28	5	4	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   15	27	28	finally
/*      */     //   28	32	28	finally
/*      */   }
/*      */   
/*      */   public static String valueToString(Object value)
/*      */     throws JSONException
/*      */   {
/* 1638 */     if ((value == null) || (value.equals(null))) {
/* 1639 */       return "null";
/*      */     }
/* 1641 */     if ((value instanceof JSONString))
/*      */     {
/*      */       try {
/* 1644 */         object = ((JSONString)value).toJSONString();
/*      */       } catch (Exception e) { Object object;
/* 1646 */         throw new JSONException(e); }
/*      */       Object object;
/* 1648 */       if ((object instanceof String)) {
/* 1649 */         return (String)object;
/*      */       }
/* 1651 */       throw new JSONException("Bad value from toJSONString: " + object);
/*      */     }
/* 1653 */     if ((value instanceof Number)) {
/* 1654 */       return numberToString((Number)value);
/*      */     }
/* 1656 */     if (((value instanceof Boolean)) || ((value instanceof JSONObject)) || ((value instanceof JSONArray)))
/*      */     {
/* 1658 */       return value.toString();
/*      */     }
/* 1660 */     if ((value instanceof Map)) {
/* 1661 */       Map<?, ?> map = (Map)value;
/* 1662 */       return new JSONObject(map).toString();
/*      */     }
/* 1664 */     if ((value instanceof Collection)) {
/* 1665 */       Collection<?> coll = (Collection)value;
/* 1666 */       return new JSONArray(coll).toString();
/*      */     }
/* 1668 */     if (value.getClass().isArray()) {
/* 1669 */       return new JSONArray(value).toString();
/*      */     }
/* 1671 */     return quote(value.toString());
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
/*      */   public static Object wrap(Object object)
/*      */   {
/*      */     try
/*      */     {
/* 1688 */       if (object == null) {
/* 1689 */         return NULL;
/*      */       }
/* 1691 */       if (((object instanceof JSONObject)) || ((object instanceof JSONArray)) || 
/* 1692 */         (NULL.equals(object)) || ((object instanceof JSONString)) || ((object instanceof Byte)) || ((object instanceof Character)) || ((object instanceof Short)) || ((object instanceof Integer)) || ((object instanceof Long)) || ((object instanceof Boolean)) || ((object instanceof Float)) || ((object instanceof Double)) || ((object instanceof String)) || ((object instanceof BigInteger)) || ((object instanceof BigDecimal)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1699 */         return object;
/*      */       }
/*      */       
/* 1702 */       if ((object instanceof Collection)) {
/* 1703 */         Collection<?> coll = (Collection)object;
/* 1704 */         return new JSONArray(coll);
/*      */       }
/* 1706 */       if (object.getClass().isArray()) {
/* 1707 */         return new JSONArray(object);
/*      */       }
/* 1709 */       if ((object instanceof Map)) {
/* 1710 */         Map<?, ?> map = (Map)object;
/* 1711 */         return new JSONObject(map);
/*      */       }
/* 1713 */       Package objectPackage = object.getClass().getPackage();
/*      */       
/* 1715 */       String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
/* 1716 */       if ((objectPackageName.startsWith("java.")) || 
/* 1717 */         (objectPackageName.startsWith("javax.")) || 
/* 1718 */         (object.getClass().getClassLoader() == null)) {
/* 1719 */         return object.toString();
/*      */       }
/* 1721 */       return new JSONObject(object);
/*      */     } catch (Exception exception) {}
/* 1723 */     return null;
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
/*      */   public Writer write(Writer writer)
/*      */     throws JSONException
/*      */   {
/* 1737 */     return write(writer, 0, 0);
/*      */   }
/*      */   
/*      */   static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException
/*      */   {
/* 1742 */     if ((value == null) || (value.equals(null))) {
/* 1743 */       writer.write("null");
/* 1744 */     } else if ((value instanceof JSONObject)) {
/* 1745 */       ((JSONObject)value).write(writer, indentFactor, indent);
/* 1746 */     } else if ((value instanceof JSONArray)) {
/* 1747 */       ((JSONArray)value).write(writer, indentFactor, indent);
/* 1748 */     } else if ((value instanceof Map)) {
/* 1749 */       Map<?, ?> map = (Map)value;
/* 1750 */       new JSONObject(map).write(writer, indentFactor, indent);
/* 1751 */     } else if ((value instanceof Collection)) {
/* 1752 */       Collection<?> coll = (Collection)value;
/* 1753 */       new JSONArray(coll).write(writer, indentFactor, indent);
/* 1754 */     } else if (value.getClass().isArray()) {
/* 1755 */       new JSONArray(value).write(writer, indentFactor, indent);
/* 1756 */     } else if ((value instanceof Number)) {
/* 1757 */       writer.write(numberToString((Number)value));
/* 1758 */     } else if ((value instanceof Boolean)) {
/* 1759 */       writer.write(value.toString());
/* 1760 */     } else if ((value instanceof JSONString))
/*      */     {
/*      */       try {
/* 1763 */         o = ((JSONString)value).toJSONString();
/*      */       } catch (Exception e) { Object o;
/* 1765 */         throw new JSONException(e); }
/*      */       Object o;
/* 1767 */       writer.write(o != null ? o.toString() : quote(value.toString()));
/*      */     } else {
/* 1769 */       quote(value.toString(), writer);
/*      */     }
/* 1771 */     return writer;
/*      */   }
/*      */   
/*      */   static final void indent(Writer writer, int indent) throws IOException {
/* 1775 */     for (int i = 0; i < indent; i++) {
/* 1776 */       writer.write(32);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public Writer write(Writer writer, int indentFactor, int indent)
/*      */     throws JSONException
/*      */   {
/*      */     try
/*      */     {
/* 1798 */       boolean commanate = false;
/* 1799 */       int length = length();
/* 1800 */       Iterator<String> keys = keys();
/* 1801 */       writer.write(123);
/*      */       
/* 1803 */       if (length == 1) {
/* 1804 */         Object key = keys.next();
/* 1805 */         writer.write(quote(key.toString()));
/* 1806 */         writer.write(58);
/* 1807 */         if (indentFactor > 0) {
/* 1808 */           writer.write(32);
/*      */         }
/* 1810 */         writeValue(writer, this.map.get(key), indentFactor, indent);
/* 1811 */       } else if (length != 0) {
/* 1812 */         int newindent = indent + indentFactor;
/* 1813 */         while (keys.hasNext()) {
/* 1814 */           Object key = keys.next();
/* 1815 */           if (commanate) {
/* 1816 */             writer.write(44);
/*      */           }
/* 1818 */           if (indentFactor > 0) {
/* 1819 */             writer.write(10);
/*      */           }
/* 1821 */           indent(writer, newindent);
/* 1822 */           writer.write(quote(key.toString()));
/* 1823 */           writer.write(58);
/* 1824 */           if (indentFactor > 0) {
/* 1825 */             writer.write(32);
/*      */           }
/* 1827 */           writeValue(writer, this.map.get(key), indentFactor, newindent);
/* 1828 */           commanate = true;
/*      */         }
/* 1830 */         if (indentFactor > 0) {
/* 1831 */           writer.write(10);
/*      */         }
/* 1833 */         indent(writer, indent);
/*      */       }
/* 1835 */       writer.write(125);
/* 1836 */       return writer;
/*      */     } catch (IOException exception) {
/* 1838 */       throw new JSONException(exception);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\JSONObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */