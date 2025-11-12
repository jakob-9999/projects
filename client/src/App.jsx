import "leaflet/dist/leaflet.css";
import "./App.css";
import { MapContainer, TileLayer } from "react-leaflet";
import SewagelandLayer from "./components/Sewageland";
import PrecipitationLayer from "./components/PrecipitationDummyData";
import SliderComponent from "./components/SliderComponent.jsx";

// This is the root component of the map, it contains the map container and the layers
export default function MapRoot() {
    return (
        <div
            style={{
                height: "100vh",
                width: "100vw",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                background: "#1e1e1e",
            }}
        >
            <MapContainer
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

                {/* Layer components */}
                <SewagelandLayer />
                <PrecipitationLayer />
            </MapContainer>
            <div style={{ position: "absolute", zIndex: 2, bottom: 20, left: 100, width: "1000px" }}>
                <SliderComponent />
            </div>
        </div>
    );
}