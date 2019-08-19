package com.teedao.zkgui.asyncTree;

import com.teedao.zkgui.service.ZkClientHelper;
import javafx.scene.control.TreeItem;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryLoader implements Callable<List<? extends TreeItem<ZooPath>>> {

    private final ZooPath path;

    public DirectoryLoader(ZooPath path) {
        this.path = path;
    }

    private static final Comparator<ZooPath> COMPARATOR = (left, right) -> {
        boolean leftIsDir = ZkClientHelper.isDirectory(left);
        if (leftIsDir ^ ZkClientHelper.isDirectory(right)) {
            return leftIsDir ? -1 : 1;
        }
        return left.compareTo(right);
    };


    @Override
    public List<? extends TreeItem<ZooPath>> call() {
        ZkClientHelper.loadChildren(path);
        Collection<ZooPath> childNodes = path.getChildren();
        try (Stream<ZooPath> stream = childNodes.stream()) {
            return stream.sorted(COMPARATOR)
                    .map(this::toTreeItem)
                    .collect(Collectors.toList());
        }
    }

    private TreeItem<ZooPath> toTreeItem(ZooPath path) {
        return ZkClientHelper.isDirectory(path)
                ? new LoadingTreeItem<>(path, new DirectoryLoader(path))
                : new TreeItem<>(path);
    }

}