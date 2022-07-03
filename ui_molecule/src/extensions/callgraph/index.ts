import { IExtension } from '@dtinsight/molecule/esm/model/extension';
import { IExtensionService } from '@dtinsight/molecule/esm/services';
import molecule from '@dtinsight/molecule';
import { callGrapheSidebar } from './base';

export class CallGraphExtension implements IExtension {

    id: string = '';
    name: string = '';

    constructor(
        id: string = 'callgraph', 
        name: string = '调用链'
    ) {
        this.id = id;
        this.name = name;
    }

    activate(extensionCtx: IExtensionService): void {
        molecule.sidebar.add(callGrapheSidebar);        
        molecule.activityBar.add({
            id: this.id,
            icon: 'star',
        });        
    }

    dispose(extensionCtx: IExtensionService): void {
    }
}