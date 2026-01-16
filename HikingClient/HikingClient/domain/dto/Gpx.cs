namespace HelloWorld.dto
{
    // Use the same naming convention as in GPX file
    record Gpx(
        DateTime date,
        Bounds bounds,
        List<Location> wpt,
        List<Rte> rte
    );
}
