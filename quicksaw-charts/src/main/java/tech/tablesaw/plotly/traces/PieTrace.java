package tech.tablesaw.plotly.traces;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Map;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.plotly.Utils;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.components.Domain;

public class PieTrace extends AbstractTrace {

  private final double[] values;
  private final Object[] labels;
  private final Marker marker;
  private final Domain domain;
  private final String textinfo;

  public enum TextInfo {
    NONE("none"),
    LABEL("label"),
    TEXT("text"),
    VALUE("value"),
    PERCENT("percent");

    private final String value;

    TextInfo(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  private PieTrace(PieBuilder builder) {
    super(builder);
    this.values = builder.values;
    this.labels = builder.labels;
    this.marker = builder.marker;
    this.domain = builder.domain;
    this.textinfo = builder.textinfo;
  }

  @Override
  public String asJavascript(int i) {
    Writer writer = new StringWriter();
    PebbleTemplate compiledTemplate;

    try {
      compiledTemplate = engine.getTemplate("pie_trace_template.html");
      compiledTemplate.evaluate(writer, getContext(i));
    } catch (PebbleException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return writer.toString();
  }

  private Map<String, Object> getContext(int i) {

    Map<String, Object> context = super.getContext();
    context.put("variableName", "trace" + i);
    context.put("values", Utils.dataAsString(values));
    if (labels != null) {
      context.put("labels", Utils.dataAsString(labels));
    }
    if (marker != null) {
      context.put("marker", marker);
    }
    if (domain != null) {
      context.put("domain", domain.asJavascript());
    }
    if (textinfo != null) {
      context.put("textinfo", textinfo);
    }
    return context;
  }

  public static PieBuilder builder(Object[] labels, double[] values) {
    return new PieBuilder(labels, values);
  }

  public static PieBuilder builder(Column<?> labels, NumericColumn<? extends Number> values) {
    return new PieBuilder(TraceBuilder.columnToStringArray(labels), values.asDoubleArray());
  }

  public static class PieBuilder extends TraceBuilder {

    private final String type = "pie";
    private final double[] values;
    private final Object[] labels;
    private Marker marker;
    private Domain domain;
    private String textinfo;

    private PieBuilder(Object[] labels, double[] values) {
      this.labels = labels;
      this.values = values;
    }

    public PieBuilder addTextInfo(TextInfo info) {
      if(info == TextInfo.NONE) {
        this.textinfo = info.toString();
      } else if(this.textinfo == null) {
        this.textinfo = info.toString();
      } else {
        this.textinfo = this.textinfo + "+" + info.toString();
      }
      return this;
    }

    public PieBuilder domain(Domain domain) {
      this.domain = domain;
      return this;
    }

    public PieTrace build() {
      return new PieTrace(this);
    }

    @Override
    protected String getType() {
      return type;
    }

    @Override
    public PieTrace.PieBuilder showLegend(boolean b) {
      super.showLegend(b);
      return this;
    }

    public PieTrace.PieBuilder marker(Marker marker) {
      this.marker = marker;
      return this;
    }

  }
}
