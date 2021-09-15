package com.ibphub.vcci_new.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

public abstract class DialogKaBap extends Dialog {

    public DialogKaBap(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initializeWindow();
    }

    public DialogKaBap(@NonNull Context context) {
        super(context);
        initializeWindow();
    }

    private void initializeWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        final int flags = View.SYSTEM_UI_FLAG_IMMERSIVE |
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                View.SYSTEM_UI_FLAG_FULLSCREEN;
        // This work only for android 4.4+
//
        if (getWindow() != null) {
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // decorView.setSystemUiVisibility(flags);
                    onKeyBoardShowHidden(true);
                } else {
                    onKeyBoardShowHidden(false);
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        Objects.requireNonNull(getWindow()).getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    protected abstract void onKeyBoardShowHidden(boolean isKeyboardVisible);
}