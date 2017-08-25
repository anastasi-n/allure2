package io.qameta.allure.history;

import io.qameta.allure.core.Configuration;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 
 * Empty trend manager.
 *
 * @param <T> items type.
 */
public class NullTrendManager<T extends Serializable> implements ITrendManager<T> {

    @Override
    public List<T> load(final Configuration configuration) throws IoTrendException {
        return Collections.emptyList();
    }

    @Override
    public void save(final Configuration configuration, final List<T> historyTrendItems)
            throws IoTrendException {
        //does nothing
    }
}
