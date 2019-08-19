package com.teedao.zkgui.model;

import java.util.List;

/**
 * 功能描述:
 * <p/>
 * 创建人: shoujun.yang
 * <p/>
 * 创建时间: 2019/1/31 2:14 PM.
 * <p/>
 */
public class NodeDetailInfo {

    private String data;

    private NodeMetaData metaData;

    private List<NodeAcl> acls;

    public String getData() {
        return data;
    }

    public NodeDetailInfo setData(String data) {
        this.data = data;
        return this;
    }

    public NodeMetaData getMetaData() {
        return metaData;
    }

    public NodeDetailInfo setMetaData(NodeMetaData metaData) {
        this.metaData = metaData;
        return this;
    }

    public List<NodeAcl> getAcls() {
        return acls;
    }

    public NodeDetailInfo setAcls(List<NodeAcl> acls) {
        this.acls = acls;
        return this;
    }

    @Override
    public String toString() {
        return "NodeDetailInfo{" +
                "data='" + data + '\'' +
                ", metaData=" + metaData +
                ", acls=" + acls +
                '}';
    }
}
