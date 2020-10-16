package com.app.erldriver.model.entity.info;


import java.util.List;

public class ModuleSelection {
    private int type,position;
    private ModuleInfo info;
    private List<ModuleInfo> moduleList;

    public ModuleSelection(int type, ModuleInfo info, List<ModuleInfo> moduleList) {
        this.type = type;
        this.info = info;
        this.moduleList = moduleList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ModuleInfo getInfo() {
        return info;
    }

    public void setInfo(ModuleInfo info) {
        this.info = info;
    }

    public List<ModuleInfo> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<ModuleInfo> moduleList) {
        this.moduleList = moduleList;
    }
}
