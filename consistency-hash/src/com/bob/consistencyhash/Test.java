package com.bob.consistencyhash;

public class Test {
    public static void main(String[] args){
        HashRing ring = new HashRing(150);
        //添加四个真实节点
        ring.addNode(new Node("node1"));
        ring.addNode(new Node("node2"));
        ring.addNode(new Node("node3"));
        ring.addNode(new Node("node4"));


        int count=420000;
        System.out.println("初始化start================");
        addData(ring, count);
        System.out.println("添加节点================");
        ring.addNode(new Node("node5"));
        addData(ring, count);
        System.out.println("删除节点================");
        ring.removeNode(new Node("node1"));
        addData(ring, count);

    }

    private static void addData(HashRing ring, int count) {
        for (int i = 0; i < count; i++) {
            String dataKey = "userId_" + i;
            String value = String.valueOf(i);
            Node node = ring.getNode(dataKey);
            node.put(dataKey, value);
        }
        ring.getNodes().stream().forEach(node -> {
            System.out.println(node.getId() + ":" + node.dataSize() + ",rate:" + 100 * node.dataSize() / count + "%");
        });
    }
}

//node1:2490
//        node2:2486
//        node3:2808
//        node4:2216
//
//        node1:2760
//        node2:1649
//        node3:1741
//        node4:3850