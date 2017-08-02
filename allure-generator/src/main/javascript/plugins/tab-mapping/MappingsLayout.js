import PaneLayout from '../../layouts/pane/PaneLayout';
import MappingsCollection from './mappings-collection/MappingsCollection';
import MappingsListView from './mappings-list/MappingsListView';
import router from '../../router';

export default class MappingsLayoutView extends PaneLayout {

    initialize() {
        super.initialize();
        this.mappingsCollection = new MappingsCollection();
    }

    loadData() {
        return this.mappingsCollection.fetch();
    }

    onStateChange() {
        const state = this.state;
        const changed = Object.assign({}, state.changed);
        const paneView = this.getChildView('content');
        paneView.expanded = state.get('expanded');
        if(!paneView.getRegion('mappings')) {
            paneView.addPane('mappings', new MappingsListView({
                baseUrl: 'mappings',
                collection: this.mappingsCollection,
                state
            }));
        }
        this.testResult.updatePanes('mappings', changed);
        paneView.updatePanesPositions();
    }

    onRouteUpdate(testResult, attachment) {
        const expanded = router.getUrlParams().expanded === 'true';
        this.state.set({testResult, attachment, expanded});
    }
}
