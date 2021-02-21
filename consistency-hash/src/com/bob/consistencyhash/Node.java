package com.bob.consistencyhash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 节点
 */

public class Node {
    /***
     * 节点唯一标识
     */
    private String id;

    /**
     * 节点上的数据
     */
    private Map<String,Object> data = new ConcurrentHashMap<>();

    public Node(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void put(String key,Object value){
        data.put(key,value);
    }

    public int dataSize(){
        return data.size();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id.equals(node.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
