#ifdef GL_ES
precision mediump float;
#endif


varying vec2 v_texCoord;
uniform sampler2D u_texture;
uniform float u_time;


// --------------------------------------------------
// Hash רציף
float hash(float n) {
	return fract(sin(n) * 43758.5453123);
}


// --------------------------------------------------
// מערבולת עגולה אמיתית
vec4 whirlpool(vec2 uv, vec2 center, float time, float strength) {


	vec2 d = uv - center;
	float dist = length(d);
	float angle = atan(d.y, d.x);


	float radius = 0.028;
	float thickness = 0.010;


	// טבעת קצף
	float ring = smoothstep(
    	thickness, 0.0,
    	abs(dist - radius)
	);


	float swirl =
    	sin(angle * 6.0 - time * 2.0) * 0.5 + 0.5;


	float mask = ring * swirl * strength;


	vec4 foam;
	foam.rgb = vec3(0.84, 0.87, 0.86);
	foam.a = mask;


	return foam;
}


void main() {


	// ==================================================
	// 1. Core Drift – זרם בסיסי קבוע
	// ==================================================
	vec2 drift = vec2(
    	u_time * 0.003,
    	u_time * 0.0015
	);


	vec2 uv = v_texCoord + drift;


	// ==================================================
	// 2. Shear Flow
	// ==================================================
	float shear =
    	sin(v_texCoord.y * 3.0 + u_time * 0.2) * 0.002;
	uv.x += shear;


	// ==================================================
	// 3. נשימת בסיס
	// ==================================================
	float baseX =
    	sin(v_texCoord.y * 5.0 + u_time * 0.12) * 0.0015;
	float baseY =
    	cos(v_texCoord.x * 4.0 + u_time * 0.10) * 0.0015;
	uv += vec2(baseX, baseY);


	// ==================================================
	// 4. צבע ים בסיסי
	// ==================================================
	vec4 water = texture2D(u_texture, uv);
	water.rgb *= 0.95;


	// ==================================================
	// 5. גלי‑אירוע / מערבולות – חזקות ותכופות יותר
	// ==================================================
	for (int i = 0; i < 3; i++) {


    	float fi = float(i);


    	vec2 center = vec2(
        	hash(fi * 2.7),
        	hash(fi * 5.3)
    	);


    	center += vec2(
        	sin(u_time * 0.05 + fi),
        	cos(u_time * 0.04 + fi)
    	) * 0.05;


    	// 🔥 כאן השינוי המרכזי 🔥
    	// תדירות גבוהה יותר ≈ כל חצי שניה
    	float active =
        	smoothstep(
            	0.4, 0.9,                	// חלון רחב יותר
            	sin(u_time * 2.5 + fi * 3.1)  // ← במקום 0.25
        	);


    	vec4 foam =
        	whirlpool(uv, center, u_time, active * 0.8);


    	water.rgb = mix(water.rgb, foam.rgb, foam.a);
	}


	gl_FragColor = water;
}



