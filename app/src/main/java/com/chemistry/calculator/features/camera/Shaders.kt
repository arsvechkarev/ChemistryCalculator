package com.chemistry.calculator.features.camera

object Shaders {
  
  val VERTEX_SHADER = """
    attribute vec2 vPosition;
    attribute vec2 vTexCoord;
    varying vec2 texCoord;

    void main() {
      texCoord = vTexCoord;
      gl_Position = vec4(vPosition.x, vPosition.y, 0.0, 1.0);
    }
    """.trimIndent()
  
  val FRAGMENT_SHADER = """
    precision mediump float;

    varying vec2 texCoord;
    uniform sampler2D iChannel0;

    void main() {
      gl_FragColor = texture2D(iChannel0, texCoord);
    }
  """.trimIndent()
  
  val FRAGMENT_SHADER_RTT = """
    #extension GL_OES_EGL_image_external : require
    precision mediump float;

    varying vec2 texCoord;
    uniform samplerExternalOES  iChannel0;

    void main() {
      gl_FragColor = texture2D(iChannel0, texCoord);
    }
  """.trimIndent()
}