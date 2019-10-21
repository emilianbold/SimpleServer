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

import quicksilver.webapp.simpleui.bootstrap4.charts.TSHorizontalBarChartPanel;
import quicksilver.webapp.simpleui.bootstrap4.components.BSCard;
import quicksilver.webapp.simpleui.bootstrap4.components.BSPanel;
import quicksilver.webapp.simpleui.bootstrap4.quick.QuickBodyPanel;
import quicksilver.webapp.simpleui.html.components.HTMLLineBreak;
import tech.tablesaw.api.Table;

public class ChartsHBar extends AbstractComponentsChartsPage {

    public ChartsHBar() {
        super();
        toolbar.setActiveButton("HBar");
    }

    protected BSPanel createContentPanelCenter() {

        QuickBodyPanel body = new QuickBodyPanel();

        // Add Chart
        Table table = Charts.createPieDataSet(true);

        body.addRowOfColumns(
            new BSCard( new TSHorizontalBarChartPanel(table, "div1", 200, 900, true), "Wide Chart")
        );

        body.addRowOfColumns(
            new BSCard( new TSHorizontalBarChartPanel(table, "div2", 200, 450, true), "Half Wide Chart"),
            new BSCard( new TSHorizontalBarChartPanel(table, "div3", 200, 450, true), "Half Wide Chart")
        );

        body.addRowOfColumns(
            new BSCard( new TSHorizontalBarChartPanel(table, "div4", 200, 300, true), "Narrow Chart"),
            new BSCard( new TSHorizontalBarChartPanel(table, "div5", 200, 300, true), "Narrow Chart"),
            new BSCard( new TSHorizontalBarChartPanel(table, "div6", 200, 300, true), "Narrow Chart")
        );

        body.doLayout();

        BSPanel panel = new BSPanel();
        panel.add(new HTMLLineBreak(1));
        panel.add(body);
        panel.add(new HTMLLineBreak(2));

        return panel;

    }

}
