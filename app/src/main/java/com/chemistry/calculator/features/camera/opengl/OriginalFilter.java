/*
 * Copyright 2016 nekocode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chemistry.calculator.features.camera.opengl;

import android.opengl.GLES20;
import com.chemistry.calculator.features.camera.Shaders;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class OriginalFilter extends CameraFilter {

  private int program;

  public OriginalFilter() {
    program = MyGLUtils.buildProgram(Shaders.INSTANCE.getVERTEX_SHADER(),
        Shaders.INSTANCE.getFRAGMENT_SHADER());
  }

  @Override
  public void onDraw(int cameraTexId, int canvasWidth, int canvasHeight) {
    setupShaderInputs(program,
        new int[]{canvasWidth, canvasHeight},
        new int[]{cameraTexId},
        new int[][]{});
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
  }
}
