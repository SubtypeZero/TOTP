/*    */ package org.json;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Iterator;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Property
/*    */ {
/*    */   public static JSONObject toJSONObject(Properties properties)
/*    */     throws JSONException
/*    */   {
/* 44 */     JSONObject jo = new JSONObject();
/* 45 */     if ((properties != null) && (!properties.isEmpty())) {
/* 46 */       Enumeration<?> enumProperties = properties.propertyNames();
/* 47 */       while (enumProperties.hasMoreElements()) {
/* 48 */         String name = (String)enumProperties.nextElement();
/* 49 */         jo.put(name, properties.getProperty(name));
/*    */       }
/*    */     }
/* 52 */     return jo;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Properties toProperties(JSONObject jo)
/*    */     throws JSONException
/*    */   {
/* 62 */     Properties properties = new Properties();
/* 63 */     if (jo != null) {
/* 64 */       Iterator<String> keys = jo.keys();
/* 65 */       while (keys.hasNext()) {
/* 66 */         String name = (String)keys.next();
/* 67 */         properties.put(name, jo.getString(name));
/*    */       }
/*    */     }
/* 70 */     return properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */