import proj4 from "proj4";

proj4.defs("EPSG:25832", "+proj=utm +zone=32 +ellps=GRS80 +units=m +no_defs");

// Recursively iterates through the multipolygon arrays until it reaches the last (x,y) coordinates which it converts to WGS84
function reprojectCoordinates(coordinates, sourceCRS, targetCRS) {
    if (Array.isArray(coordinates) && typeof coordinates[0] === 'number' && typeof coordinates[1] === 'number') {
        const [lon, lat] = proj4(sourceCRS, targetCRS, coordinates);
        return [lon, lat];
    }
    return coordinates.map((coordinate) => reprojectCoordinates(coordinate, sourceCRS, targetCRS));
}

// Convert the api response to a FeatureCollection that leaaflet can work with.
export function sewerDataToWgs84FeatureCollection(sewerData) {
    return {
        type: "FeatureCollection",
        features: sewerData.map((sewerData) => {
            const coordinates = JSON.parse(sewerData.coordinates); // Parses the String into JSON to work with
            const properties = sewerData.sewerType;

            return {
                type: "Feature",
                geometry: {
                    type: "MultiPolygon",
                    coordinates: reprojectCoordinates(coordinates, 'EPSG:25832', 'WGS84')
                },
                properties
            }
        })
    }
}