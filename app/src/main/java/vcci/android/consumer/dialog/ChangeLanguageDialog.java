package vcci.android.consumer.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vcci.android.consumer.R;
import vcci.android.consumer.interfaces.LanguageSelectionListener;

public class ChangeLanguageDialog extends DialogKaBap {
    /*@BindView(R.id.tv_english)
    TextView tv_english;
    @BindView(R.id.tv_gujarati)
    TextView tv_gujarati;
    @BindView(R.id.btn_cancel_dialog)
    Button btn_cancel_dialog;*/
    private final LanguageSelectionListener languageSelectionListener;

    public ChangeLanguageDialog(@NonNull Context context, LanguageSelectionListener languageSelectionListener) {
        super(context);
        this.languageSelectionListener = languageSelectionListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_change_language_dialog);
        ButterKnife.bind(this);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    @OnClick({R.id.tv_english, R.id.tv_gujarati, R.id.btn_cancel_dialog, R.id.cl_dialog_holder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_english:
            case R.id.tv_gujarati:
                languageSelectionListener.onLanguageClicked(view, 0);
                break;
            case R.id.btn_cancel_dialog:
            case R.id.cl_dialog_holder:
                dismiss();
                break;
        }
        dismiss();
    }

    @Override
    protected void onKeyBoardShowHidden(boolean isKeyboardVisible) {

    }
}