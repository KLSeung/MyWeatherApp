package com.example.skyulee.myweatherproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class NetworkAlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder networkBuilder = new AlertDialog.Builder(context);

        networkBuilder.setTitle(getString(R.string.error_title))
                .setMessage(getString(R.string.Error_Network_Message))
                .setPositiveButton(getString(R.string.error_button_ok_text),null);

        return networkBuilder.create();
    }
}
