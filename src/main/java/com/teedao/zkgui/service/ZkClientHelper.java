package com.teedao.zkgui.service;

import com.teedao.zkgui.asyncTree.ZooPath;
import com.teedao.zkgui.model.NodeAcl;
import com.teedao.zkgui.model.NodeDetailInfo;
import com.teedao.zkgui.model.NodeMetaData;
import org.I0Itec.zkclient.ZkClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZkClientHelper {
    private static final Logger logger = LogManager.getLogger(ZkClientHelper.class);

    public static final int CONNECTION_TIME_OUT = 10 * 1000;
    public static final int SESSION_TIME_OUT = 60 * 60 * 1000;

    private static ConcurrentHashMap<String, ZkClient> clients = new ConcurrentHashMap<String, ZkClient>();

    private static String currentHost;


    public static synchronized void current(String hostPort) {
        logger.debug("connect to :{}", hostPort);
        currentHost = hostPort;
        connect(currentHost);
        logger.debug("connect success");
    }


    public static ZkClient connect(String hostPort) {
        currentHost = hostPort;
        if (clients.get(hostPort) != null) {
            return clients.get(hostPort);
        }
        ZkClient zkClient = new ZkClient(hostPort, SESSION_TIME_OUT, CONNECTION_TIME_OUT, new StringSerializer());
        clients.put(hostPort, zkClient);
        logger.debug("connect to zookeeper {}", hostPort);
        return zkClient;
    }


    public static void loadChildren(ZooPath parentPath) {
        ZkClient connect = connect(currentHost);
        List<String> list = connect.getChildren(parentPath.getFullName());
        logger.debug("load nodes [{}]:{}", parentPath, list);
        parentPath.addChildren(list);
    }


    public static boolean isDirectory(ZooPath parentPath) {

        ZkClient connect = connect(currentHost);
        List<String> list = connect.getChildren(parentPath.getFullName());
        return !list.isEmpty();
    }


    public static String getNodeData(String path) {
        String data = null;
        ZkClient connect = connect(currentHost);
        if (connect.exists(path)) {
            data = connect.readData(path, true);
        }
        logger.debug("get Node Data {}: {}", path, data);
        return data;
    }


    public static void addNode(String path, String nodeName,
                               String nodeValue, int createMode) throws Exception {
        ZkClient connect = connect(currentHost);
        if (connect.exists(path)) {
            connect.create(("/".equals(path) ? "" : path) + "/" + nodeName, nodeValue,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.fromFlag(createMode));
            logger.debug("add Node {}, data:{}", path, nodeValue);
        }
    }

    public static void removeNode(String path, int version) throws Exception {
        ZkClient connect = connect(currentHost);
        if (connect.exists(path)) {
            connect.delete(path, version);
            logger.debug("remove Node {}, version:{}", path, version);
        }
    }


    public static void updateNodeData(String path, String data, int version) throws Exception{
        ZkClient connect = connect(currentHost);
        if (connect.exists(path)) {
            connect.writeData(path, data, version);
            logger.debug("remove Node {}, version:{}", path, version);
        }
    }


    public static NodeMetaData getNodeMetaData(String path) {
        ZkClient connect = connect(currentHost);
        NodeMetaData metaData = new NodeMetaData();
        if (connect.exists(path)) {
            Map.Entry<List<ACL>, Stat> acl = connect.getAcl(path);
            Stat stat = acl.getValue();
            metaData.setAclVersion(stat.getAversion());
            metaData.setCreateTime(stat.getCtime());
            metaData.setCreationId(stat.getCzxid());
            metaData.setDataLength(stat.getDataLength());
            metaData.setDataVersion(stat.getVersion());
            metaData.setChildrenVersion(stat.getCversion());
            metaData.setEphemeralOwner(stat.getEphemeralOwner());
            metaData.setLastModifyTime(stat.getMtime());
            metaData.setModifyId(stat.getMzxid());
            metaData.setNumOfChildren(stat.getNumChildren());
            metaData.setNodeId(stat.getPzxid());
        }
        return metaData;
    }


    public static NodeDetailInfo getNodeDetail(String path) {
        ZkClient connect = connect(currentHost);
        NodeDetailInfo nodeDetailInfo = new NodeDetailInfo();
        String data = getNodeData(path);
        if (connect.exists(path)) {
            Map.Entry<List<ACL>, Stat> nodeDetail = connect.getAcl(path);
            NodeMetaData metaData = new NodeMetaData();
            Stat stat = nodeDetail.getValue();
            metaData.setAclVersion(stat.getAversion());
            metaData.setCreateTime(stat.getCtime());
            metaData.setCreationId(stat.getCzxid());
            metaData.setDataLength(stat.getDataLength());
            metaData.setDataVersion(stat.getVersion());
            metaData.setChildrenVersion(stat.getCversion());
            metaData.setEphemeralOwner(stat.getEphemeralOwner());
            metaData.setLastModifyTime(stat.getMtime());
            metaData.setModifyId(stat.getMzxid());
            metaData.setNumOfChildren(stat.getNumChildren());
            metaData.setNodeId(stat.getPzxid());

            List<NodeAcl> acls = new ArrayList<>();
            List<ACL> list = nodeDetail.getKey();
            for (ACL acl : list) {
                NodeAcl nodeAcl = new NodeAcl();
                nodeAcl.setId(acl.getId().getId());
                nodeAcl.setSchema(acl.getId().getScheme());
                int perms = acl.getPerms();
                StringBuilder sb = new StringBuilder();
                if ((perms & ZooDefs.Perms.READ) == ZooDefs.Perms.READ) {
                    sb.append("Read");
                    sb.append(", ");
                }
                if ((perms & ZooDefs.Perms.WRITE) == ZooDefs.Perms.WRITE) {
                    sb.append("Write");
                    sb.append(", ");
                }
                if ((perms & ZooDefs.Perms.CREATE) == ZooDefs.Perms.CREATE) {
                    sb.append("Create");
                    sb.append(", ");
                }
                if ((perms & ZooDefs.Perms.DELETE) == ZooDefs.Perms.DELETE) {
                    sb.append("Delete");
                    sb.append(", ");
                }
                if ((perms & ZooDefs.Perms.ADMIN) == ZooDefs.Perms.ADMIN) {
                    sb.append("Admin");
                }
                nodeAcl.setPermissions(sb.toString());
                acls.add(nodeAcl);
            }
            nodeDetailInfo.setData(data)
                    .setAcls(acls)
                    .setMetaData(metaData);
        }

        return nodeDetailInfo;
    }
}
