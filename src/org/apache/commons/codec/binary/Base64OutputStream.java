/*    */ package org.apache.commons.codec.binary;
/*    */ 
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Base64OutputStream
/*    */   extends BaseNCodecOutputStream
/*    */ {
/*    */   public Base64OutputStream(OutputStream out)
/*    */   {
/* 56 */     this(out, true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Base64OutputStream(OutputStream out, boolean doEncode)
/*    */   {
/* 69 */     super(out, new Base64(false), doEncode);
/*    */   }
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
/*    */   public Base64OutputStream(OutputStream out, boolean doEncode, int lineLength, byte[] lineSeparator)
/*    */   {
/* 90 */     super(out, new Base64(lineLength, lineSeparator), doEncode);
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\apache\commons\codec\binary\Base64OutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */