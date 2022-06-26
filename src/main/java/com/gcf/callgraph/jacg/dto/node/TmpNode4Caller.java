package com.gcf.callgraph.jacg.dto.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gcf.callgraph.jacg.common.DC;

import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/6/18
 * @description: 生成指定类调用的所有向下的调用关系时，使用的临时节点
 */

public class TmpNode4Caller {

    private String currentCalleeMethodHash;

    private int currentCalleeMethodId;

    private ObjectNode jsonNode;

    public static TmpNode4Caller genNode(String currentMethodHash, int currentMethodId) {
        TmpNode4Caller node = new TmpNode4Caller();
        node.setCurrentCalleeMethodHash(currentMethodHash);
        node.setCurrentCalleeMethodId(currentMethodId);
        return node;
    }

    //

    public String getCurrentCalleeMethodHash() {
        return currentCalleeMethodHash;
    }

    public void setCurrentCalleeMethodHash(String currentCalleeMethodHash) {
        this.currentCalleeMethodHash = currentCalleeMethodHash;
    }

    public int getCurrentCalleeMethodId() {
        return currentCalleeMethodId;
    }

    public void setCurrentCalleeMethodId(int currentCalleeMethodId) {
        this.currentCalleeMethodId = currentCalleeMethodId;
    }

    public ObjectNode getJsonNode() {
        return jsonNode;
    }

    public void setJsonNode(ObjectNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public ObjectNode addCallee(Map<String, Object> calleeMethodMap, ObjectMapper mapper){
        ObjectNode tmpnode = mapper.createObjectNode();
        tmpnode.put("method_hash", (String)calleeMethodMap.get(DC.MC_CALLEE_METHOD_HASH));
        tmpnode.put("method_full", (String)calleeMethodMap.get(DC.MC_CALLEE_FULL_METHOD));
        tmpnode.put("lineNum", (int)calleeMethodMap.get(DC.MC_CALLER_LINE_NUM));
        if (jsonNode.has("children")) {
            jsonNode.withArray("children").add(tmpnode);
        } else {
            ArrayNode children = mapper.createArrayNode();

            children.add(tmpnode);
            jsonNode.set("children", children);
        }
        return tmpnode;
    }
}
