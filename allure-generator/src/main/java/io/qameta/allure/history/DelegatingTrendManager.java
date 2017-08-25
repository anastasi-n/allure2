package io.qameta.allure.history;

import io.qameta.allure.core.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * Delegating trend manager.
 *
 * @param <T> type of trend manager.
 */
public class DelegatingTrendManager<T extends Serializable> implements ITrendManager<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingTrendManager.class);

    private String trendName;
    private String activeManager;
    private Map<String, ITrendManager<T>> managers;

    @Override
    public List<T> load(final Configuration configuration) throws IoTrendException {
        return getActiveManager().load(configuration);
    }

    @Override
    public void save(final Configuration configuration, final List<T> historyTrendItems)
            throws IoTrendException {
        getActiveManager().save(configuration, historyTrendItems);
    }

    private ITrendManager<T> getActiveManager() {
        ITrendManager<T> manager = managers.get(activeManager);
        if (manager == null) {
            LOGGER.error(
                    "{} trend is disabled. Reason: storage mode not recognized. Actual: {}, Expected one of: {}",
                    trendName, activeManager, managers.keySet());
            manager = new NullTrendManager<>();
        }
        return manager;
    }

    public void setTrendName(final String trendName) {
        this.trendName = trendName;
    }

    public void setActiveManager(final String activeManager) {
        this.activeManager = activeManager;
    }

    public void setManagers(final Map<String, ITrendManager<T>> managers) {
        this.managers = managers;
    }
}
