import http from "../common/http";

//const basePath = './mock';
const basePath = '';

const api = {
    getFolderTree() {
        return http.get(`${basePath}/folderTree.json`);
    },

    search(value: string) {
        return http.get(`${basePath}/folderTree.json`, { query: value });
    },

    getDataSource() {
        return http.get(`${basePath}/dataSource.json`);
    },

    getSourceFile(project:string, className:string, linenum:number) {
        return http.get(`${basePath}/source_file?project_name=log4j&class=${className}&linenum=${linenum}`);        
    },    
    
    getCallees() {
        return http.get(`${basePath}/callees_method?project_name=log4j`);        
    },
    
    getCallerGraph(method: string) {
        //return http.get(`${basePath}/callerGraph.json`);
        return http.get(`${basePath}/caller_graph?project_name=log4j&method=${method}`);
    },

    getCalleeGraph(method: string) {
        //return http.get(`${basePath}/callerGraph.json`);
        return http.get(`${basePath}/callee_graph?project_name=log4j&method=${method}`);
    },    
    getDataSourceById(sourceId: string): Promise<DataSourceType> {
        return new Promise<DataSourceType>((resolve, reject) => {
            const mockDataSource: DataSourceType = {
                id: sourceId,
                name: `dataSource` + sourceId,
                type: 'MySQL',
                jdbcUrl: 'http://jdbc:127.0.0.1//3306',
                updateTime: Date.now() + ''
            }
            resolve(mockDataSource)
        });
    },

    createDataSource(dataSource: Omit<DataSourceType, 'id'>) {
        return new Promise((resolve, reject) => {
            resolve({
                code: 200,
                message: 'success',
                data: dataSource
            })
        });
    },

    async query(query: string = '') {
        const res = await http.get(`${basePath}/folderTree.json`);
        const result: any[] = [];
        const search = (nodeItem: any) => {
            if (!nodeItem) return;
            const target = nodeItem.name || '';
            if (target.includes(query) || query.includes(target)) {
                result.push(nodeItem);
            }
            if (nodeItem.children) {
                nodeItem.children.forEach((item: any) => { search(item) })
            }
        }
        search(res.data);

        return result;
    }
}

export default api;