package com.teedao.zkgui.service;

import com.teedao.zkgui.asyncTree.ZooPath;
import com.teedao.zkgui.model.NewNodeRequest;
import com.teedao.zkgui.model.NodeDetailInfo;
import com.teedao.zkgui.model.NodeMetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ZkService {
    private static final Logger logger = LogManager.getLogger(ZkService.class);

    private static Map<String, Integer> CREATE_MODEL_MAP = new HashMap<String, Integer>();

    static {
        CREATE_MODEL_MAP.put("PERSISTENT", 0);
        CREATE_MODEL_MAP.put("PERSISTENT_SEQUENTIAL", 2);
        CREATE_MODEL_MAP.put("EPHEMERAL", 1);
        CREATE_MODEL_MAP.put("EPHEMERAL_SEQUENTIAL", 3);
    }

    public static synchronized void current(String hostPort) {
        ZkClientHelper.current(hostPort);
        logger.info("zookeeper server: {} ", hostPort);
    }

    public static void addNode(NewNodeRequest request) throws Exception {
        int model = CREATE_MODEL_MAP.get(request.getMode());
        ZooPath parent = request.getParent();
        String path = (parent == null ? "/" : parent.getFullName() + "/") + request.getName();
        ZkClientHelper.addNode(parent == null ? "/" : parent.getFullName(), request.getName(), request.getValue(), model);
        logger.info("add Node , path: {} , value :{} , model : {}", path, request.getValue(), request.getMode());
    }

    public static void updateNode(String path, String data) throws Exception {
        NodeMetaData metaData = ZkClientHelper.getNodeMetaData(path);
        int version = metaData.getDataVersion();
        ZkClientHelper.updateNodeData(path, data, version);
        logger.info("update Node value , path:{} ,before{} , after:{}", path, ZkClientHelper.getNodeData(path), data);

    }


    public static void removeNode(ZooPath zooPath) throws Exception {
        String path = zooPath.getFullName();
        NodeMetaData metaData = ZkClientHelper.getNodeMetaData(path);
        int version = metaData.getDataVersion();
        ZkClientHelper.removeNode(path, version);
        logger.info("remove Node, path : {} , version :{} ", path, version);

    }


    public static NodeDetailInfo getNodeDetail(ZooPath zooPath) {
        String path = zooPath.getFullName();
        NodeDetailInfo nodeDetailInfo = ZkClientHelper.getNodeDetail(path);
        return nodeDetailInfo;
    }


}
