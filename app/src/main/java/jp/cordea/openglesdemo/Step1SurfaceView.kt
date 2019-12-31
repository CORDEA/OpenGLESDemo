package jp.cordea.openglesdemo

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class Step1SurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    init {
        setEGLContextClientVersion(2)
        setRenderer(Step1Renderer())
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}
