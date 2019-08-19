package com.teedao.zkgui.asyncTree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ZooPath implements Comparable<ZooPath> {
    private String name;
    private ZooPath parent;
    final HashMap<String, ZooPath> children;


    public ZooPath(ZooPath parent, String name) {
        this.name = name;
        this.children = new HashMap();
        this.parent = parent;
    }


    public void addChild(String childName) {
        synchronized (this.children) {
            if (!this.children.containsKey(childName)) {
                ZooPath child = new ZooPath(this, childName);
                this.children.put(childName, child);
            }
        }
    }

    public void addChildren(List<String> childrenNames) {
        synchronized (this.children) {
            for (String childName : childrenNames) {
                if (!this.children.containsKey(childName)) {
                    ZooPath child = new ZooPath(this, childName);
                    this.children.put(childName, child);
                }
            }
        }
    }

    public void deleteChild(String childName) {
        synchronized (this.children) {
            if (this.children.containsKey(childName)) {
                this.children.remove(childName);
            }
        }
    }

    public ZooPath getChild(String childName) {
        synchronized (this.children) {
            return !this.children.containsKey(childName) ? null : this.children.get(childName);
        }
    }

    public Collection<ZooPath> getChildren() {
        synchronized (this.children) {
            return this.children.values();
        }
    }

    public String[] getChildrenName() {
        synchronized (this.children) {
            return this.children.keySet().toArray(new String[0]);
        }
    }

    public String getFullName() {
        if (parent == null) {
            return name;
        } else {
            if ("/".equals(parent.getFullName())) {
                return "/" + name;
            } else {
                return parent.getFullName() + "/" + name;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " : [");
        synchronized (this.children) {
            Iterator var3 = this.children.keySet().iterator();
            while (var3.hasNext()) {
                String str = (String) var3.next();
                sb.append("," + str);
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZooPath getParent() {
        return parent;
    }

    public void setParent(ZooPath parent) {
        this.parent = parent;
    }


    @Override
    public int compareTo(ZooPath o) {
        return this.name.compareTo(o.name);
    }
}

