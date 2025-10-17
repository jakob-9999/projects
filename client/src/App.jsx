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


        /* Kode til at vise Aarhus grænse
        fetch('/data/aarhus-boundary.geojson')
            .then((response) => response.json())
            .then((geojsonData) => {
                const outline = L.geoJSON(geojsonData, {
                    style: {
                        color: '#ff4d4d', // red outline color
                        weight: 1,        // line thickness
                        fill: false,      // no filled area
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
