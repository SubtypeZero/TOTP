/*    */ package org.apache.commons.codec;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ public class StringEncoderComparator
/*    */   implements Comparator
/*    */ {
/*    */   private final StringEncoder stringEncoder;
/*    */   
/*    */   @Deprecated
/*    */   public StringEncoderComparator()
/*    */   {
/* 48 */     this.stringEncoder = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public StringEncoderComparator(StringEncoder stringEncoder)
/*    */   {
/* 58 */     this.stringEncoder = stringEncoder;
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
/*    */   public int compare(Object o1, Object o2)
/*    */   {
/* 77 */     int compareCode = 0;
/*    */     
/*    */ 
/*    */     try
/*    */     {
/* 82 */       Comparable<Comparable<?>> s1 = (Comparable)this.stringEncoder.encode(o1);
/* 83 */       Comparable<?> s2 = (Comparable)this.stringEncoder.encode(o2);
/* 84 */       compareCode = s1.compareTo(s2);
/*    */     } catch (EncoderException ee) {
/* 86 */       compareCode = 0;
/*    */     }
/* 88 */     return compareCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\apache\commons\codec\StringEncoderComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */