package com.teedao.zkgui.asyncTree;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.List;

public class TreeViewUtils {

    public static <T> void installSelectionBugWorkaround(TreeView<T> tree) {
        List<TreeItem<T>> selected = new ArrayList<>(0);
        EventHandler<TreeModificationEvent<T>> preAdd = event -> {
            event.consume();
            selected.addAll(tree.getSelectionModel().getSelectedItems());
            tree.getSelectionModel().clearSelection();
        };
        EventHandler<TreeModificationEvent<T>> postAdd = event -> {
            event.consume();
            selected.forEach(tree.getSelectionModel()::select);
            selected.clear();
        };
        ChangeListener<TreeItem<T>> rootListener = (observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.removeEventHandler(LoadingTreeItem.preAddLoadedChildrenEvent(), preAdd);
                oldValue.removeEventHandler(LoadingTreeItem.postAddLoadedChildrenEvent(), postAdd);
            }
            if (newValue != null) {
                newValue.addEventHandler(LoadingTreeItem.preAddLoadedChildrenEvent(), preAdd);
                newValue.addEventHandler(LoadingTreeItem.postAddLoadedChildrenEvent(), postAdd);
            }
        };
        rootListener.changed(tree.rootProperty(), null, tree.getRoot());
        tree.rootProperty().addListener(rootListener);
    }

    private TreeViewUtils() {
    }
}