package com.teedao.zkgui.skin.impl;

import javafx.scene.control.TextField;

public class TextFieldSkin extends TextFieldWithButtonSkin {
    public TextFieldSkin(TextField textField) {
        super(textField);
    }

    protected void rightButtonPressed() {
        getSkinnable().setText("");
    }

}