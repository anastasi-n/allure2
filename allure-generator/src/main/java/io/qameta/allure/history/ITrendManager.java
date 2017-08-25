package io.qameta.allure.history;

import io.qameta.allure.core.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Trend manager.
 *
 * @param <T> items type
 */
public interface ITrendManager<T extends Serializable> {

    List<T> load(Configuration configuration) throws IoTrendException;

    void save(Configuration configuration, List<T> trendItems) throws IoTrendException;
}
