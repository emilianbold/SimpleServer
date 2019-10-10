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

package quicksilver.commons.datafeed;

import java.io.IOException;
import quicksilver.commons.data.TSDataSet;
import tech.tablesaw.api.Table;

public class DataFeedTest {

    public static void main(String[] args) throws IOException {

        DataFeed dataFeed;
        Table dataTable;

        // Test CSV
        dataFeed = new DataFeedCSV("https://raw.githubusercontent.com/jtablesaw/tablesaw/master/data/bush.csv");
        dataFeed.request();
        dataTable = dataFeed.getDataTable();
        System.out.print(dataTable.structure());
        System.out.print(dataTable.print());

        // Test JSON
        dataFeed = new DataFeedCSV("http://someurl.com/file.json");
        dataFeed.request();
        dataTable = dataFeed.getDataTable();
        System.out.print(dataTable.structure());
        System.out.print(dataTable.print());

    }

}