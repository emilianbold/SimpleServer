/*
 * Copyright 2018-2019 Niels Gron and Contributors All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quicksilver.webapp.simpleserver.controllers.root.components.charts;

import java.time.LocalDate;
import quicksilver.commons.data.TSDataSetFactory;
import quicksilver.webapp.simpleserver.controllers.root.components.tables.TableData;
import quicksilver.webapp.simpleui.bootstrap4.charts.*;
import quicksilver.webapp.simpleui.bootstrap4.components.BSCard;
import quicksilver.webapp.simpleui.bootstrap4.components.BSPanel;
import quicksilver.webapp.simpleui.bootstrap4.quick.QuickBodyPanel;
import quicksilver.webapp.simpleui.html.components.HTMLLineBreak;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.charts.ChartBuilder;
import tech.tablesaw.charts.impl.calheatmap.CalHeatmapLayout;
import tech.tablesaw.plotly.components.Layout;

public class ChartsAll extends AbstractComponentsChartsPage {

    public ChartsAll() {
    }

    protected BSPanel createContentPanelCenter() {

        QuickBodyPanel body = new QuickBodyPanel();

        Table economicTable = TSDataSetFactory.createSampleCountryEconomicData().getTSTable();
        Table stockEquitiesTable = TSDataSetFactory.createSampleStockMarketEquities().getTSTable();
        Table stockPricesTable = null;

        try {
            stockPricesTable = TableData.loadStockPrices(LocalDate.of(2019, 9, 10), LocalDate.of(2019, 9, 24));
        } catch ( Exception e ) {
            stockPricesTable = TSDataSetFactory.createSampleStockPrices().getTSTable();
        }

        // Add Vertical Bar Chart
        // Add Horizontal Bar Chart
        Table hBarTable = economicTable;

        ChartBuilder vbarChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(hBarTable)
                .chartType(ChartBuilder.CHART_TYPE.VERTICAL_BAR, Layout.BarMode.STACK)
                .layout(500, 200, false)
                .columnsForViewColumns("Continent")
                .columnsForViewRows("GDP")
                .columnForColor("Country")
                ;
        ChartBuilder hbarChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(hBarTable)
                .chartType(ChartBuilder.CHART_TYPE.HORIZONTAL_BAR)
                .layout(500, 200, false)
                .columnsForViewColumns("Country")
                .columnsForViewRows("GDP")
                ;

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(vbarChartBuilder.divName("vbarDiv").build(), "vbarDiv"),
                        "Vertical Bar Chart"),
                new BSCard(new TSFigurePanel(hbarChartBuilder.divName("hbarDiv").build(), "hbarDiv"),
                        "Horizontal Bar Chart")
        );

        // Add Line Chart
        // Add Area Chart
        Table lineTable = economicTable;
        Table areaTable = economicTable;

        ChartBuilder lineChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(lineTable)
                .chartType(ChartBuilder.CHART_TYPE.LINE)
                .layout(500, 200, false)
                .columnsForViewColumns("Country")
                .columnsForViewRows("GDP")
                ;
        ChartBuilder areaChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(areaTable)
                .chartType(ChartBuilder.CHART_TYPE.AREA)
                .layout(500, 200, false)
                .columnsForViewColumns("Country")
                .columnsForViewRows("GDP")
                ;

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(lineChartBuilder.divName("lineDiv").build(), "lineDiv"),
                        "Line Chart"),
                new BSCard(new TSFigurePanel(areaChartBuilder.divName("areaDiv").build(), "areaDiv"),
                        "Area Chart")
        );

        // Add Pie Chart
        // Add Timeseries Chart
        Table pieTable = economicTable;
        Table timeSeriesTable = stockPricesTable;

        ChartBuilder pieChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(pieTable)
                .chartType(ChartBuilder.CHART_TYPE.PIE)
                .layout(500, 200, false)
                .columnsForViewColumns("Country")
                .columnsForViewRows("GDP")
                ;
        ChartBuilder timeseriesChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(timeSeriesTable)
                .chartType(ChartBuilder.CHART_TYPE.TIMESERIES)
                .layout(500, 200, false)
                .columnsForViewColumns("Date")
                .columnsForViewRows("Close")
                ;

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(pieChartBuilder.divName("pieDiv").build(), "pieDiv"),
                        "Pie Chart"),
                new BSCard(new TSFigurePanel(timeseriesChartBuilder.divName("timeseriesDiv").build(), "timeseriesDiv"),
                        "Timeseries Chart")
        );

        // Add Scatter Plot Chart
        // Add Bubble Chart
        Table scatterTable = economicTable;
        Table bubbleTable = economicTable;

        DoubleColumn oldColumn = (DoubleColumn) bubbleTable.column("GDP_Capita");
        DoubleColumn newColumn = DoubleColumn.create("GDP_Capita");
        newColumn = newColumn.emptyCopy(oldColumn.size());

        oldColumn.mapInto((Double p) -> { return p / 1000; }, newColumn);
        bubbleTable.replaceColumn(newColumn);

        ChartBuilder scatterChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(scatterTable)
                .chartType(ChartBuilder.CHART_TYPE.SCATTERPLOT)
                .columnsForViewColumns("Population")
                .columnsForViewRows("GDP")
                .layout(500, 200, false)
                .axisTitles("Population", "GDP")
                ;
        ChartBuilder bubbleChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(bubbleTable)
                .chartType(ChartBuilder.CHART_TYPE.BUBBLE)
                .columnsForViewColumns("Population")
                .columnsForViewRows("GDP")
                .columnForSize("GDP_Capita")
                .layout(500, 200, false)
                .axisTitles("Population", "GDP")
                ;

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(scatterChartBuilder.divName("scatterDiv").build(), "scatterDiv"),
                        "Scatter Chart"),
                new BSCard(new TSFigurePanel(bubbleChartBuilder.divName("bubbleDiv").build(), "bubbleDiv"),
                        "Bubble Chart")
        );

        // Add Histogram Chart
        // Add Heatmap Chart
        Table histogramTable = economicTable;
        Table heatmapTable = stockEquitiesTable;

        ChartBuilder histogramChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(histogramTable)
                .chartType(ChartBuilder.CHART_TYPE.HISTOGRAM)
                .layout(500, 200, false)
                //.columnsForViewColumns("Country")
                .columnsForViewRows("Population")
                ;
        ChartBuilder heatmapChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(heatmapTable)
                .chartType(ChartBuilder.CHART_TYPE.HEATMAP)
                .layout(500, 200, false)
                .columnsForViewColumns("Company", "Sector")
                //.columnsForViewRows("Close")
                ;

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(histogramChartBuilder.divName("histogramDiv").build(), "histogramDiv"),
                        "Histogram Chart"),
                new BSCard(new TSFigurePanel(heatmapChartBuilder.divName("heatmapDiv").build(), "heatmapDiv"),
                        "Heatmap Chart")
        );

        // Add Candlestick Chart
        // Add Open High Low Close Chart
        Table candleStickTable = stockPricesTable;
        Table ohlcTable = stockPricesTable;

        ChartBuilder candlestickChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(candleStickTable)
                .chartType(ChartBuilder.CHART_TYPE.CANDLESTICK)
                .layout(500, 200, false)
                .columnsForViewColumns("Date")
                .columnsForViewRows("Open", "High", "Low", "Close")
                ;
        ChartBuilder ohlcChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(ohlcTable)
                .chartType(ChartBuilder.CHART_TYPE.OHLC)
                .layout(500, 200, false)
                .columnsForViewColumns("Date")
                .columnsForViewRows("Open", "High", "Low", "Close")
                ;

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(candlestickChartBuilder.divName("candlestickDiv").build(), "candlestickDiv"),
                        "Candlestick Chart"),
                new BSCard(new TSFigurePanel(ohlcChartBuilder.divName("ohlcDiv").build(), "ohlcDiv"),
                        "OHLC Chart")
        );

        // Add Treemap Chart
        // Add Sunburst Chart
        Table treemapTable = stockEquitiesTable;
        Table sunburstTable = stockEquitiesTable;

        ChartBuilder treemapBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(treemapTable)
                .chartType(ChartBuilder.CHART_TYPE.TREEMAP)
                .columnsForViewColumns("Company", "Sector")
                .layout(500, 200, false);

        ChartBuilder sunburstBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(sunburstTable)
                .chartType(ChartBuilder.CHART_TYPE.SUNBURST)
                .columnsForViewColumns("Company", "Sector")
                .columnForSize("MarketCap")
                .layout(500, 200, false);

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(treemapBuilder.divName("treemapDiv").build(), "treemapDiv"),
                        "Treemap Chart"),
                new BSCard(new TSFigurePanel(sunburstBuilder.divName("sunburstDiv").build(), "sunburstDiv") ,
                        "Sunburst Chart")
        );

        // Add Calendar Heatmap
        Table calendarHeatmapTable = stockPricesTable;

        ChartBuilder calendarHeatmapChartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.CALHEATMAP)
                .dataTable(calendarHeatmapTable)
                .chartType(ChartBuilder.CHART_TYPE.HEATMAP_CALENDAR)
                .layout(500, 200, false)
                .columnsForViewColumns("Date")
                .columnsForViewRows("Volume")
                ;

        CalHeatmapLayout.LayoutBuilder layoutBuilder = (CalHeatmapLayout.LayoutBuilder)calendarHeatmapChartBuilder.getLayoutBuilder();

        layoutBuilder.domain("month")
                .subDomain("x_day")
                .cellSize(20)
                .domainGutter(14)
                .range(3)
                .highlight(LocalDate.of(2019,10, 15))
                .subDomainTextFormat("%d");


        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(calendarHeatmapChartBuilder.divName("calHeat1Div").build(), "calHeat1Div"),
                        "Calendar Heatmap Chart"),
                new BSCard(new TSFigurePanel(calendarHeatmapChartBuilder.divName("calHeat2Div").build(), "calHeat2Div"),
                        "Calendar Heatmap Chart")
        );


        BSPanel panel = new BSPanel();
        panel.add(new HTMLLineBreak(1));
        panel.add(body);
        panel.add(new HTMLLineBreak(2));

        return panel;
    }


}
