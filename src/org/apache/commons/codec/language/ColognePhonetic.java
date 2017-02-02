/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringEncoder;
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
/*     */ public class ColognePhonetic
/*     */   implements StringEncoder
/*     */ {
/* 185 */   private static final char[] AEIJOUY = { 'A', 'E', 'I', 'J', 'O', 'U', 'Y' };
/* 186 */   private static final char[] SCZ = { 'S', 'C', 'Z' };
/* 187 */   private static final char[] WFPV = { 'W', 'F', 'P', 'V' };
/* 188 */   private static final char[] GKQ = { 'G', 'K', 'Q' };
/* 189 */   private static final char[] CKQ = { 'C', 'K', 'Q' };
/* 190 */   private static final char[] AHKLOQRUX = { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' };
/* 191 */   private static final char[] SZ = { 'S', 'Z' };
/* 192 */   private static final char[] AHOUKQX = { 'A', 'H', 'O', 'U', 'K', 'Q', 'X' };
/* 193 */   private static final char[] TDX = { 'T', 'D', 'X' };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private abstract class CologneBuffer
/*     */   {
/*     */     protected final char[] data;
/*     */     
/*     */ 
/*     */ 
/* 204 */     protected int length = 0;
/*     */     
/*     */     public CologneBuffer(char[] data) {
/* 207 */       this.data = data;
/* 208 */       this.length = data.length;
/*     */     }
/*     */     
/*     */     public CologneBuffer(int buffSize) {
/* 212 */       this.data = new char[buffSize];
/* 213 */       this.length = 0;
/*     */     }
/*     */     
/*     */     protected abstract char[] copyData(int paramInt1, int paramInt2);
/*     */     
/*     */     public int length() {
/* 219 */       return this.length;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 224 */       return new String(copyData(0, this.length));
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneOutputBuffer extends ColognePhonetic.CologneBuffer
/*     */   {
/*     */     public CologneOutputBuffer(int buffSize) {
/* 231 */       super(buffSize);
/*     */     }
/*     */     
/*     */     public void addRight(char chr) {
/* 235 */       this.data[this.length] = chr;
/* 236 */       this.length += 1;
/*     */     }
/*     */     
/*     */     protected char[] copyData(int start, int length)
/*     */     {
/* 241 */       char[] newData = new char[length];
/* 242 */       System.arraycopy(this.data, start, newData, 0, length);
/* 243 */       return newData;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneInputBuffer extends ColognePhonetic.CologneBuffer
/*     */   {
/*     */     public CologneInputBuffer(char[] data) {
/* 250 */       super(data);
/*     */     }
/*     */     
/*     */     public void addLeft(char ch) {
/* 254 */       this.length += 1;
/* 255 */       this.data[getNextPos()] = ch;
/*     */     }
/*     */     
/*     */     protected char[] copyData(int start, int length)
/*     */     {
/* 260 */       char[] newData = new char[length];
/* 261 */       System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
/* 262 */       return newData;
/*     */     }
/*     */     
/*     */     public char getNextChar() {
/* 266 */       return this.data[getNextPos()];
/*     */     }
/*     */     
/*     */     protected int getNextPos() {
/* 270 */       return this.data.length - this.length;
/*     */     }
/*     */     
/*     */     public char removeNext() {
/* 274 */       char ch = getNextChar();
/* 275 */       this.length -= 1;
/* 276 */       return ch;
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
/* 289 */   private static final char[][] PREPROCESS_MAP = { { 'Ä', 'A' }, { 'Ü', 'U' }, { 'Ö', 'O' }, { 'ß', 'S' } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean arrayContains(char[] arr, char key)
/*     */   {
/* 300 */     for (char element : arr) {
/* 301 */       if (element == key) {
/* 302 */         return true;
/*     */       }
/*     */     }
/* 305 */     return false;
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
/*     */   public String colognePhonetic(String text)
/*     */   {
/* 320 */     if (text == null) {
/* 321 */       return null;
/*     */     }
/*     */     
/* 324 */     text = preprocess(text);
/*     */     
/* 326 */     CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
/* 327 */     CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
/*     */     
/*     */ 
/*     */ 
/* 331 */     char lastChar = '-';
/* 332 */     char lastCode = '/';
/*     */     
/*     */ 
/*     */ 
/* 336 */     int rightLength = input.length();
/*     */     
/* 338 */     while (rightLength > 0) {
/* 339 */       char chr = input.removeNext();
/*     */       char nextChar;
/* 341 */       char nextChar; if ((rightLength = input.length()) > 0) {
/* 342 */         nextChar = input.getNextChar();
/*     */       } else
/* 344 */         nextChar = '-';
/*     */       char code;
/*     */       char code;
/* 347 */       if (arrayContains(AEIJOUY, chr)) {
/* 348 */         code = '0';
/* 349 */       } else if ((chr == 'H') || (chr < 'A') || (chr > 'Z')) {
/* 350 */         if (lastCode == '/') {
/*     */           continue;
/*     */         }
/* 353 */         char code = '-'; } else { char code;
/* 354 */         if ((chr == 'B') || ((chr == 'P') && (nextChar != 'H'))) {
/* 355 */           code = '1'; } else { char code;
/* 356 */           if (((chr == 'D') || (chr == 'T')) && (!arrayContains(SCZ, nextChar))) {
/* 357 */             code = '2'; } else { char code;
/* 358 */             if (arrayContains(WFPV, chr)) {
/* 359 */               code = '3'; } else { char code;
/* 360 */               if (arrayContains(GKQ, chr)) {
/* 361 */                 code = '4';
/* 362 */               } else if ((chr == 'X') && (!arrayContains(CKQ, lastChar))) {
/* 363 */                 char code = '4';
/* 364 */                 input.addLeft('S');
/* 365 */                 rightLength++; } else { char code;
/* 366 */                 if ((chr == 'S') || (chr == 'Z')) {
/* 367 */                   code = '8'; } else { char code;
/* 368 */                   if (chr == 'C') { char code;
/* 369 */                     if (lastCode == '/') { char code;
/* 370 */                       if (arrayContains(AHKLOQRUX, nextChar)) {
/* 371 */                         code = '4';
/*     */                       } else
/* 373 */                         code = '8';
/*     */                     } else {
/*     */                       char code;
/* 376 */                       if ((arrayContains(SZ, lastChar)) || (!arrayContains(AHOUKQX, nextChar))) {
/* 377 */                         code = '8';
/*     */                       } else
/* 379 */                         code = '4';
/*     */                     }
/*     */                   } else { char code;
/* 382 */                     if (arrayContains(TDX, chr)) {
/* 383 */                       code = '8'; } else { char code;
/* 384 */                       if (chr == 'R') {
/* 385 */                         code = '7'; } else { char code;
/* 386 */                         if (chr == 'L') {
/* 387 */                           code = '5'; } else { char code;
/* 388 */                           if ((chr == 'M') || (chr == 'N')) {
/* 389 */                             code = '6';
/*     */                           } else
/* 391 */                             code = chr;
/*     */                         }
/*     */                       } } } } } } } } }
/* 394 */       if ((code != '-') && (((lastCode != code) && ((code != '0') || (lastCode == '/'))) || (code < '0') || (code > '8'))) {
/* 395 */         output.addRight(code);
/*     */       }
/*     */       
/* 398 */       lastChar = chr;
/* 399 */       lastCode = code;
/*     */     }
/* 401 */     return output.toString();
/*     */   }
/*     */   
/*     */   public Object encode(Object object) throws EncoderException
/*     */   {
/* 406 */     if (!(object instanceof String)) {
/* 407 */       throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 413 */     return encode((String)object);
/*     */   }
/*     */   
/*     */   public String encode(String text)
/*     */   {
/* 418 */     return colognePhonetic(text);
/*     */   }
/*     */   
/*     */   public boolean isEncodeEqual(String text1, String text2) {
/* 422 */     return colognePhonetic(text1).equals(colognePhonetic(text2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String preprocess(String text)
/*     */   {
/* 429 */     text = text.toUpperCase(Locale.GERMAN);
/*     */     
/* 431 */     char[] chrs = text.toCharArray();
/*     */     
/* 433 */     for (int index = 0; index < chrs.length; index++) {
/* 434 */       if (chrs[index] > 'Z') {
/* 435 */         for (char[] element : PREPROCESS_MAP) {
/* 436 */           if (chrs[index] == element[0]) {
/* 437 */             chrs[index] = element[1];
/* 438 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 443 */     return new String(chrs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\apache\commons\codec\language\ColognePhonetic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */