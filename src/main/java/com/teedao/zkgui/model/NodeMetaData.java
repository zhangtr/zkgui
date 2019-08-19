package com.teedao.zkgui.model;

/**
 * 功能描述:
 * <p/>
 * 创建人: shoujun.yang
 * <p/>
 * 创建时间: 2019/1/31 2:15 PM.
 * <p/>
 */
public class NodeMetaData {

    private Integer aclVersion;

    private Long createTime;

    private Integer childrenVersion;

    private Long creationId;

    private Integer dataLength;

    private Long ephemeralOwner;

    private Long lastModifyTime;

    private Long modifyId;

    private Integer numOfChildren;

    private Long nodeId;

    private Integer dataVersion;

    public Integer getAclVersion() {
        return aclVersion;
    }

    public NodeMetaData setAclVersion(Integer aclVersion) {
        this.aclVersion = aclVersion;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public NodeMetaData setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getChildrenVersion() {
        return childrenVersion;
    }

    public NodeMetaData setChildrenVersion(Integer childrenVersion) {
        this.childrenVersion = childrenVersion;
        return this;
    }

    public Long getCreationId() {
        return creationId;
    }

    public NodeMetaData setCreationId(Long creationId) {
        this.creationId = creationId;
        return this;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public NodeMetaData setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
        return this;
    }

    public Long getEphemeralOwner() {
        return this.ephemeralOwner;
    }

    public NodeMetaData setEphemeralOwner(Long ephemeralOwner) {
        this.ephemeralOwner = ephemeralOwner;
        return this;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public NodeMetaData setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
        return this;
    }

    public Long getModifyId() {
        return modifyId;
    }

    public NodeMetaData setModifyId(Long modifyId) {
        this.modifyId = modifyId;
        return this;
    }

    public Integer getNumOfChildren() {
        return numOfChildren;
    }

    public NodeMetaData setNumOfChildren(Integer numOfChildren) {
        this.numOfChildren = numOfChildren;
        return this;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public NodeMetaData setNodeId(Long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public NodeMetaData setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
        return this;
    }

    @Override
    public String toString() {
        return "NodeMetaData{" +
                "aclVersion=" + aclVersion +
                ", createTime=" + createTime +
                ", childrenVersion=" + childrenVersion +
                ", creationId=" + creationId +
                ", dataLength=" + dataLength +
                ", ephemeralOwner=" + ephemeralOwner +
                ", lastModifyTime=" + lastModifyTime +
                ", modifyId=" + modifyId +
                ", numOfChildren=" + numOfChildren +
                ", nodeId=" + nodeId +
                ", dataVersion=" + dataVersion +
                '}';
    }
}
