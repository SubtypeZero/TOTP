/*     */ package org.json;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XML
/*     */ {
/*  40 */   public static final Character AMP = Character.valueOf('&');
/*     */   
/*     */ 
/*  43 */   public static final Character APOS = Character.valueOf('\'');
/*     */   
/*     */ 
/*  46 */   public static final Character BANG = Character.valueOf('!');
/*     */   
/*     */ 
/*  49 */   public static final Character EQ = Character.valueOf('=');
/*     */   
/*     */ 
/*  52 */   public static final Character GT = Character.valueOf('>');
/*     */   
/*     */ 
/*  55 */   public static final Character LT = Character.valueOf('<');
/*     */   
/*     */ 
/*  58 */   public static final Character QUEST = Character.valueOf('?');
/*     */   
/*     */ 
/*  61 */   public static final Character QUOT = Character.valueOf('"');
/*     */   
/*     */ 
/*  64 */   public static final Character SLASH = Character.valueOf('/');
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escape(String string)
/*     */   {
/*  81 */     StringBuilder sb = new StringBuilder(string.length());
/*  82 */     int i = 0; for (int length = string.length(); i < length; i++) {
/*  83 */       char c = string.charAt(i);
/*  84 */       switch (c) {
/*     */       case '&': 
/*  86 */         sb.append("&amp;");
/*  87 */         break;
/*     */       case '<': 
/*  89 */         sb.append("&lt;");
/*  90 */         break;
/*     */       case '>': 
/*  92 */         sb.append("&gt;");
/*  93 */         break;
/*     */       case '"': 
/*  95 */         sb.append("&quot;");
/*  96 */         break;
/*     */       case '\'': 
/*  98 */         sb.append("&apos;");
/*  99 */         break;
/*     */       default: 
/* 101 */         sb.append(c);
/*     */       }
/*     */     }
/* 104 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void noSpace(String string)
/*     */     throws JSONException
/*     */   {
/* 116 */     int length = string.length();
/* 117 */     if (length == 0) {
/* 118 */       throw new JSONException("Empty string.");
/*     */     }
/* 120 */     for (int i = 0; i < length; i++) {
/* 121 */       if (Character.isWhitespace(string.charAt(i))) {
/* 122 */         throw new JSONException("'" + string + "' contains a space character.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean parse(XMLTokener x, JSONObject context, String name)
/*     */     throws JSONException
/*     */   {
/* 144 */     JSONObject jsonobject = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */     Object token = x.nextToken();
/*     */     
/*     */ 
/*     */ 
/* 163 */     if (token == BANG) {
/* 164 */       char c = x.next();
/* 165 */       if (c == '-') {
/* 166 */         if (x.next() == '-') {
/* 167 */           x.skipPast("-->");
/* 168 */           return false;
/*     */         }
/* 170 */         x.back();
/* 171 */       } else if (c == '[') {
/* 172 */         token = x.nextToken();
/* 173 */         if (("CDATA".equals(token)) && 
/* 174 */           (x.next() == '[')) {
/* 175 */           String string = x.nextCDATA();
/* 176 */           if (string.length() > 0) {
/* 177 */             context.accumulate("content", string);
/*     */           }
/* 179 */           return false;
/*     */         }
/*     */         
/* 182 */         throw x.syntaxError("Expected 'CDATA['");
/*     */       }
/* 184 */       int i = 1;
/*     */       do {
/* 186 */         token = x.nextMeta();
/* 187 */         if (token == null)
/* 188 */           throw x.syntaxError("Missing '>' after '<!'.");
/* 189 */         if (token == LT) {
/* 190 */           i++;
/* 191 */         } else if (token == GT) {
/* 192 */           i--;
/*     */         }
/* 194 */       } while (i > 0);
/* 195 */       return false; }
/* 196 */     if (token == QUEST)
/*     */     {
/*     */ 
/* 199 */       x.skipPast("?>");
/* 200 */       return false; }
/* 201 */     if (token == SLASH)
/*     */     {
/*     */ 
/*     */ 
/* 205 */       token = x.nextToken();
/* 206 */       if (name == null) {
/* 207 */         throw x.syntaxError("Mismatched close tag " + token);
/*     */       }
/* 209 */       if (!token.equals(name)) {
/* 210 */         throw x.syntaxError("Mismatched " + name + " and " + token);
/*     */       }
/* 212 */       if (x.nextToken() != GT) {
/* 213 */         throw x.syntaxError("Misshaped close tag");
/*     */       }
/* 215 */       return true;
/*     */     }
/* 217 */     if ((token instanceof Character)) {
/* 218 */       throw x.syntaxError("Misshaped tag");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 223 */     String tagName = (String)token;
/* 224 */     token = null;
/* 225 */     jsonobject = new JSONObject();
/*     */     for (;;) {
/* 227 */       if (token == null) {
/* 228 */         token = x.nextToken();
/*     */       }
/*     */       
/*     */ 
/* 232 */       if (!(token instanceof String)) break;
/* 233 */       String string = (String)token;
/* 234 */       token = x.nextToken();
/* 235 */       if (token == EQ) {
/* 236 */         token = x.nextToken();
/* 237 */         if (!(token instanceof String)) {
/* 238 */           throw x.syntaxError("Missing value");
/*     */         }
/* 240 */         jsonobject.accumulate(string, 
/* 241 */           JSONObject.stringToValue((String)token));
/* 242 */         token = null;
/*     */       } else {
/* 244 */         jsonobject.accumulate(string, "");
/*     */       }
/*     */     }
/*     */     
/* 248 */     if (token == SLASH)
/*     */     {
/* 250 */       if (x.nextToken() != GT) {
/* 251 */         throw x.syntaxError("Misshaped tag");
/*     */       }
/* 253 */       if (jsonobject.length() > 0) {
/* 254 */         context.accumulate(tagName, jsonobject);
/*     */       } else {
/* 256 */         context.accumulate(tagName, "");
/*     */       }
/* 258 */       return false;
/*     */     }
/* 260 */     if (token == GT) {
/*     */       do {
/*     */         for (;;) {
/* 263 */           token = x.nextContent();
/* 264 */           if (token == null) {
/* 265 */             if (tagName != null) {
/* 266 */               throw x.syntaxError("Unclosed tag " + tagName);
/*     */             }
/* 268 */             return false; }
/* 269 */           if (!(token instanceof String)) break;
/* 270 */           String string = (String)token;
/* 271 */           if (string.length() > 0) {
/* 272 */             jsonobject.accumulate("content", 
/* 273 */               JSONObject.stringToValue(string));
/*     */           }
/*     */         }
/* 276 */       } while ((token != LT) || 
/*     */       
/* 278 */         (!parse(x, jsonobject, tagName)));
/* 279 */       if (jsonobject.length() == 0) {
/* 280 */         context.accumulate(tagName, "");
/* 281 */       } else if ((jsonobject.length() == 1) && 
/* 282 */         (jsonobject.opt("content") != null)) {
/* 283 */         context.accumulate(tagName, jsonobject
/* 284 */           .opt("content"));
/*     */       } else {
/* 286 */         context.accumulate(tagName, jsonobject);
/*     */       }
/* 288 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 293 */     throw x.syntaxError("Misshaped tag");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Object stringToValue(String string)
/*     */   {
/* 308 */     return JSONObject.stringToValue(string);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/* 328 */     JSONObject jo = new JSONObject();
/* 329 */     XMLTokener x = new XMLTokener(string);
/* 330 */     while ((x.more()) && (x.skipPast("<"))) {
/* 331 */       parse(x, jo, null);
/*     */     }
/* 333 */     return jo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Object object)
/*     */     throws JSONException
/*     */   {
/* 345 */     return toString(object, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Object object, String tagName)
/*     */     throws JSONException
/*     */   {
/* 360 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/*     */ 
/*     */     int i;
/*     */     
/*     */ 
/*     */ 
/* 368 */     if ((object instanceof JSONObject))
/*     */     {
/*     */ 
/* 371 */       if (tagName != null) {
/* 372 */         sb.append('<');
/* 373 */         sb.append(tagName);
/* 374 */         sb.append('>');
/*     */       }
/*     */       
/*     */ 
/* 378 */       JSONObject jo = (JSONObject)object;
/* 379 */       Iterator<String> keys = jo.keys();
/* 380 */       while (keys.hasNext()) {
/* 381 */         String key = (String)keys.next();
/* 382 */         Object value = jo.opt(key);
/* 383 */         if (value == null) {
/* 384 */           value = "";
/* 385 */         } else if (value.getClass().isArray()) {
/* 386 */           value = new JSONArray(value);
/*     */         }
/* 388 */         String string = (value instanceof String) ? (String)value : null;
/*     */         
/*     */ 
/* 391 */         if ("content".equals(key)) {
/* 392 */           if ((value instanceof JSONArray)) {
/* 393 */             JSONArray ja = (JSONArray)value;
/* 394 */             i = 0;
/* 395 */             for (Object val : ja) {
/* 396 */               if (i > 0) {
/* 397 */                 sb.append('\n');
/*     */               }
/* 399 */               sb.append(escape(val.toString()));
/* 400 */               i++;
/*     */             }
/*     */           } else {
/* 403 */             sb.append(escape(value.toString()));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/* 408 */         else if ((value instanceof JSONArray)) {
/* 409 */           JSONArray ja = (JSONArray)value;
/* 410 */           for (Object val : ja) {
/* 411 */             if ((val instanceof JSONArray)) {
/* 412 */               sb.append('<');
/* 413 */               sb.append(key);
/* 414 */               sb.append('>');
/* 415 */               sb.append(toString(val));
/* 416 */               sb.append("</");
/* 417 */               sb.append(key);
/* 418 */               sb.append('>');
/*     */             } else {
/* 420 */               sb.append(toString(val, key));
/*     */             }
/*     */           }
/* 423 */         } else if ("".equals(value)) {
/* 424 */           sb.append('<');
/* 425 */           sb.append(key);
/* 426 */           sb.append("/>");
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 431 */           sb.append(toString(value, key));
/*     */         }
/*     */       }
/* 434 */       if (tagName != null)
/*     */       {
/*     */ 
/* 437 */         sb.append("</");
/* 438 */         sb.append(tagName);
/* 439 */         sb.append('>');
/*     */       }
/* 441 */       return sb.toString();
/*     */     }
/*     */     
/*     */ 
/* 445 */     if (object != null) {
/* 446 */       if (object.getClass().isArray()) {
/* 447 */         object = new JSONArray(object);
/*     */       }
/*     */       
/* 450 */       if ((object instanceof JSONArray)) {
/* 451 */         JSONArray ja = (JSONArray)object;
/* 452 */         for (Object val : ja)
/*     */         {
/*     */ 
/*     */ 
/* 456 */           sb.append(toString(val, tagName == null ? "array" : tagName));
/*     */         }
/* 458 */         return sb.toString();
/*     */       }
/*     */     }
/*     */     
/* 462 */     String string = object == null ? "null" : escape(object.toString());
/*     */     
/* 464 */     return "<" + tagName + ">" + string + "</" + tagName + ">";
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\XML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */