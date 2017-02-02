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
/*     */ public class JSONML
/*     */ {
/*     */   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja)
/*     */     throws JSONException
/*     */   {
/*  56 */     String closeTag = null;
/*     */     
/*  58 */     JSONArray newja = null;
/*  59 */     JSONObject newjo = null;
/*     */     
/*  61 */     String tagName = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     for (;;)
/*     */     {
/*  70 */       if (!x.more()) {
/*  71 */         throw x.syntaxError("Bad XML");
/*     */       }
/*  73 */       Object token = x.nextContent();
/*  74 */       if (token == XML.LT) {
/*  75 */         token = x.nextToken();
/*  76 */         if ((token instanceof Character)) {
/*  77 */           if (token == XML.SLASH)
/*     */           {
/*     */ 
/*     */ 
/*  81 */             token = x.nextToken();
/*  82 */             if (!(token instanceof String)) {
/*  83 */               throw new JSONException("Expected a closing name instead of '" + token + "'.");
/*     */             }
/*     */             
/*     */ 
/*  87 */             if (x.nextToken() != XML.GT) {
/*  88 */               throw x.syntaxError("Misshaped close tag");
/*     */             }
/*  90 */             return token; }
/*  91 */           if (token == XML.BANG)
/*     */           {
/*     */ 
/*     */ 
/*  95 */             char c = x.next();
/*  96 */             if (c == '-') {
/*  97 */               if (x.next() == '-') {
/*  98 */                 x.skipPast("-->");
/*     */               } else {
/* 100 */                 x.back();
/*     */               }
/* 102 */             } else if (c == '[') {
/* 103 */               token = x.nextToken();
/* 104 */               if ((token.equals("CDATA")) && (x.next() == '[')) {
/* 105 */                 if (ja != null) {
/* 106 */                   ja.put(x.nextCDATA());
/*     */                 }
/*     */               } else {
/* 109 */                 throw x.syntaxError("Expected 'CDATA['");
/*     */               }
/*     */             } else {
/* 112 */               int i = 1;
/*     */               do {
/* 114 */                 token = x.nextMeta();
/* 115 */                 if (token == null)
/* 116 */                   throw x.syntaxError("Missing '>' after '<!'.");
/* 117 */                 if (token == XML.LT) {
/* 118 */                   i++;
/* 119 */                 } else if (token == XML.GT) {
/* 120 */                   i--;
/*     */                 }
/* 122 */               } while (i > 0);
/*     */             }
/* 124 */           } else if (token == XML.QUEST)
/*     */           {
/*     */ 
/*     */ 
/* 128 */             x.skipPast("?>");
/*     */           } else {
/* 130 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 136 */           if (!(token instanceof String)) {
/* 137 */             throw x.syntaxError("Bad tagName '" + token + "'.");
/*     */           }
/* 139 */           tagName = (String)token;
/* 140 */           newja = new JSONArray();
/* 141 */           newjo = new JSONObject();
/* 142 */           if (arrayForm) {
/* 143 */             newja.put(tagName);
/* 144 */             if (ja != null) {
/* 145 */               ja.put(newja);
/*     */             }
/*     */           } else {
/* 148 */             newjo.put("tagName", tagName);
/* 149 */             if (ja != null) {
/* 150 */               ja.put(newjo);
/*     */             }
/*     */           }
/* 153 */           token = null;
/*     */           for (;;) {
/* 155 */             if (token == null) {
/* 156 */               token = x.nextToken();
/*     */             }
/* 158 */             if (token == null) {
/* 159 */               throw x.syntaxError("Misshaped tag");
/*     */             }
/* 161 */             if (!(token instanceof String)) {
/*     */               break;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 167 */             String attribute = (String)token;
/* 168 */             if ((!arrayForm) && (("tagName".equals(attribute)) || ("childNode".equals(attribute)))) {
/* 169 */               throw x.syntaxError("Reserved attribute.");
/*     */             }
/* 171 */             token = x.nextToken();
/* 172 */             if (token == XML.EQ) {
/* 173 */               token = x.nextToken();
/* 174 */               if (!(token instanceof String)) {
/* 175 */                 throw x.syntaxError("Missing value");
/*     */               }
/* 177 */               newjo.accumulate(attribute, JSONObject.stringToValue((String)token));
/* 178 */               token = null;
/*     */             } else {
/* 180 */               newjo.accumulate(attribute, "");
/*     */             }
/*     */           }
/* 183 */           if ((arrayForm) && (newjo.length() > 0)) {
/* 184 */             newja.put(newjo);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 189 */           if (token == XML.SLASH) {
/* 190 */             if (x.nextToken() != XML.GT) {
/* 191 */               throw x.syntaxError("Misshaped tag");
/*     */             }
/* 193 */             if (ja == null) {
/* 194 */               if (arrayForm) {
/* 195 */                 return newja;
/*     */               }
/* 197 */               return newjo;
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 204 */             if (token != XML.GT) {
/* 205 */               throw x.syntaxError("Misshaped tag");
/*     */             }
/* 207 */             closeTag = (String)parse(x, arrayForm, newja);
/* 208 */             if (closeTag != null) {
/* 209 */               if (!closeTag.equals(tagName)) {
/* 210 */                 throw x.syntaxError("Mismatched '" + tagName + "' and '" + closeTag + "'");
/*     */               }
/*     */               
/* 213 */               tagName = null;
/* 214 */               if ((!arrayForm) && (newja.length() > 0)) {
/* 215 */                 newjo.put("childNodes", newja);
/*     */               }
/* 217 */               if (ja == null) {
/* 218 */                 if (arrayForm) {
/* 219 */                   return newja;
/*     */                 }
/* 221 */                 return newjo;
/*     */               }
/*     */               
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 228 */       else if (ja != null) {
/* 229 */         ja.put((token instanceof String) ? 
/* 230 */           JSONObject.stringToValue((String)token) : token);
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
/*     */   public static JSONArray toJSONArray(String string)
/*     */     throws JSONException
/*     */   {
/* 251 */     return toJSONArray(new XMLTokener(string));
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
/*     */   public static JSONArray toJSONArray(XMLTokener x)
/*     */     throws JSONException
/*     */   {
/* 268 */     return (JSONArray)parse(x, true, null);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x)
/*     */     throws JSONException
/*     */   {
/* 286 */     return (JSONObject)parse(x, false, null);
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
/*     */   public static JSONObject toJSONObject(String string)
/*     */     throws JSONException
/*     */   {
/* 304 */     return toJSONObject(new XMLTokener(string));
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
/*     */   public static String toString(JSONArray ja)
/*     */     throws JSONException
/*     */   {
/* 321 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 327 */     String tagName = ja.getString(0);
/* 328 */     XML.noSpace(tagName);
/* 329 */     tagName = XML.escape(tagName);
/* 330 */     sb.append('<');
/* 331 */     sb.append(tagName);
/*     */     
/* 333 */     Object object = ja.opt(1);
/* 334 */     if ((object instanceof JSONObject)) {
/* 335 */       int i = 2;
/* 336 */       JSONObject jo = (JSONObject)object;
/*     */       
/*     */ 
/*     */ 
/* 340 */       Iterator<String> keys = jo.keys();
/* 341 */       while (keys.hasNext()) {
/* 342 */         String key = (String)keys.next();
/* 343 */         XML.noSpace(key);
/* 344 */         String value = jo.optString(key);
/* 345 */         if (value != null) {
/* 346 */           sb.append(' ');
/* 347 */           sb.append(XML.escape(key));
/* 348 */           sb.append('=');
/* 349 */           sb.append('"');
/* 350 */           sb.append(XML.escape(value));
/* 351 */           sb.append('"');
/*     */         }
/*     */       }
/*     */     }
/* 355 */     int i = 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 360 */     int length = ja.length();
/* 361 */     if (i >= length) {
/* 362 */       sb.append('/');
/* 363 */       sb.append('>');
/*     */     } else {
/* 365 */       sb.append('>');
/*     */       do {
/* 367 */         object = ja.get(i);
/* 368 */         i++;
/* 369 */         if (object != null) {
/* 370 */           if ((object instanceof String)) {
/* 371 */             sb.append(XML.escape(object.toString()));
/* 372 */           } else if ((object instanceof JSONObject)) {
/* 373 */             sb.append(toString((JSONObject)object));
/* 374 */           } else if ((object instanceof JSONArray)) {
/* 375 */             sb.append(toString((JSONArray)object));
/*     */           } else {
/* 377 */             sb.append(object.toString());
/*     */           }
/*     */         }
/* 380 */       } while (i < length);
/* 381 */       sb.append('<');
/* 382 */       sb.append('/');
/* 383 */       sb.append(tagName);
/* 384 */       sb.append('>');
/*     */     }
/* 386 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(JSONObject jo)
/*     */     throws JSONException
/*     */   {
/* 399 */     StringBuilder sb = new StringBuilder();
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
/* 411 */     String tagName = jo.optString("tagName");
/* 412 */     if (tagName == null) {
/* 413 */       return XML.escape(jo.toString());
/*     */     }
/* 415 */     XML.noSpace(tagName);
/* 416 */     tagName = XML.escape(tagName);
/* 417 */     sb.append('<');
/* 418 */     sb.append(tagName);
/*     */     
/*     */ 
/*     */ 
/* 422 */     Iterator<String> keys = jo.keys();
/* 423 */     while (keys.hasNext()) {
/* 424 */       String key = (String)keys.next();
/* 425 */       if ((!"tagName".equals(key)) && (!"childNodes".equals(key))) {
/* 426 */         XML.noSpace(key);
/* 427 */         String value = jo.optString(key);
/* 428 */         if (value != null) {
/* 429 */           sb.append(' ');
/* 430 */           sb.append(XML.escape(key));
/* 431 */           sb.append('=');
/* 432 */           sb.append('"');
/* 433 */           sb.append(XML.escape(value));
/* 434 */           sb.append('"');
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 441 */     JSONArray ja = jo.optJSONArray("childNodes");
/* 442 */     if (ja == null) {
/* 443 */       sb.append('/');
/* 444 */       sb.append('>');
/*     */     } else {
/* 446 */       sb.append('>');
/* 447 */       int length = ja.length();
/* 448 */       for (int i = 0; i < length; i++) {
/* 449 */         Object object = ja.get(i);
/* 450 */         if (object != null) {
/* 451 */           if ((object instanceof String)) {
/* 452 */             sb.append(XML.escape(object.toString()));
/* 453 */           } else if ((object instanceof JSONObject)) {
/* 454 */             sb.append(toString((JSONObject)object));
/* 455 */           } else if ((object instanceof JSONArray)) {
/* 456 */             sb.append(toString((JSONArray)object));
/*     */           } else {
/* 458 */             sb.append(object.toString());
/*     */           }
/*     */         }
/*     */       }
/* 462 */       sb.append('<');
/* 463 */       sb.append('/');
/* 464 */       sb.append(tagName);
/* 465 */       sb.append('>');
/*     */     }
/* 467 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\JSONML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */