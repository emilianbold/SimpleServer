package tech.tablesaw.plotly.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.jetty.util.TopologicalSort;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

class Extract {

    static TableInfo createPairs(Table table, String[] cols, Map<String, String> attCols, Map<String, Object> attDefaults) {
        return createPairs(table, false, cols, attCols, attDefaults, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    static TableInfo createPairs(Table table, boolean familyTree, String[] cols, Map<String, String> attCols,
            Map<String, Object> attDefaults,
            Map<String, Function<List<Object>, Object>> attAggregates,
            Map<String, Function<Object, Object>> attPresenters) {
        if (cols.length < 2) {
            throw new IllegalStateException("At least two columns needed");
        }
        if (attCols == null) {
            attCols = new HashMap<>();
        }
        //it's important the (child, parent) pairs map is sorted to build the list more easily later on
        TreeMap<String, String> pairs = new TreeMap<>();
        TreeMap<String, String>[] idPairs = new TreeMap[cols.length];
        //for a family tree each column gets an id from the same pool
        TreeMap<String, String> globalIds = familyTree ? new TreeMap<>() : null;
        for (int i = 0; i < idPairs.length; i++) {
            idPairs[i] = familyTree ? globalIds : new TreeMap<>();
        }

        Map<String, Map<String, Object>> attributes = new HashMap<>();
        pairs(table, cols, attCols, new IdGenerator() {
            int[] colCounter = new int[cols.length];

            @Override
            public String id(Row row, int colSubIndex, String column) {
                if (familyTree) {
                    //same counter
                    int v = colCounter[0]++;
                    return "f-" + v;
                } else {
                    int v = colCounter[colSubIndex]++;
                    return colSubIndex + "-" + v;
                }
            }
        }, pairs, idPairs, attributes);

        List<String> ids = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> parents = new ArrayList<>();
        Map<String, List<Object>> attributeLists = new HashMap<>();
        attCols.forEach((name, colName) -> {
            attributeLists.put(name, new ArrayList<>());
        });

        for (TreeMap<String, String> id : idPairs) {
            for (Map.Entry<String, String> e : id.entrySet()) {
                String label = e.getKey();
                String labelId = e.getValue();

                if (!ids.contains(labelId)) {
                    ids.add(labelId);
                    labels.add(label);

                    String parentId = pairs.get(labelId);
                    if (parentId == null) {
                        parentId = "";
                    }

                    parents.add(parentId);
                }
            }
        }

        if (!attCols.isEmpty()) {
            //add some attributes for parents too
            List<String> sortedIds = new ArrayList<>(ids);
            //XXX: Using helper class from Jetty, something similar will have to be re-implemented here.
            TopologicalSort<String> topology = new TopologicalSort<>();
            pairs.forEach((childId, parentId) -> topology.addDependency(childId, parentId));
            topology.sort(sortedIds);
            Collections.reverse(sortedIds);

            attCols.forEach((name, colName) -> {
                if (attAggregates.containsKey(name)) {
                    //prepare aggregate list for parents
                    Map<String, List<Object>> futureAggregates = new HashMap<>();

                    for (String labelId : sortedIds) {
                        Map<String, Object> attrs = attributes.get(labelId);
                        //is it a parent?
                        if (futureAggregates.containsKey(labelId)) {
                            if (attrs == null) {
                                attrs = new HashMap<>();
                                attributes.put(labelId, attrs);
                            }
                            Function<List<Object>, Object> aggregate = attAggregates.get(name);
                            attrs.put(name, aggregate.apply(futureAggregates.get(labelId)));
                            //clean up
                            futureAggregates.remove(labelId);
                        }

                        if (attrs != null && attrs.containsKey(name)) {
                            String parentId = pairs.get(labelId);
                            if (parentId != null) {
                                List<Object> agg = futureAggregates.computeIfAbsent(parentId, (x) -> new ArrayList<>());

                                agg.add(attrs.get(name));
                            }
                        }
                    }
                }

                ids.stream().map(labelId -> {
                    Function<Object, Object> presenter = attPresenters.get(name);
                    if(presenter == null) {
                        presenter = (o) -> o;
                    }

                    Map<String, Object> attrs = attributes.get(labelId);

                    if (attrs != null && attrs.containsKey(name)) {
                        return presenter.apply(attrs.get(name));
                    } else {
                        return presenter.apply(attDefaults.get(name));
                    }
                }).collect(Collectors.toCollection(() -> attributeLists.get(name)));
            });

        }

        Map<String, Object[]> attributeArrays = new HashMap<>();
        attributeLists.forEach((name, list) -> attributeArrays.put(name, list.toArray()));

        return new TableInfo(ids.stream().toArray(String[]::new), labels.toArray(), parents.toArray(), attributeArrays);
    }

    static class TableInfo {

        String[] ids;
        Object[] labels;
        Object[] labelParents;
        Map<String, Object[]> attributeLists;

        public TableInfo(String[] ids, Object[] labels, Object[] labelParents, Map<String, Object[]> attributeLists) {
            this.ids = ids;
            this.labels = labels;
            this.labelParents = labelParents;
            this.attributeLists = attributeLists;
        }

    }

    interface IdGenerator {

        /**
         *
         * @param row the current row
         * @param colSubIndex the column index within the column cohort used (no
         * relation to the row column index)
         * @param column the colum name
         * @return global unique id for the given column
         */
        String id(Row row, int colSubIndex, String column);
    }

    private static void pairs(Table table, String[] cols, Map<String, String> attCols, IdGenerator idGen,
            TreeMap<String, String> pairs, TreeMap<String, String>[] ids, Map<String, Map<String, Object>> attributes) {
        for (Row row : table) {
            String child = row.getString(cols[0]);

            //generate child id
            String childId = ids[0].get(child);
            if (childId == null) {
                childId = idGen.id(row, 0, cols[0]);
                ids[0].put(child, childId);

                if (attCols != null && !attCols.isEmpty()) {
                    Map<String, Object> att = attributes.get(childId);
                    if (att == null) {
                        att = new HashMap<>();
                        attributes.put(childId, att);
                    }
                    for (Map.Entry<String, String> attribute : attCols.entrySet()) {
                        att.put(attribute.getKey(), row.getObject(attribute.getValue()));
                    }
                }
            }

            for (int i = 1; i < cols.length; i++) {
                String parent = row.getString(cols[i]);

                //generate parent id
                String parentId = ids[i].get(parent);
                if (parentId == null) {
                    parentId = idGen.id(row, i, cols[i]);
                    ids[i].put(parent, parentId);
                }

                pairs.put(childId, parentId);
                child = parent;
                childId = parentId;
            }
        }
    }

}
