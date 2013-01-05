/**
* Landing page
* @author huydx 
* @version 0.9
*/

package com.ritsumei.mobileProject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing);
		ImageButton gotoTop = (ImageButton)findViewById(R.id.goto_map);
        gotoTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), SubMainActivity.class);
				startActivity(i);
			}
		});
	}
}
