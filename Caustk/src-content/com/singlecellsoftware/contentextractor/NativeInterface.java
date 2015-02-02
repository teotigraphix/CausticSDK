package com.singlecellsoftware.contentextractor;

public class NativeInterface
{   
  public static native void InitGraphics();
  public static native void Resize(int w, int h);
  public static native int  Render();
  public static native void TouchBegin(int idx, int x, int y);
  public static native void SetRootPath(String sPath);
  public static native void SetAPKPath(String sPath);
  public static native void SetProgressBarCoordinates(int x0, int y0, int x1, int y1);
  public static native void SetProgressBarColor(int red, int green, int blue);
  public static native void SetExitButtonCoordinates(int x0, int y0, int x1, int y1);
  public static native void SetWebpageCoordinates(int x0, int y0, int x1, int y1);
}
