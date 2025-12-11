import { useMap } from "react-leaflet";
import LeafletLegend from "./LeafletLegend";


export default function LegendWrapper({ getColor }) {
    const map = useMap();
    return <LeafletLegend map={map} getColor={getColor} />;
}