import "leaflet/dist/leaflet.css";
import "./App.css";
import { MapContainer, TileLayer, Pane} from "react-leaflet";
import { useState, useRef } from "react";

import SewagelandLayer from "./components/Sewageland";
import PrecipitationLayer from "./components/PrecipitationLayer.jsx";
import MapDraggingController from "./components/MapDraggingController";
import VerticalToggleButtons from "./components/ToggleButton";
import LegendWrapper from "./components/LegendWrapper";

// This is the root component of the map, it contains the map container and the layers
export default function MapRoot() {
    const sewerHasFitRef = useRef(false);

    // This is needed so we know if the map should be draggable or not, when using the slider it should not be
    const [isDraggingEnabled, setIsDraggingEnabled] = useState(true);

    // This is needed so we can toggle the visibility of the layers
    const [showSewage, setShowSewage] = useState(true);
    const [showPrecipitation, setShowPrecipitation] = useState(true);

    return (
        <div>
            <MapContainer
                dragging={isDraggingEnabled}
                center={[56.1629, 10.2039]}
                zoom={11}
                style={{
                    //zIndex determines which layer is on top (1 is beneath 2)
                    //zIndex: 1,
                    height: "100vh",
                    width: "100vw",
                    boxShadow: "0 0 20px rgba(0,0,0,0.4)",
                }}
            >
                <TileLayer
                    url="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                />

                {/*Ensures the MapContainer attribute dragging is set dynamically whenever the state of isDraggingEnabled changes*/}
                <MapDraggingController isDraggingEnabled={isDraggingEnabled}/>

                {/* Layer components */}

                <LegendWrapper />


                <Pane name="sewerPane" style={{zIndex: 400}}/>
                <Pane name="precipitationPane" style={{zIndex: 500}}/>

                {/*Show/hide Sewageland*/}
                {showSewage && <SewagelandLayer pane = "sewerPane" visible={showSewage} hasFitRef={sewerHasFitRef} />}


                {/*Passing props to ensure the map is not dragging when the slider is moved*/}
                {/*Show/hide Precipitation*/}
                    {showPrecipitation && (
                        <PrecipitationLayer
                            pane = "precipitationPane"
                            onSliderMouseDown={() => setIsDraggingEnabled(false)}
                            onSliderMouseUp={() => setIsDraggingEnabled(true)}
                        />
                    )}


                {/*Passing control to toggle buttons*/}
                <VerticalToggleButtons
                    showSewage={showSewage}
                    setShowSewage={setShowSewage}
                    showPrecipitation={showPrecipitation}
                    setShowPrecipitation={setShowPrecipitation}
                />
            </MapContainer>
        </div>
    );
}