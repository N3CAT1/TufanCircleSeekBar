<h1>Android TufanCircleSeekBar</h1>

A TufanCircleSeekBar inspired by Android HoloColorPicker designed by Necati TUFAN and developed by Lars Werkman.

![image](https://files.myopera.com/N3CAT1/files/tufan_circle_seek_bar_1.png)
![image](https://files.myopera.com/N3CAT1/files/tufan_circle_seek_bar_2.png)



<h2>Documentation</h2>
Add this to your xml

	<com.tufan.library.TufanCircleSeekBar
        android:id="@+id/tufanCircleSeekBar1"
        android:layout_width="250dp"
        android:layout_height="250dp"/>

Don't forget to add this at the root of your View in the xml layout when you use TufanCircleSeekBar:

	xmlns:app="http://schemas.android.com/apk/res-auto"
        
You can change some widget components (text size, wheel color, point color, etc) sith this attrs. Note that width and radius values in dip unit, text size value in sp unit.
 
    app:angle_end="360"
    app:angle_start="0"
    app:init_progress="0"
    app:max="100"
    app:pointer_color="#FF0000"
    app:pointer_halo_color="#88FF0000"
    app:pointer_halo_radius="10"
    app:pointer_radius="3"
    app:wheel_active_color="#FF6A00"
    app:wheel_second_active_color="#B6FF00"
    app:wheel_stroke_width="5"
    app:wheel_unactive_color="#FFCCCCCC"

To get the value of the circle seekbar

    TufanCircleSeekBar tufanCircleSeekBar1 = (TufanCircleSeekBar) findViewById(R.id.tufanCircleSeekBar1);
    tufanCircleSeekBar1.getProgress();
    tufanCircleSeekBar1.getSecondProgress();
	
To set the value of the circle seekbar

    TufanCircleSeekBar tufanCircleSeekBar1 = (TufanCircleSeekBar) findViewById(R.id.tufanCircleSeekBar1);
    tufanCircleSeekBar1.setProgress(30);
    tufanCircleSeekBar1.getSecondProgress(60);
	
<H2>License</H2>
	
 	 Copyright 2013 Necati TUFAN
 	
 	 Licensed under the Apache License, Version 2.0 (the 	"License");
 	 you may not use this file except in compliance with 	the License.
 	 You may obtain a copy of the License at
 	
 	     http://www.apache.org/licenses/LICENSE-2.0
 	
 	 Unless required by applicable law or agreed to in 	writing, software
	 distributed under the License is distributed on an 	"AS IS" BASIS,
 	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 	either express or implied.
 	 See the License for the specific language governing 	permissions and
 	 limitations under the License.
 	
 	
<h2>Original by</h2>
**Lars Werkman**

<h2>Modified By</h2>
**Necati TUFAN**
