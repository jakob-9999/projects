import { useState } from 'react'
import { useEffect, useRef} from "react";
import L from "leaflet";
import 'leaflet/dist/leaflet.css';
import './App.css'
import IDFChart from './components/IDFChart';
import proj4 from "proj4"; //JavaScript library that converts a coordinates system to another, npm install proj4

// Define the coordinate system used in your data (UTM zone 32N / ETRS89)
proj4.defs("EPSG:25832", "+proj=utm +zone=32 +ellps=GRS80 +units=m +no_defs");

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <IDFChart/>
    </>
  )
}

export default function AppMap(){
    const mapEl = useRef(null);

    useEffect(() =>{
        const map = L.map('map').setView([56.1629, 10.2039], 11);

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        }).addTo(map);


        // code for visualizing Aarhus' boundary
        /*fetch('/data/aarhus-boundary.geojson')
            .then((response) => response.json())
            .then((geojsonData) => {
                const outline = L.geoJSON(geojsonData, {
                    style: {
                        color: '#ff4d4d', // The outline color (red)
                        weight: 1,        // The lines thickness
                        fill: true, // Is the polygon filled or not (true/false)
                        fillColor: '#ff4d4d', // If true then this color (red)
                        fillOpacity: 0.3, //Level of transparency: 0=transparent, 1=Solid color

                    },
                }).addTo(map);

                map.fitBounds(outline.getBounds());
            })

            .catch((error) => {
                console.error('Error loading Aarhus boundary:', error);
            });*/


        //Fetches our backend data thorugh Spring boot API, There's a proxy in vite.config.js
        fetch('/api/kloakopland')
            .then((res) => res.json())
            .then((data) => {
                console.log("Raw data:", data);

                // converts coordinates to WGS84
                const converted = {
                    ...data,  // ' ...' is a spread operator
                    features: data.features.map((f) => ({
                        ...f,
                        geometry: {
                            ...f.geometry,
                            coordinates: f.geometry.coordinates.map((polygon) =>
                                polygon.map((ring) =>
                                    ring.map(([x, y]) => {
                                        const [lon, lat] = proj4("EPSG:25832", "WGS84", [x, y]);
                                        return [lon, lat];
                                    })
                                )
                            ),
                        },
                    })),
                };

                //Adds the convertet coordinates to the map with the according colors to the sewer type
                const layer = L.geoJSON(converted, {
                    style: (feature) => {
                        const type = feature.properties?.vaerd1201a || "";
                        let color = "#888"; // Default color set to grey

                        if (type.includes("Spildevandskloakeret")) {
                            color = "red";
                        } else if (type.includes("samme")) { //Fælleskolakeret
                            color = "yellow";
                        }
                        else if (type.includes("Separatkloakeret")){
                            color = 'blue';
                        }

                        return {
                            color: color,
                            fillColor: color,
                            weight: 2,
                            fillOpacity: 0.4,
                        };
                    },
                    onEachFeature: (feature, layer) => {
                        // Use a readable property for popup
                        //const name = feature.properties?.temanavn || "Ukendt område";
                        layer.bindPopup(`${feature.properties.vaerd1201a}`);
                    },
                }).addTo(map);

                map.fitBounds(layer.getBounds());
            })
            .catch((err) => console.error("Error fetching:", err));

        return() => map.remove();
        }, []);

    return (
        <div style={{ height: '100vh', width: '100%' }}>
            <div id="map" ref={mapEl}></div>
        </div>
    );
}
