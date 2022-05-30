package com.diabetesapp.service;

import com.diabetesapp.model.DiabetesRecord;
import com.diabetesapp.repositories.DiabetesRecordsRepository;
import lombok.NoArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class LineChart extends JFrame {

    private DiabetesRecordsRepository diabetesRecordsRepository;

    private String chartCase;

    private Date startingDate;

    private Date endingDate;

    private byte[] producedImageOfChart;

    public LineChart(DiabetesRecordsRepository diabetesRecordsRepository, Date startingDate, Date endingDate, String chartCase) {
        this.diabetesRecordsRepository = diabetesRecordsRepository;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.chartCase = chartCase;
        initialiseUI();
    }

    private void initialiseUI() {
        XYDataset dataset = createDataset(startingDate, endingDate);
        JFreeChart chart = createChart(dataset);
        byte[] image = null;
        try {
            image = ChartUtils.encodeAsPNG(chart.createBufferedImage(450, 400));
        } catch (IOException e) {
            System.out.println(e);
        }
        producedImageOfChart = image;
    }

    private XYDataset createDataset(Date startingDate, Date endingDate) {
        TimeSeries dailyBloodGlucoseLevelSeries = new TimeSeries("daily blood glucose level");
        TimeSeries dailyCarbonIntakeSeries = new TimeSeries("daily carbon intake");
        List<DiabetesRecord> diabetesRecords = diabetesRecordsRepository.listSpecified(startingDate, endingDate);
        /*
           diabetesRecords.sort((date1, date2) -> date1.getDateRecorded().compareTo(date2.getDateRecorded()));
           statement below is equivalent to the one above.
        */
        diabetesRecords.sort(Comparator.comparing(DiabetesRecord::getDateRecorded));
        diabetesRecords.forEach((record) -> {
            switch (chartCase) {
                case "bloodGlucoseSingle":
                    dailyBloodGlucoseLevelSeries.addOrUpdate(new Day(record.getDateRecorded()),
                            record.getBloodGlucoseLevel());
                break;
                case "carbonIntakeSingle":
                    dailyCarbonIntakeSeries.addOrUpdate(new Day(record.getDateRecorded()),
                            record.getCarbIntake());
                break;
                case "bothInOne":
                    dailyBloodGlucoseLevelSeries.addOrUpdate(new Day(record.getDateRecorded()),
                            record.getBloodGlucoseLevel());
                    dailyCarbonIntakeSeries.addOrUpdate(new Day(record.getDateRecorded()),
                            record.getCarbIntake());
                break;
            }
        });
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        switch (chartCase) {
            case "bloodGlucoseSingle":
                    dataSet.addSeries(dailyBloodGlucoseLevelSeries);
                break;
            case "carbonIntakeSingle":
                    dataSet.addSeries(dailyCarbonIntakeSeries);
                break;
            case "bothInOne":
                    dataSet.addSeries(dailyBloodGlucoseLevelSeries);
                    dataSet.addSeries(dailyCarbonIntakeSeries);
                break;
        }
        return dataSet;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Diabetes Monitoring Chart",
                "Date",
                "Value (Daily Average)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.setTitle(new TextTitle("Diabetes Monitoring Chart",
            new Font("Serif", Font.BOLD, 18)));
        return chart;
    }

    public byte[] getChartImagePNG() {
        return producedImageOfChart;
    }

}
