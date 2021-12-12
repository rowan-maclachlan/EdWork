package org.edwork.goodcoffee.database.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.geojson.Point;

import java.util.Arrays;
import java.util.List;

public class LocationConverter implements DynamoDBTypeConverter<List<Double>, Point> {

    public LocationConverter() {}

    @Override
    public List<Double> convert(Point point) {
        return Arrays.asList(point.getCoordinates().getLongitude(), point.getCoordinates().getLatitude());
    }

    @Override
    public Point unconvert(List<Double> doubles) {
        return new Point(doubles.get(0), doubles.get(1));
    }
}
