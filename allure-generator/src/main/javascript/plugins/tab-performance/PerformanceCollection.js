import {Collection} from 'backbone';

export default class PerformanceCollection extends Collection {
    url = 'data/performance.json';

    parse({pages}) {
        return pages;
    }
}