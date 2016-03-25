package org.conf2.data;

/**
 *
 */
public class NodeKeyPair {
    public Node node;
    public Value[] key;

    public NodeKeyPair(Node n) {
        this.node = n;
    }

    public NodeKeyPair(Node n, Value[] key) {
        this.node = n;
        this.key = key;
    }
}
