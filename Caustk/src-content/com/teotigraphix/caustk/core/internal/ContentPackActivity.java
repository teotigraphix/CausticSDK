
package com.teotigraphix.caustk.core.internal;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.singlecellsoftware.contentextractor.NativeInterface;

public abstract class ContentPackActivity extends Activity {

    static {
        System.loadLibrary("contentpackcopier");
    }

    private GLSurfaceView m_GLView;

    protected String abortString;

    protected String packageName;

    protected String urlString;

    protected Vector4 progressBarColor;

    protected Vector4 progressBounds;

    protected Vector4 exitButtonBounds;

    protected Vector4 webButtonBounds;

    public void CheckForMountedSDCard() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("No SDCard preset")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            return;
                        }
                    }).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setup();

        ApplicationInfo appInfo = null;
        PackageManager packMgmr = getPackageManager();
        try {
            appInfo = packMgmr.getApplicationInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(abortString);
        }

        NativeInterface.SetAPKPath(appInfo.sourceDir);
        String sRootPath = Environment.getExternalStorageDirectory().getPath() + "/";
        NativeInterface.SetRootPath(sRootPath);

        CheckForMountedSDCard();

        super.onCreate(savedInstanceState);
        m_GLView = new ContentPackSurfaceView(this);
        setContentView(m_GLView);

        NativeInterface.SetProgressBarColor(progressBarColor.getX(), progressBarColor.getY(),
                progressBarColor.getWidth());
        NativeInterface.SetProgressBarCoordinates(progressBounds.getX(), progressBounds.getY(),
                progressBounds.getWidth(), progressBounds.getHeight());
        NativeInterface.SetExitButtonCoordinates(exitButtonBounds.getX(), exitButtonBounds.getY(),
                exitButtonBounds.getWidth(), exitButtonBounds.getHeight());
        NativeInterface.SetWebpageCoordinates(webButtonBounds.getX(), webButtonBounds.getY(),
                webButtonBounds.getWidth(), webButtonBounds.getHeight());
    }

    protected abstract void setup();

    public static class Vector4 {
        private int x;

        private int y;

        private int width;

        private int height;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Vector4(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    class ContentPackSurfaceView extends GLSurfaceView {

        ContentPackRenderer m_Renderer;

        public ContentPackSurfaceView(ContentPackActivity context) {
            super(context);
            m_Renderer = new ContentPackRenderer(context);
            setRenderer(m_Renderer);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            m_Renderer.onTouch(event);
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
            }
            return true;
        }
    }

    class ContentPackRenderer implements GLSurfaceView.Renderer {

        private GL10 m_GL;

        public ContentPackActivity m_Context;

        public ContentPackRenderer(ContentPackActivity context) {
            m_Context = context;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            if (gl != m_GL) {
                m_GL = gl;

                NativeInterface.InitGraphics();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int w, int h) {
            NativeInterface.Resize(w, h);
        }

        public void onTouch(MotionEvent event) {
            try {
                int id = 0;
                int index = 0;
                final int action = event.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        id = event.getPointerId(0);
                        NativeInterface.TouchBegin(id, (int)event.getX(0), (int)event.getY(0));
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        index = event.getAction() >> 8;
                        id = event.getPointerId(index);
                        NativeInterface.TouchBegin(id, (int)event.getX(index),
                                (int)event.getY(index));
                        break;
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            int retCode = NativeInterface.Render();

            if (retCode == 1) {
                m_Context.finish();
            } else if (retCode == 2) {
                m_Context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlString)));
            }
        }
    }
}
