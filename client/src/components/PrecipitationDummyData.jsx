import { Rectangle, Popup } from "react-leaflet";
import {useEffect} from "react";
import {useState} from "react";
import {geoJSON} from "leaflet";

// fetch grid cells
export default function PrecipitationLayer() {
    const [precipitationData, setPrecipitationData] = useState([])

    useEffect(() => {
        fetch("/api/dmi-dini-bbox/get-grid")
            .then((res) => {
                return res.json()
            })
            .then(data => {
                console.log(data)

                if (data.features.length > 0) {
                    // found the first time step
                    const firstStep = data.features[0].properties.step;

                    // filter only features with first step
                    const firstSTepFeatures = data.features.filter(f => f.properties.step === firstStep);
                    setPrecipitationData(firstSTepFeatures);
                } else {
                    setPrecipitationData([]);
                }
            })
            .catch((err) => console.error("Error fetching geoJSON:", err));
    }, []);

    const getColor = (precipitation) => {
        if (precipitation > 10) return "blue";
        if (precipitation > 5) return "dodgerblue";
        if (precipitation > 0.5) return "lightblue";
        return "transparent";
    };

    function kmToDegreesOffset(latDeg, km) {
        const latOffset = km / 111.32; // 1 degree of latitude is approximately 111.32 km
        const lonOffset = km / (111.32 * Math.cos(latDeg * Math.PI / 180));
        return { latOffset, lonOffset };
    }

    const cellSizeKm = 2; // each grid is 2*2 km

    return (
        <>
            {precipitationData.map((feature, i) => {
                // Extract coordinates (GeoJSON is [lon, lat])
                const [lon, lat] = feature.geometry.coordinates;
                const { latOffset, lonOffset } = kmToDegreesOffset(lat, cellSizeKm / 2);

                // Calculate grid corners
                const bounds = [
                    [lat - latOffset, lon - lonOffset],
                    [lat + latOffset, lon + lonOffset],
                ];

                // Extract precipitation value
                // ?? 0 is a safeguard in case the property is missing
                const value = feature.properties["total-precipitation"] ?? 0;
                const color = getColor(value);


                return (
                    <Rectangle
                        key={i}
                        bounds={bounds}
                        pathOptions={{
                            color: color,
                            fillColor: color,
                            fillOpacity: 0.3,
                        }}
                    >
                        <Popup>
                            <b>Rain:</b> {value} mm/h
                            <br />
                            <b>Time:</b> {feature.properties.step}
                        </Popup>
                    </Rectangle>
                );
            })}
        </>
    );
}
