import "leaflet/dist/leaflet.css";
import "./App.css";
import { MapContainer, TileLayer } from "react-leaflet";
import { useState } from "react";

import SewagelandLayer from "./components/Sewageland";
import PrecipitationLayer from "./components/ForecastGridCellsComponent.jsx";
import MapDraggingController from "./components/MapDraggingController";

// This is the root component of the map, it contains the map container and the layers
export default function MapRoot() {

    // This is needed so we know if the map should be draggable or not, when using the slider it should not be
    const [isDraggingEnabled, setIsDraggingEnabled] = useState(true);

    return (
        <div>
            <MapContainer
                dragging={isDraggingEnabled}
                center={[56.1629, 10.2039]}
                zoom={11}
                style={{
                    //zIndex determines which layer is on top (1 is beneath 2)
                    zIndex: 1,
                    height: "90vh",
                    width: "90vw",
                    borderRadius: "12px",
                    boxShadow: "0 0 20px rgba(0,0,0,0.4)",
                }}
            >
                <TileLayer
                    url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                />

                {/*Ensures the MapContainer prop dragging is set dynamically whenever the state of isDraggingEnabled changes*/}
                <MapDraggingController isDraggingEnabled={isDraggingEnabled}/>
                {/* Layer components */}
                <SewagelandLayer />

                {/*Passing props to ensure the map is not dragging when the slider is moved*/}
                <PrecipitationLayer
                    onSliderMouseDown={() => setIsDraggingEnabled(false)}
                    onSliderMouseUp={() => setIsDraggingEnabled(true)}/>
            </MapContainer>
        </div>
    );
}