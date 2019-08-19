package com.teedao.zkgui.service;

import com.teedao.zkgui.asyncTree.ZooPath;
import com.teedao.zkgui.model.NewNodeRequest;
import com.teedao.zkgui.model.NodeDetailInfo;
import com.teedao.zkgui.model.NodeMetaData;

import java.util.HashMap;
import java.util.Map;

public class ZkService {
    private static Map<String, Integer> CREATE_MODEL_MAP = new HashMap<String, Integer>();

    static {
        CREATE_MODEL_MAP.put("PERSISTENT", 0);
        CREATE_MODEL_MAP.put("PERSISTENT_SEQUENTIAL", 2);
        CREATE_MODEL_MAP.put("EPHEMERAL", 1);
        CREATE_MODEL_MAP.put("EPHEMERAL_SEQUENTIAL", 3);
    }


    public static void addNode(NewNodeRequest request) throws Exception {
        int model = CREATE_MODEL_MAP.get(request.getMode());
        ZooPath parent = request.getParent();
        ZkClientHelper.addNode(parent == null ? "/" : parent.getFullName(), request.getName(), request.getValue(), model);
    }

    public static void updateNode(String path, String data) {
        NodeMetaData metaData = ZkClientHelper.getNodeMetaData(path);
        int version = metaData.getDataVersion();
        ZkClientHelper.updateNodeData(path, data, version);
    }


    public static void removeNode(ZooPath zooPath) throws Exception {
        String path = zooPath.getFullName();
        NodeMetaData metaData = ZkClientHelper.getNodeMetaData(path);
        int version = metaData.getDataVersion();
        ZkClientHelper.removeNode(path, version);
    }


    public static NodeDetailInfo getNodeDetail(ZooPath zooPath) {
        String path = zooPath.getFullName();
        NodeDetailInfo nodeDetailInfo = ZkClientHelper.getNodeDetail(path);
        return nodeDetailInfo;
    }


}
