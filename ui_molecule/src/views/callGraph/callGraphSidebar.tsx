import molecule from '@dtinsight/molecule';
import React, { useState } from 'react';
import { IEditorTab } from '@dtinsight/molecule/esm/model';
import { IActionBarItemProps, ITreeNodeItemProps } from '@dtinsight/molecule/esm/components';
import { Header, Content } from '@dtinsight/molecule/esm/workbench/sidebar';
import { ICollapseItem } from '@dtinsight/molecule/esm/components/collapse';
import API from '../../api';
import { Button, Input, Tree, Select,List } from 'antd'
import InfiniteScroll from 'react-infinite-scroll-component';
import 'antd/dist/antd.dark.css';

const Toolbar = molecule.component.Toolbar;
const Collapse = molecule.component.Collapse;
const { Option } = Select;

function test1() {
    const tabData: IEditorTab = {
        id: 123,
        name: 'my.txt',
        data: {
            path: 'ddd',
            value: 'abc\n123\nddd',
        },

    };
    molecule.editor.open(tabData);

    setTimeout(() => {
        if (molecule.editor.editorInstance != null) {
            //molecule.editor.editorInstance.setSelection(new Range(2,2,2,5));
            //molecule.editor.editorInstance.setPosition(new Position(2,2))
        }
    })

}

function renderHeaderToolbar(): IActionBarItemProps[] {
    return [
        {
            icon: 'file',
            id: 'selectProject',
            title: '选择项目',
        }, {
            icon: 'add',
            id: 'addProject',
            title: '添加项目',
        }
    ]
}

export function ManageProjectView() {

    const handleChange = (value: string) => {
        localStorage.setItem('current_proj', value);
        console.log(`selected ${value}`);
    };

    const getCurrentProject = () => {
        return localStorage.getItem('current_proj');
    }

    return (
        <div >
            <p>选择项目</p>
            <Select defaultValue="log4j" style={{ width: 120 }} onChange={handleChange}>
                <Option value="log4j">log4j</Option>
                <Option value="test">test</Option>
            </Select>

            <p>当前项目:  {getCurrentProject()}</p>


        </div>
    );
}

export function OverviewView() {
    const fetchCallees = async () => {
        const res = await API.getCallees();
        setCallees(res);

    }    
    const [callees, setCallees] = useState([] )
    return (
        <div     style={{
            height: 1000,
            overflow: 'auto',
            padding: '0 16px',
            border: '1px solid rgba(140, 140, 140, 0.35)',
          }}>
            <Button onClick={() => fetchCallees()}>获取所有调用函数</Button>

            <List
                size="small"
                dataSource={callees}
                renderItem={item => <List.Item><Button>go</Button>{item}</List.Item>}  
                 
            />

        </div>
    );
}

export function CallGraphView() {
    const [treeData, setTreeData] = useState([] as any);
    const [caller_method, setCallerMethod] = useState('');

    const transferCallerGraph2TreeJson = (callGraph: any) => {
        let id = 0
        function insertNodeIntoTree(node: any, newNode: any) {
            //console.log(node.method_full);
            newNode.data = {};
            newNode.data.fullMethod = node.method_full;
            newNode.title = node.method_full;
            if (node.hasOwnProperty('lineNum')) {
                newNode.data.lineNum = node.lineNum;
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

        var newNode = {};
        insertNodeIntoTree(callGraph, newNode)
        //console.log(callGraph);
        //console.log(JSON.stringify(newNode, null, '\t'));
        return [newNode];
    }
    const fetchCallerGraph = async () => {
        //console.log(caller_method);
        const res = await API.getCallerGraph(caller_method);
        //console.log(res);
        const callgraph = transferCallerGraph2TreeJson(res);
        //console.log('=========ok');
        setTreeData(callgraph);
        /*
        if (res.message === 'success') {
            const callgraph = transferCallerGraph2TreeJson(res.data);
            console.log(callgraph);
            setTreeData([callgraph]);
        }*/
    }

    const titleRender = ({ title, key, data }: any) => {
        return <div>{data.lineNum}:{data.fullMethod}</div>;
    }

    const onSelect = (selectedKeys: any, info: any) => {
        console.log('selected', selectedKeys, info);
    };

    return (
        <div >
            <Input onChange={(e) => { setCallerMethod(e.target.value) }}></Input>
            <Button onClick={() => fetchCallerGraph()}>获取数据</Button>
            <Tree
                treeData={treeData}
                onSelect={onSelect}
                titleRender={titleRender}
                //style={{overflow:'scroll', width:'100%', height:'100%'}}
                height={600}
            />

        </div>
    )
}

function renderCollapse(): ICollapseItem[] {

    return [
        {
            id: 'manageProject',
            name: '管理项目',
            renderPanel: () => {
                return (<ManageProjectView />)
            }
        },
        {
            id: 'overview',
            name: '项目概要',
            renderPanel: () => {
                return (<OverviewView />)
            }
        },
        {
            id: 'callgraph',
            name: '调用链',
            renderPanel: () => {
                return (<CallGraphView />)
            }
        },
        {
            id: 'codeSnippet',
            name: '代码片段',
            renderPanel: () => {
                return (
                    <div>
                        <Button type='primary'>xxx</Button>

                    </div>
                )
            }
        }
    ]
}

export function CallGraphSidebarView() {
    return (
        <div className="callGraph" style={{ width: '100%', height: '100%' }}>
            <Header title={'查看调用链'} toolbar={
                <Toolbar data={renderHeaderToolbar()} />
            } />
            <Content>
                <Collapse data={renderCollapse()} />
            </Content>
        </div>

    );

}