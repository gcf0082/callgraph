import { ISidebarPane} from '@dtinsight/molecule/esm/model';
import molecule from '@dtinsight/molecule';
import { CallGraphSidebarView } from '../../views/callGraph/callGraphSidebar';
import 'antd/dist/antd.css'


export const callGrapheSidebar: ISidebarPane = {
    id: 'callgraph',
    title: '调用链',
    render: () => {
        return <CallGraphSidebarView/>;
    }
}