package com.teedao.zkgui.asyncTree;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

public class LoadingTreeItem<T> extends TreeItem<T> {

    private static final EventType<?> PRE_ADD_LOADED_CHILDREN
            = new EventType<>(treeNotificationEvent(), "PRE_ADD_LOADED_CHILDREN");
    private static final EventType<?> POST_ADD_LOADED_CHILDREN
            = new EventType<>(treeNotificationEvent(), "POST_ADD_LOADED_CHILDREN");

    @SuppressWarnings("unchecked")
    static <T> EventType<TreeModificationEvent<T>> preAddLoadedChildrenEvent() {
        return (EventType<TreeModificationEvent<T>>) PRE_ADD_LOADED_CHILDREN;
    }

    @SuppressWarnings("unchecked")
    static <T> EventType<TreeModificationEvent<T>> postAddLoadedChildrenEvent() {
        return (EventType<TreeModificationEvent<T>>) POST_ADD_LOADED_CHILDREN;
    }

    private final Callable<List<? extends TreeItem<T>>> callable;
    private boolean needToLoadData = true;

    private CompletableFuture<?> future;

    public LoadingTreeItem(T value, Callable<List<? extends TreeItem<T>>> callable) {
        super(value);
        this.callable = callable;
        super.getChildren().add(new TreeItem<>());
        addExpandedListener();
    }

    @SuppressWarnings("unchecked")
    private void addExpandedListener() {
        expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                needToLoadData = true;
                if (future != null) {
                    future.cancel(true);
                }
                super.getChildren().setAll(new TreeItem<>());
            }
        });
    }

    @Override
    public ObservableList<TreeItem<T>> getChildren() {
        if (needToLoadData) {
            needToLoadData = false;
            future = CompletableFuture.supplyAsync(new CallableToSupplierAdapter<>(callable))
                    .whenCompleteAsync(this::handleAsyncLoadComplete, Platform::runLater);
        }
        return super.getChildren();
    }

    private void handleAsyncLoadComplete(List<? extends TreeItem<T>> result, Throwable th) {
        if (th != null) {
            Thread.currentThread().getUncaughtExceptionHandler()
                    .uncaughtException(Thread.currentThread(), th);
        } else {
            Event.fireEvent(this, new TreeModificationEvent<>(preAddLoadedChildrenEvent(), this));
            super.getChildren().setAll(result);
            Event.fireEvent(this, new TreeModificationEvent<>(postAddLoadedChildrenEvent(), this));
        }
        future = null;
    }

    private static class CallableToSupplierAdapter<T> implements Supplier<T> {

        private final Callable<T> callable;

        private CallableToSupplierAdapter(Callable<T> callable) {
            this.callable = callable;
        }

        @Override
        public T get() {
            try {
                return callable.call();
            } catch (Exception ex) {
                throw new CompletionException(ex);
            }
        }

    }

}
