package io.qameta.allure.history;

import io.qameta.allure.ConfigurationBuilder;
import io.qameta.allure.context.JacksonContext;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.entity.Statistic;
import io.qameta.allure.testdata.TestData;
import org.assertj.core.groups.Tuple;
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

import static io.qameta.allure.testdata.TestData.randomHistoryTrendItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
public class FileSystemHistoryTrendManagerTest
{
    private static final String HISTORY_TREND_JSON = "history-trend.json";

    @Rule
    private final ExpectedException expectedException = ExpectedException.none();

    @Rule
    private final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final FileSystemHistoryTrendManager historyTrendManager = new FileSystemHistoryTrendManager();

    @SuppressWarnings("unchecked")
    @Test
    public void shouldLoadOldData() throws Exception {
        final Path resultsDirectory = temporaryFolder.newFolder().toPath();
        final Path history = Files.createDirectories(resultsDirectory.resolve("history"));
        final Path trend = history.resolve(HISTORY_TREND_JSON);
        TestData.unpackFile("history-trend-old.json", trend);

        final Configuration configuration = mock(Configuration.class);
        when(configuration.requireContext(JacksonContext.class))
                .thenReturn(new JacksonContext());

        historyTrendManager.setHistoryDirectory(history);

        assertThat(historyTrendManager.load(configuration))
                .hasSize(4)
                .extracting(HistoryTrendItem::getStatistic)
                .extracting(Statistic::getTotal)
                .containsExactly(20L, 12L, 12L, 1L);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldLoadNewData() throws Exception {
        final Path resultsDirectory = temporaryFolder.newFolder().toPath();
        final Path history = Files.createDirectories(resultsDirectory.resolve("history"));
        final Path trend = history.resolve(HISTORY_TREND_JSON);
        TestData.unpackFile("history-trend.json", trend);

        final Configuration configuration = mock(Configuration.class);
        when(configuration.requireContext(JacksonContext.class))
                .thenReturn(new JacksonContext());

        historyTrendManager.setHistoryDirectory(history);
        List<HistoryTrendItem> result = historyTrendManager.load(configuration);

        assertThat(result)
                .hasSize(4)
                .extracting(HistoryTrendItem::getStatistic)
                .extracting(Statistic::getTotal)
                .containsExactly(20L, 12L, 12L, 1L);

        assertThat(result)
                .hasSize(4)
                .extracting(HistoryTrendItem::getBuildOrder,
                        HistoryTrendItem::getReportName, HistoryTrendItem::getReportUrl)
                .containsExactly(
                        Tuple.tuple(7L, "some", "some/report#7"),
                        Tuple.tuple(6L, "some", "some/report#6"),
                        Tuple.tuple(5L, "some", "some/report#5"),
                        Tuple.tuple(4L, "some", "some/report#4")
                );
    }

    @Test
    public void testLoadFileNotExists() throws Exception
    {
        historyTrendManager.setHistoryDirectory(Paths.get("not/exists"));
        assertTrue("History tend items are absent", historyTrendManager.load(mock(Configuration.class)).isEmpty());
    }

    @Test
    @PrepareForTest({ Files.class, FileSystemHistoryTrendManager.class })
    public void testLoadIOException() throws Exception
    {
        final Path history = Files.createDirectories(temporaryFolder.newFolder().toPath().resolve("history"));
        final Path trend = history.resolve(HISTORY_TREND_JSON);
        TestData.unpackFile("history-trend-old.json", trend);
        expectedException.expect(IoTrendException.class);
        expectedException.expectMessage("Could not read trend file " + trend);
        historyTrendManager.setHistoryDirectory(history);
        mockStatic(Files.class);
        when(Files.newInputStream(trend)).thenThrow(new IOException());
        historyTrendManager.load(mock(Configuration.class));
    }

    @Test
    public void testSave() throws Exception
    {
        historyTrendManager.setHistoryDirectory(temporaryFolder.newFolder().toPath());
        Configuration configuration = getConfiguration();
        List<HistoryTrendItem> saved = randomHistoryTrendItems();;
        historyTrendManager.save(configuration, saved);
        assertEquals("Loaded history trend items equal to saved", historyTrendManager.load(configuration), saved);
    }

    @Test
    @PrepareForTest({Files.class, FileSystemHistoryTrendManager.class})
    public void testSaveIOException() throws Exception
    {
        expectedException.expect(IoTrendException.class);
        Path historyDirectory = temporaryFolder.newFolder().toPath();
        historyTrendManager.setHistoryDirectory(historyDirectory);
        mockStatic(Files.class);
        when(Files.createDirectories(historyDirectory)).thenThrow(IOException.class);
        historyTrendManager.save(mock(Configuration.class), Collections.emptyList());
    }

    private Configuration getConfiguration()
    {
        return new ConfigurationBuilder().fromExtensions(Collections.singletonList(new JacksonContext())).build();
    }
}
