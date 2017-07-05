import AppLayout from '../../layouts/application/AppLayout';
import PerformanceCollection from './PerformanceCollection';
import PerformanceView from './PerformanceView';

export default class PerformanceLayout extends AppLayout {

    initialize() {
        this.performanceCollection = new PerformanceCollection();
    }

    loadData() {
        return this.performanceCollection.fetch();
    }

    getContentView() {
        return new PerformanceView({collection: this.performanceCollection});
    }
}