package com.app.erldriver.util;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import com.app.erldriver.model.entity.info.ModuleInfo;
import com.app.erldriver.model.entity.info.ModuleSelection;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PopupMenuHelper {

    public static void showPopupMenu(Context mContext, View view, List<ModuleInfo> list, int dialogIdentifier) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        for (int i = 0; i < list.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, list.get(i).getName());
        }

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int position = menuItem.getItemId();
            ModuleSelection moduleSelection = new ModuleSelection(dialogIdentifier, list.get(position), list);
            moduleSelection.setPosition(position);
            EventBus.getDefault().post(moduleSelection);
            return false;
        });

        popupMenu.show();
    }
}
