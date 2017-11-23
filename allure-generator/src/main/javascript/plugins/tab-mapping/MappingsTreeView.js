import '../../components/tree/styles.scss';
import {behavior, className, regions} from '../../decorators';
import TreeViewContainer from '../../components/tree-view-container/TreeViewContainer';

@className('tree')
@behavior('TooltipBehavior', {position: 'bottom'})
@regions({
    search: '.pane__search',
    sorter: '.tree__sorter',
    filter: '.tree__filter',
    content: '.tree__content'
})
class MappingsTreeView extends TreeViewContainer {

    findTestResult(treeNode) {
        return this.$(`[data-uid='${treeNode.testResult}']`);
    }
}

export default MappingsTreeView;


