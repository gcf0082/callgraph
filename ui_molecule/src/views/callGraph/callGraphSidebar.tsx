import molecule from '@dtinsight/molecule';
import React, { useState } from 'react';
import { IEditorTab } from '@dtinsight/molecule/esm/model';
import { IActionBarItemProps, ITreeNodeItemProps } from '@dtinsight/molecule/esm/components';
import { Header, Content } from '@dtinsight/molecule/esm/workbench/sidebar';
import { ICollapseItem } from '@dtinsight/molecule/esm/components/collapse';
import API from '../../api';
import { Button, Input, Tree, Select,List} from 'antd'
import {Position, Range} from '@dtinsight/molecule/esm/monaco';
import InfiniteScroll from 'react-infinite-scroll-component';
import 'antd/dist/antd.dark.css';
import { NONAME } from 'dns';

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
            WebkitUserSelect:'text',
            MozUserSelect:'text',
            msUserSelect:'text'
          }}>
            <Button onClick={() => fetchCallees()}>获取所有调用函数</Button>
            <List
                size="small"
                dataSource={callees}
                renderItem={item => <List.Item>{item}</List.Item>}  
                 
            />

        </div>
    );
}

export function CallGraphView() {
    const [treeData, setTreeData] = useState([] as any);
    const [caller_method, setCallerMethod] = useState('');

    const transferCallGraph2TreeJson = (callGraph: any, callerFlag:boolean) => {   
        let id = 0;
        let parentNode:any = null;             
        function insertNodeIntoTree(node: any, newNode: any) {
            //console.log(node.method_full);
            newNode.data = {};
            newNode.data.fullMethod = node.method_full;
            newNode.title = node.method_full;
            if (node.hasOwnProperty('lineNum')) {
                newNode.data.lineNum = node.lineNum;
            }
            if (callerFlag && parentNode != null) {
                newNode.data.callerMethod = parentNode.method_full;
            }
            id++;
            newNode.key = id;

            if (node.children != null) {
                if (callerFlag) {
                    parentNode = node;
                }                
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
        const callgraph = transferCallGraph2TreeJson(res, true);
        console.log(callgraph);
        setTreeData(callgraph);
    }

    const fetchCalleeGraph = async () => {
        //console.log(caller_method);
        const res = await API.getCalleeGraph(caller_method);
        //console.log(res);
        const callgraph = transferCallGraph2TreeJson(res, false);
        console.log(callgraph);
        setTreeData(callgraph);
    }      

    const fetchSourceFile = async (project:string, className:string, linenum:number) => {
        //console.log(caller_method);
        const res = await API.getSourceFile(project, className, linenum);
        console.log(res);
        return res;
    }    

  

    const titleRender = ({ title, key, data }: any) => {
        let fullClassName = data.fullMethod.split(':')[0];
        let method = data.fullMethod.split(':')[1];
        let methodName = method.split('(')[0];
        let classname = fullClassName.substring(fullClassName.lastIndexOf('.')+1);
        let args = '()';
        if (method.indexOf('()') == -1) {
            args = '(..)';
        }
        return <div style={{color:'#9f9fa3'}}>
            <span>{fullClassName}:</span>
            <span  style={{fontWeight: 'bold'}}>{methodName}{args}</span>
            </div>;
    }

    const onSelect = async(selectedKeys: any, info: any) => {
        if (info.node.data.lineNum == null) {
            return;
        }

        let method = '';
        if (info.node.data.callerMethod == null) {
            method = info.node.data.fullMethod
        } else {
            method = info.node.data.callerMethod
        }
        const res = await API.getSourceFile('project', 
        method.split(':')[0], 
        info.node.data.lineNum);
        console.log(res);  
        const tabData: IEditorTab = {
            id: info.node.key,
            name: '查看代码',
            data: {
                path: 'ddd',
                language:'java',
                value: res.file_content,
            },
    
        };              
        console.log(info.node.data.fullMethod);
        molecule.editor.open(tabData);       
    
        setTimeout(() => {
            if (molecule.editor.editorInstance != null) {
                molecule.editor.editorInstance.setPosition(new Position(res.lineNum,1));
                molecule.editor.editorInstance.revealLineInCenter(res.lineNum);
                molecule.editor.editorInstance.deltaDecorations([],[
                    {
                        range: new Range(res.lineNum, 1, res.lineNum, 1),
                        options: {
                            isWholeLine: true,
                            className: 'myContentClass',
                            glyphMarginClassName : 'myGlyphMarginClass'
                        }
                    }
                ]
                    );
            }
        })
    };

    return (
        <div >
            <Input onChange={(e) => { setCallerMethod(e.target.value) }}></Input>
            <Button onClick={() => fetchCalleeGraph()}>获取向上调用链据</Button>
            <Button onClick={() => fetchCallerGraph()}>获取向下调用链据</Button>
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