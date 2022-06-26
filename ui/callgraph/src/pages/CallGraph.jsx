import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { Button, Result, Tree } from 'antd';
import ProTable from '@ant-design/pro-table';
import React, { useState } from 'react';
import { history } from 'umi';
import { getCallerGraph } from '@/services/ant-design-pro/callgraph';

let id = 0
function insertNodeIntoTree(node, newNode) {
    if (node.hasOwnProperty('lineNum')) {
        newNode.data = {};
        newNode.data.fullMethod = node.method_full;
        newNode.data.lineNum = node.lineNum;
        if (node.children != null) {
            newNode.children = new Array(node.children.length);
            for (let i = 0; i < node.children.length; i++) {
                newNode.children[i] = {}
                insertNodeIntoTree(node.children[i], newNode.children[i]);
            }
        }
    } else {
        newNode.data = {};
        newNode.data.fullMethod = node.method_full;
    }
    id++;
    newNode.key = id;

    if (node.children != null) {
        newNode.children = new Array(node.children.length);
        for (let i = 0; i < node.children.length; i++) {
            newNode.children[i] = {}
            insertNodeIntoTree(node.children[i], newNode.children[i]);
        }
    }
}

const CallGraph = (props) => {
    const onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    const titleRender = ({ title, key, data }) => {
        return <div>{data.lineNum}:{data.fullMethod} </div>;
    }

    const getcaller = async () => {
        const result = await getCallerGraph();
        var newNode = {};
        console.log(result);
        insertNodeIntoTree(result, newNode);
        console.log(newNode);
        setTreeData([newNode]);
    }

    const [treeData, setTreeData] = useState([]);
    return (

        <PageHeaderWrapper>

            <Tree
                treeData={treeData}
                onSelect={onSelect}
                titleRender={titleRender}
            />



            <Button onClick={() => getcaller()}>获取数据</Button>
        </PageHeaderWrapper>


    );
};

export default CallGraph;
