import { useEffect } from "react";
import L from "leaflet";
import "./legend.css";


// NEW: Static legend groups for sewage + precipitation
export default function LeafletLegend({ map }) {
    useEffect(() => {
        if (!map) return;


        const legend = L.control({ position: "bottomright" });


        legend.onAdd = function () {
            const div = L.DomUtil.create("div", "info legend");


            // Sewage categories (top)
            const sewage = [
                { label: "Fælleskloakeret", color: "#4A1F00" },
                { label: "Separatkloakeret", color: "#B84D00" },
                { label: "Spildevandskloakeret", color: "#FF7F32" },
                { label: "Ukloakeret", color: "#B0B0B0" }
            ];


            // Precipitation categories (bottom)
            const rain = [
                { label: "0.5–5 mm", color: "lightblue" },
                { label: "5–10 mm", color: "dodgerblue" },
                { label: "10+ mm", color: "blue" }
            ];


            // Build sewage section
            div.innerHTML += `<strong>Kloak typer</strong><br>`;
            sewage.forEach(item => {
                div.innerHTML += `<i style=\"background:${item.color}\"></i>${item.label}<br>`;
            });


            // Spacer
            div.innerHTML += `<div style='height:10px;'></div>`;


            // Build rain section
            div.innerHTML += `<strong>Regn mængde</strong><br>`;
            rain.forEach(item => {
                div.innerHTML += `<i style=\"background:${item.color}\"></i>${item.label}<br>`;
            });


            return div;
        };


        legend.addTo(map);
        return () => legend.remove();
    }, [map]);


    return null;
}