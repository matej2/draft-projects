namespace HelloWorld.dto
{
    // Use the same naming convention as in GPX file
    record Location
    (
        double ele,
        DateTime time,
        String name,
        String desc,
        String sym,
        String type,
        double _lat,
        double _lon
    );
}
