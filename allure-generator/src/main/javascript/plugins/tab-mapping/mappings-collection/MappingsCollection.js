import {Collection} from 'backbone';

export default class MappingsCollection extends Collection {
    url = 'data/mappings.json';

    initialize() {
        this.on('sync', this.updateMappings, this);
    }

    updateMappings() {
        this.each(type => {
            type.get('mappings').forEach(mapping => {
                mapping.type = type.get('type');
            });
        });
        this.allMappings = [].concat(...this.pluck('mappings'));
    }

    parse({mappingsCollection}) {
        return mappingsCollection;
    }
}
