import {GeoJSON, useMap } from "react-leaflet";
import {useEffect} from "react";
import { useSewagelandData} from "../hooks/useSewagelandData.js";
import { geoJSON } from "leaflet";
import { getSewagelandColor } from "../utils/getSewagelandColor.js";

// Component to render the Sewageland layer.
// It fetches the GeoJSON data from the server and renders it on the map.
export default function SewagelandLayer({pane, visible, hasFitRef}) {
    const map = useMap();
    const data = useSewagelandData();

    useEffect(() => {
        if (!data) return;
        if (hasFitRef.current) return;

        const bounds = geoJSON(data).getBounds();
        if (bounds.isValid()) {
            map.fitBounds(bounds);
            hasFitRef.current = true;
        }
    }, [data, map, hasFitRef]);

    if (!data) return null;

    return (
        <GeoJSON
            data={data}
            pane={pane}
            style={(feature) => {
                const type = feature.properties.vaerd1201a;
                const color = getSewagelandColor(type);
                return {
                    fillColor: color,
                    fillOpacity: visible ? 0.7 : 0.0,
                    color: "#000",
                    opacity: visible ? 1.0 : 0.0,
                    weight: 0.5
                    /*fillColor: color, // Color of the polygon
                    fillOpacity: 0.5,
                    color: "#000", // Color of the border
                    weight: 0.5, // Thickness of the border*/
                };
            }}
        />
    );
}
