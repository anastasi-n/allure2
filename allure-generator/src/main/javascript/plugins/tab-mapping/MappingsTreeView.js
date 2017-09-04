import '../../components/tree/styles.scss';
import {behavior, className, regions} from '../../decorators';
import TreeView from '../../components/tree/TreeView';

@className('tree')
@behavior('TooltipBehavior', {position: 'bottom'})
@regions({
    sorter: '.tree__sorter',
    filter: '.tree__filter'
})
class MappingsTreeView extends TreeView {

    findTestResult(treeNode) {
        return this.$(`[data-uid='${treeNode.testResult}']`);
    }
}

export default MappingsTreeView;


