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

public class ChartsVBar extends AbstractComponentsChartsPage {

    public ChartsVBar() {
        super();
        toolbar.setActiveButton("VBar");
    }

    protected BSPanel createContentPanelCenter() {

        QuickBodyPanel body = new QuickBodyPanel();

        String divName = "vbarDiv";

        // Add Chart
        Table table = TSDataSetFactory.createSampleCountryEconomicData().getTSTable();

        ChartBuilder chartBuilder = ChartBuilder.createBuilder()
                .dataTable(table)
                .chartType(ChartBuilder.CHART_TYPE.VERTICAL_BAR)
                .layout(500, 200, false)
                .rowColumns("Country")
                .dataColumns("GDP")
                .axisTitles("Country", "GDP")
                ;

        chartBuilder.layout(1000, 200, 5, 40, 45, 5, false);

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "1").build(), divName + "1"),
                        "Wide Chart")
        );

        chartBuilder.layout(450, 200, false);

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "2").build(), divName + "2"),
                        "Half Width Chart"),
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "3").build(), divName + "3"),
                        "Half Width Chart")
        );

        chartBuilder.layout(300, 200, false);

        body.addRowOfColumns(
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "4").build(), divName + "4"),
                        "Narrow Chart"),
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "5").build(), divName + "5"),
                        "Narrow Chart"),
                new BSCard(new TSFigurePanel(chartBuilder.divName(divName + "6").build(), divName + "6"),
                        "Narrow Chart")
        );

        body.doLayout();

        BSPanel panel = new BSPanel();
        panel.add(new HTMLLineBreak(1));
        panel.add(body);
        panel.add(new HTMLLineBreak(2));

        return panel;

    }

}
