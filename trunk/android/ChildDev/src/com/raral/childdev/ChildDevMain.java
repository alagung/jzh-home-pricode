package com.raral.childdev;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class ChildDevMain extends Activity{
    // private static final String LOG_TAG = SpritesTest.class.getSimpleName();
    private CCGLSurfaceView mGLSurfaceView;

	private Handler handler;
	public Handler getHandler() {
		return handler;
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGLSurfaceView = new CCGLSurfaceView(this);
        handler = new Handler();
        setContentView(mGLSurfaceView);

        // attach the OpenGL view to a window
        CCDirector.sharedDirector().attachInView(mGLSurfaceView);

        // set landscape mode
        CCDirector.sharedDirector().setLandscape(false);

        // show FPS
        CCDirector.sharedDirector().setDisplayFPS(true);

        // frames per second
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);

        ShowChapters.getInstance().start();
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();

        CCDirector.sharedDirector().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        CCDirector.sharedDirector().onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ShowChapters.getInstance().clean();
        
        CCDirector.sharedDirector().end();
        CCTextureCache.sharedTextureCache().removeAllTextures();
        CCSpriteFrameCache.sharedSpriteFrameCache().removeAllSpriteFrames();
        
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0); 
    }
}
