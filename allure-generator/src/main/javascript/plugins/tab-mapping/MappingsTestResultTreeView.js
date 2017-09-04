import {className} from '../../decorators';
import MappingsTreeView from './MappingsTreeView';
import TestResultTreeView from '../../components/testresult-tree/TestResultTreeView';

@className('side-by-side')
class MappingsTestResultTreeView extends TestResultTreeView {

    createTreeView(tabName, baseUrl) {
        return new MappingsTreeView({
            collection: this.tree,
            routeState: this.routeState,
            treeSorters: [],
            tabName: tabName,
            baseUrl: baseUrl
        });
    }
}

export default MappingsTestResultTreeView;
