/*
 Copyright 2011, 2012 Chris Banes.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.ibphub.vcci_new.image_slider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ibphub.vcci_new.R;
import com.ibphub.vcci_new.activity.MainActivity;
import com.ibphub.vcci_new.util.Stash;

public class LogoutActivity extends AppCompatActivity {

    private TextView tv_cancel;
    private TextView tv_logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        tv_cancel = findViewById(R.id.tv_cancel);
        tv_logout = findViewById(R.id.tv_logout);

        tv_cancel.setOnClickListener(v -> finish());

        tv_logout.setOnClickListener(v -> {
            Stash.put("isVerified", false);
            Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
