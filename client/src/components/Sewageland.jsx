import {GeoJSON, useMap } from "react-leaflet";
import { useSewagelandData} from "../hooks/useSewagelandData.js";
import { geoJSON } from "leaflet";
import { getSewagelandColor } from "../utils/getSewagelandColor.js";

// Component to render the Sewageland layer.
// It fetches the GeoJSON data from the server and renders it on the map.
export default function SewagelandLayer() {
    const map = useMap();
    const data = useSewagelandData();

    if (!data) return null;

    const bounds = geoJSON(data).getBounds();
    map.fitBounds(bounds);

    return (
        <GeoJSON
            data={data}
            style={(feature) => {
                const type = feature.properties.vaerd1201a;
                const color = getSewagelandColor(type);
                return {
                    fillColor: color, // Color of the polygon
                    fillOpacity: 0.7,
                    color: "#000", // Color of the border
                    weight: 0.5, // Thickness of the border
                };
            }}
        />
    );
}
