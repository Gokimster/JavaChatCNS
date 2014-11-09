public class Base64 {
  static byte[] code = new byte[64];
  static int[] decode = new int[128];

  Base64()
  {
    init();
  }

  public static void init() {
    for (int i=0;i<26;i++) code[i] = (byte)('A' + i);
    for (int i=0;i<26;i++) code[26 + i] = (byte)('a' + i);
    for (int i=0;i<10;i++) code[52 + i] = (byte)('0' + i);
    code[62] = (byte)'+'; code[63] = (byte)'/';
    for (int i=0;i<128;i++) decode[i] = -1;
    for (int i=0;i<64;i++) decode[ (int)code[i] ] = i;
  }

  public static byte[] encode(byte[] in) {
    int c, d, e, j = 0, k = 0, end = 0;
    int len = 4 * (in.length / 3);
    if ( in.length % 3 != 0 ) len += 4;
    byte[] out = new byte[len];
    while( end == 0 && k + 3 < out.length ) {
      c = 0; d = 0; e = 0;
      if ( j < in.length )
        if ( in[j] < 0 ) c = 256 + in[j]; else c = in[j];
      else end = 1;
      if ( j + 1 < in.length )
        if ( in[j+1] < 0 ) d = 256 + in[j+1]; else d = in[j+1];
      else end += 1;
      if ( j + 2 < in.length )
        if ( in[j+2] < 0 ) e = 256 + in[j+2]; else e = in[j+2];
      else end += 1;
      out[k] = code[c >>> 2];
      out[k+1] = code[ ( 0x00000003 & c ) << 4 | d >>> 4 ];
      out[k+2] = code[ ( 0x0000000F & d ) << 2 | e >>> 6 ];
      out[k+3] = code[ e & 0x0000003F ];
      if ( end >= 1 ) out[k+3] = (byte)'=';
      if ( end == 2 ) out[k+2] = (byte)'=';
      j += 3; k += 4;
    }
    return out;
  }

  public static byte[] decode(byte[] in) {
    int c = 0, d = 0, e = 0, f = 0, i = 0, k = 0, n = 0;
    if ( in.length % 4 != 0 ) {
      System.err.println("decode(): input size isn't multiple of 4");
      return null;
    }
    int len = 3 * (in.length / 4);
    if ( in[in.length-1] == (byte)'=' ) len--;
    if ( in[in.length-2] == (byte)'=' ) len--;
    byte[] out = new byte[len];
    for (int j=0; j<in.length; j++) {
      f = (int)in[j];
      if ( f >= 0 && f < 128 && (i = decode[f]) != -1 ) {
        if ( n % 4 == 0 ) {
          c = i << 2;
        } else if ( n % 4 == 1 ) {
          c = c | ( i >>> 4 );
          d = ( i & 0x0000000f ) << 4;
        } else if ( n % 4 == 2 ) {
          d = d | ( i >>> 2 );
          e = ( i & 0x00000003 ) << 6;
        } else e = e | i;
        n++;
        if ( n % 4 == 0 ) {
          out[k] = (byte)c;
          out[k+1] = (byte)d;
          out[k+2] = (byte)e;
          k += 3;
        }
      }
    }
    if ( n % 4 == 3 ) {
      out[k] = (byte)c;
      out[k+1] = (byte)d;
    } else if ( n % 4 == 2 )
      out[k] = (byte)c;
    return out;
  }
}