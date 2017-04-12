package usagitoneko.nekof.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import usagitoneko.nekof.R;

/**
 * Created by DareBacon on 11/4/2017.
 */

public class ChangePasswordDialog extends android.support.v4.app.DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialDialog.Builder(getContext()).customView(R.layout.change_password, true). title("Change your 4 digit password.").negativeText("Cancel").negativeColor(Color.DKGRAY).canceledOnTouchOutside(false)
                .onNegative(new MaterialDialog.SingleButtonCallback(){
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //callbacks.getWriteStatus(false);
                    }
                }).positiveText("Confirm").positiveColor(Color.GRAY).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).show();
    }
}
