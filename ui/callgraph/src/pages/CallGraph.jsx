import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { Button, Result, Tree } from 'antd';
import ProTable from '@ant-design/pro-table';
import React, { useState } from 'react';
import { history } from 'umi';


const treeData1 = [
    {
        title: '0-0',
        key: '0-0',
        data: { name: "yyyy" },
        children: [
            {
                title: '0-0-0',
                key: '0-0-0',
                data: { name: "yyyy" },
                children: [
                    {
                        title: '0-0-0-0',
                        key: '0-0-0-0',
                        data: { name: "yyyy" },
                    }
                ],
            },
            {
                title: '0-0-1',
                key: '0-0-1',
                data: { "name": "yyyy" },
                children: [
                    {
                        title: '0-0-1-0',
                        key: '0-0-1-0',
                        data: { name: "yyyy" },
                    },
                    {
                        title: '0-0-1-1',
                        key: '0-0-1-1',
                        data: { name: "zzz" },
                    }
                ],
            },
            {
                title: '0-0-2',
                key: '0-0-2',
                data: { "name": "www" },
            },
        ],
    }
];


const CallGraph = (props) => {
    const onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    const titleRender = ({ title, key, data }) => {
        return <div>{title} {data.name}</div>;
    }

    const [treeData, setTreeData] = useState([]);
    return (

        <PageHeaderWrapper>

            <Tree
                treeData={treeData}
                onSelect={onSelect}
                titleRender={titleRender}
            />



            <Button onClick={() => setTreeData(treeData1)}>获取数据</Button>
        </PageHeaderWrapper>


    );
};

export default CallGraph;
