package com.teedao.zkgui.controllers;


import com.teedao.zkgui.asyncTree.*;
import com.teedao.zkgui.model.NewNodeRequest;
import com.teedao.zkgui.model.NodeAcl;
import com.teedao.zkgui.model.NodeDetailInfo;
import com.teedao.zkgui.model.NodeMetaData;
import com.teedao.zkgui.service.ZkClientHelper;
import com.teedao.zkgui.service.ZkService;
import com.teedao.zkgui.skin.JMetro;
import com.teedao.zkgui.skin.Style;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    private static final Logger logger = LogManager.getLogger(MainViewController.class);

    @FXML
    public VBox root;

    @FXML
    public TextArea nodeDataText;

    @FXML
    public GridPane metaForm;

    @FXML
    public GridPane aclForm;

    @FXML
    public Button submitBtn;

    @FXML
    public VBox leftBox;

    @FXML
    public TreeView<ZooPath> nodeTree;

    @FXML
    public TextField uriField;
    @FXML
    public Button connectBtn;

    @FXML
    ToggleButton themeBtn;

    private ZooPath currentPath;


    public MainViewController() {
        super();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectBtn.setOnAction(event -> {
            String uri = this.uriField.textProperty().getValue();
            if (!uri.isEmpty()) {
                connectBtn.setDisable(true);
                uriField.setDisable(true);
                reLoadZooTree(uri);
                connectBtn.setDisable(false);
                uriField.setDisable(false);
            }
        });
        themeBtn.setOnAction(event -> {
            boolean selected = themeBtn.isSelected();
            if (selected) {
                new JMetro(root.getScene().getRoot(), Style.DARK);
            } else {
                new JMetro(root.getScene().getRoot(), Style.LIGHT);
            }
        });
    }

    private void reLoadZooTree(String uri) {
        ZkClientHelper.current(uri);
        nodeTree.setCellFactory(LazyTreeCell.forTreeView("Loading...", MainViewController::pathToString));
        TreeViewUtils.installSelectionBugWorkaround(nodeTree);
        ZooPath fsRoot = new ZooPath(null, "/");
        nodeTree.getRoot().getChildren().setAll(new LoadingTreeItem<>(fsRoot, new DirectoryLoader(fsRoot)));
        ContextMenu contextFileMenu = new ContextMenu();
        contextFileMenu.getItems().addAll(getContextMenuList(nodeTree));
        nodeTree.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {
            if (me.getButton() == MouseButton.SECONDARY || me.isControlDown()) {
                contextFileMenu.show(root, me.getScreenX(), me.getScreenY());
            } else {
                contextFileMenu.hide();
            }
            if (me.getButton() == MouseButton.PRIMARY) {
                Node node = me.getPickResult().getIntersectedNode();
                if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                    TreeItem<ZooPath> selectItem = nodeTree.getSelectionModel().getSelectedItem();
                    currentPath = selectItem.getValue();
                    NodeDetailInfo nodeDetail = ZkService.getNodeDetail(currentPath);
                    bindNodeMete(nodeDetail);
                    logger.debug("node :{}", nodeDetail);
                }
            }
        });
        submitBtn.setOnAction(actionEvent -> {
            if (currentPath != null) {
                String value = nodeDataText.textProperty().getValue();
                try {
                    ZkService.updateNode(currentPath.getFullName(), value);
                } catch (Exception e) {
                    logger.error("update Node Error", e);
                }
            }
        });
    }

    private void bindNodeMete(NodeDetailInfo nodeDetail) {
        NodeMetaData nodeMetaData = nodeDetail.getMetaData();
        List<NodeAcl> acls = nodeDetail.getAcls();
        NodeAcl nodeAcl = acls.get(0);
        nodeDataText.textProperty().bindBidirectional(new SimpleStringProperty(nodeDetail.getData()));
        ObservableList<Node> children = metaForm.getChildren();
        for (Node child : children) {
            if (child instanceof TextField) {
                try {
                    String id = child.getId();
                    String getMethodName = "get" + id.substring(0, 1).toUpperCase() + id.substring(1);
                    Method m = nodeMetaData.getClass().getMethod(getMethodName);
                    Object value = m.invoke(nodeMetaData);
                    ((TextField) child).textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(value)));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

        ObservableList<Node> children1 = aclForm.getChildren();
        for (Node child : children1) {
            if (child instanceof TextField) {
                try {
                    String id = child.getId();
                    String getMethodName = "get" + id.substring(0, 1).toUpperCase() + id.substring(1);
                    Method m = nodeAcl.getClass().getMethod(getMethodName);
                    Object value = m.invoke(nodeAcl);
                    ((TextField) child).textProperty().bindBidirectional(new SimpleStringProperty(String.valueOf(value)));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private List<MenuItem> getContextMenuList(TreeView<ZooPath> tree) {
        //Add button
        List<MenuItem> menuList = new ArrayList<>();
        MenuItem addBtn = new MenuItem("Add");
        addBtn.setOnAction(event -> {
            TreeItem<ZooPath> selectItem = tree.getSelectionModel().getSelectedItem();
            ZooPath parent = selectItem.getValue();
            NewNodeRequest nodeRequest = new NewNodeRequest();
            nodeRequest.setParent(parent);
            nodeRequest.setMode("PERSISTENT");
            NewNodeDialog nc = new NewNodeDialog(nodeRequest, this.root);
            Optional<NewNodeRequest> connInfoOptional = nc.showAndWait();
            if (connInfoOptional.isPresent()) {
                NewNodeRequest newNode = connInfoOptional.get();
                try {
                    ZkService.addNode(newNode);
                    ZooPath newPath = new ZooPath(parent, newNode.getName());
                    selectItem.getChildren().add(new TreeItem<>(newPath));
                } catch (Exception e) {
                    logger.error("添加节点异常:", e);
                }
            }
        });
        //Remove Button
        MenuItem removeBtn = new MenuItem("Remove");
        removeBtn.setOnAction(event -> {
            TreeItem<ZooPath> selectItem = tree.getSelectionModel().getSelectedItem();
            ZooPath value = selectItem.getValue();
            try {
                ZkService.removeNode(value);
                value.getParent().deleteChild(value.getName());
                selectItem.getParent().getChildren().remove(selectItem);
            } catch (Exception e) {
                String message = e.getMessage();
                String[] split = message.split(":");
                Alert alert = new Alert(Alert.AlertType.ERROR, split[1], ButtonType.CLOSE);
                alert.showAndWait();
                logger.error("remove Node Error", e);
            }

        });
        MenuItem fullPathBtn = new MenuItem("Full Path");
        fullPathBtn.setOnAction(event -> {
            TreeItem<ZooPath> selectItem = tree.getSelectionModel().getSelectedItem();
            ZooPath value = selectItem.getValue();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, value.getFullName(), ButtonType.CLOSE);
            alert.showAndWait();
        });
        MenuItem refreshBtn = new MenuItem("Refresh");
        refreshBtn.setOnAction(event -> {
            TreeItem<ZooPath> selectItem = tree.getSelectionModel().getSelectedItem();
            ZooPath value = selectItem.getValue();
            selectItem.getChildren().removeAll();
            value.getChildren().clear();
        });
        menuList.add(addBtn);
        menuList.add(removeBtn);
        menuList.add(new SeparatorMenuItem());
        menuList.add(fullPathBtn);
        menuList.add(refreshBtn);
        return menuList;
    }

    private static String pathToString(ZooPath p) {
        if (p == null) {
            return "null";
        }
        return p.getName();
    }


}
