package com.iot.shcs.helper;

import java.util.HashMap;
import java.util.Locale;

public class Color
{
  public static final int BLACK = -16777216;
  public static final int DKGRAY = -12303292;
  public static final int GRAY = -7829368;
  public static final int LTGRAY = -3355444;
  public static final int WHITE = -1;
  public static final int RED = -65536;
  public static final int GREEN = -16711936;
  public static final int BLUE = -16776961;
  public static final int YELLOW = -256;
  public static final int CYAN = -16711681;
  public static final int MAGENTA = -65281;
  public static final int TRANSPARENT = 0;
  
  public static int alpha(int color)
  {
    return color >>> 24;
  }
  
  public static int red(int color)
  {
    return color >> 16 & 0xFF;
  }
  
  public static int green(int color)
  {
    return color >> 8 & 0xFF;
  }
  
  public static int blue(int color)
  {
    return color & 0xFF;
  }
  
  public static int rgb(int red, int green, int blue)
  {
    return 0xFF000000 | red << 16 | green << 8 | blue;
  }
  
  public static int argb(int alpha, int red, int green, int blue)
  {
    return alpha << 24 | red << 16 | green << 8 | blue;
  }
  
  public static float hue(int color)
  {
    int r = color >> 16 & 0xFF;
    int g = color >> 8 & 0xFF;
    int b = color & 0xFF;
    
    int V = Math.max(b, Math.max(r, g));
    int temp = Math.min(b, Math.min(r, g));
    float H;
    if (V == temp)
    {
      H = 0.0F;
    }
    else
    {
      float vtemp = V - temp;
      float cr = (V - r) / vtemp;
      float cg = (V - g) / vtemp;
      float cb = (V - b) / vtemp;
      if (r == V)
      {
        H = cb - cg;
      }
      else
      {
        if (g == V) {
          H = 2.0F + cr - cb;
        } else {
          H = 4.0F + cg - cr;
        }
      }
      H /= 6.0F;
      if (H < 0.0F) {
        H += 1.0F;
      }
    }
    return H;
  }
  
  public static float saturation(int color)
  {
    int r = color >> 16 & 0xFF;
    int g = color >> 8 & 0xFF;
    int b = color & 0xFF;
    

    int V = Math.max(b, Math.max(r, g));
    int temp = Math.min(b, Math.min(r, g));
    float S;
    if (V == temp) {
      S = 0.0F;
    } else {
      S = (V - temp) / V;
    }
    return S;
  }
  
  public static float brightness(int color)
  {
    int r = color >> 16 & 0xFF;
    int g = color >> 8 & 0xFF;
    int b = color & 0xFF;
    
    int V = Math.max(b, Math.max(r, g));
    
    return V / 255.0F;
  }
  
  public static int parseColor(String colorString)
  {
    if (colorString.charAt(0) == '#')
    {
      long color = Long.parseLong(colorString.substring(1), 16);
      if (colorString.length() == 7) {
        color |= 0xFF000000;
      } else if (colorString.length() != 9) {
        throw new IllegalArgumentException("Unknown color");
      }
      return (int)color;
    }
    Integer color = (Integer)sColorNameMap.get(colorString.toLowerCase(Locale.US));
    if (color != null) {
      return color.intValue();
    }
    throw new IllegalArgumentException("Unknown color");
  }
  
  private static final HashMap<String, Integer> sColorNameMap = new HashMap();
  
  static
  {
    sColorNameMap.put("black", Integer.valueOf(-16777216));
    sColorNameMap.put("darkgray", Integer.valueOf(-12303292));
    sColorNameMap.put("gray", Integer.valueOf(-7829368));
    sColorNameMap.put("lightgray", Integer.valueOf(-3355444));
    sColorNameMap.put("white", Integer.valueOf(-1));
    sColorNameMap.put("red", Integer.valueOf(-65536));
    sColorNameMap.put("green", Integer.valueOf(-16711936));
    sColorNameMap.put("blue", Integer.valueOf(-16776961));
    sColorNameMap.put("yellow", Integer.valueOf(-256));
    sColorNameMap.put("cyan", Integer.valueOf(-16711681));
    sColorNameMap.put("magenta", Integer.valueOf(-65281));
    sColorNameMap.put("aqua", Integer.valueOf(65535));
    sColorNameMap.put("fuchsia", Integer.valueOf(16711935));
    sColorNameMap.put("darkgrey", Integer.valueOf(-12303292));
    sColorNameMap.put("grey", Integer.valueOf(-7829368));
    sColorNameMap.put("lightgrey", Integer.valueOf(-3355444));
    sColorNameMap.put("lime", Integer.valueOf(65280));
    sColorNameMap.put("maroon", Integer.valueOf(8388608));
    sColorNameMap.put("navy", Integer.valueOf(128));
    sColorNameMap.put("olive", Integer.valueOf(8421376));
    sColorNameMap.put("purple", Integer.valueOf(8388736));
    sColorNameMap.put("silver", Integer.valueOf(12632256));
    sColorNameMap.put("teal", Integer.valueOf(32896));
  }
}
