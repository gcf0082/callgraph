package com.gcf.callgraph.jacg.dto.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author adrninistrator
 * @date 2021/6/18
 * @description: 生成调用指定类的所有向上的调用关系时，使用的临时节点
 */

public class TmpNode4Callee {

    private String currentCalleeMethodHash;

    private String currentCallerMethodHash;

    private ObjectNode jsonNode;

    public static TmpNode4Callee genNode(String currentCalleeMethodHash, String currentCallerMethodHash) {
        TmpNode4Callee node = new TmpNode4Callee();
        node.setCurrentCalleeMethodHash(currentCalleeMethodHash);
        node.setCurrentCallerMethodHash(currentCallerMethodHash);
        return node;
    }

    //
    public String getCurrentCalleeMethodHash() {
        return currentCalleeMethodHash;
    }

    public void setCurrentCalleeMethodHash(String currentCalleeMethodHash) {
        this.currentCalleeMethodHash = currentCalleeMethodHash;
    }

    public String getCurrentCallerMethodHash() {
        return currentCallerMethodHash;
    }

    public void setCurrentCallerMethodHash(String currentCallerMethodHash) {
        this.currentCallerMethodHash = currentCallerMethodHash;
    }

    public ObjectNode getJsonNode() {
        return jsonNode;
    }

    public void setJsonNode(ObjectNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public ObjectNode addCaller(String methodHash, ObjectMapper mapper){
        ObjectNode tmpnode = mapper.createObjectNode();
        tmpnode.put("method", methodHash);
        if (jsonNode.has("children")) {
            jsonNode.withArray("children").add(tmpnode);
        } else {
            ArrayNode children = mapper.createArrayNode();

            children.add(tmpnode);
            jsonNode.put("children", children);
        }
        return tmpnode;
    }
}
