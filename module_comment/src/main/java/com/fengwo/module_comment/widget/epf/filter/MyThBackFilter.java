package com.fengwo.module_comment.widget.epf.filter;


public class MyThBackFilter extends GlFilter {



    private static final String VERTEX_SHADER =
            "precision mediump float; +" +
                    "                 varying vec2 ft_Position;  +  " +
                    "                  uniform sampler2D sTexture;    +  " +
                    "                  void main()    +  " +
                    "                  {    +  " +
                    "                  vec4 v=texture2D(sTexture, ft_Position);    +  " +
                    "                  if(v.r < 0.5 && v.g < 0.5 && v.b < 0.5)   +  " +
                    "                  {   +  " +
                    "                  gl_FragColor = vec4(v.r, v.g, v.b, 0);   +  " +
                    "                  }else{   +  " +
                    "                  gl_FragColor = vec4(v.r, v.g, v.b, v.a);   +  " +
                    "                  }}";
    //竖屏
//            "vTextureCoord = vec2(aTextureCoord.x, aTextureCoord.y*0.5+0.5);\n" +
//            "vTextureCoord2 = vec2(aTextureCoord.x, aTextureCoord.y*0.5);\n" +

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying highp vec2 vTextureCoord;\n" +
                    "varying highp vec2 vTextureCoord2;\n" +
                    "uniform lowp sampler2D sTexture;\n" +
                    "void main() {\n" +
                    "vec4 color1 = texture2D(sTexture, vTextureCoord);\n" +
                    "vec4 color2 = texture2D(sTexture, vTextureCoord2);\n" +
                    "gl_FragColor = vec4(color1.rgb, color2.r);\n" +
                    "}\n";


    public MyThBackFilter() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }
}