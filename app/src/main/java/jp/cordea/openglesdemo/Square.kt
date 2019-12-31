package jp.cordea.openglesdemo

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val VERTEX = 3
private const val SIZE = 0.5f
private const val HALF_SIZE = SIZE / 2f

private val COORDS = floatArrayOf(
    -HALF_SIZE, HALF_SIZE, 0f,
    -HALF_SIZE, -HALF_SIZE, 0f,
    HALF_SIZE, -HALF_SIZE, 0f,
    HALF_SIZE, HALF_SIZE, 0f
)
private val ORDER = shortArrayOf(0, 1, 2, 0, 2, 3)
private val COLOR = floatArrayOf(0f, 0f, 1f, 1f)

class Square {
    private val vertexBuffer = ByteBuffer
        .allocateDirect(COORDS.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(COORDS)
                position(0)
            }
        }

    private val listBuffer = ByteBuffer
        .allocateDirect(ORDER.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(ORDER)
                position(0)
            }
        }

    private val vertexShader = """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;

        void main() {
            gl_Position = uMVPMatrix * vPosition;
        }
    """.trimIndent()

    private val fragmentSharder = """
        precision mediump float;
        uniform vec4 vColor;

        void main() {
            gl_FragColor = vColor;
        }
    """.trimIndent()

    private val program by lazy {
        GLES20.glCreateProgram().apply {
            GLES20.glAttachShader(
                this,
                GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER).apply {
                    GLES20.glShaderSource(this, vertexShader)
                    GLES20.glCompileShader(this)
                }
            )
            GLES20.glAttachShader(
                this,
                GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER).apply {
                    GLES20.glShaderSource(this, fragmentSharder)
                    GLES20.glCompileShader(this)
                }
            )
            GLES20.glLinkProgram(this)
        }
    }

    fun draw(mvp: FloatArray) {
        GLES20.glUseProgram(program)

        val position = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(position)
        GLES20.glVertexAttribPointer(
            position,
            VERTEX,
            GLES20.GL_FLOAT,
            false,
            VERTEX * 4,
            vertexBuffer
        )

        val color = GLES20.glGetUniformLocation(program, "vColor")
        GLES20.glUniform4fv(color, 1, COLOR, 0)

        val matrix = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(matrix, 1, false, mvp, 0)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            ORDER.size,
            GLES20.GL_UNSIGNED_SHORT,
            listBuffer
        )

        GLES20.glDisableVertexAttribArray(position)
    }
}
