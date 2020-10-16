package com.app.utilities.callbacks;

public interface DialogRadioButtonItemSelectListener {
    void onItemSelect(int dialogIdentifier, int position);

    void onPositiveButtonClicked(int dialogIdentifier, int position);

    void onNegativeButtonClicked(int dialogIdentifier, int position);
}
