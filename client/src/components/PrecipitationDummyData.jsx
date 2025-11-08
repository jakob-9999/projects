import { CircleMarker, Popup } from "react-leaflet";
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
                setPrecipitationData(data.features)
            })
            .catch((err) => console.error("Error fetching geoJSON:", err));
    }, []);

    const getColor = (precipitation) => {
        if (precipitation > 10) return "blue";
        if (precipitation > 5) return "dodgerblue";
        if (precipitation > 0.5) return "lightblue";
        return "gray";
    };

    return (
        <>
            {precipitationData.map((data, i) => (
                <CircleMarker
                    key={i}
                    //order of coordinates is opposite of GeoJson-format (lat, long) because leaflet expects (long, lat)
                    center={[data.geometry.coordinates[1], data.geometry.coordinates[0]]}
                    radius={10}
                    pathOptions={{
                        color: "blue",
                        fillColor: "blue",
                        fillOpacity: 0.6,
                    }}
                >
                    {<Popup>
                        <b>Rain:</b> {data.properties["total-precipitation"]} mm/hr
                        <br />
                        <b>Time:</b> {data.properties.step}
                    </Popup>}
                </CircleMarker>
            ))}
        </>
    );
}
