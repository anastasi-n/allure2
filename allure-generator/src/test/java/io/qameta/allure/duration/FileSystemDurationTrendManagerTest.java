package io.qameta.allure.duration;

import io.qameta.allure.context.JacksonContext;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.entity.TestResult;
import io.qameta.allure.entity.Time;
import io.qameta.allure.history.IoTrendException;
import io.qameta.allure.testdata.TestData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
public class FileSystemDurationTrendManagerTest {

    private static final String DURATION_TREND_JSON = "duration-trend.json";
    private static final String HISTORY_DIRECTORY = "history";
    private static final String DURATION = "duration";

    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    private final ExpectedException expectedException = ExpectedException.none();

    private final FileSystemDurationTrendManager durationTrendManager = new FileSystemDurationTrendManager();

    @Test
    public void shouldLoadNewData() throws Exception {
        final Path resultsDirectory = temporaryFolder.newFolder().toPath();
        final Path history = Files.createDirectories(resultsDirectory.resolve(HISTORY_DIRECTORY));
        final Path trend = history.resolve(DURATION_TREND_JSON);
        TestData.unpackFile(DURATION_TREND_JSON, trend);

        durationTrendManager.setHistoryDirectory(history);
        List<DurationTrendItem> result = durationTrendManager.load(mockConfiguration());

        assertEquals(2, result.size());
        assertEquals(Long.valueOf(85981), result.get(0).getData().get(DURATION));
        assertEquals(Long.valueOf(84444), result.get(1).getData().get(DURATION));
    }

    @Test
    public void testLoadFileNotExists() throws Exception {
        durationTrendManager.setHistoryDirectory(Paths.get("not/exists"));
        assertTrue("Time tend items are absent", durationTrendManager.load(mock(Configuration.class)).isEmpty());
    }

    @Test
    public void testSave() throws Exception {
        durationTrendManager.setHistoryDirectory(temporaryFolder.newFolder().toPath());
        Configuration configuration = mockConfiguration();
        
        DurationTrendItem item = new DurationTrendItem();
        item.updateTime(new TestResult().setTime(Time.create(100L, 200L)));

        List<DurationTrendItem> saved = Collections.singletonList(item);
        durationTrendManager.save(configuration, saved);
        DurationTrendItem s = durationTrendManager.load(configuration).get(0);
        assertEquals("Loaded duration equal to saved", durationTrendManager.load(configuration).get(0).getData()
                .get(DURATION), item.getData().get(DURATION));
    }

    @Test
    @PrepareForTest({Files.class, FileSystemDurationTrendManager.class})
    public void testSaveIOException() throws Exception {
        expectedException.expect(IoTrendException.class);
        Path historyDirectory = temporaryFolder.newFolder().toPath();
        durationTrendManager.setHistoryDirectory(historyDirectory);
        mockStatic(Files.class);
        when(Files.createDirectories(historyDirectory)).thenThrow(IOException.class);
        durationTrendManager.save(mock(Configuration.class), Collections.emptyList());
    }

    private Configuration mockConfiguration() {
        final Configuration configuration = mock(Configuration.class);
        when(configuration.requireContext(JacksonContext.class)).thenReturn(new JacksonContext());
        return configuration;
    }
}
