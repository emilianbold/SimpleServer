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

import quicksilver.commons.data.TSDataSetFactory;
import quicksilver.webapp.simpleui.bootstrap4.charts.TSFigurePanel;
import quicksilver.webapp.simpleui.bootstrap4.components.BSCard;
import quicksilver.webapp.simpleui.bootstrap4.components.BSPanel;
import quicksilver.webapp.simpleui.bootstrap4.quick.QuickBodyPanel;
import quicksilver.webapp.simpleui.html.components.HTMLLineBreak;
import tech.tablesaw.api.Table;
import tech.tablesaw.charts.ChartBuilder;

public class ChartsHistogram extends AbstractComponentsChartsPage {

    public ChartsHistogram() {
        super();
        toolbar.setActiveButton("Histo");
    }

    protected BSPanel createContentPanelCenter() {

        QuickBodyPanel body = new QuickBodyPanel();

        String divName = "histogramDiv";

        boolean autoSize = true;

        // Add Chart
        Table histogramTable = TSDataSetFactory.createSampleCountryEconomicData().getTSTable();

        ChartBuilder chartBuilder = ChartBuilder.createBuilder(ChartBuilder.CHART_RENDERER.EXPLORER)
                .dataTable(histogramTable)
                .chartType(ChartBuilder.CHART_TYPE.HISTOGRAM)
                //.columnsForViewColumns("Country")
                .columnsForViewRows("Population")
                .axisTitles("Population", "")
                ;

        if ( !autoSize ) {
            chartBuilder.layout(1000, 200, false);
        } else {
            chartBuilder.layout(false);
            chartBuilder.getLayoutBuilder()
                    .autosize(true)
                    .height(250);
        }

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "1").build(), divName + "1"),
                        "Histogram Chart")
        );

        if ( !autoSize ) {
            chartBuilder.layout(450, 200, false);
        }

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "2").build(), divName + "2"),
                        "Histogram Chart"),
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "3").build(), divName + "3"),
                        "Histogram Chart")
        );

        if ( !autoSize ) {
            chartBuilder.layout(300, 200, false);
        }

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "4").build(), divName + "4"),
                        "Histogram Chart"),
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "5").build(), divName + "5"),
                        "Histogram Chart"),
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "6").build(), divName + "6"),
                        "Histogram Chart")
        );

        BSPanel panel = new BSPanel();
        panel.add(new HTMLLineBreak(1));
        panel.add(body);
        panel.add(new HTMLLineBreak(2));

        return panel;

    }

}
