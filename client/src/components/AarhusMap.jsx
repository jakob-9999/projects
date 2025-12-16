import { useEffect, useMemo, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, useMap } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

function FitToData({ data }) {
    const map = useMap();
    useEffect(() => {
        if (!data) return;
        try {
            const layer = new L.GeoJSON(data);
            const b = layer.getBounds();
            if (b.isValid()) map.fitBounds(b, { padding: [20, 20] });
        } catch (e) {
            map.setView([56.1629, 10.2039], 11);
        }
    }, [data, map]);
    return null;
}

export default function AarhusMap() {
    const [layer1, setLayer1] = useState(null);
    const [layer2, setLayer2] = useState(null);

    useEffect(() => {
        fetch("/package.json").then(r => r.ok ? r.json() : null).then(setLayer1).catch(() => {});
        fetch("/package-lock.json").then(r => r.ok ? r.json() : null).then(setLayer2).catch(() => {});
    }, []);

    const style1 = useMemo(() => ({ color: "#e74c3c", weight: 2, fillOpacity: 0.15 }), []);
    const style2 = useMemo(() => ({ color: "#2ecc71", weight: 2, fillOpacity: 0.15 }), []);

    return (
        <MapContainer
            center={[56.1629, 10.2039]}
            zoom={11}
            style={{ height: "100vh", width: "100%" }}
        >
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="&copy; OpenStreetMap contributors"
            />
            {layer1 && <GeoJSON data={layer1} style={style1} />}
            {layer2 && <GeoJSON data={layer2} style={style2} />}
            <FitToData data={layer1 || layer2} />
        </MapContainer>
    );
}
