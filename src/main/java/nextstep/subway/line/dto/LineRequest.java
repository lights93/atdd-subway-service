package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int additionalFee;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId,
        final int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId,
        final int distance, final int additionalFee) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.additionalFee = additionalFee;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public Line toLine() {
        return new Line(name, color);
    }
}
