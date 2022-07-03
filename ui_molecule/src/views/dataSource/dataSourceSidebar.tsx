import React from 'react';
import molecule from '@dtinsight/molecule';
import { Header, Content } from '@dtinsight/molecule/esm/workbench/sidebar';
import { IActionBarItemProps, ITreeNodeItemProps } from '@dtinsight/molecule/esm/components';
import { ICollapseItem } from '@dtinsight/molecule/esm/components/collapse';
import { localize } from '@dtinsight/molecule/esm/i18n/localize';

import API from '../../api';
import DataSourceDetail from '../../components/dataSource/detail';
import { openCreateDataSourceView } from '../../extensions/dataSource/base';

import { IEditorTab} from '@dtinsight/molecule/esm/model';
import {Position, Range} from '@dtinsight/molecule/esm/monaco';

import { Input } from '@dtinsight/molecule/esm/components/input';
//import {Button} from '@dtinsight/molecule/esm/components/button';
import {Button} from 'antd'
import 'antd/dist/antd.css'

const Tree = molecule.component.TreeView;
const Toolbar = molecule.component.Toolbar;
const Collapse = molecule.component.Collapse;
export class DataSourceSidebarView extends React.Component {

    state = {
        data: [],
        callerGraph:[],
        callerMethod:'',
        currentDataSource: undefined
    }

    handleInputChange(e:any) {
        //console.log(e.target.value);
        this.setState({
            callerMethod:e.target.value
        })
    }

    componentDidMount() {
        this.fetchData();
        //this.fetchCallerGraph();
        molecule.event.EventBus.subscribe('addDataSource', () => { this.reload() });
    }

    async fetchData() {
        const res = await API.getDataSource();
        if (res.message === 'success') {
            this.setState({
                data: res.data.children || []
            });
        }
    }     

    transferCallerGraph2TreeJson(callGraph:any) {
        let id = 0
        function insertNodeIntoTree(node:any, newNode:any) {
            if (node.hasOwnProperty('lineNum')) {
                newNode.data = {};
                newNode.data.fullMethod = node.method_full;
                newNode.data.lineNum = node.lineNum;
                newNode.name = node.method_full;
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
                newNode.name = node.method_full;
            }
            id++;
            newNode.id = id;
        
            if (node.children != null) {
                newNode.fileType = 'Folder';
                newNode.isLeaf = false;
                newNode.children = new Array(node.children.length);
                for (let i = 0; i < node.children.length; i++) {
                    newNode.children[i] = {}
                    insertNodeIntoTree(node.children[i], newNode.children[i]);
                }
            } else {
                newNode.fileType = 'File';
                newNode.isLeaf = true;
            }
        }  
        
        var newNode = {};      
        insertNodeIntoTree(callGraph, newNode)
        console.log(callGraph);
        console.log(JSON.stringify(newNode,null,'\t'));
        return [newNode];      
    }

    async fetchCallerGraph() {
        console.log(this.state.callerMethod);
        const res = await API.getCallerGraph();
        if (res.message === 'success') {           
            this.setState({
                callerGraph: this.transferCallerGraph2TreeJson(res.data) || []
            });
        }
    }    

    fetchDataSource = async (id: string) => {
        const dataSource = await API.getDataSourceById(id);
        this.setState({ currentDataSource : dataSource });
    }

    reload() {
        this.fetchData();
    }

    create() {
        openCreateDataSourceView();
    }

    selectedSource = (node: ITreeNodeItemProps) => {
        if (node.isLeaf) {
            this.fetchDataSource(node.id as string);
        }
    }

    selectedMethod = (node: ITreeNodeItemProps) => {
        const tabData: IEditorTab = {
            id: node.id,
            name:node.name,
            data: {
                path: 'ddd',
                value:'abc\n123\nddd',
            },
 
        };
        molecule.editor.open(tabData);

        setTimeout(()=>{     if (molecule.editor.editorInstance != null) {
            //molecule.editor.editorInstance.setSelection(new Range(2,2,2,5));
            molecule.editor.editorInstance.setPosition(new Position(2,2))
        }}, )
   
        
        //console.log();
    }    

    renderHeaderToolbar(): IActionBarItemProps[] {
        return [
            {
                icon: 'refresh',
                id: 'reload',
                title: 'Reload',
                onClick: () => this.reload()
            }, {
                icon: 'add',
                id: 'addDataSource',
                title: 'Create Data Source',
                onClick: () => this.create()
            }
        ]
    }

    renderTitle(node: ITreeNodeItemProps, index: number, isLeaf: boolean)  {
        console.log('renderTitle');
        return (<div><h1>ddd</h1> <h2>kkk</h2></div>);
    }

    renderCollapse(): ICollapseItem[] {
        const dataSource: DataSourceType | undefined = this.state.currentDataSource;
        return [
            {
                id: 'DataSourceList',
                name: 'Catalogue',
                renderPanel: () => {
                    return (
                        <Tree data={this.state.data} onSelect={this.selectedSource}/>
                    )
                }
            },
            {
                id: 'DataSourceDetail',
                name: 'Detail',
                renderPanel: () => {
                    return (
                        <DataSourceDetail dataSource={dataSource}/>
                    )
                }
            },
            {
                id: 'callgraph',
                name: '调用链',
                renderPanel: () => {
                    return (
                        <div>
                            <Button type='primary'>xxx</Button>
                        <Input placeholder="please input large size" 
                        onPressEnter={(e) =>this.fetchCallerGraph()} 
                        onChange={(e) =>this.handleInputChange(e)}/>
                        <Tree data={this.state.callerGraph} onSelect={this.selectedMethod} renderTitle={this.renderTitle}/>
                        </div>
                    )
                }
            }
        ]
    }

    render() {
        return (
            <div className="dataSource" style={{width: '100%', height: '100%' }}>
                <Header title={ localize('demo.dataSourceManagement', "DataSource Management") } toolbar={
                    <Toolbar data={this.renderHeaderToolbar()} />
                }/>
                <Content>
                    <Collapse data={this.renderCollapse()} />
                </Content>
            </div>
        );
    }
}

export default DataSourceSidebarView;
