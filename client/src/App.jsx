import { useState } from 'react'
import { useEffect, useRef} from "react";
import L from "leaflet";
import 'leaflet/dist/leaflet.css';
import './App.css'
import IDFChart from './components/IDFChart';

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


        // Kode til at vise Aarhus grænse
        /*fetch('/data/aarhus-boundary.geojson')
            .then((response) => response.json())
            .then((geojsonData) => {
                const outline = L.geoJSON(geojsonData, {
                    style: {
                        color: '#ff4d4d', // grænse-linje farve
                        weight: 1,        // linje tykkelse
                        fill: true, // område fyldt
                        fillColor: '#ff4d4d', // område farve
                        fillOpacity: 0.3, // hvor farvet: 0=fuldt transparent, 1=helt farvet

                    },
                }).addTo(map);

                map.fitBounds(outline.getBounds());
            })

            .catch((error) => {
                console.error('Error loading Aarhus boundary:', error);
            });
        */


        return() => map.remove();
        }, []);

    return (
        <div style={{ height: '100vh', width: '100%' }}>
            <div id="map" ref={mapEl}></div>
        </div>
    );
}
