import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { Button, Result, Tree } from 'antd';
import ProTable from '@ant-design/pro-table';
import React, { useState } from 'react';
import { history } from 'umi';
import { getCallerGraph } from '@/services/ant-design-pro/callgraph';


const treeData1 = `{
    "data": {
     "fullMethod": "org.apache.logging.log4j.core.net.JndiManager:getJndiManager(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.Properties)"
    },
    "key": 1,
    "children": [
     {
      "data": {
       "fullMethod": "org.apache.logging.log4j.core.net.JndiManager:createProperties(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.Properties)",
       "lineNum": 85
      },
      "children": [
       {
        "data": {
         "fullMethod": "java.util.Properties:<init>()",
         "lineNum": 131
        },
        "key": 2
       },
       {
        "data": {
         "fullMethod": "java.util.Properties:setProperty(java.lang.String,java.lang.String)",
         "lineNum": 132
        },
        "key": 3
       },
       {
        "data": {
         "fullMethod": "java.util.Properties:setProperty(java.lang.String,java.lang.String)",
         "lineNum": 134
        },
        "key": 4
       },
       {
        "data": {
         "fullMethod": "org.apache.logging.log4j.Logger:warn(java.lang.String,java.lang.Object)",
         "lineNum": 136
        },
        "key": 5
       },
       {
        "data": {
         "fullMethod": "java.util.Properties:setProperty(java.lang.String,java.lang.String)",
         "lineNum": 140
        },
        "key": 6
       },
       {
        "data": {
         "fullMethod": "java.util.Properties:setProperty(java.lang.String,java.lang.String)",
         "lineNum": 143
        },
        "key": 7
       },
       {
        "data": {
         "fullMethod": "java.util.Properties:setProperty(java.lang.String,java.lang.String)",
         "lineNum": 145
        },
        "key": 8
       },
       {
        "data": {
         "fullMethod": "org.apache.logging.log4j.Logger:warn(java.lang.String,java.lang.Object)",
         "lineNum": 147
        },
        "key": 9
       },
       {
        "data": {
         "fullMethod": "java.util.Properties:putAll(java.util.Map)",
         "lineNum": 152
        },
        "key": 10
       }
      ],
      "key": 11
     },
     {
      "data": {
       "fullMethod": "org.apache.logging.log4j.core.net.JndiManager:createManagerName()",
       "lineNum": 87
      },
      "children": [
       {
        "data": {
         "fullMethod": "java.lang.StringBuilder:<init>()",
         "lineNum": 103
        },
        "key": 12
       },
       {
        "data": {
         "fullMethod": "java.lang.Class:getName()",
         "lineNum": 103
        },
        "key": 13
       },
       {
        "data": {
         "fullMethod": "java.lang.StringBuilder:append(java.lang.String)",
         "lineNum": 103
        },
        "key": 14
       },
       {
        "data": {
         "fullMethod": "java.lang.StringBuilder:append(char)",
         "lineNum": 103
        },
        "key": 15
       },
       {
        "data": {
         "fullMethod": "java.lang.Object:hashCode()",
         "lineNum": 103
        },
        "key": 16
       },
       {
        "data": {
         "fullMethod": "java.lang.StringBuilder:append(int)",
         "lineNum": 103
        },
        "key": 17
       },
       {
        "data": {
         "fullMethod": "java.lang.StringBuilder:toString()",
         "lineNum": 103
        },
        "key": 18
       }
      ],
      "key": 19
     },
     {
      "data": {
       "fullMethod": "org.apache.logging.log4j.core.net.JndiManager:getManager(java.lang.String,org.apache.logging.log4j.core.appender.ManagerFactory,java.lang.Object)",
       "lineNum": 87
      },
      "key": 20
     }
    ]
   }`;


const CallGraph = (props) => {
    const onSelect = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    const titleRender = ({ title, key, data }) => {
        return <div>{data.fullMethod} </div>;
    }

    const getcaller = async () => {
        const result = await getCallerGraph();
        setTreeData(result)
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
