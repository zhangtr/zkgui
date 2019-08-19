package com.teedao.zkgui.model;

/**
 * 功能描述:
 * <p/>
 * 创建人: shoujun.yang
 * <p/>
 * 创建时间: 2019/1/31 2:15 PM.
 * <p/>
 */
public class NodeAcl {

    private String schema;

    private String id;

    private String permissions;

    public String getSchema() {
        return schema;
    }

    public NodeAcl setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public String getId() {
        return id;
    }

    public NodeAcl setId(String id) {
        this.id = id;
        return this;
    }

    public String getPermissions() {
        return permissions;
    }

    public NodeAcl setPermissions(String permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public String toString() {
        return "NodeAcl{" +
                "schema='" + schema + '\'' +
                ", id='" + id + '\'' +
                ", permissions='" + permissions + '\'' +
                '}';
    }
}
