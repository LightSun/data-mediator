package com.heaven7.java.data.mediator.test;

public interface ISelectable {

    int SELECT_MODE_SINGLE  = 1;
    int SELECT_MODE_MULTI   = 2;

    int INVALID_POSITION    = -1;

    void setSelected(boolean selected);
    boolean isSelected();
}
