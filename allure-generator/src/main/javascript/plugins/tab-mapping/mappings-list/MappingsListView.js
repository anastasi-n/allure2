import './styles.scss';
import DataGridView from '../../../components/data-grid/DataGridView';
import {on, className} from '../../../decorators';
import template from './MappingsListView.hbs';
import router from '../../../router';

@className('mappings-list')
class MappingsListView extends DataGridView {
    template = template;
    settingsKey = 'mappingsSettings';

    initialize({state}) {
        this.state = state;
        this.listenTo(state, 'change:testResult', this.highlightTestcase, this);
    }

    onRender() {
        const state = this.state;
        this.highlightTestcase(state, state.get('testResult'));
    }

    @on('click .mappings-list__mapping-row')
    onMappingClick(e) {
        this.$(e.currentTarget).toggleClass('mappings-list__mapping-row_expanded');
    }

    @on('click .mappings-list__story-row')
    onStoryClick(e) {
        this.$(e.currentTarget).toggleClass('mappings-list__story-row_expanded');
    }

    @on('click .mappings-list__testcase')
    onTestCaseClick(e) {
        const testCaseId = this.$(e.currentTarget).data('uid');
        router.to('mappings/' + testCaseId);
    }

    highlightTestcase(testcase) {
        if (testcase) {
            const testcaseId = testcase.get('testResult');
            this.highlightItem(testcaseId);
            const expandedStory = this.$el.find('.mappings-list__testcase[data-uid="' + testcaseId + '"]')
                .parent().find(`.${this.className}__story-row`);
            expandedStory.addClass(`${this.className}__story-row_expanded`);
            expandedStory.parent().parent().find(`.${this.className}__mapping-row`)
            .addClass(`${this.className}__mapping-row_expanded`);
        }
    }

    serializeData() {
        return {
            sorting: this.getSettings(),
            mappingTypes: this.collection.toJSON().map(type =>
                Object.assign({}, type, {
                    mappings: this.applySort(type.mappings)
                })
            )
        };
    }
}

export default MappingsListView;
