package com.example.soff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CuttingMechDialog extends DialogFragment {

    private static final String TAG = "CuttingMechDialog";

    public TextView dialogText;
    private Button reset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstandceState){
        View view = inflater.inflate(R.layout.activity_main_popup, container, false);

        dialogText = view.findViewById(R.id.cutDialog);
        reset = view.findViewById(R.id.btnReset);

//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: closing dialog");
//                getDialog().dismiss();
//            }
//        });

        return view;
    }

    public void doneSlicing() {
        // Change dialog to ask user to pick up fruit
        String text = "Done! Please pick up the slices then press the Reset button below.";
        dialogText.setText(text);

        // Set reset button to visible
        reset.setVisibility(View.VISIBLE);
    }

    public void setReset() {
        getDialog().dismiss();
    }
}
