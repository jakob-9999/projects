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
        //The hasFitRef remembers if we already zoomed the map to the sewer layer. If this is true, we stop here so the map does NOT jump back
        //when the layer is toggled on and off
        if (hasFitRef.current) return;

        const bounds = geoJSON(data).getBounds();
        if (bounds.isValid()) {
            map.fitBounds(bounds);
            //Here we mark that the zoom has happened and then remember that so we don't run it again
            hasFitRef.current = true;
        }
    }, [data, map, hasFitRef]);

    if (!data) return null;

    return (
        <GeoJSON
            data={data}
            pane={pane} //Controls which layer position this has compared to other layers
            style={(feature) => {
                const type = feature.properties.vaerd1201a;
                const color = getSewagelandColor(type);
                return {
                    fillColor: color, // Color of the polygon
                    fillOpacity: visible ? 0.7 : 0.0, //If visible is false, the layer is "hidden" but still mounted so it doesn't reset
                    color: "#000", // Color of the border
                    opacity: visible ? 1.0 : 0.0,
                    weight: 0.5 // Thickness of the border
                };
            }}
        />
    );
}
