package org.apache.commons.codec;

public abstract interface StringDecoder
  extends Decoder
{
  public abstract String decode(String paramString)
    throws DecoderException;
}


/* Location:              C:\Users\Admin\Dropbox\TOTP.jar!\org\apache\commons\codec\StringDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */