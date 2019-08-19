package com.teedao.zkgui.asyncTree;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class LazyTreeCell<T> extends TreeCell<T> {

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(String placeholderText,
                                                                     Callback<? super T, String> toStringCallback) {
        return tree -> new LazyTreeCell<>(placeholderText, toStringCallback);
    }

    private final String placeholderText;
    private final Callback<? super T, String> toStringCallback;

    public LazyTreeCell(String placeholderText, Callback<? super T, String> toStringCallback) {
        this.placeholderText = placeholderText;
        this.toStringCallback = toStringCallback;
    }

    /*
     * Assumes that if "item" is null **and** the parent TreeItem is an instance of
     * LoadingTreeItem that this is a "placeholder" cell.
     */
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (item == null && getTreeItem().getParent() instanceof LoadingTreeItem) {
            setText(placeholderText);
        } else {
            setText(toStringCallback.call(item));
        }
    }

}