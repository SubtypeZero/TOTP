/*     */ package org.json;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
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
/*     */ public class JSONTokener
/*     */ {
/*     */   private long character;
/*     */   private boolean eof;
/*     */   private long index;
/*     */   private long line;
/*     */   private char previous;
/*     */   private Reader reader;
/*     */   private boolean usePrevious;
/*     */   
/*     */   public JSONTokener(Reader reader)
/*     */   {
/*  58 */     this.reader = (reader.markSupported() ? reader : new BufferedReader(reader));
/*     */     
/*     */ 
/*  61 */     this.eof = false;
/*  62 */     this.usePrevious = false;
/*  63 */     this.previous = '\000';
/*  64 */     this.index = 0L;
/*  65 */     this.character = 1L;
/*  66 */     this.line = 1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONTokener(InputStream inputStream)
/*     */     throws JSONException
/*     */   {
/*  75 */     this(new InputStreamReader(inputStream));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONTokener(String s)
/*     */   {
/*  85 */     this(new StringReader(s));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void back()
/*     */     throws JSONException
/*     */   {
/*  95 */     if ((this.usePrevious) || (this.index <= 0L)) {
/*  96 */       throw new JSONException("Stepping back two steps is not supported");
/*     */     }
/*  98 */     this.index -= 1L;
/*  99 */     this.character -= 1L;
/* 100 */     this.usePrevious = true;
/* 101 */     this.eof = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int dehexchar(char c)
/*     */   {
/* 112 */     if ((c >= '0') && (c <= '9')) {
/* 113 */       return c - '0';
/*     */     }
/* 115 */     if ((c >= 'A') && (c <= 'F')) {
/* 116 */       return c - '7';
/*     */     }
/* 118 */     if ((c >= 'a') && (c <= 'f')) {
/* 119 */       return c - 'W';
/*     */     }
/* 121 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean end() {
/* 125 */     return (this.eof) && (!this.usePrevious);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean more()
/*     */     throws JSONException
/*     */   {
/* 135 */     next();
/* 136 */     if (end()) {
/* 137 */       return false;
/*     */     }
/* 139 */     back();
/* 140 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public char next()
/*     */     throws JSONException
/*     */   {
/*     */     int c;
/*     */     
/*     */     int c;
/*     */     
/* 151 */     if (this.usePrevious) {
/* 152 */       this.usePrevious = false;
/* 153 */       c = this.previous;
/*     */     } else {
/*     */       try {
/* 156 */         c = this.reader.read();
/*     */       } catch (IOException exception) { int c;
/* 158 */         throw new JSONException(exception);
/*     */       }
/*     */       
/* 161 */       if (c <= 0) {
/* 162 */         this.eof = true;
/* 163 */         c = 0;
/*     */       }
/*     */     }
/* 166 */     this.index += 1L;
/* 167 */     if (this.previous == '\r') {
/* 168 */       this.line += 1L;
/* 169 */       this.character = (c == 10 ? 0L : 1L);
/* 170 */     } else if (c == 10) {
/* 171 */       this.line += 1L;
/* 172 */       this.character = 0L;
/*     */     } else {
/* 174 */       this.character += 1L;
/*     */     }
/* 176 */     this.previous = ((char)c);
/* 177 */     return this.previous;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char next(char c)
/*     */     throws JSONException
/*     */   {
/* 189 */     char n = next();
/* 190 */     if (n != c) {
/* 191 */       throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
/*     */     }
/*     */     
/* 194 */     return n;
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
/*     */   public String next(int n)
/*     */     throws JSONException
/*     */   {
/* 208 */     if (n == 0) {
/* 209 */       return "";
/*     */     }
/*     */     
/* 212 */     char[] chars = new char[n];
/* 213 */     int pos = 0;
/*     */     
/* 215 */     while (pos < n) {
/* 216 */       chars[pos] = next();
/* 217 */       if (end()) {
/* 218 */         throw syntaxError("Substring bounds error");
/*     */       }
/* 220 */       pos++;
/*     */     }
/* 222 */     return new String(chars);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public char nextClean()
/*     */     throws JSONException
/*     */   {
/*     */     for (;;)
/*     */     {
/* 233 */       char c = next();
/* 234 */       if ((c == 0) || (c > ' ')) {
/* 235 */         return c;
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
/*     */   public String nextString(char quote)
/*     */     throws JSONException
/*     */   {
/* 254 */     StringBuilder sb = new StringBuilder();
/*     */     for (;;) {
/* 256 */       char c = next();
/* 257 */       switch (c) {
/*     */       case '\000': 
/*     */       case '\n': 
/*     */       case '\r': 
/* 261 */         throw syntaxError("Unterminated string");
/*     */       case '\\': 
/* 263 */         c = next();
/* 264 */         switch (c) {
/*     */         case 'b': 
/* 266 */           sb.append('\b');
/* 267 */           break;
/*     */         case 't': 
/* 269 */           sb.append('\t');
/* 270 */           break;
/*     */         case 'n': 
/* 272 */           sb.append('\n');
/* 273 */           break;
/*     */         case 'f': 
/* 275 */           sb.append('\f');
/* 276 */           break;
/*     */         case 'r': 
/* 278 */           sb.append('\r');
/* 279 */           break;
/*     */         case 'u': 
/* 281 */           sb.append((char)Integer.parseInt(next(4), 16));
/* 282 */           break;
/*     */         case '"': 
/*     */         case '\'': 
/*     */         case '/': 
/*     */         case '\\': 
/* 287 */           sb.append(c);
/* 288 */           break;
/*     */         default: 
/* 290 */           throw syntaxError("Illegal escape.");
/*     */         }
/*     */         break;
/*     */       default: 
/* 294 */         if (c == quote) {
/* 295 */           return sb.toString();
/*     */         }
/* 297 */         sb.append(c);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String nextTo(char delimiter)
/*     */     throws JSONException
/*     */   {
/* 310 */     StringBuilder sb = new StringBuilder();
/*     */     for (;;) {
/* 312 */       char c = next();
/* 313 */       if ((c == delimiter) || (c == 0) || (c == '\n') || (c == '\r')) {
/* 314 */         if (c != 0) {
/* 315 */           back();
/*     */         }
/* 317 */         return sb.toString().trim();
/*     */       }
/* 319 */       sb.append(c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String nextTo(String delimiters)
/*     */     throws JSONException
/*     */   {
/* 332 */     StringBuilder sb = new StringBuilder();
/*     */     for (;;) {
/* 334 */       char c = next();
/* 335 */       if ((delimiters.indexOf(c) >= 0) || (c == 0) || (c == '\n') || (c == '\r'))
/*     */       {
/* 337 */         if (c != 0) {
/* 338 */           back();
/*     */         }
/* 340 */         return sb.toString().trim();
/*     */       }
/* 342 */       sb.append(c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object nextValue()
/*     */     throws JSONException
/*     */   {
/* 355 */     char c = nextClean();
/*     */     
/*     */ 
/* 358 */     switch (c) {
/*     */     case '"': 
/*     */     case '\'': 
/* 361 */       return nextString(c);
/*     */     case '{': 
/* 363 */       back();
/* 364 */       return new JSONObject(this);
/*     */     case '[': 
/* 366 */       back();
/* 367 */       return new JSONArray(this);
/*     */     }
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
/* 379 */     StringBuilder sb = new StringBuilder();
/* 380 */     while ((c >= ' ') && (",:]}/\\\"[{;=#".indexOf(c) < 0)) {
/* 381 */       sb.append(c);
/* 382 */       c = next();
/*     */     }
/* 384 */     back();
/*     */     
/* 386 */     String string = sb.toString().trim();
/* 387 */     if ("".equals(string)) {
/* 388 */       throw syntaxError("Missing value");
/*     */     }
/* 390 */     return JSONObject.stringToValue(string);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public char skipTo(char to)
/*     */     throws JSONException
/*     */   {
/*     */     try
/*     */     {
/* 404 */       long startIndex = this.index;
/* 405 */       long startCharacter = this.character;
/* 406 */       long startLine = this.line;
/* 407 */       this.reader.mark(1000000);
/*     */       char c;
/* 409 */       do { c = next();
/* 410 */         if (c == 0) {
/* 411 */           this.reader.reset();
/* 412 */           this.index = startIndex;
/* 413 */           this.character = startCharacter;
/* 414 */           this.line = startLine;
/* 415 */           return c;
/*     */         }
/* 417 */       } while (c != to);
/*     */     } catch (IOException exception) {
/* 419 */       throw new JSONException(exception); }
/*     */     char c;
/* 421 */     back();
/* 422 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONException syntaxError(String message)
/*     */   {
/* 433 */     return new JSONException(message + toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 443 */     return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\json\JSONTokener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */