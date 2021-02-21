package com.bob.consistencyhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * hash环
 */
public class HashRing {

    private static MessageDigest md5 = null;
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("no md5 algrithm found");
        }
    }
    /**
     * 默认每个真实节点对应的虚拟节点个数
     */
    private static final int DEFAULT_VNODE_NUM=150;

    /**
     * 虚拟节点个数
     */
    private int vNodeNum;

    /**
     * 真实节点
     */
    private Set<Node> nodes = new LinkedHashSet<>();

    private TreeMap<Long,Node> vNodes = new TreeMap<>();

    public HashRing(){
        this(DEFAULT_VNODE_NUM);
    }

    public HashRing(int vNodeNum){
        this.vNodeNum = vNodeNum;
    }

    /**
     * 添加节点
     */
    public void addNode(Node node){
        if(nodes.contains(node))
            return;
        nodes.add(node);
        for(int i=0;i<vNodeNum;i++){
            String vNodeId = node.getId()+"_"+i;
            vNodes.put(hash(vNodeId),node);
        }
    }

    /**
     * hash算法
     * @param key
     * @return
     */
//    public static long hash(String key){
//        md5.reset();
//        md5.update(key.getBytes());
//        byte[] bKey = md5.digest();
//        //具体的哈希函数实现细节--每个字节 & 0xFF 再移位
//        long result = ((long) (bKey[3] & 0xFF) << 24)
//                | ((long) (bKey[2] & 0xFF) << 16
//                | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF));
//        return result & 0xffffffffL;
//    }

    /**
     * FNV
     * @param key
     * @return
     */
    public static long hash(String key){
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }
    /**
     * 定位节点
     * @param key
     * @return
     */
    public Node getNode(String key){
        SortedMap<Long, Node> tailMap = vNodes.tailMap(hash(key));
        return vNodes.get(tailMap.isEmpty()?vNodes.firstKey():tailMap.firstKey());
    }

    /**
     * 移除节点
     * @param node
     */
    public void removeNode(Node node){
        if(!nodes.contains(node))
            return;
        nodes.remove(node);
        for(int i=0;i<vNodeNum;i++) {
            String vNodeId = node.getId() + "_" + i;
            vNodes.remove(hash(vNodeId));
        }
    }

    /**
     * 打印数据分布
     */
    public void systemOutDataDistribute(){
        System.out.println("================");
        nodes.stream().forEach(node->{
            System.out.println(node.getId()+":"+node.dataSize());
        });
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
