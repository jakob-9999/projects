import {useEffect, useState} from "react";
import {GeoJSON, useMap} from "react-leaflet";
import {geoJSON} from "leaflet";

// Component to render the Sewageland layer.
// It fetches the GeoJSON data from the server and renders it on the map.
export default function SewagelandLayer() {
    const [data, setData] = useState(null);
    const map = useMap();

    useEffect(() => {
        fetch("/api/sewageland/features")
            .then((res) => res.json())
            .then((raw) => {
                setData(raw);
                const bounds = geoJSON(raw).getBounds();
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
                const type = feature.properties.vaerd1201a;
                const color = getColor(type);
                return {
                    fillColor: color, // Color of the polygon
                    fillOpacity: 0.4,
                    color: "#000", // Color of the border
                    weight: 0.5, // Thickness of the border
                };
            }}
            onEachFeature={(feature, layer) => {
                layer.bindPopup(`${feature.properties.vaerd1201a}`);
            }}
        />
    );
}
