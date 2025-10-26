import { CircleMarker, Popup } from "react-leaflet";

export default function PrecipitationLayer() {
    const precipitationData = [
        { lat: 56.16, lon: 10.20, intensity: 0, time: "2025-10-24 12:00" },
        { lat: 56.18, lon: 10.22, intensity: 3, time: "2025-10-24 12:00" },
        { lat: 56.14, lon: 10.19, intensity: 8, time: "2025-10-24 12:00" },
        { lat: 56.20, lon: 10.25, intensity: 12, time: "2025-10-24 12:00" },
        { lat: 56.20, lon: 10.11, intensity: 15, time: "2025-10-24 12:00" },
        { lat: 56.17, lon: 10.23, intensity: 20, time: "2025-10-24 12:00" },
        { lat: 56.13, lon: 10.15, intensity: 25, time: "2025-10-24 12:00" },
        { lat: 56.19, lon: 10.13, intensity: 30, time: "2025-10-24 12:00" },
        { lat: 56.14, lon: 10.20, intensity: 35, time: "2025-10-24 12:00" },
        { lat: 56.20, lon: 10.10, intensity: 40, time: "2025-10-24 12:00" },
        { lat: 56.15, lon: 10.21, intensity: 45, time: "2025-10-24 12:00" },
        { lat: 56.17, lon: 10.07, intensity: 6, time: "2025-10-24 12:00" },
        { lat: 56.13, lon: 10.12, intensity: 10, time: "2025-10-24 12:00" },
        { lat: 56.16, lon: 10.20, intensity: 15, time: "2025-10-24 12:00" },
        { lat: 56.15, lon: 10.20, intensity: 20, time: "2025-10-24 12:00" },
        { lat: 56.15, lon: 10.21, intensity: 25, time: "2025-10-24 12:00" },
        { lat: 56.16, lon: 10.19, intensity: 30, time: "2025-10-24 12:00" },
        { lat: 56.17, lon: 10.20, intensity: 35, time: "2025-10-24 12:00" },
    ];

    const getColor = (intensity) => {
        if (intensity > 10) return "blue";
        if (intensity > 5) return "dodgerblue";
        if (intensity > 0.5) return "lightblue";
        return "gray";
    };

    return (
        <>
            {precipitationData.map((point, i) => (
                <CircleMarker
                    key={i}
                    center={[point.lat, point.lon]}
                    radius={10}
                    pathOptions={{
                        color: getColor(point.intensity),
                        fillColor: getColor(point.intensity),
                        fillOpacity: 0.6,
                    }}
                >
                    <Popup>
                        <b>Rain:</b> {point.intensity} mm/hr
                        <br />
                        <b>Time:</b> {point.time}
                    </Popup>
                </CircleMarker>
            ))}
        </>
    );
}
