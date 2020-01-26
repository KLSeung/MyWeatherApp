package com.example.skyulee.myweatherproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {   //AlertDialogFragment is like an Activity and is used to alert the users about errors
        Context context = getActivity();                        //getActivity is used to provide the Activity and context we need
        AlertDialog.Builder builder = new AlertDialog.Builder(context); //this AlertDialog.Builder is a special code to configure the dialog
                                                                        //normally we would use "this" or "Activity.this", but we are in another class so we must use getActivity
        builder.setTitle(getString(R.string.error_title))                        //This is used to set the title of the dialog box
        .setMessage(getString(R.string.error_message))   //This is used to set the message, but here we are "chaining" the methods of the title and the message
        .setPositiveButton(getString(R.string.error_button_ok_text), null);         //This is setting a Positive Button that the onClickListener is set as "null" which just closes the dialog box
                                                            //You can set a positive, negative, or a neutral button depending on the situation.

        return builder.create();                            //used to return the builder (dialog box)

    }
}
