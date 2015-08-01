uniform vec2 u_resolution;
uniform vec3 u_bottomColor;
uniform vec3 u_topColor;

void main() {
    vec2 pos = gl_FragCoord.xy / u_resolution.xy;
    
   /*
    gl_FragColor = vec4(
        u_bottomColor.r * (1.0 - pos.y) + u_topColor.r * pos.y,
        u_bottomColor.g * (1.0 - pos.y) + u_topColor.g * pos.y,
        u_bottomColor.b * (1.0 - pos.y) + u_topColor.b * pos.y,
        1
    );*/
    
    
    
     gl_FragColor = vec4(
     u_bottomColor.r * pos.y + u_topColor.r * (1.0 - pos.y),
     u_bottomColor.g * pos.y + u_topColor.g * (1.0 - pos.y),
     u_bottomColor.b * pos.y + u_topColor.b * (1.0 - pos.y),
     1
     );
    
    

    
}