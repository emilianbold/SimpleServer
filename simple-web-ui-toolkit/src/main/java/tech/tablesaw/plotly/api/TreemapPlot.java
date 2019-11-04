package tech.tablesaw.plotly.api;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.TreemapTrace;

public class TreemapPlot {

    public static Figure create(String title, Table table, boolean familyTree, String[] cols, Map<String, String> attCols, Map<String, Object> attDefaults, Map<String, Function<List<Object>, Object>> attAggregates, Map<String, Function<Object, Object>> attPresenters) {
        return create(Layout.builder(title).build(), table, familyTree, cols, attCols, attDefaults, attAggregates, attPresenters);
    }

    /**
     * @param cols the columns in hierarchy order (smallest element first,
     * parent last)
     */
    public static Figure create(Layout layout, Table table, boolean familyTree, String[] cols, Map<String, String> attCols, Map<String, Object> attDefaults, Map<String, Function<List<Object>, Object>> attAggregates,  Map<String, Function<Object, Object>> attPresenters) {
        Extract.TableInfo info = Extract.createPairs(table, familyTree, cols, attCols, attDefaults, attAggregates, attPresenters);
        Object[] labels = info.labels;
        Object[] labelParents = info.labelParents;

        return create(layout, info.ids, labels, labelParents, info.attributeLists);
    }

    public static Figure create(String title, String[] ids, Object[] labels, Object[] labelParents) {
        return create(Layout.builder(title).build(), ids, labels, labelParents, null);
    }

    public static Figure create(Layout layout, String[] ids, Object[] labels, Object[] labelParents, Map<String, Object[]> extra) {
        sanitize(labels);
        sanitize(labelParents);
        TreemapTrace trace = TreemapTrace.builder(
                ids,
                labels,
                labelParents,
                extra)
                .build();
        return new Figure(layout, trace);
    }

    //see https://github.com/jtablesaw/tablesaw/pull/699
    static void sanitize(Object[] list) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] instanceof String) {
                list[i] = ((String) list[i]).replaceAll("\\'", "\\\\'");
            }
        }
    }

}
