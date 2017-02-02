/*    */ package org.json;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CookieList
/*    */ {
/*    */   public static JSONObject toJSONObject(String string)
/*    */     throws JSONException
/*    */   {
/* 50 */     JSONObject jo = new JSONObject();
/* 51 */     JSONTokener x = new JSONTokener(string);
/* 52 */     while (x.more()) {
/* 53 */       String name = Cookie.unescape(x.nextTo('='));
/* 54 */       x.next('=');
/* 55 */       jo.put(name, Cookie.unescape(x.nextTo(';')));
/* 56 */       x.next();
/*    */     }
/* 58 */     return jo;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String toString(JSONObject jo)
/*    */     throws JSONException
/*    */   {
/* 71 */     boolean b = false;
/* 72 */     Iterator<String> keys = jo.keys();
/*    */     
/* 74 */     StringBuilder sb = new StringBuilder();
/* 75 */     while (keys.hasNext()) {
/* 76 */       String string = (String)keys.next();
/* 77 */       if (!jo.isNull(string)) {
/* 78 */         if (b) {
/* 79 */           sb.append(';');
/*    */         }
/* 81 */         sb.append(Cookie.escape(string));
/* 82 */         sb.append("=");
/* 83 */         sb.append(Cookie.escape(jo.getString(string)));
/* 84 */         b = true;
/*    */       }
/*    */     }
/* 87 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\CookieList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */