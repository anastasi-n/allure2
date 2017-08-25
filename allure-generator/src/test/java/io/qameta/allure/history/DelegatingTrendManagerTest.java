package io.qameta.allure.history;

import io.qameta.allure.core.Configuration;
import org.junit.Rule;
import org.junit.Test;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;
import uk.org.lidalia.slf4jtest.TestLoggerFactoryResetRule;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;

public class DelegatingTrendManagerTest
{

    private static final String ACTIVE_MANAGER = "mocked";
    private static final String TREND_NAME = "generic";

    private static final TestLogger TEST_LOGGER = TestLoggerFactory.getTestLogger(DelegatingTrendManager.class);
    private static final String WRONG_MANAGER = "wrong";

    @Rule
    public TestLoggerFactoryResetRule testLoggerFactoryResetRule = new TestLoggerFactoryResetRule();

    private DelegatingTrendManager<Serializable> delegatingTrendManager = new DelegatingTrendManager<>();

    @Test
    public void testLoad() throws Exception {
        ITrendManager mockedTrendManager = mockTrendManager(ACTIVE_MANAGER);
        Configuration configuration = mock(Configuration.class);
        delegatingTrendManager.load(configuration);
        verify(mockedTrendManager).load(configuration);
    }

    @Test
    public void testLoadWrongManager() throws Exception {
        mockTrendManager(WRONG_MANAGER);
        delegatingTrendManager.setTrendName(TREND_NAME);
        delegatingTrendManager.load(mock(Configuration.class));
        assertLogger();
    }

    @Test
    public void testSave() throws Exception {
        ITrendManager<Serializable> mockedTrendManager = mockTrendManager(ACTIVE_MANAGER);
        Configuration configuration = mock(Configuration.class);
        List<Serializable> items = Collections.emptyList();
        delegatingTrendManager.save(configuration, items);
        verify(mockedTrendManager).save(configuration, items);
    }

    @Test
    public void testSaveWrongManager() throws Exception {
        mockTrendManager(WRONG_MANAGER);
        delegatingTrendManager.setTrendName(TREND_NAME);
        delegatingTrendManager.save(mock(Configuration.class), Collections.emptyList());
        assertLogger();
    }

    private ITrendManager<Serializable> mockTrendManager(String activeManager) {
        @SuppressWarnings("unchecked")
        ITrendManager<Serializable> mockedTrendManager = mock(ITrendManager.class);
        delegatingTrendManager.setManagers(Collections.singletonMap(ACTIVE_MANAGER, mockedTrendManager));
        delegatingTrendManager.setActiveManager(activeManager);
        return mockedTrendManager;
    }

    private void assertLogger() {
        assertThat(TEST_LOGGER.getLoggingEvents(), is(Collections.singletonList(error(
                "{} trend is disabled. Reason: storage mode not recognized. Actual: {}, Expected one of: {}",
                TREND_NAME, WRONG_MANAGER, Collections.singleton(ACTIVE_MANAGER)))));
    }
}
