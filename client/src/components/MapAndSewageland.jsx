import { useEffect, useState } from "react";
import { GeoJSON, useMap } from "react-leaflet";
import { geoJSON } from "leaflet";
import proj4 from "proj4";

proj4.defs("EPSG:25832", "+proj=utm +zone=32 +ellps=GRS80 +units=m +no_defs");

const convertToWGS84 = (geojson) => ({
    ...geojson,
    features: geojson.features.map((f) => ({
        ...f,
        geometry: {
            ...f.geometry,
            coordinates: f.geometry.coordinates.map((polygon) =>
                polygon.map((ring) =>
                    ring.map(([x, y]) => {
                        const [lon, lat] = proj4("EPSG:25832", "WGS84", [x, y]);
                        return [lon, lat];
                    })
                )
            ),
        },
    })),
});

export default function SewagelandLayer() {
    const [data, setData] = useState(null);
    const map = useMap();

    useEffect(() => {
        fetch("/api/sewageland")
            .then((res) => res.json())
            .then((raw) => {
                const converted = convertToWGS84(raw);
                setData(converted);
                const bounds = geoJSON(converted).getBounds();
                map.fitBounds(bounds);
            })
            .catch((err) => console.error("Error fetching geoJSON:", err));
    }, [map]);

    const getColor = (type) => {
        if (type?.includes("Spildevandskloakeret")) return "red";
        if (type?.includes("samme")) return "yellow";
        if (type?.includes("Separatkloakeret")) return "green";
        return "#888";
    };

    if (!data) return null;

    return (
        <GeoJSON
            data={data}
            style={(feature) => {
                const type = feature.properties?.vaerd1201a;
                const color = getColor(type);
                return {
                    color,
                    fillColor: color,
                    weight: 2,
                    fillOpacity: 0.4,
                };
            }}
            onEachFeature={(feature, layer) => {
                layer.bindPopup(`${feature.properties.vaerd1201a}`);
            }}
        />
    );
}
