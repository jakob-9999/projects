import {useEffect} from 'react';
import {useMap} from 'react-leaflet/hooks'

// Component to control whether the dragging of the map is enabled or disabled.
// It uses the useMap hook from react-leaflet to access the map instance
export default function MapDraggingController({ isDraggingEnabled }) {
    const map = useMap();

    useEffect(() => {
        if (isDraggingEnabled) {
            map.dragging.enable();
        } else {
            map.dragging.disable();
        }
    }, [isDraggingEnabled, map]); // Whenever the map or dragging state changes, this effect runs
};