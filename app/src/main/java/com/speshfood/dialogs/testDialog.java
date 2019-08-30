package com.speshfood.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.speshfood.R;

public class testDialog extends AppCompatDialogFragment
{
    @Override
    public
    Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater=getActivity ().getLayoutInflater ();
        View rootView=inflater.inflate (R.layout.dialog_test,null,false);
        final AlertDialog dialog=new AlertDialog.Builder(getActivity()).setView(rootView)
                .show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
