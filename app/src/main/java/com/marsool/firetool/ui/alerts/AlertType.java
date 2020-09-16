package com.marsool.firetool.ui.alerts;

import java.util.ArrayList;
import java.util.Arrays;

public enum AlertType {
    CONFIRM(ButtonType.CANCEL, ButtonType.YES), INFORMATION(ButtonType.OK);

    private ArrayList<ButtonType> buttons;

    AlertType(ButtonType...buttonTypes) {
        buttons = new ArrayList<>();
        buttons.addAll(Arrays.asList(buttonTypes));
    }

    public ArrayList<ButtonType> getButtons() {
        return buttons;
    }
}
