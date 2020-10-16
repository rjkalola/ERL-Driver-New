package com.app.utilities.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.callbacks.DialogRadioButtonItemSelectListener;

/**
 * @AutherBy Dhaval Jivani
 */

public final class AlertDialogHelper {
    private static boolean isDialogOpen;

    public static void showDialog(Context context,
                                  String dialogTitle, String dialogMessage, String textPositiveButton,
                                  String textNegativeButton, boolean isCancelable,
                                  final DialogButtonClickListener buttonClickListener,
                                  final int dialogIdentifier) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if (!StringHelper.isEmpty(dialogTitle)) {
            alertDialogBuilder.setTitle(dialogTitle);
        }
        alertDialogBuilder.setMessage(dialogMessage);
        if (isCancelable) {
            alertDialogBuilder.setCancelable(true);
        } else {
            alertDialogBuilder.setCancelable(false);
        }

        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogOpen = false;
            }
        });

        alertDialogBuilder.setPositiveButton(textPositiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                isDialogOpen = false;
                if (buttonClickListener == null) {
                    dialog.dismiss();
                } else {
                    buttonClickListener.onPositiveButtonClicked(dialogIdentifier);
                }
            }
        });

        if (textNegativeButton != null) {
            alertDialogBuilder.setNegativeButton(textNegativeButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id1) {
                    isDialogOpen = false;
                    if (buttonClickListener == null) {
                        dialog.dismiss();
                    } else {
                        buttonClickListener.onNegativeButtonClicked(dialogIdentifier);
                    }
                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        //	alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        if (!((Activity) context).isFinishing() && !isDialogOpen) {
            isDialogOpen = true;
            alertDialog.show();
        }
    }

    public static void showDialog(final Context context,
                                  String dialogTitle, String dialogMessage, String textPositiveButton,
                                  String textNegativeButton, boolean isCancelable,
                                  final boolean finish) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if (!StringHelper.isEmpty(dialogTitle)) {
            alertDialogBuilder.setTitle(dialogTitle);
        }
        alertDialogBuilder.setMessage(dialogMessage);
        if (isCancelable) {
            alertDialogBuilder.setCancelable(true);
        } else {
            alertDialogBuilder.setCancelable(false);
        }

        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogOpen = false;
            }
        });

        alertDialogBuilder.setPositiveButton(textPositiveButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id1) {
                        isDialogOpen = false;
                        if (finish) {
                            ((Activity) context).finish();
                        }
                    }
                });

        if (textNegativeButton != null) {
            alertDialogBuilder.setNegativeButton(textNegativeButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id1) {
                    isDialogOpen = false;
                    dialog.dismiss();
                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        //	alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        if (!((Activity) context).isFinishing() && !isDialogOpen) {
            isDialogOpen = true;
            alertDialog.show();
        }
    }

    public static void showDropDownDialog(final Context context,
                                          String dialogTitle, String dialogMessage, String[] listItems, int checkedItem, String textPositiveButton,
                                          String textNegativeButton, boolean isCancelable,
                                          final DialogRadioButtonItemSelectListener itemSelectListener,
                                          final int dialogIdentifier) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        if (!StringHelper.isEmpty(dialogTitle)) {
            alertDialogBuilder.setTitle(dialogTitle);
        }
        alertDialogBuilder.setMessage(dialogMessage);
        if (isCancelable) {
            alertDialogBuilder.setCancelable(true);
        } else {
            alertDialogBuilder.setCancelable(false);
        }

        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogOpen = false;
            }
        });

        alertDialogBuilder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isDialogOpen = false;
                if (itemSelectListener == null) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    itemSelectListener.onItemSelect(dialogIdentifier, which);
                }
            }
        });

        alertDialogBuilder.setPositiveButton(textPositiveButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id1) {
                isDialogOpen = false;
                if (itemSelectListener == null) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    itemSelectListener.onPositiveButtonClicked(dialogIdentifier, 0);
                }
            }
        });

        if (textNegativeButton != null) {
            alertDialogBuilder.setNegativeButton(textNegativeButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id1) {
                    isDialogOpen = false;
                    if (itemSelectListener == null) {
                        dialog.dismiss();
                    } else {
                        itemSelectListener.onNegativeButtonClicked(dialogIdentifier, 0);
                    }
                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (!((Activity) context).isFinishing() && !isDialogOpen) {
            isDialogOpen = true;
            alertDialog.show();
        }
    }

    public static boolean isIsDialogOpen() {
        return isDialogOpen;
    }

    public static void setIsDialogOpen(boolean isDialogOpen) {
        AlertDialogHelper.isDialogOpen = isDialogOpen;
    }

}
