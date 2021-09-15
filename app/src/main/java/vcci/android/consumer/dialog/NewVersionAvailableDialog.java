package vcci.android.consumer.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vcci.android.consumer.R;
import vcci.android.consumer.interfaces.UpdateDialogClickListener;

public class NewVersionAvailableDialog extends DialogKaBap {

    private final UpdateDialogClickListener itemClickListener;

    public NewVersionAvailableDialog(@NonNull Context context, UpdateDialogClickListener itemClickListener) {
        super(context);
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void onKeyBoardShowHidden(boolean isKeyboardVisible) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_new_version);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_no_dialog})
    void onClickCancel(View view) {
        itemClickListener.onUpdateDialogItemClicked(view, 0);
        dismiss();
    }


    @OnClick({R.id.btn_yes_dialog})
    public void onYesClicked(View view) {
        itemClickListener.onUpdateDialogItemClicked(view, 0);
        dismiss();
    }
}